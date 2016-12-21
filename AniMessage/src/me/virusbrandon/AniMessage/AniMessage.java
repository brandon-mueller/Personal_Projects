package me.virusbrandon.AniMessage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * THIS PLUGIN, ANIMESSAGE IS STRICTLY PROTECTED BY U.S. COPYRIGHT. ANYONE
 * CAUGHT USING THIS SOFTWARE WITHOUT THE EXPRESS WRITTEN CONSENT OF THE AUTHOR(S)
 * WILL BE PURSECUTED TO THE FULLEST EXTENT OF THE LAW.
 * 
 * INFRINGEMENT(S) OF COPYRIGHT IS UNLAWFUL, DON'T DO IT.
 * 
 * ©2014, UNDERLORDPVP.COM
 */

public class AniMessage extends JavaPlugin implements java.io.Serializable, Listener{
	private static final long serialVersionUID = 5674321478129031L;
	private static final Logger log = Logger.getLogger("AniMessage");
	private static ArrayList<Message> messages = new ArrayList<>();
	private static ArrayList<FrameManager> frames = new ArrayList<>();
	private static ArrayList<Parkour> parkours = new ArrayList<>();
	private static ArrayList<Burst> bursts = new ArrayList<>();
	private ArrayList<Timer> mT = new ArrayList<>();
	private ArrayList<Timer> fT = new ArrayList<>();
	private ArrayList<Timer> pT = new ArrayList<>();
	private ArrayList<Timer> bT = new ArrayList<>();
	private ArrayList<Selection> s = new ArrayList<>();
	private boolean v;
	private String go=ChatColor.GOLD+"",gr=ChatColor.GREEN+"",bo=ChatColor.BOLD+"",wh=ChatColor.WHITE+"";
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockBreak(BlockBreakEvent event){
		try{
		if(event.getPlayer().getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(wh + "" + bo + "Sign Tool")){
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
		}catch(Exception e0){
			//e0.printStackTrace(); //Unimportant Exceptions are thrown here.
		}
	}
	
	@EventHandler
	public void blockForm(EntityChangeBlockEvent e){
		if(e.getEntityType()==EntityType.FALLING_BLOCK)
		e.setCancelled(true);
	}

	@Override
	public void onEnable(){
		loadSigns();
		try{	
			for(int x = 0;x<messages.size();x++){
				Message temp = messages.get(x);
				for(int y = 0;y<Bukkit.getServer().getWorlds().size();y++){
					if(Bukkit.getServer().getWorlds().get(y).getName().equalsIgnoreCase(temp.getWorldName())){
						messages.get(x).setWorld(y);
					}
				}
				mT.add(new Timer(messages.get(x),null,null,null));
			}
		} catch(Exception e23){
			System.out.println("[AniMessage] No Signs!");
		}
		try{
		for(int x = 0;x<frames.size();x++){
			FrameManager temp = frames.get(x);
			for(int y = 0;y<Bukkit.getServer().getWorlds().size();y++){
				if(Bukkit.getServer().getWorlds().get(y).getName().equalsIgnoreCase(temp.getWorldName())){
					frames.get(x).setWorld(y);
				}
			}
			fT.add(new Timer(null,frames.get(x),null,null));
			}
		} catch(Exception e24){
			log.info("[AniMessage] No FrameSets!");
		}
		try{
			for(int x = 0;x<parkours.size();x++){
				Parkour temp = parkours.get(x);
				for(int y = 0;y<Bukkit.getServer().getWorlds().size();y++){
					if(Bukkit.getServer().getWorlds().get(y).getName().equalsIgnoreCase(temp.getWorldName())){
						parkours.get(x).setWorld(y);
					}
				}
				pT.add(new Timer(null,null,parkours.get(x),null));
			}
		} catch(Exception e24){
			log.info("[AniMessage] No Parkours");
		}
		
		try{
			for(int x = 0;x<bursts.size();x++){
				Burst temp = bursts.get(x);
				if(temp.getBH()>5){
					temp.setBH(5);
				}
				for(int y = 0;y<Bukkit.getServer().getWorlds().size();y++){
					if(Bukkit.getServer().getWorlds().get(y).getName().equalsIgnoreCase(temp.getWorldName())){
						bursts.get(x).setWorld(y);
					}
				}
				pT.add(new Timer(null,null,null,bursts.get(x)));
			}
		} catch(Exception e24){
			log.info("[AniMessage] No Bursts");
		}
		
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
		log.info("[AniMessage] Has Been Enabled!");
	}
	
	@Override
	public void onDisable(){
		try{
			for(int x = 0;x<messages.size();x++){
				mT.get(x).stopTimer();
			} 
		} catch(Exception e96){}
		try{
			for(int x = 0;x<frames.size();x++){
				fT.get(x).stopTimer();
			}
		} catch(Exception e97){}
		try{
			for(int x = 0;x<parkours.size();x++){
				pT.get(x).stopTimer();
			}
		} catch(Exception e97){}
		try{
			for(int x = 0;x<bursts.size();x++){
				bT.get(x).stopTimer();
			}
		} catch(Exception e97){}
		saveSigns();
		log.info("[AniMessage] Has Been Disabled!");
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender se, Command cmd, String commandlbl, String[] args){
		if(se.hasPermission("AniMessage.use")){
			if(commandlbl.equalsIgnoreCase("Message") & args.length != 0){
				Player p = (Player) se;
				v = false;
				if(args[0].equalsIgnoreCase("create")){ //Have A Sign Already Made For Scrolling
					try{
						if(s.get(0).getWorldId() == s.get(1).getWorldId()){
							se.sendMessage("\nStarting Sign Scroll...");
						messages.add(new Message(s.get(0).getWorldId(),s.get(0).getX(),s.get(0).getY(),s.get(0).getZ(),s.get(1).getX(),s.get(1).getY(),s.get(1).getZ(),10,messages.size(),1,args[1]));
						mT.add(new Timer(messages.get(messages.size()-1),null,null,null));
						s.clear();
						for(int x = 0;x<messages.size()-1;x++){
							if(messages.get(x).getT1() == messages.get(messages.size()-1).getT1()){
								if(messages.get(x).getT2() == messages.get(messages.size()-1).getT2()){
									if(messages.get(x).getT3() == messages.get(messages.size()-1).getT3()){
										if(messages.get(x).getT4() == messages.get(messages.size()-1).getT4()){
											if(messages.get(x).getT5() == messages.get(messages.size()-1).getT5()){
												if(messages.get(x).getT6() == messages.get(messages.size()-1).getT6()){
													if(messages.get(x).getWorld() == messages.get(messages.size()-1).getWorld()){
														se.sendMessage(ChatColor.RED + "WARNING: DELETED DUPLICATE SIGN!");
														messages.get(messages.size()-1).stopTimer();
														messages.remove(messages.size()-1);
													}
												}
											}
										}
									}
								}
							}
						}
						} else {
							signHelp(se);
						}
					} catch (Exception e1){
						se.sendMessage(go + "\nUse The Command Like This:");
						se.sendMessage(gr + "First Do /Message Tool To Get The Selection Tool");
						se.sendMessage(gr + ">>> First Two Clicks Selects A Region");
						se.sendMessage(gr + ">>> Third Click Clears The Region");
						se.sendMessage(gr + "");
						se.sendMessage(gr + "Then Do /Message Create <Name> To Create Your Sign!");
						se.sendMessage(gr + ">>Default Sign Tick Delay: 10 ticks (.5 Seconds)");
					}
					v = true;
				}
				if(s.size()>0){
					s.clear(); //Prevents You From Going On To Other Commands And Forgetting About Stored Values.
							   //We Don't Want Any Unwanted Block Translations, Now Do We?
				}
				if(args[0].equalsIgnoreCase("delete") | args[0].equalsIgnoreCase("remove")){
					try{
						int del = Integer.parseInt(args[1]);
						mT.get(del).stopTimer();
						mT.remove(del);
						if(messages.get(del).getState()==1)
						messages.get(del).toggle();
						messages.remove(del);
						se.sendMessage(ChatColor. GREEN + "\nSign #" + Integer.parseInt(args[1]) + " Removed!");
					} catch (Exception e2){
						se.sendMessage(go + "\nType The Command Like This:");
						se.sendMessage(gr + "/Message delete <ID>");
					}
					v = true;
				}
			
				if(args[0].equalsIgnoreCase("setdelay")){
					try{
						int t =  Integer.parseInt(args[1]);
						int t1 = messages.get(t).getDelay();
						messages.get(t).setDelay(Math.abs(Integer.parseInt(args[2])));
						int t2 = messages.get(t).getDelay();
						mT.get(t).stopTimer();
						mT.set(t, new Timer(messages.get(t),null,null,null));
						se.sendMessage(gr + "Change Timer From " + t1 + "tick(s) To " + t2 + "tick(s)");
						
					} catch(Exception e3){
						se.sendMessage(go + "\nType The Command Like This:");
						se.sendMessage(gr + "/Message setDelay <ID> <Delay>");
					}
					v = true;
				}
				
				if(args[0].equalsIgnoreCase("toggle")){
					try{
						se.sendMessage(messages.get(Integer.parseInt(args[1])).toggle());
					} catch(Exception e4){
						se.sendMessage(go + "\nType The Command Like This:");
						se.sendMessage(gr + "/Message toggle <ID>");
					}
					v = true;
				}
				
				if(args[0].equalsIgnoreCase("switch")){
					try{
						messages.get(Integer.parseInt(args[1])).changeDirection();
						se.sendMessage("Scroll Direction Reversed!");
					} catch(Exception e5){
						se.sendMessage(go + "\nType The Command Like This:");
						se.sendMessage(gr + "/Message switch <ID>");
					}
					v = true;
				}
				
				if(args[0].equalsIgnoreCase("locate")){
					try{
						Message temp = messages.get(Integer.parseInt(args[1]));
						se.sendMessage(go + "\nSign #" + temp.getID() + " Is Located At:");
						se.sendMessage(gr + "World: " + Bukkit.getServer().getWorlds().get(temp.getWorld()).getName());
						se.sendMessage(gr + temp.getCoords());
						if(se instanceof Player){
							Player pl = (Player) se;
							pl.teleport(new Location(Bukkit.getServer().getWorlds().get(temp.getWorld()),temp.getT1(),temp.getT2(),temp.getT3()));
						}
						
					} catch(Exception e6){
						se.sendMessage(go + "\nType The Command Like This:");
						se.sendMessage(gr + "/Message locate <ID>");
					}
					v = true;
				}
				
				if(args[0].equalsIgnoreCase("rename")){
					try{
						se.sendMessage(messages.get(Integer.parseInt(args[1])).setName(args[2]));
					} catch(Exception e7) {
						se.sendMessage(go + "\nType The Command Like This:");
						se.sendMessage(gr + "/Message rename <ID> <NewName>");
					}
					v = true;
				}
				
				if(args[0].equalsIgnoreCase("list")){
					try{
						if(messages.size() > 0){
							int temp =  Integer.parseInt(args[1]);
							if(temp <= ((messages.size()/9)+1) & temp > 0){
								se.sendMessage(go + "\nSign List Page: " + temp + " OF " + ((messages.size()/9)+1));
								for(int x = (9*(temp-1));x<(temp * 9);x++){
									if(x >= messages.size())
										break;
									se.sendMessage(gr + "ID: " + x + " " + messages.get(x).toString());
								}
							} else {
								se.sendMessage(go + "\nType The Command Like This:");
								se.sendMessage(gr + "/Message list <Page #> /" + ((messages.size()/10)+1));
							}
						} else {
							se.sendMessage(ChatColor.RED + "No Signs Exist!");
						}
					} catch(Exception e8){
						se.sendMessage(go + "\nType The Command Like This:");
						se.sendMessage(gr + "/Message list <Page #> /" + ((messages.size()/10)+1));
					}
					v = true;
				}
				
				if(args[0].equalsIgnoreCase("tool")){
					try{
						Player player = (Player) se;
						ItemStack is = new ItemStack(Material.STICK, 1);
						ItemMeta im = is.getItemMeta();
						im.setDisplayName(wh + "" + bo + "Sign Tool");
						is.setItemMeta(im);
						player.getInventory().addItem(is);
						se.sendMessage(go + "Use This To Create Your Signs!");
					} catch(Exception e9){
						//e9.printStackTrace(); //Un-Comment The Statement To The Left To See Exception Information
					}
					v = true;
				}
				if(args[0].equalsIgnoreCase("setWorld")){
					try{
						int x = Integer.parseInt(args[1]);
						if(args[2].equalsIgnoreCase("this")){args[2]=p.getWorld().getName();}
						messages.get(x).setWorldName(args[2]);
						se.sendMessage(gr+bo+"\n> Set World > OK! Changed To: " + args[2]);
						se.sendMessage(go+bo+"> /!\\ > Restart AniMessage To See The Changes!");
					}catch(Exception e3){
						se.sendMessage(go + "\nUse The Command Like This:");
						se.sendMessage(go + "/Message setWorld <ID> <WorldName> OR <THIS>");
					}
					v = true;
				}
				if(args[0].equalsIgnoreCase("help") | args[0].equalsIgnoreCase("info")){
					signHelp(se);
					v = true;
				}
				
				if(v == false){
					se.sendMessage(ChatColor.RED + "\nInvalid Sub-Command: " + args[0] + "\n");
					signHelp(se);
				}
			} 
			if(commandlbl.equalsIgnoreCase("Frame") & args.length != 0){
				Player p = (Player) se;
				v = false;
				if(args[0].equalsIgnoreCase("create")){
					try{
						frames.add(new FrameManager(s.get(0).getWorldId(),s.get(0).getX(),s.get(0).getY(),s.get(0).getZ(),s.get(1).getX(),s.get(1).getY(),s.get(1).getZ(),10,frames.size(),args[1]));
						fT.add(new Timer(null,frames.get(frames.size()-1),null,null));
						se.sendMessage(gr + "\nA New Frame Reel Was Created!");
						se.sendMessage(gr + "Add Frames And Toggle On With /Frame Toggle");
						s.clear();
					} catch(Exception e13){
						se.sendMessage(go + "\nUse The Command Like This:");
						se.sendMessage(gr + "First Do /Message Tool To Get The Selection Tool");
						se.sendMessage(gr + ">>> Must Be In GameMode Survival");
						se.sendMessage(gr + ">>> First Two Clicks Selects A Region");
						se.sendMessage(gr + ">>> Third Click Clears The Region");
						se.sendMessage(gr + "");
						se.sendMessage(gr + "Then Do /Frame Create <Name> To Create Your Sign!");
						se.sendMessage(gr + ">>Default Sign Tick Delay: 10ticks (1/2 Second)");
					}
					v = true;
				}
				
				if(args[0].equalsIgnoreCase("add")){
					try{
						se.sendMessage(frames.get(Integer.parseInt(args[1])).addFrame());
					} catch(Exception e14){
						se.sendMessage(go + "\nType The Command Like This:");
						se.sendMessage(gr + "/Frame add <ID>");
					}
					v = true;
				}
				
				if(args[0].equalsIgnoreCase("delete") | args[0].equalsIgnoreCase("remove")){
					try{
						int t = Integer.parseInt(args[1]);
						if(frames.get(t).getState()==0)
						frames.get(t).toggle();
						fT.get(t).stopTimer();
						frames.remove(t);
						se.sendMessage(gr + "\nFrameReel #" + Integer.parseInt(args[1]) + " Removed!");
					} catch(Exception e14){
						se.sendMessage(go + "\nType The Command Like This:");
						se.sendMessage(gr + "/Frame delete <ID>");
					}
					v = true;
				}
				
				if(args[0].equalsIgnoreCase("setdelay")){
					try{
						int t =  Integer.parseInt(args[1]);
						int t1 = frames.get(t).getDelay();
						frames.get(t).setDelay(Math.abs(Integer.parseInt(args[2])));
						int t2 = frames.get(t).getDelay();
						fT.get(t).stopTimer();
						fT.set(t, new Timer(null,frames.get(t),null,null));
						se.sendMessage(gr + "Change Timer From " + t1 + "tick(s) To " + t2 + "tick(s)");
					} catch(Exception e3){
						se.sendMessage(go + "\nType The Command Like This:");
						se.sendMessage(gr + "/Frame setDelay <ID> <Delay>");
					}
					v = true;
				}
				
				if(args[0].equalsIgnoreCase("toggle")){
					try{
						se.sendMessage(frames.get(Integer.parseInt(args[1])).toggle());
					} catch(Exception e4){
						se.sendMessage(go + "\nType The Command Like This:");
						se.sendMessage(gr + "/Frame toggle <ID>");
					}
					v = true;
				}
				
				if(args[0].equalsIgnoreCase("locate")){
					try{
						FrameManager temp = frames.get(Integer.parseInt(args[1]));
						se.sendMessage(go + "\nFrameReel #" + temp.getID() + " Is Located At:");
						se.sendMessage(gr + "World: " + Bukkit.getServer().getWorlds().get(temp.getWorld()).getName());
						se.sendMessage(gr + temp.getCoords());
						if(se instanceof Player){
							Player pl = (Player) se;
							pl.teleport(new Location(Bukkit.getServer().getWorlds().get(temp.getWorld()),temp.getT1(),temp.getT2(),temp.getT3()));
						}
					} catch(Exception e6){
						se.sendMessage(go + "\nType The Command Like This:");
						se.sendMessage(gr + "/Frame locate <ID>");
					}
					v = true;
				}
				
				if(args[0].equalsIgnoreCase("rename")){
					try{
						se.sendMessage(frames.get(Integer.parseInt(args[1])).setName(args[2]));
					} catch(Exception e7) {
						se.sendMessage(go + "\nType The Command Like This:");
						se.sendMessage(gr + "/Frame rename <ID> <NewName>");
					}
					v = true;
				}
				
				if(args[0].equalsIgnoreCase("setWorld")){
					try{
						int x = Integer.parseInt(args[1]);
						if(args[2].equalsIgnoreCase("this")){args[2]=p.getWorld().getName();}
						frames.get(x).setWorldName(args[2]);
						se.sendMessage(gr+bo+"\n> Set World > OK! Changed To: " + args[2]);
						se.sendMessage(go+bo+"> /!\\ > Restart AniMessage To See The Changes!");
					}catch(Exception e3){
						se.sendMessage(go + "\nUse The Command Like This:");
						se.sendMessage(go + "/Frame setWorld <ID> <WorldName> OR <THIS>");
					}
					v = true;
				}
				
				if(args[0].equalsIgnoreCase("list")){
					try{
						if(frames.size() > 0){
							int temp =  Integer.parseInt(args[1]);
							if(temp <= ((frames.size()/9)+1) & temp > 0){
								se.sendMessage(go + "\nSign List Page: " + temp + " OF " + ((frames.size()/9)+1));
								for(int x = (9*(temp-1));x<(temp * 9);x++){
									if(x >= frames.size())
										break;
									se.sendMessage(gr + "ID: " + x + " " + frames.get(x).toString());
								}
							} else {
								se.sendMessage(go + "\nType The Command Like This:");
								se.sendMessage(gr + "/Frame list <Page #> /" + ((frames.size()/10)+1));
							}
						} else {
							se.sendMessage(ChatColor.RED + "No FrameReels Exist!");
						}
					} catch(Exception e8){
						se.sendMessage(go + "\nType The Command Like This:");
						se.sendMessage(gr + "/Frame list <Page #> /" + ((frames.size()/10)+1));
					}
					v = true;
				}
				
				if(args[0].equalsIgnoreCase("help") | args[0].equalsIgnoreCase("info")){
					frameHelp(se);
					v = true;
				}
				if(v == false){
					se.sendMessage(ChatColor.RED + "\nInvalid Sub-Command: " + args[0] + "\n");
					signHelp(se);
				}
			} 
			if(commandlbl.equalsIgnoreCase("Pkr") & args.length != 0){
				boolean v = false;
				try{
					Player p = (Player)se;
					if(args[0].equalsIgnoreCase("create")){
						try{
						parkours.add(new Parkour(p.getWorld()));
						pT.add(new Timer(null,null,parkours.get(parkours.size()-1),null));
						se.sendMessage(gr+bo+"\n> Pkr > Course Created!");
						} catch(Exception e1){
							se.sendMessage(go+bo+"> Use The Command Like This:\n> /Pkr Create");
						}
						v = true;
					}
					
					if(args[0].equalsIgnoreCase("addBlock")){
						try{
							int x = Integer.parseInt(args[1]);
							int y = Integer.parseInt(args[2]);
							parkours.get(x).S().get(y).addBlock(s.get(0));
							se.sendMessage("\n> Pkr > Block Added To Segment!");
							s.clear();
						} catch(Exception e1){
							se.sendMessage(go+bo+"> Use The Command Like This:\n> /Pkr addBlock <Course ID> <Segment ID>\n> \n> Must Create A Segment Before Using\n> This Command If No Courses Or Segments Exist!");
						}
						v = true;
					}
					
					if(args[0].equalsIgnoreCase("addSeg")){
						try{
							int x = Integer.parseInt(args[1]);
							parkours.get(x).addSeg();
							se.sendMessage("\n> Pkr > Segment Added To Parkour Course!");
						} catch(Exception e1){
							se.sendMessage(go+bo+"> Use The Command Like This:\n> /Pkr addSeg <Course ID>\n> \n> Must Create A Segment Before Using\n> This Command If No Courses Or Segments Exist!");
						}
						v = true;
					}
					
					if(args[0].equalsIgnoreCase("setDelay")){
						try{
							int x = Integer.parseInt(args[1]);
							int y = Integer.parseInt(args[2]);
							parkours.get(x).setDelay(y);
							se.sendMessage("\n> Pkr > Parkour Translation Delay Set To " + y);
						} catch(Exception e1){
							se.sendMessage(go+bo+"\n> Use The Command Like This:\n> /Pkr setDelay <Course ID> <Delay>\n> \n> Adjusts The Speed Of The Course!");
						}
						v = true;
					}
					
					if(args[0].equalsIgnoreCase("Info")){
						try{
							int x = Integer.parseInt(args[1]);
							se.sendMessage("\n> General Parkour Info\n> Courses: " + parkours.size());
							se.sendMessage("\n> This Parkour Course: ID " + x+"\n> Segments: " + parkours.get(x).S().size()+"\n> Speed: " + parkours.get(x).getDelay()+" Cycles");
							try{
								int y = Integer.parseInt(args[2]);
								se.sendMessage("\n> Focused Segment Info:\n> Block Count: " + parkours.get(x).S().get(y).getBlocks().size());
							}catch(Exception e6){}
						} catch(Exception e5){
							se.sendMessage(go+bo+"\n> Use The Command Like This:\n> /Pkr Info <Parkour ID> <Optional Segment ID>");
						}
						v = true;
					}
					
					if(args[0].equalsIgnoreCase("delete")){
						try{
							int x = Integer.parseInt(args[1]);
							pT.get(x).stopTimer();pT.remove(x);
							parkours.remove(x);
							se.sendMessage(gr+bo+"\n> Pkr > Course Deleted!");
						} catch(Exception e2){
							se.sendMessage(go+bo+"> Use The Command Like This:\n> /Pkr Delete <ID>");
						}
						v = true;
					}
					
					if(args[0].equalsIgnoreCase("clear")){
						try{
							int x = Integer.parseInt(args[1]);
							int y = Integer.parseInt(args[2]);
							parkours.get(x).S().get(y).getBlocks().clear();
							parkours.get(x).S().get(y).getTBlocks().clear();
							se.sendMessage(gr+bo+"\n> Pkr > Blocks Cleared!");
						} catch(Exception e2){
							se.sendMessage(go+bo+"> Use The Command Like This:\n> /Pkr Clear <PKR ID> <SEG ID> ");
						}
						v = true;
					}
					
					if(args[0].equalsIgnoreCase("toggle")){
						try{
							int x = Integer.parseInt(args[1]);
							se.sendMessage(parkours.get(x).toggle());
						} catch(Exception e3){
							se.sendMessage(go+bo+"\n> Use The Command Like This:\n> /Pkr Toggle\n> Toggles Parkour Course Movement");
						}
						v = true;
					}
					
					if(args[0].equalsIgnoreCase("setTrail")){
						try{
							int x = Integer.parseInt(args[1]);
							int y = Integer.parseInt(args[2]);
							int z = Integer.parseInt(args[3]);
							se.sendMessage(parkours.get(x).S().get(y).setTb(z));
						} catch(Exception e3){
							se.sendMessage(go+bo+"\n> Use The Command Like This:\n> /Pkr SetTrail <PKR ID> <SEG ID> <BLOCK>");
						}
						v = true;
					}
					
					if(args[0].equalsIgnoreCase("setWorld")){
						try{
							int x = Integer.parseInt(args[1]);
							if(args[2].equalsIgnoreCase("this")){args[2]=p.getWorld().getName();}
							parkours.get(x).setWorldName(args[2]);
							se.sendMessage(gr+bo+"> Set World > OK! Changed To: " + args[2]);
							se.sendMessage(go+bo+"> /!\\ > Restart AniMessage To See The Changes!");
						}catch(Exception e3){
							se.sendMessage(go + "\nUse The Command Like This:");
							se.sendMessage(go + "/Pkr setWorld <ID> <WorldName> OR <THIS>");
						}
						v = true;
					}
					
					if(args[0].equalsIgnoreCase("list")){
						try{
							if(parkours.size() > 0){
								int temp =  Integer.parseInt(args[1]);
								if(temp <= ((parkours.size()/9)+1) & temp > 0){
									se.sendMessage(go + "\nList Page: " + temp + " OF " + ((parkours.size()/9)+1));
									for(int x = (9*(temp-1));x<(temp * 9);x++){
										if(x >= parkours.size())
											break;
										se.sendMessage(gr + "ID: " + x + " " + parkours.get(x).toString());
									}
								} else {
									se.sendMessage(go + "\nType The Command Like This:");
									se.sendMessage(gr + "/pkr list <Page #> /" + ((parkours.size()/10)+1));
								}
							} else {
								se.sendMessage(ChatColor.RED + "No Parkours Exist!");
							}
						} catch(Exception e8){
							se.sendMessage(go + "\nType The Command Like This:");
							se.sendMessage(gr + "/pkr list <Page #> /" + ((parkours.size()/10)+1));
						}
						v = true;
					}
					
					
					if(args[0].equalsIgnoreCase("locate")){
						try{
 							int x = Integer.parseInt(args[1]);
							int y = Integer.parseInt(args[2]);
							p.teleport(parkours.get(x).L(y));
							p.sendMessage("> Pkr > Brought To Course #" + x + ", Segment #" + y);
						} catch(Exception e3){
							se.sendMessage(go+bo+"\n> Use The Command Like This:\n> /Pkr Locate <PKR ID> <SEG ID>\n> Takes You To The Focused Parkour Segment");
						}
						v = true;
					}
				} catch(Exception e5){e5.printStackTrace();}
				if(v == false){
					se.sendMessage(ChatColor.RED + "\nInvalid Sub-Command: " + args[0] + "\n");
					parkourHelp(se);
				}
			}	
			if(commandlbl.equalsIgnoreCase("Burst") & args.length != 0){
				boolean v = false;
				Player p = (Player)se;
				if(args[0].equalsIgnoreCase("create")){
					try{
						int[] blks = new int[args.length-1];
						for(int x = 1;x<args.length;x++){
							blks[x-1] = (Integer.parseInt(args[x]));
							if(new ItemStack(blks[x-x]).getType().isBlock()){} else {
								throw new IllegalArgumentException("ID Must Represent A Block!");
							}
						}
						bursts.add(new Burst(s.get(0),blks));
						bT.add(new Timer(null,null,null,bursts.get(bursts.size()-1)));
						s.clear();
					} catch(Exception e3){
						e3.printStackTrace();
						se.sendMessage(go+bo+"\n> Use The Command Like This:\n> /Burst Create Blocks...\n> Must Use Blocks!");
					}
					v = true;
				}
				if(args[0].equalsIgnoreCase("setDelay")){
					try{
						int x = Integer.parseInt(args[1]);
						int y = Integer.parseInt(args[2]);
						se.sendMessage("\n> Burst > "+bursts.get(x).setDelay(y));
					} catch(Exception e1){
						se.sendMessage(go+bo+"\n> Use The Command Like This:\n> /Burst setDelay <Burst ID> <Delay>");
					}
					v = true;
				}
				if(args[0].equalsIgnoreCase("Toggle")){
					try{
						int x = Integer.parseInt(args[1]);
						se.sendMessage("> State " + bursts.get(x).toggle());
					} catch(Exception e1){
						se.sendMessage(go+bo+"\n> Use The Command Like This:\n> /Burst Toggle <Burst ID>");
					}
					v = true;
				}
				if(args[0].equalsIgnoreCase("SetBH")){
					try{
						int x = Integer.parseInt(args[1]);
						float y = Float.parseFloat(args[2]);
						if(y <= 5){
							se.sendMessage("\n> Burst > "+bursts.get(x).setBH(y));
						} else {
							se.sendMessage(wh+bo+"> ERROR > Burst Height Can't Exceed 5!");
						}
					} catch(Exception e1){
						se.sendMessage(go+bo+"\n> Use The Command Like This:\n> /Burst setBH <Burst ID> <Delay>");
					}
					v = true;
				}
				if(args[0].equalsIgnoreCase("Delete")){
					try{
						int x = Integer.parseInt(args[1]);
						try{bT.get(x).stopTimer();}catch(Exception e2){}
						try{bT.remove(x);}catch(Exception e3){}
						try{bursts.remove(x);}catch(Exception e1){}
						se.sendMessage("> Burst > Deleted!");
						getServer().getPluginManager().disablePlugin(this);
				     	getServer().getPluginManager().enablePlugin(this);
					} catch(Exception e1){
						se.sendMessage(go+bo+"\n> Use The Command Like This:\n> /Burst Delete <ID>");
					}
					v = true;
				}
				
				if(args[0].equalsIgnoreCase("setWorld")){
					try{
						int x = Integer.parseInt(args[1]);
						if(args[2].equalsIgnoreCase("this")){args[2]=p.getWorld().getName();}
						bursts.get(x).setWorldName(args[2]);
						se.sendMessage(gr+bo+"> Set World > OK! Changed To: " + args[2]);
						se.sendMessage(go+bo+"> /!\\ > Restart AniMessage To See The Changes!");
					}catch(Exception e3){
						se.sendMessage(go + "\nUse The Command Like This:");
						se.sendMessage(go + "/Burst setWorld <ID> <WorldName> OR <THIS>");
					}
					v = true;
				}
				
				if(args[0].equalsIgnoreCase("list")){
					try{
						if(bursts.size() > 0){
							int temp =  Integer.parseInt(args[1]);
							if(temp <= ((bursts.size()/9)+1) & temp > 0){
								se.sendMessage(go + "\nSign List Page: " + temp + " OF " + ((bursts.size()/9)+1));
								for(int x = (9*(temp-1));x<(temp * 9);x++){
									if(x >= bursts.size())
										break;
									se.sendMessage(gr + "ID: " + x + " " + bursts.get(x).toString());
								}
							} else {
								se.sendMessage(go + "\nType The Command Like This:");
								se.sendMessage(gr + "/Burst list <Page #> /" + ((bursts.size()/10)+1));
							}
						} else {
							se.sendMessage(ChatColor.RED + "No Bursts Exist!");
						}
					} catch(Exception e8){
						se.sendMessage(go + "\nType The Command Like This:");
						se.sendMessage(gr + "/Burst list <Page #> /" + ((bursts.size()/10)+1));
					}
					v = true;
				}
				
				if(args[0].equalsIgnoreCase("Locate")){
					try{
						int x = Integer.parseInt(args[1]);
						p.teleport(bursts.get(x).L());
						se.sendMessage("> Burst >  Brought To Burst #"+ x);
					} catch(Exception e1){
						se.sendMessage(go+bo+"\n> Use The Command Like This:\n> /Burst Locate <ID>");
					}
					v = true;
				}
				if(v == false){burstHelp(se);}
			}
		} else { //Inadequate Permissions!
			se.sendMessage(ChatColor.RED + "ACCESS DENIED!");
		}
		return true;
	} //END onCommand() METHOD
	
	public void signHelp(CommandSender se){
		String b = "Message";
		se.sendMessage(go + "\nScrolling Message Commands:");
		se.sendMessage(gr + "/"+b+" Create >>> Create A Scrolling Sign");
		se.sendMessage(gr + "/"+b+" Delete >>> Disable And Remove A Message");
		se.sendMessage(gr + "/"+b+" SetDelay >>> Change Scroll Timing");
		se.sendMessage(gr + "/"+b+" Toggle >>> Stop / Start Sign Scroll");
		se.sendMessage(gr + "/"+b+" Switch >>> Change Scroll Direction");
		se.sendMessage(gr + "/"+b+" Rename >>> Rename A Sign");
		se.sendMessage(gr + "/"+b+" SetWorld >>> Assign Different World");
		se.sendMessage(gr + "/"+b+" List >>> List Signs");
	}
	
	public void frameHelp(CommandSender se){
		String b = "Frame";
		se.sendMessage(go + "\nSwapping Frame Commands:");
		se.sendMessage(gr + "/"+b+" Create >>> Create A Scrolling Sign");
		se.sendMessage(gr + "/"+b+" Delete >>> Disable And Remove A FrameSet");
		se.sendMessage(gr + "/"+b+" SetDelay >>> Change Scroll Timing");
		se.sendMessage(gr + "/"+b+" Toggle >>> Stop / Start Sign Scroll");
		se.sendMessage(gr + "/"+b+" Rename >>> Rename A Sign");
		se.sendMessage(gr + "/"+b+" SetWorld >>> Assign Different World");
		se.sendMessage(gr + "/"+b+" List >>> List Frames");
	}
	
	public void parkourHelp(CommandSender se){
		String b = "Pkr";
		se.sendMessage(go + "\nParkour Commands:");
		se.sendMessage(gr + "/"+b+" Create >>> Create A Parkour Course");
		se.sendMessage(gr + "/"+b+" addSeg >>> Add New Segment To Parkour Course");
		se.sendMessage(gr + "/"+b+" Clear >>> Clears The Blocks From A Segment");
		se.sendMessage(gr + "/"+b+" Delete >>> Disable And Remove Parkour Course");
		se.sendMessage(gr + "/"+b+" SetDelay >>> Change Segment Translation Timing");
		se.sendMessage(gr + "/"+b+" SetTrail >>> Change Block Trail");
		se.sendMessage(gr + "/"+b+" Toggle >>> Stop / Start Parkour Movement");
	}
	
	public void burstHelp(CommandSender se){
		String b = "Burst";
		se.sendMessage(go + "\nBurst Commands:");
		se.sendMessage(gr + "/"+b+" Create >>> Create A Block Burst");
		se.sendMessage(gr + "/"+b+" Delete >>> Disable And Remove Block Burst");
		se.sendMessage(gr + "/"+b+" SetDelay >>> Change Block Burst Timing");
		se.sendMessage(gr + "/"+b+" Toggle >>> Stop / Start Block Burst");
		se.sendMessage(gr + "/"+b+" setBh >>> Sets The Height Of Blocks");
		se.sendMessage(gr + "/"+b+" Locate >>> Tp To A Block Burst");
	}
	
	public static void saveSigns(){
		try {
			File file = new File("plugins/AniMessage/Signs.data");
	        FileOutputStream fileOutput = new FileOutputStream(file);
	        ObjectOutputStream objectOutput = new ObjectOutputStream(fileOutput);
	        try{objectOutput.writeObject(messages);}catch(Exception e101){/*e101.printStackTrace();*/}
	        try{objectOutput.writeObject(frames);}catch(Exception e102){/*e102.printStackTrace();*/}
	        try{objectOutput.writeObject(parkours);}catch(Exception e102){/*e102.printStackTrace();*/}
	        try{objectOutput.writeObject(bursts);}catch(Exception e103){e103.printStackTrace();}
	        objectOutput.close();
	        log.info("[AniMessage] Plugin Information Saved!");	
		} catch (Exception e10) {
	        	e10.printStackTrace();
	        	File file = new File("plugins/AniMessage");
				file.mkdir();
	    }
	}
	
	@SuppressWarnings("unchecked")
	public static void loadSigns(){
		try{
			ObjectInputStream read = new ObjectInputStream(new FileInputStream("plugins/AniMessage/Signs.data"));
			try{messages = (ArrayList<Message>)read.readObject();} catch(Exception e111){/*e111.printStackTrace();*/}
			try{frames = (ArrayList<FrameManager>)read.readObject();} catch(Exception e112){/*e112.printStackTrace();*/}
			try{parkours = (ArrayList<Parkour>)read.readObject();} catch(Exception e113){/*e113.printStackTrace();*/}
			try{bursts = (ArrayList<Burst>)read.readObject();} catch(Exception e114){e114.printStackTrace();}
			read.close();
			System.out.println("[AniMessage] Plugin Information Loaded!");	
		} catch(Exception e11){
			e11.printStackTrace();
			File file = new File("plugins/AniMessage");
			file.mkdir();
		}
	}
}
