package me.virusbrandon.agarlobbysigns;

import java.util.ArrayList;

import me.virusbrandon.agargameboard.GameBoard;
import me.virusbrandon.agarmc.Main;
import me.virusbrandon.agarutils.TPS;

import org.bukkit.*;
import org.bukkit.block.Block;

/**
 * For The Record, These Lobby Signs
 * Will Not Be Able To Respond To User
 * Interactions. They Are Specifically
 * For Information Only.
 * 
 * Use The Arena Status GUI To Check A More
 * Detailed Look At Arena Information And To
 * Spectate Arenas That Are Currently IN GAME.
 * 
 * @author Brandon
 *
 */
public class LobbySignManager {
	Runnable timer = new Runnable() {
		public void run() {
			try{
				for(LobbySign sign:signs){
					sign.setText(signDisp(gb));
				}
				lI();
			}catch(Exception e1){}
			if(errors > 250){	/* Reaching this threshold pretty much means that there is a recurring error that must be resolved... douchebags... */
				gb.getMain().errorFatal("Try Not Destroying The Lobby Signs With WorldEdit, Please!");
			}
		}
	};
	
	private static LobbySignManager instance;
	private GameBoard gb;
	private ArrayList<LobbySign> signs = new ArrayList<>();
	private int id;
	private String s;
	private String bl=ChatColor.BLACK+"",bo=ChatColor.BOLD+"",gr=ChatColor.GREEN+"",wh=ChatColor.WHITE+"",lp=ChatColor.LIGHT_PURPLE+"",bu=ChatColor.BLUE+"";
	private String loader = "▍ ▍ ▍                           ";
	private String fill,full;
	private int errors = 0;
	
	private LobbySignManager(GameBoard gb){
		this.gb = gb;
		this.fill = gb.getMain().gF().draw("▍",4,"");
		this.full = gb.getMain().gF().draw("▍",32,"");
		try{
			Location l = gb.getLobby();
			for(int x=40,y=20,z=40;y>0;x--){
				Location ori = new Location(l.getWorld(),((l.getX()+20)-x),((l.getY()+10)-y),((l.getZ()+20)-z));
				Block block = ori.getWorld().getBlockAt(ori);
				if(block.getType()==Material.WALL_SIGN){
					new LobbySign(ori,this);
				}
				if(x==0){
					x=40;
					z--;
					if(z==0){
						z=40;
						y--;
					}
				}
			}
			start(2);
		}catch(Exception e1){}
	}
	
	public static LobbySignManager getInstance(GameBoard gb){
		if(instance == null){
			instance = new LobbySignManager(gb);
		}
		return instance;
	}
	
	protected void addSign(LobbySign sign){
		signs.add(sign);
	}
	
	protected void addError(){
		errors++;
	}
	
	private String[] signDisp(GameBoard gb){
		String[] ss;
		if(gb.isReady()){
			ss = new String[]{bu+fill+" "+TPS.getTPS()+" "+bu+fill,wh+bo+"AGAR.MC",gr+bo+"Join Now!",bu+full};
		} else if(!gb.isReady()&!gb.isInProgress()){
			ss = new String[]{lp+full,wh+bo+"Waiting...",wh+loader,lp+full};
		} else if(!gb.isReady()&gb.isInProgress()){
			ss = new String[]{gr+full,wh+bo+"Starting Up",wh+(gb.getProgress()>100?100:gb.getProgress())+"%",gr+full};
		} else {
			ss = new String[]{};
		}
		return ss;
	}
	
	/**
	 * Returns All Lobby Signs.
	 * 
	 */
	public ArrayList<LobbySign> getSigns(){
		return signs;
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
	public void stop(){
		String[] ss = ns();
		for(LobbySign s:signs){
			s.setText(ss);
		}
		Bukkit.getScheduler().cancelTask(id);
	}
	
	/**
	 * No Signal Signs
	 * 
	 */
	private String[] ns(){
		s = bl+full;
		return new String[]{s,s,s,s};
	}
	
	/**
	 * A Generic Loading Icon
	 * 
	 */
	private String lI(){
		loader+=loader.substring(0,1);
		loader=loader.substring(1,loader.length());
		return loader;
	}
}

/*
 * © 2016 Brandon Mueller
 * DO NOT DE-COMPILE THIS SOFTWARE OR ATTEMPT ANY FORM OF REVERSE ENGINEERING!
 */