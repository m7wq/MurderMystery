package org.drnull.murdermystery.Round;

import org.bukkit.entity.Player;

import java.util.List;

public class Round {

    private Player murderer;
    private Player spy;
    private long startTime;
    List<Player> remainingPlayers;
    private static final long ROUND_DURATION = 8 * 60 * 1000;

    public List<Player> getRemainingPlayers() {
        return remainingPlayers;
    }

    public Round(Player murderer, Player spy, long startTime, List<Player> remainingPlayers) {
        this.murderer = murderer;
        this.spy = spy;
        this.startTime = startTime;
        this.remainingPlayers =remainingPlayers;

    }

    public long getRemainingTime() {
        long elapsed = System.currentTimeMillis() - startTime;
        return ROUND_DURATION - elapsed;
    }

    public String getFormattedRemainingTime() {
        long remainingTime = getRemainingTime();
        long minutes = (remainingTime / 1000) / 60;
        long seconds = (remainingTime / 1000) % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    public Player getSpy() {
        return spy;
    }

    public void setSpy(Player spy) {
        this.spy = spy;
    }

    public Player getMurderer() {
        return murderer;
    }

    public void setMurderer(Player murder) {
        this.murderer = murder;
    }

    public boolean isMurderer(Player player){
        return player.getName().equalsIgnoreCase(getMurderer().getName());
    }
    public boolean isSpy(Player player){
        return player.getName().equalsIgnoreCase(getSpy().getName());
    }
}
