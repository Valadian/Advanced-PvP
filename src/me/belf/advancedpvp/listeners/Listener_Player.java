package me.belf.advancedpvp.listeners;

import java.util.Date;

import org.bukkit.ChatColor;
import org.bukkit.event.block.Action;
import org.bukkit.entity.Player;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.event.player.PlayerItemHeldEvent;

import me.belf.advancedpvp.AdvancedPvP;
import me.belf.advancedpvp.Users;
import me.belf.advancedpvp.datatypes.PlayerData;
import me.belf.advancedpvp.Properties;


import me.belf.advancedpvp.AdvancedPvP;


public class Listener_Player extends PlayerListener {
    private AdvancedPvP plugin;

    public Listener_Player(AdvancedPvP plugin){
        this.plugin = plugin;
    }

    @Override
    public void onItemHeldChange(PlayerItemHeldEvent event){
        
        if(Properties.cooldown_on_change) {     
            long cooldown = 0;
            Date currentDate = new Date();
            Player player = event.getPlayer();
            PlayerData playerData = Users.getPlayerData(player);
            int weaponID = player.getItemInHand().getTypeId();

            if(Properties.confCooldowns.get(weaponID) == null) return;

            cooldown = Properties.confCooldowns.get(weaponID);  
            if(!player.hasPermission("advancedpvp.nocooldown")) {
                playerData.setUsedWeapon((currentDate.getTime() + cooldown));
            }
        }
        return;
    }
        
    public void onPlayerAnimation(PlayerAnimationEvent event){
        
        if(event.isCancelled()) {
            return;
        }
        
        
        Date currentDate = new Date();
        Player player = event.getPlayer();
        PlayerData playerData = Users.getPlayerData(player);
        
        if(playerData.getUsedWeapon() > currentDate.getTime()) {
            if(event.getAnimationType().equals(PlayerAnimationType.ARM_SWING)) {
                event.setCancelled(true);
                return;
            }
        }
        
    }
    
    @Override
    public void onPlayerInteract (PlayerInteractEvent event) {
        
        if(event.isCancelled()) {
            return;
        }
        
        long cooldown = 0;
        Date currentDate = new Date();         
        Player player = event.getPlayer(); 
        int weaponID = player.getItemInHand().getTypeId();      
        Action action = event.getAction();
        
        if(Properties.confCooldowns.get(weaponID) == null) {
            return;
        }
        
        
        if(weaponID == 261) {
            return;       
        }        
        else if(Properties.confCooldowns.get(weaponID) == null || (Properties.cooldown_on_damages_only) || 
                (action != Action.LEFT_CLICK_BLOCK && action != Action.LEFT_CLICK_AIR)) {
            return;
        }
        else {
            // Other weapons
            
            PlayerData playerData = Users.getPlayerData(player);
            
            if(playerData.getUsedWeapon() > currentDate.getTime()) {
                return;
            }
            
            
            cooldown = Properties.confCooldowns.get(weaponID);
            if(!player.hasPermission("advancedpvp.nocooldown")) {
                playerData.setUsedWeapon((currentDate.getTime() + cooldown));
            }
        }        
    }  

    @Override
    public void onPlayerQuit(PlayerQuitEvent event) {        
        Player player = event.getPlayer(); 
        Users.remove(player);
    }
    
    

}
