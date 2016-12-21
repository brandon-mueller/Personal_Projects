package me.virusbrandon.Micro_SG;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.InputMismatchException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener{
	ArrayList<Selection> s = new ArrayList<>();
	String gr=ChatColor.GREEN+"",go=ChatColor.GOLD+"",bo=ChatColor.BOLD+"",aq=ChatColor.AQUA+"",kk=ChatColor.MAGIC+"",st=ChatColor.STRIKETHROUGH+"",bu=ChatColor.BLUE+"",wh=ChatColor.WHITE+"",re=ChatColor.RED+"",bl=ChatColor.BLACK+"",un=ChatColor.UNDERLINE+"",ye=ChatColor.YELLOW+"",ga=ChatColor.GRAY+"";
	String[] cmds = new String[]{"SETUP","BUILD","RENAME","SETLOBBY","SETJOIN","DELETE","SETMAX","SETIP","LIST","TOOL","ADDPOD","CLEARPODS","DENYCMD","SHOWDENIEDCMDS","CLEARDENIEDCMDS","STATS","TOGGLE","LOCATE"};
	ArrayList<String> disallowedCMDS = new ArrayList<>();
	ArenaManager m;
	ArrayList<Player> loadQueue = new ArrayList<>();
	Exception InvalidPermissionsException = new Exception();
	Timer t;
	int startTemplateSize = 0;
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerCommand(PlayerCommandPreprocessEvent e) {
		Player p = e.getPlayer();
		if(disallowedCMDS.contains(e.getMessage().toUpperCase())&!p.hasPermission("RANK.ADMIN")){
			p.sendMessage(re+"Insert Comical Error Message Here!");
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void join(PlayerJoinEvent e){
		Player p = e.getPlayer();
		try{
			Bukkit.broadcastMessage(gr+bo+"| Oh Hai | > "+ga+e.getPlayer().getName());
			m.loadQueue(p);
			m.addToRefresh(p);
		} catch(Exception e1){/*Arena Manager Not Initialized*/}
	}
	
	@EventHandler
	public void exit(PlayerQuitEvent e){
		Player p=e.getPlayer();
		try{
			m.checkAndLeave(p);
			if(m.getQueue().contains(p)){
				for(Player pl:m.getQueue()){
					pl.sendMessage(m.w()+">> TRIBUTE, "+p.getName()+" Fled!");
				}
				m.setQueue(m.remove(m.getQueue(), p));
			}
		}catch(Exception e1){/*Arena Manager Not Initialized*/}
		Bukkit.broadcastMessage(re+bo+"| K Bai | > "+ga+p.getName());
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockBreak(BlockBreakEvent event){
		try{
			if(event.getPlayer().getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(wh+""+bo+"SG Tool")){
				if(s.size() >= 2){
		    		s.clear();
		    		event.getPlayer().sendMessage(gr+bo+"Selection Reset!\n");
		    	} else {
		    		s.add(new Selection(event.getBlock().getLocation()));
		    		if(s.size() == 1){
		    			event.getPlayer().sendMessage(gr+bo+"Point "+re+bo+s.size()+gr+bo+" > WORLD: "+Bukkit.getServer().getWorlds().get(s.get(0).getWorldId()).getName()+","+" AT: ("+s.get(0).getX() +","+s.get(0).getY()+","+s.get(0).getZ()+")");
		    		} else {
		    			event.getPlayer().sendMessage(gr+bo+"Point "+re+bo+s.size()+gr+bo+" > WORLD: "+Bukkit.getServer().getWorlds().get(s.get(1).getWorldId()).getName()+","+" AT: ("+s.get(1).getX() +","+s.get(1).getY()+","+s.get(1).getZ() + ")   SIZE: "+s.get(0).getRegionSize(s.get(1)));
		    		}	
		    	}
				event.setCancelled(true);
			}
		}catch(Exception e0){}
	}
	
	@EventHandler
	public void signChange(SignChangeEvent e){
		if(e.getLines()[0].equalsIgnoreCase("JOIN")){
			e.setLine(0, wh+bo+"MICRO SG");
			e.setLine(1, gr+bo+"Join Queue!");
		}
	}
	
	@EventHandler
	public void interact(PlayerInteractEvent e){
		try{
			Player pl = e.getPlayer();
			Sign s;
			if (e.getClickedBlock().getType() == Material.WALL_SIGN) {
				BlockState state = e.getClickedBlock().getState();
				if (state instanceof Sign) {
					s = null;
					s = (Sign) state;
					if(s.getLines()[1].equalsIgnoreCase(gr+bo+"Join Queue!")){
						m.join(pl);
					}
				}
			}
		}catch(Exception e1){}
	}
	
	
	@Override
	public void onEnable(){
		try{
			load();
			Bukkit.getServer().getPluginManager().registerEvents(this, this);
		}catch(Exception e1){setTS(0);}
	}
	
	@Override
	public void onDisable(){
		save();
	}
	
	@Override
	public boolean onCommand(CommandSender se, Command cmd, String lbl, String[] args){
		try{
			if(lbl.equalsIgnoreCase("SG")){ //Base Command
				if(se.hasPermission("RANK.ADMIN") | se.getName().equalsIgnoreCase(devName())){
					int v = 0;
					if(args[0].equalsIgnoreCase("setup")){
						try{m.shutdown();}catch(Exception e1){}
						try{
							Player p = (Player) se;
							m = new ArenaManager(p.getLocation());
							for(Player pl:Bukkit.getOnlinePlayers()){
								m.load(pl);
								m.addToRefresh(pl);
							}
							aST(se,"Arena Manager","Initialized");
						}catch(Exception e1){
							se.sendMessage("\n"+e()+"- CMD USAGE > /SG <Max Players>");
						}
						v = 1;
					}
					if(args[0].equalsIgnoreCase("save")){
						try{
							if(isValid(args[1])==true){
								m.saveTemplate(s, args[1]);
								aST(se,"Arena Template","Created");
								s.clear();
							} else {
								nameFailure(se);
							}
						}catch(Exception e1){
							se.sendMessage("\n"+e()+"- CMD USAGE > /SG Save <Name>");
						}
						v = 1;
					}
					if(args[0].equalsIgnoreCase("build")){
						try{
							if(m.buildArena(Integer.parseInt(args[1])) == 0){
								se.sendMessage("\n"+hT());
								se.sendMessage(sd()+s()+"An Arena Is Loading...");
								se.sendMessage(hB());
							}else{
								se.sendMessage("\n"+hT());
								se.sendMessage(sd()+e()+"Arena Construction Error");
								se.sendMessage(sd()+w()+"Likely Causes:");
								se.sendMessage(sd()+m(0));se.sendMessage(sd()+m(1));se.sendMessage(sd()+m(2));
								se.sendMessage(sd()+w()+"- An Arena Is Already Being Constructed");
								se.sendMessage(sd()+w()+"- CMD USAGE > /SG build <ID>");
								se.sendMessage(hB());
							}
						} catch(Exception e1){
							se.sendMessage("\n"+hT());
							se.sendMessage(sd()+e()+"Arena Construction Error!\n");
							se.sendMessage(sd()+m(0));se.sendMessage(sd()+m(1));se.sendMessage(sd()+m(2));
							se.sendMessage(sd()+w()+"- CMD USAGE > /SG build <ID>");
							se.sendMessage(hB());
						}
						v = 1;
					}
					
					if(args[0].equalsIgnoreCase("reName")){
						try{
							if(isValid(args[2])==true){
								if(m.changeName(Integer.parseInt(args[1]), args[2])==0){
									se.sendMessage("\n"+hT());
									se.sendMessage(sd()+s()+"Arena ReName Successful!");
									se.sendMessage(hB());
								} else {
									se.sendMessage("\n"+hT());
									se.sendMessage(sd()+e()+"Arena ReName Failed!");
									se.sendMessage(sd()+w()+"New Name Too Long");
									se.sendMessage(sd()+s()+"10 Characters Or Less Please");
									se.sendMessage(hB());
								}
							} else {
								nameFailure(se);
							}
						}catch(Exception e1){
							se.sendMessage("\n"+hT());
							se.sendMessage(sd()+e()+"Re-Name Arena Error!\n");
							se.sendMessage(sd()+w()+"Likely Causes:");
							se.sendMessage(sd()+m(0));se.sendMessage(m(1));
							se.sendMessage(sd()+w()+"- CMD USAGE > /SG reName <ID>");
							se.sendMessage(hB());
						}
						v = 1;
					}
					
					if(args[0].equalsIgnoreCase("addPod")){
						try{
							if(m.addSpawnPoint(((Player)se).getLocation(),Integer.parseInt(args[1]))==0){
								se.sendMessage("\n"+hT());
								se.sendMessage(sd()+s()+"Pod Added Successfully!");
								se.sendMessage(hB());
							}else{
								se.sendMessage("\n"+hT());
								se.sendMessage(sd()+e()+"Add Pod Error!\n");
								se.sendMessage(sd()+w()+"Likely Causes:");mAll(se);
								se.sendMessage(sd()+w()+"- Pod Limit Reached");
								se.sendMessage(sd()+w()+"- CMD USAGE > /SG addPod <ID>");
								se.sendMessage(hB());
							}
						}catch(Exception e1){
							se.sendMessage("\n"+hT());
							se.sendMessage(sd()+e()+"Add Pod Error!\n");
							se.sendMessage(sd()+w()+"Likely Causes:");mAll(se);
							se.sendMessage(sd()+w()+"- CMD USAGE > /SG addPod <ID>");
							se.sendMessage(hB());
						}
						v = 1;
					}
					
					if(args[0].equalsIgnoreCase("setLobby")){
						try{
							m.setLobby(((Player)se).getLocation());aST(se,"Lobby Location","Set");
						}catch(Exception e1){
							se.sendMessage("\n"+hT());
							se.sendMessage(sd()+e()+"Set-Lobby Error!\n");
							se.sendMessage(sd()+w()+"Likely Causes:");
							se.sendMessage(sd()+m(0));se.sendMessage(sd()+m(1));se.sendMessage(sd()+m(3));
							se.sendMessage(sd()+e()+"- CMD USAGE > /SG setLobby");
							se.sendMessage(hB());
						}
						v = 1;
					}
					
					if(args[0].equalsIgnoreCase("clearPods")){
						try{
							m.getTemplates().get(Integer.parseInt(args[1])).getSpawnPoints().clear();
							se.sendMessage("\n"+hT());
							se.sendMessage(sd()+s()+"Pods Cleared!");
							se.sendMessage(hB());
						}catch(Exception e1){
							se.sendMessage("\n"+hT());
							se.sendMessage(sd()+e()+"Pod Clearing Error!\n");
							se.sendMessage(sd()+w()+"Likely Causes:");mAll(se);
							se.sendMessage(sd()+e()+"- CMD USAGE > /SG clearPods");
							se.sendMessage(hB());
						}
						v = 1;
					}
					
					if(args[0].equalsIgnoreCase("setJoin")){
						try{
							m.setJoinSpot(((Player)se).getLocation());aST(se,"Join Location","Set");
						}catch(Exception e1){
							se.sendMessage("\n"+hT());
							se.sendMessage(sd()+e()+"Set Join Location Error!\n");
							se.sendMessage(sd()+w()+"Likely Causes:");mAll(se);
							se.sendMessage(sd()+w()+"- CMD USAGE > /SG setJoin");
							se.sendMessage("\n"+hT());
						}
						v = 1;
					}
					
					if(args[0].equalsIgnoreCase("delete")){
						if(m.deleteArena(Integer.parseInt(args[1]))==0){
							aST(se,"Delete Arena","Completed");
						} else {
							se.sendMessage("\n"+hT());
							se.sendMessage(sd()+e()+"Arena Removal Error!");
							se.sendMessage(sd()+w()+"Likely Causes:");
							se.sendMessage(sd()+m(0));
							se.sendMessage(sd()+m(1));
							se.sendMessage(sd()+w()+"- CMD USAGE > /SG Delete <ID>");
							se.sendMessage(hB());
						}
						v = 1;
					}
					
					if(args[0].equalsIgnoreCase("setmax")){
						try{
							if(m.setMaxPlayers(Integer.parseInt(args[1]))==0){
								se.sendMessage("\n"+hT());
								se.sendMessage(sd()+s()+"Max Updated To "+m.getMaxPlayers());
								se.sendMessage(hB());
							} else {
								se.sendMessage("\n"+hT());
								se.sendMessage(sd()+e()+"Set Max Players Error!");
								se.sendMessage(sd()+w()+"Acceptable Range [2 - 24]");
								se.sendMessage(hB());
							}
						}catch(Exception e1){
							se.sendMessage("\n"+e()+"Set Max Players Error!\n");
							se.sendMessage(sd()+w()+"Likely Causes:");
							se.sendMessage(sd()+m(0));se.sendMessage(m(2));
							se.sendMessage(sd()+w()+"- CMD USAGE: /SG setmax <MAX>");
							se.sendMessage(hB());
						}
						v = 1;
					}
					
					if(args[0].equalsIgnoreCase("setIp")){
						try{
							m.setIp(args[1]);
							se.sendMessage("\n"+hT());
							se.sendMessage(sd()+s()+"IP Was Set Successfully!");
							se.sendMessage(hB());
						}catch(Exception e1){
							se.sendMessage("\n"+hT());
							se.sendMessage(sd()+e()+"\nSet IP Error!\n");
							se.sendMessage(sd()+w()+"Usage: /SG SetIp <Your Server's IP>");
							se.sendMessage(hB());
						}
						v = 1;
					}
					
					if(args[0].equalsIgnoreCase("list")){
						try{
							m.listArenas(se);
						}catch(Exception e1){
							se.sendMessage(hT());
							se.sendMessage(sd()+e()+"ArenaManager Has Not Been Setup!");
							se.sendMessage(sd()+w()+"Stand Where You Want The Arenas To Generate");
							se.sendMessage(sd()+s()+"And Run The Command /SG SETUP");
							se.sendMessage(hB());
						}
						v = 1;
					}
					
					if(args[0].equalsIgnoreCase("denycmd")){
						try{
							String s = args[1].toUpperCase();
							if(!disallowedCMDS.contains(s)){
								disallowedCMDS.add(s);
								se.sendMessage("\n"+hT());
								se.sendMessage(sd()+gr+bo+"\nCommand Added To BlackList!");
								se.sendMessage(hB());
							} else {
								disallowedCMDS.remove(s);
								se.sendMessage("\n"+hT());
								se.sendMessage(sd()+re+bo+"\nCommand Removed From BlackList!");
								se.sendMessage(hB());
							}
							v = 1;
						}catch(Exception e1){
							se.sendMessage("\n"+hT());
							se.sendMessage(sd()+e()+"- CMD USAGE: /SG denycmd <CMD>");
							se.sendMessage(hB());
						}
					}
					
					if(args[0].equalsIgnoreCase("showDeniedCmds")){
						se.sendMessage(re+bo+"\nCommand BlackList:");
						se.sendMessage("\n"+hT());
						for(String s:disallowedCMDS){
							se.sendMessage(sd()+wh+bo+">> "+re+s);
						}
						se.sendMessage(hB());
						v = 1;
					}
					
					if(args[0].equalsIgnoreCase("cleardeniedcmds")){
						disallowedCMDS.clear();
						se.sendMessage("\n"+hT());
						se.sendMessage(sd()+gr+bo+"\nOk! Dis-Allowed Commands Cleared!");
						se.sendMessage(hB());
						v = 1;
					}

					if(args[0].equalsIgnoreCase("toggle")){
						try{
							se.sendMessage("\n"+hT());
							se.sendMessage(sd()+m.getTemplates().get(Integer.parseInt(args[1])).toggle());
							se.sendMessage(hB());
						}catch(Exception e1){
							se.sendMessage("\n"+hT());
							se.sendMessage(sd()+e()+"- CMD USAGE: /SG TOGGLE <ID>");
							se.sendMessage(hB());
						}
						v=1;
					}
					
					if(args[0].equalsIgnoreCase("Tool")){
						try{
							Player p = (Player) se;
							ItemStack is = new ItemStack(Material.STICK, 1);
							ItemMeta im = is.getItemMeta();
							im.setDisplayName(wh + "" + bo + "SG Tool");
							is.setItemMeta(im);
							p.getInventory().addItem(is);aW(se);
						} catch(Exception e1){}
						v = 1;
					}
					
					if(args[0].equalsIgnoreCase("Locate")){
						try{
							Player p = (Player)se;
							p.teleport(m.getOrigin());
							p.sendMessage("\n"+hT()+"");
							p.sendMessage(sd()+s()+"Locating Arena Manager Origin Point...");
							p.sendMessage(hB());
						}catch(Exception e1){
							se.sendMessage("\n"+hT());
							se.sendMessage(sd()+e()+"- CMD USAGE: /SG LOCATE");
							se.sendMessage(sd()+e()+"The Arena Manager May Not Exist!");
							se.sendMessage(hB());
						}
						v = 1;
					}
					if(args[0].equalsIgnoreCase("GC")){
						try{
							Player p = (Player)se;
							p.setGameMode(GameMode.CREATIVE);
						}catch(Exception e1){}
					}
					if(args[0].equalsIgnoreCase("Stats")){
						try{
							if(args[1].equalsIgnoreCase("addRevives")){
								try{
									Stats s = m.findStatsByName(args[2]);
									String[] w=new String[]{"Added","To","Taken","From"};
									int toChange=Integer.parseInt(args[3]),a=0;
									if(toChange!=0){
									if(toChange<0){a=2;}
										try{	
											if(s.setElixer(s.getElixers(),toChange)==0){
												try{
													for(Player p:Bukkit.getOnlinePlayers()){
														if(p.getName().equalsIgnoreCase(args[2])){
															p.sendMessage("\n"+hT());
															p.sendMessage(sd()+m.w()+">> "+Math.abs(toChange)+" Revival Elixers Were "+w[a]);
															p.sendMessage(sd()+m.e()+">> "+w[a+1]+" Your Micro SG Pouch!");
															se.sendMessage(hB());
														}
													}
												}catch(Exception e1){}
												se.sendMessage("\n"+hT());
												se.sendMessage(sd()+m.s()+">> Operation Successful!");
												se.sendMessage(hB());
											} else {
												se.sendMessage("\n"+hT());
												se.sendMessage(sd()+m.e()+"Not Enough Elixers To Remove!");
												se.sendMessage(hB());
											}
										}catch(Exception e1){
											se.sendMessage("\n"+hT());
											se.sendMessage(sd()+m.e()+"Couldn't Find Anyone By That Name!");
											se.sendMessage(hB());

										}
									}
								}catch(Exception e1){
									se.sendMessage("\n"+hT());
									se.sendMessage(sd()+m.e()+"Use The Command Like This:");
									se.sendMessage(sd()+m.e()+"/SG stats addRevives <Name> <Qty>");
									se.sendMessage(hB());
								}
								v=2;
							}
							if(args[1].equalsIgnoreCase("addPoints")){
								try{
									Stats s = m.findStatsByName(args[2]);
									String[] w=new String[]{"Added","To","Taken","From"};
									int toChange=Integer.parseInt(args[3]),a=0;
									if(toChange!=0){
										if(toChange<0){a=2;}
										if(s.setPoints(s.getPoints(),toChange,1,true)==0){
											for(Player p:Bukkit.getOnlinePlayers()){
												if(p.getName().equalsIgnoreCase(args[2])){
													se.sendMessage("\n"+hT());
													se.sendMessage(sd()+m.s()+">> Operation Successful!");
													se.sendMessage(hB());
													p.sendMessage("\n"+hT());
													p.sendMessage(sd()+m.w()+">> "+Math.abs(toChange)+" Points Were "+w[a]);
													p.sendMessage(sd()+m.e()+">> "+w[a+1]+" Your Micro SG Account!");
													se.sendMessage(hB());
													m.addToRefresh(p);
												}
											}
										} else {
											se.sendMessage("\n"+hT());
											se.sendMessage(sd()+m.e()+"Point Balance Can't Be Negative!");
											se.sendMessage(hB());
										}
									}
								}catch(Exception e1){
									se.sendMessage("\n"+hT());
									se.sendMessage(sd()+m.e()+"Use The Command Like This:");
									se.sendMessage(sd()+m.e()+"/SG stats addPoints <Name> <Qty>");
									se.sendMessage(hB());
								}
								v=2;
							}
							if(args[1].equalsIgnoreCase("print")){
								try{
									Stats s = m.findStatsByName(args[2]);
									if(s!=null){
										se.sendMessage(s.toString());
									}else{
										se.sendMessage("\n"+hT());
										se.sendMessage(m.e()+"No Stats Were Found For That Name!");
										se.sendMessage(hB());
									}
								}catch(Exception e1){
									se.sendMessage("\n"+hT());
									se.sendMessage(sd()+m.e()+"Use The Command Like This:");
									se.sendMessage(sd()+m.e()+"/SG stats Print <Name>");
									se.sendMessage(hB());
								}
								v=2;
							}
							if(args[1].equalsIgnoreCase("nuke")){
								try{
									if(m.findStatsByName(args[2])!=null){
										m.getStats().remove(m.findStatsByName(args[2]));
										se.sendMessage("\n"+hT());
										se.sendMessage(sd()+m.w()+args[2]+"'s Stats Were Nuked!");
										se.sendMessage(hB());
									} else {
										se.sendMessage("\n"+hT());
										se.sendMessage(sd()+m.e()+"No Stats Were Found For That Name!");
										se.sendMessage(hB());
									}
								}catch(Exception e1){
									se.sendMessage("\n"+hT());
									se.sendMessage(sd()+m.e()+"Use The Command Like This:");
									se.sendMessage(sd()+m.e()+"/SG stats Nuke <Name>");
									se.sendMessage(hB());
								}
								v=2;
							}
							if(v!=2){
								throw new InputMismatchException("");
							}
						}catch(Exception e1){
							se.sendMessage("\n"+hT());
							se.sendMessage(sd()+m.e()+"Available Stats Commands:");
							se.sendMessage(sd()+m.w()+"/SG stats AddRevives <params...>");
							se.sendMessage(sd()+m.w()+"/SG stats AddPoints <params...>");
							se.sendMessage(sd()+m.w()+"/SG stats Print <params...>");	
							se.sendMessage(sd()+m.w()+"/SG stats Nuke <params...>");	
							se.sendMessage(hB());
						}
						v=1;
					}
					if(v == 0){
						se.sendMessage("\n"+e()+"Invalid Command, Available Commands:");
						se.sendMessage(hT());
						for(int x = 0;x<cmds.length;x++){
							se.sendMessage(sd()+wh+bo+"/SG "+cmds[x]+" <Params>");
						}
						se.sendMessage(hB());
					}
				} else {
					se.sendMessage("\n"+hT());
					se.sendMessage(sd()+ye+bo+"/!\\ "+re+bo+"Insert Error Message Here XD");
					se.sendMessage(hB());
				}
			}
		}catch(Exception e1){}
		return true;
	}
	
	public String s(){ //A Success Icon
		return gr+bo+"[  " + wh+bo+"!" + gr+bo+"  ] "+wh+bo;
	}
	
	public String w(){ //A Warning Icon
		return ye+bo+"[  " + wh+bo+"!" + ye+bo+"  ] "+wh+bo;
	}
	
	public String e(){ //An Error Icon
		return re+bo+"[  " + wh+bo+"!" + re+bo+"  ] "+wh+bo;
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
	
	public void mAll(CommandSender se){
		se.sendMessage(sd()+m(0));
		se.sendMessage(sd()+m(1));
		se.sendMessage(sd()+m(2));
		se.sendMessage(sd()+m(3));
	}
	
	public String m(int i){
		switch(i){
		case 0:
			return w()+"- Arena Manager Not Setup";
		case 1:
			return w()+"- The Arena Doesn't Exist";
		case 2:
			return w()+"- Invalid Parameters";
		case 3:
			return w()+"- Cmd Cannot Be Used From Console";
		}
		return "";
	}
	
	public boolean isValid(String name){
		String[] safety=new String[]{"\\","/",":","*","?","<",">"};/*Characters Dis-Allowed By Host OS*/
		for(String s:safety){
			if(name.contains(s)){
				return false;		
			}
		}
		return true;
	}
	
	public void nameFailure(CommandSender se){
		se.sendMessage("\n"+hT());
		se.sendMessage(sd()+e()+"Invalid Arena Name,");
		se.sendMessage(sd()+e()+"Name Contains Forbidden");
		se.sendMessage(sd()+e()+"Characters");
		se.sendMessage(hB());
	}
	
	public void mvChecker(){
		try{
			if(!m.getWN().equalsIgnoreCase(Bukkit.getWorlds().get(m.getOW()).getName())){
				for(int x=0;x<Bukkit.getWorlds().size();x++){
					if(Bukkit.getWorlds().get(x).getName().equalsIgnoreCase(m.getWN())){
						m.setOW(x);
					}
				}
			}
		}catch(Exception e1){}
	}
	
	public void wait(int delay){
		long time=System.currentTimeMillis();
		while(System.currentTimeMillis()<time+delay){
			/*Wait...*/
		}
	}
	
	@SuppressWarnings("unchecked")
	public void load(){
		UpDownMsg("Starting Up...");
		t=new Timer(this,13);t.start(10);
		for(Player p:Bukkit.getOnlinePlayers()){
			p.playSound(p.getLocation(),Sound.CHICKEN_EGG_POP, 2, 2);
		}
		try {
			ObjectInputStream read;
			read = new ObjectInputStream(new FileInputStream("plugins/Micro-SG/arena.data"));
		 	m = (ArenaManager)read.readObject();read.close();
		 	ArrayList<ArrayList<TBlock>> tt = new ArrayList<>();
			read = new ObjectInputStream(new FileInputStream("plugins/Micro-SG/BlockData/block.data"));
			try{
				while(true){
					tt.add((ArrayList<TBlock>)read.readObject());
				}				
			}catch(Exception e1){}
			read.close();
			try{
			 	for(int x = 0;x<tt.size();x++){
			 		ArrayList<SpawnPoint> tsp = new ArrayList<>();
			 		try{tsp = m.getTemplates().get(x).getSpawnPoints();}catch(Exception e1){}
			 		m.getTemplates().set(x, new Arena(null,m,m.getTemplates().get(x).getName(),tt.get(x),m.getTemplates().get(x).getState()));
			 		try{m.getTemplates().get(x).setSpawns(tsp);}catch(Exception e1){}
			 		setTS(getTS()+tt.get(x).size());
			 	}
		 	}catch(Exception e1){}
		 	try{disallowedCMDS=(ArrayList<String>)read.readObject();}catch(Exception e1){}
		read.close();
		} catch (Exception e11) {
			File file = new File("plugins/Micro-SG/BlockData");
			file.mkdirs();
		}
		try{m.init();}catch(Exception e1){}
	}
	
	@SuppressWarnings("static-access")
	public void save(){
		UpDownMsg("Shutting Down...");
		try{m.shutdown();t.stop();t=null;}catch(Exception e1){}
		for(Player p:Bukkit.getOnlinePlayers()){
			p.playSound(p.getLocation(),Sound.CHICKEN_EGG_POP, 2, 2);
			p.closeInventory();
			p.getInventory().clear();
		}
		try {
			File file;FileOutputStream fileOutput;ObjectOutputStream oop;
			int size = 0;
			try{
				for(Arena a:m.getTemplates()){
					size+=a.getArena().listLength(a.getArena());
				}
			}catch(Exception e1){}
			if((size+m.getTemplates().size())!=getTS()){
					try {
						oop = new ObjectOutputStream(new FileOutputStream("plugins/Micro-SG/BlockData/block.data"));
						for(int x = 0;x<m.getTemplates().size();x++){
							BlockNode b = m.getTemplates().get(x).getArena();
							ArrayList<TBlock> blocks = new ArrayList<>();
							while(b!=null){
								blocks.add(b.getTBlock());
								b = b.link;
							}	
							try{oop.writeObject(blocks);}catch(Exception e1){}
							for(int y = 0;y<m.getTemplates().size();y++){
								m.getTemplates().get(x).getArena().link = null;
							}
							blocks.clear();
						}
					} catch (Exception e1) {
						file = new File("plugins/Micro-SG/BlockData");
						file.mkdirs();
					}
			} else {
				for(int y = 0;y<m.getTemplates().size();y++){
					m.getTemplates().get(y).getArena().link = null;
				}
				aS();
			}
			file = new File("plugins/Micro-SG/arena.data");
			fileOutput = new FileOutputStream(file);
			oop = new ObjectOutputStream(fileOutput);
			try{oop.writeObject(m);}catch(Exception e1){}
			try{oop.writeObject(disallowedCMDS);}catch(Exception e1){}
			oop.close();
		} catch (Exception e1) {
			File file = new File("plugins/Micro-SG");
			file.mkdir();
		}
	}
	
	public void UpDownMsg(String message){
		Bukkit.broadcastMessage("\n"+hT());
		Bukkit.broadcastMessage(go+bo+"│ "+e()+"- Micro SG Is "+message);
		if(message.equalsIgnoreCase("Starting Up...")){
			Bukkit.broadcastMessage(go+bo+"│ "+w()+"- Loading Map Templates");
		} else {
			Bukkit.broadcastMessage(go+bo+"│ "+w()+"- Saving Map Templates");
		}
		Bukkit.broadcastMessage(go+bo+"│ "+s()+"- Software v"+JavaPlugin.getProvidingPlugin(Main.class).getDescription().getVersion());
		Bukkit.broadcastMessage(go+bo+"│ "+"");
		Bukkit.broadcastMessage(hB());
	}
	
	public void aS(){
		Bukkit.broadcastMessage("\n"+hT());
		Bukkit.broadcastMessage(sd()+e()+"Arena Template Save");
		Bukkit.broadcastMessage(sd()+s()+"Not Needed"+re+bo+" - "+gr+bo+"Skipping...");
		Bukkit.broadcastMessage(hB());
	}
	
	public void aW(CommandSender se){
		se.sendMessage("\n"+hT());
		se.sendMessage(sd()+w()+gr+bo+"Use This Wand To Define");
		se.sendMessage(sd()+w()+gr+bo+"Arena Templates!");
		se.sendMessage(hB());
	}
	
	public void aST(CommandSender se,String what,String more){
		se.sendMessage("\n"+hT());
		se.sendMessage(sd()+s()+"Micro SG - "+what);
		se.sendMessage(sd()+s()+more+" Successfully");
		se.sendMessage(hB());
	}
	
	public void setTS(int size){
		startTemplateSize = size;
	}
	
	public int getTS(){
		return startTemplateSize;
	}
	
	public String devName(){
		return "VirusBrandon";
	}
}
