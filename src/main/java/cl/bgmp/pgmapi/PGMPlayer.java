package cl.bgmp.pgmapi;

public class PGMPlayer {
  private String uuid;
  private String nick;
  private int kills;
  private int deaths;
  private int killed;
  private double kd;
  private double kk;
  private int wools;
  private int monuments;
  private int cores;

  public PGMPlayer(
      String uuid,
      String nick,
      int kills,
      int deaths,
      int killed,
      double kd,
      double kk,
      int wools,
      int monuments,
      int cores) {
    this.uuid = uuid;
    this.nick = nick;
    this.kills = kills;
    this.deaths = deaths;
    this.killed = killed;
    this.kd = kd;
    this.kk = kk;
    this.wools = wools;
    this.monuments = monuments;
    this.cores = cores;

    touchKdAndKk();
  }

  public String getUUID() {
    return uuid;
  }

  public String getNick() {
    return nick;
  }

  public int getKills() {
    return kills;
  }

  public int getDeaths() {
    return deaths;
  }

  public int getKilled() {
    return killed;
  }

  public int getWools() {
    return wools;
  }

  public int getMonuments() {
    return monuments;
  }

  public int getCores() {
    return cores;
  }

  public double getKd() {
    touchKd();
    return kd;
  }

  public double getKk() {
    touchKk();
    return kk;
  }

  public void touchKdAndKk() {
    touchKd();
    touchKk();
  }

  public void touchKd() {
    if (deaths == 0) this.kd = kills;
    else this.kd = kills / (double) deaths;
  }

  public void touchKk() {
    if (killed == 0) this.kk = kills;
    else this.kk = kills / (double) killed;
  }

  public void addKill() {
    this.kills++;
    touchKdAndKk();
  }

  public void addDeath() {
    this.deaths++;
    touchKdAndKk();
  }

  public void addKilled() {
    this.killed++;
    touchKdAndKk();
  }

  public void addWool() {
    this.wools++;
  }

  public void addMonument() {
    this.monuments++;
  }

  public void addCore() {
    this.cores++;
  }
}
