package me.virusbrandon.ui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.virusbrandon.game.Arena;
import me.virusbrandon.smashverse.Main;
import me.virusbrandon.sv_utils.ArenaStatus.Status;

public class StatusWindow {
	private Main main;
	private UiTimer driver;
	private int[]btns = new int[]{0,1,2,45,46,47,3,4,5,48,49,50,6,7,8,51,52,53};
	private String bl=ChatColor.BLACK+"",bo=ChatColor.BOLD+"",wh=ChatColor.WHITE+"",lp=ChatColor.LIGHT_PURPLE+"",bu=ChatColor.BLUE+"",gr=ChatColor.GREEN+"",dg=ChatColor.DARK_GREEN+"",ga=ChatColor.GRAY+"",re=ChatColor.RED+"",dr=ChatColor.DARK_RED+"",aq=ChatColor.AQUA+"";
	private Inventory inv;
	private int[] stats;
	private String[] stmsg;
	private AdminWindow ad;
	private String ss;
	
	public StatusWindow(Main main){
		this.main = main;
		this.inv = Bukkit.createInventory(null, 54,"[SV] "+bl+bo+"Arena Status");
		this.ad = new AdminWindow(main);
		this.ss = main.gF().draw(bu+"─", 25, "");
		driver = new UiTimer(this,0);
		driver.start(1);
	}
	
	@SuppressWarnings("deprecation")
	protected boolean refresh(){
		for(int i:btns){
			main.gF().setUpItem(inv, i, new ItemStack(Material.END_CRYSTAL,1),wh+bo+"Summary Of Operations: ",stc());
		}
		for(int i=9;i<45;i++){
			if((i-9)<main.aM().getArenas().size()){
				Arena a = main.aM().getArenas().get(i-9);
				String[] s = main.aM().getLMan().signDisp(a);
				boolean b =(a.getStatus()==Status.IN_GAME|a.getStatus()==Status.SUDDENDEATH);
				try{
					main.gF().setUpItem(inv,i,new ItemStack(Material.STAINED_GLASS_PANE,(i-9),dc(a.getStatus())),bl+main.aM().getArenas().get(i-9).getSlot(),ss,"",wh+bo+"Arena Status: "+s[0].substring(0, 2)+s[1].substring(2,s[1].length())+((a.getStatus()==Status.PREPARING)?" ( "+gr+a.genPercent()+ga+bo+" ) ":""),wh+bo+"Map: "+wh+a.getTemplate().getName(),"",(b)?wh+bo+"Players: "+gr+bo+a.getActivePlayerCount():"",(b)?wh+bo+"Spectators: "+aq+bo+a.getSpectators().size():"","",(b)?re+"Click Here To Spectate!":re+"No Match In Progress - Cannot Spectate","",ss);
				}catch(Exception e1){
					main.gF().setUpItem(inv,i,new ItemStack(Material.STAINED_GLASS_PANE,1),wh+bo+" ","",ss,"",wh+bo+"We're Getting A Few Things Ready,",wh+bo+"Hold That Thought...","",ss);
				}
			} else {
				main.gF().setUpItem(inv, i, new ItemStack(Material.STAINED_GLASS_PANE,1,DyeColor.WHITE.getData()),wh+bo+"No Arena Signal!");
			}
		}
		return true;
	}
	
	/**
	 * Status Count On Active Arenas
	 * 
	 * @param s
	 * @return
	 */
	private String[] stc(){
		stats = new int[9];
		stmsg = new String[]{wh+" | No Signal",lp+" | Queued",ga+" | Preparing",bu+" | Ready",re+" | Starting",aq+" | In Game",dr+" | SuddenDeath",dg+" | Ending",gr+" | Finished",bl+"#ADMIN"};
		int t = 0;
		for(Arena a:main.aM().getArenas()){
			t = (a.getStatus()==Status.NO_SIGNAL)?0:(a.getStatus()==Status.QUEUED)?1:(a.getStatus()==Status.PREPARING)?2:(a.getStatus()==Status.WAITING)?3:(a.getStatus()==Status.STARTING)?4:a.getStatus()==Status.IN_GAME?5:a.getStatus()==Status.SUDDENDEATH?6:(a.getStatus()==Status.ENDING)?7:(a.getStatus()==Status.FINISHED)?8:-1;
			stats[t]++;
		}
		for(int x=0;x<9;x++){
			stmsg[x]=(ChatColor.getLastColors(stmsg[x])+bo+((stats[x]<10)?("0"+stats[x]):stats[x])+stmsg[x]);
		}
		return stmsg;
	}
	
	@SuppressWarnings("deprecation")
	private Byte dc(Status s){
		DyeColor d = ((s == Status.NO_SIGNAL)|(s == Status.CLEAR_READY))?DyeColor.BLACK:(s == Status.QUEUED)?DyeColor.PURPLE:(s == Status.PREPARING)?DyeColor.BLACK:(s == Status.WAITING)?DyeColor.BLUE:(s == Status.STARTING)?DyeColor.ORANGE:(s == Status.IN_GAME)?DyeColor.CYAN:(s == Status.SUDDENDEATH)?DyeColor.RED:(s == Status.ENDING)?DyeColor.GREEN:(s == Status.FINISHED)?DyeColor.LIME:null;
		return d.getData();
	}
	
	/**
	 * Returns A Ui To The User (0 For This One, Or 1 For The Admin Ui)
	 * 
	 * @param i
	 * @return
	 */
	public Inventory getUi(int i){
		return((i==0)?inv:(i==1)?ad.getUi():null);
	}
	
	/**
	 * Returns The Border, Preventing
	 * Duplicate Field Declarations.
	 * 
	 * @return
	 */
	public int[] getBtns(){
		return btns;
	}
	
	/**
	 * Stops The Ui Runnable
	 * 
	 */
	public void stopUi(){
		driver.stop();
		ad.stopUi();
	}
	/*
	 * © 2016 Brandon Mueller
	 * DO NOT DE-COMPILE THIS SOFTWARE OR ATTEMPT ANY FORM OF REVERSE ENGINEERING!
	 */
}