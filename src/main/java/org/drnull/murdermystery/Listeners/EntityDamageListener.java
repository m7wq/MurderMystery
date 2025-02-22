package org.drnull.murdermystery.Listeners;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;
import org.drnull.murdermystery.MurderMystery;
import org.drnull.murdermystery.Round.Round;
import org.drnull.murdermystery.Utils.RoundsUtils;

import java.util.List;

public class EntityDamageListener extends RoundsUtils implements Listener {

    private final MurderMystery plugin = MurderMystery.getInstance();


    @EventHandler
    public void cancelHandHit(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player) || !(e.getDamager() instanceof Player))
            return;


        Player damager = (Player) e.getDamager();

        if (!plugin.getGameStarted().containsKey(damager.getWorld().getUID()))
            return;

        Player victim = (Player) e.getEntity();

        if (damager.getItemInHand().getType() == Material.AIR || damager.getItemInHand() == null)
            e.setCancelled(true);
    }


    @EventHandler
    public void Murder(EntityDamageByEntityEvent e) {


        Entity attacker = e.getDamager();
        Entity entity = e.getEntity();

        if (attacker == null || entity == null || !(attacker instanceof Player) || !(entity instanceof Player))
            return;


        Player damager = (Player) attacker;
        Player victim = (Player) entity;


        if (!plugin.getGameStarted().containsKey(damager.getWorld().getUID()))
            return;


        Round round = plugin.getGameStarted().get(victim.getWorld().getUID());

        if (!round.isMurderer(damager))
            return;

        if (damager.getItemInHand().getType() != Material.DIAMOND_SWORD) return;

        damager.playSound(victim.getLocation(), Sound.ORB_PICKUP, 1, 1);


        if (round.isSpy(victim))
            victim.getWorld().dropItem(victim.getLocation(), new ItemStack(Material.BOW));


        setGameMode(victim, GameMode.SPECTATOR);
        sendTitle(victim, "&cYOU DIED", "&fYou can stay to watch"); // اي شخابيط

        if (!isAllDied(round.getRemainingPlayers(), round.getSpy()))
            return;

        plugin.getEndTaskMap().get(damager.getWorld().getUID()).cancel();



        setGameMode(round.getSpy(), round.getMurderer(), GameMode.SPECTATOR);
        setGameMode(round.getRemainingPlayers(), GameMode.SPECTATOR);

        sendTitle(round.getMurderer(), "&aYOU WON!", "&fCongratulations");
        sendTitle(round.getSpy(), "&cYOU LOST", "&fSadly...");
        sendTitle(round.getRemainingPlayers(), "&cYOU LOST", "&fSadly...");
        for (Player p : round.getRemainingPlayers()){
            p.getInventory().clear();
        }
        round.getSpy().getInventory().clear();
        round.getMurderer().getInventory().clear();

        plugin.getGameStarted().remove(round.getSpy().getWorld().getUID());


        new BukkitRunnable() {
            @Override
            public void run() {
                round.getSpy().setGameMode(GameMode.ADVENTURE);
                round.getMurderer().setGameMode(GameMode.ADVENTURE);
                for (Player player : round.getRemainingPlayers()) {
                    player.setGameMode(GameMode.ADVENTURE);
                }

                round.getSpy().teleport(round.getSpy().getWorld().getSpawnLocation());
                round.getMurderer().teleport(round.getMurderer().getWorld().getSpawnLocation());
                for (Player player : round.getRemainingPlayers()) {
                    player.teleport(player.getWorld().getSpawnLocation());
                }
            }
        }.runTaskLater(plugin, 20 * 5);




    }

    public boolean isAllDied(List<Player> remaining, Player spy) {
        if (spy.getGameMode() != GameMode.SPECTATOR)
            return false;

        for (Player player : remaining) {
            if (player == null) continue;

            if (player.getGameMode() != GameMode.SPECTATOR)
                return false;
        }

        return true;
    }

    @EventHandler
    public void SpyShoot(EntityDamageByEntityEvent e) {
        Entity attacker = e.getDamager();
        Entity entity = e.getEntity();

        if (!(attacker instanceof Arrow) || !(entity instanceof Player))
            return;

        Arrow arrow = (Arrow) attacker;
        ProjectileSource shooter = arrow.getShooter();

        if (!(shooter instanceof Player))
            return;

        Player damager = (Player) shooter;
        Player victim = (Player) entity;

        if (!plugin.getGameStarted().containsKey(damager.getWorld().getUID()))
            return;

        if (damager.getInventory().getItemInHand().getType() != Material.BOW)
            return;

        Round round = plugin.getGameStarted().get(victim.getWorld().getUID());

        if (round.isMurderer(victim)) {
            plugin.getEndTaskMap().get(damager.getWorld().getUID()).cancel();

            round.getSpy().setGameMode(GameMode.SPECTATOR);
            round.getMurderer().setGameMode(GameMode.SPECTATOR);
            for (Player player : round.getRemainingPlayers()) {
                player.setGameMode(GameMode.SPECTATOR);
            }

            sendTitle(round.getMurderer(), "&cYOU LOST", "&fSadly...");
            sendTitle(round.getSpy(), "&aYOU WON!", "&fCongratulations");
            round.getSpy().getInventory().clear();
            round.getMurderer().getInventory().clear();
            for (Player player : round.getRemainingPlayers()) {
                if (player == null) continue;

                sendTitle(player, "&aYOU WON!", "&fCongratulations");
                player.getInventory().clear();
            }
            plugin.getGameStarted().remove(round.getSpy().getWorld().getUID());

            new BukkitRunnable() {
                @Override
                public void run() {
                    round.getSpy().setGameMode(GameMode.ADVENTURE);
                    round.getMurderer().setGameMode(GameMode.ADVENTURE);
                    for (Player player : round.getRemainingPlayers()) {
                        player.setGameMode(GameMode.ADVENTURE);
                    }

                    round.getSpy().teleport(round.getSpy().getWorld().getSpawnLocation());
                    round.getMurderer().teleport(round.getMurderer().getWorld().getSpawnLocation());
                    for (Player player : round.getRemainingPlayers()) {
                        player.teleport(player.getWorld().getSpawnLocation());
                    }
                }
            }.runTaskLater(MurderMystery.getInstance(), 20 * 5);

        } else {
            setGameMode(damager, victim, GameMode.SPECTATOR);
            sendTitle(damager, "&cYOU MISSED", "You killed an innocent person");
            sendTitle(victim, "&cYOU DIED", "The spy killed you");

            if (!isAllDied(round.getRemainingPlayers(), round.getSpy()))
                return;

            plugin.getEndTaskMap().get(damager.getWorld().getUID()).cancel();

            setGameMode(round.getSpy(), round.getMurderer(), GameMode.SPECTATOR);
            setGameMode(round.getRemainingPlayers(), GameMode.SPECTATOR);

            sendTitle(round.getMurderer(), "&aYOU WON!", "&fCongratulations");
            sendTitle(round.getSpy(), "&cYOU LOST", "&fSadly...");
            sendTitle(round.getRemainingPlayers(), "&cYOU LOST", "&fSadly...");
            for (Player p : round.getRemainingPlayers()) {
                if (p == null) continue;
                p.getInventory().clear();
            }
            round.getSpy().getInventory().clear();
            round.getMurderer().getInventory().clear();

            plugin.getGameStarted().remove(round.getSpy().getWorld().getUID());

            new BukkitRunnable() {
                @Override
                public void run() {
                    round.getSpy().setGameMode(GameMode.ADVENTURE);
                    round.getMurderer().setGameMode(GameMode.ADVENTURE);
                    for (Player player : round.getRemainingPlayers()) {
                        player.setGameMode(GameMode.ADVENTURE);
                    }

                    round.getSpy().teleport(round.getSpy().getWorld().getSpawnLocation());
                    round.getMurderer().teleport(round.getMurderer().getWorld().getSpawnLocation());
                    for (Player player : round.getRemainingPlayers()) {
                        player.teleport(player.getWorld().getSpawnLocation());
                    }
                }
            }.runTaskLater(MurderMystery.getInstance(), 20 * 5);
        }
    }

}
