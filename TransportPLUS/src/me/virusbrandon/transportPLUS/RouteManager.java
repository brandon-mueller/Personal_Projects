package me.virusbrandon.transportPLUS;

import java.io.File;
import java.util.ArrayList;

import me.virusbrandon.exceptions.EpicDoucheBaggeryException;
import me.virusbrandon.localapis.GUIFactory;
import me.virusbrandon.transportutils.HoloManager;
import me.virusbrandon.ui.ListerWindow;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.util.Vector;

public class RouteManager implements Listener{
	private ArrayList<Route> routes = new ArrayList<>();
	private Main main;
	private HoloManager hm;
	private YamlConfiguration config;
	private String wh=ChatColor.WHITE+"",bo=ChatColor.BOLD+"",go=ChatColor.GOLD+"",re=ChatColor.RED+"",gr=ChatColor.GREEN+"";
	private GUIFactory fact;
	private ListerWindow lis;
	private boolean actionBarOn = true;
	private GameMode destGM = GameMode.SURVIVAL;
	
	@EventHandler
	public void click(InventoryClickEvent e){
		Player p = (Player)e.getWhoClicked();
		try{
			if(p.getOpenInventory().getTopInventory().getTitle().contains("[TP]")){
				e.setCancelled(true);
				for(Route r:routes){
					if(r.isBusy(p)){
						return; /* This Player Is Already In Transit To Somewhere Else, We're Done Here */
					}
				}
				routes.get(main.psI(e.getCurrentItem().getItemMeta().getDisplayName())).GO(p);
			}
		}catch(Exception e1){}
	}
	
	public RouteManager(Main main){
		this.main = main;
		this.hm = new HoloManager();
		this.fact = new GUIFactory();
		this.lis = new ListerWindow(this);
		Bukkit.getServer().getPluginManager().registerEvents(this, main);
	}
	
	/**
	 * Add Route Function:
	 * 
	 * Adds A Route For Users
	 * To Traverse Across
	 * 
	 * Must Set Dest Location And
	 * Text For This Shit To Be Usable.
	 * 
	 * @param l
	 * @param s
	 */
	public void addRoute(Location l, String ... ss){
		String s = "";
		for(int x=1;x<ss.length;x++){
			s+=ss[x]+" ";
		}
		routes.add(new Route(this,l,s));
	}
	
	/**
	 * Remove Route Function:
	 * 
	 * Removes A Route From
	 * The Software.
	 * 
	 * @param i
	 */
	public void delRoute(int i){
		try{
			routes.get(i).kill();
			routes.remove(i);
		}catch(Exception e1){
			/* Catch Exception When Douchebags Provided Invalid Route ID */
		}
	}
	
	/**
	 * Returns All Routes
	 * 
	 */
	public ArrayList<Route> getRoutes(){
		return routes;
	}
	
	/**
	 * Returns A String Representation
	 * Of All Available Routes
	 * 
	 */
	public String showRoutes(){
		String s=(routes.size()>0?"":wh+bo+"\nNo Routes To Display!");
		for(int x=0;x<routes.size();x++){
			s+=wh+"\nRoute #"+x+" \n"+routes.get(x).toString();
		}
		return s;
	}
	
	/**
	 * Returns Reference To Main
	 * 
	 * @return
	 */
	public Main getMain(){
		return main;
	}
	
	/**
	 * Returns If A Player Is Busy
	 * On Another Route
	 * 
	 */
	public boolean isPlayerBusy(Player p){
		for(Route r:routes){
			if(r.isBusy(p)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Returns The HoloManager
	 * 
	 */
	public HoloManager hM(){
		return hm;
	}
	
	/**
	 * Direction Correction Function.
	 * 
	 * Moves A Player Towards A Target
	 * Location.
	 * 
	 * @param source
	 * @param target
	 */
	protected void corDir(Player source, Location target) {
		Location l = source.getLocation();
		Vector v = l.toVector().subtract(target.toVector()).normalize();
	    source.setVelocity(new Location(l.getWorld(),l.getX(),l.getY(),l.getZ(),(180-(float)Math.toDegrees(Math.atan2(v.getX(), v.getZ()))),(90-(float)Math.toDegrees(Math.acos(v.getY())))).getDirection().multiply(calcDist(l,target)/20));
	}
	
	/**
	 * The Distance Calculation Function:
	 * 
	 * Will Be Used In Both Handling Player
	 * Joining Events and Player In Game
	 * Death Events.
	 * 
	 * @param loc1
	 * @param loc2
	 * @return
	 */
	public double calcDist(Location loc1, Location loc2){
		return Math.sqrt(Math.pow(loc1.getBlockX()-loc2.getBlockX(),2)+Math.pow(loc1.getBlockY()- 
		loc2.getBlockY(),2)+Math.pow(loc1.getBlockZ()-loc2.getBlockZ(),2));
	}
	
	/**
	 * Returns The Lister Window
	 * User Interface
	 * 
	 */
	public Inventory getUi(){
		return lis.ui();
	}
	
	/**
	 * Returns The GUI Factory Instance
	 * So We Can Manage User Interfaces
	 * 
	 */
	public GUIFactory gF(){
		return fact;
	}
	
	/**
	 * Returns A Location toString
	 * 
	 */
	public String locToString(Location l){
		return go+"World: "+re+l.getWorld().getName()+gr+", X: "+re+l.getBlockX()+gr+", Y: "+re+l.getBlockY()+gr+", Z: "+re+l.getBlockZ();
	}
	
	/**
	 * Returns Whether The ActionBar
	 * Should Show Up Or Not.
	 * 
	 */
	public boolean isActionBarOn(){
		return actionBarOn;
	}
	
	/**
	 * Sets ActionBar Enabled
	 * 
	 */
	public void setActionBarOn(boolean on){
		this.actionBarOn = on;
	}
	
	/**
	 * Returns The Gamemode That A Player
	 * Will Be Set To Upon Arrival At A
	 * Route Destination
	 * 
	 */
	public GameMode getGamemode(){
		return destGM;
	}
	
	/**
	 * Sets The Gamemode That A Player
	 * Will Be Set To Upon Arrival At A
	 * Route Destination
	 * 
	 * @param gm
	 */
	public void setDestGamemode(GameMode gm){
		this.destGM = gm;
	}
	
	public void load() throws EpicDoucheBaggeryException{
		File file = new File(main.getDataFolder(),"config.yml");
		try{
			if(!file.exists()){
				file.createNewFile();
			}
		}catch(Exception e1){}
		this.config = YamlConfiguration.loadConfiguration(file);
		for(int x=0;x<config.getInt("Total");x++){
			routes.add(new Route(this,null,""));
			routes.get(x).setOri((Location)config.get("Routes."+x+".ORIGIN"), config.getString("Routes."+x+".ORIGINTXT")).setDest((Location)config.get("Routes."+x+".DEST"), config.getString("Routes."+x+".DESTTXT")).setrM(this);
		}
		setActionBarOn(config.getBoolean("ActionBarEnabled"));
		try{
			setDestGamemode(GameMode.valueOf(config.getString("DestGamemode")));
		}catch(Exception e1){
			throw new EpicDoucheBaggeryException("Try Putting A Gamemode That Actually Exists In The Config M8");
			/* Caused By Epic Douche-Baggery In The Config */
		}
	}
	
	public void save(){
		hm.remAllHolos();
		File file = new File(main.getDataFolder(),"config.yml");
		try{
			if(!file.exists()){
				file.createNewFile();
			}
		}catch(Exception e1){}
		this.config = YamlConfiguration.loadConfiguration(file);
		config.set("Total", routes.size());
		config.set("ActionBarEnabled", actionBarOn);
		config.set("DestGamemode", GameMode.ADVENTURE.name());
		for(int x=0;x<routes.size();x++){
			Route r = routes.get(x);
			r.kill();
			config.set("Routes."+x+".ORIGIN",r.getOri());
			config.set("Routes."+x+".DEST",r.getDest());
			config.set("Routes."+x+".ORIGINTXT",r.getOriText());
			config.set("Routes."+x+".DESTTXT",r.getDestText());
		}
		try{
			config.save(file);
		}catch(Exception e1){}
	}
}
