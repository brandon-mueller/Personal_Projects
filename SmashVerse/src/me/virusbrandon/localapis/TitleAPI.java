package me.virusbrandon.localapis;

import java.lang.reflect.*;
import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import net.minecraft.server.v1_10_R1.IChatBaseComponent;
import net.minecraft.server.v1_10_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_10_R1.PacketPlayOutTitle;
import net.minecraft.server.v1_10_R1.PacketPlayOutTitle.EnumTitleAction;
import net.minecraft.server.v1_10_R1.PlayerConnection;

public class TitleAPI {
	
	public TitleAPI() {}
	
	/**
	 * Call This Directly To Send A Title And Or
	 * SubTitle To A Single Player
	 * 
	 * @param p
	 * @param fadeIn
	 * @param stay
	 * @param fadeOut
	 * @param title
	 * @param subtitle
	 */
	public void sendTitle(Player p, Integer fadeIn, Integer stay, Integer fadeOut, String title, String subtitle) {
		PlayerConnection connection = ((CraftPlayer)p).getHandle().playerConnection;
         PacketPlayOutTitle packetPlayOutTimes = new PacketPlayOutTitle(EnumTitleAction.TIMES, null, fadeIn.intValue(), stay.intValue(), fadeOut.intValue());
         connection.sendPacket(packetPlayOutTimes);
         if (subtitle != null)
         {
           subtitle = subtitle.replaceAll("%player%", p.getDisplayName());
           subtitle = ChatColor.translateAlternateColorCodes('&', subtitle);
           IChatBaseComponent titleSub = ChatSerializer.a("{\"text\": \"" + subtitle + "\"}");
           PacketPlayOutTitle packetPlayOutSubTitle = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, titleSub);
           connection.sendPacket(packetPlayOutSubTitle);
         }
         if (title != null)
         {
           title = title.replaceAll("%player%", p.getDisplayName());
           title = ChatColor.translateAlternateColorCodes('&', title);
           IChatBaseComponent titleMain = ChatSerializer.a("{\"text\": \"" + title + "\"}");
           PacketPlayOutTitle packetPlayOutTitle = new PacketPlayOutTitle(EnumTitleAction.TITLE, titleMain);
           connection.sendPacket(packetPlayOutTitle);
         }
            
     }
     
	/**
	 * Send Title And Or SubTitle To Several Players
	 * 
	 * @param p
	 * @param fadeIn
	 * @param stay
	 * @param fadeOut
	 * @param title
	 * @param subTitle
	 */
     public void sendTitle(Collection <? extends Player> p,int fadeIn, int stay, int fadeOut, String title, String subTitle){
    	 for(Player pp:p){
    		 sendTitle(pp,fadeIn,stay,fadeOut,title,subTitle);
    	 }
     }
     
     
     /**
	   * Send A Tab Title To A Particular Player
	   * @param player
	   * @param header
	   * @param footer
	   */
	  public void sendTabTitle(Player player, String header, String footer){
		  if (header == null) {
			  header = "";
		  }
		  header = ChatColor.translateAlternateColorCodes('&', header);
		  if (footer == null) {
			  footer = "";
		  }
		  footer = ChatColor.translateAlternateColorCodes('&', footer);
		  header = header.replaceAll("%player%", player.getDisplayName());
		  footer = footer.replaceAll("%player%", player.getDisplayName());
		  try{
		      Object tabHeader = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", new Class[] { String.class }).invoke(null, new Object[] { "{\"text\":\"" + header + "\"}" });
		      Object tabFooter = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", new Class[] { String.class }).invoke(null, new Object[] { "{\"text\":\"" + footer + "\"}" });
		      Constructor<?> titleConstructor = getNMSClass("PacketPlayOutPlayerListHeaderFooter").getConstructor(new Class[] { getNMSClass("IChatBaseComponent") });
		      Object packet = titleConstructor.newInstance(new Object[] { tabHeader });
		      Field field = packet.getClass().getDeclaredField("b");
		      field.setAccessible(true);
		      field.set(packet, tabFooter);
		      sendPacket(player, packet);
		  }catch (Exception ex){
			  ex.printStackTrace();
		  }
	  }
	  
	  /**
	   * Send A Tab Title To A List Of Players
	   * Such As Bukkit.getOnlinePlayers
	   * @param player
	   * @param header
	   * @param footer
	   */
	  public void sendTabTitle(Collection<? extends Player> pp,String header, String footer){
		  for(Player p:pp){
			  sendTabTitle(p,header,footer);
		  }
	  }
	  
	  
	  /* EXTREMELY IMPORTANT SHIT -- DON'T TOUCH! */
		private void sendPacket(Player player, Object packet){
		   try{
			   Object handle = player.getClass().getMethod("getHandle", new Class[0]).invoke(player, new Object[0]);
			   Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
			   playerConnection.getClass().getMethod("sendPacket", new Class[] { getNMSClass("Packet") }).invoke(playerConnection, new Object[] { packet });
		   }catch (Exception e){
			   e.printStackTrace();
		   }
		}
		  
		private static Class<?> getNMSClass(String name){
			String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
			try{
				return Class.forName("net.minecraft.server." + version + "." + name);
			} catch (ClassNotFoundException e){
				e.printStackTrace();
			}
			return null;	
		}
		/* EXTREMELY IMPORTANT SHIT -- DON'T TOUCH! */
}
