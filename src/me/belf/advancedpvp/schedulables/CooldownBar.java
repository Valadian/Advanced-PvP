package me.belf.advancedpvp.schedulables;

import java.util.Date;
import java.sql.Timestamp;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import org.getspout.spoutapi.event.screen.ButtonClickEvent;
import org.getspout.spoutapi.event.screen.ScreenListener;
import org.getspout.spoutapi.event.spout.SpoutCraftEnableEvent;
import org.getspout.spoutapi.event.spout.SpoutListener;
import org.getspout.spoutapi.gui.Color;
import org.getspout.spoutapi.gui.Container;
import org.getspout.spoutapi.gui.ContainerType;
import org.getspout.spoutapi.gui.Screen;
import org.getspout.spoutapi.gui.GenericGradient;
import org.getspout.spoutapi.gui.GenericContainer;
import org.getspout.spoutapi.gui.RenderPriority;
import org.getspout.spoutapi.player.SpoutPlayer;
import org.getspout.spoutapi.gui.WidgetAnchor;

import me.belf.advancedpvp.AdvancedPvP;
import me.belf.advancedpvp.Schedulable;
import me.belf.advancedpvp.Users;
import me.belf.advancedpvp.datatypes.PlayerData;


public class CooldownBar extends Schedulable {

	private Player player;
	private PlayerData playerData;
        
	
	public CooldownBar(Player player, PlayerData playerData) {
            this.player = player;
            this.playerData = playerData;
	}        
        
	@Override
	protected void tick() {
            Date currentDate = new Date();
            
            double initialInterval = (double)(playerData.getUsedWeapon() - playerData.getstartCD());
            double currentInterval = (double)(playerData.getUsedWeapon() - currentDate.getTime());
            
            double percent = 1 - (currentInterval/initialInterval);
            if(percent > 1) percent = 1;
            if(percent < 0) percent = 0;
            
            if(playerData.getSpoutCraftEnabled()) {                
                
                SpoutPlayer sPlayer = (SpoutPlayer) player;
                Screen screen = sPlayer.getMainScreen();

                int newWidth = (int)(50 * percent);

                GenericGradient sGreenBar = (GenericGradient) screen.getWidget(playerData.getGreenBarId());
                GenericGradient sRedBar = (GenericGradient) screen.getWidget(playerData.getRedBarId());

                sGreenBar.setVisible(true);
                sRedBar.setVisible(true);

                sGreenBar.setWidth(newWidth);
                sRedBar.setWidth(50 - sGreenBar.getWidth()).setX(sGreenBar.getWidth() - 25);

                sGreenBar.setDirty(true);
                sRedBar.setDirty(true);
                
                if(percent >= 1) {
                    sGreenBar.setVisible(false);                
                    sRedBar.setVisible(false);                
                    sGreenBar.setWidth(0);
                }
            }

            if(percent >= 1) {
                
                if(!playerData.getSpoutCraftEnabled()) {
                    player.sendMessage(ChatColor.GREEN + "Ready to fight!");
                }
                
                this.cancel();
            }
	}
}
