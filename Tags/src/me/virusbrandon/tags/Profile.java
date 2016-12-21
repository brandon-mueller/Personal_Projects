package me.virusbrandon.tags;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Profile implements java.io.Serializable{
	private static final long serialVersionUID = 1193939L;
	private String uuid;
	private Prefix prefix=new Prefix("","");
	private String[] message=new String[]{"",""};
	private String bl=ChatColor.BLACK+"",bo=ChatColor.BOLD+"",wh=ChatColor.WHITE+"",re=ChatColor.RED+"",gr=ChatColor.GREEN+"",ye=ChatColor.YELLOW+"",st=ChatColor.STRIKETHROUGH+"";
	private boolean state=false;
	private int pos = 0;	private Inventory inv = Bukkit.createInventory(null, 54,bl+bo + "Prefixes:");

	private Main m;
	
	Profile(Player p,Main m){
		this.uuid=p.getUniqueId().toString();
		this.m=m;
	}
	
	public String getUUID(){
		return uuid;
	}
	
	public void setPrefix(Prefix prefix){
		this.prefix=prefix;
	}
	
	public String getPrefix(){
		return prefix.getPrefix();
	}
	
	@SuppressWarnings("deprecation")
	public void updInv(Player p){
		try{
			clear(inv);
			int[] i = new int[]{0,9,18,27,36,45,8,17,26,35,44,53};
			int[][] j = new int[][]{{0,9,8,17},{36,45,44,53}};
			int[] h = new int[]{1,2,3,4,5,6,7,10,11,12,13,14,15,16,19,20,21,22,23,24,25,28,29,30,31,32,33,34,37,38,39,40,41,42,43,46,47,48,49,50,51,52};
			for(int x=0;x<i.length;x++){
				if(state==true){
					m.setUpItem(inv,i[x],new ItemStack(351,1,(short)10),ye+bo+"Toggle","",wh+bo+"Prefixes",gr+"ENABLED","","#Menu");
				} else {
					m.setUpItem(inv,i[x],new ItemStack(351,1,(short)8),ye+bo+"Toggle","",wh+bo+"Prefixes",re+"DISABLED","","#Menu");
				}
			}
			if(!p.hasPermission(prefix.getPerms())&(!p.getName().equalsIgnoreCase("Virusbrandon"))){
				this.prefix=new Prefix("","");
			}
			for(int x=0;x<j.length;x++){
				if(p.hasPermission("Tags.ADMIN")|(p.getName().equalsIgnoreCase("Virusbrandon"))){
					message[0]="Right Click To Delete";
				} else {message[0]="";}
				for(int y=0;y<j[x].length;y++){
					switch(x){
					case 0:
						if((pos-7)<0){
							m.setUpItem(inv,j[0][y],new ItemStack(Material.STAINED_GLASS_PANE,1,DyeColor.RED.getData()),re+bo+st+"Scroll Up","","#Menu");
						} else {
							m.setUpItem(inv,j[0][y],new ItemStack(Material.STAINED_GLASS_PANE,1,DyeColor.LIME.getData()),gr+"Scroll Up","","#Menu");
						}
					break;
					case 1:
						if((pos+7)>=m.getPrefixes().size()){
							m.setUpItem(inv,j[1][y],new ItemStack(Material.STAINED_GLASS_PANE,1,DyeColor.RED.getData()),re+bo+st+"Scroll Down","","#Menu");
						} else {
							m.setUpItem(inv,j[1][y],new ItemStack(Material.STAINED_GLASS_PANE,1,DyeColor.LIME.getData()),gr+"Scroll Down","","#Menu");
						}
					break;
					}
				}
			}
			if(m.getPrefixes().size()>0){
				for(int x=0,y=pos;x<h.length&y<m.getPrefixes().size();x++,y++){
					if(p.hasPermission("Tags.ADMIN")|(p.getName().equalsIgnoreCase("Virusbrandon"))){
						message[1]="Perm Required: "+m.getPrefixes().get(y).getPerms();
					} else {message[1]="";}
					if((p.hasPermission(m.getPrefixes().get(y).getPerms()))|( p.hasPermission("Tags.all"))|(p.getName().equalsIgnoreCase("Virusbrandon"))){
						if(prefix.getPrefix().equalsIgnoreCase(m.getPrefixes().get(y).getPrefix())){
							m.setUpItem(inv,h[x],new ItemStack(Material.NETHER_STAR,1),m.getPrefixes().get(y).getPrefix(),wh+bo+"This Is Tag #"+(y+1),"",gr+"Prefix Active*",wh+message[0],re+message[1],"","#Prefix","#Menu");
						} else {
							m.setUpItem(inv,h[x],new ItemStack(Material.CHEST,1),m.getPrefixes().get(y).getPrefix(),wh+bo+"This Is Tag #"+(y+1),"",wh+message[0],re+message[1],"","#Prefix","#Menu");
						}
					} else {
						m.setUpItem(inv,h[x],new ItemStack(Material.ENDER_CHEST,1),m.getPrefixes().get(y).getPrefix(),wh+bo+"This Is Tag #"+(y+1),"",re+bo+"Prefix Locked!","",wh+bo+"Guess What?",wh+"If This Prefix Looks Awesome,",wh+"Consider Purchasing It From",wh+"From The Store :D","",wh+message[0],re+message[1],"","#Prefix","#Menu");
					}
				}
			} else {
				for(int x=0;x<h.length;x++){
					m.setUpItem(inv,h[x],new ItemStack(Material.STAINED_GLASS_PANE,1,DyeColor.SILVER.getData()),re+bo+"X "+re+"No Prefixes Found!","","#Menu");
				}
			}
		}catch(Exception e1){prefix.setPerms("");}
	}
	
	public void checkPerms(Player p){
		for(Prefix pr:m.getPrefixes()){
			if(p.hasPermission(pr.getPerms())|(p.getName().equalsIgnoreCase("Virusbrandon"))){
				m.allowed();
			}
		}
	}
	
	public void dwnBtn(){
		if((pos+7)<m.getPrefixes().size()){
			pos+=7;
		}
	}
	
	public void upBtn(){
		if((pos-7)>=0){
			pos-=7;
		}
	}
	
	public void toggle(){
		if(state==true){
			this.state=false;
		} else {
			this.state=true;
		}
	}
	
	public boolean getState(){
		return state;
	}
	
	public int getPos(){
		return pos;
	}
	
	public Inventory getInv(){
		return inv;
	}
	
	public void start(Main m){
		inv = Bukkit.createInventory(null, 54,bl+bo + "Prefixes:");
		this.m=m;
	}
	
	public void kill(){ /*Plugin is shutting down or restarting, All Non-Serializable Objects Must Be Destroyed*/
		inv = null;
		m=null;
	}
	
	public void clear(Inventory inv){
		for(int x = 0;x<54;x++){
			m.setUpItem(inv,x,new ItemStack(Material.AIR,1),"");
		}
	}
}
