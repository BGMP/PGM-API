package cl.bgmp.pgmapi.listeners;

import cl.bgmp.pgmapi.PGMAPI;
import cl.bgmp.pgmapi.StatsManager;
import java.util.Objects;
import java.util.Optional;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import tc.oc.pgm.api.match.MatchScope;
import tc.oc.pgm.api.party.Competitor;
import tc.oc.pgm.api.player.MatchPlayer;
import tc.oc.pgm.api.player.MatchPlayerState;
import tc.oc.pgm.api.player.ParticipantState;
import tc.oc.pgm.api.player.PlayerRelation;
import tc.oc.pgm.api.player.event.MatchPlayerDeathEvent;
import tc.oc.pgm.core.CoreBlockBreakEvent;
import tc.oc.pgm.destroyable.DestroyableHealthChange;
import tc.oc.pgm.destroyable.DestroyableHealthChangeEvent;
import tc.oc.pgm.events.ListenerScope;
import tc.oc.pgm.wool.PlayerWoolPlaceEvent;

@ListenerScope(MatchScope.RUNNING)
public class StatsEvents implements Listener {
  private StatsManager statsManager = PGMAPI.get().getStatsManager();

  @EventHandler
  public void onPlayerDeath(MatchPlayerDeathEvent event) {
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
    final ParticipantState participantState = event.getPlayer();
    final Competitor competitor = participantState.getParty();
    final MatchPlayer matchPlayer = competitor.getPlayer(participantState.getId());

    if (matchPlayer != null) statsManager.addWoolToPlayer(matchPlayer.getBukkit().getName());
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onPlayerMonumentTouch(DestroyableHealthChangeEvent event) {
    final DestroyableHealthChange change = event.getChange();
    if (change == null) return;

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
    final MatchPlayerState touchingMatchPlayerState = event.getPlayer();

    final Optional<MatchPlayer> matchPlayer = touchingMatchPlayerState.getPlayer();
    if (matchPlayer.isPresent()) {
      if (!event.getCore().hasTouched(matchPlayer.get().getParticipantState())) {
        statsManager.addCoreToPlayer(matchPlayer.get().getBukkit().getName());
      }
    }
  }
}
