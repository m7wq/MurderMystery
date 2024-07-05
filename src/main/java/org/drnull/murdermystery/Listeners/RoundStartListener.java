package org.drnull.murdermystery.Listeners;


import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;
import org.drnull.murdermystery.CustomEvents.RoundStartEvent;
import org.drnull.murdermystery.MurderMystery;
import org.drnull.murdermystery.Tasks.EndTask;
import org.drnull.murdermystery.Utils.RoundsUtils;


public class RoundStartListener extends RoundsUtils implements Listener {

    public enum State{
        MURDERER,SPY,CREWMATE
    }

    public MurderMystery plugin;



    public RoundStartListener(MurderMystery plugin) {
        this.plugin=plugin;
    }


    //

    @EventHandler
    public void startEvent(RoundStartEvent e){

        setGameMode(e.getSpy(), e.getMurderer(), GameMode.SURVIVAL);
        setGameMode(e.getRemainingPlayers(), GameMode.SURVIVAL);

        Player murderer = e.getMurderer();
        Player spy = e.getSpy();

        murderer.getInventory().clear();
        spy.getInventory().clear();

        murderer.getInventory().addItem(new ItemStack(Material.DIAMOND_SWORD));


        giveInfiniteBow(spy);

        spy.getInventory().addItem(new ItemStack(Material.ARROW));

        EndTask endTask = new EndTask(e, e.getMurderer().getWorld().getUID());
        endTask.runTaskTimer(MurderMystery.getInstance(),0,20);



        plugin.getEndTaskMap().put(e.getSpy().getWorld().getUID(), endTask);

        new BukkitRunnable(){

            @Override
            public void run() {
                if (MurderMystery.getInstance().getGameStarted()
                        .containsKey(e.getRound().getSpy().getWorld().getUID())){

                    e.getMurderer().setScoreboard(createScoreboard(e.getMurderer(),e.getRound().getFormattedRemainingTime(), State.MURDERER));
                    e.getSpy().setScoreboard(createScoreboard(e.getSpy(),e.getRound().getFormattedRemainingTime(), State.SPY));
                    for (Player player : e.getRemainingPlayers()){
                        if (player == null)continue;

                        player.setScoreboard(createScoreboard(player,e.getRound().getFormattedRemainingTime(),State.CREWMATE));
                    }

                }else {
                    removePlayerScoreboard(e.getMurderer());
                    removePlayerScoreboard(e.getSpy());

                    for (Player player : e.getRemainingPlayers()){
                        if (player == null)continue;

                        removePlayerScoreboard(player);
                    }
                    cancel();
                }
            }
        }.runTaskTimer(MurderMystery.getInstance(),0,1);
    }





    public void removePlayerScoreboard(Player player) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        if (manager != null) {
            Scoreboard emptyScoreboard = manager.getNewScoreboard();
            player.setScoreboard(emptyScoreboard);
        }
    }



    public Scoreboard createScoreboard(Player player, String remainingTime, State state) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getNewScoreboard();


        Objective objective = board.registerNewObjective("test", "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(colorize("&aMurderMystery"));



        objective.getScore(colorize(" ")).setScore(10);
        objective.getScore(colorize("&fName: &a"+player.getName())).setScore(9);
        objective.getScore(colorize("&fCoins: &a"+plugin.getCoins(player))).setScore(8);
        objective.getScore(colorize("&fStatue: &a"+state.name())).setScore(7);
        objective.getScore(" ").setScore(6);
        objective.getScore(colorize("&fTime Remaining: &a"+remainingTime)).setScore(5);
        objective.getScore(colorize(" ")).setScore(4);
        objective.getScore(colorize("&fVersion: &a1.0")).setScore(3); // 3shan akhle alscoreboard 6wylh
        objective.getScore(colorize(" ")).setScore(2);
        objective.getScore(colorize("&eplay.PixalsNetwork.net")).setScore(1);

        return board;
    }

    public void giveInfiniteBow(Player player) {


        ItemStack bow = new ItemStack(Material.BOW);

        ItemMeta meta = bow.getItemMeta();


        meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);



        bow.setItemMeta(meta);
        player.getInventory().addItem(bow);
    }




    public String colorize(String s) {
        return ChatColor.translateAlternateColorCodes('&',s);
    }
}
