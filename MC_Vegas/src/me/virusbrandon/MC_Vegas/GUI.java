package me.virusbrandon.MC_Vegas;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GUI implements java.io.Serializable {
	private static final long serialVersionUID = 193294999L;

	private String gr = ChatColor.GREEN + "",re = ChatColor.RED +"",ye=ChatColor.YELLOW+"",go = ChatColor.GOLD + "",bo = ChatColor.BOLD + "",wh = ChatColor.WHITE + "",bl = ChatColor.BLACK + "",aq = ChatColor.AQUA + "";
	List<String> l = new ArrayList<>();
	
	GUI(){} //Default Constructor
	public void slotMenu(Player p,Profile pl){
		Inventory inv = Bukkit.createInventory(null, 9,bl+bo+"Slots Menu!");
		setUpItem(inv,1,new ItemStack(Material.NETHER_STAR,1),wh+bo+"Spin The Slot!",ye+bo+"- Requires 1 Coin Per Spin","  ",re+bo+"- Running Out Of Coins?",gr+bo+"- Purchase "+aq+bo+"More"+re+bo+" On",gr+bo+"- "," Our Website ",go+bo+"- The More Coins You Purchase",go+bo+"- The More Bonus Coins You Get!");
		setUpItem(inv,3,new ItemStack(Material.DOUBLE_PLANT,1),go+bo+"Your Coin Balance: ",gr+pl.getBalance(),"",go+bo+"Ticket Balance:",gr+pl.getTickets(),"",re+bo+"- Purchase Coins On",re+bo+" Our Website");
		setUpItem(inv,5,new ItemStack(Material.CHEST,1),gr+bo+"Prizes!",wh+bo+"- Click To Take A Look!");
		setUpItem(inv,7,new ItemStack(Material.REDSTONE_BLOCK,1),re+bo+"Walk Away!"," ",aq+bo+"- Someone Is GOING To Win Soon!",re+bo+"- Don't Leave!");
		p.openInventory(inv);
	}
	
	@SuppressWarnings("deprecation")
	public void payouts(Player p,SlotManager m){
		Inventory inv = Bukkit.createInventory(null, 9,bl+bo+"Slots Menu > Prizes!");
		for(int x = 0;x<m.getWins().size();x++){
			Win w = m.getWins().get(x);
			setUpItem(inv,x,new ItemStack(w.getID(),1),wh+bo+"Prize: "+re+bo+w.getPrize(),"  ",go+"- Match 3 Of These",go+"- On The Center Line",go+"- To Win This Fantastic Prize!"," ",gr+"- There Are " + w.getAvailable()+" Of",gr+"- These Prizes Left!");
		}
		setUpItem(inv,8,new ItemStack(Material.REDSTONE_BLOCK,1),re+bo+"Go Back");
		p.openInventory(inv);
	}
	
	public Inventory setUpItem(Inventory inv,int slot,ItemStack st,String disp,String ... lore){	 
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
			return inv;
		} catch (Exception e5){return null;}
	}
	
	@SuppressWarnings("deprecation")
	public Inventory frm(Inventory inv){
		int ph = 0;
		int[] i = new int[]{0,1,2,3,4,5,6,7,8,17,26,35,44,53,52,51,50,49,48,47,46,45,36,27,18,9};
		DyeColor[] d = new DyeColor[]{DyeColor.BLUE,DyeColor.LIGHT_BLUE};
		for(int x = 0;x<i.length;x++){
			setUpItem(inv,i[x],new ItemStack(Material.STAINED_GLASS_PANE,1,d[ph].getData()),"");
			ph++;
			ph = (ph%2);
		}
		return inv;
	}
}
