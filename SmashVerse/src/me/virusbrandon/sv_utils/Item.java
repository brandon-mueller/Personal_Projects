package me.virusbrandon.sv_utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.inventivegames.hologram.HologramAPI;
import me.virusbrandon.energydrink.EnergyDrink;
import me.virusbrandon.game.Arena;
import me.virusbrandon.game.ArenaDriver;
import me.virusbrandon.smashverse.Main;
import me.virusbrandon.sv_utils.ArenaStatus.Status;

public class Item implements Listener{
	Runnable timer = new Runnable() {
		public void run() {
			try{
				itemUsed();
			}catch(Exception e1){}
		}	
	};
	
	Runnable timer2 = new Runnable() {
		public void run() {
			try{
				p.playSound(p.getLocation(),Sound.ENTITY_VILLAGER_DEATH,1,f);
				f+=.1f;
				p.setExp((float)(f/1.5));
				if(f>=2.0){
					p.setExp(0);
					stop1();
				}
			}catch(Exception e1){}
		}	
	};
	
	private Arena ar;
	private ArrayList<HoloObject> holos = new ArrayList<>();
	private Location ll;
	private int id,id2;
	private float f = .5f;
	private Player p;
	private String wh=ChatColor.WHITE+"",bo=ChatColor.BOLD+"",ul=ChatColor.UNDERLINE+"",gr=ChatColor.GREEN+"",rs=ChatColor.RESET+"",re=ChatColor.RED+"";
	
	@EventHandler
	public void move(PlayerMoveEvent e){
		Player p = e.getPlayer();
		if(ar.getPlayers().contains(p)){
			switch(ar.getStatus()){
			case IN_GAME:
			case SUDDENDEATH:
				if((ar.getMan().calcDist(ll, p.getLocation())<2)&&(!ar.findPlayer(p).isKOd())){
					this.p = p;
					start1(1);
					itemUsed(p);
				}
			break;
			default:
			}
		}
	}
	
	public Item(Arena ar, Location l){
		this.ar = ar;
		double d = l.getY();
		String[] s = ar.getMan().Itemtext();
		for(int x=0;x<s.length;x++){
			if(x<=(s.length/2)){
				this.ll = new Location(l.getWorld(),l.getX(),d+(.3*x),l.getZ());
			}
			holos.add(ar.getMan().getMain().hM().addHolo(HologramAPI.createHologram(new Location(l.getWorld(),l.getX(),d+(.3*x),l.getZ()),s[(s.length-1)-x]),140,true));
		}
		Bukkit.getServer().getPluginManager().registerEvents(this, ar.getMan().getMain());
		start(140);
	}
	
	public void itemUsed(Player ... p){
		try{
			unreg();
			for(HoloObject h:holos){
				ar.getMan().getMain().hM().remHolo(h);
			}
			ar.remItem(this);
			ItemStack i = ar.getMan().grabItem();
			if(!i.getType().isEdible()&(i.getType()!=Material.MILK_BUCKET)){
				p[0].getInventory().clear();
				ItemMeta m = disp(i.getType(),i.getItemMeta());
				for(int x=0;x<9;x++){
					m.setDisplayName(m.getDisplayName()+rs+" ");
					i.setItemMeta(m);
					p[0].getInventory().addItem(i);
				}
			} else {
				if(i.getType().isEdible()){
					if(ar.getStatus()!=Status.SUDDENDEATH){
	    				p[0].sendMessage(ar.getMan().getMain().getPfx()+gr+bo+"Healed!! "+((ar.findPlayer(p[0]).getDamagePercent()>=20)?"[ -20% ]":"[ -"+ar.findPlayer(p[0]).getDamagePercent()+"% ]"));
						ar.getMan().getMain().hM().addHolo(HologramAPI.createHologram(p[0].getLocation(), gr+bo+"Health Recovery -20%"),false);
	    				new ArenaDriver(ar.findPlayer(p[0]),3,20).start(5);
	    			} else {
	    				p[0].sendMessage(ar.getMan().getMain().getPfx()+re+"We're In Sudden Death -"+re+bo+" Healing BLOCKED!");
	    			}
				} else if(i.getType()==Material.MILK_BUCKET) {
					ar.getMan().getMain().hM().addHolo(HologramAPI.createHologram(p[0].getLocation(), gr+bo+"Speed Boost!"),false);
					new EnergyDrink(ar,p[0]).useWeapon();
				}
			}
			
		}catch(Exception e1){}
	}
	
	/**
	 * Powerup Item Name And Descriptions
	 * 
	 * @param m
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private ItemMeta disp(Material m,ItemMeta me){
		List<String>l = new ArrayList<>();
		YamlConfiguration ids = ar.getMan().getIdesc();
		me.setDisplayName(wh+bo+ul+ids.getString(m.name()+".N"));
		for(String s:(List<String>)ids.get(m.name()+".L")){
			l.add(wh+s);
		}
		me.setLore(l);
		return me;
	}
	
	public void unreg(){
		PlayerMoveEvent.getHandlerList().unregister(this);
		stop();
	}
	
	/**
	 * Starts The Timer
	 * 
	 * @param delay
	 * 
	 * 
	 */
	private void start(int delay){
		id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(Main.class), timer, delay, delay);
	}

	/**
	 * Stops The Timer
	 * 
	 * 
	 */
	private void stop(){
		Bukkit.getScheduler().cancelTask(id);
	}
	
	/**
	 * Starts The Timer
	 * 
	 * @param delay
	 * 
	 * 
	 */
	private void start1(int delay){
		id2 = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(Main.class), timer2, delay, delay);
	}

	/**
	 * Stops The Timer
	 * 
	 * 
	 */
	private void stop1(){
		Bukkit.getScheduler().cancelTask(id2);
	}
	
	/*
	 * © 2016 Brandon Mueller
	 * DO NOT DE-COMPILE THIS SOFTWARE OR ATTEMPT ANY FORM OF REVERSE ENGINEERING!
	 */
}