package cl.bgmp.pgmapi.listeners;

import cl.bgmp.pgmapi.MySQLConnection;
import cl.bgmp.pgmapi.PGMAPI;
import cl.bgmp.pgmapi.StatsManager;
import java.util.Objects;
import java.util.Optional;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import tc.oc.pgm.api.party.Competitor;
import tc.oc.pgm.api.player.MatchPlayer;
import tc.oc.pgm.api.player.MatchPlayerState;
import tc.oc.pgm.api.player.ParticipantState;
import tc.oc.pgm.api.player.PlayerRelation;
import tc.oc.pgm.api.player.event.MatchPlayerDeathEvent;
import tc.oc.pgm.core.CoreBlockBreakEvent;
import tc.oc.pgm.destroyable.DestroyableHealthChange;
import tc.oc.pgm.destroyable.DestroyableHealthChangeEvent;
import tc.oc.pgm.wool.PlayerWoolPlaceEvent;

public class PlayerEvents implements Listener {

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    final MySQLConnection database = PGMAPI.get().getMySQLDatabase();
    final StatsManager statsManager = PGMAPI.get().getStatsManager();

    final Player player = event.getPlayer();
    final String uuid = database.getPlayerUUID(player);
    final String nick = player.getName();

    database.registerPlayer(uuid, nick);
    statsManager.loadPlayer(database.getPGMPlayer(uuid, nick));
  }

  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent event) {
    PGMAPI.get().getStatsManager().unloadPlayer(event.getPlayer().getName());
  }

  @EventHandler
  public void onPlayerDeath(MatchPlayerDeathEvent event) {
    final StatsManager statsManager = PGMAPI.get().getStatsManager();
    MatchPlayer victim = event.getVictim();
    MatchPlayer murderer = null;

    statsManager.addDeathToPlayer(victim.getBukkit().getName());

    if (event.getKiller() != null)
      murderer = event.getKiller().getParty().getPlayer(event.getKiller().getId());
    if (murderer == null) return;

    final PlayerRelation playerRelation =
        PlayerRelation.get(Objects.requireNonNull(victim.getParticipantState()), murderer);
    if (playerRelation == PlayerRelation.ALLY || playerRelation == PlayerRelation.SELF) return;

    statsManager.addKillToPlayer(murderer.getBukkit().getName());
    statsManager.addKilledToPlayer(victim.getBukkit().getName());
  }

  @EventHandler
  public void onPlayerWoolPlace(PlayerWoolPlaceEvent event) {
    final StatsManager statsManager = PGMAPI.get().getStatsManager();
    final ParticipantState participantState = event.getPlayer();
    final Competitor competitor = participantState.getParty();
    final MatchPlayer matchPlayer = competitor.getPlayer(participantState.getId());

    if (matchPlayer != null) statsManager.addWoolToPlayer(matchPlayer.getBukkit().getName());
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onPlayerMonumentTouch(DestroyableHealthChangeEvent event) {
    final StatsManager statsManager = PGMAPI.get().getStatsManager();
    final DestroyableHealthChange change = event.getChange();

    final ParticipantState participantState = change.getPlayerCause();
    if (participantState == null) return;

    final Competitor competitor = participantState.getParty();
    final MatchPlayer matchPlayer = competitor.getPlayer(participantState.getId());

    if (matchPlayer != null && !event.getDestroyable().hasTouched(participantState)) {
      statsManager.addMonumentToPlayer(matchPlayer.getBukkit().getName());
    }
  }

  @EventHandler
  public void onPlayerCoreTouch(CoreBlockBreakEvent event) {
    final StatsManager statsManager = PGMAPI.get().getStatsManager();
    final MatchPlayerState touchingMatchPlayerState = event.getPlayer();

    final Optional<MatchPlayer> matchPlayer = touchingMatchPlayerState.getPlayer();
    if (matchPlayer.isPresent()) {
      if (!event.getCore().hasTouched(matchPlayer.get().getParticipantState())) {
        statsManager.addCoreToPlayer(matchPlayer.get().getBukkit().getName());
      }
    }
  }
}
