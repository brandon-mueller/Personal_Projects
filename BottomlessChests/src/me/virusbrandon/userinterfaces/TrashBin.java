package me.virusbrandon.userinterfaces;

import me.virusbrandon.bottomlesschests.*;

import org.bukkit.*;
import org.bukkit.inventory.*;

public class TrashBin {
	private Chest chest;
	private Inventory inv;
	private String bl = ChatColor.BLACK+"",bo=ChatColor.BOLD+"",re=ChatColor.RED+"",aq=ChatColor.AQUA+"",bu=ChatColor.BLUE+"",ul=ChatColor.UNDERLINE+"";
	private String ss;
	private Main main;
	
	/**
	 * TrashBin Object Constructor
	 * 
	 * 
	 */
	public TrashBin(Chest chest, Main main){
		this.chest = chest;
		this.main = main;
		this.ss = bu+main.getFact().draw("─", 20, "");
		inv = Bukkit.createInventory(null, 54,bl+bo+main.gL().gT("Trash_Title"));
	}
	
	/**
	 * Returns The Inventory So The Player
	 * Can Open The Trash Bin And View Its Contents.
	 * 
	 * 
	 */
	public Inventory showTrash(){
		return inv;
	}
	
	/**
	 * Updates The User Interface
	 * 
	 * 
	 */
	@SuppressWarnings("deprecation")
	public void update(){
		for(int x=0;x<54;x++){
			main.getFact().setUpItem(inv,x,new ItemStack(chest.getMain().getSettings().getNoButton(),1,DyeColor.RED.getWoolData()),re+bo+ul+main.gL().gT("Exit"),"",ss,aq+bo+main.gL().gT("Important"),aq+main.gL().gT("TrashBin_Dis"),aq+main.gL().gT("TrashBin_Into"),aq+main.gL().gT("TrashBin_Send"),aq+main.gL().gT("TrashBin_Back"),"",re+bo+main.gL().gT("TrashBin_Note"),re+main.gL().gT("TrashBin_Leave"),re+main.gL().gT("TrashBin_All"),re+main.gL().gT("TrashBin_Perm"),re+main.gL().gT("TrashBin_Care"),ss,bl+"#Chest");
		}
		for(int x=3;x<6;x++){
			main.getFact().setUpItem(inv,x,new ItemStack(chest.getMain().getSettings().getTrashButton(),1),re+bo+ul+main.gL().gT("TrashBin_Empty"),bl+"#EMPTY");
		}
		for(int x=48;x<51;x++){
			main.getFact().setUpItem(inv,x,new ItemStack(chest.getMain().getSettings().getTrashButton(),1),re+bo+ul+main.gL().gT("TrashBin_Empty"),bl+"#EMPTY");
		}
		for(int x=9;x<(chest.getTrash().size()+9);x++){
			inv.setItem(x,chest.getTrash().get(x-9));
		}
	}
	
	/*
	 * © 2016 Brandon Mueller
	 * DO NOT DE-COMPILE THIS SOFTWARE OR ATTEMPT ANY FORM OF REVERSE ENGINEERING!
	 */
}
