package me.virusbrandon.userinterfaces;

import org.bukkit.*;
import org.bukkit.inventory.*;

import me.virusbrandon.bottomlesschests.Main;

public class ScrollbarAdjuster {
	private Main main;
	private Inventory inv;
	private String bl=ChatColor.BLACK+"",bo=ChatColor.BOLD+"",re=ChatColor.RED+"",gr=ChatColor.GREEN+"",st=ChatColor.STRIKETHROUGH+"",wh=ChatColor.WHITE+"",aq=ChatColor.AQUA+"",bu=ChatColor.BLUE+"";
	@SuppressWarnings("deprecation")
	private Byte lb = DyeColor.LIME.getWoolData(),rb = DyeColor.RED.getWoolData();
	private Material sgp = Material.STAINED_GLASS_PANE;
	private String ss;
	
	/**
	 * The ScrollbarAdjuster Window Constructor
	 * 
	 * @param main
	 */
	public ScrollbarAdjuster(Main main){
		this.main = main;
		this.ss = bu+main.getFact().draw("─",20,"");
		inv = Bukkit.createInventory(null, 54,bl+bo+main.gL().gT("Scroll_Title"));
		refresh();
	}
	
	/**
	 * Updates The State Of
	 * The Interface
	 * 
	 */
	@SuppressWarnings("deprecation")
	public void refresh(){
		for(int x = 0;x<54;x++){
			main.getFact().setUpItem(inv,x,new ItemStack(sgp,1,DyeColor.BLACK.getWoolData())," ",bl+"#CHEST");
		}
		String sel = wh+bo+main.getSettings().getScrollStrings()[main.getSettings().getScrollFocus()];
		String scsl = wh+main.gL().gT("Scroll_CurSel");
		String scvl = wh+main.gL().gT("Scroll_CurVal");
		String val = wh+bo+main.getSettings().getScrollbarSettings()[main.getSettings().getScrollFocus()];
		if(main.getSettings().getScrollFocus()>0){
			main.getFact().setUpItem(inv,21,new ItemStack(sgp,1,lb),gr+bo+main.gL().gT("Scroll_SOL"),"",ss,scsl,sel,"",scvl,val,ss,bl+"#sLEFT",bl+"#CHEST");
		} else {
			main.getFact().setUpItem(inv,21,new ItemStack(sgp,1,rb),re+bo+st+main.gL().gT("Scroll_CSL"),"",ss,scsl,sel,"",scvl,val,ss,bl+"",bl+"#CHEST");
		}
		if(main.getSettings().getScrollFocus()<2){
			main.getFact().setUpItem(inv,23,new ItemStack(sgp,1,lb),gr+bo+main.gL().gT("Scroll_SOR"),"",ss,scsl,sel,"",scvl,val,ss,bl+"#sRIGHT",bl+"#CHEST");
		} else {
			main.getFact().setUpItem(inv,23,new ItemStack(sgp,1,rb),re+bo+st+main.gL().gT("Scroll_CSR"),"",ss,scsl,sel,"",scvl,val,ss,bl+"",bl+"#CHEST");
		}
		int current = main.getSettings().getScrollbarSettings()[main.getSettings().getScrollFocus()];
		if(current<100){
			main.getFact().setUpItem(inv,13,new ItemStack(sgp,1,lb),gr+bo+main.gL().gT("Scroll_INC"),"",ss,scsl,sel,"",scvl,val,ss,bl+"#sUP",bl+"#CHEST");
		} else {
			main.getFact().setUpItem(inv,13,new ItemStack(sgp,1,rb),re+bo+st+main.gL().gT("Scroll_CI"),"",ss,scsl,sel,"",scvl,val,ss,bl+"#CHEST");
		}
		if(current>1){
			main.getFact().setUpItem(inv,31,new ItemStack(sgp,1,lb),gr+bo+main.gL().gT("Scroll_DEC"),"",ss,scsl,sel,"",scvl,val,ss,bl+"#sDOWN",bl+"#CHEST");
		} else {
			main.getFact().setUpItem(inv,31,new ItemStack(sgp,1,rb),re+bo+st+main.gL().gT("Scroll_CD"),"",ss,scsl,sel,"",scvl,val,ss,bl+"#CHEST");
		}
		main.getFact().setUpItem(inv,22,new ItemStack(Material.ENCHANTED_BOOK,1),aq+bo+main.gL().gT("Scroll_Mid"),"",ss,aq+main.gL().gT("Scroll_Pan"),aq+main.gL().gT("Scroll_Adj"),aq+main.gL().gT("Scroll_Nav"),aq+main.gL().gT("Scroll"),ss,bl+"#CHEST");
		for(int x=45;x<54;x++){
			main.getFact().setUpItem(inv,x,new ItemStack(sgp,1,rb),re+bo+main.gL().gT("Back_To_Admin"),bl+"#CHEST");
		}
	}
	
	/**
	 * Returns This Interface To
	 * Be Opened By A Player
	 * 
	 * @return
	 */
	public Inventory openMenu(){
		return inv;
	}
	
	/*
	 * © 2016 Brandon Mueller
	 * DO NOT DE-COMPILE THIS SOFTWARE OR ATTEMPT ANY FORM OF REVERSE ENGINEERING!
	 */
}
