package me.virusbrandon.skillstop;

import java.text.DecimalFormat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class SkillWindow implements Listener{
	Runnable timer2 = new Runnable() {
		public void run() {
			try{
				if(owner.getOpenInventory().getTopInventory().getTitle().contains("[ST]")){
					owner.playSound(owner.getLocation(),Sound.ENTITY_WOLF_HOWL,.5f,sss);
				}
				sss+=dir2;
				dir2=(sss>=2?-.1f:(sss<=.5f)?.1f:dir2);	
			}catch(Exception e1){}
		}	
	};
	
	Runnable timer = new Runnable() {
		public void run() {
			try{
				tick();
				if(done){
					left--;
					if(left<=0){
						unreg();
						if(owner.getOpenInventory().getTopInventory().getTitle().contains("[ST]")){
							owner.closeInventory();
						}
					}
				} else {
					if(owner.getOpenInventory().getTopInventory().getTitle().contains("[ST]")){
						owner.playSound(owner.getLocation(),Sound.values()[(int)(Math.random()*Sound.values().length)],.1f,ss);
					}
					ss+=dir;
					dir=(ss>=2?-.1f:(ss<=.5f)?.1f:dir);
					time-=.05;
					if(time<=.25){
						done = true;
						owner.sendMessage(main.pfx()+re+bo+"Darn! "+re+"You Ran Out Of Time!");
					}
				}
			}catch(Exception e1){unreg();}
		}	
	};
	
	@EventHandler
	public void click(InventoryClickEvent e){
		Player p = (Player)e.getWhoClicked();
		if(!p.equals(owner)){return;}
		if(owner.getOpenInventory().getTopInventory().getTitle().contains("[ST]")){
			if(!done){
				done = true;
				if((spcs[cntr]==49)){
					if((main.getReqPlays()>0)){
						cntr++;
						cntr=cntr%spcs.length;
						owner.sendMessage(main.pfx()+re+bo+"Darn! "+re+"So Close, Try Again!");
					} else {
						main.swj(owner);
						id2 = start(timer2,1,id2);
					}
				} else {
					if((spcs[cntr]==50)|(spcs[cntr]==48)){
						owner.sendMessage(main.pfx()+re+bo+"Darn! "+re+"So Close, Try Again!");
					} else {
						owner.sendMessage(main.pfx()+re+bo+"Darn! "+re+"Give It Another Try!");
					}
				}
				owner.playSound(owner.getLocation(), Sound.BLOCK_NOTE_HARP,2, 2f);
				owner.playSound(owner.getLocation(), Sound.BLOCK_NOTE_BASS,2, 2f);
			}
			e.setCancelled(true);
		}
	}
	
	private Main main;
	private Player owner;
	private int id,id2,cntr = 0,left = 40;
	private double time;
	private float dir = .1f,ss = .5f,dir2 = .1f, sss = .5f;
	private Inventory inv;
	private int[] spcs = new int[]{0,1,2,3,4,5,6,7,8,17,26,35,44,53,52,51,50,49,48,47,46,45,36,27,18,9};
	private String wh=ChatColor.WHITE+"",gr=ChatColor.GREEN+"",bo=ChatColor.BOLD+"",re=ChatColor.RED+"",bu=ChatColor.BLUE+"";
	private boolean done = false;
	private DecimalFormat df = new DecimalFormat("###");
	
	/**
	 * The SkillWindow Constructor
	 * 
	 * @param main
	 * @param owner
	 */
	public SkillWindow(Main main, Player owner){
		this.main = main;
		this.owner = owner;
		this.inv = Bukkit.createInventory(null, 54,"[ST] Line Up Greens!");
		this.time = main.getTimeLimit();
		Bukkit.getServer().getPluginManager().registerEvents(this, main);
		id = start(timer,1,id);
		main.spn();
	}
	
	/**
	 * Updates The User Interface
	 * 
	 */
	@SuppressWarnings("deprecation")
	private void tick(){
		for(int x=0;x<54;x++){
			main.getFact().setUpItem(inv, x, new ItemStack(Material.STAINED_GLASS_PANE,1,((done)?((spcs[cntr]==49)?DyeColor.LIME:DyeColor.RED):DyeColor.BLACK).getData()),((done)?((spcs[cntr]==49)?gr+bo+"WINNER!":re+bo+"Sorry, Try Again!"):wh+bo+"Click To Stop!"),"",main.getFact().draw(bu+"─", 15, ""),"",wh+bo+"Current Jackpot: "+gr+main.pot(),wh+bo+"Time Left: "+((time>=10)?gr:re+bo)+df.format(time)+" Seconds","",main.getFact().draw(bu+"─", 15, ""));
		}
		main.getFact().setUpItem(inv, 40, new ItemStack(Material.STAINED_GLASS_PANE,1,DyeColor.LIME.getData()), gr+bo+"Stop The Green Piece HERE!");
		main.getFact().setUpItem(inv, 49, new ItemStack(Material.STAINED_GLASS_PANE,1,DyeColor.GRAY.getData())," ",gr+"Stop The Green Piece",gr+"Right Here To WIN!");
		for(int x=0;x<spcs.length;x++){
			if(x == cntr){
				main.getFact().setUpItem(inv, spcs[x], new ItemStack(Material.STAINED_GLASS_PANE,1,DyeColor.LIME.getData()), gr+bo+"Stop Me!", wh+"The Object Of This Game",wh+"Is To Line Me Up With",wh+"Another Green Piece To Win!");
			} else {
				if(spcs[x]!=49){
					main.getFact().setUpItem(inv, spcs[x], new ItemStack(Material.STAINED_GLASS_PANE,1,DyeColor.BLUE.getData())," ", re+"Line Up Greens And Avoid",re+"Stopping The Lone Green",re+"Piece Here, Or You Lose!");
				}
			}
		}
		if(!done){
			cntr++;
			cntr=(cntr%spcs.length);	
		}
	}
	
	/**
	 * Returns This User Interface
	 * 
	 * @return
	 */
	public void play(Player p){
		p.openInventory(inv);
	}
	
	/**
	 * Returns The Owner Of
	 * This Skill Stop Window
	 * 
	 */
	public Player getOwner(){
		return owner;
	}
	
	/**
	 * Returns The Unique ID Of
	 * The Player That Owns This Game Board.
	 * 
	 * @return
	 */
	public String getUUID(){
		return owner.getUniqueId().toString();
	}
	
	/**
	 * Wraps Things Up And Unregisters
	 * Timers And Listeners When This Game
	 * Is Finished.
	 * 
	 */
	public void unreg(){
		InventoryClickEvent.getHandlerList().unregister(this);
		stop(id);stop(id2);
		main.remST(this);
	}
	
	/**
	 * Starts The Timer
	 * 
	 * @param delay
	 * 
	 * 
	 */
	private int start(Runnable t, int delay, int id){
		id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(Main.class), t, delay, delay);
		return id;
	}

	/**
	 * Stops The Timer
	 * 
	 * 
	 */
	private void stop(int i){
		Bukkit.getScheduler().cancelTask(i);
	}
}
