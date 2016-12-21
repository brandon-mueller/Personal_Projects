package me.virusbrandon.agarui;

import java.util.HashMap;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import me.virusbrandon.agarmc.*;
import me.virusbrandon.agarutils.RGBContainer;

public class AIUI implements Listener{
	Runnable timer = new Runnable() {
		public void run() {
			cnt++;
			if(cnt >= delay){
				cnt=0;
				refresh();
			}
		}	
	};
	
	@EventHandler
	public void click(InventoryClickEvent e){
		try{
			Player p = (Player)e.getWhoClicked();
			if(e.getInventory().getTitle().equalsIgnoreCase("[AG] "+bl+bo+"Active AIs")){
				AgarPlayer pp = ais.get(e.getCurrentItem().getItemMeta().getDisplayName());
				details(p,pp);
			} else if(e.getInventory().getTitle().equalsIgnoreCase("[AG] "+bl+bo+"Choose Cell Color")){
				//Do Nothing
			} else {
				String string = e.getInventory().getTitle();
				String disp = e.getCurrentItem().getItemMeta().getDisplayName();
				AgarPlayer pp = ais.get(string);
				if(disp.equalsIgnoreCase("LOCATE AI")){
					p.teleport(pp.getCells().get(0).getEntity().getLocation());
				} else if(disp.equalsIgnoreCase("SPLIT CELLS")){
					pp.split(-1);
				} else if(disp.equalsIgnoreCase("EJECT MASS")){
					pp.eject();
				} else if(disp.equalsIgnoreCase("MERGE CELLS")){
					pp.setSplitTime(0);
				} else if(disp.equalsIgnoreCase("KILL AI")){
					pp.done(false);
					p.openInventory(inv);
				} else if(disp.equalsIgnoreCase("BACK")){
					p.openInventory(inv);
				}
				refresh();
			}
		}catch(Exception e1){}
	}
	
	private Inventory inv;
	private Main main;
	private String bl=ChatColor.BLACK+"",bo=ChatColor.BOLD+"",wh=ChatColor.WHITE+"";
	private static AIUI instance;
	private int id;
	private int delay = 100;
	private int cnt = 0;
	private int[] slots = new int[]{11,12,13,14,15,20,21,22,23,24,29,30,31,32,33,38,39,40,41,42};
	private HashMap<String,AgarPlayer> ais = new HashMap<>();
	
	/**
	 * AIUI Constructor ( Singleton )
	 * 
	 * @param main
	 */
	private AIUI(Main main){
		this.main = main;
		inv = Bukkit.createInventory(null, 54,"[AG] "+bl+bo+"Active AIs");
		Bukkit.getServer().getPluginManager().registerEvents(this, main);
		refresh();
		start(1);
	}
	
	/**
	 * Returns The Current Instance's
	 * Inventory View
	 * 
	 * @param main
	 * @return
	 */
	public static Inventory getInstance(Main main){
		if(instance == null){
			instance = new AIUI(main);
		}
		return instance.getInv();
	}
	
	/**
	 * Refreshes The Inventory View
	 * With New Information
	 * 
	 */
	@SuppressWarnings("deprecation")
	private void refresh(){
		try{	
			for(int x=0;x<54;x++){
				main.gF().setUpItem(inv, x, new ItemStack(Material.STAINED_GLASS_PANE,1,DyeColor.BLACK.getWoolData())," ",bl+"#AGAR");
			}
			int i = 0;
			ais.clear();
			for(AgarPlayer p:main.getAllAIs()){
				try{
					if(p.getEnd()==0){
						RGBContainer c = main.gB().getColor(p.getCells().get(0).getCellColor().name());
						ItemStack st = new ItemStack(Material.LEATHER_CHESTPLATE,1);
						LeatherArmorMeta met = (LeatherArmorMeta)st.getItemMeta(); 
						met.setColor(Color.fromRGB(c.IR(), c.IG(), c.IB()));
						st.setItemMeta(met);
						main.gF().setUpItem(inv, slots[i], st,p.getName(), wh+bo+"Color: "+wh+p.getCells().get(0).getCellColor().name(),wh+bo+"Mass: "+wh+p.getTotalMass(),wh+bo+"Time Alive: "+wh+getTime((int)(System.currentTimeMillis()-p.getStart())),bl+"#AGAR");
						ais.put(p.getName(), p);
					}
				} catch(Exception e1) {}
				i++;
			}
		}catch(Exception e1){}
	}
	
	public void details(Player p, AgarPlayer pl){
		Inventory temp = Bukkit.createInventory(null, 9,pl.getName());
		main.gF().setUpItem(temp, 0, new ItemStack(Material.REDSTONE_BLOCK,1),"BACK",bl+"#AGAR");
		main.gF().setUpItem(temp, 2, new ItemStack(Material.GLASS,1),"LOCATE AI",bl+"#AGAR");
		main.gF().setUpItem(temp, 3, new ItemStack(Material.GLASS,2),"SPLIT CELLS",bl+"#AGAR");
		main.gF().setUpItem(temp, 4, new ItemStack(Material.GLASS,3),"EJECT MASS",bl+"#AGAR");
		main.gF().setUpItem(temp, 5, new ItemStack(Material.GLASS,4),"MERGE CELLS",bl+"#AGAR");
		main.gF().setUpItem(temp, 6, new ItemStack(Material.GLASS,5),"KILL AI",bl+"#AGAR");
		main.gF().setUpItem(temp, 8, new ItemStack(Material.REDSTONE_BLOCK,1),"BACK",bl+"#AGAR");
		p.openInventory(temp);
	}
	
	/**
	 * Returns The Inventory
	 * 
	 * @return
	 */
	private Inventory getInv(){
		return inv;
	}
	
	/**
	 * Starts The Timer
	 * 
	 * @param delay
	 * 
	 * 
	 */
	private void start(int delay){
		id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(Main.class), timer, delay, delay);
	}
	
	/**
	 * Stops The Timer
	 * 
	 * 
	 */
	public void stop(){
		Bukkit.getScheduler().cancelTask(id);
	}
	
	/**
	 * Format Milli Into Time
	 * 
	 */
	private String getTime(int millis){
		int sec = ((millis/1000)%60);
		int min = ((millis/1000) - ((millis/1000)%60))/60;
		return (min<10?"0":"")+min+":"+(sec<10?"0":"")+sec;
	}
}

/*
 * © 2016 Brandon Mueller
 * DO NOT DE-COMPILE THIS SOFTWARE OR ATTEMPT ANY FORM OF REVERSE ENGINEERING!
 */