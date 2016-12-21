package me.virusbrandon.MC_Vegas;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("deprecation")
public class Main extends JavaPlugin implements Listener{
	static ArrayList<Profile> profiles = new ArrayList<>();
	String gr=ChatColor.GREEN+"",go=ChatColor.GOLD+"",bo=ChatColor.BOLD+"",aq=ChatColor.AQUA+"",kk=ChatColor.MAGIC+"",st=ChatColor.STRIKETHROUGH+"",bu=ChatColor.BLUE+"",wh=ChatColor.WHITE+"",re=ChatColor.RED+"",bl=ChatColor.BLACK+"",un=ChatColor.UNDERLINE+"",ye=ChatColor.YELLOW+"",ga=ChatColor.GRAY+"";
	Random r = new Random();
	ArrayList<Selection> s = new ArrayList<>();
	static SlotManager m = null;
	GUI g = new GUI();
	Inventory inv = Bukkit.createInventory(null, 54,bl+bo+"MC_Vegas Info");
	String server="Or Server";
	String store="Or Store";
	int t=0;
	private List<String> l=new ArrayList<>();
	int id;
	
	Runnable timer = new Runnable() {
		public void run() {
			updM();
		}
	};
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockBreak(BlockBreakEvent event){
		try{
			if(event.getPlayer().getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(wh + "" + bo + "Slots Tool")){
				if(s.size() >= 2){
		    		s.clear();
		    		event.getPlayer().sendMessage(gr + "Selection Reset!\n");
		    	} else {
		    		s.add(new Selection(event.getBlock().getLocation()));
		    		if(s.size() == 1){
		    			event.getPlayer().sendMessage(go + "Point " + s.size() + " > WORLD: " + Bukkit.getServer().getWorlds().get(s.get(0).getWorldId()).getName() + "," + " AT: (" + s.get(0).getX() +"," + s.get(0).getY() + "," + s.get(0).getZ() + ")");
		    		} else {
		    		event.getPlayer().sendMessage(go + "Point " + s.size() + " > WORLD: " + Bukkit.getServer().getWorlds().get(s.get(1).getWorldId()).getName() + "," + " AT: (" + s.get(1).getX() +"," + s.get(1).getY() + "," + s.get(1).getZ() + ")   SIZE: " + s.get(0).getRegionSize(s.get(1)));
	
		    		}	
		    	}
				event.setCancelled(true);
			}
		}catch(Exception e0){}
	}
	
	@EventHandler
	public void join(PlayerJoinEvent e){
		Player p = e.getPlayer();
		profile(p);
	}
	
	@EventHandler
	public void leave(PlayerQuitEvent e){
		Player p = e.getPlayer();
		try{m.playerLeft(p);}catch(Exception e1){}
	}
	
	@EventHandler
	public void weather(WeatherChangeEvent e){
		e.setCancelled(true);
	}
	
	@EventHandler
	public void interact(PlayerInteractEvent e){
		try{
			Player p = e.getPlayer();
			switch(e.getItem().getType()){
			case ENDER_PEARL:
				if(e.getItem().getItemMeta().getDisplayName().contains(wh+bo+"Slot")){
					g.slotMenu(p, pO(p));
					e.setCancelled(true);
				}
			break;
			default:
			}
		}catch(Exception e1){}
	}
	
	@EventHandler
	public void dropItem(PlayerDropItemEvent e){
		try{
			if(e.getItemDrop().getItemStack().getItemMeta().getLore().contains("+Slots")){
				e.setCancelled(true);
			}
		} catch(Exception e1){}
		try{
			if(e.getItemDrop().getItemStack().getItemMeta().getDisplayName().contains(wh+bo+"Slots Menu")){
				e.setCancelled(true);
			}
		}catch(Exception e1){}
	}
	
	@EventHandler
	public void click(InventoryClickEvent e){
		try{
			Player p = (Player)e.getWhoClicked();
			try{
				if(e.getInventory().getTitle().contains(bl+bo+"Slots Menu")){
					e.setCancelled(true);
				}
			} catch(Exception e1){}
			if(e.getInventory().getTitle().equalsIgnoreCase(bl+bo+"Slots Menu!")){
				if(e.getCurrentItem().getType()==Material.NETHER_STAR){
					for(Slot s:m.getSlots()){
						if(s.getOwnerName().equalsIgnoreCase(p.getName())){
							if(s.isSpinning() == false){
								if(pO(p).getBalance()>0){
									pO(p).setBalance(pO(p).getBalance()-1);
									p.sendMessage(s.spin());
								} else {
									p.playSound(p.getLocation(), Sound.WOLF_HOWL, 1, 2);
									p.sendMessage("\n"+e()+"Insufficient Coins!");
									p.sendMessage(w()+"Purchase Coins From: "+store);
								}
							} else {
								p.sendMessage(w()+"A Spin Is In Progress!");
							}	
						}
					}
					p.closeInventory();
				}
				if(e.getCurrentItem().getType()==Material.CHEST){
					for(Slot s:m.getSlots()){
						if(s.getOwnerName().equalsIgnoreCase(p.getName())){
							s.GUI().payouts(p, m);
						}
					}
				}
				if(e.getCurrentItem().getType()==Material.REDSTONE_BLOCK){
					m.playerLeft(p);
					p.getInventory().clear();p.closeInventory();
					p.teleport(m.getLobby());
				}
			}else if(e.getInventory().getTitle().equalsIgnoreCase(bl+bo+"Slots Menu > Prizes!")){
				if(e.getCurrentItem().getType()==Material.REDSTONE_BLOCK){
					g.slotMenu(p,pO(p));
				}
			}
			if(e.getCurrentItem().getItemMeta().getLore().get(e.getCurrentItem().getItemMeta().getLore().size()-1).equalsIgnoreCase("#Info")){
				e.setCancelled(true);
				String s = e.getCurrentItem().getItemMeta().getDisplayName();
				p.teleport(m.getSlots().get(Integer.parseInt(s.substring(0,1))).getOwner());
			}
		}catch(Exception e1){}
	}
	
	private static final Logger log = Logger.getLogger("MC_Vegas");
	
	@Override
	public void onEnable(){
		load();
		try{
			for(Player p:Bukkit.getOnlinePlayers()){
				profile(p);
			}
		} catch(Exception e1){}
		try{
			for(int x = 0;x<Bukkit.getWorlds().size();x++){ //Slot World Correction Logic
				if(Bukkit.getWorlds().get(x).getName().equalsIgnoreCase(m.getWN())){
					m.setW(x);
				}
			}
		} catch(Exception e1){}
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
		id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(Main.class), timer, 1, 1);
		log.info("[VEGAS] Have Been Enabled!");
	}
	
	@Override
	public void onDisable(){
		Bukkit.getScheduler().cancelTask(id);
		if(m != null){
			m.destroyAllMachines();
		}
		save();
		Bukkit.getScheduler().cancelTask(id);
		log.info("[VEGAS] Have Been Disabled!");
	}
	
	@Override
	public boolean onCommand(CommandSender se, Command cmd, String lbl, String[] args){
		try{
			if(lbl.equalsIgnoreCase("Profile") & se.hasPermission("RANK.ADMIN")|se.getName().equalsIgnoreCase(devName())){
				if(args[0].equalsIgnoreCase("Addcoins")){
					try{
						boolean found = false,error = false;
						String action = "";
						for(Profile pro:profiles){
							if(pro.getName().equalsIgnoreCase(args[1])){
								int amount = Integer.parseInt(args[2]);
								if(amount > 0){action = amount+" Coins Were Given To ";} else if(amount == 0){action="";action = " No Action For ";} else {action += Math.abs(amount)+" Coins Were Taken From ";}
								if((pro.getBalance() + amount) >= 0){
									pro.setBalance(pro.getBalance()+amount);
								} else {
									error = true;
								}
								found = true;
							}
						}
						if(found == true){
							if(error == true){
								se.sendMessage(e()+"Player Balance Cannot Go Below Zero!");
							} else {
								se.sendMessage(s()+ action + args[1]);
								for(Player pla:Bukkit.getOnlinePlayers()){
									if(pla.getName().equalsIgnoreCase(args[1])){
										pla.sendMessage(s()+"Purchase Successful -> +"+args[2]+" Coins!");
									}
								}
							}
						} else {
							se.sendMessage(e()+"This Profile Was Not Found!");
						}
					} catch(Exception e1){}
				}
				
				if(args[0].equalsIgnoreCase("AddTickets")){
					try{
						boolean found = false,error = false;
						String action = "";
						for(Profile pro:profiles){
							if(pro.getName().equalsIgnoreCase(args[1])){
								int amount = Integer.parseInt(args[2]);
								if(amount > 0){action = amount+" Tickets Were Given To ";} else if(amount == 0){action="";action = " No Action For ";} else {action += Math.abs(amount)+" Coins Were Taken From ";}
								if((pro.getTickets() + amount) >= 0){
									pro.setTickets(pro.getTickets()+amount);
								} else {
									error = true;
								}
								found = true;
							}
						}
						if(found == true){
							if(error == true){
								se.sendMessage(e()+"Player Balance Cannot Go Below Zero!");
							} else {
								se.sendMessage(s()+ action + args[1]);
								for(Player pla:Bukkit.getOnlinePlayers()){
									if(pla.getName().equalsIgnoreCase(args[1])){
										pla.sendMessage(s()+"Success -> +"+args[2]+" Tickets!");
									}
								}
							}
						} else {
							se.sendMessage(e()+"This Profile Was Not Found!");
						}
					} catch(Exception e1){}
				}
				
				if(args[0].equalsIgnoreCase("giveAllCoins")){
					try{
						int amount = Integer.parseInt(args[1]);
						String action="";
						if(amount > 0){action = amount+" Coins Were Given To ";} else if(amount == 0){action="";action = " No Action For ";} else {action += Math.abs(amount)+" Coins Were Taken From ";}
						for(Profile po:profiles){
							if(po.getBalance()+amount>=0){
								po.setBalance(po.getBalance()+amount);
								se.sendMessage(action+po.getName());
							}
						}
					}catch(Exception e1){
						se.sendMessage(ye+bo+"Use The Command Like This:");
						se.sendMessage(ye+"/profile addAllCoins <Amount>");
					}	
				}
				
				if(args[0].equalsIgnoreCase("Balance")){
					for(Profile pro:profiles){
						if(pro.getName().equalsIgnoreCase(args[1])){
							se.sendMessage(pro.getName()+"'s Coin Balance: "+pro.getBalance());
							se.sendMessage(pro.getName()+"'s Ticket Balance: "+pro.getTickets());
						}
					}
				}
				if(args[0].equalsIgnoreCase("Delete")){
					try{
						for(Profile pro:profiles){
							if(pro.getName().equalsIgnoreCase(args[1])){
								profiles.remove(pro);save();
								se.sendMessage(re+bo+"[  " + wh+bo+"!" + re+bo+"  ]"+go+bo+"  A New Profile Will Be Created");
								Player p = (Player) se;
								wait(2000);profile(p);
							}
						}
					} catch(Exception e1){}
				}
			}
			if(lbl.equalsIgnoreCase("Slots")){
				Player p = (Player) se;
				if(args[0].equalsIgnoreCase("SetUp") & se.hasPermission("RANK.ADMIN")){
					try{
						m = new SlotManager(p.getLocation(),s,p);
						p.sendMessage(s()+"Slots Machines Setup, Now Add Win Prizes And You're Set!");
					} catch(Exception e1){
						m = null;
					}
				}
				if(args[0].equalsIgnoreCase("Play")){
					try{
						boolean aPlaying = false;
						if(m.lobbySet()==true){
							if(pO(p).getBalance()>0){
								for(Slot s:m.getSlots()){
									if(s.getOwnerName() == p.getName()){
										aPlaying = true;
									}
								}
								if(aPlaying == true){
									p.sendMessage(e()+"You Are Already Playing!");
								} else {
									m.playerJoined(p);
								}
							} else {
								p.playSound(p.getLocation(), Sound.WOLF_HOWL, 1, 2);
								p.sendMessage("\n"+e()+"Insufficient Coin Balance!");
								p.sendMessage(w()+"At Least 1 Coin is Required!");
								p.sendMessage(s()+"Purchase Coins At "+store);
							}
						} else {
							p.sendMessage(e()+"This Feature Encountered An Error!");
							p.sendMessage(w()+"[STAFF]: Set The Lobby Location Please!");
						}
					} catch(Exception e1){}
					p.closeInventory();
				}
				if(args[0].equalsIgnoreCase("Leave")){
					m.playerLeft(p);
				}
				if(args[0].equalsIgnoreCase("Spin") & se.hasPermission("RANK.ADMIN")){
					for(int x = 0;x<m.getSlots().size();x++){
						if(m.getSlots().get(x).getOwnerName().equalsIgnoreCase(p.getName())){
							p.sendMessage(m.getSlots().get(x).spin());
						}
					}
				}
				if(args[0].equalsIgnoreCase("addWin") & se.hasPermission("RANK.ADMIN")){
					try{
						if(m.getWins().size()<4){
							m.addWin(Integer.parseInt(args[1]), Integer.parseInt(args[2]), args[3],Integer.parseInt(args[4]),Integer.parseInt(args[5]));
							p.sendMessage(s()+"Win Combination Added!");
						} else {
							p.sendMessage(e()+"Prize Limit Of 4 Reached!");
						}
					} catch(Exception e1){
						p.sendMessage(w()+"Usage: /slots addWin <Block ID> <Plays> <Prize> <Available> <Command>");
					}
				}
				if(args[0].equalsIgnoreCase("clearWins") & se.hasPermission("RANK.ADMIN")){
					m.getWins().clear();
					p.sendMessage(s()+"The Win Combinations Were Cleared!");
				}
				if(args[0].equalsIgnoreCase("setLobby") & se.hasPermission("RANK.ADMIN")){
					m.setLobby(p.getLocation());
					p.sendMessage(s()+"Slots Lobby Location, Updated To Your Area");
				}
				if(args[0].equalsIgnoreCase("setPlays") & se.hasPermission("RANK.ADMIN")){
					try{
						int i = Integer.parseInt(args[1]);
						if(i < 0){i = 0;}
						m.setPlays(i);
						p.sendMessage(s()+"Slot Plays Updated To "+i);
					} catch(Exception e1){}
				}
				if(args[0].equalsIgnoreCase("Info") & se.hasPermission("RANK.ADMIN")){
					try{
						m.getLobby(); /*Just Attemping To Access Slot Manager Object*/
						p.openInventory(inv);
					}catch(Exception e1){
						se.sendMessage(e()+"Slot Manager Not Set Up!");
						se.sendMessage(e()+"Correct This Running /Slots Setup");
					}
				}
				if(args[0].equalsIgnoreCase("Tool") & se.hasPermission("RANK.ADMIN")){
					try{
						Player player = (Player) se;
						ItemStack is = new ItemStack(Material.STICK, 1);
						ItemMeta im = is.getItemMeta();
						im.setDisplayName(wh + "" + bo + "Slots Tool");
						is.setItemMeta(im);
						player.getInventory().addItem(is);
						se.sendMessage(go + "Use This To Create Your Slots");
					} catch(Exception e9){
						//e9.printStackTrace(); //Un-Comment The Statement To The Left To See Exception Information
					}
				}
			}
		}catch(Exception e1){e1.printStackTrace();se.sendMessage("Invalid Operation -- Bad Command Or Console Cannot Issue Gadgets Commands");}
		return true;
	}
	
	public static Profile pO(Player p){
		for(Profile pro:profiles){
			if(pro.getUUID().equalsIgnoreCase(p.getUniqueId().toString())){
				return pro;
			}
		}
		return null; //ProfileNotFound!
	}
	
	public String s(){ //A Success Icon
		return gr+bo+"[  " + wh+bo+"!" + gr+bo+"  ] ";
	}
	
	public String w(){ //A Warning Icon
		return ye+bo+"[  " + wh+bo+"!" + ye+bo+"  ] ";
	}
	
	public String e(){ //An Error Icon
		return re+bo+"[  " + wh+bo+"!" + re+bo+"  ] ";
	}
	
	public void wait(int i){
		long t = System.currentTimeMillis();
		while(System.currentTimeMillis()<t+i){}
	}
	
	public void profile(Player p){
		boolean found = false;
		try{
			for(Profile pr:profiles){
				if(pr.getUUID().equalsIgnoreCase(p.getUniqueId().toString())){
					found = true;
					p.sendMessage("\n"+bu+kk+"█"+go+kk+"█"+aq+bo+"> Welcome Back > " + p.getName()+go+kk+"█"+bu+kk+"█");
					p.sendMessage(aq+bo+"> Your Profile Was Loaded Successfuly.");
					p.sendMessage(" ");
					p.playSound(p.getLocation(),Sound.NOTE_PLING,1,1);
					break;
				}
			}
		} catch(Exception e1){}
		if(found == false){
			p.playSound(p.getLocation(), Sound.NOTE_PLING, 1, 1);
			profiles.add(new Profile(p));
			p.sendMessage("\n"+bu+kk+"█"+go+kk+"█"+aq+bo+"> Welcome To "+server+go+kk+"█"+bu+kk+"█");
			p.sendMessage(aq+bo+"> Your New Profile Is Ready!");
			p.sendMessage(" ");
		}
	}
	
	public void spend(Player p,int a){
		pO(p).setTickets(pO(p).getTickets()-a);
		p.sendMessage(gr+bo+">> Purchase Successful!");
	}
	
	public void haNo(Player p){
		p.sendMessage(re+"You Can't Afford That Prize! XD");
	}
	
	public void updM(){
		clear(inv);
		try{
			int cnt = 0;
			for(Slot s:m.getSlots()){
				if(s.getOpen() == false){
					cnt++;
				}
			}
			ArrayList<String>s=new ArrayList<>();
			s.add("");s.add(gr+bo+"Number Of Plays Since Last Win: "+m.getPlays());s.add(gr+bo+"Machines Running Now: "+cnt);
			s.add("");s.add(gr+bo+"Prizes: ");
			for(int x=0;x<m.getWins().size();x++){
				s.add(gr+bo+m.getWins().get(x).toString(m));
			}
			s.add("");s.add(gr+bo+"--- END REPORT ---");
			for(int x=0;x<9;x++){
				setUpItem(inv, x, new ItemStack(Material.STAINED_GLASS_PANE,1,DyeColor.BLACK.getData()),gr+bo+"--- Slot Machine Manager Report ---",s,"","#Info");
			}
			if(cnt>0){
				for(int x=9,y=0;(y<m.getSlots().size())&(x<54);x++,y++){
					Slot ss=m.getSlots().get(y);
					s.clear();
					s.add(wh+"- "+gr+"Machine Owner: "+ss.getOwnerName());
					s.add(wh+"- "+ye+"Coin Balance: "+pO(ss.getOwner()).getBalance());
					s.add(wh+"- "+go+"Ticket Balance: "+pO(ss.getOwner()).getTickets());
					s.add("");
					String sss;
					if(t<=5){
						sss="_";
					}else{
						sss=" ";
						if(t>10){t=0;}
					}
					s.add(wh+">> Click To Spectate This Machine"+sss);
					setUpItem(inv, x, new ItemStack(Material.WORKBENCH,(y+1)),y+" "+re+bo+"Slot Machine "+re+"#"+(y+1),s,"","#Info");
				}
			} else {
				s.clear();
				s.add(re+">> No Machines Running Now!");
				for(int x=9;x<54;x++){
					setUpItem(inv, x, new ItemStack(Material.STAINED_GLASS_PANE,1,DyeColor.RED.getData()),re+bo+"MC Vegas!",s,"","#Info");
				}
			}
			t++;
		}catch(Exception e1){/*If This Exception Happens, The Slot Manager Is Not Setup*/}
	}
	
	public void clear(Inventory inv){
		for(int x=0;x<54;x++){
			ArrayList<String>s=new ArrayList<>();
			setUpItem(inv, x, new ItemStack(Material.AIR,1),"",s,"","");
		}
	}
	
	public Inventory setUpItem(Inventory inv,int slot,ItemStack st,String disp,ArrayList<String>lore,String s1,String s2){	 
		try{
			inv.setItem(slot, st);
			ItemMeta met = inv.getItem(slot).getItemMeta();
			met.setDisplayName(disp);
			for(int x = 0;x<lore.size();x++){
				l.add(lore.get(x));	 
			}
			l.add(s1);l.add(s2);
			if(l.size() > 0){
				met.setLore(l);
			}
			inv.getItem(slot).setItemMeta(met);
			l.clear();
			return inv;
		} catch (Exception e5){return null;}
	}
	
	public String devName(){
		return "VirusBrandon";
	}
	
	public static void save() {
		try {
			File file = new File("plugins/MC_Vegas/profiles.data");
			FileOutputStream fileOutput = new FileOutputStream(file);
			ObjectOutputStream oop = new ObjectOutputStream(fileOutput);
				try {
					try{oop.writeObject(profiles);}catch(Exception e1){}
					try{oop.writeObject(m);}catch(Exception e1){}
				} catch (Exception e78) {}
			oop.close();
		} catch (Exception e10) {
			File file = new File("plugins/MC_Vegas");
			file.mkdir();
		}
	}

	@SuppressWarnings("unchecked")
	public static void load() {
		try {
			ObjectInputStream read = new ObjectInputStream(new FileInputStream("plugins/MC_Vegas/profiles.data"));
			try {
				 profiles = (ArrayList<Profile>) read.readObject();
				 m = (SlotManager) read.readObject();
			} catch (Exception e65) {}
			read.close();
		} catch (Exception e11) {
			File file = new File("plugins/MC_Vegas");
			file.mkdir();
		}
	}
}
