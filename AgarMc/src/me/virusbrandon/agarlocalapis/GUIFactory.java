package me.virusbrandon.agarlocalapis;

import java.util.*;

import org.bukkit.inventory.*;
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
			ItemMeta met = st.getItemMeta();
			met.setDisplayName(disp);
			for(int x = 0;x<lore.length;x++){
				l.add(lore[x]);	 
			}
			if(l.size() > 0){
				met.setLore(l);
			}
			st.setItemMeta(met);
			inv.setItem(slot, st);
			l.clear();
		} catch (Exception e5){}
		return this;
	}
	
	
	/**
	 * Draw Function:
	 * 
	 * Eliminate The Need For Repeated Characters,
	 * Let This Function Auto-Concat For You ^_^
	 * 
	 */
	public String draw(String cHar,int howMany,String temp){
		return (howMany>0)?draw(cHar,howMany-1,temp+cHar):temp;		
	}
}

/*
 * � 2016 Brandon Mueller
 * DO NOT DE-COMPILE THIS SOFTWARE OR ATTEMPT ANY FORM OF REVERSE ENGINEERING!
 */