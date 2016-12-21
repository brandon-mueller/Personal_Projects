package me.virusbrandon.userinterfaces;

import java.io.File;
import java.util.*;

import org.bukkit.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;

import me.virusbrandon.bc_utils.Maint_Sorter;
import me.virusbrandon.bottomlesschests.*;

public class MaintenanceWindow {
	
	private Main main;
	private ArrayList<String> chestFiles = new ArrayList<>();
	private Inventory inv;
	private int[] slots = new int[]{1,2,3,4,5,6,7,10,11,12,13,14,15,16,19,20,21,22,23,24,25,28,29,30,31,32,33,34,37,38,39,40,41,42,43,46,47,48,49,50,51,52};
	private int[][] btns = new int[][]{{0,9,18,27,36,45,8,17,26,35,44,53},{0,9,8,17},{36,45,44,53}};
	@SuppressWarnings("deprecation")
	private Byte[] btnc = new Byte[]{DyeColor.YELLOW.getWoolData(),DyeColor.LIME.getWoolData(),DyeColor.RED.getWoolData()};
	private int slot = 0;
	private String bu=ChatColor.BLUE+"",wh=ChatColor.WHITE+"",bo=ChatColor.BOLD+"",gr=ChatColor.GREEN+"",re=ChatColor.RED+"",ye=ChatColor.YELLOW+"",bl=ChatColor.BLACK+"";
	private String method = "";
	private String ss;
	
	/**
	 * Maintenance Mode GUI Constructor
	 * 
	 * @param main
	 */
	public MaintenanceWindow(Main main){
		this.main = main;
		this.ss = bu+main.getFact().draw("─",25,"");
		inv = Bukkit.createInventory(null, 54,bl+bo+"[BTC] Maintenance");
		updInfo();
	}
	
	/**
	 * Scrolls The Menu Up And Down
	 * 
	 * @param i
	 */
	public void scroll(int i){
		slot+=(i<0)?(((slot-7)>=0)?i:0):(i>0)?((((slot+7)+41)<(chestFiles.size()+7))?i:0):0;
	}
	
	public MaintenanceWindow updInfo(){
		try{
			YamlConfiguration yml = null;
			method = ((!main.getSettings().getFileReadMethod())?"Name":"UUID");
			File file = new File(main.getDataFolder(),"Chests/"+method);
			chestFiles.clear();
			for(File chest:file.listFiles()){
				new YamlConfiguration();
				yml = YamlConfiguration.loadConfiguration(chest);
				String s = yml.getString("C.N");
				if((s!=null)&&(!s.equalsIgnoreCase(""))){
					chestFiles.add(s);
				}
			}
			this.chestFiles = new Maint_Sorter().sort(chestFiles);
		}catch(Exception e1){}
		return refresh();
	}
	
	/**
	 * Refreshes The Maintenance Mode
	 * GUI And Displays New Information Based
	 * On Changes Of State For User Chests.
	 * 
	 */
	@SuppressWarnings("deprecation")
	public MaintenanceWindow refresh(){
		try{
			for(int x:slots){
				main.getFact().setUpItem(inv, x, new ItemStack(Material.STAINED_GLASS_PANE,1,DyeColor.GRAY.getWoolData())," ");
			}
			for(int x = 0;x<slots.length;x++){
				if((x+slot)<chestFiles.size()){
					ItemStack st = new ItemStack(Material.CHEST,1);
					String s = chestFiles.get(x+slot);
					boolean stat = isLoaded(s);
					main.getFact().setUpItem(inv,slots[x], st,s,"",ss,"",wh+bo+main.gL().gT("Maint_Status")+" "+(stat?gr+main.gL().gT("Maint_Online"):re+main.gL().gT("Maint_Offline")),(!main.getSettings().isMaintenance())?re+main.gL().gT("Maint_Ena")+" "+((stat)?main.gL().gT("Maint_InvSee"):main.gL().gT("Maint_Load"))+" "+main.gL().gT("Maint_ThChst"):gr+main.gL().gT("Maint_Click")+" "+(stat?main.gL().gT("Maint_InvSee"):main.gL().gT("Maint_Load"))+" "+main.gL().gT("Maint_ThChst")+"!","",bl+(stat?"#INVSEE":"#LOAD"),ss);
				}
			}
			if((slot+34)>=chestFiles.size()){
				scroll(-7);
			}
		}catch(Exception e1){
			for(int i:slots){
				main.getFact().setUpItem(inv, i, new ItemStack(Material.STAINED_GLASS_PANE,1,DyeColor.BLACK.getWoolData())," "," ");
			}
		}
		setUpStuff();
		return this;
	}
	
	/**
	 * Sets Up The Basic Buttons On The GUI
	 * 
	 */
	private void setUpStuff(){
		try{
			for(int x=0;x<btns.length;x++){
				for(int i:btns[x]){
					main.getFact().setUpItem(inv,i,new ItemStack(Material.STAINED_GLASS_PANE,1,(x==1)?((slot>0)?btnc[1]:btnc[2]):(x==2)?(((slot+41)<chestFiles.size())?btnc[1]:btnc[2]):btnc[0]),(x==0)?ye+bo+main.gL().gT("Maint_Return"):(x==1)?((slot>0)?gr+bo+main.gL().gT("Scroll_Up"):re+bo+main.gL().gT("Cannot_SU")):(x==2)?(((slot+41)<chestFiles.size())?gr+bo+main.gL().gT("Scroll_Down"):re+bo+main.gL().gT("Cannot_SD")):"","",ss,gNR(),gAR(),ss);
				}
			}	
		}catch(Exception e1){}
	}
	
	/**
	 * Returns What Range Of Numbers
	 * Are Being Shown In The Maintenance GUI
	 * 
	 */
	private String gNR(){
		String sss = gr+main.gL().gT("Maint_ShowChests")+" "+re+"("+gr+(slot+1)+" "+re+main.gL().gT("ChestView_Thru")+" "+gr+((slot+1)+41)+re+")"+wh+"/"+gr+chestFiles.size();
		return sss;
	}
	
	/**
	 * Return What Range Of Names
	 * Are Bring Shown In The Maintenance GUI;
	 * 
	 */
	private String gAR(){
		return gr+main.gL().gT("Maint_ShowNames")+" "+re+"("+gr+chestFiles.get(slot).substring(0,3).toUpperCase()+re+" "+main.gL().gT("ChestView_Thru")+" "+gr+chestFiles.get(((slot+41)>chestFiles.size())?chestFiles.size()-1:(slot+41)).substring(0,3).toUpperCase()+re+")";
	}
	
	
	/**
	 * Determines If A Particular Chest
	 * Is Currently Loaded or Not.
	 * 
	 * @param s
	 * @return
	 */
	public boolean isLoaded(String s){
		for(UUID u:main.getChestKeys()){
			Chest ch = main.getChests().get(u);
			if(ch.getName().equalsIgnoreCase(s)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Opens The GUI For A Player
	 * 
	 * @param p
	 */
	public void openUI(Player p){
		p.openInventory(inv);
	}
	
	/*
	 * © 2016 Brandon Mueller
	 * DO NOT DE-COMPILE THIS SOFTWARE OR ATTEMPT ANY FORM OF REVERSE ENGINEERING!
	 */
}
