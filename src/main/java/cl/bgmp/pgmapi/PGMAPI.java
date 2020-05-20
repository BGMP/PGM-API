package cl.bgmp.pgmapi;

import cl.bgmp.pgmapi.commands.StatsCommand;
import cl.bgmp.pgmapi.listeners.PlayerEvents;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class PGMAPI extends JavaPlugin {
  private static PGMAPI apipgm;
  private MySQLConnection database;
  private StatsManager statsManager;

  public static PGMAPI get() {
    return apipgm;
  }

  public MySQLConnection getMySQLDatabase() {
    return database;
  }

  public StatsManager getStatsManager() {
    return statsManager;
  }

  @Override
  public void onEnable() {
    apipgm = this;
    loadConfiguration();

    database = new MySQLConnection(this.getLogger());
    statsManager = new StatsManager(database);

    registerCommands();
    registerEvents(new PlayerEvents());
  }

  @Override
  public void onDisable() {
    statsManager.deployPGMPlayerStatistics();
  }

  private void registerCommands() {
    final PluginCommand statsCommand = getCommand("stats");

    assert statsCommand != null;
    statsCommand.setExecutor(new StatsCommand());
  }

  private void registerEvents(Listener... listeners) {
    final PluginManager pluginManager = Bukkit.getPluginManager();
    for (Listener listener : listeners) pluginManager.registerEvents(listener, this);
  }

  private void loadConfiguration() {
    getConfig().options().copyDefaults(true);
    saveConfig();
  }
}
