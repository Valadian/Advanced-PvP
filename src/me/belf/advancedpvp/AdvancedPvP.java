package me.belf.advancedpvp;

import java.util.UUID;
import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.util.config.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.scheduler.BukkitScheduler;


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



import me.belf.advancedpvp.listeners.Listener_Player;
import me.belf.advancedpvp.listeners.Listener_Entity;
//import me.belf.advancedpvp.listeners.Listener_Packet_Spout;

import me.belf.advancedpvp.Users;
import me.belf.advancedpvp.datatypes.PlayerData;


public class AdvancedPvP extends JavaPlugin{
    private Logger log;
    private PluginDescriptionFile description;
    Properties config = new Properties();
    

    public static String prefix;
    private Listener_Player listenerPlayer;
    private Listener_Entity listenerEntity;  


    @Override
    public void onEnable(){
        log = Logger.getLogger("Minecraft");
        description = getDescription();
        prefix = "["+description.getName()+"] ";

        log("loading "+description.getFullName());


        config.checkProperties();

        listenerPlayer = new Listener_Player(this);
        listenerEntity = new Listener_Entity(this);


        getServer().getPluginManager().registerEvent(Type.PLAYER_ITEM_HELD, listenerPlayer, Priority.Normal, this);
        getServer().getPluginManager().registerEvent(Type.PLAYER_ANIMATION, listenerPlayer, Priority.Normal, this);
        getServer().getPluginManager().registerEvent(Type.ENTITY_DAMAGE, listenerEntity, Priority.Normal, this);
        getServer().getPluginManager().registerEvent(Type.PLAYER_INTERACT, listenerPlayer, Priority.Low, this);
        getServer().getPluginManager().registerEvent(Type.PROJECTILE_HIT, listenerEntity, Priority.Low, this);

        getServer().getPluginManager().registerEvent(Type.CUSTOM_EVENT, new spoutCraftListener(), Priority.Normal, this);

        //SpoutManager.getPacketManager().addListener(18, listenerPacketSpout);            
                
        
        Schedulable.setAdvancedPvP(this);


    }

    @Override
    public void onDisable(){
        log("disabled "+description.getFullName());

    }
    
    public void log(String message){
        log.info(prefix+message);
    }


    public class spoutCraftListener extends SpoutListener {

        @Override
        public void onSpoutCraftEnable(SpoutCraftEnableEvent event) {

            Player player = event.getPlayer();
            PlayerData playerData = Users.getPlayerData(player);
            SpoutPlayer sPlayer = (SpoutPlayer) player;
            
            if(sPlayer.isSpoutCraftEnabled()) {
                
                playerData.setSpoutCraftEnabled(true);

                Screen screen = sPlayer.getMainScreen();
                //screen.removeWidgets(AdvancedPvP.this);

                Color green = new Color(0f, 1f, 0f, 1f);
                Color red = new Color(1f, 0f, 0f, 1f);

                GenericGradient sRedBar = new GenericGradient();
                GenericGradient sGreenBar = new GenericGradient();

                sRedBar.setBottomColor(red).setTopColor(red).setWidth(50).setHeight(2).setX(-25).setY(10).setAnchor(WidgetAnchor.TOP_CENTER).setPriority(RenderPriority.Highest).setVisible(false).setDirty(true);
                sGreenBar.setBottomColor(green).setTopColor(green).setWidth(0).setHeight(2).setX(-25).setY(10).setAnchor(WidgetAnchor.TOP_CENTER).setPriority(RenderPriority.Highest).setVisible(false).setDirty(true);

                screen.attachWidget(AdvancedPvP.this, sRedBar);
                screen.attachWidget(AdvancedPvP.this, sGreenBar);


                UUID sGreenBarId = sGreenBar.getId();
                UUID sRedBarId = sRedBar.getId();

                Users.getPlayerData(player).setGreenBarId(sGreenBarId);
                Users.getPlayerData(player).setRedBarId(sRedBarId);
            }

        }

    }
	
}
