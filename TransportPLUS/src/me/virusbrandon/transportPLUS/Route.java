package me.virusbrandon.transportPLUS;

import java.text.DecimalFormat;
import java.util.ArrayList;

import me.virusbrandon.transportutils.LocSetting;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.util.Vector;

public class Route implements Listener{
	private RouteManager man;
	private ArrayList<Player> enroute = new ArrayList<>();
	private Location ori;
	private Location dest;
	private LocSetting lso,lsd;
	private String oriText="";
	private String destText="";
	private DecimalFormat df = new DecimalFormat("#####.##");
	private String re=ChatColor.RED+"",gr=ChatColor.GREEN+"",bo=ChatColor.BOLD+"",go=ChatColor.GOLD+"";
	
	@EventHandler
	public void move(PlayerMoveEvent e){
		Player p = e.getPlayer();
		if(enroute.contains(p)){
			if(man.calcDist(p.getLocation(), dest)>1){
				man.corDir(p, dest);
				if(man.isActionBarOn()){
					man.getMain().gB().sendActionBar(p, gr+bo+"We're "+df.format(man.calcDist(p.getLocation(), dest))+" Blocks Away");
				}
			} else {
				enroute.remove(p);
				p.setGameMode(man.getGamemode());
				p.setVelocity(new Vector(0,.5,0));
				man.getMain().gB().sendActionBar(p," ");
			}
		} else {
			if((man.calcDist(p.getLocation(), ori)<=1)&&(!man.isPlayerBusy(p))){
				GO(p);
			}
		}
	}
	
	@EventHandler
	public void leave(PlayerQuitEvent e){
		Player p = e.getPlayer();
		if(enroute.contains(p)){
			enroute.remove(p);
		}
	}
	
	/**
	 * The Route Object constructor.
	 * 
	 * We'll add the destination
	 * location later.
	 * 
	 * @param ori
	 */
	public Route(RouteManager man, Location ori, String oriText){
		this.man = man;
		this.ori = ori;
		this.oriText = oriText;
	}
	
	/**
	 * The GO Function:
	 * 
	 * Adds A Player To The Enroute
	 * List For This Route And Begins
	 * Moving The Player To The Dest.
	 * 
	 */
	public void GO(Player p){
		enroute.add(p);
		p.setGameMode(GameMode.SPECTATOR);
		p.setVelocity(new Vector(0,.5,0));
		p.sendMessage(gr+"Heading For: "+go+bo+getDestText());
		p.closeInventory();
	}
	
	/**
	 * The isBusy Function:
	 * 
	 * Checks Whether A Player Is
	 * Already In Transit On Another
	 * Route.
	 * 
	 * Prevents Two Or More Routes
	 * Fighting Over A Player (Infinite Loop)
	 * 
	 */
	public boolean isBusy(Player p){
		return enroute.contains(p);
	}
	
	/**
	 * Sets The Origin Location And
	 * Origin Text
	 * 
	 * @param loc
	 * @param oriText
	 */
	public Route setOri(Location loc, String ... oriText){
		this.ori = loc;
		for(int x=2;x<oriText.length;x++){
			this.oriText += oriText[x]+" ";
		}
		if(chk()?reg():false){/* Pointless If */}
		return this;
	}
	
	public Route setOri(Location loc, String oriText){
		this.ori = loc;
		this.oriText = oriText;
		if(chk()?reg():false){/* Pointless If */}
		return this;
	}
	
	/**
	 * Sets The Destination And
	 * Destination Text
	 * 
	 * @param loc
	 * @param destText
	 */
	public Route setDest(Location loc, String ... destText){
		this.dest = loc;
		for(int x=2;x<destText.length;x++){
			this.destText += destText[x]+" ";
		}
		if(chk()?reg():false){/* Pointless If */}
		return this;
	}
	
	public Route setDest(Location loc, String destText){
		this.dest = loc;
		this.destText = destText;
		if(chk()?reg():false){/* Pointless If */}
		return this;
	}
	
	/**
	 * Returns The Origin Location
	 * 
	 */
	public Location getOri(){
		return ori;
	}
	
	/**
	 * Returns The Destination Location
	 * 
	 */
	public Location getDest(){
		return dest;
	}
	
	/**
	 * Returns The Destination Text
	 * 
	 * @return
	 */
	public String getDestText(){
		return destText;
	}
	
	/**
	 * Returns The Origin Text
	 * 
	 * @return
	 */
	public String getOriText(){
		return oriText;
	}
	
	/**
	 * Checks To See If The Required
	 * Fields Are Set For Transportation To
	 * Commence.
	 * 
	 * @return
	 */
	public boolean chk(){
		return((ori!=null)&&(dest!=null)&&(oriText!="")&&(destText!=""));
	}
	
	/**
	 * Registers Events After This Object's
	 * Requirements Are Met.
	 * 
	 * @return
	 */
	public boolean reg(){
		try{
			lso.getTimer().stop();
		}catch(Exception e1){}
		try{
			lsd.getTimer().stop();
		}catch(Exception e1){}
		unreg();
		Bukkit.getServer().getPluginManager().registerEvents(this, man.getMain());
		try{
			this.lso = new LocSetting(this,ori,gr+bo+oriText);
			this.lsd = new LocSetting(this,dest,re+bo+destText);
		}catch(Exception e1){}
		return true;
	}
	
	/**
	 * Unregisters Events
	 * 
	 */
	public void unreg(){
		PlayerMoveEvent.getHandlerList().unregister(this);
		PlayerQuitEvent.getHandlerList().unregister(this);
	}
	
	/**
	 * Returns Reference To The
	 * RouteManager
	 * 
	 */
	public RouteManager rM(){
		return man;
	}
	
	/**
	 * Sets New Reference To The
	 * RouteManager
	 * 
	 */
	public void setrM(RouteManager rm){
		this.man = rm;
	}
	
	/**
	 * Returns The Origin LocSetting
	 * 
	 */
	public LocSetting getOriLS(){
		return lso;
	}
	
	/**
	 * Returns The Destination LocSetting
	 * 
	 */
	public LocSetting getDestLS(){
		return lsd;
	}
	
	/**
	 * Kills A Route Before
	 * Deleting It.
	 * 
	 */
	public void kill(){
		try{
			lso.getTimer().stop();
			lso = null;
		}catch(Exception e1){}
		try{
			lsd.getTimer().stop();
			lsd = null;
		}catch(Exception e1){}
		man = null;
		enroute.clear();
		unreg();
	}
	
	/**
	 * Returns A String Representation Of This Route
	 *
	 */
	public String toString(){
		if(chk()){
			return "\n"+man.locToString(ori)+"\n"+man.locToString(dest)+go+"\nDist Apart: ~"+re+df.format(man.calcDist(ori, dest))+" Blocks\n\n";
		} else {
			return re+((ori==null)?"- Set The Origin Location":"")+((dest==null)?"\nSet The Destination Location":"");
		}
	}
}