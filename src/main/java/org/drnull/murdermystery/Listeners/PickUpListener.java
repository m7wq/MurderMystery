package org.drnull.murdermystery.Listeners;


import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.Inventory;

import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.drnull.murdermystery.MurderMystery;
import org.drnull.murdermystery.Round.Round;

public class PickUpListener implements Listener {

    @EventHandler
    public void pickup(PlayerPickupItemEvent e){

        if (!MurderMystery.getInstance().getGameStarted()
                .containsKey(e.getPlayer().getWorld().getUID()))
            return;

        Player player = e.getPlayer();

        if (e.getItem()==null)
            return;


        Inventory inv = player.getInventory();


        Round round = MurderMystery.getInstance()
                .getGameStarted().get(player.getWorld().getUID());



        if (!round.getRemainingPlayers().contains(player)){
            e.setCancelled(true);
            return;
        }

        if (e.getItem().getItemStack().getType()!=Material.GOLD_INGOT)
            return;


        MurderMystery.getInstance().addCoins(player,e.getItem().getItemStack().getAmount());

        e.getItem().remove();

        e.setCancelled(true);

        player.playSound(e.getItem().getLocation(), Sound.ORB_PICKUP, 1,1);

        if (MurderMystery.getInstance().getCoins(player) < 10)
            return;

        if (inv.contains(Material.BOW)){
            player.sendMessage(MurderMystery.getInstance().colorize(MurderMystery.getInstance().getPrefix())+" &cYou already have a bow");
            e.setCancelled(true);
            return;
        }


        MurderMystery.getInstance().setCoins(player,
                MurderMystery.getInstance().getCoins(player)-10);


        inv.addItem(new ItemStack(Material.BOW));
        inv.addItem(new ItemStack(Material.ARROW));

    }

}
