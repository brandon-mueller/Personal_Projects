package me.virusbrandon.Block_Launcher;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Timer {
	Runnable timer = new Runnable() {
		public void run() {
			try{
				switch(mode){
				case 0:
					ma.tick1();
				break;
				case 1:
					ma.tick2();
				break;
				}
			}catch(Exception e1){}
		}	
	};
	
	Main ma;
	Player p;
	int id;
	int mode;
	
	Timer(Main ma,int mode){
		this.ma=ma;
		this.mode=mode;
	}
	
	public void start(int delay){
		id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(Main.class), timer, delay, delay);
	}
	
	public void stop(){
		Bukkit.getScheduler().cancelTask(id);
	}
}
