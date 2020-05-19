package cl.bgmp.apipgm;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;

public final class Config {

  private static Configuration getConfig() {
    PGMAPI apipgm = PGMAPI.get();
    if (apipgm != null) return apipgm.getConfig();
    else return new YamlConfiguration();
  }

  public static class MySQL {

    public static String getHost() {
      return getConfig().getString("mysql.host");
    }

    public static String getDatabase() {
      return getConfig().getString("mysql.database");
    }

    public static String getUsername() {
      return getConfig().getString("mysql.username");
    }

    public static String getPassword() {
      return getConfig().getString("mysql.password");
    }

    public static int getPort() {
      return getConfig().getInt("mysql.port");
    }

    public static String getUsersTable() {
      return getConfig().getString("mysql.tables.users");
    }

    public static String getPGMTable() {
      return getConfig().getString("mysql.tables.pgm");
    }
  }
}
