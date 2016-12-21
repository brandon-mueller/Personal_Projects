package me.virusbrandon.Micro_SG;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Timer {
	Runnable timer = new Runnable() {
		public void run() {
			try{
				switch(mode){
					case 0:
						m.ArenaBuildTick();
					break;
					case 1:
						m.managerTick();
					break;
					case 2:
						a.dropTick();
					break;
					case 3:
						m.updateMenu();
					break;
					case 4:
						a.ReviveSound(p,flag);
						flag++;
					break;
					case 5:
						m.updateScores();
					break;
					case 6:
						m.updateShop();
					break;
					case 7:
						m.updateInv();
					break;
					case 8:
						m.clearInv();
					break;
					case 9:
						m.giveChest();
					break;
					case 10:
						a.cntTick();
					break;
					case 11:
						m.loadTick();
					break;
					case 12:
						m.admin();
					break;
					case 13:
						ma.mvChecker();
					break;
					case 14:
						m.clearConsole();
					break;
					case 15:
						m.cleanFW();
					break;
					case 16:
						m.boom();
					break;
				}
			}catch(Exception e1){}
		}	
	};
	
	Main ma;
	ArenaManager m;
	Arena a;
	Player p;
	int id;
	int flag = 0;
	int mode;
	
	Timer(ArenaManager m, int mode){
		this.m = m;
		this.mode = mode;
	}
	
	Timer(Arena a, int mode){
		this.a = a;
		this.mode = mode;
	}
	
	Timer(Arena a,Player p,int mode){
		this.a = a;
		this.p = p;
		this.mode = mode;
	}
	
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
