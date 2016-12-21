package me.virusbrandon.tags;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Timer {
	Main ma;
	int id;
	int mode;
	
	Runnable timer = new Runnable() {
		public void run() {
		switch(mode){
			case 0:
				for(Player p:Bukkit.getOnlinePlayers()){
					try{
						ma.findP(p).updInv(p);
					}catch(Exception e1){}
				}
			break;
			case 1:
				ma.awesomeSauce();
			break;
		}
		}
	};
	
	Timer(Main ma,int mode){
		this.ma=ma;
		this.mode = mode;
	}
	
	public void start(int delay){
		id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(Main.class), timer, delay, delay);
	}
	
	public void stop(){
		Bukkit.getScheduler().cancelTask(id);
	}
}
