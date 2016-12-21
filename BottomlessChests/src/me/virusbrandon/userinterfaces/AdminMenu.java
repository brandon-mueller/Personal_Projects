package me.virusbrandon.userinterfaces;

import me.virusbrandon.bottomlesschests.Main;

import org.bukkit.*;
import org.bukkit.inventory.*;

public class AdminMenu {
	private int MAX_ICON = 431;
	private int[] up = new int[]{10,11,12,14,15,16};
	private int[] slots= new int[]{19,20,21,23,24,25};
	private int[] down = new int[]{28,29,30,32,33,34};
	private int[] current = new int[]{2,2,2,0,0,0};
	private int[] close = new int[]{9,18,27,36,45,46,47,48,49,50,51,52,53,17,26,35,44};
	private int[] settings = new int[]{0,8,1,2,3,4,5,6,7};
	private Inventory inv;
	private Main main;
	private String bl = ChatColor.BLACK+"",bo=ChatColor.BOLD+"",re=ChatColor.RED+"",gr=ChatColor.GREEN+"",aq=ChatColor.AQUA+"",wh=ChatColor.WHITE+"",st=ChatColor.STRIKETHROUGH+"",ul=ChatColor.UNDERLINE+"",bu=ChatColor.BLUE+"",lp=ChatColor.LIGHT_PURPLE+"";
	private ChatColor opt = ChatColor.WHITE;
	private String[] lores = new String[]{bl+"#OK",bl+"#NO",bl+"#TRASH",bl+"#OPEN",bl+"#SCROLL",bl+"#DESTROY"};
	private String[] message;
	String ss;
	private boolean temp;
	
	/**
	 * AdminMenu Object Constructor
	 * 
	 * @param main
	 * 
	 * 
	 */
	public AdminMenu(Main main){
		this.main = main;
		this.ss = bu+main.getFact().draw("─",20, "");
		inv = Bukkit.createInventory(null, 54,bl+bo+main.gL().gT("Admin_Title"));
		this.message = new String[]{main.gL().gT("Admin_Ok"),main.gL().gT("Admin_No"),main.gL().gT("Admin_Trash"),main.gL().gT("Admin_ChestOpen"),main.gL().gT("Admin_Scroll"),main.gL().gT("Admin_Destroy")};
		for(int x = 0;x<main.getSettings().getSettingPositions().length;x++){
			current[x] = main.getSettings().getSettingPositions()[x];
		}
		setUpButtons();
	}
	
	/**
	 * Sets Up The Button On The Menu
	 * 
	 * 
	 */
	@SuppressWarnings("deprecation")
	public void setUpButtons(){
		try{
			this.temp = main.getSettings().getTrashBinEnabled();
			for(int x=0;x<54;x++){
				main.getFact().setUpItem(inv,x,new ItemStack(Material.STAINED_GLASS_PANE,1,DyeColor.BLUE.getWoolData()),wh+bo+main.gL().gT("Admin_Inf_Title"),"",ss,"",re+"- "+lp+main.gL().gT("Admin_Inf_Name")+" "+gr+main.getInfo("Name"),re+"- "+lp+main.gL().gT("Admin_Inf_Author")+" "+gr+main.getInfo("Author"),re+"- "+lp+main.gL().gT("Admin_Inf_Version")+" "+gr+main.getInfo("Version"),re+"- "+lp+main.gL().gT("Admin_Inf_SVersion")+" "+gr+main.gFSV(),"",ss,"",bl+"#ADMIN",bl+"#CHEST");
			}
			for(int x=0;x<settings.length;x++){
				((x==0)?main.getFact().setUpItem(inv,settings[x],new ItemStack(Material.MAP,1),re+bo+main.gL().gT("Admin_Read"),aq+main.gL().gT("Admin_CurSet")+" "+opt+main.getSettings().getMethodString(main.getSettings().getFileReadMethod()),"",ss,wh+main.gL().gT("Admin_ReadDesc"),wh+main.gL().gT("Admin_ReadDesc2"),ss,bl+"#READ",bl+"#CHEST"):(x==1)?main.getFact().setUpItem(inv,settings[x],new ItemStack(Material.BOOK_AND_QUILL,1),re+bo+main.gL().gT("Admin_Write"),aq+main.gL().gT("Admin_CurSet")+" "+opt+main.getSettings().getMethodString(main.getSettings().getFileWriteMethod()),"",ss,wh+main.gL().gT("Admin_WriteDesc"),wh+main.gL().gT("Admin_WriteDesc2"),ss,bl+"#WRITE",bl+"#CHEST"):(x==2)?main.getFact().setUpItem(inv,settings[x],new ItemStack(Material.SIGN,1),re+bo+main.gL().gT("Admin_TgAct"),aq+main.gL().gT("Admin_CurSet")+" "+opt+main.getSettings().getActionBarString(main.getSettings().getActionBar()),"",ss,wh+main.gL().gT("Admin_Act"),wh+main.gL().gT("Admin_ActDesc"),wh+main.gL().gT("Admin_ActDesc2"),ss,bl+"#ACTIONBAR",bl+"#CHEST"):(x==3)?main.getFact().setUpItem(inv,settings[x],new ItemStack(Material.DIAMOND_PICKAXE,1),re+bo+main.gL().gT("Admin_AutoIP"),aq+main.gL().gT("Admin_CurSet")+" "+opt+main.getSettings().getBlockPickupString(main.getSettings().getAutoPickup()),"",ss,wh+main.gL().gT("Admin_AutoIPDesc"),wh+main.gL().gT("Admin_AutoIPDesc2"),wh+main.gL().gT("Admin_AutoIPDesc3"),ss,bl+"#AUTO",bl+"#CHEST"):(x==4)?main.getFact().setUpItem(inv,settings[x],new ItemStack(Material.ENDER_PEARL,1),re+bo+main.gL().gT("Admin_PWI"),aq+main.gL().gT("Admin_CurSet")+" "+opt+main.getSettings().getPerWorldItemString(main.getSettings().getPerWorldItems()),"",ss,wh+main.gL().gT("Admin_PWIDesc"),wh+main.gL().gT("Admin_PWIDesc2"),ss,bl+"#PER",bl+"#CHEST"):(x==5)?main.getFact().setUpItem(inv,settings[x],new ItemStack(Material.WORKBENCH),re+bo+main.gL().gT("Admin_CS"),"",ss,wh+main.gL().gT("Admin_CSDesc"),wh+main.gL().gT("Admin_CSDesc2"),ss,bl+"#BAR",bl+"#CHEST"):(x==6)?main.getFact().setUpItem(inv,settings[x],new ItemStack(Material.CAULDRON_ITEM),re+bo+main.gL().gT("Admin_TTB"),aq+main.gL().gT("Admin_CurSet")+" "+opt+main.getSettings().getTrashBinEnabledString(temp),"",ss,wh+main.gL().gT("Admin_TD1")+wh+bo+main.gL().gT("Admin_TD2")+re+(temp?"":wh+bo+main.gL().gT("Admin_TD3")),wh+main.gL().gT("Admin_TD4")+((!temp)?main.gL().gT("Admin_TD5"):main.gL().gT("Admin_TD6")),wh+((!temp)?main.gL().gT("Admin_TD7"):""),ss,bl+"#ENTRASH",bl+"#CHEST"):(x==7)?main.getFact().setUpItem(inv,settings[x],new ItemStack(Material.BOOK),re+bo+main.gL().gT("Admin_CLT"),aq+main.gL().gT("Admin_CLF")+" "+opt+main.gL().getFileName(),"",ss,wh+main.gL().gT("Admin_CLFDesc"),wh+main.gL().gT("Admin_CLFDesc2"),wh+main.gL().gT("Admin_CLFDesc3"),ss,bl+"#LANG",bl+"#CHEST"):(x==8)?main.getFact().setUpItem(inv, settings[x], new ItemStack(Material.SKULL_ITEM,1), re+bo+main.gL().gT("Admin_ODSTitle"),aq+main.gL().gT("Admin_CurSet")+" "+opt+main.getSettings().getOnDeathPercentString(),aq+main.gL().gT("Admin_Action")+" "+opt+main.getSettings().getOnDeathItemLossActionString(),"",ss,wh+main.gL().gT("Admin_ODSDesc"),wh+main.gL().gT("Admin_ODSDesc2"),wh+main.gL().gT("Admin_ODSDesc3"),ss,bl+"#ONDEATH",bl+"#CHEST"):null).toString();
			}
			main.getFact().setUpItem(inv,13,new ItemStack(Material.HOPPER,1),re+bo+main.gL().gT("Admin_SORT1"),aq+main.gL().gT("Admin_CurSet")+" "+opt+main.getSettings().getItemSortOptionString(),bl+"#SORT",bl+"#CHEST");
			main.getFact().setUpItem(inv,22,new ItemStack(Material.ENCHANTMENT_TABLE,1),re+bo+main.gL().gT("Admin_TMaint"),aq+main.gL().gT("Admin_CurSet")+" "+opt+(main.getSettings().isMaintenance()?gr+bo+main.gL().gT("Admin_EN"):re+bo+main.gL().gT("Admin_DIS")),bl+"#MAINTENANCE",bl+"#CHEST");
			main.getFact().setUpItem(inv,31,new ItemStack(Material.CHEST,1),re+bo+main.gL().gT("Admin_OMaint"),bl+"#MAINTMEN",bl+"#CHEST");
			for(int x = 0;x<up.length;x++){
				if(x<3){
					if(current[x]>2){
						main.getFact().setUpItem(inv,up[x],new ItemStack(Material.STAINED_GLASS_PANE,(x+1),DyeColor.LIME.getWoolData()),gr+bo+main.gL().gT("Admin_Up"),wh+current[x]+"/"+MAX_ICON,"",gr+bo+message[x]+" "+main.gL().gT("Admin_BI"),aq+bo+main.gL().gT("Admin_CurSel"),aq+Material.getMaterial(current[x]).name(),lores[x],bl+"#Chest");
					} else {
						main.getFact().setUpItem(inv,up[x],new ItemStack(Material.STAINED_GLASS_PANE,(x+1),DyeColor.RED.getWoolData()),re+bo+st+main.gL().gT("Admin_Up"),wh+current[x]+"/"+MAX_ICON,"",gr+bo+message[x]+" "+main.gL().gT("Admin_BI"),aq+bo+main.gL().gT("Admin_CurSel"),aq+Material.getMaterial(current[x]).name(),lores[x],bl+"#Chest");
					}
				} else {
					if(current[x]>0){
						main.getFact().setUpItem(inv,up[x],new ItemStack(Material.STAINED_GLASS_PANE,(x+1),DyeColor.LIME.getWoolData()),gr+bo+main.gL().gT("Admin_Up"),wh+(current[x]+1)+"/"+Sound.values().length,"",gr+bo+message[x]+" "+main.gL().gT("Admin_S"),aq+bo+main.gL().gT("Admin_CurSel"),aq+Sound.values()[current[x]].name(),lores[x],bl+"#Chest");
					} else {
						main.getFact().setUpItem(inv,up[x],new ItemStack(Material.STAINED_GLASS_PANE,(x+1),DyeColor.RED.getWoolData()),re+bo+st+main.gL().gT("Admin_Up"),wh+(current[x]+1)+"/"+Sound.values().length,"",gr+bo+message[x]+" "+main.gL().gT("Admin_S"),aq+bo+main.gL().gT("Admin_CurSel"),aq+Sound.values()[current[x]].name(),lores[x],bl+"#Chest");
					}
				}
			}
			for(int x = 0;x<slots.length;x++){
				if(x<3){
					if(main.getSettings().getSettingPositions()[x]==current[x]){
						main.getFact().setUpItem(inv,slots[x],new ItemStack(Material.BARRIER,1),Material.getMaterial(current[x]).name(),"",re+bo+main.gL().gT("Admin_CurSet2"),re+bo+"As The "+message[x]+" "+main.gL().gT("Admin_BI"),aq+Material.getMaterial(current[x]).name(),lores[x],bl+"#Chest");
					} else {
						main.getFact().setUpItem(inv,slots[x],new ItemStack(Material.getMaterial(current[x]),1),Material.getMaterial(current[x]).name(),"",aq+bo+main.gL().gT("Admin_ClkToSet"),aq+bo+main.gL().gT("Admin_AST")+" "+message[x]+" "+main.gL().gT("Admin_BI"),aq+Material.getMaterial(current[x]).name(),bl+"#ICON",lores[x],bl+"#Chest");
					}
				} else {
					if(main.getSettings().getSettingPositions()[x]==current[x]){
						main.getFact().setUpItem(inv,slots[x],new ItemStack(Material.BARRIER,1),Sound.values()[current[x]].name(),"",re+bo+main.gL().gT("Admin_CurSet2"),re+bo+main.gL().gT("Admin_AST")+" "+message[x]+" "+main.gL().gT("Admin_S"),aq+Sound.values()[current[x]].name(),lores[x],bl+"#Chest");
					} else {
						main.getFact().setUpItem(inv,slots[x],new ItemStack(Material.JUKEBOX,1),Sound.values()[current[x]].name(),"",aq+bo+main.gL().gT("Admin_ClkToSet"),aq+bo+main.gL().gT("Admin_AST")+" "+message[x]+" "+main.gL().gT("Admin_S"),aq+Sound.values()[current[x]].name(),bl+"#SOUND",lores[x],bl+"#Chest");
					}
				}
			}
			for(int x = 0;x<down.length;x++){
				if(x<3){
					if(current[x]<MAX_ICON){
						main.getFact().setUpItem(inv,down[x],new ItemStack(Material.STAINED_GLASS_PANE,(x+1),DyeColor.LIME.getWoolData()),gr+bo+main.gL().gT("Admin_Dwn"),wh+current[x]+"/"+MAX_ICON,"",gr+bo+message[x]+" "+main.gL().gT("Admin_BI"),aq+bo+main.gL().gT("Admin_CurSel"),aq+Material.getMaterial(current[x]),lores[x],bl+"#Chest");
					} else {
						main.getFact().setUpItem(inv,down[x],new ItemStack(Material.STAINED_GLASS_PANE,(x+1),DyeColor.RED.getWoolData()),re+bo+st+main.gL().gT("Admin_Dwn"),wh+current[x]+"/"+MAX_ICON,"",gr+bo+message[x]+" "+main.gL().gT("Admin_BI"),aq+bo+main.gL().gT("Admin_CurSel"),aq+Material.getMaterial(current[x]),lores[x],bl+"#Chest");
					}
				} else {
					if(current[x]<(Sound.values().length-1)){
						main.getFact().setUpItem(inv,down[x],new ItemStack(Material.STAINED_GLASS_PANE,(x+1),DyeColor.LIME.getWoolData()),gr+bo+main.gL().gT("Admin_Dwn"),wh+(current[x]+1)+"/"+Sound.values().length,"",gr+bo+message[x]+" "+main.gL().gT("Admin_S"),aq+bo+main.gL().gT("Admin_CurSel"),aq+Sound.values()[current[x]].name(),lores[x],bl+"#Chest");
					} else {
						main.getFact().setUpItem(inv,down[x],new ItemStack(Material.STAINED_GLASS_PANE,(x+1),DyeColor.RED.getWoolData()),re+bo+st+main.gL().gT("Admin_Dwn"),wh+(current[x]+1)+"/"+Sound.values().length,gr+bo+message[x]+" "+main.gL().gT("Admin_S"),aq+bo+main.gL().gT("Admin_CurSel"),aq+Sound.values()[current[x]].name(),lores[x],bl+"#Chest");
					}
				}
			}
			for(int x:close){
				main.getFact().setUpItem(inv,x,new ItemStack(Material.STAINED_GLASS_PANE,1,DyeColor.RED.getWoolData()),re+bo+ul+main.gL().gT("Exit"),bl+"#Chest");
			}
		}catch(Exception e1){}
	}
	
	/**
	 * Scrolls The Particular Option Menu
	 * That Is Currently In Focus
	 * 
	 * @param lore
	 * @param direction
	 * @return
	 * 
	 * 
	 */
	@SuppressWarnings("deprecation")
	public Sound scroll(String lore, int direction){
		for(int x = 0;x<lores.length;x++){
			if(lore.equalsIgnoreCase(lores[x])){
				current[x]+=direction;
				try{
					if(x<3){	
						main.getFact().setUpItem(inv,slots[x],new ItemStack(Material.getMaterial(current[x]),1),Material.getMaterial(current[x]).name(),"",aq+bo+main.gL().gT("Admin_ClkToSet"),aq+bo+main.gL().gT("Admin_AST")+" "+message[x]+" "+main.gL().gT("Admin_BI"),aq+Material.getMaterial(current[x]).name(),bl+"#ICON",lores[x],bl+"#Chest");	
						if(inv.getItem(slots[x])==null){
							throw new Exception();
						}
					}
				}catch(Exception e1){
					scroll(lore,direction);
				}
				return main.getSettings().getScrollSound();
			}
		}
		return null;
	}
	
	/**
	 * Opens The Admin Menu
	 * 
	 * @return
	 * 
	 * 
	 */
	public Inventory openMenu(){
		return inv;
	}
	
	/**
	 * Returns The Current Admin Menu
	 * Positions
	 * 
	 * 
	 */
	public int[] getCurrents(){
		return current;
	}
	
	/*
	 * © 2016 Brandon Mueller
	 * DO NOT DE-COMPILE THIS SOFTWARE OR ATTEMPT ANY FORM OF REVERSE ENGINEERING!
	 */
}
