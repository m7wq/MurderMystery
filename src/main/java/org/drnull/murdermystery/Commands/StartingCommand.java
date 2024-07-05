package org.drnull.murdermystery.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.drnull.murdermystery.CustomEvents.RoundStartEvent;
import org.drnull.murdermystery.MurderMystery;
import org.drnull.murdermystery.Round.Round;
import org.drnull.murdermystery.Tasks.StartTask;

import java.util.*;

public class StartingCommand implements CommandExecutor {

    MurderMystery plugin;


    public StartingCommand(MurderMystery murderMystery){
        this.plugin = murderMystery;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {

        if (!(sender instanceof Player))
            return false;


        Player player = (Player) sender;

        World world = player.getWorld();

        if (plugin.getGameStarted().
                containsKey(world.getUID())){

            player.sendMessage(plugin.colorize(
                    plugin.getPrefix())+" &cYour world already in game!");



            return false;

        }



        startGame((HashMap<UUID, Round>) plugin.getGameStarted(), player.getWorld().getUID());



        return false;
    }



    private void startGame(HashMap<UUID,Round> map, UUID uuid){

        StartTask startTask = new StartTask(uuid,map,plugin);

        startTask.runTaskTimer(MurderMystery.getInstance(),0,20);


    }

}
