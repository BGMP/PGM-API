package cl.bgmp.pgmapi;

public class PGMPlayer {
  private String uuid;
  private String nick;
  private int kills;
  private int deaths;
  private int killed;
  private double kd;
  private double kk;

  public PGMPlayer(String uuid, String nick, int kills, int deaths, int killed) {
    this.uuid = uuid;
    this.nick = nick;
    this.kills = kills;
    this.deaths = deaths;
    this.killed = killed;

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

  public double getKd() {
    return kd;
  }

  public double getKk() {
    return kk;
  }

  public void touchKdAndKk() {
    if (deaths == 0) this.kd = kills;
    else this.kd = kills / (double) deaths;

    if (killed == 0) this.kk = kills;
    else this.kk = kills / (double) killed;
  }

  public void addKill() {
    this.kills = kills + 1;
    touchKdAndKk();
  }

  public void addDeath() {
    this.deaths = deaths + 1;
    touchKdAndKk();
  }

  public void addKilled() {
    this.killed = killed + 1;
    touchKdAndKk();
  }
}
