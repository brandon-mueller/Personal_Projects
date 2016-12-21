package me.virusbrandon.game;

import me.virusbrandon.smashverse.Main;
import me.virusbrandon.sv_utils.ArenaStatus.Status;

import org.bukkit.Bukkit;

public class ArenaDriver {
	Runnable timer = new Runnable() {
		public void run() {
			try{
				if((mode==0)?arena.go():(mode==1)?arena.tick():(mode==2)?man.scoreboard():(mode==3)?s.heal(1):false){/* Pointless If */}
				if(mode==3){
					toHeal--;
					if((s.getCurrentArena().getStatus()==Status.SUDDENDEATH)|(toHeal<=0)){
						stop();
					}
				}
			}catch(Exception e1){e1.printStackTrace();}
		}	
	};
	
	private int id;
	private int mode;
	private int toHeal;
	private Arena arena;
	private ArenaManager man;
	private SmashVersePlayer s;
	
	/**
	 * The Timer Constructor:
	 * 
	 * @param chest
	 * @param mode
	 * 
	 * 
	 */
	
	public ArenaDriver(Arena arena, int mode){
		this.arena = arena;
		this.mode = mode;
	}
	
	public ArenaDriver(ArenaManager man, int mode){
		this.man = man;
		this.mode = mode;
	}
	
	public ArenaDriver(SmashVersePlayer s, int mode, int toHeal){
		this.s = s;
		this.mode = mode;
		this.toHeal = toHeal;
	}
	
	/**
	 * Starts The Timer
	 * 
	 * @param delay
	 * 
	 * 
	 */
	public void start(int delay){
		id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(Main.class), timer, delay, delay);
	}
	
	public void start(int delay1, int delay2){
		id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(Main.class), timer, delay1, delay2);
	}

	
	/**
	 * Stops The Timer
	 * 
	 * 
	 */
	public void stop(){
		Bukkit.getScheduler().cancelTask(id);
	}
}

/*
 * © 2016 Brandon Mueller
 * DO NOT DE-COMPILE THIS SOFTWARE OR ATTEMPT ANY FORM OF REVERSE ENGINEERING!
 */