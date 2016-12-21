package me.virusbrandon.bottomlesschests;

import org.bukkit.inventory.ItemStack;

public class ChestItem{
	private ItemStack ITEM_STACK = null;
	private int ITEM_ID = 0;
	private int ITEM_QUANTITY = 0;
	private byte ITEM_DATA = 0;
	private short ITEM_DURABILITY = 0;
	private String ORG_WORLD = "";
	
	/**
	 * ChestItem Constructor
	 * 
	 * Tests Items For Various Attributes
	 * And Saves Those That Apply To Each Item
	 * 
	 * @param stack
	 * 
	 * 
	 */
	@SuppressWarnings("deprecation")
	public ChestItem(ItemStack stack, Main main, Chest chest){
		if(stack!=null){
			this.ITEM_STACK = stack;
			this.ITEM_ID = stack.getTypeId();
			this.ITEM_DATA = stack.getData().getData();
			this.ITEM_QUANTITY = stack.getAmount();
			this.ITEM_DURABILITY = stack.getDurability();
		}
	}
	
	/**
	 * Returns The ItemStack Assoicated
	 * With This ChestItem.
	 * 
	 */
	public ItemStack getItemStack(){
		return ITEM_STACK;
	}
	
	/**
	 * Sets The ItemStack That Is To Be
	 * Associated With This ChestItem.
	 */
	public ChestItem setItemStack(ItemStack ITEM_STACK){
		this.ITEM_STACK = ITEM_STACK;
		return this;
	}
	
	/**
	 * Returns The Item's ID
	 * 
	 * @return
	 * 
	 * 
	 */
	public int getItemId(){
		return ITEM_ID;
	}
	
	/**
	 * Sets The Item's ID
	 * 
	 * 
	 */
	@SuppressWarnings("deprecation")
	public ChestItem setItemId(int ITEM_ID){
		this.ITEM_ID = ITEM_ID;
		this.ITEM_STACK.setTypeId(ITEM_ID);
		return this;
	}
	
	/**
	 * Returns The Item's Data
	 * 
	 * @return
	 * 
	 * 
	 */
	public byte getData(){
		return ITEM_DATA;
	}
	
	/**
	 * Sets The Item's Data
	 * 
	 * 
	 */
	@SuppressWarnings("deprecation")
	public ChestItem setData(int ITEM_DATA){
		this.ITEM_DATA = (byte)ITEM_DATA;
		this.ITEM_STACK.getData().setData((byte)ITEM_DATA);
		return this;
	}
	
	/**
	 * Returns The Item's Quantity
	 * 
	 * @return
	 * 
	 * 
	 */
	public int getAmount(){
		return ITEM_QUANTITY;
	}
	
	/**
	 * Sets The Item's Quantity
	 * 
	 * 
	 */
	public ChestItem setAmount(int amount){
		this.ITEM_QUANTITY = amount;
		this.ITEM_STACK.setAmount(amount);
		return this;
	}
	
	/**
	 * Returns The Item's Durability
	 * 
	 */
	public short getDurability(){
		return ITEM_DURABILITY;
	}
	
	/**
	 * Sets The Item's Durability
	 * 
	 */
	public ChestItem setDurability(short ITEM_DURABILITY){
		this.ITEM_DURABILITY = ITEM_DURABILITY;
		this.ITEM_STACK.setDurability(ITEM_DURABILITY);
		return this;
	}
	
	/**
	 * Gets The Original World That This
	 * Item Was Stored In
	 * 
	 */
	public String getOrgWorld(){
		return ORG_WORLD;
	}
	
	/**
	 * Sets The Original World That This
	 * Item Was Stored In
	 * 
	 */
	public ChestItem setOrgWorld(String ORG_WORLD){
		this.ORG_WORLD = ORG_WORLD;
		return this;
	}
	
	/**
	 * ChestItem Equals Method
	 * 
	 * Determines If This ChestItem Compared With Another
	 * ChestItem Are The EXACT Same.
	 * 
	 */
	public boolean equals(ChestItem other){
		return ITEM_STACK.equals(other.getItemStack());
	}
	
	/*
	 * © 2016 Brandon Mueller
	 * DO NOT DE-COMPILE THIS SOFTWARE OR ATTEMPT ANY FORM OF REVERSE ENGINEERING!
	 */
}