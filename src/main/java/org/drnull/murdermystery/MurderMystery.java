package org.drnull.murdermystery;


import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import org.drnull.murdermystery.Commands.StartingCommand;
import org.drnull.murdermystery.Listeners.BowShootListener;
import org.drnull.murdermystery.Listeners.EntityDamageListener;
import org.drnull.murdermystery.Listeners.PickUpListener;
import org.drnull.murdermystery.Listeners.RoundStartListener;
import org.drnull.murdermystery.Round.Round;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MurderMystery extends JavaPlugin {

    private final Map<UUID, Round> GameStarted = new HashMap<>();
    private final Map<UUID, BukkitRunnable> EndTaskMap = new HashMap<>();

    private final String prefix = "&c&lMurder&7&lMystery";
    HashMap<Player, Integer> coinsMap = new HashMap<>();

    public int getCoins(Player player){
        return coinsMap.getOrDefault(player, 0);
    }

    public void addCoins(Player player, int amount){

        if (coinsMap.containsKey(player)){
            coinsMap.replace(player, getCoins(player)+amount);
            return;
        }

        coinsMap.put(player , getCoins(player)+amount);
    }

    public void setCoins(Player player, int amount){
        if (coinsMap.containsKey(player)){
            coinsMap.replace(player, amount);
            return;
        }

        coinsMap.put(player , amount);
    }

    private static MurderMystery instance;

    @Override
    public void onEnable() {

        instance=this;

        getServer().getPluginManager().registerEvents(new EntityDamageListener(), this);
        getServer().getPluginManager().registerEvents(new RoundStartListener(this), this);
        getServer().getPluginManager().registerEvents(new PickUpListener(), this);
        getServer().getPluginManager().registerEvents(new BowShootListener(this), this);


        getCommand("start").setExecutor(new StartingCommand(this));

    }

    @Override
    public void onDisable() {

    }

    public Map<UUID, Round> getGameStarted() {
        return GameStarted;
    }

    public static MurderMystery getInstance(){
        return instance;
    }

    public String getPrefix(){
        return prefix;
    }

    public String colorize(String s){
        return ChatColor.translateAlternateColorCodes('&', s);
    }


    public Map<UUID, BukkitRunnable> getEndTaskMap() {
        return EndTaskMap;
    }
}
