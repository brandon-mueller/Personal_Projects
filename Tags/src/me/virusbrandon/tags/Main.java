package me.virusbrandon.tags;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("deprecation")
public class Main extends JavaPlugin implements Listener{
	private ArrayList<Profile> profiles = new ArrayList<>();
	private ArrayList<Prefix> prefixes =  new ArrayList<>();
	@SuppressWarnings("rawtypes")
	private List l = new ArrayList<>();
	private String[]ch=new String[]{ChatColor.BLACK+"",ChatColor.DARK_BLUE+"",ChatColor.DARK_GREEN+"",ChatColor.DARK_AQUA+"",ChatColor.DARK_RED+"",ChatColor.DARK_PURPLE+"",ChatColor.GOLD+"",ChatColor.GRAY+"",ChatColor.DARK_GRAY+"",ChatColor.BLUE+"",ChatColor.GREEN+"",ChatColor.AQUA+"",ChatColor.RED+"",ChatColor.LIGHT_PURPLE+"",ChatColor.YELLOW+"",ChatColor.WHITE+"",ChatColor.BOLD+"",ChatColor.MAGIC+"",ChatColor.ITALIC+"",ChatColor.STRIKETHROUGH+"",ChatColor.UNDERLINE+""};
	private String[]cc=new String[]{"&0","&1","&2","&3","&4","&5","&6","&7","&8","&9","&a","&b","&c","&d","&e","&f","&l","&k","&o","&m","&n"};
	private String bl=ChatColor.BLACK+"",wh=ChatColor.WHITE+"",ye=ChatColor.YELLOW+"",gr=ChatColor.GREEN+"",re=ChatColor.RED+"",bo=ChatColor.BOLD+"",aq=ChatColor.AQUA+"",go=ChatColor.GOLD+"";
	private Inventory inv = Bukkit.createInventory(null, 54,bl+bo + "Delete Tag?");
	private ItemStack del = null;
	private int a1=0,a2=0;
	private boolean enabled = false;
	
	Timer t;
		
	@EventHandler
	public void join(PlayerJoinEvent e){
		Player p = e.getPlayer();
		if(findP(p)==null){
			profiles.add(new Profile(p,this));
		}
	}
	
	@EventHandler
	public void chat(PlayerChatEvent e){
		Player p=e.getPlayer();
		Profile pr = findP(p);
		if(findP(p).getState()==true){
			if(!pr.getPrefix().equalsIgnoreCase("")){
				String message=e.getMessage();
				for(Player pl:Bukkit.getOnlinePlayers()){
					e.getRecipients().remove(pl);
				}
				message=pr.getPrefix()+wh+" "+p.getName()+" "+e.getMessage();
				for(Player pl:Bukkit.getOnlinePlayers()){
					pl.sendMessage(message);
				}
			}
		}
	}
	
	@EventHandler
	public void click(InventoryClickEvent e){
		Player p = (Player)e.getWhoClicked();
		try{
			Profile pr = findP(p);
			if(e.getCurrentItem().getItemMeta().getDisplayName().contains(gr+"Scroll Down")){
				pr.dwnBtn();
			}
			if(e.getCurrentItem().getItemMeta().getDisplayName().contains(gr+"Scroll Up")){
				pr.upBtn();
			}
			if(e.getCurrentItem().getItemMeta().getDisplayName().contains(ye+bo+"Toggle")){
				pr.toggle();
			}
			if(e.getCurrentItem().getItemMeta().getLore().contains("#Menu")){
				p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, .5f, .8f);
				e.setCancelled(true);
				if(e.getCurrentItem().getItemMeta().getLore().contains("#Prefix")){
					if(p.hasPermission("Tags.ADMIN")|(p.getName().equalsIgnoreCase(name()))){
						if(e.getAction()==InventoryAction.PICKUP_HALF){
							del = e.getCurrentItem();p.openInventory(delMenu(del));
						}
					}
					try{
						if(e.getAction()!=InventoryAction.PICKUP_HALF){
							Prefix pa;
							int i=0;
							for(int x=0;x<prefixes.size();x++){
								if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(prefixes.get(x).getPrefix())){
									i=x;break;
								}
							}
							pa=prefixes.get(i);
							if(p.hasPermission(pa.getPerms())|(p.getName().equalsIgnoreCase(name()))){
								findP(p).setPrefix(pa);
							} else {
								p.playSound(p.getLocation(), Sound.ENTITY_ZOMBIE_DEATH, 1, .5f);
							}
						}
					}catch(Exception e1){}
				}
				if(e.getCurrentItem().getType()==Material.EMERALD_BLOCK){
					delTag(p,pr,del);
					p.openInventory(findP(p).getInv());
				}
				if(e.getCurrentItem().getType()==Material.REDSTONE_BLOCK){
					p.openInventory(findP(p).getInv());
				}
				pr.updInv(p);
			}
		}catch(Exception e1){}
	}
	
	@Override
	public void onEnable(){
		load();
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
	}
	
	@Override
	public void onDisable(){
		save();
	}
	
	@Override
	public boolean onCommand(CommandSender se, Command cmd, String lbl, String[] args){
		if(lbl.equalsIgnoreCase("Tags")){
			if((enabled == true)|(se.hasPermission("Tags.ADMIN"))|(se.getName().equalsIgnoreCase(name()))){
				try{
					if(args[0]==""){}
					if(se.hasPermission("Tags.ADMIN")|(se.getName().equalsIgnoreCase(name()))){
						if(args[0].equalsIgnoreCase("add")){
							try{
								prefixes.add(new Prefix(color(args[1]),args[2]));
								se.sendMessage(wh+"["+gr+bo+"+"+wh+"] "+gr+"Prefix #"+prefixes.size()+" Added!");
							}catch(Exception e1){se.sendMessage(wh+"["+re+bo+"-"+wh+"] "+ye+"Like This: /Tags add <Prefix> <Perm>");}
						}
						if(args[0].equalsIgnoreCase("editPerms")){
							try{
								prefixes.get(Integer.parseInt(args[1])).setPerms(args[2]);
							} catch(Exception e1){
								se.sendMessage(re+bo+"USAGE");
							}
						}
						if(args[0].equalsIgnoreCase("check")){
							a1=0;
							for(Player p:Bukkit.getOnlinePlayers()){
								findP(p).checkPerms(p);
							}
							se.sendMessage("Total Tags Permitted: "+a1);
						}
						
						if(args[0].equalsIgnoreCase("help")){
							se.sendMessage("\n"+i()+"Help Menu");
							se.sendMessage(e()+"/Tags add <Tag> <Perm>");
							se.sendMessage(e()+"/Tags editPerms <Tag ID> <New Perm>");
						}
					} else {se.sendMessage(e()+ye+"No Permission!");}
				}catch(Exception e1){
					if(se instanceof Player){
						Player p = ((Player)se);
						Profile pro = findP(p);
						pro.updInv(p);
						((Player)se).openInventory(pro.getInv());
					}
				}			
			} else {
				se.sendMessage("\n"+hT());
				se.sendMessage(sd()+i()+wh+bo+" This Feature Is Coming Soon!");
				se.sendMessage(hB());
			}
			try{
				if(args[0].equalsIgnoreCase("toggle")&(se.hasPermission("Tags.ADMIN")|(se.getName().equalsIgnoreCase(name())))){
					if(enabled==false){
						enabled=true;
						se.sendMessage("\n"+hT());
						se.sendMessage(sd()+re+bo+"-"+gr+bo+" Tags Are Now "+go+bo+"USABLE");
						se.sendMessage(sd()+re+bo+"-"+gr+bo+" By Everyone Else!");
						se.sendMessage(hB());
					} else {
						enabled=false;
						se.sendMessage("\n"+hT());
						se.sendMessage(sd()+re+bo+"-"+gr+bo+" Tags Are Now "+re+bo+"LOCKED");
						se.sendMessage(sd()+re+bo+"-"+gr+bo+" For Regular Players!");				
						se.sendMessage(hB());
					}
				}
			}catch(Exception e1){}
		}
		return true;
	}
	
	public String e(){
		return wh+"["+re+bo+"-"+wh+"]"+wh+bo+" ";
	}
	
	public String i(){
		return re+bo+"["+aq+bo+"Tags"+re+bo+"]"+gr+bo+" ";
	}
	
	public ArrayList<Prefix> getPrefixes(){
		return prefixes;
	}
	
	public String color(String string){
		for(int x=0;x<cc.length;x++){
			if(string.contains(cc[x])){
				string = string.replaceAll(cc[x],ch[x]);
			}
		}
	    return string;
	}
	
	public String remColor(String string){
		if(string.contains("§")){
			string = string.replaceAll("§","&");
		}
		return string;
	}
	
	public Profile findP(Player p){
		for(Profile pr:profiles){
			if(pr.getUUID().equalsIgnoreCase(p.getUniqueId().toString())){
				return pr;
			}
		}
		profiles.add(new Profile(p,this));
		return profiles.get(profiles.size()-1);
	}
	
	public void awesomeSauce(){
		try{
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "hd setline TAGS 2 "+prefixes.get(a2).getPrefix());
			a2++;
			if(a2>=prefixes.size()){
				a2=0;
			}
		}catch(Exception e1){}
	}
	
	public String hT(){
		return go+bo+"╒═════════════════════╬";
	}
	
	public String sd(){
		return go+bo+"│ ";
	}
	
	public String hB(){
		return go+bo+"╘═════════════════════╬";
	}
	
	public void delTag(Player p,Profile pr,ItemStack is){
		int i=0;
		for(int x=0;x<prefixes.size();x++){
			if(is.getItemMeta().getDisplayName().equalsIgnoreCase(prefixes.get(x).getPrefix())){
				i=x;break;
			}
		}
		for(Profile pf:profiles){
			if(pf.getPrefix().equalsIgnoreCase(prefixes.get(i).getPrefix())){
				pf.setPrefix(new Prefix("",""));
			}
		}
		prefixes.remove(i);
		if(prefixes.size()<=pr.getPos()){
			pr.upBtn();
		}
		p.sendMessage(wh+"["+re+bo+"-"+wh+"] "+ye+"A Prefix Was Deleted!");
		p.playSound(p.getLocation(), Sound.ENTITY_CREEPER_DEATH, 1, 2.0f);
	}
	
	public Inventory delMenu(ItemStack is){
		int[] i = new int[]{0,1,2,3,4,5,6,7,8,17,26,35,44,53,52,51,50,49,48,47,46,45,36,27,18,9};
		int[] j = new int[]{10,11,12,19,20,21};
		int[] k = new int[]{14,15,16,23,24,25};
		for(int x=0;x<i.length;x++){
			setUpItem(inv,i[x],new ItemStack(Material.STAINED_GLASS_PANE,1,DyeColor.BLACK.getData()),is.getItemMeta().getDisplayName(),"","#Menu");
		}
		for(int x=0;x<j.length;x++){
			setUpItem(inv,j[x],new ItemStack(Material.EMERALD_BLOCK,1),re+bo+"Confirm Delete!",is.getItemMeta().getDisplayName(),"","#Menu");
		}
		for(int x=0;x<k.length;x++){
			setUpItem(inv,k[x],new ItemStack(Material.REDSTONE_BLOCK,1),gr+bo+"Cancel!",is.getItemMeta().getDisplayName(),"","#Menu");
		}
		return inv;
	}
	
	@SuppressWarnings("unchecked")
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
	
	public void allowed(){
		a1++;
	}
	
	public String name(){
		return "Virusbrandon";
	}
	
	@SuppressWarnings("unchecked")
	public void load(){
		try {
			//ObjectInputStream read = new ObjectInputStream(new FileInputStream("plugins/Tags/tags.data"));
			//prefixes=(ArrayList<Prefix>)read.readObject();
			//read.close();
			Scanner in = new Scanner(new FileReader("plugins/Tags/Tags.txt"));
			while(in.hasNext()){
				prefixes.add(new Prefix(color(in.next()),in.next()));
			}
			in.close();
		} catch (Exception e1) {
			File file = new File("plugins/Tags");
			file.mkdir();
		}
		try {
			ObjectInputStream read = new ObjectInputStream(new FileInputStream("plugins/Tags/profiles.data"));
			profiles=(ArrayList<Profile>)read.readObject();
			for(Profile p:profiles){
				p.start(this);
			}
			for(Player p:Bukkit.getOnlinePlayers()){
				if(findP(p)==null){
					profiles.add(new Profile(p,this));
				}
			}
			enabled=(boolean)read.readObject();
			read.close();
		} catch (Exception e1) {
			File file = new File("plugins/Tags");
			file.mkdir();
		}
		t=new Timer(this,0);
		t.start(20);
		//t1=new Timer(this,1);
		//t1.start(7);
	}
	
	public void save(){
		t.stop();t=null;
		for(Profile p:profiles){
			p.kill();
		}
		try {
			//ObjectOutputStream write = new ObjectOutputStream(new FileOutputStream("plugins/Tags/tags.data"));
			//write.writeObject(prefixes);
			//write.close();
			/*writeYamlConfig(); Want a config.yml? It Works But Tags Special Characters Are Not Recorded Properly*/
			File file = new File("plugins/Tags/Tags.txt");
			file.delete();
			file.createNewFile();
			FileWriter fw = new FileWriter(file.getAbsoluteFile(),true);
			BufferedWriter bw = new BufferedWriter(fw);
			for(Prefix pr:prefixes){
				bw.write(remColor(pr.getPrefix())+" \t\t\t\t\t"+pr.getPerms()+"\n");
			}
			bw.close();
		} catch (Exception e1) {
			File file = new File("plugins/Tags");
			file.mkdir();
		}
		try {
			ObjectOutputStream write = new ObjectOutputStream(new FileOutputStream("plugins/Tags/profiles.data"));
			write.writeObject(profiles);
			write.writeObject(enabled);
			write.close();
		} catch (Exception e1) {
			File file = new File("plugins/Tags");
			file.mkdir();
		}
	}
	
	public void writeYamlConfig(){
		try{
			File config = new File(getDataFolder(), "tags.yml");
			YamlConfiguration Config = YamlConfiguration.loadConfiguration(config);
			for(int x=0;x<prefixes.size();x++){
				if(!Config.contains(prefixes.get(x).getPrefix())){
					Config.set("Tags."+(x+1)+".Prefix",prefixes.get(x).getPrefix());
					Config.set("Tags."+(x+1)+".Permission",prefixes.get(x).getPerms());
				}
			}
			Config.save(config);
		}catch(Exception e1){}
	}
}
