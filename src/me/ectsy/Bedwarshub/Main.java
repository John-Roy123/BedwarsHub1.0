package me.ectsy.Bedwarshub;

import me.ectsy.Bedwarshub.manager.arenaManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

        @Override
        public void onEnable(){
            //Makes a directory within the "plugins" folder about this plugin
            if(!(getDataFolder().exists())){
                getDataFolder().mkdir();
            }
            //Looks for a config file for this plugin and if it doesn't exist, saves a new one
            if(getConfig() == null){
                saveDefaultConfig();
            }

        }
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String args[]){
        if(!(sender instanceof Player)){
            sender.sendMessage("only players may execute this!");
            return true;
        }

        Player p = (Player) sender;

        if(cmd.getName().equalsIgnoreCase("create")){
            arenaManager.getManager().createArena(p.getLocation());
            p.sendMessage("Created arena at " + p.getLocation().toString());

            return true;
        }
        if(cmd.getName().equalsIgnoreCase("join")){
            if(args.length != 1){
                p.sendMessage("Insuffcient arguments!");
                return true;
            }
            int num = 0;
            try{
                num = Integer.parseInt(args[0]);
            }catch(NumberFormatException e){
                p.sendMessage("Invalid arena ID");
            }
            arenaManager.getManager().addPlayer(p, num);

            return true;
        }
        if(cmd.getName().equalsIgnoreCase("leave")){
            arenaManager.getManager().removePlayer(p);
            p.sendMessage("You have left the arena!");

            return true;
        }
        if(cmd.getName().equalsIgnoreCase("remove")){
            if(args.length != 1){
                p.sendMessage("Insuffcient arguments!");
                return true;
            }
            int num = 0;
            try{
                num = Integer.parseInt(args[0]);
            }catch(NumberFormatException e){
                p.sendMessage("Invalid arena ID");
            }
            arenaManager.getManager().removeArena(num);

            return true;
        }

        return false;
    }
}
