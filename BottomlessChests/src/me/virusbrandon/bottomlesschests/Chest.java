package me.virusbrandon.bottomlesschests;

import java.text.DecimalFormat;
import java.util.*;

import me.virusbrandon.bc_utils.*;
import me.virusbrandon.fileio.Reader;
import me.virusbrandon.userinterfaces.*;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;

@SuppressWarnings("deprecation")
public class Chest{
	private HashMap<Integer,ChestItem> ITEMS_IN_CHEST = new HashMap<>();
	private ArrayList<ItemStack> ITEMS_IN_TRASH = new ArrayList<>();
	private ArrayList<Friend> FRIEND_LIST = new ArrayList<>();
	private boolean isLoading = true;
	private Chest invSeeChest;
	private ChestView chest;
	private DecimalFormat df = new DecimalFormat("###.##");
	private int MAX_CHEST_ROWS,SCREEN_SIZE = 42,slot = 0;
	private double BAR_TICKS = 60; /* WARNING - Divide By ZERO Access (DO NOT SET TO 0!) */
	private Main main;
	private Player owner;
	private String CHEST_OWNER_NAME,CHEST_OWNER_UUID;
	private String dr=ChatColor.DARK_RED+"",re=ChatColor.RED+"",go=ChatColor.GOLD+"",ye=ChatColor.YELLOW+"",gr=ChatColor.GREEN+"",bo=ChatColor.BOLD+"",bl=ChatColor.BLACK+"",ul=ChatColor.UNDERLINE+"",wh=ChatColor.WHITE+"";
	private String[]ss = new String[2];
	private Timer init,autosave;
	private TrashBin bin;
	private Object[][] text = new Object[][]{{.9,.8,.65,.5},{dr,re,go,ye,gr}};
	private Sorter sorter; /* We're Going To Use An Independent Instance For Each Chest */
	private long wait;
	
	/**
	 * Bottomless Chest Constructor
	 * 
	 * 
	 */
	public Chest(Player p, int maxRows,Main main){
		this.CHEST_OWNER_NAME = p.getName();
		this.CHEST_OWNER_UUID = p.getUniqueId().toString();
		this.MAX_CHEST_ROWS = maxRows;
		this.main = main;
		this.owner = p;
		hlper();
	}
	
	/**
	 * Bottomless Chests Constructor (Without Player)
	 * 
	 */
	public Chest(String uuid, String name, int maxRows, Main main){
		this.CHEST_OWNER_NAME = name;
		this.CHEST_OWNER_UUID = uuid;
		this.MAX_CHEST_ROWS = maxRows;
		this.main = main;
		hlper();
	}
	
	private void hlper(){
		this.bin = new TrashBin(this,main);
		clearTrash();
		this.ss[0] = main.getFact().draw(" ", 17, "");
		this.ss[1] = main.getFact().draw(" ", 5, "");	
		if(owner != null){
			owner.sendMessage(gr+">> "+main.gL().gT("Loading"));
		}
		init = new Timer(this,3);
		init.start(1);
		wait = System.currentTimeMillis()+2000;
		autosave = new Timer(this,6);
		autosave.start(6000);
	}
	
	/**
	 * Called When The Scroll-Down Button
	 * Is Pressed On The User Interface
	 * 
	 * @param p
	 * 
	 * 
	 */
	public Chest dwnBtn(int rows){
		if(canScrollDown()){
			chest.recordItems(this,(7*rows));
		} else {
			chest.recordItems(this,0);
		}
		return this;
	}
	
	/**
	 * Called When The Scroll-Up Button
	 * Is Pressed On The User Interface
	 * 
	 * @param p
	 * 
	 * 
	 */
	public Chest upBtn(int rows){
		if(canScrollUp()){
			chest.recordItems(this,(-7*rows));
		} else {
			chest.recordItems(this,0);
		}
		return this;
	}
	
	/**
	 * The updwnBtn Helper Function:
	 * 
	 * Fixes a timing issue that potentially
	 * creates duplicate items.
	 * 
	 * 
	 */
	public void updwnHelper(int amt, ChestView view){
		amt=(amt>0)?(((slot)+amt)>((MAX_CHEST_ROWS*7)-SCREEN_SIZE)?(((MAX_CHEST_ROWS*7)-SCREEN_SIZE)-slot):amt):(amt<0)?((slot+amt)<=0)?amt-(slot+amt):amt:amt;	
		this.slot+=amt;
		view.refresh(slot);
		if(main.getSettings().getActionBar()==2){
			sendBar();
		}
	}
	
	/**
	 * Returns Information Needed To Construct
	 * And Update The User Interface
	 * 
	 * @return
	 * 
	 * 
	 */
	public ChestView getView(){
		return chest;
	}
	
	/**
	 * The Reload View Function:
	 * 
	 */
	public void reloadView(){
		chest = new ChestView(this,main);
		chest.refresh(slot);
		this.bin = new TrashBin(this,main);
		main.getBar().sendActionBar(owner, getSpaceUsed());	
	}
	
	/**
	 * Returns Reference To Main
	 * 
	 * 
	 */
	public Main getMain(){
		return main;
	}
	
	/**
	 * Returns The Current First Slot Of The
	 * Bottomless Chest GUI
	 * 
	 * Used To Determine Where The End Of The
	 * Visible Portion Of The Chest Is
	 * 
	 * @return
	 * 
	 * 
	 */
	public int getSlot(){
		return slot;
	}
	
	/**
	 * Determines If The User May Scroll Further Down
	 * In Their Bottomless Chest
	 * 
	 * Scroll-Down Botton Become Inactive If The Below
	 * Conditions Cannot be Satisfied
	 * 
	 * @return
	 * 
	 * 
	 */
	public boolean canScrollDown(){
		return ((((slot+chest.getSlotSize())/7)+1)<=(MAX_CHEST_ROWS));
	}
	
	/**
	 * Determines If The User May Scroll Further Up
	 * In Their Bottomless Chest
	 * 
	 * Scroll-Up Botton Become Inactive If The Below
	 * Conditions Cannot be Satisfied
	 * 
	 * @return
	 * 
	 * 
	 */
	public boolean canScrollUp(){
		return ((slot-7)>=0);
	}
	
	/**
	 * After The Player Bottomless Chest Inventory
	 * Can Be Constructed Using Available Data, The
	 * User is Presented Will Their Chest With All
	 * Contents, Just As They Left It.
	 * 
	 * @return
	 * 
	 * 
	 */
	public Inventory openChest(){
		return chest.showChest();
	}
	
	/**
	 * When Something Changes Inside Of A Player's
	 * Bottomless Chest, The Information That The User
	 * Sees Is Kept In Sync With The Back-End Information
	 * That Allows For The Bottomless Chest To Be Constructed
	 * 
	 * @param stack
	 * @param slots
	 * 
	 * 
	 */
	public void setItem(ItemStack stack, int slots){
		try{
			if(ITEMS_IN_CHEST.containsValue(new ChestItem(stack,main,this))){
				addItem(stack);
			} else {
				ITEMS_IN_CHEST.put(slot+slots,new ChestItem(stack,main,this));
			}
		} catch(Exception e1){}
	}
	
	/**
	 * Adds An Item To The Chest. Relies On One Of
	 * The firstOpenSlot Methods Directly Below This One
	 * 
	 * 
	 */
	public boolean addItem(ItemStack stack){
		try{
			if(stack.getAmount()<1){return false;}
			int x = firstOpenSlot(stack);
			if(ITEMS_IN_CHEST.get(x).getItemId()==0){
				ITEMS_IN_CHEST.put(x, new ChestItem(stack,main,this));
			} else {
				int amount = ITEMS_IN_CHEST.get(x).getAmount();
				int max = constructItem(ITEMS_IN_CHEST.get(x)).getMaxStackSize();
				if((stack.getAmount()+amount)>=max){
					ITEMS_IN_CHEST.get(x).setAmount(ITEMS_IN_CHEST.get(x).getAmount()+(max-amount));
					stack.setAmount(stack.getAmount()-(max-amount));
					addItem(stack);
				} else {
					ITEMS_IN_CHEST.get(x).setAmount(ITEMS_IN_CHEST.get(x).getAmount()+stack.getAmount());
					stack.setAmount(0);
				}
			}
			chest.refresh(slot);
			sendBar();
		} catch(Exception e1){
			return false;
		}
		return true;
	}
	
	/**
	 * Adds An Item To The Chest. Relies On One Of
	 * The firstOpenSlot Methods Directly Below This One
	 * 
	 * 
	 */
	private boolean addTrashItem(ItemStack stack){
		try{
			if(stack.getAmount()<1){return true;}
			if(!main.getSettings().getTrashBinEnabled()){
				return true;
			} else {
				int x = firstOpenSlot(stack,ITEMS_IN_TRASH);
				if(x==-1){return false;}
				if(ITEMS_IN_TRASH.get(x).getTypeId()==0){
					ITEMS_IN_TRASH.set(x, stack);
				} else {
					int amount = ITEMS_IN_TRASH.get(x).getAmount();
					int max = stack.getMaxStackSize();
					if((stack.getAmount()+amount)>=max){
						ITEMS_IN_TRASH.get(x).setAmount(ITEMS_IN_TRASH.get(x).getAmount()+(max-amount));
						stack.setAmount(stack.getAmount()-(max-amount));
						addTrashItem(stack);
					} else {
						ITEMS_IN_TRASH.get(x).setAmount(ITEMS_IN_TRASH.get(x).getAmount()+stack.getAmount());
						stack.setAmount(0);
					}
				}
			}
		} catch(Exception e1){
			return false;
		}
		return true;
	}
	
	/**
	 * Returns The First Available Slot In Your Chest
	 * 
	 */ 
	public int firstOpenSlot(ItemStack stack){
		for(int x = 0;x<ITEMS_IN_CHEST.size();x++){
			try{
				if((ITEMS_IN_CHEST.get(x).getItemId() == 0)||(ITEMS_IN_CHEST.get(x).equals(new ChestItem(stack,main,this))&&(ITEMS_IN_CHEST.get(x).getAmount()<constructItem(ITEMS_IN_CHEST.get(x)).getMaxStackSize()))){
					return x;
				}
			}catch(Exception e1){return x;}
		}
		return -1;
	}
	
	/**
	 * Returns The First Available Slot
	 * In This Chest's Trash Bin
	 * 
	 * @param stack
	 * @param trash
	 * @return
	 * 
	 * 
	 */
	private int firstOpenSlot(ItemStack stack, ArrayList<ItemStack> trash){
		for(int x = 0;x<trash.size();x++){
			ChestItem item = new ChestItem(trash.get(x),main,this);
			if((item.getItemId() == 0)|(item.equals(new ChestItem(stack,main,this))&(item.getAmount()<constructItem(item).getMaxStackSize()))){
				return x;
			}
		}
		return -1;
	}
	
	/**
	 * Places An ItemStack Into The Trash Bin.
	 * WARNING - This Trash Bin Is Empties When You Leave
	 * The Server, So Be Careful Not To Place Anything Into
	 * The Trash That You Do Not Want To Lose!
	 * @throws Exception 
	 * 
	 * 
	 */
	public boolean trashItem(ItemStack stack) throws Exception{
		return addTrashItem(stack);
	}
	
	/**
	 * Returns The List Of Items In The Trash.
	 * 
	 * 
	 */
	public ArrayList<ItemStack> getTrash(){
		return ITEMS_IN_TRASH;
	}
	
	/**
	 * Empties This Chest's Trash Bin
	 * 
	 * 
	 */
	public void clearTrash(){
		ITEMS_IN_TRASH.clear();
		for(int x=0;x<36;x++){
			ITEMS_IN_TRASH.add(new ItemStack(Material.AIR));
		}
	}
	
	/**
	 * Returns The Inventory View Of This Chest's
	 * Trash Bin For Viewing
	 * 
	 * 
	 */
	public TrashBin trashView(){
		bin.update();
		return bin;
	}
	
	/**
	 * This Method Is Called By A Runnable Only When
	 * A Exception Is Thrown Because Of An Issue With The
	 * Size Of The Chest
	 * 
	 * This Error Has Been Observed To Only Happen When
	 * A Player's Permissions Have Changed, Such That, They
	 * Have Access To More Space Inside Their Bottomless Chest.
	 * 
	 * This Method Call Due To An Exception Allows For The
	 * Chest To Auto Correct Itself Of Sizing Errors.
	 * 
	 * The Only Loss Of Functionality Is A Temportary Loss Of
	 * The Actionbar Which Displays The Status Of A User's Chest
	 * 
	 * 
	 */
	public void adjustSize(){
		for(int x=0;x<7;x++){
			ITEMS_IN_CHEST.put(ITEMS_IN_CHEST.size()+x,new ChestItem(new ItemStack(Material.AIR,1),main,this));
		}
	}
	
	/**
	 * Returns The List Of All Items Contained Within
	 * A User's Bottomless Chest and all details about every
	 * item.
	 * 
	 * @return
	 * 
	 * 
	 */
	public HashMap<Integer,ChestItem> getChest(){
		return ITEMS_IN_CHEST;
	}
	
	/**
	 * Sets The List Of ChestItems That Are Inside
	 * This Bottomless Chest
	 * 
	 * 
	 */
	public Chest setChest(HashMap<Integer,ChestItem> ITEMS_IN_CHEST){
		this.ITEMS_IN_CHEST = ITEMS_IN_CHEST;
		chest.refresh(slot);
		return this;
	}
	
	/**
	 * Returns The Name Of The Owner Of This
	 * Bottomless Chest
	 * 
	 * @return
	 * 
	 * 
	 */
	public String getName(){
		return CHEST_OWNER_NAME;
	}
	
	/**
	 * The Set Owner Function:
	 * 
	 */
	public Chest setOwner(Player owner){
		this.owner = owner;
		return this;
	}
	
	/**
	 * Returns The Owner Of This Chest
	 * 
	 * 
	 */
	public Player getOwner(){
		return owner; 
	}
	
	/**
	 * Returns The UUID Of The Owner Of This
	 * Bottomless Chest.
	 * 
	 * @return
	 * 
	 * 
	 */
	public String UUID(){
		return CHEST_OWNER_UUID;
	}
	
	/**
	 * Returns The Maximum Size Of This Bottomless Chest
	 * By The Number Of Rows
	 * 
	 * @return
	 * 
	 * 
	 */
	public int getMaxRows(){
		return MAX_CHEST_ROWS;
	}
	
	/**
	 * Sets The Chest That We Will Be Looking At
	 * When You Run The InvSee Command
	 * 
	 * 
	 */
	public Chest setInvSeeChest(Chest chest){
		this.invSeeChest = chest;
		return this;
	}
	
	/**
	 * Returns The Chest That You Currently
	 * Invsee-ing
	 * 
	 * 
	 */
	public Chest getInvSeeChest(){
		return invSeeChest;
	}
	
	/**
	 * This Method Deletes All Items From A Given Chest
	 * And Can Only Be Called By A Player Or Staff Member
	 * With Elevated Permissions
	 * 
	 * 
	 */
	public void clearChest(int slot){
		for(int x=0;x<ITEMS_IN_CHEST.size();x++){
			ITEMS_IN_CHEST.put(x, new ChestItem(new ItemStack(Material.AIR),main,this));
		}
		for(int x=0;x<ITEMS_IN_TRASH.size();x++){
			ITEMS_IN_TRASH.set(x, new ItemStack(Material.AIR));
		}
		chest = new ChestView(this,main);
		chest.refresh(slot);
		main.getBar().sendActionBar(owner, getSpaceUsed());	
	}
	
	/**
	 * This Method Re-Constructs An Item Inside A Player's
	 * Bottomless Chest Back Into It's Usable In-Game Form
	 * 
	 * @param item
	 * @return
	 * 
	 * 
	 */
	public ItemStack constructItem(ChestItem item){
		return item.getItemStack();
	}
	
	/**
	 * Returns The Sorter Instance
	 * For This Chest.
	 * 
	 */
	public Sorter getSorter(){
		return sorter;
	}
	
	/**
	 * Returns A Bar Representation Of How Much Space
	 * A Player Has Occupied Inside Their Bottomless Chest.
	 * 
	 * @return
	 * 
	 * 
	 * 
	 */
	protected String getSpaceUsed(){
		int used = 0;
		for(int x = 0;x<(MAX_CHEST_ROWS*7);x++){
			if(ITEMS_IN_CHEST.get(x).getItemId()!=0){
				used++;
			}
		}
		return ss[0]+wh+"« "+gr+bo+ul+main.gL().gT("Chest_CMD")+wh+" »"+ss[1]+getBar((float)used/(float)(MAX_CHEST_ROWS*7));
	}
	
	/**
	 * Returns A Progress Bar Representation Of
	 * The Amount Of Space Used Out Of Total
	 * Available Space In A User's Bottomless
	 * Chest
	 * 
	 * @param usedRatio
	 * @return
	 * 
	 * 
	 */
	private String getBar(float usedRatio){
		String bar = "";
		bar+=main.getFact().draw(getColor(usedRatio)+"▍", (int)Math.floor(usedRatio/((float)(1.00/BAR_TICKS))),"");
		bar+=main.getFact().draw(bl+"▍",(int)(BAR_TICKS-Math.floor(usedRatio/((float)(1.00/BAR_TICKS))))-1, "");
		bar+=ss[1]+wh+"« "+getColor(usedRatio)+ul+bo+df.format(usedRatio*100)+main.gL().gT("Chest_PU")+wh+" »";
		return getColor(usedRatio)+bar;
	}
	
	/**
	 * The Send Bar Function:
	 * 
	 * Specifically For Sending A ActionBar
	 * To Players When The Temporary Display Option
	 * Is Enable In The Admin Menu.
	 */
	private void sendBar(){
		try{
			main.getBar().sendActionBar(Bukkit.getPlayer(owner.getName()),getSpaceUsed());
		}catch(Exception e1){}
	}
	
	/**
	 * Returns A Color That The Bar Will Be
	 * In Coorespondance With The Amount Of
	 * Space Used In The Chest, Green Being
	 * Less Than Half Full And Closer To Red
	 * Meaning Getting Full
	 * 
	 * @param value
	 * @return
	 * 
	 * 
	 */
	private String getColor(float value){
		return(value>((double)text[0][0]))?(String)text[1][0]:(value>(double)text[0][1])?(String)text[1][1]:(value>(double)text[0][2])?(String)text[1][2]:(value>(double)text[0][3])?(String)text[1][3]:(String)text[1][4];
	}
	
	/**
	 * Adds A Friend To This Players Chest So
	 * That Player Has Access To Open This Chest.
	 * 
	 * Recommend That This Is Used With Caution, As Friends
	 * Steal Too ;)
	 * 
	 * 
	 */
	protected void addFriend(Player friend){
		FRIEND_LIST.add(new Friend(friend));
	}
	
	/**
	 * Has Friend Method Determines If A Player Who
	 * Has Requested Access To Your Chest Actually
	 * Has Permission To Proceed.
	 * 
	 * 
	 */
	protected boolean hasFriend(String requesterUUID){
		return(FRIEND_LIST.contains(requesterUUID));
	}
	
	/**
	 * Returns This Chest's List Of Friends
	 * 
	 * 
	 */
	public ArrayList<Friend> getFriends(){
		return FRIEND_LIST;
	}

	/**
	 * Necessary Delay When A User's Chest
	 * Is Preparing For Startup.
	 * 
	 */
	protected void initTick(){
		if(System.currentTimeMillis() >= wait){
			init.stop();
			init = null;
			if(owner != null){
				initChest(owner);
			} else {
				initChest(null);
			}	
		}
	}
	
	/**
	 * Initializes A User's Bottomless Chest. If The User
	 * Has Already Stored items Before, They Will be Restored
	 * 
	 * @param p
	 * 
	 * 
	 */
	public void initChest(Player p){
		this.chest = new ChestView(this,main);
		this.sorter = new Sorter(main,this);
		Reader read;
		if(p == null){
			read = new Reader(this,CHEST_OWNER_NAME,CHEST_OWNER_UUID,main.getSettings().getFileReadMethod());
		} else {
			read = new Reader(this, p,main.getSettings().getFileReadMethod());
		}
		read.read();
	}
	
	/**
	 * Returns Whether This Chest Is In The Process
	 * Of Loading Up.
	 * 
	 * This Chest's Information Cannot Be Written Back
	 * Out To The Hard Disk If It's In The Process Of
	 * Loading.
	 * 
	 * 
	 */
	protected boolean isLoading(){
		return isLoading;
	}
	
	/**
	 * Sets The isLoading Field
	 * Grants Or Blocks Access To This
	 * Chest While Loading Is In Progress
	 * 
	 * 
	 */
	public void setLoading(boolean loading){
		this.isLoading = loading;		
	}
	
	/**
	 * Creates A New Empty Chest
	 * For A Player
	 * 
	 * 
	 */
	 public void createChest(){
		for(int x=0;x<MAX_CHEST_ROWS;x++){
			for(int y=0;y<7;y++){
				this.ITEMS_IN_CHEST.put(ITEMS_IN_CHEST.size()+y,new ChestItem(new ItemStack(Material.AIR,1),main,this));
			}
		}
	 }
	 
	 /**
	  * Chest AutoSave Method
	  * 
	  * 
	  */
	 protected void autoSave(){
		 main.write(this,true);
	 }
	 
	 /**
	  * Chest Shutdown Method
	  * 
	  * 
	  */
	 public Chest shutdown(){
		 for(UUID u:main.getChestKeys()){
			 Chest chest = main.getChests().get(u);
			 try{
				if(chest.getInvSeeChest().equals(main.findChest(CHEST_OWNER_NAME))){
					Player pp = Bukkit.getPlayer(chest.getName());
					pp.closeInventory();
				}	
			 }catch(Exception e1){} 
		 }
		 try{
			 init.stop();
			 init = null;
		 } catch(Exception e1){}
		 try{
			 autosave.stop();
			 autosave = null;
		 }catch(Exception e1){}
		 return this;
	 }
	 
	 /**
	  * Chest Object Equals Method
	  * 
	  * 
	  */
	 private boolean equals(Chest other){
		 return getOwner().equals(other.getOwner()); 
	 }
	
	 /*
	  * © 2016 Brandon Mueller
	  * DO NOT DE-COMPILE THIS SOFTWARE OR ATTEMPT ANY FORM OF REVERSE ENGINEERING!
	  */
}