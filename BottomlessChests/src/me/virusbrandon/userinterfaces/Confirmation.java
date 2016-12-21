package me.virusbrandon.userinterfaces;

import me.virusbrandon.bottomlesschests.Main;

import org.bukkit.*;
import org.bukkit.inventory.*;

public class Confirmation {
	private String bl = ChatColor.BLACK+"",bo=ChatColor.BOLD+"",re=ChatColor.RED+"",gr=ChatColor.GREEN+"",ul=ChatColor.UNDERLINE+"",bu=ChatColor.BLUE+"";
	private String sss;
	private Inventory delinv = Bukkit.createInventory(null, 54,bl+bo + "Empty Trash Bin?");
	private int[] icon2 = new int[]{0,1,2,3,4,5,6,7,8,17,26,35,44,53,52,51,50,49,48,47,46,45,36,27,18,9};
	private Main main;
	
	/**
	 * Confirmation Object Constructor
	 * 
	 * 
	 */
	public Confirmation(Main main){
		this.main = main;
		this.sss = bu+main.getFact().draw("─",20,"");
	}
	
	/**
	 * Returns The Confirmation User Interface View
	 * So The Player Can Decide If They Want To Manually
	 * Empty The Chest's Trash Bin.
	 * 
	 * @return
	 * 
	 * 
	 */
	@SuppressWarnings("deprecation")
	public Inventory showDel(){
		for(int x = 0;x<26;x++){
			main.getFact().setUpItem(delinv,icon2[x],new ItemStack(Material.STAINED_GLASS_PANE,1,DyeColor.BLACK.getWoolData()),bl+".",bl+"#CHEST");
		}
		int[][]i=new int[][]{{10,11,12,19,20,21,28,29,30,37,38,39,31,40,160},{14,15,16,23,24,25,32,33,34,41,42,43,13,22,160}};String[]ss=new String[]{gr+bo+ul+main.gL().gT("Confirm"),re+bo+ul+main.gL().gT("Cancel")};
		for(int x=0;x<i.length;x++){
			for(int y=0;y<i[x].length-1;y++){
				main.getFact().setUpItem(delinv,i[x][y],new ItemStack(i[x][i[x].length-1],1,((x==0)?DyeColor.LIME:(x==1)?DyeColor.RED:DyeColor.BLACK).getWoolData()),ss[x],"",sss,re+bo+main.gL().gT("Conf_Del"),re+bo+main.gL().gT("Conf_NoRec"),sss,bl+"#CHEST");
			}
		}	
		return delinv;
	}
	
	/*
	 * © 2016 Brandon Mueller
	 * DO NOT DE-COMPILE THIS SOFTWARE OR ATTEMPT ANY FORM OF REVERSE ENGINEERING!
	 */
}
