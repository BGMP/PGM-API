package cl.bgmp.pgmapi;

import java.util.HashSet;
import java.util.Set;
import net.md_5.bungee.api.ChatColor;
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
   * Removes the given player from the {@link StatsManager}'s list and performs a manual deploy of
   * their stats to avoid clashes between this instance of deployment and the main mass-deploy task
   *
   * @param nick Nick of the player whose in-memory statistics will be unloaded
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

  public void addWoolToPlayer(String nick) {
    final PGMPlayer pgmPlayer = getPlayerByNick(nick);
    if (pgmPlayer != null) pgmPlayer.addWool();
  }

  public void addMonumentToPlayer(String nick) {
    final PGMPlayer pgmPlayer = getPlayerByNick(nick);
    if (pgmPlayer != null) pgmPlayer.addMonument();
  }

  public void addCoreToPlayer(String nick) {
    final PGMPlayer pgmPlayer = getPlayerByNick(nick);
    if (pgmPlayer != null) pgmPlayer.addCore();
  }

  public PGMPlayer getPlayerByNick(String nick) {
    return pgmPlayers.stream()
        .filter(pgmPlayer -> pgmPlayer.getNick().equals(nick))
        .findFirst()
        .orElse(null);
  }

  public String buildPrettyStatsMessage(String nick) {
    final PGMPlayer pgmPlayer = getPlayerByNick(nick);
    if (pgmPlayer == null)
      return ChatColor.DARK_AQUA
          + nick
          + ChatColor.RED
          + " is currently offline. Do not worry, though,  we will soon allow you to check on offline players's stats!";

    return ChatColor.GOLD
        + "Kills: "
        + ChatColor.AQUA
        + pgmPlayer.getKills()
        + "\n"
        + ChatColor.GOLD
        + "Deaths: "
        + ChatColor.AQUA
        + pgmPlayer.getDeaths()
        + "\n"
        + ChatColor.GOLD
        + "KD: "
        + ChatColor.AQUA
        + String.format("%.2f", pgmPlayer.getKd())
        + "\n"
        + ChatColor.GOLD
        + "KK: "
        + ChatColor.AQUA
        + String.format("%.2f", pgmPlayer.getKk())
        + "\n"
        + ChatColor.GOLD
        + "Wools: "
        + ChatColor.AQUA
        + pgmPlayer.getWools()
        + "\n"
        + ChatColor.GOLD
        + "Monuments: "
        + ChatColor.AQUA
        + pgmPlayer.getMonuments()
        + "\n"
        + ChatColor.GOLD
        + "Cores: "
        + ChatColor.AQUA
        + pgmPlayer.getCores();
  }
}
