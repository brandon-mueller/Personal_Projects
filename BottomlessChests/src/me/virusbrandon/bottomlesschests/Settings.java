package me.virusbrandon.bottomlesschests;

import org.bukkit.*;

public class Settings implements java.io.Serializable{
	private static final long serialVersionUID = 1589399L;
	private boolean FILE_READ_METHOD = true; 					/* false = byName -- true = byUUID */
	private boolean FILE_WRITE_METHOD = true; 					/* false = byName -- true = byUUID */
	private boolean PER_WORLD_ITEMS = false; 					/* false = No -- true = Yes */
	private boolean TRASHBIN_ENABLED = true; 					/* false = No -- true = Yes */
	private boolean MAINTENANCE_ENABLED = false;				/* false = No -- true = Yes*/
	private int AUTO_BLOCK_PICKUP = 0; 							/* 0 = No -- 1 = Enabled */
	private int LANGUAGE_SETTING = 0; 							/* Depends On How Many Language Files Are Provided */
	private int INFO_ENABLED = 1; 								/* 0 = Disabled -- 1 = Temporary Display -- 2 = Solid Enabled */
	private int RATIO_ITEMDROPS_ONDEATH = 0;					/* Percentage Of A User's Chest Items That Will be Dropped On Death */
	private int ONDEATH_DELETE_ITEMS = 0;						/* Whether The Above Ratio Of Items Are Dropped Or Just Disappear */
	private int ITEM_SORT_OPTION = 0;							/* 0 = Sort By ItemID -- 1 = Sort By Item Material Name */
	private int[] currents = new int[]{160,160,166,12,134,158}; 
	private int[] scrolls = new int[]{1,6,100};
	private String OK_BUTTON = "STAINED_GLASS_PANE";
	private String NO_BUTTON = "STAINED_GLASS_PANE";
	private String TRASH_BUTTON = "BARRIER";
	private String CHEST_OPEN = "BLOCK_CHEST_OPEN";
	private String SCROLL = "ENTITY_BAT_TAKEOFF";
	private String DESTROY = "ENTITY_CREEPER_DEATH";
	private String[] scrollsW = new String[]{"Left Click","Right Click","Shift Click"};
	private int scrollFocus = 0;
	
	/**
	 * Settings Class Constructor
	 * 
	 * 
	 */
	public Settings(Main main){
		this.CHEST_OPEN = "BLOCK_CHEST_OPEN";
		this.SCROLL = "ENTITY_BAT_TAKEOFF";
		this.DESTROY = "ENTITY_CREEPER_DEATH";
	}
	
	/**
	 * Returns The Item To Be Used For
	 * The Chest Menu OK Button
	 * 
	 * @return
	 * 
	 * 
	 */
	public Material getOkButton(){
		return Material.getMaterial(OK_BUTTON);
	}
	
	/**
	 * Sets The OK Button Item
	 * 
	 * @param name
	 * 
	 * 
	 */
	public Sound setOkButton(String name, int pos){
		this.OK_BUTTON = name;
		okBtnPos(pos);
		return Sound.valueOf(SCROLL);
	}
	
	/**
	 * Returns The OK Button Menu
	 * Position
	 * 
	 * 
	 */
	public int okBtnPos(){
		return currents[0];
	}
	
	/**
	 * Sets The OK Button Menu
	 * Position
	 * 
	 * 
	 */
	private void okBtnPos(int pos){
		currents[0] = pos;
	}
	
	/**
	 * Returns The Item To Be Used For
	 * The Chest Menu NO Button
	 * 
	 * @return
	 * 
	 * 
	 */
	public Material getNoButton(){
		return Material.getMaterial(NO_BUTTON);
	}
	
	/**
	 * Sets The No Button Item
	 * 
	 * @param name
	 * 
	 * 
	 */
	public Sound setNoButton(String name, int pos){
		this.NO_BUTTON = name;
		noBtnPos(pos);
		return Sound.valueOf(SCROLL);
	}
	
	/**
	 * Returns The NO Button Menu
	 * Position
	 * 
	 * 
	 */
	public int noBtnPos(){
		return currents[1];
	}
	
	/**
	 * Sets The NO Button Menu
	 * Position
	 * 
	 * 
	 */
	private void noBtnPos(int pos){
		currents[1]=pos;
	}
	
	/**
	 * Returns The Item To Be Used For
	 * The Chest Menu TRASH Button
	 * 
	 * @return
	 * 
	 * 
	 */
	public Material getTrashButton(){
		return Material.getMaterial(TRASH_BUTTON);
	}
	
	/**
	 * Sets The Trash Button Item
	 * 
	 * @param name
	 * 
	 * 
	 */
	public Sound setTrashButton(String name, int pos){
		this.TRASH_BUTTON = name;
		trashBtnPos(pos);
		return Sound.valueOf(SCROLL);
	}
	
	/**
	 * Returns The TRASH Button Menu
	 * Position
	 * 
	 * 
	 */
	public int trashBtnPos(){
		return currents[2];
	}
	
	/*
	 * Sets The TRASH Button Menu
	 * Position
	 * 
	 * 
	 */
	private void trashBtnPos(int pos){
		currents[2]=pos;
	}
	
	/**
	 * Returns The Sound To Be Played When
	 * The Chest Menu Is Opened
	 * 
	 * @return
	 * 
	 * 
	 */
	public Sound getChestOpenSound(){
		return Sound.valueOf(CHEST_OPEN);
	}
	
	/**
	 * Sets The Chest Open Sound
	 * 
	 * @param name
	 * 
	 * 
	 */
	public Sound setChestOpenSound(String name, int pos){
		this.CHEST_OPEN = name;
		openSoundPos(pos);
		return Sound.valueOf(SCROLL);
	}	
	
	/**
	 * Returns The OK Button Menu
	 * Position
	 * 
	 * 
	 */
	public int openSoundPos(){
		return currents[3];
	}
	
	/**
	 * Sets The Chest Open Sound Menu
	 * Position
	 * 
	 * 
	 */
	private void openSoundPos(int pos){
		currents[3]=pos;
	}
	
	/**
	 * Returns The Sound To Be Played When
	 * The Chest Menu Scroll Bar Is Clicked
	 * 
	 * @return
	 * 
	 * 
	 */
	public Sound getScrollSound(){
		return Sound.valueOf(SCROLL);
	}
	
	/**
	 * Sets The Scroll Sound
	 * 
	 * @param name
	 * 
	 * 
	 */
	public Sound setScrollSound(String name, int pos){
		this.SCROLL = name;
		scrollSoundPos(pos);
		return Sound.valueOf(SCROLL);
	}
	
	/**
	 * Returns The SCROLL Sound
	 * Position
	 * 
	 * 
	 */
	public int scrollSoundPos(){
		return currents[4];
	}
	
	/**
	 * Sets The Scroll Menu Sound
	 * Position
	 * 
	 * 
	 */
	private void scrollSoundPos(int pos){
		currents[4]=pos;
	}
	
	/**
	 * Returns The Sound To Be Played When
	 * An Item Is Discarded
	 * 
	 * @return
	 * 
	 * 
	 */
	public Sound getTrashSound(){
		return Sound.valueOf(DESTROY);
	}
	
	/**
	 * Sets The Trash Sound
	 * 
	 * @param name
	 * 
	 * 
	 */
	public Sound setTrashSound(String name, int pos){
		this.DESTROY = name;
		discardSoundPos(pos);
		return Sound.valueOf(SCROLL);
	}
	
	/**
	 * Returns The DISCARD Sound
	 * Position
	 * 
	 * 
	 */
	public int discardSoundPos(){
		return currents[5];
	}
	
	/**
	 * Sets The DISCARD Sound Menu
	 * Position
	 * 
	 * 
	 */
	private void discardSoundPos(int pos){
		currents[5]=pos;
	}
	
	/**
	 * Returns The Setting Menu
	 * Positions
	 * 
	 * 
	 */
	public int[] getSettingPositions(){
		return currents;
	}
	
	/**
	 * Returns The File Read Method
	 * 
	 * 
	 */
	public boolean getFileReadMethod(){
		return FILE_READ_METHOD;
	}
	
	/**
	 * Toggles The File Read Method
	 * 
	 * 
	 */
	public Sound toggleFileReadMethod(){
		this.FILE_READ_METHOD=(FILE_READ_METHOD==false)?true:(FILE_READ_METHOD==true)?false:true;
		this.FILE_WRITE_METHOD = FILE_READ_METHOD;
		return Sound.valueOf(SCROLL);
	}
	
	/**
	 * Returns The File Write Method
	 * 
	 * 
	 */
	public boolean getFileWriteMethod(){
		return FILE_WRITE_METHOD;
	}
	
	/**
	 * Toggles The File Write Method
	 * 
	 * 
	 */
	public Sound toggleFileWriteMethod(){
		this.FILE_WRITE_METHOD=(FILE_WRITE_METHOD==false)?true:(FILE_WRITE_METHOD==true)?false:true;
		this.FILE_READ_METHOD = FILE_WRITE_METHOD;
		return Sound.valueOf(SCROLL);
	}
	
	/**
	 * Returns A Worded Representation Of The
	 * Read Or Write Method
	 * 
	 * 
	 */
	public String getMethodString(boolean method){
		if(!method){
			return "byName";
		} else {
			return "byUUID";
		}
	}
	
	/**
	 * Returns Whether The ActionBar Is Enabled
	 * Or Not.
	 * 
	 * 
	 */
	public int getActionBar(){
		return INFO_ENABLED;
	}
	
	/**
	 * Toggles The State Of The ActionBar.
	 * 
	 * 
	 */
	public Sound toggleActionBar(){
		int ie = INFO_ENABLED;
		ie++;
		ie=ie%3;
		this.INFO_ENABLED = ie;
		return Sound.valueOf(SCROLL);
	}
	
	/**
	 * Returns A Worded Representation Of The
	 * ActionBar Settings
	 * 
	 * 
	 */
	public String getActionBarString(int method){
		return (method==0)?"Disabled":(method==1)?"Enabled - Solid Display":(method==2)?"Enabled - Temporary Display":"";
	}
	
	/**
	 * Returns Whether Mined Blocks Should
	 * Be Auto-Collected Into The Player's
	 * Bottomless Chest
	 * 
	 * 
	 */
	public int getAutoPickup(){
		return AUTO_BLOCK_PICKUP;
	}
	
	/**
	 * Sets The State Of Auto Block Pickup
	 * 
	 *  
	 */
	public Sound changeAutoBlockPickup(){
		int abp = AUTO_BLOCK_PICKUP;
		abp++;
		abp=(abp%2);
		this.AUTO_BLOCK_PICKUP=abp;
		return Sound.valueOf(SCROLL);
	}
	
	/**
	 * Returns A Worded Representation Of The
	 * Auto Block Pickup Setting
	 * 
	 * 
	 */
	public String getBlockPickupString(int method){
		return(method==0)?"Disabled":(method==1)?"Enabled":"Meh";
	}
	
	/**
	 * Returns Whether Items Should Be
	 * Separated By World Or Not
	 * 
	 * 
	 */
	public boolean getPerWorldItems(){
		return PER_WORLD_ITEMS;
	}
	
	/**
	 * Sets The State Of Per World Items
	 * 
	 * 
	 */
	public Sound togglePerWorldItems(){
		this.PER_WORLD_ITEMS=(PER_WORLD_ITEMS==false)?true:(PER_WORLD_ITEMS==true)?false:true;
		return Sound.valueOf(SCROLL);
	}
	
	/**
	 * Returns Whether The TrashBin
	 * Is Enabled Or Not
	 * 
	 */
	public boolean getTrashBinEnabled(){
		return TRASHBIN_ENABLED;
	}
	
	/**
	 * Sets The State Of The TrashBin
	 * 
	 * 
	 */
	public Sound toggleTrashBinEnabled(){
		this.TRASHBIN_ENABLED=(TRASHBIN_ENABLED==false)?true:(TRASHBIN_ENABLED==true)?false:true;
		return Sound.valueOf(SCROLL);
	}
	
	/**
	 * Returns A Worded Representation Of The
	 * TrashBin Enabled Setting
	 * 
	 * 
	 */
	public String getTrashBinEnabledString(boolean method){
		return(method)?"Enabled":"Disabled";
	}
	
	/**
	 * Returns A Worded Representation Of The
	 * Per World Items Setting
	 * 
	 * 
	 */
	public String getPerWorldItemString(boolean method){
		if(!method){
			return "Universal Items";
		} else {
			return "Per World Items";
		}
	}
	
	/**
	 * Changes The Items Dropped
	 * On Death Ratio.
	 * 
	 * Acceptable Values:
	 * 0 Thru 1.0 Where .5 Represents 50%
	 *
	 */
	public Sound advanceOnDeathPercent(int i,boolean toggle){
		if(!toggle){
			RATIO_ITEMDROPS_ONDEATH = (RATIO_ITEMDROPS_ONDEATH+i);
			if(RATIO_ITEMDROPS_ONDEATH>100){
				RATIO_ITEMDROPS_ONDEATH = 0;
			} else if(RATIO_ITEMDROPS_ONDEATH < 0){
				RATIO_ITEMDROPS_ONDEATH = 100;
			}
		} else {
			toggleOnDeathItemLossAction();
		}
		return Sound.valueOf(SCROLL);
	}
	
	/**
	 * Returns A String Representation Of
	 * The Current Setting For The On Death
	 * Item Drop Percent.
	 * 
	 */
	public String getOnDeathPercentString(){
		if(RATIO_ITEMDROPS_ONDEATH>0){
			return RATIO_ITEMDROPS_ONDEATH+"%";	
		} else {
			return "None";
		}
	}
	
	/**
	 * Returns The Actual Value Of The
	 * Current Setting For The On Death
	 * item Drop Percent.
	 * 
	 */
	public double getOnDeathPercent(){
		return (RATIO_ITEMDROPS_ONDEATH/100.00);
	}
	
	/**
	 * Toggles The Action Taken
	 * With A Player's Chest Items
	 * On Death.
	 * 
	 */
	private Sound toggleOnDeathItemLossAction(){
		ONDEATH_DELETE_ITEMS = ((ONDEATH_DELETE_ITEMS==0)?1:0);
		return Sound.valueOf(SCROLL);
	}
	
	/**
	 * Returns The Action Taken
	 * With A Player's Chest Items
	 * On Death.
	 * 
	 */
	public int getOnDeathItemLossAction(){
		return ONDEATH_DELETE_ITEMS;
	}
	
	/**
	 * Returns A String Representation
	 * Of The OnDeathItemLossAction Setting.
	 * 
	 */
	public String getOnDeathItemLossActionString(){
		return "Items "+((getOnDeathItemLossAction()==0)?"Drop":"Disappear");
	}
	
	/**
	 * Changes The Item Sort Option
	 * 
	 */
	public Sound advanceItemSortOption(){
		ITEM_SORT_OPTION+=1;
		ITEM_SORT_OPTION=(ITEM_SORT_OPTION%3);
		return Sound.valueOf(SCROLL);
	}
	
	/**
	 * Returns A String Representation Of
	 * The Current Setting For The Item Sort
	 * Option.
	 * 
	 */
	public String getItemSortOptionString(){
		return (ITEM_SORT_OPTION == 0)?"No Sorting":(ITEM_SORT_OPTION == 1)?"By Item ID":(ITEM_SORT_OPTION == 2)?"By Item Material Name":"";
	}
	
	/**
	 * Returns The Actual Value Of THe
	 * Current Setting For The Item Sort
	 * Option.
	 * 
	 */
	public int getItemSortOption(){
		return ITEM_SORT_OPTION;
	}
	
	/**
	 * Returns The Scrollbar Settings
	 * 
	 * 
	 */
	public int[] getScrollbarSettings(){
		return scrolls;
	}
	
	/**
	 * Sets Scrollbar Values
	 * 
	 * 
	 */
	public Sound setScrollbarValue(int value){
		this.scrolls[scrollFocus] = value;
		return Sound.valueOf(SCROLL);
	}
	
	/**
	 * Returns The Current Scrollbar Setting
	 * Index That Is In Focus
	 * 
	 * 
	 */
	public int getScrollFocus(){
		return scrollFocus;
	}
	
	/**
	 * Sets The Current Scrollbar Setting
	 * Index That Is In Focus
	 * 
	 * 
	 */
	public Sound setScrollFocus(int scrollFocus){
		this.scrollFocus = scrollFocus;
		return Sound.valueOf(SCROLL);
	}
	
	/**
	 * Returns The Worded Representations Of
	 * The Current Setting In Focus
	 * 
	 * 
	 */
	public String[] getScrollStrings(){
		return scrollsW;
	}
	
	/**
	 * Returns The Current Language Setting
	 * 
	 */
	public int getLanguageSetting(){
		return LANGUAGE_SETTING;
	}
	
	/**
	 * Jumps To The Next Language File
	 * If Such A File Exists
	 * 
	 */
	public Sound advanceLanguageSetting(Main main){
		int lang = LANGUAGE_SETTING;
		lang++;
		lang = lang%main.getActionLanguageFiles().size();
		this.LANGUAGE_SETTING = lang;
		return Sound.valueOf(SCROLL);
	}
	
	/**
	 * Returns Whether Maintenance Mode Is
	 * Currently Enabled
	 * 
	 */
	public boolean isMaintenance(){
		return MAINTENANCE_ENABLED;
	}
	
	/**
	 * Toggles Maintenance Mode
	 * 
	 */
	public Sound toggleMaintenance(){
		this.MAINTENANCE_ENABLED = (MAINTENANCE_ENABLED?false:true);
		return Sound.valueOf(SCROLL);
	}
	
	
	/*
	 * © 2016 Brandon Mueller
	 * DO NOT DE-COMPILE THIS SOFTWARE OR ATTEMPT ANY FORM OF REVERSE ENGINEERING!
	 */
}