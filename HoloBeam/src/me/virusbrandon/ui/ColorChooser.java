package me.virusbrandon.ui;

import me.virusbrandon.HoloBeam.Main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ColorChooser {
	private Main main;
	private Inventory inv;
	@SuppressWarnings("deprecation")
	private Byte[] dc = new Byte[]{DyeColor.LIME.getData(),DyeColor.RED.getData(),DyeColor.BLACK.getData()};
	private int[] btn = new int[]{9,17};
	private String temp="";
	
	private int a=0,b=6;
	private String bl = ChatColor.BLACK+"",wh=ChatColor.WHITE+"",bo=ChatColor.BOLD+"",re=ChatColor.RED+"",gr=ChatColor.GREEN+"",sr=ChatColor.STRIKETHROUGH+"";
	
	public ColorChooser(Main main){
		this.main = main;
		inv = Bukkit.createInventory(null, 27,bl+bo+"Beam Color");
		refresh();
	}
	
	private void refresh(){
		try{
			temp = "";
			for(int x=0;x<27;x++){
				main.gF().setUpItem(inv,x,new ItemStack(Material.STAINED_GLASS_PANE,1,dc[2])," ",bl+"#BEAM");
			}
			for(int x = btn[0];x<16;x++){
				ItemStack st =  new ItemStack(Material.STAINED_GLASS_PANE,a+((x+1)-btn[0]));
				ItemMeta met = st.getItemMeta();
				st.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
				main.gF().setUpItem(inv,x+1,st,wh+bo+"Click To Choose This Color",ChatColor.values()[a+(x-btn[0])]+"█████     "+wh+ChatColor.values()[a+(x-btn[0])].name(),bl+"#CHOOSE",bl+"#BEAM");
				met = inv.getItem(x+1).getItemMeta();
				temp+=met.getLore().get(met.getLore().size()-3).substring(0,3);
			}
			for(int i:btn){
				ItemStack st = new ItemStack(Material.STAINED_GLASS_PANE,1,(i==btn[0])?(a>0)?dc[0]:dc[1]:(b<ChatColor.values().length-7?dc[0]:dc[1]));
				st.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
				main.gF().setUpItem(inv,i,st,(i==btn[0])?(a>0)?gr+bo+"Scroll Left":re+bo+sr+"Can't Scroll Left":(i==btn[1])?(b<ChatColor.values().length-7)?gr+bo+"Scroll Right":re+bo+sr+"Can't Scroll Right":"","",wh+bo+"Colors Showing:",temp,"",(i==btn[0])?bl+"#LEFT":bl+"#RIGHT",bl+"#BEAM");
			}
		}catch(Exception e1){e1.printStackTrace();}
	}
	
	public void scroll(int dir){
		if(((dir<0)&(a>0))|((dir>0)&(b<(ChatColor.values().length-7)))){
			a+=dir;b+=dir;
		}
		refresh();
	}
	
	public void open(Player p){
		p.openInventory(inv);
	}
}
