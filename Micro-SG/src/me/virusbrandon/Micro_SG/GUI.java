package me.virusbrandon.Micro_SG;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GUI implements java.io.Serializable{
	private static final long serialVersionUID = 154323543L;
	private int slot;
	private List<String> l = new ArrayList<>();
	private String gr = ChatColor.GREEN + "",re = ChatColor.RED +"",ye=ChatColor.YELLOW+"",go = ChatColor.GOLD + "",bo = ChatColor.BOLD + "" + "",wh = ChatColor.WHITE + "",bl = ChatColor.BLACK + "",kk=ChatColor.MAGIC+"",dr=ChatColor.DARK_RED+"";
	private Inventory inv = Bukkit.createInventory(null, 54,bl+bo + "          GET READY!   "); 
	private Inventory spec = Bukkit.createInventory(null, 54,slot+" "+bl+bo + "      Spectate Who?   ");
	private Inventory delinv = Bukkit.createInventory(null, 54,bl+bo + "Delete Player Stats?");
	private Random r = new Random();
	private int t1=0,t2=0;
	int[][][] numbers = new int[][][]
			{{{1,2,3,12,21,30,39,48,47,46,37,28,19,10},{2,11,20,29,38,47},{1,2,3,12,21,20,19,28,37,46,47,48},{1,2,3,12,21,30,39,48,47,46,20,19},{1,10,19,20,21,12,3,30,39,48},{1,2,3,10,19,20,21,30,39,48,47,46},{1,2,3,10,19,28,37,46,47,48,39,30,21,20},{1,2,3,12,21,30,39,48},{1,2,3,12,21,30,39,48,47,46,37,28,19,10,20},{20,19,10,1,2,3,12,21,30,39,48,47,46}},{{5,6,7,16,25,34,43,52,51,50,41,32,23,14},{6,15,24,33,42,51},{5,6,7,16,25,24,23,32,41,50,51,52},{5,6,7,16,25,34,43,52,51,50,24,23},{5,14,23,24,25,16,7,34,43,52},{5,6,7,14,23,24,25,34,43,52,51,50},{5,6,7,14,23,32,41,50,51,52,43,34,25,24},{5,6,7,16,25,34,43,52},{5,6,7,16,25,34,43,52,51,50,41,32,23,14,24},{5,6,7,14,23,24,16,25,34,43,52,51,50}}};
	private int[] icon = new int[]{45,36,27,18,9,0,8,17,26,35,44,53};
	private int[] icon2 = new int[]{0,1,2,3,4,5,6,7,8,17,26,35,44,53,52,51,50,49,48,47,46,45,36,27,18,9};
	private int[] middle = new int[]{10,11,12,13,14,15,16,19,20,21,22,23,24,25,28,29,30,31,32,33,34,37,38,39,40,41,42,43};
	private ArenaManager m;
	private DecimalFormat d = new DecimalFormat("###.##");
	
	GUI(int slot,ArenaManager m){
		this.slot=slot;
		this.m=m;
	}
	
	public void intro(Player p, int time,int players,int cdn){
		if(time>30){
			clear(inv);
			try{
				if((time>0)&(time<(cdn+1))){
					if(!p.getOpenInventory().getTopInventory().getTitle().equalsIgnoreCase(bl+bo+"          GET READY!   ")){
						p.openInventory(inv);
					}
					if((t1+2>=0)&(t1+2<icon.length)){
						setUpItem(inv,icon[t1+2],new ItemStack(Material.OBSIDIAN,1),gr+bo+"Good Luck Tributes!","",go+bo+"May The Odds Be",go+bo+"Ever In Your Favor!","","#Micro-SG");
					}
					if((t1+1>=0)&(t1+1<icon.length)){
						setUpItem(inv,icon[t1+1],new ItemStack(Material.OBSIDIAN,1),gr+bo+"Good Luck Tributes!","",go+bo+"May The Odds Be",go+bo+"Ever In Your Favor!","","#Micro-SG");
					}
					if((t1>=0)&(t1<icon.length)){
						setUpItem(inv,icon[t1],new ItemStack(Material.OBSIDIAN,1),gr+bo+"Good Luck Tributes!","",go+bo+"May The Odds Be",go+bo+"Ever In Your Favor!","","#Micro-SG");
					}
					t2++;
					if(t2%players==0){
						t2=0;
						t1++;
					}
					if(t1==icon.length){
						t1 = -2;
					}
					String m;
					if((cdn-time)>10){
						m="LAPIS_BLOCK";
					} else {
						m="REDSTONE_BLOCK";
					}
					for(int x =0;x<2;x++){
						if(x==0){
							for(int y=0;y<numbers[x][((cdn-time)/10)].length;y++){
								setUpItem(inv,numbers[x][((cdn-time)/10)][y],new ItemStack(Material.getMaterial(m),1),ye+bo+"Starting In: "+(cdn-time),"",wh+bo+"Good Luck",wh+bo+"Tributes!","","#Micro-SG");
							}
						}else{
							for(int y=0;y<numbers[x][(9-((time-1)%10))].length;y++){
								setUpItem(inv,numbers[x][(9-((time-1)%10))][y],new ItemStack(Material.getMaterial(m),1),ye+bo+"Starting In: "+(cdn-time),"",wh+bo+"Good Luck",wh+bo+"Tributes!","","#Micro-SG");
							}
						}	
					}
				}
			}catch(Exception e1){}
		}
	}
	
	public void specMenu(ArrayList<Player> p, Arena a){
		m.frm5(spec);
		for(int x=0;x<p.size();x++){
			float health = (float)((Damageable)a.getPlayers().get(x)).getHealth();
			float hunger = p.get(x).getFoodLevel();
			setUpItem(spec,middle[x],new ItemStack(Material.IRON_CHESTPLATE,1),wh+bo+p.get(x).getName(),go+bo+"- "+gr+bo+"Health: "+bar(health,20)+wh+bo+"  --  "+AsP(health),go+bo+"- "+ye+bo+"Hunger: "+bar(hunger,20)+wh+bo+"  --  "+AsP(hunger),go+bo+"- "+re+bo+"Closest Player: ",go+bo+"- "+re+bo+a.getNearest(a.getPlayers().get(x)),"",wh+bo+"Status:",gr+bo+"ALIVE","",ye+bo+"WARNING:",ye+"If You Spectate Other Players",ye+"Or Move Between Arenas While",ye+"Spectating, You Might Not Be Able",ye+"To Revive Yourself In A Game",ye+"That You Were Eliminated From!","","#Micro-SG");
		}
	}
	
	public Inventory spec(){
		return spec;
	}
	
	public String AsP(float input){
		return d.format((input/20)*100)+"%";
	}
	
	public String bar(float current, float max){
		String bar = "";
		float s = (((current/max)*100)/2.5f);
		for(int x=0;x<(s-1);x++){
			bar+="|";
		}
		bar+=kk+"|";
		return color(bar,current,max);
	}
	
	private String color(String bar,float current,float max){
		if((current/max)>.6f){
			return gr+bar;
		} else if((current/max)>.3f){
			return ye+bar;
		} else if((current/max)>.15f){
			return re+bar;
		} else {
			return dr+bar;
		}
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
	
	public void clear(Inventory inv){
		for(int x = 0;x<54;x++){
			setUpItem(inv,x,new ItemStack(Material.AIR,1),"");
		}
	}
	
	@SuppressWarnings("deprecation")
	public Inventory delfrm(String s){
		clear(delinv);
		for(int x = 0;x<26;x++){
			setUpItem(delinv,icon2[x],new ItemStack(Material.STAINED_GLASS_PANE,1,DyeColor.BLACK.getData()),gr+bo+"Micro SG!","","#Micro-SG");
		}
		int[][]i=new int[][]{{10,11,12,19,20,21,133},{14,15,16,23,24,25,152}};String[]ss=new String[]{gr+bo+"CONFIRM!",re+bo+"CANCEL!"};
		for(int x=0;x<i.length;x++){
			for(int y=0;y<i[x].length-1;y++){
				setUpItem(delinv,i[x][y],new ItemStack(i[x][i[x].length-1],1),ss[x],"",re+bo+"Deleted Stats",re+bo+"CANNOT Be Recovered!","",s,"#Micro-SG");
			}
		}	
		return delinv;
	}
	
	@SuppressWarnings("deprecation")
	public void frm(Inventory inv){
		clear(inv);
		for(int x = 0;x<26;x++){
			try{
				setUpItem(inv,icon[x],new ItemStack(Material.STAINED_GLASS_PANE,1,m.getD()[r.nextInt(5)].getData()),gr+bo+"Micro SG!","",m.hT(),m.sd()+ye+bo+"Current Time:",m.sd()+m.getTime(),m.sd(),m.sd()+"Matches Running:",m.sd()+go+m.getInGame().size(),m.sd(),m.sd()+re+bo+"Online Players:",m.sd()+re+Bukkit.getOnlinePlayers().size()+"",m.sd(),m.sd()+wh+bo+"Ⓒ "+m.getCpyr().format(new Date()),m.sd()+wh+m.getIP(),m.sd()+"",m.hB(),"","#Menu-Item","#Micro-SG");
			} catch(Exception e1){/*Date Object Not Initialized*/}
		}
	}
	
	public Inventory getInv(){
		return inv;
	}
}
