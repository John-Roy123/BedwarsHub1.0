package me.ectsy.Bedwarshub.manager;


import me.ectsy.Bedwarshub.Main;
import me.ectsy.Bedwarshub.arenas.Arena;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class arenaManager {
    //Saves where the player teleported to
    public Map<String, Location>locs = new HashMap<String, Location>();
    //Instantiate a new instance of this class
    public static arenaManager am = new arenaManager();
    //Logs inventory and armor
    public Map<String, ItemStack[]>inv = new HashMap<String, ItemStack[]>();
    public Map<String, ItemStack[]>armor = new HashMap<String, ItemStack[]>();
    //Lists arenas
    List<Arena> arenas = new ArrayList <Arena> ();
    int arenaSize = 0;
    static Main plugin;
    public arenaManager(Main main) {
        plugin = main;
    }
    arenaManager(){}


    //an instance of the arena to work statically
    public static arenaManager getManager() { return am; }


    //get an arena from the list
    public Arena getArena(int i){
    for(Arena a : arenas) {
        if (a.getId() == i){
            return a;
    }
    }
    return null;
    }

    //add player to the arena while saving their inventory
    public void addPlayer(Player p, int i){
        Arena a = getArena(i); //get the arena you want to join
        if (a == null){
        p.sendMessage("Something failed! (Invalid arena)");
        return;
        }

        a.getPlayers().add(p.getName());//add them to the arena list of players
        inv.put(p.getName(), p.getInventory().getContents());//save inventory
        armor.put(p.getName(), p.getInventory().getArmorContents());

        p.getInventory().setArmorContents(null);
        p.getInventory().clear();

        locs.put(p.getName(), p.getLocation());
        p.teleport(a.spawn);//teleport to the arena spawn
    }
    public void removePlayer(Player p){
        Arena a = null; //make an arena
        for(Arena arena : arenas){
            if(arena.getPlayers().contains(p.getName())){
                a = arena; //checks if the player was already in an arena, sets the arena to the player's
            }
            //if arena isn't found, it'll be set to null
        }
        if(a == null || !a.getPlayers().contains(p.getName())){//make sure it is not null
            p.sendMessage("Invalid operation!");
            return;
        }
        a.getPlayers().remove(p.getName()); //remove from arena

        p.getInventory().clear();
        p.getInventory().setArmorContents(null);

        p.getInventory().setContents(inv.get(p.getName())); //restore inventory
        p.getInventory().setArmorContents(armor.get(p.getName()));

        inv.remove(p.getName()); //remove entries from hashmaps
        armor.remove(p.getName());
        p.teleport(locs.get(p.getName()));
        locs.remove(p.getName());

        p.setFireTicks(0);
    }
    //create arena
    public Arena createArena(Location l){
        int num = arenaSize + 1;
        arenaSize++;

        Arena a = new Arena(l, num);
        arenas.add(a);
        plugin.getConfig().set("Arenas."+num, serializeLoc(l));
        List<Integer> list = plugin.getConfig().getIntegerList(("Arenas.Arenas"));
        list.add(num);
        plugin.getConfig().set("Arenas.Arenas", list);
        plugin.saveConfig();

        return a;
    }
    public Arena reloadArena(Location l){
        int num = arenaSize + 1;
        arenaSize++;

        Arena a = new Arena(l, num);
        arenas.add(a);
        return a;
    }
    public void removeArena(int i){
        Arena a = getArena(i);
        if (a == null){
            return;
        }
        arenas.remove(a);
        plugin.getConfig().set("Arenas."+ i, null);
        List<Integer> list = plugin.getConfig().getIntegerList("Arenas.Arenas");
        list.remove(i);
        plugin.getConfig().set("Arenas.Arenas", list);
        plugin.saveConfig();
    }
    public boolean isInGame(Player p){
        for(Arena a : arenas){
            if (a.getPlayers().contains((p.getName()))) {
                return true;
            }
        }
        return false;
    }

    public void loadGames(){
        arenaSize = 0;

        if(plugin.getConfig().getIntegerList("Arenas.Arenas").isEmpty()){
            return;
        }
        for(int i : plugin.getConfig().getIntegerList("Arenas.Arenas")){
            Arena a = reloadArena(deserializeLoc(plugin.getConfig().getString("Arenas."+i)));
            a.id = i;
        }
    }

    public String serializeLoc(Location l){
        return l.getWorld().getName()+","+l.getBlockX()+","+l.getBlockY()+","+l.getBlockZ();
    }
    public Location deserializeLoc(String s){
        String[] st = s.split(",");
        return new Location(Bukkit.getWorld(st[0]), Integer.parseInt(st[1]), Integer.parseInt(st[2]), Integer.parseInt(st[3]));
    }
}
