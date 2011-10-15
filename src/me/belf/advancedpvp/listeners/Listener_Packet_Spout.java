/*package me.belf.advancedpvp.listeners;

import java.util.HashMap;

import net.minecraft.server.Packet;
import net.minecraft.server.Packet201PlayerInfo;

import org.bukkit.entity.Player;
import org.getspout.spoutapi.packet.listener.PacketListener;
import org.getspout.spoutapi.packet.standard.MCPacket;
import org.getspout.spoutapi.packet.standard.MCPacket18ArmAnimation;

public class Listener_Packet_Spout implements PacketListener {
	
	public static boolean ignorePackets = false;
					
	public boolean checkPacket(Player player, MCPacket mcpacket) {
		if(ignorePackets) return true;                
                
		Packet packet = (Packet) mcpacket.getPacket(); 
		int id = mcpacket.getId();
                
                if(mcpacket instanceof MCPacket18ArmAnimation) {
                    System.out.println("Packet Animation : " + id);
                    return false;
                }
		

		return true;
	}
	
}
*/