package me.virusbrandon.Block_Launcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class Main extends JavaPlugin implements Listener{
	String gr=ChatColor.GREEN+"",go=ChatColor.GOLD+"",bo=ChatColor.BOLD+"",aq=ChatColor.AQUA+"",kk=ChatColor.MAGIC+"",st=ChatColor.STRIKETHROUGH+"",bu=ChatColor.BLUE+"",wh=ChatColor.WHITE+"",re=ChatColor.RED+"",bl=ChatColor.BLACK+"",un=ChatColor.UNDERLINE+"",ye=ChatColor.YELLOW+"",ga=ChatColor.GRAY+"";
	ArrayList<Cooldown> cd=new ArrayList<>();
	ArrayList<Witch> w=new ArrayList<>();
	ArrayList<ItemStack> s = new ArrayList<>();
	Random r = new Random();
	Timer t1,t2;
	
	public void tick1(){
		try{
			for(Cooldown c:cd){
				c.tick();
				if(c.getTime()>=3){
					cd.remove(c);
				}
			}
		}catch(Exception e1){}
	}
			
	public void tick2(){
		try{
			for(Witch wt:w){
				wt.tick();
				if(wt.getTime()>=200){
					w.remove(wt);
				}
			}
		}catch(Exception e1){}
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e){
		Player p = e.getPlayer();
		welcomePlayer(p);
	}
	
	@EventHandler
	public void blockForm(EntityChangeBlockEvent e){
		e.setCancelled(true);
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void interact(PlayerInteractEvent e){
		Player p =e.getPlayer();
		Location l = p.getLocation();
		try{
			switch(e.getItem().getType()){
			case IRON_HOE:
				if(e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("✦ Block-Launcher!!")){
					try{
						Block b;int iter = 0;
						do{
							b = l.getWorld().getBlockAt(-5+(l.getBlockX()+r.nextInt(10)),-5+(l.getBlockY()+r.nextInt(10)),-5+(l.getBlockZ()+r.nextInt(10)));
							iter++;
							if(iter > 100){p.sendMessage(re+"> No Good Block To Throw!");break;}
						}while(b.getType()==Material.AIR);
						if(iter <= 100){
							p.playEffect(p.getLocation(), Effect.MOBSPAWNER_FLAMES, 10);
							throwThisBlock(l,b,p.getLocation().getDirection());
							p.playSound(p.getLocation(),Sound.WITHER_SHOOT,1,2);
							checkHand(p);
						}
					}catch(Exception e1){}
				break;
				}	
			case FIREWORK_CHARGE:
				if(e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("✦ Human-FireWork!!")){
					boolean found=false;
					for(Cooldown c:cd){
						if(c.getPlayer().equals(p)){
							found=true;break;
						}
					}
					if(found==false){
						if(p.getWorld().getBlockAt(new Location(p.getWorld(),p.getLocation().getX(),p.getLocation().getY()+2,p.getLocation().getZ())).getType()==Material.AIR){
							p.playSound(p.getLocation(), Sound.FIREWORK_LARGE_BLAST2, 1, 1);
							for(int x = 0;x<50;x++){
								sendItFlying(new Location(p.getWorld(),p.getLocation().getX(),p.getLocation().getY()+2,p.getLocation().getZ()));
							}
							p.playEffect(p.getLocation(), Effect.MOBSPAWNER_FLAMES, 10);
							cd.add(new Cooldown(p));
							checkHand(p);
						} else {
							p.playSound(p.getLocation(), Sound.NOTE_PLING, 1, .5F);
							p.sendMessage(re+bo+"> Human FireWork > Unsuitable Location!");
						}			
					}else{
						p.sendMessage(re+bo+"Hey! "+ga+"Wait A Few Seconds Before Using That Again!");
					}
				}
			break;	
			case FEATHER:
				if(e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("✦ SpaceBoots!!")){
					p.setVelocity(new Vector(0, 1, 0));
					p.playSound(p.getLocation(), Sound.FIZZ, 1, 1);
					p.playEffect(p.getLocation(), Effect.MOBSPAWNER_FLAMES, 10);
					checkHand(p);
				}
			break;
			case STICK:
				if(e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("✦ Witch's-Broom!!")){
					boolean found=false;
					for(Witch wt:w){
						if(wt.getWitch().equals(p)){
							found=true;
						}
					}
					if(found==false){
						w.add(new Witch(p));
						checkHand(p);
					}
				}

			default:
			}
		}catch(Exception e1){}
	}
	
	@Override
	public void onEnable(){
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
		prepareItems();
		t1 = new Timer(this,0);t2 = new Timer(this,1);
		t1.start(20);t2.start(1);
	}
	
	@Override
	public void onDisable(){
		t1.stop();t1=null;
		t2.stop();t2=null;
	}
	
	@Override
	public boolean onCommand(CommandSender se,Command c, String lbl,String[] args){
		if(lbl.equalsIgnoreCase("Gadgets")){
			if(se instanceof Player){
				Player p = (Player)se;
				try{
					p.getInventory().addItem(s.get(Integer.parseInt(args[0])-1));
				}catch(Exception e1){
					se.sendMessage(re+"- Invalid Gadget, Try Again -");
				}	
			} else {
				se.sendMessage(re+"- Only Players Can Use Gadgets -");
			}
		}
		
		return true;
	}
	
	public void checkHand(Player p){
		if(p.getItemInHand().getAmount() <= 1){
			p.getInventory().remove(p.getItemInHand());
		} else {
			p.getItemInHand().setAmount(p.getItemInHand().getAmount()-1);
		}
	}
	
	@SuppressWarnings("deprecation")
	public void throwThisBlock(Location l,Block b,Vector v){
		FallingBlock fb = l.getWorld().spawnFallingBlock(new Location(l.getWorld(),l.getX(),l.getY()+3,l.getZ()), b.getType(), b.getData());
    	fb.setVelocity(new Vector(v.getX()*2,v.getY()*2,v.getZ()*2));
    	fb.setDropItem(false);
	}
	
	@SuppressWarnings("deprecation")
	public void sendItFlying(Location l){
		Block b = l.getWorld().getBlockAt(l);
		b.setType(Material.WOOL);
		b.setData((byte)r.nextInt(16));
		int n1 = r.nextInt(2);if(n1==0){n1 = -1;}else{n1 = 1;}
		int n2 = r.nextInt(2);if(n2==0){n2 = -1;}else{n2 = 2;}
    	int n3 = r.nextInt(2);if(n3==0){n3 = -1;}else{n3 = 1;}
    	FallingBlock fb = b.getLocation().getWorld().spawnFallingBlock(b.getLocation(), b.getType(), b.getData());
    	fb.setVelocity(new Vector(n1*(.02+(r.nextInt(33)*.03)),n2*(.02+(r.nextInt(33)*.03)),n3*(.02+(r.nextInt(33)*.03))));
    	fb.setDropItem(false);
    	b.setType(Material.AIR);
	}
	
	public void welcomePlayer(Player p){
		p.playSound(p.getLocation(), Sound.NOTE_PLING, 1, 1);
		String[] s = new String[]{aq+bo+"-- "+aq+"Welcome Players, Available Gadgets"+aq+bo+" --",hT(),sd()+gr+"- Block Launcher x 10:"+re+bo+"     /Gadgets 1",sd()+gr+"- Human Firework x5:"+re+bo+"     /Gadgets 2",sd()+gr+"- SpaceBoots x10:"+re+bo+"     /Gadgets 3",sd()+gr+"- Witch's Broom x5"+re+bo+"     /Gadgets 4",hB()};
		for(String ss:s){
			p.sendMessage(ss);
		}
	}
	
	public String hT(){
		return go+bo+"╒═════════════════════╬";
	}
	
	public String hB(){
		return go+bo+"╘═════════════════════╬";
	}
	
	public String sd(){
		return go+bo+"│ ";
	}
	
	public ItemStack giveItem(Material ma,int qty,String name,String ... extraArgs){
		List<String> l = new ArrayList<>();
		ItemStack i = new ItemStack(ma,qty);
		ItemMeta m = i.getItemMeta();
		m.setDisplayName(name);
		for(int x=0;x<extraArgs.length;x++){
			try{l.add(extraArgs[x]);}catch(Exception e1){}
		}
		m.setLore(l);l.clear();
		i.setItemMeta(m);
		return i;
	}
	
	public void prepareItems(){
		s.add(giveItem(Material.IRON_HOE,50,"✦ Block-Launcher!!"));
		s.add(giveItem(Material.FIREWORK_CHARGE,5,"✦ Human-FireWork!!"));
		s.add(giveItem(Material.FEATHER,20,"✦ SpaceBoots!!"));
		s.add(giveItem(Material.STICK,5,"✦ Witch's-Broom!!"));
	}
}
