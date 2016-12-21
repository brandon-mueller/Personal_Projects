package me.virusbrandon.game;

import java.util.ArrayList;

import me.virusbrandon.smashverse.Main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
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
				for(int x=0;x<signs.size();x++){
					if(x<man.getArenaCount()){
						signs.get(x).setText(signDisp(man.getArenas().get(x)));
					} else {
						s = man.getMain().gF().draw(bl+"▍", SB, "");
						signs.get(x).setText(new String[]{s,wh+bo+"No",wh+bo+"Signal D:",s});
					}
				}
				lI();
			}catch(Exception e1){e1.printStackTrace();}
		}	
	};
	
	private ArenaManager man;
	private ArrayList<LobbySign> signs = new ArrayList<>();
	private int id;
	private String s;
	private String bl=ChatColor.BLACK+"",bo=ChatColor.BOLD+"",re=ChatColor.RED+"",aq=ChatColor.AQUA+"",gr=ChatColor.GREEN+"",dg=ChatColor.DARK_GREEN+"",ga=ChatColor.GRAY+"",wh=ChatColor.WHITE+"",dr=ChatColor.DARK_RED+"",bu=ChatColor.BLUE+"",lp=ChatColor.LIGHT_PURPLE+"";
	private String loader = "▍ ▍ ▍                           ";
	private int SB = 32;
	
	public LobbySignManager(ArenaManager man, Location loc){
		try{
			for(int x=40,y=20,z=40;y>0;x--){
				Location ori = new Location(loc.getWorld(),((loc.getX()+20)-x),((loc.getY()+10)-y),((loc.getZ()+20)-z));
				Block block = ori.getWorld().getBlockAt(ori);
				if(block.getType()==Material.WALL_SIGN){
					signs.add(new LobbySign(ori));
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
		}catch(Exception e1){e1.printStackTrace();}
		this.man = man;
	}
	
	public String[] signDisp(Arena arena){
		String[] ss;
		switch(arena.getStatus()){
			case NO_SIGNAL:
				ss = ns();
			break;
			case QUEUED:
				s = man.getMain().gF().draw(lp+"▍", SB, "");
				ss = new String[]{s,wh+bo+"Queued",wh+loader,s};
			break;
			case PREPARING:
				s = man.getMain().gF().draw(ga+"▍", SB, "");
				ss = new String[]{s,wh+bo+"Preparing",wh+bo+arena.genPercent(),s};
			break;
			case WAITING:
				s = man.getMain().gF().draw(bu+"▍", SB, "");
				ss = new String[]{s,wh+bo+"Ready",re+"SLOT "+arena.getSlot(),s};
			break;
			case STARTING:
				s = man.getMain().gF().draw(re+"▍", SB, "");
				ss = new String[]{s,wh+bo+"Starting",wh+"In: "+arena.startingIn(),s};
			break;
			case IN_GAME:
				s = man.getMain().gF().draw(aq+"▍", SB, "");
				ss = new String[]{s,wh+bo+"In Game","",s};
			break;
			case SUDDENDEATH:
				s = man.getMain().gF().draw(dr+"▍", SB, "");
				ss = new String[]{s,wh+bo+"SUDDEN",wh+bo+"DEATH",s};
			break;
			case ENDING:
				s = man.getMain().gF().draw(dg+"▍", SB, "");
				ss = new String[]{s,wh+bo+"Ending","",s};
			break;
			case FINISHED:
				s = man.getMain().gF().draw(gr+"▍", SB, "");
				ss = new String[]{s,wh+bo+"Finished","",s};
			break;
			case CLEAR_READY:
				ss = ns();
			break;
			default:
				s = man.getMain().gF().draw(wh+"▍", SB, "");
				ss = new String[]{s,wh+bo+"Meh","",s};
			break;
		}
		return ss;
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
		s = man.getMain().gF().draw(bl+"▍", SB, "");
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
