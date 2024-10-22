package org.drnull.murdermystery.Utils;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.drnull.murdermystery.MurderMystery;


import java.util.List;

public class RoundsUtils  {


    public void setGameMode(Player player, GameMode gameMode){
        player.setGameMode(gameMode);
    }

    public void setGameMode(Player player ,Player player2, GameMode gameMode){
        player.setGameMode(gameMode);
        player2.setGameMode(gameMode);
    }
    public void setGameMode(List<Player> list, GameMode gameMode){
        for (Player player : list){
            if (player == null) continue;
            player.setGameMode(gameMode);
        }
    }
    public void sendTitle(Player player, String title, String subtittle){
        player.sendTitle(MurderMystery.getInstance().colorize(title),MurderMystery.getInstance().colorize(subtittle));
    }
    public void sendTitle(Player player, Player player2, String title, String subtittle){
        player.sendTitle(MurderMystery.getInstance().colorize(title),MurderMystery.getInstance().colorize(subtittle));
        player2.sendTitle(MurderMystery.getInstance().colorize(title),MurderMystery.getInstance().colorize(subtittle));
    }

    public void sendTitle(List<Player> list ,String title, String subtittle){
        for (Player player : list){
            if (player == null) continue;
            player.sendTitle(MurderMystery.getInstance().colorize(title),MurderMystery.getInstance().colorize(subtittle));
        }
    }

    public void teleportToSpawn(List<Player> players){
        for (Player player : players){
            if (player == null) continue;
            player.teleport(player.getWorld().getSpawnLocation());
        }
    }
    public void teleportToSpawn(Player player){

        player.teleport(player.getWorld().getSpawnLocation());

    }
    public void teleportToSpawn(Player player, Player player2){

        player.teleport(player.getWorld().getSpawnLocation());
        player2.teleport(player2.getWorld().getSpawnLocation());

    }
}
