package me.ectsy.Bedwarshub.Listener;

import me.ectsy.Bedwarshub.Main;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class blockListener implements Listener {
    static Main plugin;
    public blockListener(Main plugin){ blockListener.plugin = plugin; }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent b){

        //Gets all the details on the block + player who broke the block
        Block block = b.getBlock();
        Material m = block.getType();
        Player p = b.getPlayer();

        //Checks if the block broken is NOT block in the shop
        if(m != Material.WOOL){
            p.sendMessage(ChatColor.DARK_RED+"You can only break blocks placed by a player");
            b.setCancelled(true);
        }
        else if(m != Material.OBSIDIAN){
            p.sendMessage(ChatColor.DARK_RED+"You can only break blocks placed by a player");
            b.setCancelled(true);
        }
        else if(m != Material.ENDER_STONE){
            p.sendMessage(ChatColor.DARK_RED+"You can only break blocks placed by a player");
            b.setCancelled(true);
        }
        else{
            p.sendMessage(ChatColor.BOLD+"You shouldn't be getting this message");
        }
    }

}
