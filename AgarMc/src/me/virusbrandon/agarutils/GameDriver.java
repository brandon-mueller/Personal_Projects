package me.virusbrandon.agarutils;

import java.util.ArrayList;

import org.bukkit.*;
import org.bukkit.entity.Player;

import me.virusbrandon.agarlocalapis.SimpleScoreboard;
import me.virusbrandon.agarmc.*;


public class GameDriver{
	private Main main;
	private ArrayList<Cell> allCells = new ArrayList<>();
	private int id;
	private SimpleScoreboard sb;
	private String wh=ChatColor.WHITE+"",bo=ChatColor.BOLD+"",gr=ChatColor.GREEN+"",re=ChatColor.RED+"";
	
	Runnable timer = new Runnable() {
		@SuppressWarnings("serial")
		public void run() {
			ArrayList<Player> pl = new ArrayList<Player>(){{addAll(Bukkit.getOnlinePlayers());}};
			try{sb.reset();}catch(Exception e1){}
			sb = new SimpleScoreboard(wh+bo+" -+- Leaderboard -+- ");
			if(main.getPlayersAL().size()>0){
				ArrayList<AgarPlayer> sp = main.sortPlayers();
				for(int x=0;x<((sp.size()>10)?10:sp.size());x++){
					int xx = sp.get(x).getCells().size();
					sb.add((((x+1)<10)?"0":"")+(x+1)+". "+((xx>0)?sp.get(x).isSick()?sp.get(x).getCells().get(0).getCellColor()+"░ ":sp.get(x).getCells().get(0).getCellColor()+"█ ":re+"DEAD ")+gr+sp.get(x).getName()+wh+" - "+gr+sp.get(x).getTotalMass());
				}
				sb.build();
				sb.send(pl);
			} else {
				for(Player p:pl){
					p.setScoreboard(sb.getBlankScoreboard());
				}
			}
		}
	};
	
	/**
	 * Game Driver Constuctor
	 * 
	 */
	public GameDriver(Main main){
		this.main = main;
		start(20);
	}
	
	public void tCell(Cell c){
		allCells = main.getAllCells();
		for(Cell cc:allCells){
			if(!c.UUID().equalsIgnoreCase(cc.UUID())){
				if(c.getEntity().getLocation().distance(cc.getEntity().getLocation())<(cc.getRadius()-c.getRadius()*.6)){
					if(c.getMass()<=(cc.getMass()*.75)){
						cc.setMass(cc.getMass()+c.death(cc));
						if(!c.getHost().isAI()){
							c.getPlayer().playSound(c.getPlayer().getLocation(), Sound.ITEM_BUCKET_EMPTY_LAVA, 1, 1);
						}
						if(!cc.getHost().isAI()){
							cc.getPlayer().playSound(cc.getPlayer().getLocation(), Sound.ITEM_BUCKET_FILL_LAVA, 1, 1);
						}
					}
				}
			}
		}
	}
	
	/**
	 * Returns A List Of Locations Needed
	 * To Produce A Circle That Is Visible
	 * To All Players In The Game.
	 * 
	 * @param center
	 * @param radius
	 * @param amount
	 * @return
	 */
	public ArrayList<Location> getCircle(Location center, double radius, int amount){
		World world = center.getWorld();
		double increment = ((2 * Math.PI) / amount);
		ArrayList<Location> locations = new ArrayList<Location>();
		for(int i = 0;i < amount; i++){
			 double angle = i * increment;
			 double x = center.getX() + (radius * Math.cos(angle));
			 double z = center.getZ() + (radius * Math.sin(angle));
			 locations.add(new Location(world, x, center.getY(), z));
		}
	    return locations;
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
	protected void stop(){
		Bukkit.getScheduler().cancelTask(id);
	}
}

/*
 * © 2016 Brandon Mueller
 * DO NOT DE-COMPILE THIS SOFTWARE OR ATTEMPT ANY FORM OF REVERSE ENGINEERING!
 */
