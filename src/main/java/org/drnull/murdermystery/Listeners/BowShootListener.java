package org.drnull.murdermystery.Listeners;


import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import org.drnull.murdermystery.MurderMystery;
import org.drnull.murdermystery.Round.Round;
import org.drnull.murdermystery.Utils.RoundsUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BowShootListener extends RoundsUtils implements Listener {

    private final Map<UUID, Long> cooldowns = new HashMap<>();
    private final long cooldownTime = 10000; // milliseconds


    Round getRound(Player player) {
        return plugin.getGameStarted().get(player.getWorld().getUID());
    }

    private final MurderMystery plugin;

    public BowShootListener(MurderMystery murderMystery) {
        this.plugin = murderMystery;
    }


    @EventHandler
    public void onBowShoot(EntityShootBowEvent e) {
        if (!(e.getEntity() instanceof Player)) return;

        Player player = (Player) e.getEntity();
        UUID playerUUID = player.getUniqueId();

        long currentTime = System.currentTimeMillis();

        if (!getRound(player).isSpy(player))
            return;

        if (cooldowns.containsKey(playerUUID)) {
            long lastShootTime = cooldowns.get(playerUUID);
            long timeLeft = (lastShootTime + cooldownTime - currentTime) / 1000;

            if (timeLeft > 0) {
                player.sendMessage(plugin.colorize(plugin.getPrefix() + " &cYou can't use your bow again until &e" + timeLeft + " &cseconds"));
                e.setCancelled(true);
                return;
            }


        }
        cooldowns.put(playerUUID, currentTime);
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent e) {

        if (!(e.getEntity() instanceof Arrow))
            return;

        if (!(e.getEntity().getShooter() instanceof Player))
            return;

        Player player = (Player) e.getEntity().getShooter();


        Arrow arrow = (Arrow) e.getEntity();


        arrow.remove();


        if (getRound(player).isSpy(player))
            return;






        new BukkitRunnable() {

            boolean hit;

            @Override
            public void run() {

                for (Entity entity : arrow.getNearbyEntities(0.5, 0.5, 0.5)) {
                    if (entity.getType() == EntityType.PLAYER) {
                        hit = true;
                        break;
                    }
                }

                if (!hit)
                    player.getInventory().remove(Material.BOW);





            }


        }.runTaskLater(plugin, 1);


    }

}
