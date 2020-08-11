package me.ectsy.Bedwarshub.Listener;

import me.ectsy.Bedwarshub.Main;
import me.ectsy.Bedwarshub.manager.arenaManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.ArrayList;
import java.util.List;

public class gameListener implements Listener{

    static List<String> players = new ArrayList<String>();
    static Main plugin;

    public gameListener(Main plugin){
        gameListener.plugin = plugin;
    }

    @EventHandler
    public void onDamange(EntityDamageByEntityEvent e){
        if(e.getEntity() instanceof Player && players.contains(((Player) e.getEntity()).getName())){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e){
        if(arenaManager.getManager().isInGame(e.getEntity())){
            arenaManager.getManager().removePlayer(e.getEntity());
        }
    }

    public static void add(Player p){
        final String name = p.getName();
        players.add(name);

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
            @Override
            public void run(){
                players.remove(name);
            }
        }, 100L);
    }
}
