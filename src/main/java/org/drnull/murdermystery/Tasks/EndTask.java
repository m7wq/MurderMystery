package org.drnull.murdermystery.Tasks;


import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.drnull.murdermystery.CustomEvents.RoundStartEvent;
import org.drnull.murdermystery.MurderMystery;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class EndTask extends BukkitRunnable {

    private final RoundStartEvent e;
    private final UUID uuid;
    private final int timeInMinutes = 8; // time in minutes
    private final int ticksPerMinute = 60 * 20; // 60 seconds * 20 ticks per second
    private final int totalTicks = timeInMinutes * ticksPerMinute;
    private int count = 0;

    public EndTask(RoundStartEvent e, UUID uuid) {
        this.e = e;
        this.uuid = uuid;
    }


    @Override
    public void run() {
        if (count >= totalTicks && MurderMystery.getInstance()
                .getGameStarted()
                .containsKey(e.getMurderer().getWorld().getUID())) {

            endRound();
            this.cancel();
            return;
        }


        if (count % (10 * 20) == 0) {
            dropGoldIngotNearMurderer();
        }

        count += 20;
    }

    private void endRound() {
        setGameMode(e.getSpy(), e.getMurderer(), GameMode.SPECTATOR);
        setGameMode(e.getRemainingPlayers(), GameMode.SPECTATOR);
        e.getSpy().getInventory().clear();
        e.getMurderer().getInventory().clear();
        sendTitle(e.getMurderer(), "&cYOU LOST", "&fSadly...");
        sendTitle(e.getSpy(), "&aYOU WON!", "&fCongratulations");
        sendTitle(e.getRemainingPlayers(), "&aYOU WON!", "&fCongratulations");
        for (Player player : e.getRemainingPlayers()){

            if (player == null)continue;

            player.getInventory().clear();
        }
        MurderMystery.getInstance().getGameStarted().remove(e.getSpy().getWorld().getUID());

        e.getSpy().getWorld().spawnEntity(e.getSpy().getLocation(), EntityType.FIREWORK);

        new BukkitRunnable() {
            @Override
            public void run() {
                setGameMode(e.getSpy(), e.getMurderer(), GameMode.ADVENTURE);
                setGameMode(e.getRemainingPlayers(), GameMode.ADVENTURE);

                teleportToSpawn(e.getSpy(), e.getMurderer());
                teleportToSpawn(e.getRemainingPlayers());
            }
        }.runTaskLater(MurderMystery.getInstance(), 20 * 5);
    }

    private void dropGoldIngotNearMurderer() {
        Location dropLocation = e.getMurderer().getLocation().add(
                ThreadLocalRandom.current().nextDouble(-5, 5),
                0,
                ThreadLocalRandom.current().nextDouble(-5, 5)
        );
        dropLocation.getWorld().dropItem(dropLocation, new ItemStack(Material.GOLD_INGOT));
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
        player.sendTitle(MurderMystery.getInstance().colorize(title),MurderMystery.getInstance().colorize(subtittle) );
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

    public void teleportToSpawn(Player player, Player player2){

        player.teleport(player.getWorld().getSpawnLocation());
        player2.teleport(player2.getWorld().getSpawnLocation());

    }
}
