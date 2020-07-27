package cl.bgmp.pgmapi.listeners;

import cl.bgmp.pgmapi.MySQLConnection;
import cl.bgmp.pgmapi.PGMAPI;
import cl.bgmp.pgmapi.StatsManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerEvents implements Listener {
  private StatsManager statsManager = PGMAPI.get().getStatsManager();
  private MySQLConnection database = PGMAPI.get().getMySQLDatabase();

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    final Player player = event.getPlayer();
    final String uuid = database.getPlayerUUID(player);
    final String nick = player.getName();

    database.registerPlayer(uuid, nick);
    statsManager.loadPlayer(database.getPGMPlayer(uuid, nick));
  }

  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent event) {
    statsManager.unloadPlayer(event.getPlayer().getName());
  }
}
