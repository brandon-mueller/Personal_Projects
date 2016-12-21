package me.virusbrandon.EnderPearl_Express;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener{
	String gr=ChatColor.GREEN+"",go=ChatColor.GOLD+"",bo=ChatColor.BOLD+"",aq=ChatColor.AQUA+"",kk=ChatColor.MAGIC+"",st=ChatColor.STRIKETHROUGH+"",bu=ChatColor.BLUE+"",wh=ChatColor.WHITE+"",re=ChatColor.RED+"",bl=ChatColor.BLACK+"",un=ChatColor.UNDERLINE+"",ye=ChatColor.YELLOW+"",ga=ChatColor.GRAY+"";
	Random r = new Random();
	
	@EventHandler
	public void blockForm(EntityChangeBlockEvent e){
		e.setCancelled(true);
	}
	
	@EventHandler
	public void join(PlayerJoinEvent e){
		ItemStack is = new ItemStack(Material.ENDER_PEARL,64);
		ItemMeta met = is.getItemMeta();
		met.setDisplayName("✦ EnderPearl Express!!");
		is.setItemMeta(met);
		e.getPlayer().getInventory().setItem(2,is);
	}
	
	@EventHandler
	public void onProjectileLaunch(ProjectileLaunchEvent e) {
		if(e.getEntity().getShooter() instanceof Player){
	    	Player p = (Player) e.getEntity().getShooter();
			if (e.getEntity() instanceof EnderPearl) {
					p.spigot().setCollidesWithEntities(false);
			        e.getEntity().setPassenger(p);
				
		    }
		}
	}
	
	@EventHandler
	public void onTeleport(PlayerTeleportEvent event) {
		if(event.getCause().equals(TeleportCause.ENDER_PEARL)) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void ouch(EntityDamageEvent e){
		if(e.getEntity()instanceof Player){
			Player p = (Player) e.getEntity();
			Location l = p.getLocation();
			if(e.getCause()==DamageCause.SUFFOCATION){
				p.teleport(new Location(p.getWorld(),l.getX(),l.getY()+5,l.getZ()));
				e.setCancelled(true);
			}
		}
	}
	
	@Override
	public void onEnable(){
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
	}
	
	@Override
	public void onDisable(){
	}
}
