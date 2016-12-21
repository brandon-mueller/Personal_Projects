package me.virusbrandon.userinterfaces;

import org.bukkit.*;
import org.bukkit.inventory.*;

import me.virusbrandon.bottomlesschests.*;

public class ChestView {
	private Chest chest;
	private int[] slots = new int[]{1,2,3,4,5,6,7,10,11,12,13,14,15,16,19,20,21,22,23,24,25,28,29,30,31,32,33,34,37,38,39,40,41,42,43,46,47,48,49,50,51,52};
	private int[][][] buttons = new int[][][]{{{0,9,18,27,36,45,8,17,26,35,44,53},{}},{{0,9,8,17},{36,45,44,53}}};
	private Inventory inv;
	private Main main;	
	private String bl=ChatColor.BLACK+"",bo=ChatColor.BOLD+"",gr=ChatColor.GREEN+"",re=ChatColor.RED+"",wh=ChatColor.WHITE+"",da=ChatColor.DARK_AQUA+"",aq=ChatColor.AQUA+"",bu=ChatColor.BLUE+"";
	String ss;
	
	/**
	 * The ChestView Object Constructor
	 * Initializes All Information Necessary To Create
	 * The Visual Aid That Show A User What They Have Inside
	 * Of Their Bottomless Chest.
	 * 
	 * @author Brandon
	 *
	 *
	 */
	public ChestView(Chest chest,Main main){
		this.chest = chest;	
		this.main = main;
		this.ss = bu+main.getFact().draw("─",20,"");
		inv = Bukkit.createInventory(null, 54,bl+bo +(chest.getName().length()>12?chest.getName().substring(0,12):chest.getName())+main.gL().gT("User_Chest"));
	}
	
	/**
	 * Returns The Inventory View To The User
	 * So They Can Begin Moving Items In And Out
	 * Of Their Bottomless Chest
	 * 
	 * @return
	 * 
	 * 
	 */
	public Inventory showChest(){
		return inv;
	}
	
	/**
	 * Keeps All Items Inside A Bottomless Chest
	 * In Sync With The Back-End So All Information
	 * Needed To Restore A Players Bottomless Chest
	 * Inventory Is Present When Requested
	 * 
	 * 
	 */
	public void recordItems(Chest ch, int amt){
		int maxSlots = slots.length;
		if(chest.getMaxRows() < 6){
			maxSlots = (7*chest.getMaxRows());
		} else {
			maxSlots = 42;
		}
		for(int y = 0;y<maxSlots;y++){
			try{
				if((inv.getItem(slots[y])!=null)){
					ItemStack stack = inv.getItem(slots[y]);
					if(stack.getItemMeta().hasDisplayName()){
						if(stack.getItemMeta().getDisplayName().equalsIgnoreCase(re+main.gL().gT("Locked"))){
							continue;
						}
					}
					chest.setItem(stack,y);
				} else {
					chest.setItem(new ItemStack(Material.AIR,1),y);
				}
			} catch(Exception e1){}
		}
		ch.updwnHelper(amt, this);
	}
	
	/**
	 * Requests The Information Prepared By The RecordItems
	 * Method And Creates The User Interface, But Does Not
	 * Display It.
	 * 
	 * This Method SHOULD NOT Be Combine With The recordItems
	 * Method.
	 * 
	 * @param slot
	 * 
	 * 
	 */
	public void refresh(int slot){
		setButtons(slot);
		if(chest.getMaxRows()<6){
			for(int y = 0;y<(7*chest.getMaxRows());y++){
				refreshHelper(slot, y);
			}
		} else {
			for(int y = 0;y<slots.length;y++){
				refreshHelper(slot, y);
			}	
		}
	}
	
	
	
	/**
	 * Private Refresh Helper Method
	 * 
	 * 
	 */
	@SuppressWarnings("deprecation")
	private void refreshHelper(int slot, int y){
		try{
			ItemStack stack = chest.constructItem(chest.getChest().get(slot+y));
			if(main.getSettings().getPerWorldItems()){
				try{
					String owd = chest.getChest().get(slot+y).getOrgWorld();
					if((owd.length()<1)&(chest.getChest().get(slot+y).getItemId()!=0)){
						chest.getChest().get(slot+y).setOrgWorld(chest.getOwner().getWorld().getName());
					}
					if(!owd.equalsIgnoreCase(chest.getOwner().getWorld().getName())&chest.getChest().get(slot+y).getItemId()!=0){
						if(Bukkit.getWorld(owd)==null){chest.getChest().remove(slot);}
						main.getFact().setUpItem(inv,slots[y],new ItemStack(Material.STAINED_GLASS_PANE,stack.getAmount(),DyeColor.BLACK.getWoolData()),re+main.gL().gT("Locked"),wh+main.gL().gT("ChestView_Item_Available"),wh+main.gL().gT("ChestView_World_Below"),"",wh+bo+owd);
						return;
					}
				}catch(Exception e1){}
				inv.setItem(slots[y],stack);	
			} else {
				inv.setItem(slots[y],stack);
			}
		} catch(Exception e1){}
	}
	
	/**
	 * Returns An Integer Value Of The Total
	 * Number Of Available Item Slots Visible
	 * On The Bottomless Chest User Interface
	 * 
	 * @return
	 * 
	 * 
	 */
	public int getSlotSize(){
		return slots.length;
	}
	
	/**
	 * Adds The Controls To The Bottomless Chest
	 * User Interface
	 * 
	 * @param slot
	 * 
	 * 
	 */
	@SuppressWarnings("deprecation")
	private void setButtons(int slot){
		for(int i:buttons[0][0]){
			boolean t = main.getSettings().getTrashBinEnabled();
			main.getFact().setUpItem(inv,i,new ItemStack(main.getSettings().getTrashButton(),1),re+bo+main.gL().gT("Trash")+(((chest.getOwner()!=null)&&(chest.getOwner().hasPermission("bChest.Trash")|chest.getOwner().hasPermission("bChest.Admin")|chest.getOwner().getName().equalsIgnoreCase(main.name())))?"":re+" ("+main.gL().gT("No_Permission")+")"),"",ss,aq+main.gL().gT("ChestView_Drag_Drop"),aq+main.gL().gT("ChestView_Destroy"),"",aq+main.gL().gT("ChestView_ShiftRight"),aq+main.gL().gT("ChestView_AutoTrash"),t?"":ss,t?re+main.gL().gT("ChestView_Left"):"",t?re+main.gL().gT("ChestView_ToView"):"",t?re+main.gL().gT("ChestView_Retrieve"):"",t?re+main.gL().gT("ChestView_NotDiscard"):"",t?ss:"",bl+"#Chest");
		}
		ItemStack s;
		String message;
		if(chest.canScrollUp()){
			s = new ItemStack(main.getSettings().getOkButton(),1,DyeColor.LIME.getWoolData());
			message = gr+bo+main.gL().gT("Scroll_Up");
		} else {
			s = new ItemStack(main.getSettings().getNoButton(),1,DyeColor.RED.getWoolData());
			message = re+bo+main.gL().gT("Cannot_SU");
		}
		for(int i:buttons[1][0]){
			main.getFact().setUpItem(inv,i,s,message,gr+main.gL().gT("ChestView_Showing")+": "+re+"("+gr+((slot/7)+1)+re+" "+main.gL().gT("ChestView_Thru")+" "+gr+((slot+getSlotSize())/7)+re+")"+wh+"/"+gr+chest.getMaxRows(),"",ss,gr+"✔  "+da+main.gL().gT("ChestView_SLeft")+main.getSettings().getScrollbarSettings()[0]+" "+main.gL().gT("ChestView_Rows"),gr+"✔  "+da+main.gL().gT("ChestView_SRight")+main.getSettings().getScrollbarSettings()[1]+" "+main.gL().gT("ChestView_Rows"),gr+"✔  "+da+main.gL().gT("ChestView_SHClick")+main.getSettings().getScrollbarSettings()[2]+" "+main.gL().gT("ChestView_Rows"),ss,bl+"#Chest");
		}
		if(chest.canScrollDown()){
			s = new ItemStack(main.getSettings().getOkButton(),1,DyeColor.LIME.getWoolData());
			message = gr+bo+main.gL().gT("Scroll_Down");
		} else {
			s = new ItemStack(main.getSettings().getNoButton(),1,DyeColor.RED.getWoolData());
			message = re+bo+main.gL().gT("Cannot_SD");
		}
		for(int i:buttons[1][1]){
			main.getFact().setUpItem(inv,i,s,message,gr+main.gL().gT("ChestView_Showing")+": "+re+"("+gr+((slot/7)+1)+re+" "+main.gL().gT("ChestView_Thru")+" "+gr+((slot+getSlotSize())/7)+re+")"+wh+"/"+gr+chest.getMaxRows(),"",ss,gr+"✔  "+da+main.gL().gT("ChestView_SLeft")+main.getSettings().getScrollbarSettings()[0]+" "+main.gL().gT("ChestView_Rows"),gr+"✔  "+da+main.gL().gT("ChestView_SRight")+main.getSettings().getScrollbarSettings()[1]+" "+main.gL().gT("ChestView_Rows"),gr+"✔  "+da+main.gL().gT("ChestView_SHClick")+main.getSettings().getScrollbarSettings()[2]+" "+main.gL().gT("ChestView_Rows"),ss,bl+"#Chest");
		}
		fillUp();
	}
	
	/**
	 * Blocks Off Inventory Spaces
	 * When A Player Does Not Have
	 * Permission To Store Their Items
	 * There.
	 * 
	 * 
	 */
	@SuppressWarnings("deprecation")
	private void fillUp(){
		if(chest.getMaxRows()<6){
			for(int x=(chest.getMaxRows()*7);x<slots.length;x++){
				main.getFact().setUpItem(inv,slots[x],new ItemStack(Material.STAINED_GLASS_PANE,1,DyeColor.BLACK.getWoolData()),re+bo+main.gL().gT("ChestView_SpaceLocked"),re+main.gL().gT("ChestView_Purchase"),re+main.gL().gT("ChestView_Store"),bl+"#Chest");
			}
		}
	}
	
	/*
	 * © 2016 Brandon Mueller
	 * DO NOT DE-COMPILE THIS SOFTWARE OR ATTEMPT ANY FORM OF REVERSE ENGINEERING!
	 */
}