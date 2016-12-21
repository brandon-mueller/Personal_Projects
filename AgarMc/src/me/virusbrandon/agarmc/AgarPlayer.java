package me.virusbrandon.agarmc;

import java.util.ArrayList;

import me.virusbrandon.agarlocalapis.SimpleScoreboard;
import me.virusbrandon.agarutils.*;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.*;
import org.bukkit.util.Vector;

public class AgarPlayer implements Listener{
	Runnable timer = new Runnable() {
		public void run() {
			try{
				if(!isAI && isSick()){
					host.getWorld().playSound(host.getLocation(), Sound.ENTITY_WOLF_HOWL, .1f, .5f);
				} else {
					for(Cell c:cells){
						c.checkForMass();
					}
				}
				totalMass = getTotalMass();
				if(getNumCells() != cells.size()){
					setNumCells(cells.size());
					calcPairs();
				}
				for(Cell[] c:pairs){
					if(c[0].isDead()){
						cells.remove(c[0]);
						if(cells.size()<1){
							stop();
							try{
								main.broadcastMessage(main.gB().getOrigin().getWorld().getName(),main.pfx()+wh+c[0].gotKilledBy()+dr+" ➲ ATE ➲ "+wh+getName());
							}catch(Exception e1){}
							new GameOverRoutine(c[0].getHost());
							if(!isAI){
        						host.getWorld().playSound(host.getPlayer().getLocation(), Sound.ENTITY_WOLF_HOWL, .3f, 2f);
    							host.setFlySpeed(0);
        					}
						} else if((cells.size() == 1) && isSick()){
							setSick(false);
						}
						break;
					}
					Double d = c[0].getEntity().getLocation().distance(c[1].getEntity().getLocation());
					if((d<((c[0].getRadius()+c[1].getRadius())))&d>.01){
						if(splitTime>0){
							corDir(c[0].getEntity(),c[1].getEntity().getLocation(),-1,.4);
						} else {
							corDir(c[0].getEntity(),c[1].getEntity().getLocation(),.001,.03);
							if(!c[1].isDead()){
								if(d<=((Math.max(c[0].getRadius(),c[1].getRadius())))){
									cells.add(c[0].merge(c[1]));
									return;
								}
							}	
						}
					}
				}
				if(!isAI){
					host.setFlySpeed(calcSpeed((totalMass/cells.size())));
				}
			}catch(Exception e1){}
			if((cells.size()>1)&(splitTime>0)){
				splitTime--;
			}
			if(!isAI && !host.isFlying()){
				host.setAllowFlight(true);
				host.setFlying(true);
				host.setGameMode(GameMode.CREATIVE);
			}
			if(!isAI && cells.size()>0){
				ChatColor c = cells.get(0).getCellColor();
				main.getBar().sendActionBar(host,toss+c+bo+"Left-Click : Cell-Split"+c+bo+" << "+wh+bo+"Total Mass: "+totalMass+c+bo+" >> "+c+bo+"Eject-Mass : Right-Click");
			}
		}	
	};
	
	/**
	 * Responds To Player Movement
	 * 
	 * AI: This Will Never Fire
	 * 
	 * @param e
	 */
	@EventHandler
	public void move(PlayerMoveEvent e){		
		Player p = e.getPlayer();
		if(p.getUniqueId().toString().equalsIgnoreCase(UUID())){
			calcViewHeight();
			for(Cell c:cells){
				c.checkForMass();
			}
		}
	}
	
	/**
	 * Responds To Inventory Click
	 * 
	 */
	@EventHandler
	public void click(InventoryClickEvent e){
		Player p = (Player)e.getWhoClicked();
		if(main.getPlayersHM().containsKey(p.getUniqueId())){
			e.setCancelled(true);
		}
	}
	
	/**
	 * Responds To The Player Interacting
	 * With Items And Other Objects.
	 * 
	 * @param e
	 */
	@EventHandler
	public void inter(PlayerInteractEvent e){
		String s = e.getAction().toString();
		if((e.getPlayer().getUniqueId().toString().equalsIgnoreCase(UUID()))?(s.contains("RIGHT_CLICK")?eject():(s.contains("LEFT_CLICK")?split(-1):true)):true){}
	}
	
	private Main main;
	private ArrayList<Cell> cells = new ArrayList<>();
	private ArrayList<Cell[]> pairs = new ArrayList<>();
	private String bo = ChatColor.BOLD+"",bl=ChatColor.BLACK+"",wh=ChatColor.WHITE+"",ye=ChatColor.YELLOW+"",dr=ChatColor.DARK_RED+"";
	private Player host;
	private int totalMass;
	private int splitTime = 0;
	private int numCells = 0;
	private int id;
	private long start = System.currentTimeMillis();
	private long end = 0;
	private boolean isSick = false;
	private String toss = "";
	private boolean isAI;
	private Location target;
	private Cell cellTarget;
	private String name;
	
	
	/**
	 * This is the default
	 * AgarPlayer Constructor.
	 * 
	 * It will not do anything...
	 * 
	 * Purely for testing purposes.
	 * 
	 */
	public AgarPlayer(){}
	
	/**
	 * The AgarPlayer Constructor
	 * 
	 * Pass In A Null Player To Trigger
	 * AI Automatic Play
	 * 
	 * @param main
	 * @param host
	 * @param pr
	 */
	public AgarPlayer(Main main, Player host, Pref pr){
		this.main = main;
		Bukkit.getServer().getPluginManager().registerEvents(this, main);
		this.host = host;
		this.isAI = (host == null);
		if(host != null){
			prep();
			host.setVelocity(new Vector(0,-.5,0));
			hidePlayer(host);
			toss = main.gF().draw(" ", 10, "");
			main.gT().sendFullTitle(host, 20, 40, 100, ye+bo+"Agar.MC", ye+"Objective: Survive...");
		} else {
			this.name = "BOT "+main.grabName();
			setTarget(main.gB().rndLoc());
		}
		cells.add(new Cell(main,this, pr));
		start(1);
	}
	
	/**
	 * Returns The Host Player
	 * 
	 * @return
	 */
	public Player getPlayer(){
		return host;
	}
	
	/**
	 * Returns The Name Of This Player
	 * 
	 */
	public String getName(){
		return (isAI()?name:host.getName());
	}
	
	/**
	 * Returns The UUID Of The
	 * Host Player That Owns All
	 * The Cells For This AgarPlayer.
	 * 
	 * @return
	 */
	public String UUID(){
		return (host != null)?host.getUniqueId().toString():"";
	}
	
	/**
	 * Returns All Cells That Are Managed
	 * By This AgarPlayer Object.
	 * 
	 * @return
	 */
	public ArrayList<Cell> getCells(){
		return cells;
	}
	
	/**
	 * Adds A Cell To Be Managed By This AgarPlayer.
	 * 
	 * This Is Normally Called When This Or Another
	 * Of Your Cell Splits, Or When The Game Starts
	 * With Your Host Cell.
	 * 
	 * @param c
	 */
	protected void addCell(Cell c){
		cells.add(c);
	}
	
	public boolean eject(){
		for(Cell c:cells){
			c.eject();
		}
		return true;
	}
	
	public boolean split(int dir){
		ArrayList<Cell> cur = new ArrayList<>();
		cur.addAll(cells);
		for(Cell cc:cur){
			cc.split(dir);
		}
		this.splitTime = (getTotalMass()/2);
		return true;
	}
	
	public void done(boolean shutdown){
		stop();
		for(Cell c: cells){
			c.death();
		}
		if(!shutdown){main.remPlayer(this);}
		if(!isAI()){
			host.getInventory().clear();
			host.teleport(main.gB().getLobby());
			showPlayer(host);
			host.setGameMode(GameMode.SURVIVAL);
			host.setScoreboard(new SimpleScoreboard("").getBlankScoreboard());
		} else {
			setEnd(System.currentTimeMillis());
		}
		unReg();
	}
	
	/**
	 * Returns The Start Time For This
	 * AgarPlayer.
	 * 
	 * @return
	 */
	public long getStart(){
		return start;
	}
	
	/**
	 * Sets THe End Time For This
	 * AgarPlayer, So We Can Determine
	 * How Long You Survived.
	 * 
	 * @param time
	 * @return
	 */
	public AgarPlayer setEnd(long time){
		this.end = time;
		return this;
	}
	
	/**
	 * Returns The End Time For
	 * This AgarPlayer.
	 * 
	 * @return
	 */
	public long getEnd(){
		return end;
	}
	
	/**
	 * Determines This Cell's Speed
	 * Based On Current Mass.
	 * @return
	 */
	private float calcSpeed(int mass){
		float f = (.12f-(mass*.00006f));
		return (f>.025f?f:.025f);
	}
	
	/**
	 * Returns The Current Target
	 * ( Only Used By AI )
	 * 
	 */
	public Location getTarget(){
		return target;	
	}
	
	/**
	 * Sets A New Target
	 * ( Only Used By AI )
	 * 
	 */
	public void setTarget(Location target){
		this.target = (target==null)?main.gB().rndLoc():target;
	}
	
	/**
	 * Returns The Cell Target
	 * If There Is One
	 * 
	 */
	public Cell getCellTarget(){
		return cellTarget;
	}
	
	/**
	 * Sets CellTarget
	 * 
	 */
	public void setCellTarget(Cell cellTarget){
		this.cellTarget = cellTarget;
	}
	
	/**
	 * Returns Split Time
	 * 
	 */
	public int getSplitTime(){
		return splitTime;
	}
	
	/**
	 * Sets Split Time
	 * 
	 */
	public void setSplitTime(int splitTime){
		this.splitTime = splitTime;
	}
	
	/**
	 * Returns The numCells Field
	 * 
	 */
	protected int getNumCells(){
		return numCells;
	}
	
	/**
	 * Sets The numCells Field
	 * 
	 */
	protected void setNumCells(int numCells){
		this.numCells = numCells;
	}
	
	/**
	 * Reforms The List Of Every Possible
	 * Pair Of Cells.
	 * 
	 */
	private void calcPairs(){
		pairs.clear();
		for(int x=0;x<cells.size();x++){
			for(int y=0;y<cells.size();y++){
				pairs.add(new Cell[]{cells.get(x),cells.get(y)});
			}
		}
	}
	
	/**
	 * Determines The View Height Based
	 * On Total Mass.
	 * 
	 */
	private void calcViewHeight(){
		if(host.getUniqueId().toString().equalsIgnoreCase(UUID())){
			Location l = host.getLocation();
			int y = (main.gB().baseHeight()+(int)(getTotalRadius()*1.75));
			if((l.getY()>(y+1))|((l.getY()<(y-1)))){
				host.setVelocity(new Vector(0,(l.getY()>(y+1))?-.4:(l.getY()<(y-1))?.4:0,0));
			}
		}
	}
	
	/**
	 * Returns Reference To Main.
	 * 
	 */
	public Main getMain(){
		return main;
	}
	
	/**
	 * Determines Total Radius Across
	 * All Of Your Active Cells
	 * 
	 */
	private double getTotalRadius(){
		double rad = 0;
		for(Cell c:cells){
			rad+=c.getRadius();
		}
		return rad;
	}
	
	/**
	 * Determines Total Mass Across
	 * All Of Your Active Cells
	 * 
	 */
	public int getTotalMass(){
		int mass = 0;
		for(Cell c:cells){
			mass+=c.getMass();
		}
		return mass;
	}
	
	/**
	 * Returns Whether The Host Cell
	 * Is Vulnerable To Germs Yet.
	 * 
	 */
	protected boolean canGetSick(){
		return ((totalMass>250)&&!isSick());
	}
	
	/**
	 * Returns Whether Or Not This
	 * Player Is An AI Player
	 * 
	 */
	public boolean isAI(){
		return isAI;
	}
	
	/**
	 * Returns Whether The Host Cell
	 * Is Ill From Eating Germs.
	 * 
	 */
	public boolean isSick(){
		return isSick;
	}
	
	/**
	 * Sets The Host Cell
	 * Ill Or Not.
	 * 
	 */
	protected void setSick(boolean isSick){
		if(!isAI && isSick){
			host.getWorld().playSound(host.getLocation(), Sound.ENTITY_LIGHTNING_IMPACT, 1, 1);
		}
		this.isSick = isSick;
	}
	
	
	/**
	 * Hides The Player From Everyone.
	 * 
	 */
	private void hidePlayer(Player p){
		for(Player pp:Bukkit.getOnlinePlayers()){
			pp.hidePlayer(p);
		}
	}
	
	/**
	 * Shows The Player To Everyone.
	 * 
	 */
	private void showPlayer(Player p){
		for(Player pp:Bukkit.getOnlinePlayers()){
			pp.showPlayer(p);
		}
	}
	
	/**
	 * The Directional Correction Function:
	 * (For Cells)
	 * 
	 * @param source
	 * @param target
	 * @param inOrOut
	 * @param strength
	 */
	protected void corDir(Bat source, Location target, double inOrOut, double strength){
		Location l = source.getLocation();
		Vector v = l.toVector().subtract(target.toVector()).multiply(inOrOut).normalize();
		source.setVelocity(new Location(l.getWorld(),l.getX(),l.getY(),l.getZ(),(180-(float)Math.toDegrees(Math.atan2(v.getX(), v.getZ()))),0).getDirection().multiply(strength));
	}
	
	/**
	 * The Directional Correction Function:
	 * (For Players)
	 * 
	 * @param source
	 * @param target
	 * @param inOrOut
	 * @param strength
	 */
	public void corDir(Player source, Location target, double inOrOut){
		Location l = source.getLocation();
		Vector v = l.toVector().subtract(target.toVector()).multiply(inOrOut).normalize();
		source.setVelocity(new Location(l.getWorld(),l.getX(),l.getY(),l.getZ(),(180-(float)Math.toDegrees(Math.atan2(v.getX(), v.getZ()))),(90-(float)Math.toDegrees(Math.acos(v.getY())))).getDirection());
	}
	
	
	/**
	 * Prepares The Player
	 * For The Game.
	 * 
	 */
	@SuppressWarnings("deprecation")
	private void prep(){
		Inventory i = host.getInventory();
		for(int x=0;x<36;x++){
			main.gF().setUpItem(i, x, new ItemStack(Material.STAINED_GLASS_PANE,1,DyeColor.SILVER.getDyeData())," ",bl+"#AGAR");
		}
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
	 * Unregisters Events
	 * 
	 */
	private void unReg(){
		PlayerInteractEvent.getHandlerList().unregister(this);
		PlayerMoveEvent.getHandlerList().unregister(this);
		InventoryClickEvent.getHandlerList().unregister(this);
	}
}

/*
 * © 2016 Brandon Mueller
 * DO NOT DE-COMPILE THIS SOFTWARE OR ATTEMPT ANY FORM OF REVERSE ENGINEERING!
 */
