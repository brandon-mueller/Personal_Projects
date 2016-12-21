package me.virusbrandon.ui;

import java.text.DecimalFormat;

import me.virusbrandon.transportPLUS.Main;
import me.virusbrandon.transportPLUS.Route;
import me.virusbrandon.transportPLUS.RouteManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ListerWindow {
	private Inventory inv;
	private RouteManager man;
	private DecimalFormat df = new DecimalFormat("#####.##");
	private Timer t;
	private String bo=ChatColor.BOLD+"",bl=ChatColor.BLACK+"",wh=ChatColor.WHITE+"",re=ChatColor.RED+"",gr=ChatColor.GREEN+"",bu=ChatColor.BLUE+"";
	private int[]btns = new int[]{0,1,2,45,46,47,3,4,5,48,49,50,6,7,8,51,52,53};
	private int d = 0;
	private String s;
	
	public ListerWindow(RouteManager man){
		this.man = man;
		this.s = man.gF().draw("◠◡",30, "");
		this.inv = Bukkit.createInventory(null, 54,"[TP] "+bl+bo+"Available Routes");
		this.t =  new Timer(this);
		t.start(3);
	}
	
	@SuppressWarnings("deprecation")
	public void tick(){
		for(int i:btns){
			man.gF().setUpItem(inv, i, new ItemStack(Material.THIN_GLASS,1)," ");
		}
		Material mat = detM();
		String ss = effS();
		for(int x=9;x<45;x++){
			try{
				if((x-9)<man.getRoutes().size()){
					Route r = man.getRoutes().get(x-9);
					man.gF().setUpItem(inv, x, new ItemStack(mat,1),(x-9)+"","",bu+bo+ss,"",wh+bo+"ID: "+wh+(x-9),"",wh+bo+"Route Origin:",man.locToString(r.getOri()),"",wh+bo+"Origin Tag:",gr+bo+r.getOriText(),"",wh+bo+"Route Desination:",man.locToString(r.getDest()),"",wh+bo+"Destiniation Tag:",re+bo+r.getDestText(),"",wh+bo+"Aproximate Route Distance:",bu+df.format(man.calcDist(r.getOri(), r.getDest()))+" Blocks","",bu+bo+ss,"");
				} else {
					man.gF().setUpItem(inv, x, new ItemStack(Material.STAINED_GLASS_PANE,1,DyeColor.BLACK.getData()),wh+"No Route Installed Here");
				}
			}catch(Exception e1){
				man.gF().setUpItem(inv, x, new ItemStack(Material.STAINED_GLASS_PANE,1,DyeColor.RED.getData()),wh+"You Attention Is Required!",re+"There Is A Route Installed",re+"Here, However, You Still Need",re+"To Set The Destination Location",re+"And Tag Information.","",wh+"Run The Command:",wh+"/Tsp setDest <ID> <TAG>","",wh+"To Resolve This Issue.");
			}
		}
	}
	
	/**
	 * Returns This User Interface
	 * 
	 * @return
	 */
	public Inventory ui(){
		return inv;
	}
	
	/**
	 * Returns The Material That Will
	 * Be Displayed This Time Around
	 * 
	 * @author Brandon
	 *
	 */
	public Material detM(){
		d++;
		d=d%2;
		return(d==0)?Material.EMPTY_MAP:Material.PAPER;
	}
	
	/**
	 * Advances The Ui Effect
	 * 
	 * @author Brandon
	 *
	 */
	public String effS(){
		s+=s.substring(0,1);
		s=s.substring(1,s.length());
		return s;
	}
	
	
	
	public class Timer {
		Runnable timer = new Runnable() {
			public void run() {
				try{
					lis.tick();
				}catch(Exception e1){e1.printStackTrace();}
			}	
		};
		private ListerWindow lis;
		private int id;
		
		/**
		 * The Timer Constructor:
		 * 
		 * @param chest
		 * @param mode
		 * 
		 * 
		 */
		
		public Timer(ListerWindow lis){
			this.lis = lis;
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

		/*
		 * © 2016 Brandon Mueller
		 * DO NOT DE-COMPILE THIS SOFTWARE OR ATTEMPT ANY FORM OF REVERSE ENGINEERING!
		 */
	}
}
