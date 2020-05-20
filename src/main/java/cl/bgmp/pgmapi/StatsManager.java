package cl.bgmp.pgmapi;

import java.util.HashSet;
import java.util.Set;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

public class StatsManager {
  private Set<PGMPlayer> pgmPlayers = new HashSet<>();
  private MySQLConnection database;

  public StatsManager(MySQLConnection database) {
    this.database = database;
    new BukkitRunnable() {
      @Override
      public void run() {
        deployPGMPlayerStatistics();
      }
    }.runTaskTimerAsynchronously(PGMAPI.get(), 600L, 600L); // Deploys every 30 seconds
  }

  public void deployPGMPlayerStatistics() {
    pgmPlayers.forEach(database::deployPGMPlayerStats);
  }

  public void deployPGMPlayerStatistics(final PGMPlayer pgmPlayer) {
    database.deployPGMPlayerStats(pgmPlayer);
  }

  public void loadPlayer(PGMPlayer pgmPlayer) {
    pgmPlayers.add(pgmPlayer);
  }

  /**
   * Unloads the player and performs a manual deploy of their stats to avoid clashes between this
   * instance of deployment and the main mass-deploy task
   *
   * @param nick Nick of the player whom's in-memory statistics will be unloaded
   */
  public void unloadPlayer(String nick) {
    final PGMPlayer toUnload = getPlayerByNick(nick);
    if (toUnload != null) {
      pgmPlayers.remove(toUnload);
      deployPGMPlayerStatistics(toUnload);
    }
  }

  public void addKillToPlayer(String nick) {
    final PGMPlayer pgmPlayer = getPlayerByNick(nick);
    if (pgmPlayer != null) pgmPlayer.addKill();
  }

  public void addDeathToPlayer(String nick) {
    final PGMPlayer pgmPlayer = getPlayerByNick(nick);
    if (pgmPlayer != null) pgmPlayer.addDeath();
  }

  public void addKilledToPlayer(String nick) {
    final PGMPlayer pgmPlayer = getPlayerByNick(nick);
    if (pgmPlayer != null) pgmPlayer.addKilled();
  }

  public String buildPrettyStatsMessage(String nick) {
    final PGMPlayer pgmPlayer = getPlayerByNick(nick);
    if (pgmPlayer == null)
      return ChatColor.DARK_AQUA + nick + ChatColor.RED + " is invalid or currently offline.";

    return ChatColor.DARK_PURPLE.toString()
        + ChatColor.BOLD
        + ChatColor.STRIKETHROUGH
        + "--------------"
        + ChatColor.RED
        + " "
        + nick
        + " "
        + ChatColor.DARK_PURPLE.toString()
        + ChatColor.BOLD
        + ChatColor.STRIKETHROUGH
        + "--------------"
        + "\n"
        + ChatColor.GOLD
        + "Kills: "
        + ChatColor.GREEN
        + pgmPlayer.getKills()
        + "\n"
        + ChatColor.GOLD
        + "Deaths: "
        + ChatColor.GREEN
        + pgmPlayer.getDeaths()
        + "\n"
        + ChatColor.GOLD
        + "KD: "
        + ChatColor.GREEN
        + pgmPlayer.getKd()
        + "\n"
        + ChatColor.GOLD
        + "KK: "
        + ChatColor.GREEN
        + pgmPlayer.getKk()
        + "\n"
        + ChatColor.DARK_PURPLE.toString()
        + ChatColor.BOLD
        + ChatColor.STRIKETHROUGH
        + "---------------"
        + nick.replaceAll("[a-zA-Z0-9]", "-")
        + "--------------";
  }

  public PGMPlayer getPlayerByNick(String nick) {
    return pgmPlayers.stream()
        .filter(pgmPlayer -> pgmPlayer.getNick().equals(nick))
        .findFirst()
        .orElse(null);
  }
}
