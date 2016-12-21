package me.virusbrandon.AniMessage;

import org.bukkit.Bukkit;

public class Timer {
	Message m;
	FrameManager f;
	Parkour p;
	Burst b;
	int id;
	
	Timer(Message m, FrameManager f, Parkour p, Burst b){
		this.m = m;
		this.f = f;
		this.p = p;
		this.b = b;
		if(!(m==null)){
			id = Bukkit.getScheduler().scheduleSyncRepeatingTask(AniMessage.getPlugin(AniMessage.class), timer,m.getDelay(), m.getDelay());
		}
		if(!(f==null)){
			id = Bukkit.getScheduler().scheduleSyncRepeatingTask(AniMessage.getPlugin(AniMessage.class), timer,f.getDelay(), f.getDelay());
		}
		if(!(p==null)){
			id = Bukkit.getScheduler().scheduleSyncRepeatingTask(AniMessage.getPlugin(AniMessage.class), timer,p.getDelay(), p.getDelay());
		}
		if(!(b==null)){
			id = Bukkit.getScheduler().scheduleSyncRepeatingTask(AniMessage.getPlugin(AniMessage.class), timer,b.getDelay(), b.getDelay());
		}
	}
	
	Runnable timer = new Runnable() {
		public void run() {
			if(!(m==null)){
				if(!(m.getState() == 1)){
					m.cycle();
				}
			}
			
			if(!(f==null)){
				if(!(f.getState() == 1)){
					f.cycle();
				}
			}
			if(!(p==null)){
				if(!(p.getState() == 1)){
					p.cycle();
				}
			}
			if(!(b==null)){
				if(!(b.getState() == 1)){
					b.cycle();
				}
			}
		}
	};
	
	public void stopTimer(){
		Bukkit.getScheduler().cancelTask(id);
	}
}
