package me.virusbrandon.ui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.virusbrandon.smashverse.Main;

public class AdminWindow {
	private Main main;
	private UiTimer driver;
	private String bo=ChatColor.BOLD+"",bl=ChatColor.BLACK+"",wh=ChatColor.WHITE+"",bu=ChatColor.BLUE+"";
	private Inventory inv;
	private int[]s=new int[]{21,22,23};
	private String[]ss=new String[]{"Lobby","Join","Origin"};
	
	public AdminWindow(Main main){
		this.main = main;
		inv = Bukkit.createInventory(null, 54,"[SV] "+bl+bo+"Admin Window");
		driver = new UiTimer(this,1);
		driver.start(1);
	}
	
	@SuppressWarnings("deprecation")
	protected boolean refresh(){
		for(int x=0;x<55;x++){
			main.gF().setUpItem(inv, x, new ItemStack(Material.STAINED_GLASS_PANE,1,DyeColor.GRAY.getData()),bu+bo+"Super Smash Verse Software Console:",main.aM().getSC());
		}
		for(int i:main.getSt().getBtns()){
			main.gF().setUpItem(inv, i, new ItemStack(Material.END_CRYSTAL,1)," ",bl+"#STATUS");
		}
		for(int x=0;x<s.length;x++){
			main.gF().setUpItem(inv, s[x], new ItemStack(Material.MAP,1),wh+bo+"Set "+ss[x]+" Location","",wh+"Sets The "+ss[x]+" Location To",wh+"To Right Where You Are",wh+"Standing.",bl+"#"+ss[x].toUpperCase());
		}
		main.gF().setUpItem(inv, 30, new ItemStack(Material.MAP,1),wh+bo+"Available Arena Templates:",main.aM().getTemplateManager().getTNameList());
		main.gF().setUpItem(inv, 31, new ItemStack(Material.MAP,1),wh+bo+"Show Map Build Region",bl+"#BUILD");
		return true;
	}
	
	public Inventory getUi(){
		return inv;
	}
	
	protected void stopUi(){
		driver.stop();
	}
	/*
	 * © 2016 Brandon Mueller
	 * DO NOT DE-COMPILE THIS SOFTWARE OR ATTEMPT ANY FORM OF REVERSE ENGINEERING!
	 */
}