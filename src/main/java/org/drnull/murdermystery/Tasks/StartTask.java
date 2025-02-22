package org.drnull.murdermystery.Tasks;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.drnull.murdermystery.CustomEvents.RoundStartEvent;
import org.drnull.murdermystery.MurderMystery;
import org.drnull.murdermystery.Round.Round;

import java.util.*;

public class StartTask extends BukkitRunnable {

    private boolean running = false;
    private MurderMystery plugin;
    private HashMap<UUID,Round> map;
    private UUID uuid;

    public StartTask(UUID uuid, HashMap<UUID, Round> map, MurderMystery plugin) {
        this.uuid = uuid;
        this.map = map;
        this.plugin = plugin;
    }





    public int count = 5;
    String prefix = MurderMystery.getInstance().getPrefix();

    @Override
    public void run() {
        if (!running) {
            if (count <= 0) {
                startGame();
                cancel();
            } else {
                if (Bukkit.getOnlinePlayers().size() < 3) {
                    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', MurderMystery.getInstance().getPrefix() + " &cNot enough players to start."));
                    this.cancel();
                    return;
                }

                Bukkit.broadcastMessage(MurderMystery.getInstance().colorize(prefix + " &aGame starting in &e" + count + " &aseconds"));
                count--;
            }
        }
    }

    private void startGame() {
        running = true;

        Bukkit.broadcastMessage(plugin.colorize(prefix + " &aGame started!"));

        long startTime = System.currentTimeMillis();
        List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
        Collections.shuffle(players);

        List<Player> remainingPlayers = players.subList(2, players.size());

        Round round=
        round = new Round(players.get(0), players.get(1), startTime, remainingPlayers);

        for (Player player : remainingPlayers) {
            sendTitle(player, "&aCREWMATE", "&fTry to be alive for 8 minutes");
        }


            sendTitle(round.getMurderer(), "&cMURDER", "&fYour mission is to kill all players");
            sendTitle(round.getSpy(), "&bSPY", "&fYour mission is to kill the murderer");

            RoundStartEvent roundStartEvent = new RoundStartEvent(round, remainingPlayers);
            Bukkit.getServer().getPluginManager().callEvent(roundStartEvent);

            map.put(uuid, round);


        running = false;
    }

    private void sendTitle(Player player, String title, String subtitle) {
        if (player != null) {
            player.sendTitle(plugin.colorize(title), plugin.colorize(subtitle));
        }
    }
}
