package me.belf.advancedpvp.datatypes;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.UUID;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.belf.advancedpvp.AdvancedPvP;
import me.belf.advancedpvp.Schedulable;
import me.belf.advancedpvp.Users;
import me.belf.advancedpvp.datatypes.PlayerData;
import me.belf.advancedpvp.schedulables.CooldownBar;
import org.bukkit.ChatColor;


public class PlayerData {
    
    private ArrayList<Integer> tasks = new ArrayList<Integer>();
    
    private long startCD = 0;
    private long usedWeapon = 0;
    private long clickedAir = 0; // Bugfix for bukkit :(
    private long attackedProjectile = 0; // Bugfix for bukkit :(
    private long stunned = 0;    
    private String playerName;
    private boolean spoutCraftEnabled = false;
    
    UUID sGreenBarId, sRedBarId;
    boolean displayBar = false;
    
    
    //WeaponType lastused = null;
    
    public PlayerData(Player player) {        
        playerName = player.getName();
    }
    
    public void setUsedWeapon(long newvalue) {       
        Date currentDate = new Date();
        
        setstartCD(currentDate.getTime());
        usedWeapon = newvalue;
        
        startCountdown();   
    }   
    
    public long getUsedWeapon() {
        return usedWeapon;        
    }
    
    public void setstartCD(long newvalue) {        
        startCD = newvalue;   
    }   
    
    public long getstartCD() {
        return startCD;        
    } 
    
    
    public void startCountdown() {
        Player player = Bukkit.getPlayer(playerName);
        
        for (Integer taskId : tasks) {
            Bukkit.getServer().getScheduler().cancelTask(taskId);
        }
        
        double resting = java.lang.Math.round(((double)(usedWeapon) - (double)(startCD))/100) / 10;
        
        
        if(!getSpoutCraftEnabled()) {
            player.sendMessage(ChatColor.RED + "You rest for " + resting + " seconds.");
        }        
        
        CooldownBar cooldownBar = new CooldownBar(player, this);
        tasks.add(Schedulable.scheduleSyncRepeatingTask(cooldownBar, 0, 1L));        
    }
    
    public void setClickedAir(long newvalue) {        
        clickedAir = newvalue;        
    }    
    
    public long getClickedAir() {
        return clickedAir;        
    }
    
    public void setAttackedProjectile(long newvalue) {        
        attackedProjectile = newvalue;        
    }    
    
    public long getAttackedProjectile() {
        return attackedProjectile;        
    }
    
    public void setGreenBarId(UUID id) {        
        sGreenBarId = id;        
    } 
    
    public void setRedBarId(UUID id) {        
        sRedBarId = id;        
    }
    
    public UUID getGreenBarId() {        
        return sGreenBarId;      
    } 
    
    public UUID getRedBarId() {        
        return sRedBarId;
    }
    
    public void setSpoutCraftEnabled(boolean bool) {        
        spoutCraftEnabled = bool;      
    } 
    
    public boolean getSpoutCraftEnabled() {        
        return spoutCraftEnabled;
    }
    
}
