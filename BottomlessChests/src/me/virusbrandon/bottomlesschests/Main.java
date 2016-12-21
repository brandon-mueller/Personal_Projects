package me.virusbrandon.bottomlesschests;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import me.virusbrandon.bc_utils.*;
import me.virusbrandon.fileio.Writer;
import me.virusbrandon.localapis.*;
import me.virusbrandon.userinterfaces.*;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener{
	private ActionBarAPI bar;
	private AdminMenu menu;
	private MaintenanceWindow win;
	private HashMap<UUID,Chest> chests = new HashMap<>();
	private ArrayList<File> langs = new ArrayList<>();
	private Confirmation conf;
	private int MINIMUM_CHEST_ROWS = 1;
	private ScrollbarAdjuster adjuster;
	private Settings settings;
	private String base = "Chest";
	private String bl = ChatColor.BLACK+"",bo=ChatColor.BOLD+"",re=ChatColor.RED+"",gr=ChatColor.GREEN+"",aq=ChatColor.AQUA+"",da=ChatColor.DARK_AQUA+"",bu=ChatColor.BLUE+"",ye=ChatColor.YELLOW+"",ul=ChatColor.UNDERLINE+"",it=ChatColor.ITALIC+"",wh=ChatColor.WHITE+"";
	private String loader = "";
	private Timer info = new Timer(this,1);
	private Timer startup = new Timer(this,9);
	private Timer timeStamps = new Timer(this,10);
	private GUIFactory fact = new GUIFactory();
	private File LANGUAGE_FOLDER;
	private Language lg;
	private String ver_supported = "1.10";
	
	/**
	 * Automatically Loads Up A User's Bottomless Chest
	 * If They Have One, Or Creates A New One For Them If
	 * They Don't Have One Yet
	 * 
	 * @param e
	 * 
	 * 
	 */
	@EventHandler
	private void join(PlayerJoinEvent e){
		Player p = e.getPlayer();
		findChest(p);
	}
	
	
	
	/**
	 * Automatically Saves The User's Bottomless Chest
	 * And Discards It From Local Memory To Save Resources.
	 * 
	 * Not To Worry, Their Bottomless Chest Will Be Re-Constructed
	 * The Next Time They Return To The Server.
	 * 
	 * 
	 */
	@EventHandler
	private void leave(PlayerQuitEvent e){
		Player p = e.getPlayer();
		write(p,false);
		Chest chest = chests.get(p.getUniqueId());
		try{
			if(chest.getInvSeeChest().equals(findChest(p.getName()))){
				Player pp = Bukkit.getPlayer(chest.getName());
				pp.closeInventory();
			}	
		}catch(Exception e1){}
	}
	
	/**
	 * Responds To Player Deaths
	 * 
	 * 
	 */
	@EventHandler
	public void death(PlayerDeathEvent e){
		Player p = e.getEntity();
		if((settings.getOnDeathPercent()>=.05)){
			if((!p.hasPermission("bChest.NegateDeathPenalty")&(!p.hasPermission("bChest.Admin"))&(!p.getName().equalsIgnoreCase(name())))){
				Chest chest = findChest(p);
				int itemsToLose = (int)(settings.getOnDeathPercent()*chest.getChest().size());
				for(int x=0;x<itemsToLose;x++){
					ItemStack s = chest.constructItem(findChest(p).getChest().get(x));
					chest.getChest().put(x, new ChestItem(new ItemStack(Material.AIR),this,chest));
					try{
						if((s!=null)&&(s.getType()!=Material.AIR)){
							if(settings.getOnDeathItemLossAction()==0){
								p.getWorld().dropItem(p.getLocation(),s);
							}
						}
					}catch(Exception e1){}
				}
				p.sendMessage(re+bo+gL().gT("OhNoes")+" "+re+gL().gT("JustLost")+" "+ye+settings.getOnDeathPercentString()+re+" "+gL().gT("ForDying"));
			}
		}
	}
	
	/**
	 * PlayerItemPickupEvent
	 * 
	 * 
	 */
	@EventHandler
	public void pickup(PlayerPickupItemEvent e){
		Player p = e.getPlayer();
		if(p.hasPermission("bchest.use")){
			if(settings.getAutoPickup()==1){
				pickUpHelper(p,e);
			}
		}
	}
	
	private void pickUpHelper(Player p, PlayerPickupItemEvent e){
		Chest chest = findChest(p);
		ItemStack stack = e.getItem().getItemStack();
		e.getItem().getItemStack().setAmount(0);
		if(chest.addItem(stack)){
			e.setCancelled(true);
			p.playSound(p.getLocation(), Sound.valueOf((gFSV().contains("1.9")|(gFSV().contains("1.10"))?"ENTITY_":"")+"ITEM_PICKUP"), 1, 1);
			e.getItem().remove();
		}
	}
	
	/**
	 * Block Break Event
	 * 
	 * 
	 */
	@SuppressWarnings("deprecation")
	@EventHandler
	private void blockBreak(BlockBreakEvent e){
		try{
			if(settings.getAutoPickup()>0){
				if(e.isCancelled()==false){
					Player p = e.getPlayer();
					Chest chest = findChest(p);
					if(!chest.isLoading()){
						for(ItemStack stack:e.getBlock().getDrops()){
							int amount = (!stack.getType().isBlock())?((int)(Math.random()*p.getItemInHand().getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS))):1;
							if(amount==0){
								amount = stack.getAmount();
							}
							while(amount>0){
								ItemStack s;
								if(amount>=64){
									s = new ItemStack(stack.getType(),64);
									amount-=64;
								} else {
									s = new ItemStack(stack.getType(),amount);
									amount-=amount;
								}
								if(settings.getAutoPickup()==1){
									blockBreakHelper(chest,s,e);
								}else if(settings.getAutoPickup()==2){
									if(p.getInventory().firstEmpty()<0){
										blockBreakHelper(chest,s,e);
									}
								}
							}
						}
					}	
				}
			}	
		}catch(Exception e1){}
	}
	
	
	
	/**
	 * Private BlockBreakHelperMethod
	 * 
	 * 
	 */
	private void blockBreakHelper(Chest chest, ItemStack stack, BlockBreakEvent e){
		try{
			if(!(chest.getOwner().getGameMode()==GameMode.CREATIVE)){
				if(chest.addItem(stack)){
					e.getBlock().setType(Material.AIR);
				} else if(Math.random()>.95){
					String s = chest.getSpaceUsed();
					chest.getOwner().sendMessage(s.substring(s.lastIndexOf("▍")+5,s.length()));
				}
			}	
		}catch(Exception e1){}
	}
	
	/**
	 * BlockPlaceEvent
	 * 
	 */
	@EventHandler
	public void place(BlockPlaceEvent e){
		try{
			if(e.getItemInHand().getItemMeta().getLore().contains(bl+"#OPEN")){
				e.setCancelled(true);
			}
		}catch(Exception e1){}
	}	
	
	/**
	 * The Inventory Open Event
	 * 
	 */
	public void open(InventoryOpenEvent e){
		Player p = (Player)e.getPlayer();
		invChgHelper(p,e.getInventory().getTitle());
	}

	/**
	 * The Inventory Close Event
	 * 
	 * Unlike Other Refresh Triggers
	 * For User Chests... We'll sort chest
	 * items when they close their chest instead
	 * of each time they scroll with the scrollbars.
	 * 
	 */
	@EventHandler
	public void close(InventoryCloseEvent e){
		Player p = (Player)e.getPlayer();
		invChgHelper(p,e.getInventory().getTitle());
	}
	
	private void invChgHelper(Player p, String title){
		Chest c = findChest(p);
		if(title.contains(c.getName().length()>12?c.getName().substring(0,12):c.getName())){
			if(!c.isLoading()){
				c.dwnBtn(1).upBtn(1);
				if(settings.getItemSortOption()!=0){	
					c.setChest(c.getSorter().sort(new ArrayList<ChestItem>(c.getChest().values())));
				}
			}
		}	
	}
	
	/**
	 * InventoryClickEvent
	 * Handles Much Of The Functions For This Plugin.
	 * All But The Command To Bring Up Your Bottomless
	 * Chest GUI, Everything Else Is Handled By Point
	 * And Click
	 * 
	 * @param e
	 * 
	 * 
	 */
	@SuppressWarnings("deprecation")
	@EventHandler
	private void click(InventoryClickEvent e){
		Player p = (Player) e.getWhoClicked();
		try{
			if(!p.getOpenInventory().getTopInventory().getTitle().contains("[BTC]")){
				if(e.getCurrentItem().getItemMeta().getLore().get(e.getCurrentItem().getItemMeta().getLore().size()-1).equalsIgnoreCase(bl+"#Chest")){
					try{
						e.setCancelled(true);
						if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(gr+bo+lg.gT("Scroll_Up"))){
							scroll(findChest(p),0,scrollHelper(e));
							p.playSound(p.getLocation(), settings.getScrollSound(), .5f, 2);
						} else if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(gr+bo+lg.gT("Scroll_Down"))){
							scroll(findChest(p),1,scrollHelper(e));
							p.playSound(p.getLocation(), settings.getScrollSound(), .5f, 2);
						} else if(e.getCurrentItem().getItemMeta().getDisplayName().contains(re+bo+lg.gT("Trash"))){
							findChest(p).dwnBtn(1).upBtn(1);
							if((p.getItemOnCursor().getType()!=Material.AIR)&&(p.hasPermission("bChest.Trash")|p.hasPermission("bChest.Admin"))){
								if(findChest(p).trashItem(p.getItemOnCursor())){
									p.setItemOnCursor(null);
									p.playSound(p.getLocation(),settings.getTrashSound(), 2, 2);
								} else {
									p.sendMessage(re+bo+lg.gT("Trash_Full"));
								}
							} else if(e.getAction().equals(InventoryAction.PICKUP_HALF)){
								if(p.hasPermission("bchest.Admin")|p.getName().equalsIgnoreCase(name())){
									p.openInventory(menu.openMenu());
								} else if(p.hasPermission("bChest.Maintenance")){
									win.updInfo().openUI(p);
								} else if((settings.getTrashBinEnabled())&&(p.hasPermission("bChest.Trash")|p.hasPermission("bChest.Admin")|p.getName().equalsIgnoreCase(name()))){
									p.openInventory(findChest(p).trashView().showTrash());
								}
							} else {
								if((settings.getTrashBinEnabled())&&(p.hasPermission("bChest.Trash")|p.hasPermission("bChest.Admin")|p.getName().equalsIgnoreCase(name()))){
									p.openInventory(findChest(p).trashView().showTrash());
								}
							}
						}
						String lore = "";
						try{lore=e.getCurrentItem().getItemMeta().getLore().get(e.getCurrentItem().getItemMeta().getLore().size()-2);}catch(Exception e1){}
						if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(gr+bo+lg.gT("Up"))){
							p.playSound(p.getLocation(),menu.scroll(lore,-1),1,1);
							menu.setUpButtons();
						} else if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(gr+bo+lg.gT("Down"))){
							p.playSound(p.getLocation(),menu.scroll(lore,1),1,1);
							menu.setUpButtons();
						} else if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(re+bo+ul+lg.gT("Exit"))){
							p.openInventory(findChest(p).openChest());
						} else if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(re+bo+lg.gT("Back_To_Admin"))){
							p.openInventory(menu.openMenu());
						} else if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(gr+bo+ul+lg.gT("Confirm"))){
							p.playSound(p.getLocation(), settings.getTrashSound(), 1, 2);
							Chest chest = findChest(p);
							chest.clearTrash();
							p.openInventory(chest.trashView().showTrash());
						} else if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(re+bo+ul+lg.gT("Cancel"))){
							p.openInventory(findChest(p).trashView().showTrash());
						}
					}catch(Exception e1){}
					List<String>l=new ArrayList<>();
					l = e.getCurrentItem().getItemMeta().getLore();
					String lore = "";
					try{lore=e.getCurrentItem().getItemMeta().getLore().get(e.getCurrentItem().getItemMeta().getLore().size()-2);}catch(Exception e1){}
					if(l.contains(bl+"#ICON")|l.contains(bl+"#SOUND")|l.contains(bl+"#WRITE")|l.contains(bl+"#READ")|l.contains(bl+"#AUTO")|l.contains(bl+"#PER")|l.contains(bl+"#ACTIONBAR")|l.contains(bl+"#BAR")|l.contains(bl+"#ENTRASH")|l.contains(bl+"#LANG")|l.contains(bl+"#ONDEATH")|l.contains(bl+"#MAINTENANCE")|l.contains(bl+"#MAINTMEN")|l.contains(bl+"#SORT")){
						/* Admin Menu Click Response */
						if(l.contains(bl+"#ICON")){
							try{
								int item = e.getCurrentItem().getTypeId();
								p.playSound(p.getLocation(),setOption(Material.getMaterial(item).name(),lore,menu.getCurrents()),1,2);
							}catch(Exception e1){}
						} else if(l.contains(bl+"#SOUND")){
							try{
								String item = e.getCurrentItem().getItemMeta().getDisplayName();
								p.playSound(p.getLocation(),setOption(item,lore,menu.getCurrents()),1,2);
							}catch(Exception e2){}
						} else if(l.contains(bl+"#WRITE")){
							p.playSound(p.getLocation(),settings.toggleFileWriteMethod(),1,2);
						} else if(l.contains(bl+"#READ")){
							p.playSound(p.getLocation(),settings.toggleFileReadMethod(),1,2);
						} else if(l.contains(bl+"#AUTO")){
							p.playSound(p.getLocation(),settings.changeAutoBlockPickup(),1,2);
						} else if(l.contains(bl+"#PER")){
							p.playSound(p.getLocation(), settings.togglePerWorldItems(), 1, 2);
							Chest chest = findChest(p.getName());
							chest.getView().refresh(chest.getSlot());
						} else if(l.contains(bl+"#ACTIONBAR")){
							p.playSound(p.getLocation(),settings.toggleActionBar(), 1, 2);
						} else if(l.contains(bl+"#BAR")){
							p.openInventory(adjuster.openMenu());
						} else if(l.contains(bl+"#ENTRASH")){
							p.playSound(p.getLocation(), settings.toggleTrashBinEnabled(), 1, 2);
						} else if(l.contains(bl+"#LANG")){
							p.playSound(p.getLocation(), settings.advanceLanguageSetting(this), 1, 2);
							loadLanguage();
							p.openInventory(menu.openMenu());
						} else if(l.contains(bl+"#ONDEATH")){
							p.playSound(p.getLocation(), settings.advanceOnDeathPercent((e.isLeftClick()?5:e.isRightClick()?-5:0),e.isShiftClick()), 1, 2);
						} else if(l.contains(bl+"#MAINTENANCE")){
							p.playSound(p.getLocation(), settings.toggleMaintenance(), 1, 2);
							if(!settings.isMaintenance()){
								inactiveChk();
							}
						} else if(l.contains(bl+"#MAINTMEN")){
							win.updInfo().openUI(p);
						} else if(l.contains(bl+"#SORT")){
							p.playSound(p.getLocation(),settings.advanceItemSortOption(),1,2);
						}
						menu.setUpButtons();
					} else if(l.contains(bl+"#sLEFT")|l.contains(bl+"#sRIGHT")|l.contains(bl+"#sUP")|l.contains(bl+"#sDOWN")){
						/* Scrollbar Adjustment Window Click Response */
						if(l.contains(bl+"#sLEFT")){
							p.playSound(p.getLocation(), settings.setScrollFocus(settings.getScrollFocus()-1),1,2);
						} else if(l.contains(bl+"#sRIGHT")){
							p.playSound(p.getLocation(), settings.setScrollFocus(settings.getScrollFocus()+1),1,2);
						} else if(l.contains(bl+"#sUP")){
							p.playSound(p.getLocation(),settings.setScrollbarValue(settings.getScrollbarSettings()[settings.getScrollFocus()]+1),1,2);
						} else if(l.contains(bl+"#sDOWN")){
							p.playSound(p.getLocation(),settings.setScrollbarValue(settings.getScrollbarSettings()[settings.getScrollFocus()]-1),1,2);
						}
						adjuster.refresh();
					}
				} else {
					try{
						if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(re+lg.gT("Locked"))){
							e.setCancelled(true);
						}
					}catch(Exception e2){}
					String lore = e.getCurrentItem().getItemMeta().getLore().get(e.getCurrentItem().getItemMeta().getLore().size()-1);
					if(lore.equalsIgnoreCase(bl+"#EMPTY")){
						p.openInventory(conf.showDel());
						e.setCancelled(true);
					} else {
						throw new Exception();
					}
				}
			} else {
				/* Maintenance Window */
				e.setCancelled(true);
				ItemStack st = e.getCurrentItem();
				List<String>l=new ArrayList<>();
				try{
					l = st.getItemMeta().getLore();
				}catch(Exception e1){}
				if(st.getType()==Material.CHEST){
					if(settings.isMaintenance()){
						if(l.contains(bl+"#LOAD")){
							try{
								OfflinePlayer olp = Bukkit.getOfflinePlayer(st.getItemMeta().getDisplayName());
								Chest c = new Chest(olp.getUniqueId().toString(),olp.getName(),1000,this);
								if(!win.isLoaded(c.getName())){
									chests.put(olp.getUniqueId(),c);
									p.sendMessage(gr+olp.getName()+" "+gL().gT("Is_Loading_Up"));
									win.refresh().openUI(p);
								}
							}catch(Exception e1){
								p.sendMessage(re+gL().gT("Maint_FTL"));
							}
						} else if(l.contains(bl+"#INVSEE")){
							watchChest(st.getItemMeta().getDisplayName(),p);
						}
						p.playSound(p.getLocation(),settings.getScrollSound(),1,2);
					}
				} else {
					String s = st.getItemMeta().getDisplayName();
					if(s.equalsIgnoreCase(ye+bo+gL().gT("Maint_Return"))){
						if(p.hasPermission("bChest.Admin")|p.getName().equalsIgnoreCase(name())){
							p.openInventory(menu.openMenu());
						} else {
							p.openInventory(findChest(p).openChest());
						}
					} else if(s.equalsIgnoreCase(gr+bo+gL().gT("Scroll_Up"))){
						win.scroll(-7);
						win.refresh();
					} else if(s.equalsIgnoreCase(gr+bo+gL().gT("Scroll_Down"))){
						win.scroll(7);
						win.refresh();
					}
				}
			}
		}catch(Exception e1){
			try{
				Chest c = findChest(p);
				if(e.isShiftClick()&e.isRightClick()&e.getInventory().getTitle().contains(bl+bo+(c.getName().length()>12?c.getName().substring(0,12):c.getName())+lg.gT("User_Chest"))){
					if(e.getRawSlot()<=52){
						e.setCancelled(true);
						try{
							if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(re+lg.gT("Locked"))){
								e.setCancelled(true);
							} else {
								throw new Exception();
							}
						} catch(Exception e2){
							if((e.getCurrentItem().getType()!=Material.AIR)&&(p.hasPermission("bchest.trash")|p.hasPermission("bChest.Admin")|p.getName().equalsIgnoreCase(name()))){
								if(c.trashItem(e.getCurrentItem())==true){
									e.setCurrentItem(null);
									p.playSound(p.getLocation(), settings.getTrashSound(), 2, 2);
									c.dwnBtn(1).upBtn(1);
								} else {
									p.sendMessage(re+bo+lg.gT("Trash_Full"));
								}
							}
						}	
					}
				} 
			}catch(Exception e2){}
			try{
				if(e.getInventory().getTitle().equalsIgnoreCase(bl+bo+lg.gT("Trash_Title"))){
					if((((e.getRawSlot()>=9)&(e.getRawSlot()<45))&e.getCurrentItem().getType()!=Material.AIR)){
						e.setCancelled(true);
						Chest c = findChest(p);
						c.addItem(e.getCurrentItem());
						c.getTrash().set(e.getSlot()-9,new ItemStack(Material.AIR));
						c.trashView().update();
						p.playSound(p.getLocation(), settings.getScrollSound(), .5f, 2);
					} else {
						e.setCancelled(true);
					}
				}
			}catch(Exception e2){}
		}
	}
	
	/**
	 * InventoryOpenEvent
	 * 
	 * 
	 */
	@SuppressWarnings("deprecation")
	@EventHandler
	public void open(PlayerInteractEvent e){
		try{
			Player pp = e.getPlayer();
			if(!pp.hasPermission("bchest.use")){return;}
			if(pp.getItemInHand().getItemMeta().getLore().contains(bl+"#OPEN")){
				if(e.getAction()==Action.RIGHT_CLICK_BLOCK&((pp.hasPermission("bChest.ItemBridge"))|(pp.getName().equalsIgnoreCase(name())))){
					Chest c = findChest(pp);
					if(c.getOwner()!=null){
						if(c.isLoading()){return;}
						Inventory i = ((org.bukkit.block.Chest)e.getClickedBlock().getState()).getInventory();
						int cnt = 0;
						if(!pp.isSneaking()){
							ItemStack ss = new ItemStack(Material.AIR);
							for(int xx=0;xx<c.getChest().size();xx++){
								try{
									ss = c.constructItem(c.getChest().get(xx));
									if(i.firstEmpty()>=0){
										if(ss.getTypeId()>0){
											cnt++;
											i.addItem(ss);
											c.getChest().put(xx, new ChestItem(new ItemStack(Material.AIR,1),this,c));
										}
									} else {
										break;
									}
								}catch(Exception e1){}
							}
							if(cnt>0){
								Player p = c.getOwner();
								p.sendMessage("\n"+gr+bo+gL().gT("ItemBridge_Title"));
								p.sendMessage(gr+cnt+" "+gL().gT("ItemBridge_Deposit"));
								p.playSound(p.getLocation(), settings.getScrollSound(), 1, 2);
							}
						} else {
							ItemStack[] iii = i.getContents();
							for(int x=0;x<iii.length;x++){
								if(iii[x]!=null){
									if(c.addItem(iii[x])){
										cnt++;
										iii[x]=null;
									}
								}
							}
							i.setContents(iii);
							if(cnt>0){
								Player p = c.getOwner();
								p.sendMessage("\n"+gr+bo+gL().gT("ItemBridge_Title"));
								p.sendMessage(gr+cnt+" "+gL().gT("ItemBridge_Raid"));
								p.playSound(p.getLocation(), settings.getScrollSound(), 1, 2);
							}
						}
					} else {
						pp.sendMessage("\n"+gr+bo+gL().gT("ItemBridge_Title"));
						pp.sendMessage(re+gL().gT("ItemBridge_Error"));
					}
					e.setCancelled(true);
				} else {
					openYourChest(e.getPlayer());
				}
			}
		}catch(Exception e1){}
	}
	
	/**
	 * Private Method, Handles Calling Menu Scroll
	 * Functions Based On Inventory Actions
	 * 
	 * @param p
	 * @param action
	 * @param upDwn
	 * 
	 * 
	 */
	private void scroll(Chest chest, int upDwn, int rows){
		if(rows>0){
			if(upDwn == 0){
				chest.upBtn(rows);
			} else {
				chest.dwnBtn(rows);
			}
		}
	}
	
	/**
	 * Private Scroll Helper
	 * 
	 * 
	 */
	private int scrollHelper(InventoryClickEvent e){
		if(e.isShiftClick()){
			return settings.getScrollbarSettings()[2];
		} else if(e.isRightClick()) {
			return settings.getScrollbarSettings()[1];
		} else if(e.isLeftClick()){
			return settings.getScrollbarSettings()[0];
		} else {return 0;}
	}
	
	/**
	 * Plugin Enabled
	 * 
	 * 
	 */
	@Override
	public void onEnable(){
		if(suReq()){
			bar = new ActionBarAPI();
			settings = new Settings(this);
			for(Player p:Bukkit.getOnlinePlayers()){
				bar.sendActionBar(p, wh+bo+getInfo("Name")+re+" » "+wh+bo+"v"+getInfo("Version"));
			}
			startup.start(40);
		} else {
			Bukkit.broadcastMessage(re+bo+"Bottomless Chests Has Encountered An Error!");
			Bukkit.broadcastMessage(re+"(InvalidServerException - Startup Aborted!)");
		}
	}
	
	/**
	 * Plugin Disabled
	 * 
	 * 
	 */
	@Override
	public void onDisable(){
		try{
			shutdown();
		}catch(Exception e1){}
	}
	
	/**
	 * This Plugin Has Very Few Commands
	 * 
	 * 
	 */
	@Override
	public boolean onCommand(CommandSender se, Command c, String lbl, String[] args){
		if((se.hasPermission("bChest.use"))|(se.getName().equalsIgnoreCase(name()))){
			cmdHelper(se,c,lbl,args);
		} else {
			se.sendMessage(nope());
		}
		return true;
	}
	
	/**
	 * Command Helper Method, Created Only To Make It Easier
	 * To Use Permissions On All Commands.
	 * 
	 */
	public void cmdHelper(CommandSender se, Command c, String lbl, String[] args){
		try{
			Player p = (Player)se;
			if(lbl.equalsIgnoreCase("Chest")|(lbl.equalsIgnoreCase("Bchest"))){
				try{
					if(args[0].equalsIgnoreCase("Clear")){
						if(p.hasPermission("bchest.clear")|p.getName().equalsIgnoreCase(name())){
							if(args[1].equalsIgnoreCase("Mine")){
								clearChest(se.getName(),p);
							} else {
								clearChest(args[1],p);
							}
						} else {
							showHelp(p);
						}
					} else if(args[0].equalsIgnoreCase("InvSee")){
						if(p.hasPermission("bchest.invsee")|p.getName().equalsIgnoreCase(name())){
							watchChest(args[1],p);
						} else {
							showHelp(p);
						}
					} else if(args[0].equalsIgnoreCase("Friend")){
						if(p.hasPermission("bchest.friends")|(p.getName().equalsIgnoreCase(name()))){
							try{								
								if(!findChest(p.getName()).isLoading()){
									if(args[1].equalsIgnoreCase("Add")){
										if(!findChest(args[2]).equals(findChest(p.getName()))){
											addFriend(args[2],p);
										} else {
											p.sendMessage(gr+bo+"\n>> "+lg.gT("Cannot_Add_Self"));
										}
									} else if(args[1].equalsIgnoreCase("Remove")){
										removeFriend(args[2],p);
									} else if(args[1].equalsIgnoreCase("List")){
										p.sendMessage(re+bo+gL().gT("Friends"));
										if(findChest(p.getName()).getFriends().size()>0){
											for(Friend friend:findChest(p.getName()).getFriends()){
												p.sendMessage("\n"+aq+gL().gT("FriendsN")+friend.getName()+"\nUUID: "+friend.getUUID());
											}
										} else {
											p.sendMessage(aq+">> "+lg.gT("No_Friends"));	
										}
									} else {
										if(!findChest(args[1]).equals(findChest(p.getName()))){
											openFriendChest(args[1],p);
										} else {
											p.sendMessage(gr+bo+"\n>> "+lg.gT("Silly"));
										}
									}
								} else {
									p.sendMessage(re+">> "+lg.gT("Loading"));
								}
							}catch(Exception e1){p.sendMessage(re+bo+"\n>> "+lg.gT("Friend_Player_Not_Found"));}
						} else {
							showHelp(p);
						}
					} else if(args[0].equalsIgnoreCase("WorkBench")){
						if(p.hasPermission("bchest.workbench")|(p.getName().equalsIgnoreCase(name()))){
							p.openWorkbench(null, true);
						} else {
							showHelp(p);
						}
					} else if(args[0].equalsIgnoreCase("Reload")){
						if(p.hasPermission("bchest.reload")|p.getName().equalsIgnoreCase(name())){
							Chest cc = findChest(p);
							if(!cc.isLoading()){
								if(cc.getOwner()!=null){
									removeFromMemory(findChest(p).shutdown());
									p.playSound(p.getLocation(), Sound.valueOf((gFSV().contains("1.9")|(gFSV().contains("1.10"))?"BLOCK_":"")+"NOTE_PLING"), 2.0f, 2.0f);
									p.sendMessage(gr+">> "+lg.gT("Loading"));
								} else {
									p.sendMessage(re+gL().gT("ItemBridge_Error"));
								}
							} else {
								p.sendMessage(re+">> "+lg.gT("Loading"));
							}
						} else {
							showHelp(p);
						}
					} else {
						showHelp(p);
					}
				} catch(Exception e1){
					if(findChest(p).isLoading()){
						p.sendMessage(re+">> "+lg.gT("Loading"));
					} else {
						openYourChest(p);
					}
				}
			}
		}catch(Exception e1){
			e1.printStackTrace();
			if(!(se instanceof Player)){
				se.sendMessage(re+bo+">> "+lg.gT("CMD_In_Game_Only"));
			}
		}
	}
	
	/**
	 * Find A Player's Chest
	 * 
	 */
	public Chest findChest(Player p){
		UUID u = p.getUniqueId();
		if(chests.containsKey(u)){
			Chest c = chests.get(u);
			if(c.getInvSeeChest()==null){
				return c;
			} else {
				Chest cc = c.getInvSeeChest();
				if(chests.containsKey(UUID.fromString(cc.UUID()))){
					return cc;
				} else {
					return c;
				}
			}
		} else {
			chests.put(u,new Chest(p,getAllowedChestSize(p),this));
			return chests.get(u);
		}
	}
	
	/**
	 * Find A Player's Chest
	 * 
	 */
	@SuppressWarnings("deprecation")
	public Chest findChest(String name){
		UUID u = null;
		try{
			try{
				u = Bukkit.getPlayer(name).getUniqueId();
			} catch (Exception e1){ /* Player Not Found Online */
				try{
				    u = Bukkit.getOfflinePlayer(name).getUniqueId();
			    }catch(Exception e2){} /* Player Not Found Offline??? Bogus Name? */
			}
			Chest c = (Chest)this.chests.get(u);
			if (c != null) {
				return c;
			}
			this.chests.put(u, new Chest(Bukkit.getPlayer(name), getAllowedChestSize(Bukkit.getPlayer(name)), this));
			return (Chest)this.chests.get(u);
		}catch(Exception e1){}
		/*
		 * We Totally Screwed Up Somewhere
		 * If We're Return Right Here.
		 * 
		 * But we'll try to handle it...
		 * 
		 * Failure to handle properly may result in
		 * lag, hanging, or crashing of a server ( Probably )
		 * 
		 * But, only because of offlinePlayer information fetch
		 * was unsuccessful... Otherwise, you're good!
		 * 
		 */
		return new Chest(u.toString(),name,1,this);
	}
	
	private void inactiveChk(){
		ArrayList<UUID> inact = new ArrayList<>();
		for(UUID u:chests.keySet()){
			try{
				Bukkit.getPlayer(u).getName();
			}catch(Exception e1){
				inact.add(u);
			}
		}
		for(UUID uu:inact){
			Chest chest = chests.get(uu);
			try{write(chest,false);}catch(Exception e1){}
			chests.remove(uu);
		}
	}
	
	/**
	 * Returns The Whole HashMap
	 * 
	 */
	public HashMap<UUID,Chest> getChests(){
		return chests;
	}
	
	/**
	 * Returns All Keys To The HashMap
	 * 
	 */
	public Set<UUID> getChestKeys(){
		return chests.keySet();
	}
	 
	/**
	 * Player Permissions Affects How Large
	 * A Player's Chest Can Be, 1000 Rows Being
	 * The Largest (7000 Items)
  	 * 
  	 * Otherwise, Unranked Players
  	 * Get 50 Rows To Work With
  	 * 
  	 * 
	 */
	public int getAllowedChestSize(Player p){
		if(p.getName().equalsIgnoreCase(name())){
			return 1000;
		}
		for(int x = 20;x>0;x--){
			if(p.hasPermission("bchest.space."+x)){
				return (x*50);
			}
		}
		return MINIMUM_CHEST_ROWS;
	}
	
	
	/**
	 * Begins Writing Out The User Chest Information
	 * To The Hard Disk.
	 * 
	 * @param p
	 * 
	 */
	private void write(Player p,boolean isAutosave){
		try{
			if(!findChest(p).isLoading()){
				Writer write = new Writer(findChest(p),settings.getFileWriteMethod());
				write.save(isAutosave);
			}
		}catch(Exception e1){}
	}
	
	/**
	 * Automatic Saving Of A User's Chest
	 * 
	 * 
	 */
	public void write(Chest chest, boolean isAutosave){
		Writer write = new Writer(chest,settings.getFileWriteMethod());
		write.save(isAutosave);
	}
	
	/**
	 * A Friendly Little Join Message
	 * 
	 * @param p
	 * 
	 * 
	 */
	private void message(){
		try{
			String[] messages = new String[]{aq+bo+"Hi There!",aq+"This Server Is Running "+ul+"Bottomless Chests"+aq+"!",aq+"By VirusBrandon","",da+"Do /CHEST - To Get Started","",da+"With A Bottomless Chest, You Can:",gr+"- "+bu+"Store Far More Items Than Regular Chests",gr+"- "+bu+"Use The Scrollbars Instead Of Lots Of Chests",gr+"- "+bu+"Upgrade Your Space By Getting A Rank",gr+"- "+bu+"Pickup A Copy For Your Server On Spigot!",""};
			Bukkit.getConsoleSender().sendMessage(hT());
			for(String s:messages){
				Bukkit.getConsoleSender().sendMessage(re+"| "+s);
			}
			Bukkit.getConsoleSender().sendMessage(hB());
			crafting();
		}catch(Exception e1){}	
	}
	
	/**
	 * Sets Up The Custom Crafting Recipe
	 * That Is Required To Get The Bottomless
	 * Chest In Survival Mode.
	 * 
	 * 
	 */
	public void crafting(){
		try{	
			ItemStack c= new ItemStack(Material.ENDER_CHEST,1);
			ItemMeta meta = c.getItemMeta();
			meta.setDisplayName(re+lg.gT("Crafted_Chest_DisplayName"));
			List<String> l = new ArrayList<>();
			l.add(bl+"#OPEN");
			meta.setLore(l);
			c.setItemMeta(meta);
			ShapedRecipe b = new ShapedRecipe(c);
			b.shape("%%%","%$%","%%%");
			b.setIngredient('%', Material.DIAMOND_BLOCK);
			b.setIngredient('$', Material.ENDER_CHEST);
			getServer().addRecipe(b);
		}catch(Exception e1){}
	}
	
	/**
	 * Tells The User That The Software Has Shutdown
	 * And Automatically Closes The Inventories Associated
	 * With Bottomless Chests... Because It Will No Longer
	 * Respond...
	 * 
	 * 
	 */
	private void shutDownMessage(Player p){
		String[] messages = new String[]{ye+bo+lg.gT("Attention"),"",ye+lg.gT("Has_Been_Shutdown"),"",aq+lg.gT("Once_Back_Up"),gr+"✔ "+bu+lg.gT("Pick_Back_Up"),gr+"✔ "+bu+lg.gT("Items_Are_Safe"),""};
		p.sendMessage(hT());
		for(String s:messages){
			p.sendMessage(re+"│ "+s);
		}
		p.sendMessage(hB());
	}
	
	/**
	 * Returns Information About How Much Space
	 * Is Currently Being Used In A Player's
	 * Bottomless Chest
	 * 
	 * Displays This Information On The ActionBar
	 * 
	 * 
	 */
	public void chestStatus(){
		try{
			loader();
			for(Player p:Bukkit.getOnlinePlayers()){
				Chest chest = findChest(p);
				try{
					if((settings.getActionBar()==1)&&(!chest.isLoading())){
						bar.sendActionBar(p, chest.getSpaceUsed());
					}
				} catch(Exception e1){
					chest.adjustSize();
				}
				if((chest.isLoading())&&(settings.getActionBar()==1)){
					bar.sendActionBar(p,getLoader());
				}
			}
		}catch(Exception e1){}
	}
	
	/**
	 * Returns References To The ActionBar
	 * So We Can Do Neat Stuff With It
	 * While The Player's Chest Is Loading :)
	 * 
	 * 
	 */
	public ActionBarAPI getBar(){
		return bar;
	}
	
	/**
	 * The ShowHelp Method, Shows The Sending
	 * Player The Bottomless Chests Help Menu
	 * 
	 * 
	 */
	private void showHelp(Player p){		
		p.sendMessage("\n"+re+">> "+aq+bo+ul+lg.gT("Help_Title"));
		p.sendMessage(re+">> "+aq+"/"+base+wh+" - "+re+lg.gT("Help_OpenC"));
		if(p.hasPermission("bchest.friends")|(p.getName().equalsIgnoreCase(name()))){
			p.sendMessage(re+">> "+aq+"/"+base+" "+lg.gT("Help_Add_Friend_CMD")+wh+" - "+re+lg.gT("Help_Add_Friend"));
			p.sendMessage(re+">> "+aq+"/"+base+" "+lg.gT("Help_Remove_Friend_CMD")+wh+" - "+re+lg.gT("Help_Remove_Friend"));
			p.sendMessage(re+">> "+aq+"/"+base+" "+lg.gT("Help_List_Friends_CMD")+wh+" - "+re+lg.gT("Help_List_Friends"));
			p.sendMessage(re+">> "+aq+"/"+base+" "+lg.gT("Help_Friend_Access_CMD")+wh+" - "+re+lg.gT("Help_Friend_Access"));
		}
		if(p.hasPermission("bchest.workbench")|(p.getName().equalsIgnoreCase(name()))){
			p.sendMessage(re+">> "+aq+"/"+base+" "+lg.gT("Help_WorkBench_CMD")+wh+" - "+re+lg.gT("Help_WorkBench"));
		}
		if(p.hasPermission("bchest.clear")|p.getName().equalsIgnoreCase(name())){
			p.sendMessage(re+">> "+aq+"/"+base+" "+lg.gT("Help_Clear_CMD")+wh+" - "+re+lg.gT("Help_Clear"));
		}
		if(p.hasPermission("bchest.invsee")|p.getName().equalsIgnoreCase(name())){
			p.sendMessage(re+">> "+aq+"/"+base+" "+lg.gT("Help_InvSee_CMD")+wh+" - "+re+lg.gT("Help_InvSee"));
		}
		if(p.hasPermission("bchest.reload")|p.getName().equalsIgnoreCase(name())){
			p.sendMessage(re+">> "+aq+"/"+base+" Reload"+wh+" - "+re+"Reload Your Chest");
		}
		p.sendMessage(re+">> "+aq+"/"+base+" "+lg.gT("Help")+" "+wh+" - "+re+lg.gT("Help_Open"));
		p.sendMessage(re+">>");
		p.sendMessage(re+">> "+aq+lg.gT("Help_Perms"));
	}
	
	/**
	 * ActionBar Loading Animation Tick
	 * 
	 * 
	 */
	private void loader(){
		loader+=loader.substring(0,3);
		loader=loader.substring(3,loader.length());
	}
	
	/**
	 * Returns Loader Animation
	 * 
	 * 
	 */
	private String getLoader(){
		return loader;
	}
	
	/**
	 * The Method Name Stands For Helper Top.
	 * Since This Design Is Used Twice, It Has
	 * Been Made Into A Very Simple Method
	 * 
	 * @return
	 * 
	 * 
	 */
	private String hT(){
		return re+"╒"+fact.draw("═", 32, "");
	}
	
	/**
	 * The Method Name Stands For Helper Bottom.
	 * Since This Design Is Used Twice, It Has
	 * Been Made Into A Very Simple Method
	 * 
	 * @return
	 * 
	 * 
	 */
	private String hB(){
		return re+"╘"+fact.draw("═", 32, "");
	}
	
	/**
	 * This Method Returns The No Permission
	 * Message
	 * 
	 * 
	 */
	public String nope(){
		return bl+bo+it+"["+ye+bo+it+" ! "+bl+bo+it+"] "+wh+bo+"[ "+re+bo+lg.gT("No_Permission")+wh+bo+" ]";
	}
	
	/**
	 * Return Reference To The Settings
	 * 
	 * 
	 */
	public Settings getSettings(){
		return settings;
	}
	
	/**
	 * Set A Particular Option
	 *
	 * 
	 */
	private Sound setOption(String name, String lore, int[] pos){
		if(lore.equalsIgnoreCase(bl+"#OK")){
			return settings.setOkButton(name,pos[0]);
		}else if(lore.equalsIgnoreCase(bl+"#NO")){
			return settings.setNoButton(name,pos[1]);
		}else if(lore.equalsIgnoreCase(bl+"#TRASH")){
			return settings.setTrashButton(name,pos[2]);
		}else if(lore.equalsIgnoreCase(bl+"#OPEN")){
			return settings.setChestOpenSound(name,pos[3]);
		}else if(lore.equalsIgnoreCase(bl+"#SCROLL")){
			return settings.setScrollSound(name,pos[4]);
		}else if(lore.equalsIgnoreCase(bl+"#DESTROY")){
			return settings.setTrashSound(name,pos[5]);
		}
		return Sound.valueOf((gFSV().contains("1.9")|gFSV().contains("1.10")?"ENTITY_":"")+"VILLAGER_DEATH");
	}
	
	/**
	 * Clears A Contents Of A Player's Chest
	 * 
	 * 
	 */
	private int clearChest(String name, Player p){
		try{
			Chest chest = findChest(name);
			if(!chest.isLoading()){
				chest.clearChest(0);
				p.sendMessage(gr+bo+lg.gT("Chest_Cleared"));
			} else {
				p.sendMessage(re+bo+lg.gT("Access_Error"));
			}
		} catch(Exception e1){
			p.sendMessage(re+bo+lg.gT("Player_Not_Found"));
		}
		return 0;
	}
	
	/**
	 * Allows You To Go Through Someone's Chest.
	 * Meant For Staff To Make Sure That A Player
	 * Isn't Holding Onto Items That They Shouldn't
	 * Have.
	 * 
	 * 
	 */
	private void watchChest(String name, Player p){
		try{
			Chest other = findChest(name);
			Chest yours = findChest(p.getName());
			if((!other.isLoading())&(!yours.isLoading())){
				yours.setInvSeeChest(other);
				p.openInventory(other.openChest());
			} else {
				p.sendMessage(re+bo+lg.gT("Access_Error"));
			}
		}catch(Exception e1){
			p.sendMessage(re+bo+lg.gT("Player_Not_Found"));
		}
	}
	
	/**
	 * Adds A Friend To A Player's Chest.
	 * Give That Friend Access To It's Contents.
	 * 
	 * 
	 */
	private void addFriend(String name, Player p){
		Chest chest = findChest(p.getName());
		try{
			if(chest.getFriends().size()<5){
				Player newFriend = Bukkit.getPlayer(name);
				if(!chest.isLoading()){
					for(Friend friend: chest.getFriends()){
						if(friend.getUUID().equalsIgnoreCase(newFriend.getUniqueId().toString())){
							p.sendMessage(re+bo+"\n>> "+lg.gT("Already_Friends"));
							return;
						}
					}
					chest.addFriend(newFriend);
					p.sendMessage(gr+bo+lg.gT("Friend_Added"));
				} else {
					p.sendMessage(re+bo+lg.gT("Access_Error"));
				}
			} else {
				p.sendMessage(re+bo+lg.gT("Max_Friends"));
			}
		}catch(Exception e1){p.sendMessage(re+bo+lg.gT("Player_Not_Found"));}
	}
	
	/**
	 * Removes A Chest From A Player's Chest.
	 * Revokes Access To That Chest From Former Friend.
	 * 
	 * 
	 */
	private void removeFriend(String name, Player p){
		Chest chest = findChest(p.getName());
		try{
			String friend = Bukkit.getPlayer(name).getUniqueId().toString();
			if(!chest.isLoading()){
				for(Friend f:chest.getFriends()){
					if(f.getUUID().equalsIgnoreCase(friend)){
						chest.getFriends().remove(f);
						p.sendMessage(gr+bo+"\n>> "+lg.gT("Friend_Removed"));
						return;
					}
				}
				p.sendMessage(re+bo+"\n>> "+lg.gT("Friend_Not_Found"));
			} else {
					p.sendMessage(re+bo+"\n>> "+lg.gT("Access_Error"));
			}
		}catch(Exception e1){}
	}
	
	/**
	 * Opens Your Friends Chest For You
	 * To Use.
	 * 
	 * 
	 */
	private void openFriendChest(String name, Player p){
		Chest chest = findChest(p.getName());
		Chest other = findChest(name);
		if(!chest.isLoading()){
			boolean found = false;
			for(Friend friend:other.getFriends()){
				if(friend.getName().equalsIgnoreCase(p.getName())){
					found = true;
				}
			}
			if(found == true){
				chest.setInvSeeChest(other);
				p.sendMessage(gr+bo+lg.gT("Friend_Access_Approved"));
				p.openInventory(findChest(p).openChest());
			} else {
				p.sendMessage(re+bo+lg.gT("Friend_Access_Declined"));
			}
		} else {
			p.sendMessage(re+bo+lg.gT("Access_Error"));	
		}
	}
	
	/**
	 * Opens Your Chest For You To Use.
	 * 
	 * 
	 */
	private void openYourChest(Player p){
		Chest yours = findChest(p.getName());
		if(!yours.isLoading()){
			yours.setInvSeeChest(yours);
			yours.getView().refresh(yours.getSlot());
			p.openInventory(yours.openChest());
			p.playSound(p.getLocation(), settings.getChestOpenSound(), 2, 0.5f);
		}
	}
	
	/**
	 * Called When The Plugin Is Being Disabled
	 * 
	 * 
	 */
	public void shutdown(){
		try{
			saveSettings();
		}catch(Exception e1){}
		try{
			info.stop();
			info=null;
		} catch(Exception e1){}
		for(Player p:Bukkit.getOnlinePlayers()){
			try{
				String title = p.getOpenInventory().getTopInventory().getTitle();
				Chest c = findChest(p);
				if((title.equalsIgnoreCase(bl+bo+(c.getName().length()>12?c.getName().substring(0,12):c.getName())+lg.gT("User_Chest")))|(title.equalsIgnoreCase(bl+bo+lg.gT("Admin_Title")))|(title.equalsIgnoreCase(bl+bo+lg.gT("Trash_Title")))|(title.equalsIgnoreCase(bl+bo+lg.gT("Empty_Trash")))|(title.equalsIgnoreCase(bl+bo+lg.gT("Scroll_Adj_Title"))|(title.equalsIgnoreCase(bl+bo+"[BTC] Maintenance")))){
					p.closeInventory();
					shutDownMessage(p);
				}
			}catch(Exception e1){}
			bar.sendActionBar(p, ye+bo+"The Software Is Being Shutdown...");
		}
		
		for(UUID u:chests.keySet()){
			write(chests.get(u),false);
		}
	}
	
	/**
	 * Remove Chest From Memory
	 * 
	 * 
	 */
	public void removeFromMemory(Chest chest){
		try{write(chest,false);}catch(Exception e1){}
		UUID uu = UUID.fromString(chest.UUID());
		chests.remove(uu);
		for(UUID u:getChestKeys()){
			Chest c = chests.get(u);
			try{
				if(c.getInvSeeChest().getName().equalsIgnoreCase(chest.getName())){
					c.setInvSeeChest(null);
				}
			}catch(Exception e1){}
		}
	}
	
	/**
	 * The Get GUIFactory Function
	 *  
	 */
	public GUIFactory getFact(){
		return fact;
	}
	
	/**
	 * Saves The Settings Information
	 * 
	 * 
	 */
	private void saveSettings(){
		ObjectOutputStream oop;
		try {
			oop = new ObjectOutputStream(new FileOutputStream("plugins/BottomLessChests/Settings/settings"));			
			oop.writeObject(settings);
			oop.close();
		} catch (Exception e1) {
			File file = new File("plugins/BottomLessChests/Settings");
			file.mkdirs();
			saveSettings();
		}
	}
	
	/**
	 * Loads Any Existing Settings Information
	 * 
	 * 
	 */
	public void loadSettings(){
		ObjectInputStream oip;
		try {			
			File file = new File("plugins/BottomLessChests/Settings/settings");
			long sz = file.length();
			if((sz>1)&&(sz<50)){
				settings = new Settings(this);
			} else {
				oip = new ObjectInputStream(new FileInputStream(file));			
				settings = (Settings)oip.readObject();
				oip.close();
			}
		} catch (Exception e1) {
			File file = new File("plugins/BottomLessChests/Settings");
			file.mkdirs();
		}
	}
	
	/**
	 * Software Startup Requirements
	 * 
	 * Checks To Make Sure That This
	 * Server We Are Trying To Run On Is
	 * Suitable.
	 * 
	 */
	private boolean suReq(){
		String s = gFSV();
		if(!(s.contains(ver_supported))){
			try{
				throw new InvalidServerException(re+"This Server Is Not Running "+ver_supported);
			}catch(Exception e1){}
			this.getServer().getPluginManager().disablePlugin(Main.getPlugin(Main.class));
			return false;
		}
		return true;
	}
	
	/**
	 * The method name stands for
	 * getFormattedServerVersion....
	 * 
	 * But I really don't feel like
	 * typing that much when I got to call this. ^_^
	 * 
	 * @return
	 */
	public String gFSV(){
		String vers = Bukkit.getServer().getVersion();
		return (vers.contains(ver_supported)?ver_supported:"No Clue -_-");
	}
	
	/**
	 * Returns Information About The Software
	 * 
	 */
	public String getInfo(String what){
		try{
			PluginDescriptionFile f = JavaPlugin.getPlugin(Main.class).getDescription();
			return what.equalsIgnoreCase("Name")?f.getName():what.equalsIgnoreCase("Version")?f.getVersion():(what.equalsIgnoreCase("Author")?f.getAuthors().get(0):"");
		}catch(Exception e1){}
		return "";
	}
	
	
	/**
	 * All Plugin Systems Are Started Here
	 * 
	 * 
	 */
	public void startup(){
		try{
			startup.stop();
			startup = null;
			loadSettings();
			if(!loadLanguage()){Bukkit.broadcastMessage(re+bo+"[BCHESTS] CRITICAL ERROR - Lang Files Missing!");this.getServer().getPluginManager().disablePlugin(Main.getPlugin(Main.class));return;}
			Bukkit.getServer().getPluginManager().registerEvents(this, this);
			info.start(1);
			timeStamps.start(1728000);
			message();
			win = new MaintenanceWindow(this);
		}catch(Exception e1){}
	}
	
	/**
	 * The Load Language Function:
	 * 
	 * Called To Load The Text In The Preferred
	 * Language. Called Again By Settings Menu When
	 * The User Wants A Different Language (Lang Files
	 * Will Be Required For Translation)
	 * 
	 */
	public boolean loadLanguage(){
		boolean foundLangFiles = false;
		try{
			File file = new File("plugins/BottomLessChests/Language");
			if(!file.exists()){
				file.mkdir();
			}
			LANGUAGE_FOLDER = file;
			DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(LANGUAGE_FOLDER.getAbsolutePath()));
			langs.clear();
			for(Path filee:stream){
				langs.add(filee.toFile());
			}
			foundLangFiles = ((langs.size()>0)?true:false);
			if(foundLangFiles){
				try{lg = new Language(langs.get(settings.getLanguageSetting()));} catch(Exception e1){settings.advanceLanguageSetting(this);loadLanguage();}
				adjuster = new ScrollbarAdjuster(this);
				conf = new Confirmation(this);
				menu = new AdminMenu(this);
				loader = fact.draw(bl+"▍", 50, "")+fact.draw(ye+"▍", 5, "");
				for(UUID u:getChestKeys()){
					chests.get(u).reloadView();
				}
			}
		}catch(Exception e1){
			/**
			 * Where Are Your Damn Language Files?
			 * 
			 * Smacks Palm Onto Face, Or In Your
			 * Primitive Non-Programming Language Grammer,
			 * It's Called A "Facepalm" ^_^
			 * 
			 */
		}
		return foundLangFiles;
	}
	
	/**
	 * The Get Language Function:
	 * 
	 * Allows Other Objects To Have
	 * Access To The Language Files
	 * 
	 */
	public Language gL(){
		return lg;
	}
	
	/**
	 * The Get Active Language Files Function:
	 * 
	 */
	public ArrayList<File> getActionLanguageFiles(){
		return langs;
	}
	
	
	
	/**
	 * Get Language Folder Function
	 * 
	 */
	public File getLanguageFolder(){
		return LANGUAGE_FOLDER;
	}
	
	/**
	 * Checks Player Chest TimeStamps Once Per Day.
	 * 
	 * If A User's Chest File Is Inactive For 6 Months,
	 * I Think It's Reasonable To Say That The User Is Not
	 * Going To Return To The Server.
	 * 
	 * 
	 */
	public void timeCheck(){
		DirectoryStream<Path> stream;
		String dp = "Plugins/BottomLessChests/Chests";
		try{
			stream = Files.newDirectoryStream(Paths.get(dp+"/UUID"));
			for (Path p:stream) {
				YamlConfiguration Config = YamlConfiguration.loadConfiguration(p.toFile());
				if(System.currentTimeMillis()-Config.getLong("C.TimeStamp")>15000000000L){
					p.toFile().delete();
				}
			}
			stream = Files.newDirectoryStream(Paths.get(dp+"/Name"));
			for (Path p:stream) {
				YamlConfiguration Config = YamlConfiguration.loadConfiguration(p.toFile());	
				if(System.currentTimeMillis()-Config.getLong("C.TimeStamp")>15000000000L){
					p.toFile().delete();
				}
			}
		}catch(Exception e1){}
	}
	
	/**
	 * WARNING - If You Change This...
	 * Your Server Will BURST INTO FLAMES!!!!!!
	 * 
	 * @return
	 * 
	 * 
	 */
	public String name(){
		return "VirusBrandon";
	}
	
	/*
	 * © 2016 Brandon Mueller
	 * DO NOT DE-COMPILE THIS SOFTWARE OR ATTEMPT ANY FORM OF REVERSE ENGINEERING!
	 */
}
