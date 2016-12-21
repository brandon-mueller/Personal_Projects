package me.virusbrandon.localapis;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GUIFactory {
	List<String> l = new ArrayList<>();
	
	public GUIFactory(){}
	
	/**
	 * This Method Sets Up A Single Item On The
	 * User Interface
	 * 
	 * @param inv
	 * @param slot
	 * @param st
	 * @param disp
	 * @param lore
	 * @return
	 * 
	 * 
	 */
	public GUIFactory setUpItem(Inventory inv,int slot,ItemStack st,String disp,String ... lore){	 
		try{
			inv.setItem(slot, st);
			ItemMeta met = inv.getItem(slot).getItemMeta();
			met.setDisplayName(disp);
			for(int x = 0;x<lore.length;x++){
				l.add(lore[x]);	 
			}
			if(l.size() > 0){
				met.setLore(l);
			}
			inv.getItem(slot).setItemMeta(met);
			l.clear();
		} catch (Exception e5){}
		return this;
	}
}
