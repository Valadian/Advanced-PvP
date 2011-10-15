package me.belf.advancedpvp;


import java.util.HashMap;
import org.bukkit.entity.Player;

import me.belf.advancedpvp.datatypes.PlayerData;


public class Users {
    
    private static volatile Users instance;
    public static HashMap<Player, PlayerData> players = new HashMap<Player, PlayerData>();
   
    public static PlayerData getPlayerData(Player player){
     
        if(players.get(player) != null) {
           return players.get(player);
        }     
        else {
           players.put(player, new PlayerData(player));
           return players.get(player);
        }
    }
    
    public static Users getInstance() {
        if (instance == null) {
            instance = new Users();
        }
        return instance;
    }
    
    public static void remove(Player player) {
        if(players.get(player) != null) {
           players.remove(players.get(player));
        }
    }
}