package org.drnull.murdermystery.CustomEvents;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.drnull.murdermystery.Round.Round;

import java.util.List;

public class RoundStartEvent extends Event {
    public List<Player> getRemainingPlayers() {
        return remainingPlayers;
    }

    Round round;
    List<Player> remainingPlayers;
    public Player murderer;
    Player spy;



    public RoundStartEvent(Round round, List<Player> remainingPlayers)
    {
        this.remainingPlayers = remainingPlayers;
        this.round = round;
        murderer = round.getMurderer();
        spy = round.getSpy();
    }

    public Round getRound() {
        return round;
    }

    public Player getMurderer() {
        return murderer;
    }

    public void setMurderer(Player murder) {
        this.murderer = murder;
    }

    public Player getSpy() {
        return spy;
    }

    public void setSpy(Player spy) {
        this.spy = spy;
    }

    private static final HandlerList HANDLERS = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
