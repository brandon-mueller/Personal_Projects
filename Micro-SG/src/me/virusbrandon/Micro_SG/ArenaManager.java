package me.virusbrandon.Micro_SG;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

public class ArenaManager implements java.io.Serializable,Listener{
	private static final long serialVersionUID = 19335933;
	private ArrayList<Arena> templates = new ArrayList<>();
	private ArrayList<Arena> ingame = new ArrayList<>();
	private ArrayList<Player> queue = new ArrayList<>();
	private ArrayList<Player> toRefresh = new ArrayList<>();
	private ArrayList<Player> toClear = new ArrayList<>();
	private ArrayList<Player> toGiveChest = new ArrayList<>();
	private ArrayList<Player> loadQueue = new ArrayList<>();
	private ArrayList<Stats> stats = new ArrayList<>();
	private ArrayList<ChestItem> items = new ArrayList<>();
	private ArrayList<String> console = new ArrayList<>();
	private ArrayList<Firework> fw = new ArrayList<>();
	private int ox,oy,oz,ow; //Arena Manager Origin Point
	private int jx,jy,jz,jw;float jYaw,jPitch;
	private int lx,ly,lz,lw;float lpitch,lyaw;
	private int temp = 0,/*t1=0,*/t2=0,t3=0,t4=0,t5=0,t6=0,t7=0,t9=0;
	private float progress = 0,size = 0;
	private boolean isBuilding = false;
	private Random r =  new Random();
	private DecimalFormat df = new DecimalFormat("##.##");
	private String gr=ChatColor.GREEN+"",go=ChatColor.GOLD+"",bo=ChatColor.BOLD+"",aq=ChatColor.AQUA+"",wh=ChatColor.WHITE+"",re=ChatColor.RED+"",bl=ChatColor.BLACK+"",ye=ChatColor.YELLOW+"",ga=ChatColor.GRAY+"";
	private BlockNode head;
	private String wn;   //World Name Is Saved For World Re-Alignment Purposes
	private Timer build,manager,inGame,scores,shop,admin,updInv,clrInv,gvChest,loadT,clrcnsl,firewrks,boom;
	private int maxPlayers  = 4; /*Defaults To 4*/
	private Inventory inv,inv2,inv3,inv4;
	private int[] middle,icon,i;
	private List<String> l;
	private DyeColor[] d;
	private int i1,i2;
	private float p1;
	private String ip = "Set Your Server IP Here, Do /SG setIp <IP>";
	private int dmSpd=1,dropFreq=90,mult=1;
	private DateFormat dateFormat,cpyr;

	@EventHandler
	public void connect(PlayerJoinEvent e){
		Player p = e.getPlayer();
		cReport(p.getName()+" Connected To The Server!");
		try {
			p.teleport(getLobby());
		} catch(Exception e1){}
		setMaxPlayers(figureMax());
	}
	
	@EventHandler
	public void leave(PlayerQuitEvent e){
		Player p = e.getPlayer();
		cReport(p.getName()+" Left The Server!");
		setMaxPlayers(figureMax());
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void click(InventoryClickEvent e){
		try{
			String name = e.getCurrentItem().getItemMeta().getDisplayName();
			Player p = (Player)e.getWhoClicked();
			if(p.hasPermission("SG.ADMIN")){
				if(e.getInventory().getTitle().equalsIgnoreCase(bl+bo+"Best Scores!")){
					if(!e.getCurrentItem().getItemMeta().getLore().get(e.getCurrentItem().getItemMeta().getLore().size()-2).equalsIgnoreCase("#Menu-Item")){
						String ss=e.getCurrentItem().getItemMeta().getDisplayName();
						dSM(p,ss);
					}	
				}
				if(e.getInventory().getTitle().equalsIgnoreCase(bl+bo+"Delete Player Stats?")){
					try{
						if(e.getCurrentItem().getType()==Material.EMERALD_BLOCK){
							dS(p,e.getCurrentItem().getItemMeta().getLore().get(e.getCurrentItem().getItemMeta().getLore().size()-2));
						}else if(e.getCurrentItem().getType()==Material.REDSTONE_BLOCK){
							p.openInventory(inv2);
						}
					}catch(Exception e1){}
				}
				if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(gr+bo+"Micro SG!")){
					p.openInventory(inv4);
				}
			}
			if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(re+bo+"LEAVE")){
				leave(p);
			}
			try{
				switch(e.getCurrentItem().getType()){
					case DOUBLE_PLANT:
						p.openInventory(inv3);
					break;
					case BOOK:
						p.openInventory(inv2);
					break;
					case SIGN:
						if(!toClear.contains(p)){
							addToClear(p);
							addToRefresh(p);
							if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(wh+bo+"✦ Stats")){
								showStats(p);
							}
						}
					break;
					case NETHER_STAR:
						if(name.equalsIgnoreCase(wh+bo+"✦ Games Running"))
						p.openInventory(inv);
						
					break;
					case ENDER_PEARL:
						e.setCancelled(true);
						if(e.getCurrentItem().getItemMeta().getDisplayName().contains(wh+bo+"LEAVE")){
							for(Arena a:ingame){
								a.leave(p);
								p.closeInventory();
							}
							cReport(p.getName()+"Left!");
							addToRefresh(p);
						}
					break;
					case NAME_TAG:
					try{
						if(!toRefresh.contains(p)){						
							Stats s = checkProfile(p);
							int price=0;
							for(int x = 0;x<i.length;x++){
								if(e.getCurrentItem().getAmount()==i[x]){
									price=(int)((i[x]*300)-((i[x]-1)*10));a(p);
									if(s.getPoints()>=price){
										s.setPoints(s.getPoints(),-price,1,false);
										s.setElixer(s.getElixers(),i[x]);
										p.playSound(p.getLocation(), Sound.LEVEL_UP, 1, 2.0F);
										p.sendMessage(sd()+s()+"- Purchase Successful!");
										p.sendMessage(sd()+w()+"- Received: "+i[x]+" Revival Elixer(s)");
										p.sendMessage(sd()+e()+"- For: "+price+" Points!");								
										cReport(p.getName()+" Purchased Some Revival Elixers!");
										if(!toClear.contains(p)){
											addToClear(p);
											addToRefresh(p);
										}
									} else {
										NO(p);
									}b(p);
								}
							}
						}
							
					}catch(Exception e1){}
					case COMMAND:
						if(e.getCurrentItem().getItemMeta().getDisplayName().contains("Click To Clear")){
							try{
								if(t7==0){
									clrcnsl.start(0);	
								}
							}catch(Exception e1){}
						}
					break;
					default:
				}
				if(name.contains(ye+bo+"Dark Matter")|name.contains(ye+bo+"Drop Package")|name.contains(ye+bo+"Bonus Point")){
					if(e.getCurrentItem().getType()!=Material.NETHER_STAR){
						p.playSound(p.getLocation(),Sound.CLICK,1,2.0f);
						int str=(Integer.parseInt(e.getCurrentItem().getItemMeta().getLore().get(e.getCurrentItem().getItemMeta().getLore().size()-4).substring(10,e.getCurrentItem().getItemMeta().getLore().get(e.getCurrentItem().getItemMeta().getLore().size()-4).length())));
						if(name.contains(ye+bo+"Dark Matter")){
							setDmSpd(str);
						}else if(name.contains(ye+bo+"Drop Package")){
							setDropFreq(str);
						}else if(name.contains(ye+bo+"Bonus Point")){
							setMult(str);
						}
					}
				}
			}catch(Exception e1){}
			try{
				if(e.getCurrentItem().getItemMeta().getLore().get(e.getCurrentItem().getItemMeta().getLore().size()-2).equalsIgnoreCase("#Random")){
					Stats s = checkProfile(p);
					if(checkProfile(p).getPoints()>=250){
						if(s.getItems().size()<5){
							try{
								s.addStartItem(e.getCurrentItem().getTypeId());
							}catch(Exception e1){
								s.initItems();
								s.addStartItem(e.getCurrentItem().getTypeId());
							}
							s.setPoints(checkProfile(p).getPoints(), -250,1,false);
							p.playSound(p.getLocation(), Sound.LEVEL_UP, 1, 2.0F);
							p.sendMessage("\n"+hT());
							p.sendMessage(sd()+w()+">> You Picked Up "+ e.getCurrentItem().getType());
							p.sendMessage(sd()+e()+">> You'll Be Able To Use That Next Game!");
							p.sendMessage(hB());
							cReport(p.getName()+" - Mystery Box");
							addToRefresh(p);
						}else{
							a(p);NOO(p);b(p);		
						}
					} else {
						p.sendMessage("\n"+hT());
						NO(p);p.sendMessage(hB());				
					}
				}
			}catch(Exception e1){}
			try{
				if((e.getCurrentItem().getType()==Material.BLAZE_ROD)&(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(wh+bo+"Dark Matter FlashBang!!"))){
					Stats s = checkProfile(p);
					if(checkProfile(p).getPoints()>=2000){
						if(s.getItems().size()<5){
							try{
								s.addStartItem(e.getCurrentItem().getTypeId());
							}catch(Exception e1){
								s.initItems();
								s.addStartItem(e.getCurrentItem().getTypeId());
							}
							s.setPoints(checkProfile(p).getPoints(), -2000,1,false);
							p.playSound(p.getLocation(), Sound.AMBIENCE_THUNDER, 1, .5F);
							p.sendMessage("\n"+wh+bo+">> You've Equipted A Dark Matter Flash Bang!");
							p.sendMessage(wh+bo+">> Good For Use Next Game!");
							cReport(p.getName()+" Bought Some Flash Bangs!");
						}else{
							a(p);NOO(p);b(p);		
						}
					}else{
						a(p);NO(p);b(p);
					}
					addToRefresh(p);
				}
			}catch(Exception e1){}
			try{
				if(e.getCurrentItem().getType()==Material.CHEST){
					if(e.getAction()!=InventoryAction.PICKUP_HALF){
						if(e.getCurrentItem().getItemMeta().getDisplayName().contains(wh+bo+"IN-GAME")){
							p.openInventory(ingame.get(Integer.parseInt(e.getCurrentItem().getItemMeta().getDisplayName().substring(0, 1))).GUI().spec());
						}
					} else{
						if(e.getWhoClicked().hasPermission("SG.ADMIN")){
							ingame.get(Integer.parseInt(e.getCurrentItem().getItemMeta().getDisplayName().substring(0, 1))).finish();
						}
					}
				}
			}catch(Exception e1){}
			try{
				if(e.getCurrentItem().getType()==Material.IRON_CHESTPLATE){
					if(checkInGame(p)>-2){
						int i = 0;
						for(Arena a:ingame){
							a.leave(p);
						}
						for(Arena a:ingame){
							Player pl=a.findPlayerByName(name);
							if(pl!=null){
								if(a.getSpecList().contains(p.getName())){
									a.getSpecList().remove(p.getName());
								}
								a.spectate(p);
								p.teleport(pl.getLocation());
								addToClear(p);addToRefresh(p);
								p.sendMessage("\n"+hT());
								p.sendMessage(sd()+s()+">> Now Spectating On Arena "+i);
								p.sendMessage(sd()+w()+">> Map: "+a.getName());
								p.sendMessage(sd()+e()+">> Watching Player: "+name);
								p.sendMessage(sd());p.sendMessage(hB());
								cReport(p.getName()+" Started Spectating");
								break;
							}
							i++;
						}
					}
				}
			}catch(Exception e1){}
			if(e.getCurrentItem().getItemMeta().getLore().contains("#Micro-SG")){
				e.setCancelled(true);
			}
		}catch(Exception e1){}
	}
	
	@EventHandler
	public void dropItem(PlayerDropItemEvent e){
		try{
			if(checkInGame(e.getPlayer())==0|e.getItemDrop().getItemStack().getItemMeta().getLore().contains("#Micro-SG")){
				e.setCancelled(true);
			}
		}catch(Exception e1){}
	}
	
	@EventHandler
	public void form(EntityChangeBlockEvent e){
		if(e.getTo()==Material.CHEST){
			Location l = e.getBlock().getLocation();
			l = new Location(l.getWorld(),l.getBlockX()-1,l.getBlockY(),l.getBlockZ()-1);
			for(int x = l.getBlockX(),t=0;t<3;t++,x++){
				for(int z = l.getBlockZ(),tt=0;tt<3;tt++,z++){
					l.getWorld().getBlockAt(new Location(l.getWorld(),x,l.getBlockY()-1,z)).setType(Material.GOLD_BLOCK);
				}
			}
		} else {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void blockPlace(BlockPlaceEvent e){
		Player p = e.getPlayer();
		if((e.getBlock().getType()!=Material.CHEST)&(e.getBlock().getType()!=Material.LAPIS_ORE)&(e.getBlock().getType()!=Material.WALL_SIGN)){
			e.setCancelled(true);
		}else{
			Location l = e.getBlock().getLocation();
			if(l.getWorld().getBlockAt(l.getBlockX(),l.getBlockY()-1,l.getBlockZ()).getType()==Material.GOLD_BLOCK){
				a(p);p.sendMessage(sd()+e()+">> You Can't Place Revive Crates Here!");b(p);
				e.setCancelled(true);
			}else{
				if(e.getBlock().getType()!=Material.LAPIS_ORE){
					addToClear(p);
				}
			}
		}
	}

	@EventHandler
	public void damage(EntityDamageByEntityEvent e){
		if(e.getEntity() instanceof Player){
			if(checkInGame((Player)e.getEntity())<-2){
				e.setCancelled(true);
			}	
		}
	}
	
	@EventHandler 
	public void blockBreak(BlockBreakEvent e){
		if(checkInGame(e.getPlayer())<-2){
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void chuckUnload(ChunkUnloadEvent e){
		if(e.getChunk().getWorld()==Bukkit.getWorlds().get(ow)){
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void interact(PlayerInteractEvent e){
		Player p = e.getPlayer();
		try{
			switch(e.getItem().getType()){
				case DOUBLE_PLANT:
					p.openInventory(inv3);
				break;
				case BOOK:
					p.openInventory(inv2);
				break;
				case SIGN:
					showStats(p);
				break;
				case NETHER_STAR:
					p.openInventory(inv);
				break;
				case ENDER_PEARL:
					e.setCancelled(true);
					if(e.getItem().getItemMeta().getDisplayName().contains(wh+bo+"LEAVE")){
						for(Arena a:ingame){
							a.leave(p);
						}
					}
				break;
				case TNT:
					if(checkInGame(p)<0){
						TNT(p);
					} else {
						WHY(p);
					}
				break;
				case FIREBALL:
					if(checkInGame(p)<0){
						FIREBALL(p);
					} else {
						WHY(p);
					}
				break;
				case FEATHER:
					if(checkInGame(p)<0){
						JUMP(p);
					} else {
						WHY(p);
					}
				break;
				default:
			}
		}catch(Exception e1){}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerCommand(PlayerCommandPreprocessEvent e) {
		Player pla = e.getPlayer();
		try{
			if((e.getMessage().equalsIgnoreCase("/Leave"))){
				leave(pla);
				e.setCancelled(true);
			}
		}catch(Exception e5){}
	}
	
	ArenaManager(Location l){
		this.ox = l.getBlockX();
		this.oy = l.getBlockY();
		this.oz = l.getBlockZ();
		for(int a = 0;a<Bukkit.getWorlds().size();a++){
			if(l.getWorld().getName().equalsIgnoreCase(Bukkit.getWorlds().get(a).getName())){
				this.ow = a;
				this.wn = l.getWorld().getName();
			}
		}
		Bukkit.getServer().getPluginManager().registerEvents(this,Main.getPlugin(Main.class));
	}
	
	public int saveTemplate(ArrayList<Selection> s,String name){
		try{
			int sX = (Math.min(s.get(0).getX(), s.get(1).getX())-5);
			int sY = (Math.min(s.get(0).getY(), s.get(1).getY())-5);
			int sZ = (Math.min(s.get(0).getZ(), s.get(1).getZ())-5);
			int eX = (Math.max(s.get(0).getX(), s.get(1).getX())+5);
			int eY = (Math.max(s.get(0).getY(), s.get(1).getY())+5);
			int eZ = (Math.max(s.get(0).getZ(), s.get(1).getZ())+5);
			BlockNode b = new BlockNode(new TBlock(sX,sY,sZ,s.get(0).getWorld().getBlockAt(s.get(0).loc)),null);
			BlockNode head = b;
			for(int x = 0, xx = sX;x<(eX-sX)+1;x++,xx++){
				for(int y = 0, yy = sY;y<(eY-sY)+1;y++,yy++){
					for(int z = 1,zz = sZ+1;z<(eZ-sZ)+1;z++,zz++){
						b = b.addNodeAfter(new TBlock((ox+x),(oy+y),(oz+z),s.get(0).getWorld().getBlockAt(xx,yy,zz)));
					}
				}
			}
			templates.add(new Arena(head,this,name,null,true));
			if(templates.size()==1){init();}
			return 0;
		}catch(Exception e1){
			return -1;
		}
	}
	
	public int buildArena(int t){
		if(isBuilding == false){
			try{
				head = templates.get(t).getArena();
				size = BlockNode.listLength(head);
				isBuilding = true;
				try{
					temp = ingame.get(ingame.size()-1).getSlotNumber();
				}catch(Exception e1){temp = 0;}
				progress = 0;build = new Timer(this,0);
				build.start(1);return 0;
			}catch(Exception e1){return -2;/*Exception In User Entry (Arena Doesn't Exist)*/ }
		} else{
			return -1;
		}
	}
	
	@SuppressWarnings("deprecation")
	public void ArenaBuildTick(){
		for(int x = 0;x<2500;x++){
			if(head!=null){
				Bukkit.getWorlds().get(ow).getBlockAt(new Location(Bukkit.getWorlds().get(ow),head.data.X()+(temp*150),head.data.Y(),head.data.Z())).setTypeId(head.data.getType());
				Bukkit.getWorlds().get(ow).getBlockAt(new Location(Bukkit.getWorlds().get(ow),head.data.X()+(temp*150),head.data.Y(),head.data.Z())).setData(head.data.getData());
				head = head.link;progress++;
			} else {
				build.stop();build = null;
				head = null;isBuilding = false;
				break;
			}
		}
	}
	
	public String getProgress(){
		try{
			return df.format((progress/size)*100)+"%";
		}catch(Exception e1){
			df = new DecimalFormat("##.##");
			return df.format((progress/size)*100)+"%";
		}
	}
	
	@SuppressWarnings("deprecation")
	public void managerTick(){
		try{
			for(Player pa:Bukkit.getOnlinePlayers()){
				if((calcDist(pa.getLocation(),getJoinSpot())<7)&(!queue.contains(pa))&pa.isOnGround()==true){
					join(pa);
				}
			}
		} catch(Exception e1){}
		
		try{
			if(queue.size()>=maxPlayers){
				int arena = r.nextInt(templates.size());
				if(templates.get(arena).getState()==true){
					if(templates.get(arena).getSpawnPoints().size()>=maxPlayers){
						if(getLobby()!=null){
							if(buildArena(arena)==0){
								cReport("Match Begin - "+templates.get(arena).getName());
								ingame.add(new Arena(templates.get(arena)));
								ingame.get(ingame.size()-1).setSlotNumber(firstFreeSlot());
								temp=ingame.get(ingame.size()-1).getSlotNumber();
								while(ingame.get(ingame.size()-1).getPlayers().size()<maxPlayers){
									Player p = queue.get(0);
									addToClear(p);resetXP(p);
									ingame.get(ingame.size()-1).getPlayers().add(p);
									p.setGameMode(GameMode.SURVIVAL);
									a(p);p.sendMessage(sd()+s()+">> The Game Will Start Shortly");
									p.sendMessage(sd()+w()+">> Arena: "+templates.get(arena).getName());
									queue.remove(0);
								}
							}
						}
					}
				}
			}
		}catch(Exception e1){}
		for(int i = 0;i<ingame.size();i++){
			try{ingame.get(i).tick();}catch(Exception e1){}
		}
		try{
			if(toClear.size()>0 & clrInv==null){
				clrInv=new Timer(this,8);
				clrInv.start(1);
			}
		}catch(Exception e1){toClear=new ArrayList<Player>();}
		try{
			if(toRefresh.size()>0 & updInv==null){
				updInv=new Timer(this,7);
				updInv.start(2);
			}
		}catch(Exception e1){toRefresh=new ArrayList<Player>();}
		try{
			if(toGiveChest.size()>0 & gvChest==null){
				gvChest=new Timer(this,9);
				gvChest.start(6);
			}
		}catch(Exception e1){toGiveChest=new ArrayList<Player>();}
		try{
			if(loadQueue.size()>0 & loadT==null){
				loadT=new Timer(this,11);
				loadT.start(5); /*Keep An Eye Out On This, Interval Change May Cause Bugs!*/
			}
		}catch(Exception e1){loadQueue=new ArrayList<Player>();}
		if(queue.size()>0){
			music();manageHunger();
		} else if(i2!=0){
			i2=0;
		}
		t5++;
		if(t5%600==0){
			String message="We Need "+(maxPlayers-queue.size())+" More Player(s)";
			if(isBuilding==true){
				message="Waiting On Next Arena";
				if(queue.size()>=maxPlayers){
					message="Players Ready, "+message;
				}
			}
			for(int x=0;x<queue.size();x++){
				queue.get(x).playSound(queue.get(x).getLocation(), Sound.CHICKEN_EGG_POP, 2, 2);
				queue.get(x).sendMessage("\n"+hT()+"\n"+sd()+re+bo+" - You Are Currently "+wh+bo+(x+1)+getSuffix(x+1)+re+bo+" In Queue\n"+sd()+go+bo+" - Each Arena Holds "+maxPlayers+" Tributes\n"+sd()+ye+bo+" - "+message+"!\n"+sd()+gr+bo+" - The Match Will Start Soon!\n"+sd()+wh+bo+" - You Can Do /Leave To Leave\n"+hB());
			}
		}
	}
	
	public int firstFreeSlot(){
		boolean found = false;
		for(int x = 0;x<100;x++){
			found=false;
			for(Arena a:ingame){
				if(a.getSlotNumber()==x){
					found=true;
					break;
				}
			}
			if(found==false){
				return x;
			}
		}
		return -69;
	}
	
	public Location getLobby(){
		try{
			if(lw==0&lx==0&ly==0&lz==0){
				return null;
			}else{
				return new Location(Bukkit.getWorlds().get(lw),lx,ly,lz,lyaw,lpitch);	
			}
		}catch(Exception e1){return null;}
	}
	
	public void setLobby(Location l){
		this.lx = l.getBlockX();
		this.ly = l.getBlockY();
		this.lz = l.getBlockZ();
		this.lpitch = l.getPitch();
		this.lyaw = l.getYaw();
		for(int a = 0;a<Bukkit.getWorlds().size();a++){
			if(Bukkit.getWorlds().get(a).getName().equalsIgnoreCase(l.getWorld().getName())){
				this.lw = a;
			}
		}
	}
	
	public Location getJoinSpot(){
		if(jw==0&jx==0&jy==0&jz==0){
			return null;
		}else{
			return new Location(Bukkit.getWorlds().get(jw),jx,jy,jz,jYaw,jPitch);	
		}
	}
	
	public void setJoinSpot(Location l){
		this.jx = l.getBlockX();
		this.jy = l.getBlockY();
		this.jz = l.getBlockZ();
		this.jYaw = l.getYaw();
		this.jPitch = l.getPitch();
		for(int a = 0;a<Bukkit.getWorlds().size();a++){
			if(Bukkit.getWorlds().get(a).getName().equalsIgnoreCase(l.getWorld().getName())){
				this.jw = a;
			}
		}
	}
	
	public int changeName(int i, String name){
		try{
			if(name.length()<=10){
				templates.get(i).setName(name);
				return 0;
			} else {
				return -1;
			}
		}catch(Exception e1){
			return -1;
		}
	}
	
	public int deleteArena(int i){
		try{
			templates.remove(i);
			return 0;
		}catch(Exception e1){
			return -1;
		}
	}
	
	public ArrayList<Arena> getInGame(){
		return ingame;
	}
	
	public void destroyMatch(Arena toDestroy){
		ingame.remove(toDestroy);
	}
	
	public void chkUI(int i){
		for(Player p:Bukkit.getOnlinePlayers()){
			try{
				if(Integer.parseInt(p.getOpenInventory().getTopInventory().getTitle().substring(0,1))==i){
					errCtch(p);
				}
			}catch(Exception e1){}
		}
	}
	
	public void remItems(int slot){
	    List<Entity> entList = Bukkit.getWorlds().get(ow).getEntities();
	    for(Entity current : entList){
			if (current instanceof Item){
				if(slot>-1){
					if(calcDist(current.getLocation(),ingame.get(slot).getSpawnPoints().get(r.nextInt(ingame.get(slot).getSpawnPoints().size())).getSpawnPoint(slot))<=100){
						current.remove();
					}
				} else {
					current.remove();
				}
			}
	    }
	}
	
	public void listArenas(CommandSender se){
		se.sendMessage("\n"+re+bo+"Arena Templates: ("+templates.size()+")\n"+hT());
		if(templates.size()>0){
			for(int x = 0;x<templates.size();x++){
				se.sendMessage(sd()+wh+bo+"- "+templates.get(x).getName()+" - Status: "+templates.get(x).checkStatus());
			}
		} else {
			se.sendMessage(sd()+wh+bo+"No Arena Templates Saved Yet!");
		}
		se.sendMessage(hB());
	}
	
	public ArrayList<Stats> getStats(){
		return stats;
	}
	
	protected void join(Player p){
		boolean found = false;
		for(Arena a:ingame){
			if(a.getPlayers().contains(p)){
				found = true;
				break;
			}
		}
		if(found == false){
			cReport(p.getName()+" Joined The Queue!");
			checkProfile(p);
			a(p);p.sendMessage(sd()+s()+">> Queued For Next Game!");
			p.sendMessage(sd()+w()+">> Tributes: "+(queue.size()+1)+"/"+maxPlayers);
			p.sendMessage(sd()+e()+">> Waiting For Players...");
			p.sendMessage(sd());b(p);
			for(Player pl:queue){
				a(pl);pl.sendMessage(sd()+w()+">> Tribute, "+p.getName());
				pl.sendMessage(sd()+w()+">> Has Joined!");b(pl);
			}
			queue.add(p);	
			addToClear(p);
			addToRefresh(p);
		}
	}
	
	public int addSpawnPoint(Location l, int arenaID){
		try{
			return templates.get(arenaID).addSpawnPoint(l);
		}catch(Exception e1){
			return -1;
		}
	}
	
	public void hidePlayer(Player p){
		for(Player pl:Bukkit.getOnlinePlayers()){
			pl.hidePlayer(p);
		}
	}
	
	public void showPlayer(Player p){
		for(Player pl:Bukkit.getOnlinePlayers()){
			pl.showPlayer(p);
		}
	}
	
	public String s(){ //A Success Icon
		return gr+bo+"[  "+wh+bo+"!"+gr+bo+"  ] "+wh+bo;
	}
	
	public String w(){ //A Warning Icon
		return ye+bo+"[  "+wh+bo+"!"+ye+bo+"  ] "+wh+bo;
	}
	
	public String e(){ //An Error Icon
		return re+bo+"[  "+wh+bo+"!"+re+bo+"  ] "+wh+bo;
	}
	
	public String i(){ //An Info Icon
		return aq+bo+"[  " + wh+bo+"i"+aq+bo+"  ] "+wh+bo;
	}
	
	public String sd(){
		return go+bo+"│ ";
	}
	
	public int getAMX(){
		return ox;
	}
	
	public int getAMY(){
		return oy;
	}
	
	public int getAMZ(){
		return oz;
	}
	
	public int getOW(){
		return ow;
	}
	
	public Location getOrigin(){
		return new Location(Bukkit.getWorlds().get(ow),ox,oy,oz);
	}
	
	public String getWN(){
		return wn;
	}
	
	public void setOW(int ow){
		this.ow=ow;
	}
	
	public ArrayList<Arena> getTemplates(){
		return templates;
	}
	
	public int getMaxPlayers(){
		return maxPlayers;
	}
	
	private int figureMax(){
		int i=Bukkit.getOnlinePlayers().size();
		if(i>=16){
			return 8;
		}else if((i<=15)&(i>=8)){
			return(i/2);
		}else{
			return 4;
		}
	}
	
	public int setMaxPlayers(int newMax){
		if(newMax>1 & newMax<25){
			this.maxPlayers = newMax;
			return 0;
		} else {
			return -1;
		}
	}
	
	public void setIp(String ip){
		this.ip =ip;
	}
	
	private void music(){
		i1++;i2++;
		if(i1<31){
			if((i1<13)&(i1%4==0)){
				beatOne();
			}
			if((i1>19&i1<29)&(i1%4==0)){
				beatOne();
				if(i1==28)
					beatTwo();
			}
		}else{
			i1=0;
		}
		if(i2<86){
			beatThree(i2,0);
		}else{
			if((i2>93)&(i2<170)){
				beatThree(i2,1);
			} else {
				if(i2>188){
					i2=0;i1=0;
				}
			}
		}
	}
	
	private void beatOne(){
		for(Player p:queue){
			p.playSound(p.getLocation(),Sound.NOTE_BASS,.6f,.5f);
			p.playSound(p.getLocation(),Sound.NOTE_BASS_DRUM,.2f,.5f);
		}
	}
	
	private void beatTwo(){
		for(Player p:queue){
			p.playSound(p.getLocation(),Sound.NOTE_BASS_DRUM,.4f,1.7f);
		}
	}
	
	private void beatThree(int i2,int a){
		switch(a){
			case 0:
				for(Player p:queue){
					if(i2<10){
						p1=.5f;
					}else if((i2>=10)&(i2<18)){
						p1=.64f;
					} else if((i2>=18)&(i2<26)){
						p1=.78f;
					} else if((i2>=26)&(i2<56)){
						p1=.82f;
					} else if((i2>=56)&(i2<86)){
						p1=.78f;
					}
					p.playSound(p.getLocation(), Sound.NOTE_PLING, .05f, p1);
				}
			break;
			case 1:
				for(Player p:queue){
					if(i2<104){
						p1=.5f;
					}else if((i2>=104)&(i2<112)){
						p1=.64f;
					} else if((i2>=112)&(i2<120)){
						p1=.78f;
					} else if((i2>=120)&(i2<128)){
						p1=.82f;
					} else if((i2>=128)&(i2<136)){
						p1=.78f;
					} else if((i2>=136)&(i2<146)){
						p1=1f;
					} else if((i2>=146)&(i2<152)){
						p1=1.14f;
					} else if((i2>=152)&(i2<170)){
						p1=1f;
					}
					p.playSound(p.getLocation(), Sound.NOTE_PLING, .05f, p1);
				}
			break;
		}
	}
	
	private void manageHunger(){
		for(Player p:queue){
			if(p.getFoodLevel()<20){
				p.setFoodLevel(20);
			}
		}
	}
	
	public Stats findStatsByName(String name){
		for(Stats s:stats){
			if(s.getName().equalsIgnoreCase(name)){
				return s;
			}
		}
		for(Player p:Bukkit.getOnlinePlayers()){
			if(name.equalsIgnoreCase(p.getName())){
				return checkProfile(p);
			}
		}
		return null;
	}
	
	private void showStats(Player p){
		Stats s = checkProfile(p);
		p.sendMessage("\n"+wh+bo+"Stats"+"\n"+wh+bo+s.getName()+"\n"+hT()+"\n"+sd()+gr+bo+"- Kills: "+s.getKills()+"\n"+sd()+ye+bo+"- Wins: "+s.getWins()+"\n"+go+bo+"│ - Points: "+s.getPoints()+"\n"+sd()+re+bo+"- Deaths: "+s.getDeaths()+"\n"+sd()+ga+bo+"- Revives: "+s.getRevives()+"\n"+sd()+ga+bo+"- Revival Elixers: "+s.getElixers()+"\n"+sd()+wh+bo+"- Current Standing: "+getStanding(p.getName())+"\n"+hB());
	}
	
	private String getStanding(String name){
		for(int x=0;x<stats.size();x++){
			if(stats.get(x).getName().equalsIgnoreCase(name)){
				return (x+1)+getSuffix(x+1)+"";
			}
		}
		return "-69";
	}
	
	private String getSuffix(int i){
		String suff="th";
		if(((i%100)>19)|((i%100)<10)){
			switch(i%10){
				case 1:
					suff="st";
				break;	
				case 2:
					suff="nd";
				break;
				case 3:
					suff="rd";
				break;	
			}
		}
		return suff;
	}
	
	public int getMult(Player p){
		if(p.hasPermission("SG.5x")){
			return 5;
		} else if(p.hasPermission("SG.4x")) {
			return 4;
		} else if(p.hasPermission("SG.3x")){
			return 3;
		} else if(p.hasPermission("SG.2x")){
			return 2;
		} else {return 1;}
	}
	
	public void fillChest(Location l){
		Inventory c=((Chest)l.getBlock().getState()).getInventory();
		ArrayList<Integer> i = new ArrayList<>();
		for(int x = 0;x<27;x++){
			i.add(x);
		}
		int slots;
		if(l.getWorld().getBlockAt(l.getBlockX(), l.getBlockY()-1, l.getBlockZ()).getType()==Material.GOLD_BLOCK){
			slots = 27; //Drop Crates Yield FULL Chests!
		}else{
			slots = r.nextInt(15);
		}
		for(int x = 0;x<27;x++){
			c.setItem(i.get(x),new ItemStack(Material.AIR,1));
		}
		for(;slots>0;slots--){
			int x = r.nextInt(i.size());
			c.setItem(i.get(x),getItem());
			i.remove(x);	
		}
	}
	
	public Stats checkProfile(Player p){
		boolean found = false;
		for(Stats s:stats){
			if(s.getName().equalsIgnoreCase(p.getName())){
				found = true;
				return s;
			}
		}
		if(found == false){
			stats.add(new Stats(p,this));
		}
		return stats.get(stats.size()-1);
	}
	
	public void init(){
		console=new ArrayList<>();
		inv = Bukkit.createInventory(null, 54,bl+bo + "Games Running:");
		inv2 = Bukkit.createInventory(null, 54,bl+bo + "Best Scores!");
		inv3 = Bukkit.createInventory(null, 54,bl+bo + "Revival Elixer Shop!");
		inv4 = Bukkit.createInventory(null, 54,bl+bo + "Admin Settings:");
		if(templates.size()>0){
			manager = new Timer(this,1);
			manager.start(1);
		}
		inGame=new Timer(this,3);
		inGame.start(1);
		scores=new Timer(this,5);
		scores.start(1);
		shop=new Timer(this,6);
		shop.start(1);
		admin=new Timer(this,12);
		admin.start(1);
		clrcnsl=new Timer(this,14);t7=0;
		clrcnsl.start(0);
		firewrks=new Timer(this,15);
		firewrks.start(1);
		boom=new Timer(this,16);
		boom.start(30);
		middle = new int[]{10,11,12,13,14,15,16,19,20,21,22,23,24,25,28,29,30,31,32,33,34,37,38,39,40,41,42,43};
		icon = new int[]{0,1,2,3,4,5,6,7,8,17,26,35,44,53,52,51,50,49,48,47,46,45,36,27,18,9};
		i=new int[]{1,2,3,5,10,20,50};
		d = new DyeColor[]{DyeColor.RED,DyeColor.BLUE,DyeColor.LIME,DyeColor.YELLOW,DyeColor.MAGENTA};
		l = new ArrayList<>();
		isBuilding = false;
		Bukkit.getServer().getPluginManager().registerEvents(this,Main.getPlugin(Main.class));
		toRefresh=new ArrayList<>();
		toClear=new ArrayList<>();
		toGiveChest=new ArrayList<>();
		checkItems();
		dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		cpyr = new SimpleDateFormat("yyyy");
		try{
			for(Player p:Bukkit.getOnlinePlayers()){
				loadQueue(p);
			}
		}catch(Exception e1){}
		try{
			File folder = new File("plugins/Micro-SG/Profiles");
			File[] fileList = folder.listFiles();
			    for (File f:fileList) {
					ObjectInputStream read = new ObjectInputStream(new FileInputStream(f));
					Stats s = (Stats)read.readObject();
					if(stats.contains(s)){/*Do Nothing*/} else {stats.add(s);}
					read.close();
			    }
		}catch(Exception e1){}
	}
	
	public void shutdown(){
		tmrDest();
		for(Player p:queue){
			p.teleport(getLobby());
		}
		try{
			for(Arena a:ingame){
				a.reset(0);
			}
		}catch(Exception e1){}
		ingame.clear();
		queue.clear();
		items.clear();
		toRefresh.clear();
		toClear.clear();
		toGiveChest.clear();
		loadQueue.clear();
		cleanAllFW();
		File folder = new File("plugins/Micro-SG/Profiles");
		File[] fileList = folder.listFiles();
		for(File f:fileList){f.delete();}
		for(Stats s:stats){
			save(s);
		}
		stats.clear();
		inv=null;
		inv2=null;
		inv3=null;
		inv4=null;
		dateFormat=null;
		cpyr=null;
		d=null;
	}
	
	public DateFormat getCpyr(){
		return cpyr;
	}
	
	public void cntRevive(Player pla){
		giveItem(pla, new ItemStack(Material.IRON_FENCE,4),1,wh+bo+"✦ Revival Elixer",hT(),sd()+re+bo+"- Can't Use An Elixer Right Now!",sd()+ye+bo+"- Purchase Revival Elixers!",sd()+gr+bo+"- Grab em' at "+ip,sd(),sd()+wh+bo+"Careful!",sd()+wh+"Every Time You Revive Yourself,",sd()+wh+"Your Stats Take A 2 Point Hit, So",sd()+wh+"While Revival Elixers Are A Major",sd()+wh+"Help, They Can Also Take Away Just",sd()+wh+"Enough Points To Get You Stuck",sd()+wh+"As The Runner-Up.",sd(),sd()+wh+bo+"A Point Of Advice:",sd()+wh+"If You've Already Revived Yourself",sd()+wh+"A Couple Of Times And You Keep",sd()+wh+"Getting Knocked Out, Don't Make",sd()+wh+"Your Stats Suffer, Just Sit Out",sd()+wh+"And Join Another Game!",sd(),hB(),"","#Menu-Item","#Micro-SG");
	}
	
	@SuppressWarnings("deprecation")
	public void checkItems(){
		try{		  
			File file = new File("plugins/Micro-SG/Items/items.txt");
			if (!file.exists()) {
				file.createNewFile();
				FileWriter fw = new FileWriter(file.getAbsoluteFile(),true);
				BufferedWriter bw = new BufferedWriter(fw);
				try{
					for(int x = 0;x<500;x++){
						bw.write(new ItemStack(x,1).getType().toString() + " 0\n");
					}
				} catch(Exception e1){}
				bw.close();
			}
			readItems();
		}catch (IOException e) {
			File file = new File("plugins/Micro-SG/Items");
			file.mkdir();
			checkItems();
		}
	}
	
	public void readItems(){
		try{
			items.clear();
			Scanner in = new Scanner(new FileReader("plugins/Micro-SG/Items/items.txt"));
			while(in.hasNext()){
				items.add(new ChestItem((in.next()),Integer.parseInt(in.next())));
				
			}
			in.close();
		}catch(Exception e1){
			e1.printStackTrace();
		}
	}
	
	public ArrayList<ChestItem> getItems(){
		return items;
	}
	
	@SuppressWarnings("deprecation")
	public void giveItem(Player pla,ItemStack i,int spaces,String name,String ... extraArgs){
		try{
			List<String> l = new ArrayList<>();
			ItemMeta m = i.getItemMeta();
			m.setDisplayName(name);
			for(int x=0;x<extraArgs.length;x++){
				try{l.add(extraArgs[x]);}catch(Exception e1){}
			}
			m.setLore(l);l.clear();
			i.setItemMeta(m);
			for(int x=0;x<spaces;x++){
				ItemStack ii = new ItemStack(new ItemStack(Material.STAINED_GLASS_PANE,1,DyeColor.SILVER.getData()));
				ItemMeta mm = ii.getItemMeta();
				mm.setDisplayName(wh+bo+ip);
				l.add(bl+r.nextDouble()+"");
				l.add("#Menu-Item");
				l.add("#Micro-SG");
				mm.setLore(l); ii.setItemMeta(mm);
				pla.getInventory().addItem(ii);
				l.clear();
			}
			pla.getInventory().addItem(i);
		}catch(Exception e1){}
	}
	
	@SuppressWarnings("deprecation")
	public void giveMenuItems(Player pla,int i){
		try{
			Stats s = checkProfile(pla);
			giveItem(pla, new ItemStack(Material.DOUBLE_PLANT,1),0,wh+bo+"✦ Revival Elixer Shop",hT(),sd()+re+bo+"- Purchase Our World-Famous",sd()+re+bo+"- Revival Concoction To Wake",sd()+ye+bo+"- You If You've Fallen In Battle",sd()+ye+bo+"- No Matter How Gruesome",sd()+gr+bo+"- Your Original Death May",sd()+gr+bo+"- Have Been!",sd(),sd()+wh+bo+"Are You A Smart Shopper?",sd()+wh+"Buying More Items In The Same",sd()+wh+"Transaction Not Only Gives You",sd()+wh+"More Stuff But Also Saves You",sd()+wh+"Points In The Long Run!",sd(),sd()+wh+bo+"Also!",sd()+wh+"Don't Forget That Having A Rank",sd()+wh+"Multiplies The Points You",sd()+wh+"Earn From Kills And Match",sd()+wh+"Victories, Up To 5x!!",sd(),hB()," ","#Menu-Item","#Micro-SG");
			giveItem(pla, new ItemStack(Material.BOOK,2),1,wh+bo+"✦ Best Scores!",hT(),sd()+re+bo+"- See The Most Skilled And Honored",sd()+ye+bo+"- Tributes Beneath This Cover.",sd()+gr+bo+"- Once Your See Their Scores, You'll",sd()+ga+bo+"- Have Some New Rivals To Beat!",sd()+wh+bo+"- Players On Record: "+stats.size(),sd(),sd()+wh+bo+"The Road To 1st Place:",sd()+wh+"Play Micro SG A Lot, Play",sd()+wh+"Fair, And Get A Rank So You",sd()+wh+"Earn More Points More Quickly",sd(),sd()+wh+bo+"Please Note:",sd()+wh+"That We Do Not Allow The Use",sd()+wh+"Of Any Sort Of Mods Or Special",sd()+wh+"Clients, So Let's Keep Things",sd()+wh+"Simple, Just Don't Do It ;D",sd(),hB(),"","#Menu-Item","#Micro-SG");
			giveItem(pla, new ItemStack(Material.SIGN,3),1,wh+bo+"✦ Stats",wh+bo+s.getName(),hT(),sd()+gr+bo+"- Kills: "+s.getKills(),sd()+ye+bo+"- Wins: "+s.getWins(),go+bo+"│ - Points: "+s.getPoints(),sd()+re+bo+"- Deaths: "+s.getDeaths(),sd()+ga+bo+"- Revives: "+s.getRevives(),sd()+ga+bo+"- Revival Elixers: "+s.getElixers(),sd()+wh+bo+"- Current Standing: "+gr+bo+getStanding(pla.getName()),sd()+wh+bo+"- Point Multiplier: "+gr+bo+getMult(pla)+"x",sd(),sd()+wh+bo+"Multiplier Information:",sd()+wh+"Points Earned Can Be",sd()+wh+"Increased By Having A",sd()+wh+"Rank And Can Range",sd()+wh+"Anywhere From 2x - 5x.",sd()+wh+"So Help Out The Server",sd()+wh+"And Reward Yourself In",sd()+wh+"The Process!",sd(),hB(),"",wh+"Click To Refresh!","","#Menu-Item","#Micro-SG");
			if(i==1){
				int qty = checkProfile(pla).getElixers();
				String message = "";
				if(qty>0){
					message=sd()+gr+bo+"- Revive Yourself Now!";
					giveItem(pla, new ItemStack(Material.GOLDEN_APPLE,4),1,wh+bo+"✦ Revival Elixer",hT(),sd()+re+bo+"- Use This To Get Back In The Game!",sd()+ye+bo+"- Elixers Available: "+qty,message,sd(),sd()+wh+bo+"Micro-SG Wisdom:",sd()+wh+"It Probably Wouldn't Be A Very Good",sd()+wh+"Idea To Revive Yourself Into An Arena",sd()+wh+"Where Most Remaining Tributes Are",sd()+wh+"Pretty Stacked, Because It Pretty Unlikely",sd()+wh+"That You'll Be Able TO Best Them!",sd(),hB(),"","#Menu-Item","#Micro-SG");
				}else{
					cntRevive(pla);
				}
			}else{
				cntRevive(pla);
			}
			giveItem(pla, new ItemStack(Material.NETHER_STAR,5),1,wh+bo+"✦ Games Running",hT(),sd()+gr+bo+"- Want To See How The Pros Do It?",sd()+ye+bo+"- Click Here To See All Games In",sd()+re+bo+"- Progress, And Spectate One Of Them.",sd(),sd()+wh+bo+"Micro-SG Wisdom:",sd()+wh+"You Might Learn A Valuable",sd()+wh+"Thing Or Two, Then You Can Stick",sd()+wh+"It To Your Opponents! :D",sd(),hB()," ","#Menu-Item","#Micro-SG");
			if(checkInGame(pla)!=0){
				giveItem(pla, new ItemStack(Material.STAINED_GLASS_PANE,1,DyeColor.SILVER.getData()),9,wh+bo+getIP(),bl+r.nextDouble()+"","#Menu-Item","#Micro-SG");
				for(int x=0;x<7;x++){
					giveItem(pla, new ItemStack(Material.STAINED_GLASS_PANE,1,DyeColor.RED.getData()),0,re+bo+"LEAVE",bl+r.nextDouble()+"","#Menu-Item","#Micro-SG");
				}
			}
			giveItem(pla, new ItemStack(Material.STAINED_GLASS_PANE,1,DyeColor.SILVER.getData()),27,wh+bo+getIP(),bl+r.nextDouble()+"","#Menu-Item","#Micro-SG");
		}catch(Exception e1){}
	}
	
	public void giveChest(){
		for(int x=0;x<toGiveChest.get(0).getInventory().getContents().length;x++){
			ItemStack i = new ItemStack(Material.CHEST,1);
			ItemMeta met = i.getItemMeta();
			met.setDisplayName(bl+r.nextDouble());
			List<String> s = new ArrayList<>();
			s.add(wh+bo+"Revival Crate!");
			s.add("#Micro-SG");
			met.setLore(s);
			i.setItemMeta(met);
			toGiveChest.get(0).getInventory().addItem(i);
		}
		toGiveChest=remove(toGiveChest,toGiveChest.get(0));
		if(toGiveChest.size()<=0){
			gvChest.stop();
			gvChest=null;
		}	
	}
	
	//DmSpd
	public int setDmSpd(int spd){
		cReport("Dark Matter Speed Edited - "+spd+" Tick(s)");
		if((spd<1)&(spd>5)){
			return -1;
		} else {
			this.dmSpd=spd;
			return 0;
		}
	}
	
	public int getDmSpd(){
		return dmSpd;
	}
	
	//DropFreq
	public int setDropFreq(int freq){
		cReport("Drop Package Frequency Edited - "+freq+" Seconds");
		if((freq<30)&(freq>150)){
			return -1;
		}else{
			this.dropFreq=freq;
			return 0;
		}
	}
	
	public int getDropFreq(){
		return dropFreq;
	}
	
	public int setMult(int mult){
		cReport("Point Multiplier Edited - "+mult+"x");
		if((mult<1)&(mult>5)){
			return -1;
		}else{
			this.mult=mult;
			return 0;
		}
	}
	
	public int getMult(){
		return mult;
	}
	
	public void clearInv(){
		toClear.get(0).getInventory().clear();
		toClear=remove(toClear,toClear.get(0));
		if(toClear.size()<=0){
			clrInv.stop();
			clrInv=null;
		}
	}
	
	public void updateInv(){
		boolean found=false;
		for(Arena a:ingame){
			if(a.checkCanRevive(toRefresh.get(0))==true){
				found=true;
				break;
			}
		}
		if(found==true){
			giveMenuItems(toRefresh.get(0),1);
		}else{
			giveMenuItems(toRefresh.get(0),0);
		}
		toRefresh=remove(toRefresh,toRefresh.get(0));
		if(toRefresh.size()<=0){
			updInv.stop();
			updInv=null;
		}
	}
	
	public void resetXP(Player p){
		p.setExp(0);p.setLevel(0);
	}
	
	public ItemStack getItem(){
		ArrayList<ItemStack> is = new ArrayList<>();
		for(int x = 0;x<getItems().size();x++){
			if(getItems().get(x).getFreq()>0){
				is.add(new ItemStack(Material.getMaterial(getItems().get(x).getName()),1));
			}
		}
		return is.get(r.nextInt(is.size()));
	}
	
	public void setQueue(ArrayList<Player>queue){
		this.queue=queue;
	}
	
	public ArrayList<Player> getQueue(){
		return queue;
	}
	
	public ArrayList<Player> remove(ArrayList<Player> source,Player p){
		ArrayList<Player> pl=new ArrayList<>();
		for(Player pla:source){
			if(pla.equals(p)){/*Do Nothing*/}else{
				pl.add(pla);
			}
		}
		return pl;
	}
		
	public ArrayList<Stats> removeS(ArrayList<Stats> s, Stats ss){
		ArrayList<Stats> sss=new ArrayList<>();
		for(Stats sa:s){
			if(sa.equals(ss)){/*Do Nothing*/}else{
				sss.add(sa);
			}
		}
		this.stats=new ArrayList<Stats>();
		return sss;
	}
	
	public void addToRefresh(Player p){
		addToClear(p);
		toRefresh.add(p);
	}
	
	public void addToClear(Player p){
		toClear.add(p);
	}
	
	public void addGiveChest(Player p){
		toGiveChest.add(p);
	}
	
	public void a(Player p){
		p.sendMessage("\n"+hT());
	}
	
	public void b(Player p){
		p.sendMessage(hB());
	}
	
	public String hT(){
		return go+bo+"╒═════════════════════╬";
	}
	
	public String hB(){
		return go+bo+"╘═════════════════════╬";
	}
	
	public Inventory inv(){
		return inv;
	}
	
	public void updateShop(){
		frm3();
		for(int x=0;x<7;x++){
			setUpItem(inv3,middle[x],new ItemStack(Material.NAME_TAG,i[x]),wh+bo+"Elixer Package #"+(x+1),hT(),sd()+re+bo+"- Revival Elixer x"+i[x],go+bo+"│ - Price: "+(int)((i[x]*300)-((i[x]-1)*10)),sd()+ye+bo+"- Buy More, Get More FREE!",sd()+gr+bo+"- So Win Games And Save Those Points!",hB(),"","#Micro-SG");
		}
		try{
			int[] i=new int[]{28,29,37,38,33,34,42,43};
			for(int x=0;x<i.length;x++){
				setUpItem(inv3,i[x],getItem(),wh+bo+"Mystery Box!",hT(),sd()+re+bo+"- Random Item",go+bo+"│ - Price: 250 Points Per Draw",sd()+ye+bo+"- Items For Next Match Only!",sd()+gr+bo+"- Strict Max Of 5 Starting Items!",sd(),sd()+wh+bo+"- Here's Your Chance To Start",sd()+wh+bo+"- Off With Some Serious Loot!",hB(),"","#Random","#Micro-SG");
			}
		}catch(Exception e1){}
		try{
			int i[]=new int[]{31,40};
			for(int x=0;x<i.length;x++){
				setUpItem(inv3,i[x],new ItemStack(Material.BLAZE_ROD,1),wh+bo+"Dark Matter FlashBang!!",hT(),sd()+re+bo+"- Confuses All Dark Matter",sd()+go+bo+"- And Causes Them To Forget",sd()+ye+bo+"- Their Target! Using This Will",sd()+gr+bo+"- Make Everyone Love You! :D",sd(),sd()+wh+bo+"- This Item Is Very Valuable",sd()+wh+bo+"- But Also Very Expensive!",sd(),sd()+wh+bo+"- Price: 2000 Points Each!",hB(),"","#Micro-SG");
			}
		}catch(Exception e1){}
	}
	
	@SuppressWarnings("deprecation")
	public void updateMenu(){
		try{
			frm();
			if(ingame.size()>0){
				for(int x = 0;x<ingame.size();x++){
					if(ingame.get(x).isFinished()==false){
						if(ingame.get(x).hasStarted()==true){
							setUpItem(inv,middle[x],new ItemStack(Material.CHEST,1),x+" "+wh+bo+"IN-GAME ",hT(),sd()+"- "+gr+bo+"Map: "+ingame.get(x).getName(),sd()+"- "+ye+bo+"Players Alive: "+ingame.get(x).getPlayers().size()+"/"+ingame.get(x).getM().getMaxPlayers(),sd()+"- "+go+bo+"Spactators: "+ingame.get(x).getSpectators().size()+"/∞",sd()+"- "+re+bo+"Active Dark Matter: "+ingame.get(x).getDarkMatter().size(),sd()+"- "+ga+bo+"Game-Time: "+ingame.get(x).getFormattedTime(),sd(),hB(),"","#Micro-SG");
						} else {
							setUpItem(inv,middle[x],new ItemStack(Material.CHEST,1),wh+bo+"STARTING... ",hT(),sd()+"- "+gr+bo+"Map: "+ingame.get(x).getName(),sd()+"- "+ye+bo+"Players In: "+ingame.get(x).getPlayers().size()+"/"+ingame.get(x).getM().getMaxPlayers(),sd()+"- "+re+bo+"Starting In: "+(40-ingame.get(x).getTime()),sd(),hB(),"","#Micro-SG");
						}
					} else {
						setUpItem(inv,middle[x],new ItemStack(Material.ENDER_CHEST,1),hT(),sd()+re+bo+"FINISHED!",sd()+ye+bo+"Finished After: "+ingame.get(x).getFormattedTime(),sd(),hB(),"#Micro-SG");
					}
				}
			} else {
				for(int x = 0;x<middle.length;x++){
					setUpItem(inv,middle[x],new ItemStack(Material.STAINED_GLASS_PANE,1,DyeColor.SILVER.getData()),re+bo+"No Matches Running!","",re+bo+"Ⓒ "+cpyr.format(new Date()),re+ip,"","#Micro-SG");
				}
			}
		}catch(Exception e1){}
	}
	
	@SuppressWarnings("deprecation")
	public void updateScores(){
		frm2();this.stats = new Sorter().sort(stats);
		Material[] m = new Material[]{Material.EMERALD,Material.DIAMOND,Material.GOLD_INGOT,Material.IRON_INGOT};
		if(stats.size()>0){
			for(int x=0;x<middle.length&x<stats.size();x++){
				setUpItem(inv2,middle[x],new ItemStack(m[x/7],x+1),wh+bo+stats.get(x).getName(),"",go+bo+"╒═════════════╬",go+bo+"│ "+gr+bo+"- Kills: "+stats.get(x).getKills(),go+bo+"│ "+ye+bo+"- Wins: "+stats.get(x).getWins(),go+bo+"│ "+re+bo+"- Deaths: "+stats.get(x).getDeaths(),go+bo+"│ "+ga+bo+"- Revives: "+stats.get(x).getRevives(),sd()+wh+bo+"- Current Standing: ",sd()+wh+bo+"- "+getStanding(stats.get(x).getName())+" Out Of "+ stats.size()+" Players!",sd(),sd()+wh+bo+"NOTE:",sd()+wh+"Only The Top 28 Players",sd()+wh+"Are Shown Here!",sd(),sd()+ga+bo+"Ⓒ "+cpyr.format(new Date()),sd()+ga+bo+ip,go+bo+"╘═════════════╬","","#Micro-SG");
				if(inv2.getContents()[middle[x]].getType()==Material.EMERALD)
				inv2.getContents()[middle[x]].addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
			}
		} else {
			for(int x=0;x<middle.length;x++){
				setUpItem(inv2,middle[x],new ItemStack(Material.STAINED_GLASS_PANE,1,DyeColor.SILVER.getData()),gr+bo+"Micro SG!",hT(),sd()+re+bo+"Nothing Here Yet!",hB(),"","#Micro-SG");
			}
		}
	}
	
	public void boom(){
		fw.add(new Firework());
		sTF(setTarget(getLobby(),r.nextInt(20),50,r.nextInt(20)));
		for(Player p:Bukkit.getOnlinePlayers()){
			if(checkInGame(p)==0){
				p.playSound(p.getLocation(),Sound.FIREWORK_LARGE_BLAST,1,1);
			}
		}
	}
	
	public void cleanFW(){
		for(Firework f:fw){
			f.tick();
			if(f.getLife()>40){
				f.destroy();
				fw.remove(f);
			}
		}
	}
	
	public void cleanAllFW(){
		for(Firework f:fw){
			f.destroy();
		}
		fw.clear();
	}
	
	public void clearConsole(){
		if(t7<=30){
			console.add("                                       ");
			if(console.size()>30){
				console.remove(0);
			}
			t7++;
		}else{
			clrcnsl.stop();
			t7=0;
			cReport("-- Console Cleared --");
		}
	}
	
	public void errCtch(Player p){
		p.openInventory(inv);
		p.playSound(p.getLocation(), Sound.ZOMBIE_DEATH, 1, .5f);
		p.sendMessage("\n"+hT());
		p.sendMessage(sd()+e()+"The Match You Were About");
		p.sendMessage(sd()+e()+"To Spectate Has Ended, And");
		p.sendMessage(sd()+e()+"Its Information Is No Longer");
		p.sendMessage(sd()+e()+"Available");
		p.sendMessage(hB());
	}
	
	@SuppressWarnings("deprecation")
	public void admin(){
		frm4();int[][]i=new int[][]{{11,12,13,14,15,160,1},{20,21,22,23,24,160,30},{29,30,31,32,33,160,1}};
		DyeColor[]d=new DyeColor[]{DyeColor.RED,DyeColor.YELLOW,DyeColor.LIME};
		String[]s=new String[]{"Dark Matter Speed: (Ticks)","Drop Package Frequency: (Seconds)","Bonus Point Multiplier: (x)"};
		int[]set=new int[]{getDmSpd(),getDropFreq(),getMult(),-1};
		for(int x=0;x<3;x++){
			for(int y=0;y<i[x].length-2;y++){
				String cs,css;
				if(((i[x][i[x].length-1])*(y+1))!=set[x]){
					cs="Change Setting To: ";css=(((i[x][i[x].length-1])*(y+1)))+"";
					setUpItem(inv4,i[x][y],new ItemStack(i[x][i[x].length-2],1,d[x].getData()),ye+bo+s[x],hT(),sd()+wh+bo+cs,sd()+gr+bo+css,hB(),"","#Micro-SG");
				}else{
					cs="Currently Set:";css=set[x]+"";
					setUpItem(inv4,i[x][y],new ItemStack(Material.NETHER_STAR,1),ye+bo+s[x],hT(),sd()+wh+bo+cs,sd()+gr+bo+css,hB(),"","#Micro-SG");
				}	
			}
		}
		int[]o=new int[]{10,19,28,37,16,25,34,43};
		for(int x=0;x<8;x++){
			setUpItem(inv4,o[x],new ItemStack(Material.COMMAND,1),wh+bo+"Micro-SG Console"+wh+" (Click To Clear)",console,"","#Micro-SG");
		}
	}
	
	public Location setTarget(Location l,int ... coords){
		int xx = r.nextInt(40);int yy = r.nextInt(40);int zz = r.nextInt(40);
		if(coords.length==3){
			xx=coords[0];yy=coords[1];zz=coords[2];
		}
		for(int x = 0;x<3;x++){
			switch(x){
			case 0:
				if(r.nextInt(2)==0){
					xx = (xx*-1)+l.getBlockX();
				} else {
					xx+=l.getBlockX();
				}
			break;
			case 1:
				yy+=l.getBlockY();
			break;
			case 2:
				if(r.nextInt(2)==0){
					zz = (zz*-1)+l.getBlockZ();
				} else {
					zz+=l.getBlockZ();
				}
			break;
			}
		}
		return new Location(l.getWorld(),xx,yy,zz);
	}
	
	public void dSM(Player p,String s){
		GUI g=new GUI(0,this);
		p.openInventory(g.delfrm(s));
	}
	
	public void dS(Player pl,String name){
		for(Stats s:stats){
			if((wh+bo+s.getName()).equalsIgnoreCase(name)){
				stats.remove(s);
				pl.playSound(pl.getLocation(),Sound.CREEPER_DEATH,1,2.0f);
				pl.openInventory(inv2);
				addToRefresh(pl);
				break;
			}
		}		
	}
	
	public int checkInGame(Player p){
		if(queue.contains(p)){
			return -3;
		}
		for(Arena a:ingame){
			if(a.getPlayers().contains(p)){
				return -2;
			}
			if(a.getSpectators().contains(p)){
				return -1;
			}
		}
		return 0;
	}
	
	public Arena getArenaPlayerIsIn(Player p){
		for(Arena a:ingame){
			if(a.getPlayers().contains(p)){
				return a;
			}
		}
		return null;
	}
	
	public String getIP(){
		return ip;
	}
	
	public void checkAndLeave(Player p){
		for(Arena a:ingame){
			if(a.getPlayers().contains(p)){
				a.leave(p);
			}
		}
		save(p);
	}

	@SuppressWarnings("deprecation")
	public void TNT(Player p){
		Location loc = p.getLocation();
		loc.getWorld().spawnEntity(new Location(loc.getWorld(), loc.getBlockX(), loc.getBlockY() + 5, loc.getBlockZ()),EntityType.PRIMED_TNT);
		p.playSound(p.getLocation(), Sound.FUSE, 1, 1);
		p.playEffect(p.getLocation(), Effect.MOBSPAWNER_FLAMES, 10);
		if(p.getItemInHand().getAmount() <= 1){
			p.getInventory().remove(p.getItemInHand());
		} else {
			p.getItemInHand().setAmount(p.getItemInHand().getAmount()-1);
		}
	}
	
	@SuppressWarnings("deprecation")
	public void FIREBALL(Player p){
		Location loc = p.getLocation();
		p.playSound(p.getLocation(), Sound.FIRE_IGNITE, 1, 1);
		p.playEffect(p.getLocation(), Effect.MOBSPAWNER_FLAMES, 10);
        p.getWorld().spawn(new Location(loc.getWorld(), loc.getBlockX(), loc.getBlockY() + 3, loc.getBlockZ()), Fireball.class).setDirection(p.getLocation().getDirection());
		if(p.getItemInHand().getAmount() <= 1){
			p.getInventory().remove(p.getItemInHand());
		} else {
			p.getItemInHand().setAmount(p.getItemInHand().getAmount()-1);
		}
	}
	
	@SuppressWarnings("deprecation")
	public void JUMP(Player p){
		p.setVelocity(new Vector(0, 1, 0));
		p.playSound(p.getLocation(), Sound.FIZZ, 1, 1);
		p.playEffect(p.getLocation(), Effect.MOBSPAWNER_FLAMES, 10);
		if(p.getItemInHand().getAmount() <= 1){
			p.getInventory().remove(p.getItemInHand());
		} else {
			p.getItemInHand().setAmount(p.getItemInHand().getAmount()-1);
		}
	}
	
	public void NO(Player p){
		p.playSound(p.getLocation(), Sound.NOTE_BASS, 1, .5f);
		p.sendMessage(sd()+e()+"- Purchase Failed!");
		p.sendMessage(sd()+w()+"- Insufficient Point Balance!");
		p.sendMessage(sd()+s()+"- Keep Playing And Come Back Later!");
	}
	
	public void NOO(Player p){
		p.playSound(p.getLocation(), Sound.NOTE_BASS, 1, .5f);
		p.sendMessage(sd()+e()+"- Purchase Failed!");
		p.sendMessage(sd()+w()+"- Maximum Start Items Reached!");
		p.sendMessage(sd()+s()+"- Come Back After Your Next Game!");
	}
	
	public void WHY(Player p){
		a(p);p.sendMessage(sd()+i()+">> Why Do You Have");
		p.sendMessage(sd()+i()+">> This Here, WHY?? :/");b(p);
	}
	
	public void frm(){
		t2++;
		if(t2%3==0){
			t2=t2%3;
			buildFrm(inv);
		}
	}
	
	public void frm2(){
		t3++;
		if(t3%3==0){
			t3=t3%3;
			buildFrm(inv2);
		}
	}
	
	public void frm3(){
		t4++;
		if(t4%3==0){
			t4=t4%3;
			buildFrm(inv3);
		}
	}
	public void frm4(){
		t6++;
		if(t6%3==0){
			t6=t6%3;
			solidFrm(inv4);
		}
	}
	public void frm5(Inventory inv5){
		t9++;
		if(t9%3==0){
			t9=t9%3;
			clear(inv5);
			buildFrm(inv5);
		}
	}
	
	@SuppressWarnings("deprecation")
	public void buildFrm(Inventory inv){
		clear(inv);
		for(int x = 0;x<26;x++){
			try{
				setUpItem(inv,icon[x],new ItemStack(Material.STAINED_GLASS_PANE,1,d[r.nextInt(5)].getData()),gr+bo+"Micro SG!","",hT(),sd()+ye+bo+"Current Time:",sd()+getTime(),sd(),sd()+"Matches Running:",sd()+go+ingame.size(),sd(),sd()+re+bo+"Online Players:",sd()+re+Bukkit.getOnlinePlayers().size()+"",sd(),sd()+wh+bo+"Ⓒ "+cpyr.format(new Date()),sd()+wh+ip,sd()+"",hB(),"","#Menu-Item","#Micro-SG");
			} catch(Exception e1){/*Date Object Not Initialized*/}
		}
	}
	
	@SuppressWarnings("deprecation")
	public void solidFrm(Inventory inv){
		clear(inv);
		for(int x = 0;x<26;x++){
			try{
				setUpItem(inv,icon[x],new ItemStack(Material.STAINED_GLASS_PANE,1,DyeColor.BLACK.getData()),gr+bo+"Micro SG!","",hT(),sd()+ye+bo+"Current Time:",sd()+getTime(),sd(),sd()+"Matches Running:",sd()+go+ingame.size(),sd(),sd()+re+bo+"Online Players:",sd()+re+Bukkit.getOnlinePlayers().size()+"",sd(),sd()+wh+bo+"Ⓒ "+cpyr.format(new Date()),sd()+wh+ip,sd()+"",hB(),"","#Menu-Item","#Micro-SG");
			} catch(Exception e1){/*Date Object Not Initialized*/}
		}
	}
	
	public String getTime(){
		return ye+dateFormat.format(new Date());
	}
	
	public DyeColor[] getD(){
		return d;
	}
	
	public void clear(Inventory invv){
		for(int x = 0;x<54;x++){
			setUpItem(invv,x,new ItemStack(Material.AIR,1),"");
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
	
	public void sTF(Location l){
		for(int x=0;x<50;x++){sIF(l);}
		l.getWorld().getBlockAt(l).setType(Material.AIR);
	}
	
	@SuppressWarnings("deprecation")
	private void sIF(Location l){
		Block b = l.getWorld().getBlockAt(l);
		b.setType(Material.WOOL);
		b.setData((byte)r.nextInt(16));
		int n1 = r.nextInt(2);if(n1==0){n1 = -1;}else{n1 = 1;}
		int n2 = r.nextInt(2);if(n2==0){n2 = -1;}else{n2 = 2;}
    	int n3 = r.nextInt(2);if(n3==0){n3 = -1;}else{n3 = 1;}
    	FallingBlock fb = b.getLocation().getWorld().spawnFallingBlock(b.getLocation(), b.getType(), b.getData());
    	fw.get(fw.size()-1).addBlock(fb);
    	fb.setVelocity(new Vector(n1*(.02+(r.nextInt(33)*.03)),n2*(.02+(r.nextInt(33)*.03)),n3*(.02+(r.nextInt(33)*.03))));
    	fb.setDropItem(false);
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
	
	public void loadQueue(Player p){
		loadQueue.add(p);
	}

	public void loadTick(){
		if(loadQueue.size()>0){
			load(loadQueue.get(0));
			loadQueue=remove(loadQueue,loadQueue.get(0));
		}else{
			loadT.stop();
			loadT=null;
		}
	}
	
	public void cReport(String info){
		console.add(aq+new SimpleDateFormat("HH:mm:ss").format(new Date())+gr+" > "+info);
		if(console.size()>30){
			console.remove(0);
		}
	}
	
	private void tmrDest(){
		try{
			manager.stop();
			manager=null;
		}catch(Exception e1){}
		try{
			build.stop();
			build=null;
			head=null;
		}catch(Exception e1){}
		try{
			inGame.stop();
			inGame=null;
		} catch(Exception e1){}
		try{
			scores.stop();
			scores=null;
		} catch(Exception e1){}
		try{
			shop.stop();
			shop=null;
		} catch(Exception e1){}
		try{
			updInv.stop();
			updInv=null;
		}catch(Exception e1){}
		try{
			clrInv.stop();
			clrInv=null;
		}catch(Exception e1){}
		try{
			gvChest.stop();
			gvChest=null;
		}catch(Exception e1){}
		try{
			loadT.stop();
			loadT=null;
		}catch(Exception e1){}
		try{
			admin.stop();
			admin=null;
		}catch(Exception e1){}
		try{
			clrcnsl.stop();
			clrcnsl=null;
		}catch(Exception e1){}
		try{
			firewrks.stop();
			firewrks=null;
		}catch(Exception e1){}
		try{
			boom.stop();
			boom=null;
		}catch(Exception e1){}
	}
	
	public void leave(Player pla){
		if(queue.contains(pla)){
			try{ScoreBoardManager.removeScoreBoard(pla);}catch(Exception e1){}
			queue=remove(queue,pla);
			pla.teleport(getLobby());
			addToClear(pla);
			addToRefresh(pla);
			a(pla);pla.sendMessage(sd()+s()+"You Have Left The Game!");b(pla);
			for(Player p:queue){
				a(p);p.sendMessage(sd()+w()+">> TRIBUTE, "+pla.getName()+" Fled!");b(p);
			}
			cReport(pla.getName()+" Left The Queue!");
		}else{
			if (checkInGame(pla)==0){
				a(pla);pla.sendMessage(sd()+e()+">> Not In Queue!");
				pla.sendMessage(sd()+w()+">> Enter The Lobby Center To Play!");b(pla);
			}
		}
	}
	
	public void load(Player p){
		try {
			ObjectInputStream read = new ObjectInputStream(new FileInputStream("plugins/Micro-SG/Profiles/"+p.getUniqueId()+".profile"));
		 	try{
		 		Stats s = ((Stats)read.readObject());
		 		boolean found=false;
		 		for(int x=0;x<stats.size();x++){
		 			if(stats.get(x).getName().equalsIgnoreCase(s.getName())){
		 				stats.set(x,s);
		 				found=true;
		 				break;
		 			}
		 		}
		 		if(found==false){s.initGUI(this);stats.add(s);}
		 	}catch(Exception e1){e1.printStackTrace();}
		read.close();
		} catch (Exception e1) {
			File file = new File("plugins/Micro-SG/Profiles");
			file.mkdir();
		}
		addToRefresh(p);
		try{
			for(int x=0;x<Bukkit.getWorlds().size();x++){
				if(Bukkit.getWorlds().get(x).getName().equalsIgnoreCase(getWN())){
					setOW(x);
					break;
				}
				try{
					for(Arena a:templates){
						for(SpawnPoint s:a.getSpawnPoints()){
							if(s.getWN().equalsIgnoreCase(Bukkit.getWorlds().get(x).getName())){
								s.setW(x);
							}
						}
					}
				}catch(Exception e1){}
			}
		}catch(Exception e1){}
	}
	
	public void save(Player p){
		try{
			ObjectOutputStream write = new ObjectOutputStream(new FileOutputStream("plugins/Micro-SG/Profiles/"+p.getUniqueId()+".profile"));
			Stats s = checkProfile(p);s.killGUI();
			write.writeObject(s);
			write.close();
		} catch(Exception e1){
			File file = new File("plugins/Micro-SG/Profiles");
			file.mkdir();
		}
	}
	
	public void save(Stats s){
		try{
			ObjectOutputStream write = new ObjectOutputStream(new FileOutputStream("plugins/Micro-SG/Profiles/"+s.getUUID()+".profile"));
			s.killGUI();
			write.writeObject(s);
			write.close();
		} catch(Exception e1){
			File file = new File("plugins/Micro-SG/Profiles");
			file.mkdir();
		}
	}
	
	public double calcDist(Location loc1, Location loc2){
		return Math.sqrt(Math.pow(loc1.getBlockX()-loc2.getBlockX(),2)+Math.pow(loc1.getBlockY()- 
		loc2.getBlockY(),2)+Math.pow(loc1.getBlockZ()-loc2.getBlockZ(),2));
	}
}