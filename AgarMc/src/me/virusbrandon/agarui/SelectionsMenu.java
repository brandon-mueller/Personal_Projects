package me.virusbrandon.agarui;

import me.virusbrandon.agarutils.Pref;
import me.virusbrandon.agarutils.RGBContainer;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class SelectionsMenu implements Listener{
	
	private Inventory inv;
	private String bl=ChatColor.BLACK+"",bo=ChatColor.BOLD+"",wh=ChatColor.WHITE+"",re=ChatColor.RED+"",gr=ChatColor.GREEN+"",ye=ChatColor.YELLOW+"";
	private Pref pr;
	private int[] frm = new int[]{0,1,2,3,4,5,6,7,8,17,26,35,44,36,27,18,9};
	private int[] clrs = new int[]{28,10,11,12,13,14,15,16,19,20,21,22,23,24,25,34,31};
	private int[][] btns = new int[][]{{46,47,48},{50,51,52},{39,40,41}};
	private DyeColor[] dclr = new DyeColor[]{DyeColor.RED,DyeColor.LIME,DyeColor.YELLOW};
	private String[] msg = new String[]{re+bo+"Close AgarMC",gr+bo+"Play AgarMC!",ye+bo+"Toggle Cell Nametag Visibility"};
	private String uuid;
	private boolean h;
	
	@EventHandler
	public void click(InventoryClickEvent e){
		try{
			Player p = (Player)e.getWhoClicked();
			if(p.getOpenInventory().getTopInventory().getTitle().contains("[AG]")){
				e.setCancelled(true);
				ItemStack i = e.getCurrentItem();
				String disp = i.getItemMeta().getDisplayName();
				int qty = i.getAmount();
				if(disp.equalsIgnoreCase(gr+bo+"Play AgarMC!")){
					pr.getMain().join(p, pr);
					unreg();
					p.closeInventory();
				} else if(disp.equalsIgnoreCase(re+bo+"Close AgarMC")){
					unreg();
					p.closeInventory();
				} else if(disp.equalsIgnoreCase(wh+bo+"Select This Color?")){
					if((qty-1) == 16){
						if((p.hasPermission("Agarmc.cells.color.random")|p.hasPermission("Agarmc.ADMIN"))){
							pr.setCellColor("Random");
						}
					} else {
						pr.setRandom(false);
						pr.setCellColor(ChatColor.values()[qty-1].name());
					}
					refresh(p);
				} else if(disp.equalsIgnoreCase(ye+bo+"Toggle Cell Nametag Visibility")){
					if((p.hasPermission("Agarmc.cells.nametag")|p.hasPermission("Agarmc.ADMIN"))){
						pr.setNameOn(pr.isNameOn()?false:true);
						refresh(p);
					}	
				}
			} 	
		}catch(Exception e1){}
	}
	
	@EventHandler
	public void close(InventoryCloseEvent e){
		Player p = (Player)e.getPlayer();
		if(p.getUniqueId().toString().equalsIgnoreCase(uuid)){
			unreg();
		}
	}
	
	/**
	 * The SelectionsMenu Constructor
	 * 
	 * @param pr
	 * @param p
	 */
	public SelectionsMenu(Pref pr,Player p){
		this.uuid = p.getUniqueId().toString();
		this.pr = pr;
		inv = Bukkit.createInventory(null, 54,"[AG] "+bl+bo+"Choose Cell Color");
		Bukkit.getServer().getPluginManager().registerEvents(this, pr.getMain());
		refresh(p);
	}
	
	/**
	 * The Refresh Function Is For
	 * Updating The State Of The User
	 * Interface After An Item Has Been
	 * Selected.
	 * 
	 */
	@SuppressWarnings("deprecation")
	private void refresh(Player p){
		for(int i = 0;i<54;i++){
			pr.getMain().gF().setUpItem(inv, i, new ItemStack(Material.STAINED_GLASS_PANE,1,DyeColor.BLACK.getWoolData()), " ",bl+"#AGAR");
		}
		for(int i:frm){
			pr.getMain().gF().setUpItem(inv, i, new ItemStack(Material.STAINED_GLASS_PANE,1,DyeColor.BLUE.getWoolData()), " ",bl+"#AGAR");
		}
		for(int i = 0;i<clrs.length;i++){
			if(i == (clrs.length-1)){
				boolean bb = (p.hasPermission("Agarmc.cells.color.random")|p.hasPermission("Agarmc.ADMIN"));
				pr.getMain().gF().setUpItem(inv, 31, new ItemStack(pr.isRandom()?Material.END_CRYSTAL:bb?Material.SLIME_BALL:Material.MAGMA_CREAM,17),pr.isRandom()?gr+bo+"Color Selected!":wh+bo+"Select This Color?","",wh+bo+"Disco Cell Colors!",wh+"Disco Cell: "+wh+bo+(pr.isRandom()?"On":"Off"),"",bb?gr+"By using disco cell, you will not be able":re+"In order to use disco cell, you'll need",bb?gr+"to blend in, at all... but it looks awesome,":re+"to purchase it from the server store.",bb?gr+"because you'll be constantly changing":"",bb?gr+"colors and stuff.":"","",bb?gr+bo+"Have Fun!":"",bl+"#AGAR");
			} else {
				ChatColor c = ChatColor.values()[i];
				String s = ChatColor.values()[i].name();
				this.h = (p.hasPermission("Agarmc.cells.color."+s)|p.hasPermission("Agarmc.ADMIN"));	
				if(pr.getCellColor().equalsIgnoreCase(c.name())){
					if(h){
						pr.getMain().gF().setUpItem(inv, clrs[i], new ItemStack(Material.END_CRYSTAL,(i+1)),gr+bo+"Color Selected!","",gr+"If the gameboard color is just right,",gr+"the other players won't even see you",gr+"coming! Oh man, this is gonna be fun!","",rp("▒▒▒▒▒▒▒▒▒█████▒▒▒▒▒▒▒▒▒",c,0),rp("▒▒▒▒▒▒███▒▒▒▒▒███▒▒▒▒▒▒",c,0),rp("▒▒▒▒██▒▒▒▒▒▒▒▒▒▒▒██▒▒▒▒", c,0),rp("▒▒▒█▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒█▒▒▒", c,0),rp("▒▒█▒▒▒▒▒▒▒▒▒▒▒▒▒▒▓▒▒█▒▒",c,0),rp("▒▒█▒▒▒▒▒▒▒▒▒▒▒▒▒▓▓▓▒█▒▒",c,0),rp("▒█▒▒▒▒▒▒▒▒▒▒▒▒▒▓▓▓▒▒▒█▒",c,0),rp("▒█▒▒▒▒▒▒▒▒▒▒▒▒▓▓▓▒▒▒▒█▒",c,0),rp("▒█▒▒▒▒▒▒▒▒▒▒▒▓▓▓▒▒▒▒▒█▒",c,0),rp("█▒▒▒▒▒▒▒▒▒▒▒▓▓▓▒▒▒▒▒▒▒█",c,0),rp("█▒▒▒▒▒▒▒▒▒▒▓▓▓▒▒▒▒▒▒▒▒█",c,0),rp("█▒▒▒▒▒▒▒▒▒▓▓▓▒▒▒▒▒▒▒▒▒█",c,0),rp("█▒▓▓▓▒▒▒▒▓▓▓▒▒▒▒▒▒▒▒▒▒█",c,0),rp("█▒▒▓▓▓▒▒▓▓▓▒▒▒▒▒▒▒▒▒▒▒█",c,0),rp("▒█▒▒▓▓▓▓▓▓▒▒▒▒▒▒▒▒▒▒▒█▒",c,0),rp("▒█▒▒▒▓▓▓▓▒▒▒▒▒▒▒▒▒▒▒▒█▒",c,0),rp("▒█▒▒▒▒▓▓▒▒▒▒▒▒▒▒▒▒▒▒▒█▒",c,0),rp("▒▒█▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒█▒▒",c,0),rp("▒▒█▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒█▒▒",c,0),rp("▒▒▒█▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒█▒▒▒",c,0),rp("▒▒▒▒██▒▒▒▒▒▒▒▒▒▒▒██▒▒▒▒",c,0),rp("▒▒▒▒▒▒███▒▒▒▒▒███▒▒▒▒▒▒",c,0),rp("▒▒▒▒▒▒▒▒▒█████▒▒▒▒▒▒▒▒▒",c,0),bl+"#AGAR");
					} else {
						pr.getMain().gF().setUpItem(inv, clrs[i], new ItemStack(Material.END_CRYSTAL,(i+1)),re+bo+"Color NOT Selected!","",ye+"A personal appeal from",ye+"an unnamed cell:","",ye+bo+"Please purchase this cell",ye+bo+"color from the server store",ye+bo+"in order to use it, thanks!","",rp("▒▒▒▒▒▒▒▒▒█████▒▒▒▒▒▒▒▒▒",c,1),rp("▒▒▒▒▒▒███▒▒▒▒▒███▒▒▒▒▒▒",c,1),rp("▒▒▒▒██▒▒▒▒▒▒▒▒▒▒▒██▒▒▒▒", c,1),rp("▒▒▒█▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒█▒▒▒", c,1),rp("▒▒█▒▒▓▒▒▒▒▒▒▒▒▒▒▒▓▒▒█▒▒",c,1),rp("▒▒█▒▓▓▓▒▒▒▒▒▒▒▒▒▓▓▓▒█▒▒",c,1),rp("▒█▒▒▒▓▓▓▒▒▒▒▒▒▒▓▓▓▒▒▒█▒",c,1),rp("▒█▒▒▒▒▓▓▓▒▒▒▒▒▓▓▓▒▒▒▒█▒",c,1),rp("▒█▒▒▒▒▒▓▓▓▒▒▒▓▓▓▒▒▒▒▒█▒",c,1),rp("█▒▒▒▒▒▒▒▓▓▓▒▓▓▓▒▒▒▒▒▒▒█",c,1),rp("█▒▒▒▒▒▒▒▒▓▓▓▓▓▒▒▒▒▒▒▒▒█",c,1),rp("█▒▒▒▒▒▒▒▒▒▓▓▓▒▒▒▒▒▒▒▒▒█",c,1),rp("█▒▒▒▒▒▒▒▒▓▓▓▓▓▒▒▒▒▒▒▒▒█",c,1),rp("█▒▒▒▒▒▒▒▓▓▓▒▓▓▓▒▒▒▒▒▒▒█",c,1),rp("▒█▒▒▒▒▒▓▓▓▒▒▒▓▓▓▒▒▒▒▒█▒",c,1),rp("▒█▒▒▒▒▓▓▓▒▒▒▒▒▓▓▓▒▒▒▒█▒",c,1),rp("▒█▒▒▒▓▓▓▒▒▒▒▒▒▒▓▓▓▒▒▒█▒",c,1),rp("▒▒█▒▓▓▓▒▒▒▒▒▒▒▒▒▓▓▓▒█▒▒",c,1),rp("▒▒█▒▒▓▒▒▒▒▒▒▒▒▒▒▒▓▒▒█▒▒",c,1),rp("▒▒▒█▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒█▒▒▒",c,1),rp("▒▒▒▒██▒▒▒▒▒▒▒▒▒▒▒██▒▒▒▒",c,1),rp("▒▒▒▒▒▒███▒▒▒▒▒███▒▒▒▒▒▒",c,1),rp("▒▒▒▒▒▒▒▒▒█████▒▒▒▒▒▒▒▒▒",c,1),bl+"#AGAR");
					}
				} else {
					RGBContainer rgb = pr.getMain().gB().getColor(s);
					ItemStack st = new ItemStack(Material.LEATHER_CHESTPLATE,(i+1));
					LeatherArmorMeta m = (LeatherArmorMeta)st.getItemMeta();
					m.setColor(Color.fromRGB(rgb.IR(), rgb.IG(), rgb.IB()));
					st.setItemMeta(m);
					pr.getMain().gF().setUpItem(inv, clrs[i],st,wh+bo+"Select This Color?","",h?"":re+"You'll need to purchase this cell color",h?"":re+"in order to start the game with it.","",h?"":ye+bo+"Depending on the game board",h?"":ye+bo+"color, this could be an excellent",h?"":ye+bo+"tool for blending right in.","",rp("▒▒▒▒▒▒▒▒▒█████▒▒▒▒▒▒▒▒▒",c,0),rp("▒▒▒▒▒▒███▒▒▒▒▒███▒▒▒▒▒▒",c,0),rp("▒▒▒▒██▒▒▒▒▒▒▒▒▒▒▒██▒▒▒▒", c,0),rp("▒▒▒█▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒█▒▒▒", c,0),rp("▒▒█▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒█▒▒",c,0),rp("▒▒█▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒█▒▒",c,0),rp("▒█▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒█▒",c,0),rp("▒█▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒█▒",c,0),rp("▒█▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒█▒",c,0),rp("█▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒█",c,0),rp("█▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒█",c,0),rp("█▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒█",c,0),rp("█▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒█",c,0),rp("█▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒█",c,0),rp("▒█▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒█▒",c,0),rp("▒█▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒█▒",c,0),rp("▒█▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒█▒",c,0),rp("▒▒█▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒█▒▒",c,0),rp("▒▒█▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒█▒▒",c,0),rp("▒▒▒█▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒█▒▒▒",c,0),rp("▒▒▒▒██▒▒▒▒▒▒▒▒▒▒▒██▒▒▒▒",c,0),rp("▒▒▒▒▒▒███▒▒▒▒▒███▒▒▒▒▒▒",c,0),rp("▒▒▒▒▒▒▒▒▒█████▒▒▒▒▒▒▒▒▒",c,0),bl+"#AGAR");
				}
			}
		}
		boolean nc = (pr.getCellColor().length() == 0);
		boolean r = (p.hasPermission("Agarmc.cells.color."+pr.getCellColor())|p.hasPermission("Agarmc.ADMIN"));
		boolean n = (p.hasPermission("Agarmc.cells.nametag")|p.hasPermission("Agarmc.ADMIN"));
		for(int i = 0;i<btns.length;i++){
			for(int x:btns[i]){
				pr.getMain().gF().setUpItem(inv, x, new ItemStack(Material.STAINED_GLASS_PANE,1,(r&!nc|((i==0)|(i==2)))?dclr[i].getWoolData():DyeColor.WHITE.getWoolData()),(r&!nc|((i==0)|(i==2)))?msg[i]+((i==2)&&(!n)?re+" [ Locked ]":""):re+bo+"Unable To Start The Game","",(nc&i==1)?re+"You'll need to choose a cell color":(r|((i==0)|(i==2)))?((i==2)?ye+"Nametag: "+wh+bo+(pr.isNameOn()?"Visible":"Not Visible"):""):ye+"A personal appeal from",(nc&i==1)?re+"before we can start the game.":(r|((i==0)|(i==2)))?"":ye+"an unnamed cell:",(i==2)?gr+"Hiding your cell nametag further":"",(nc&i==1)?"":(r|((i==0)|(i==2)))?((i==2)?gr+"helps you sneak up on other players!":""):ye+bo+"Please purchase this cell",(nc&i==1)?"":(r|((i==0)|(i==2)))?"":ye+bo+"color from the server store",(nc&i==1)?"":(r|((i==0)|(i==2)))?((i==2)&(!n)?re+"To hide your cell nametag, you'll":""):ye+bo+"in order to use it, thanks!",((i==2)&(!n)?re+"need to purchase it from the store.":""),bl+"#AGAR");
			}
		}
	}
	
	/**
	 * Private Text Replacement
	 * Helper Function
	 * 
	 * @param s
	 * @param c
	 * @param sw
	 * @return
	 */
	private String rp(String s, ChatColor c,int sw){
		return s.replaceAll("▒", wh+"▒").replaceAll("█",c+"█").replaceAll("▓",(sw==0)?gr+"▓":(sw==1)?re+"▓":"");
	}
	
	/**
	 * Returns The Inventory View For
	 * This User Interface
	 * 
	 * @return
	 */
	public Inventory getUI(){
		return inv;
	}
	
	/**
	 * Unregisters Listener When We're
	 * Finished With The User Interface.
	 * 
	 */
	private void unreg(){
		InventoryClickEvent.getHandlerList().unregister(this);
		InventoryCloseEvent.getHandlerList().unregister(this);
	}
}

/*
 * © 2016 Brandon Mueller
 * DO NOT DE-COMPILE THIS SOFTWARE OR ATTEMPT ANY FORM OF REVERSE ENGINEERING!
 */
