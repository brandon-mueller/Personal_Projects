package me.virusbrandon.agarutils;

import org.bukkit.Bukkit;
import org.bukkit.*;
import org.bukkit.block.Block;

import me.virusbrandon.agarmc.Main;

public class AutomatedSetup {
	
	/**
	 * This section will check for the existence
	 * of the world: "AGAR.MC-LOBBY.
	 * 
	 * This world will be used for the lobby
	 * for this software.
	 * 
	 * Conditional upon it's lack of existence
	 * 
	 */
	Runnable CREATE_LOBBY_WORLD_ROUTINE = new Runnable() { 
		public void run() {
			if(!inProg1){
				try{
					Bukkit.broadcastMessage(ye+bo+"AGAR.MC - AUTO SETUP (BE PATIENT!)");
					Bukkit.getWorld("AGAR.MC-LOBBY").getDifficulty(); /* NULL CHECK - No Other Purpose */
					stop1();
					start2(1);
					Bukkit.broadcastMessage(main.pfx()+gr+"Lobby World - Found It...");
				}catch(Exception e1){
					Bukkit.broadcastMessage(main.pfx()+gr+"Lobby World - Creating It...");
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"MVCREATE AGAR.MC-LOBBY NORMAL -G CleanroomGenerator:.");
					inProg1 = !inProg1;
				}
			} else {
				TTCLW-=1;
				if(TTCLW <= 0){
					stop1();
					start2(1);
				}
			}
		}
	};
	
	/**
	 * This section will generate a basic structure
	 * for the lobby area
	 * 
	 * This will only happen when there is found
	 * to be an absense of a configuration file.
	 * 
	 * After that, this entire automated set of routines
	 * will not run again.
	 * 
	 * Therefore, it will be safe to build a custom
	 * lobby around the default generated structure.
	 * 
	 */
	Runnable PREPARE_LOBBY_WORLD_ROUTINE = new Runnable() { 
		@SuppressWarnings("deprecation")
		public void run() {
			try{
				if(!inProg2){
					Bukkit.getWorld("AGAR.MC-LOBBY").setDifficulty(Difficulty.PEACEFUL);
					Bukkit.broadcastMessage(main.pfx()+gr+"Lobby Platform - Creating It...");
					inProg2 = !inProg2;
					Location loc = new Location(Bukkit.getWorld("AGAR.MC-LOBBY"),0,50,0,90,0);
					main.gB().setLobby(loc);
					int CONST = 10;
					for(int x=((int)(loc.getX()-(CONST-1))),z=((int)(loc.getZ()+(CONST-1)));z>((int)(loc.getZ()-CONST));x++){
						Location l = new Location(loc.getWorld(),x,loc.getY(),z);
						if(x==((int)(loc.getX()+CONST))){
							x=((int)(loc.getX()-(CONST)));
							z--;	
						} 
						l.getWorld().getBlockAt(l).setType(Material.QUARTZ_BLOCK);
						l.getWorld().getBlockAt(new Location(l.getWorld(),l.getX(),l.getY()+200,l.getZ())).setType(Material.GLASS);
					}
					for(int x=((int)(loc.getX()-(CONST-1))),y=(int)(loc.getY()+1),z=((int)(loc.getZ()+(CONST-1)));(z>((int)(loc.getZ()-CONST)));y++){
						Location l = new Location(loc.getWorld(),x,y,z);
						Location s = new Location(loc.getWorld(),(x+1),y,z);
						l.getWorld().getBlockAt(l).setType(Material.QUARTZ_BLOCK);
						Block sign = s.getWorld().getBlockAt(s);
						sign.setTypeIdAndData(68,(byte)0x5, true);
						if(y>=((int)(loc.getY()+5))){
							y=((int)(loc.getY()));
							z--;
						}
					}
				} else {
					TTPLW-=1;
					if(TTPLW <= 0){
						stop2();
						start3(1);
					}
				}
			}catch(Exception e1){
				Bukkit.broadcastMessage(main.pfx()+gr+"Lobby World Exists - Loading It...");
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"mv load AGAR.MC-LOBBY");
			}
		}	
	};
	
	/**
	 * This section will check for the existence
	 * of the world: "AGAR.MC-GAMEBOARD.
	 * 
	 * This world will be used for the Gameboard
	 * for this software.
	 * 
	 * Conditional upon it's lack of existence
	 * 
	 */
	Runnable CREATE_GAMEBOARD_WORLD_ROUTINE = new Runnable() { 
		public void run() {
			if(!inProg3){
				try{
					Bukkit.getWorld("AGAR.MC-GAMEBOARD").getDifficulty(); /* NULL CHECK - No Other Purpose */
					stop3();		 									  /* NULL CHECK PASSED - WORLD EXISTS - SKIP ROUTINE */
					start4(1);
					Bukkit.broadcastMessage(main.pfx()+gr+"GameBoard World - Found It...");
				}catch(Exception e1){
					Bukkit.broadcastMessage(main.pfx()+gr+"GameBoard World - Creating It...");
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"mvcreate AGAR.MC-GAMEBOARD normal -g CleanroomGenerator:");
					inProg3 = !inProg3;
				}							 
			} else {
				TTCGBW-=1;
				if(TTCGBW <= 0){
					stop3();
					start4(1);
				}
			}
		}	
	};
	
	/**
	 * This section will set the Gameboard origin
	 * location in the world AGAR.MC-GAMEBOARD
	 * 
	 * Once both the lobby location and the Gameboard
	 * origin locations are set, a valid configuration
	 * will be saved on shutdown and the Game Driver will
	 * start.
	 * 
	 * The lobby signs(if any) will begin showing output
	 * at this time.
	 * 
	 */
	Runnable PREPARE_GAMEBOARD_ORIGIN_ROUTINE = new Runnable() { 
		public void run() {
			try{
				if(!inProg4){
					Bukkit.getWorld("AGAR.MC-GAMEBOARD").setDifficulty(Difficulty.PEACEFUL);
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mvm set animals false AGAR.MC-GAMEBOARD");
					inProg4 = !inProg4;
					Bukkit.broadcastMessage(main.pfx()+gr+"GameBoard Origin - Starting Game Driver...");
				} else {
					TTGBO-=1;
					if(TTGBO <= 0){
						Location loc = new Location(Bukkit.getWorld("AGAR.MC-GAMEBOARD"),0,0,0);
						main.gB().setOri(loc);
						stop4();
						start5(1);
					}
				}
			}catch(Exception e1){
				Bukkit.broadcastMessage(main.pfx()+gr+"GameBoard World Exists - Loading It...");
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"mv load AGAR.MC-GAMEBOARD");
			}
		}	
	};
	
	/**
	 * Just Waiting For A Moment, 
	 * What's It To Ya!!?!
	 * 
	 */
	Runnable WRAPPING_THINGS_UP = new Runnable() { 
		public void run() {
			try{
				if(!inProg5){
					Bukkit.broadcastMessage(main.pfx()+gr+"Wrapping Things Up - One Moment...");
					Location cn = main.gB().getLobby();
					WorldBorder border = cn.getWorld().getWorldBorder();
					border.setCenter(cn);
					border.setSize(50);
					cn = main.gB().getGameboardCenter();
					border = cn.getWorld().getWorldBorder();
					border.setCenter(cn);
					border.setSize((main.gB().getBoardSize()-(main.gB().getBoardPadding()*2)));
					inProg5 = !inProg5;
				} else {
					WTU--;
					if(WTU <= 0){
						stop5();
						Bukkit.broadcastMessage(main.pfx()+gr+"Done - "+gr+bo+"Go Play!");
					}
				}
			}catch(Exception e1){}
		}	
	};
	
	private int TTCLW = 100;
	private int TTPLW = 50;
	private int TTCGBW = 100;
	private int TTGBO = 50;
	private int WTU = 125;
	private boolean inProg1 = false;
	private boolean inProg2 = false;
	private boolean inProg3 = false;
	private boolean inProg4 = false;
	private boolean inProg5 = false;
	private int id;
	private Main main;
	private String gr=ChatColor.GREEN+"",ye=ChatColor.YELLOW+"",bo=ChatColor.BOLD+"";
	
	
	public AutomatedSetup(Main main){
		this.main = main;
		start1(1);
	}
	
	public AutomatedSetup(Main main, boolean flag){
		this.main = main;
	}
	
	/**
	 * Not to worry folks, this function destorys
	 * the worlds associated with Agar.mc and then
	 * resets the software for a clean setup.
	 * 
	 */
	public void destroyEverything(){
		if(main.gB().isRunning("Multiverse-Core","PlugMan")){
			if(main.gB().isReady()){
				main.endAll();
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"mv delete AGAR.MC-LOBBY");
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"mvconfirm");
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"mv delete AGAR.MC-GAMEBOARD");
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"mvconfirm");
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"plugman reload Agarmc");
			} else {
				Bukkit.broadcastMessage("This Command Cannot Be Run Until The Game Is Ready! Operation Aborted!");
			}
		} else {
			Bukkit.broadcastMessage("This Command Requires Multiverse and Plugman! Operation Aborted!");
		}
	}
	
	
	/**
	 * Starts The Timer
	 * 
	 * @param delay
	 * 
	 * 
	 */
	private void start1(int delay){
		id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(Main.class), CREATE_LOBBY_WORLD_ROUTINE, delay, delay);
	}

	/**
	 * Stops The Timer
	 * 
	 * 
	 */
	private void stop1(){
		Bukkit.getScheduler().cancelTask(id);
	}
	
	
	/**
	 * Starts The Timer
	 * 
	 * @param delay
	 * 
	 * 
	 */
	private void start2(int delay){
		id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(Main.class), PREPARE_LOBBY_WORLD_ROUTINE , delay, delay);
	}

	/**
	 * Stops The Timer
	 * 
	 * 
	 */
	private void stop2(){
		Bukkit.getScheduler().cancelTask(id);
	}
	
	
	/**
	 * Starts The Timer
	 * 
	 * @param delay
	 * 
	 * 
	 */
	private void start3(int delay){
		id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(Main.class), CREATE_GAMEBOARD_WORLD_ROUTINE, delay, delay);
	}

	/**
	 * Stops The Timer
	 * 
	 * 
	 */
	private void stop3(){
		Bukkit.getScheduler().cancelTask(id);
	}
	
	/**
	 * Starts The Timer
	 * 
	 * @param delay
	 * 
	 * 
	 */
	private void start4(int delay){
		id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(Main.class), PREPARE_GAMEBOARD_ORIGIN_ROUTINE, delay, delay);
	}

	/**
	 * Stops The Timer
	 * 
	 * 
	 */
	private void stop4(){
		Bukkit.getScheduler().cancelTask(id);
	}
	
	/**
	 * Starts The Timer
	 * 
	 * @param delay
	 * 
	 * 
	 */
	private void start5(int delay){
		id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(Main.class), WRAPPING_THINGS_UP, delay, delay);
	}

	/**
	 * Stops The Timer
	 * 
	 * 
	 */
	private void stop5(){
		Bukkit.getScheduler().cancelTask(id);
	}
}

/*
 * © 2016 Brandon Mueller
 * DO NOT DE-COMPILE THIS SOFTWARE OR ATTEMPT ANY FORM OF REVERSE ENGINEERING!
 */
