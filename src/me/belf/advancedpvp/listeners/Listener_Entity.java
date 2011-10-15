/*
 * BetterPVP - by Belf
 * 
 *
 * powered by Kickstarter
 */

package me.belf.advancedpvp.listeners;

import java.util.Date;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

import org.bukkit.inventory.ItemStack;
import org.bukkit.Location;
import org.bukkit.World;

import me.belf.advancedpvp.AdvancedPvP;
import me.belf.advancedpvp.Users;
import me.belf.advancedpvp.Abilities;
import me.belf.advancedpvp.datatypes.PlayerData;
import me.belf.advancedpvp.Properties;


public class Listener_Entity extends EntityListener {
    private AdvancedPvP plugin;

    public Listener_Entity(AdvancedPvP plugin){
        this.plugin = plugin;
    }
    
    
    @Override
    public void onProjectileHit(ProjectileHitEvent event) {        
        if(!(Properties.cooldown_on_damages_only)) {
            Entity entity = event.getEntity();

            if(entity instanceof Arrow) {            
                Arrow arrow = (Arrow) entity;

                if(arrow.getShooter() instanceof Player) {

                    Player player = (Player) arrow.getShooter();                
                    PlayerData playerData = Users.getPlayerData(player);
                    Date currentDate = new Date();

                    if(playerData.getUsedWeapon() > currentDate.getTime()) {                    
                        return;
                    }

                    int cooldown = Properties.confCooldowns.get(261);
                    
                    if(!player.hasPermission("advancedpvp.nocooldown")) {
                        playerData.setUsedWeapon((currentDate.getTime() + cooldown));
                    }
                    
                    playerData.setClickedAir(currentDate.getTime()); // Dirty bugfix for bukkit :(
                    
                    return;                
                }     
            }
        }
    }
    
    
    @Override
    public void onEntityDamage(EntityDamageEvent event)	{
        
        Date currentDate = new Date();
        double backstabMultiplier, parryProbability = 0, parryKnockbackPower = 0, criticalProbability, disarmProbability, knockbackProbability, knockbackPower, 
                blockingProbability, blockingKnockbackPower, counterattackProbability = 0, attackerDirection = 0, random;
        
        int cooldown = 0, damages, damagesCounterattack = 0, criticalDamages, blockingDamages, weaponID, weaponIDDefender = 0;
        boolean isDefenderPlayer, isDropable = false;
        long canDamageIn;
        
        
                
        if (event.getCause() == DamageCause.ENTITY_ATTACK){
	    EntityDamageByEntityEvent e = (EntityDamageByEntityEvent)event;
	    
           
            if (e.getDamager() instanceof Player) {
		
                if(Properties.pvp_only && !(e.getEntity() instanceof Player)) {                    
                    return;
                }
                
                
                Player attacker = (Player)e.getDamager(); 
                PlayerData playerDataAttacker = Users.getPlayerData(attacker); 
                
                Player defenderPlayer = attacker; // Dirty
                
                World world = attacker.getWorld();
                
                               
                
                weaponID = attacker.getItemInHand().getTypeId();
                
                int intervalProjectile = ((int)currentDate.getTime() - (int)playerDataAttacker.getAttackedProjectile());
                
                // Dirty bugfix for bukkit :(
                if(weaponID == 261 && intervalProjectile > 100) {
                    event.setCancelled(true);
                    return;
                }
                
                if(weaponID == 261 && intervalProjectile < 100) {
                    return;
                }
                
                if(Properties.confCooldowns.get(weaponID) == null) {
                    event.setCancelled(true);
                    return;
                }
                
                
                cooldown = Properties.confCooldowns.get(weaponID);
                backstabMultiplier = Properties.confBackstab.get(weaponID);
                disarmProbability = Properties.confDisarm.get(weaponID);
                
                blockingKnockbackPower = Properties.confBlockingKnockbackPower.get(weaponID);
                blockingProbability = Properties.confBlockingProbability.get(weaponID);
                blockingDamages = Properties.confBlockingDamages.get(weaponID);
                
                knockbackPower = Properties.confKnockbackPower.get(weaponID);
                knockbackProbability = Properties.confKnockbackProbability.get(weaponID);
                
                criticalProbability = Properties.confCriticalProbability.get(weaponID);
                criticalDamages = Properties.confCriticalExtraDamages.get(weaponID);      
                
                
                
                
                if(Properties.confDamages.get(weaponID) == -1) damages = event.getDamage();
                else damages = Properties.confDamages.get(weaponID);
                          
                canDamageIn = (playerDataAttacker.getUsedWeapon() - currentDate.getTime());
                
                if(canDamageIn > 0){
                    if(!playerDataAttacker.getSpoutCraftEnabled()) {
                        attacker.sendMessage(ChatColor.RED + "You are too tired, you missed...");
                    }
                    
                    event.setCancelled(true);
                    return;
                }
                else {                                          
                    if(!attacker.hasPermission("advancedpvp.nocooldown")) {
                        playerDataAttacker.setUsedWeapon(currentDate.getTime() + cooldown);
                    }
                }
                
                
                Entity defender = e.getEntity();
                attackerDirection = attacker.getLocation().getDirection().dot(defender.getLocation().getDirection());                
                
                if(defender instanceof Player) {
                    isDefenderPlayer = true;
                    defenderPlayer = (Player)defender;                    
                    
                    weaponIDDefender = defenderPlayer.getItemInHand().getTypeId();
                    
                    if(Properties.confCooldowns.get(weaponIDDefender) != null) {                        
                        counterattackProbability = Properties.confCounterattack.get(weaponIDDefender);
                        parryProbability = Properties.confParryProbability.get(weaponIDDefender);
                        parryKnockbackPower = Properties.confParryKnockbackPower.get(weaponIDDefender);
                    }
                    else {
                        isDefenderPlayer = false;
                        defenderPlayer = null;
                    }
                }
                else {
                    isDefenderPlayer = false;
                    defenderPlayer = null;
                }
                
                
                
                /// *** Critical strike *** //   
                if(criticalProbability > 0) {
                    random = Math.random();

                    if(random <= criticalProbability) {
                        damages += criticalDamages;
                        attacker.sendMessage(ChatColor.GREEN + "Critical strike!"); 
                        if(isDefenderPlayer) defenderPlayer.sendMessage(ChatColor.RED + "Critical strike!"); 
                    }                
                }
                // *** *** //
                
                /// *** Backstab *** //   
                if(backstabMultiplier > 1) {                        
                    if(attackerDirection > 0.5) {
                        damages += (int)(damages * backstabMultiplier);
                        
                        attacker.sendMessage(ChatColor.GREEN + "Backstab!"); 
                        if(isDefenderPlayer) defenderPlayer.sendMessage(ChatColor.RED + "Backstab!"); 
                    }             
                }
                // *** *** //
                               
                /// *** Knockback *** //  
                if(knockbackProbability > 0 && attackerDirection <= 0.5) {
                    random = Math.random();
                    
                    if(random <= knockbackProbability) {                    
                        Abilities.knockback(attacker, defender, knockbackPower);
                        
                        attacker.sendMessage(ChatColor.GREEN + "Knockback!"); 
                        if(isDefenderPlayer) defenderPlayer.sendMessage(ChatColor.RED + "Knockback!");
                    }   
                }
                // *** *** //
                
                
                /******* Linked to defender stats *******/
                
                /// *** Blocking *** //  
                if(isDefenderPlayer && blockingProbability > 0 && attackerDirection <= 0.5) {
                    random = Math.random();
                    
                    if(random <= blockingProbability) {                        
                        damages -= blockingDamages;                        
                        Abilities.knockback(attacker, defender, blockingKnockbackPower);
                        
                        attacker.sendMessage(ChatColor.RED + "Blocking!"); 
                        defenderPlayer.sendMessage(ChatColor.GREEN + "Blocking!");
                    }   
                }
                // *** *** //
                
                /// *** Counterattack *** //  
                if(isDefenderPlayer && counterattackProbability > 0) {
                    random = Math.random();
                    
                    if(random <= counterattackProbability) {                
                        if(Properties.confDamages.get(weaponIDDefender) == -1) damagesCounterattack = 2;
                        else damagesCounterattack = Properties.confDamages.get(weaponIDDefender);
                        
                        attacker.damage(damagesCounterattack);
                        
                        attacker.sendMessage(ChatColor.RED + "Counterattack!");
                        defenderPlayer.sendMessage(ChatColor.GREEN + "Counterattack!");
                    }   
                }
                // *** *** //
                
                /// *** Parry *** //   
                if(isDefenderPlayer && parryProbability > 0 && attackerDirection <= 0.5) {
                    random = Math.random();

                    if(random <= parryProbability) {
                        damages = 0;
                        Abilities.knockback(defender, attacker, parryKnockbackPower);
                        
                        attacker.sendMessage(ChatColor.RED + "Parry!"); 
                        defenderPlayer.sendMessage(ChatColor.GREEN + "Parry!");
                    }                
                }
                // *** *** // 
                
                /// *** Disarm *** //  
                if(isDefenderPlayer && disarmProbability > 0) {                    
                    random = Math.random();
                    
                    if(random <= disarmProbability) {
                        ItemStack itemInHand = defenderPlayer.getInventory().getItemInHand();
                        defenderPlayer.getInventory().removeItem(attacker.getInventory().getItemInHand());

                        isDropable = true;
                        for(int i=9; i<=35; i++) {
                            if(attacker.getInventory().getItem(i).getTypeId() == 0) {
                                attacker.getInventory().setItem(i, itemInHand);
                                isDropable = false;
                                break;
                            }        
                        }

                        if(isDropable) {
                            Location locDrop = defender.getLocation();
                            world.dropItemNaturally(locDrop, itemInHand);
                        } 
                        
                        attacker.sendMessage(ChatColor.GREEN + "Disarm!"); 
                        defenderPlayer.sendMessage(ChatColor.RED + "Disarm!");
                    }
                }
                // *** *** //
                
                
                event.setDamage((damages));
                                
                
                      
                
            }     
        }
        else if (event.getCause() == DamageCause.PROJECTILE) {
            // Gestion de l'arc
            
            EntityDamageByEntityEvent e = (EntityDamageByEntityEvent)event;

            if(e.getDamager() instanceof Arrow) {
                Arrow arrow = (Arrow) e.getDamager();
                if(arrow.getShooter() instanceof Player) {
                    
                    weaponID = 261;
                    Player attacker = (Player) arrow.getShooter(); 
                    PlayerData playerDataAttacker = Users.getPlayerData(attacker); 
                    World world = attacker.getWorld();
                    
                    playerDataAttacker.setAttackedProjectile(currentDate.getTime());
                    
                    Player defenderPlayer = attacker; // Dirty
                    
                    cooldown = Properties.confCooldowns.get(weaponID);
                    
                    canDamageIn = (playerDataAttacker.getUsedWeapon() - currentDate.getTime());
                    
                    // Dirty bugfix for bukkit =(
                    int canDamageInterval = (int)((int)(currentDate.getTime()) - (int)(playerDataAttacker.getClickedAir()));
                                        
                    
                    if(canDamageIn > 0 && canDamageInterval > 100){
                        if(!playerDataAttacker.getSpoutCraftEnabled()) {
                            attacker.sendMessage(ChatColor.RED + "You are too tired, you missed...");
                        }

                        event.setCancelled(true);
                        return;
                    }
                    else if(Properties.cooldown_on_damages_only) {                                          
                        if(!attacker.hasPermission("advancedpvp.nocooldown")) {
                            playerDataAttacker.setUsedWeapon(currentDate.getTime() + cooldown);
                        }
                    }
                    
                    /////////////////////////
                    
                    Entity defender = event.getEntity();
                    attackerDirection = attacker.getLocation().getDirection().dot(defender.getLocation().getDirection());                

                    if(defender instanceof Player) {
                        isDefenderPlayer = true;
                        defenderPlayer = (Player)defender;                    

                        weaponIDDefender = defenderPlayer.getItemInHand().getTypeId();
                        
                        if(Properties.confCooldowns.get(weaponIDDefender) != null) {                        
                            counterattackProbability = Properties.confCounterattack.get(weaponIDDefender);
                            parryProbability = Properties.confParryProbability.get(weaponIDDefender);
                            parryKnockbackPower = Properties.confParryKnockbackPower.get(weaponIDDefender);
                        }
                        else {
                            isDefenderPlayer = false;
                            defenderPlayer = null;
                        }
                    }
                    else {
                        isDefenderPlayer = false;
                        defenderPlayer = null;
                    }                    
                    
                    /////////////////////////
                    
                    cooldown = Properties.confCooldowns.get(weaponID);
                    backstabMultiplier = Properties.confBackstab.get(weaponID);
                    disarmProbability = Properties.confDisarm.get(weaponID);

                    blockingKnockbackPower = Properties.confBlockingKnockbackPower.get(weaponID);
                    blockingProbability = Properties.confBlockingProbability.get(weaponID);
                    blockingDamages = Properties.confBlockingDamages.get(weaponID);

                    knockbackPower = Properties.confKnockbackPower.get(weaponID);
                    knockbackProbability = Properties.confKnockbackProbability.get(weaponID);

                    criticalProbability = Properties.confCriticalProbability.get(weaponID);
                    criticalDamages = Properties.confCriticalExtraDamages.get(weaponID);        

                    
                    if(Properties.confDamages.get(weaponID) == -1) damages = event.getDamage();
                    else damages = Properties.confDamages.get(weaponID);
                    
                    
                    /// *** Critical strike *** //   
                    if(criticalProbability > 0) {
                        random = Math.random();

                        if(random <= criticalProbability) {
                            damages += criticalDamages;
                            attacker.sendMessage(ChatColor.GREEN + "Critical strike!"); 
                            if(isDefenderPlayer) defenderPlayer.sendMessage(ChatColor.RED + "Critical strike!"); 
                        }                
                    }
                    // *** *** //

                    /// *** Knockback *** //  
                    if(knockbackProbability > 0 && attackerDirection <= 0.5) {
                        random = Math.random();

                        if(random <= knockbackProbability) {                    
                            Abilities.knockback(defender, attacker, knockbackPower);

                            attacker.sendMessage(ChatColor.GREEN + "Knockback!"); 
                            if(isDefenderPlayer) defenderPlayer.sendMessage(ChatColor.RED + "Knockback!");
                        }   
                    }
                    // *** *** //


                    /******* Linked to defender stats *******/

                    /// *** Blocking *** //  
                    if(isDefenderPlayer && blockingProbability > 0 && attackerDirection <= 0.5) {
                        random = Math.random();

                        if(random <= blockingProbability) {                        
                            damages -= blockingDamages;                        
                            Abilities.knockback(defender, attacker, blockingKnockbackPower);

                            attacker.sendMessage(ChatColor.RED + "Blocking!"); 
                            defenderPlayer.sendMessage(ChatColor.GREEN + "Blocking!");
                        }   
                    }
                    // *** *** //

                    /// *** Disarm *** //  
                    if(isDefenderPlayer && disarmProbability > 0) {                    
                        random = Math.random();

                        if(random <= disarmProbability) {
                            ItemStack itemInHand = defenderPlayer.getInventory().getItemInHand();
                            defenderPlayer.getInventory().removeItem(attacker.getInventory().getItemInHand());

                            isDropable = true;
                            for(int i=9; i<=35; i++) {
                                if(attacker.getInventory().getItem(i).getTypeId() == 0) {
                                    attacker.getInventory().setItem(i, itemInHand);
                                    isDropable = false;
                                    break;
                                }        
                            }

                            if(isDropable) {
                                Location locDrop = defender.getLocation();
                                world.dropItemNaturally(locDrop, itemInHand);
                            } 

                            attacker.sendMessage(ChatColor.GREEN + "Disarm!"); 
                            defenderPlayer.sendMessage(ChatColor.RED + "Disarm!");
                        }
                    }
                    // *** *** //

                    event.setDamage((damages));                    
                }
            }
        }
    }
}
