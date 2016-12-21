package me.virusbrandon.Micro_SG;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Arena implements java.io.Serializable, Listener{
	private static final long serialVersionUID = 14894982L;
	private ArrayList<Player> players = new ArrayList<>();
	private ArrayList<Player> spectators = new ArrayList<>();
	private ArrayList<String> canRevive =  new ArrayList<>();
	private ArrayList<DarkMatter> darkM = new ArrayList<>();
	private ArrayList<SpawnPoint> sp = new ArrayList<>();
	private ArrayList<Location> chests = new ArrayList<>();
	private ArrayList<Timer> timers;
	
	private ArenaManager m;
	private Random r = new Random();
	private BlockNode b;
	private String name;
	private String gr=ChatColor.GREEN+"",go=ChatColor.GOLD+"",bo=ChatColor.BOLD+"",wh=ChatColor.WHITE+"",re=ChatColor.RED+"",ye=ChatColor.YELLOW+"";
	private int slotNumber=-10;
	private int t = 0;
	private int time = 0;
	private int t2,t3;private float t4;
	private int end = 250;
	private int cdn = 40;
	private boolean started = false,finished = false, ready=false,dm=false,state=true;
	private Timer drop,cnt;
	private GUI g;
	
	/**
	 * PlayerDeathEvent, Is Called
	 * When A Player Is Killed, Then Performs Various
	 * Actions Based On The Player's State In The
	 * Game.
	 * 
	 * @param e
	 * 
	 * 
	 */
	@EventHandler
	public void death(PlayerDeathEvent e){
		Player pl = e.getEntity();
		Player kl = e.getEntity().getKiller();
		if(players.contains(pl)){
			Stats s = m.checkProfile(pl);
			s.setDeaths(s.getDeaths(),1);
			pl.sendMessage("\n"+m.w()+"Dang!");
			pl.sendMessage(m.e()+">> -5 Points For That Death!");
			e.setDeathMessage("");
			try{
				s = m.checkProfile(kl);
				int mult=m.getMult(kl);
				s.setKills(s.getKills(),1,mult);
				a(kl);kl.sendMessage(m.sd()+m.w()+"Nice!");
				if(m.getMult()<=1){
					kl.sendMessage(m.sd()+m.s()+">> +(5x"+mult+"): "+(5*mult)+" Points For That Kill!");
				}else{
					kl.playSound(kl.getLocation(), Sound.LEVEL_UP,1,.5f);
					kl.sendMessage(m.sd()+m.s()+">> "+getMultWord(m.getMult())+" Point Weekend!");
					kl.sendMessage(m.sd()+m.s()+">> +(5x"+(mult*m.getMult())+"): "+(5*(mult*m.getMult()))+" Points For That Kill!");
				}b(kl);
				announceDeath(pl,kl);
			}catch(Exception e1){
				announceNoobDeath(pl);
			}
			try{
				if(started==true){
					pl.setHealth(20);
					canRevive.add(pl.getName());
					spectate(pl);
					m.sTF(pl.getLocation());
					Entity.fireWork(pl.getLocation());
					this.players=remove(players,pl);
					try{spectators.get(spectators.size()-1).teleport(sp.get(r.nextInt(sp.size()-1)).getSpawnPoint(slotNumber));}catch(Exception e1){}
					try{
						for(DarkMatter dm:darkM){
							try{pl.playSound(pl.getLocation(),dm.victimDied(pl),.5F,1);}catch(Exception e1){}
						}
					} catch(Exception e1){/*Do Nothing :J*/}
					if(players.size()>1){}else{
						finished = true;
						try{
							for(Player p:spectators){
								gE(p);
							}
							gE(players.get(0));
							m.chkUI(getSlotNumber());
						}catch(Exception e1){}
					}
				} else {
					if(players.contains(pl)){
						a(pl);pl.sendMessage(m.sd()+m.w()+"Bruh! Don't Be Dying On Me Now!");
						pl.sendMessage(m.sd()+m.w()+"That's Coming Shortly >:D");b(pl);
						heal(pl);pl.teleport(m.getLobby());
					}	
				}
			}catch(Exception e1){}
		}
	}
	
	
	
	/**
	 * InventoryClickEvent, Is Called
	 * When A Player Clicks On A Item On Their Inventory.
	 * This One Specifically Responds To A Player's Attempt
	 * To Revive Themselves.
	 * 
	 * @param e
	 * 
	 * 
	 */
	@EventHandler
	public void click(InventoryClickEvent e){
		Player p = (Player)e.getWhoClicked();
		if(spectators.contains(e.getWhoClicked())){
			if(e.getCurrentItem().getType()==Material.GOLDEN_APPLE){
				revH(p);
			}	
			e.setCancelled(true);
		}
	}
	
	
	
	/**
	 * PlayerDropItemEvent, Is Called
	 * When A Player Drops An Item On The Ground. This
	 * Event Is To Be Allowed To Run It's Course If The
	 * Player Who Attempted To Drop An Item Is Actively In
	 * Game; In Other Words, This Prevents Spectators From
	 * Unfairly Assisting Players. 
	 * 
	 * @param e
	 * 
	 */
	@EventHandler
	public void dropItem(PlayerDropItemEvent e){
		try{
			if(started==false|spectators.contains(e.getPlayer())){
				e.setCancelled(true);
			}
			try{
				if(e.getItemDrop().getItemStack().getItemMeta().getLore().contains("#Micro-SG")){
					e.setCancelled(true);
				}
			}catch(Exception e1){}
		}catch(Exception e1){}
	}
	
	
	
	/**
	 * BlockBreakEvent, Is Called
	 * When A Player Breaks A Block. This Event Will
	 * Be Allowed To Run For Certain Blocks.
	 * 
	 * @param e
	 * 
	 */
	@EventHandler 
	public void blockBreak(BlockBreakEvent e){
		if(spectators.contains(e.getPlayer())){
			e.setCancelled(true);
		}
		if(players.contains(e.getPlayer())){
			if(e.getBlock().getType()==Material.LEAVES | e.getBlock().getType()==Material.LEAVES_2 | e.getBlock().getType()==Material.LAPIS_ORE){
			}else{
				e.setCancelled(true);
			}
		}
	}
	
	
	
	/**
	 * BlockDamageEvent, Is Called
	 * When A Player Begins To Hit A Block To Damage It.
	 * This Event Is Allowed For In Game Action Players.
	 * 
	 * @param e
	 * 
	 */
	@EventHandler
	public void blockDamage(BlockDamageEvent e){
		if(spectators.contains(e.getPlayer())){
			e.setCancelled(true);
		}
	}
	
	
	
	/**
	 * PlayerPickupItemEvent, Is Called
	 * When A Player Grabs An Item From The Ground. This
	 * Event Is Cancelled If They Are Spectating.
	 * 
	 * @param e
	 * 
	 */
	@EventHandler
	public void onItemGet(PlayerPickupItemEvent e) {
	    if(spectators.contains(e.getPlayer())){
	    	e.setCancelled(true);
	    }
	}
	
	/**
	 * EntityDamageEvent, Is Called When
	 * A Player Is Damaged By Something Other Than A Player.
	 * The Event Is Cancelled If The Game Hasn't Started
	 * Yet, Or If The Game Is Over.
	 * 
	 * @param e
	 * 
	 */
	@EventHandler
	public void hit(EntityDamageEvent e){
		try{
			Player p = (Player)e.getEntity();
			if((started==false)|(finished==true)){
				if(players.contains(p)){
					e.setCancelled(true);
				}
			}
		}catch(Exception e1){}
	}
	
	
	
	/**
	 * EntityDamageByEntityEvent, Is Called
	 * When A Player Damages Another Player. This
	 * Event Is Cancelled If The Game Has Not Been
	 * Running For More Than 15 Seconds Yet.
	 * 
	 * @param e
	 * 
	 */
	@EventHandler
	public void hitOther(EntityDamageByEntityEvent e){
		try{
			if(e.getDamager() instanceof Player){
				Player d = (Player)e.getDamager();
				if((this.time<=15)&(players.contains(d))&(started==true)){
					a(d);d.sendMessage(m.sd()+m.w()+">> Damage Blocked By "+re+bo+"Grace Period!");b(d);
					d.playSound(d.getLocation(), Sound.BAT_HURT, 1, .5f);
					e.setCancelled(true);
				}
				if(spectators.contains(d)){
					e.setCancelled(true);
				}
			}
		}catch(Exception e1){}
	}
	
	
	
	/**
	 * PlayerInteractEvent, Is Called When
	 * A Player Interacted With Something.
	 * 
	 * @param e
	 * 
	 */
	@EventHandler
    public void interact(PlayerInteractEvent e){
		try{
			Player p = e.getPlayer();
			if(players.contains(p)){
				if(started==true){
					try{
						Location l = e.getClickedBlock().getLocation();
						if(m.calcDist(l,sp.get(0).getSpawnPoint(slotNumber))<90){
							if(e.getClickedBlock().getType()==Material.CHEST){
								if(!chests.contains(l)){
									m.fillChest(l);
									chests.add(l);
								}
							}		
						}
					}catch(Exception e1){}
					try{
						if(p.getItemInHand().getType()==Material.BLAZE_ROD){
							if(finished==false){
								if(dm==false){
									for(Player pa:players){
										pa.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 400,2));
									}
									for(DarkMatter d:darkM){
										d.setVictim(null);
									}
									for(Player pl:players){
										dmfbm(pl);
									}
									for(Player pl:spectators){
										dmfbm(pl);
									}
									if(p.getItemInHand().getAmount() <= 1){
										p.getInventory().remove(p.getItemInHand());
									} else {
										p.getItemInHand().setAmount(p.getItemInHand().getAmount()-1);
									}	
								} else {
									a(p);p.sendMessage(m.sd()+m.e()+"This Can't Be Used");
									p.sendMessage(m.sd()+m.e()+"During DeathMatch!");b(p);
								}
							}else{
								a(p);p.sendMessage(m.sd()+m.e()+"The Match Is Over");
								p.sendMessage(m.sd()+m.e()+"Item Can't Be Used!");b(p);
							}
						}
					}catch(Exception e1){}
				}else{
					e.setCancelled(true);
				}
			}
			if(spectators.contains(e.getPlayer())){
				if(p.getItemInHand().getType()==Material.GOLDEN_APPLE){
					revH(p);
				}	
				e.setCancelled(true);
			}
		}catch(Exception e1){}
    }
	
	
	
	/**
	 * PlayerMoveEvent, Is Called When A Player
	 * Moves. This Prevents A Player From Going Any
	 * Where Before A Match Begins.
	 * 
	 * @param e
	 * 
	 */
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerMove(PlayerMoveEvent e) {
		try{
			if(time>28){
				if(players.contains(e.getPlayer())){
					if(started == false){
						if(e.getFrom().getX() == e.getTo().getX() && e.getFrom().getZ() == e.getTo().getZ()){
							return;
						} else {
							e.getPlayer().teleport(new Location(e.getFrom().getWorld(),e.getFrom().getBlockX(),e.getFrom().getBlockY(),e.getFrom().getBlockZ(),e.getPlayer().getLocation().getYaw(),e.getPlayer().getLocation().getPitch()));
						}
					} 
				}
			}
		}catch(Exception e1){}
		try{
			Player p = e.getPlayer();
			if(spectators.contains(p)){
				Location l=getSpawnPoints().get(r.nextInt(getSpawnPoints().size())).getSpawnPoint(slotNumber);
				if(m.calcDist(p.getLocation(),l)>70){
					p.teleport(l);a(p);p.sendMessage(m.sd()+m.e()+">> Returned To Arena Space!");b(p);
				}
			}
		}catch(Exception e1){}
	}
	
	
	
	/**
	 * PlayerQuitEvent, Is Called When A
	 * Player Leaves The Server. If They Were
	 * In A Game, Their Stats Suffer For Running
	 * Away.
	 * 
	 * @param e
	 * 
	 */
	@EventHandler
	public void quit(PlayerQuitEvent e){
		try{
			if(players.contains(e.getPlayer())){
				this.players=remove(players,e.getPlayer());
				ScoreBoardManager.removeScoreBoard(e.getPlayer());
				Stats s = m.checkProfile(e.getPlayer());
				s.setDeaths(s.getDeaths(), 1);
				if(players.size()<=1){
					finished = true;
				}
			}
		}catch(Exception e1){}
	}
	
	
	
	/**
	 * PlayerCommandPreprocessEvent, Is Called When
	 * A Player Issues A Server Command. This Blocks All
	 * Commands While The Player Is In Game, Only /Leave Works
	 * At This Point.
	 * 
	 * @param e
	 * 
	 */
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerCommand(PlayerCommandPreprocessEvent e) {
		Player pla = e.getPlayer();
		try{
			if(players.contains(pla) | spectators.contains(pla)){
				if((e.getMessage().equalsIgnoreCase("/Leave"))){
					leave(pla);
					e.setCancelled(true);
				} else {
					e.setCancelled(true);
					a(pla);pla.sendMessage(m.sd()+m.e()+">> Commands Are Currently Disabled!");;
					pla.sendMessage(m.sd()+m.w()+">> Do /Leave To Leave The Arena");pla.sendMessage(m.sd());b(pla);
				}
			} 
		}catch(Exception e5){}
	}
	
	
	
	/**
	 * Arena Object Constructor
	 * 
	 * @param b
	 * @param m
	 * @param name
	 * @param t
	 * @param state
	 * 
	 */
	Arena(BlockNode b,ArenaManager m,String name,ArrayList<TBlock> t, boolean state){
		if(b == null){
			b = new BlockNode(t.get(0),null);
			BlockNode head = b;
			for(int x = 1;x<t.size();x++){
				b = b.addNodeAfter(t.get(x));
			}
			this.b = head;
		} else {
			this.b = b;
		}
		this.m = m;
		this.name = name;
		this.state = state;
	}
	
	
	
	/**
	 * Second Arena Object Constructor
	 * 
	 * @param arena
	 * 
	 */
	Arena(Arena arena){
		this.name = arena.getName();
		this.m = arena.getM();
		for(int x = 0;x<arena.getSpawnPoints().size();x++){
			Location l = arena.getSpawnPoints().get(x).getSpawnPoint(0);
			addSpawnPoint(new Location(l.getWorld(),l.getBlockX(),l.getBlockY(),l.getBlockZ(),l.getYaw(),l.getPitch()));
		}
		g=new GUI(slotNumber,m);
		canRevive=new ArrayList<>();
		timers=new ArrayList<>();
		Bukkit.getServer().getPluginManager().registerEvents(this,Main.getPlugin(Main.class));
	}
	
	
	
	/**
	 * Tick Method, Called By An External Runnable
	 * 
	 */
	@SuppressWarnings("deprecation")
	public void tick(){
		if(started==false){
			if(players.size()<2){
				for(Player p:players){
					a(p);p.sendMessage(m.sd()+m.e()+"Match Cancelled...");
					p.sendMessage(m.sd()+m.w()+"All Other Tributes Fled!");b(p);
					reset(-1);
				}
			}else{
				if((time == 0)&(t == 0)){
					for(Player p:players){
						p.sendMessage(m.sd()+m.e()+">> Tping To Arena In 30!");
						p.sendMessage(m.sd());b(p);
					}
				}
			}
			t++;
			if((t%20)==0){
				time++;
				if(time==1){
					for(Player p:players){
						m.giveItem(p, new ItemStack(Material.ENDER_PEARL,1),8,m.w()+"LEAVE "+m.w(),"",wh+bo+"- "+gr+bo+"Return To The Lobby!",wh+bo+"- "+go+bo+"If You Like Micro SG",wh+bo+"- "+re+bo+"Please Support Us!"," ","#Micro-SG");
						m.giveItem(p, new ItemStack(Material.STAINED_GLASS_PANE,1),27,".");
					}
				}
				if(time==30){
					for(int a = 0;a<players.size();a++){
						Player p =players.get(a);
						p.teleport(sp.get(a).getSpawnPoint(slotNumber));
						a(p);p.sendMessage(m.sd()+m.s()+">> We're Starting In 10");b(p);
						p.setGameMode(GameMode.SURVIVAL);
						m.remItems(slotNumber);
					}
				}
				if(time < 30){
					for(Player p:players){
						p.playSound(p.getLocation(), Sound.NOTE_STICKS, 2.0f, .5f);
					}
				}
				if(time>=30){
					cnt=new Timer(this,10);cnt.start(1);t4=2.0f;
					if(time==cdn){
						started=true;
						time=0;t=0;
						for(Player p:players){
							heal(p);
							try{
								Stats s=m.checkProfile(p);
								for(int i:s.getItems()){
									m.giveItem(p, new ItemStack(Material.getMaterial(i),1),0,"","");
								}
								m.checkProfile(p).initItems();
							}catch(Exception e1){}
							p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 600,2));
							p.closeInventory();
							p.playSound(p.getLocation(), Sound.EXPLODE, 2.0f, .5f);
							a(p);p.sendMessage(m.sd()+m.s()+">> GO!!!");
							p.sendMessage(m.sd()+m.w()+">> ");
							p.sendMessage(m.sd()+m.w()+">> 15 Second Grace Period");
							p.sendMessage(m.sd()+m.e()+">> First Dark Matter In 25 Sec");
							p.sendMessage(m.sd()+m.e()+">> Avoid Dark Matter At All Costs!");
							p.sendMessage(m.sd()+m.e()+">> Loot Chest And KILL EVERYONE!");
							p.sendMessage(m.sd());b(p);
							p.setVelocity(p.getLocation().getDirection().multiply(2));
						}
					}
				}
			} 
			if(t%2==0){
				for(Player p:players){
					g.intro(p,time,players.size(),cdn);
				}
			}
		} else {
			if(finished == false){
				t++;
				if((t%20)==0){
					t=t%20;
					this.time++;
					if(((time%25)==0)&&(darkM.size()<25)){
						darkM.add(new DarkMatter(sp.get(r.nextInt(sp.size())).getSpawnPoint(slotNumber),this));
					}
					for(Player pa:players){
						if(m.calcDist(pa.getLocation(),sp.get(r.nextInt(m.getMaxPlayers())).getSpawnPoint(slotNumber)) > 60){
							pa.damage(2);
							a(pa);pa.sendMessage(m.sd()+m.e()+">> RETURN TO PLAYABLE AREA!");b(pa);
							pa.playSound(pa.getLocation(), Sound.NOTE_BASS_GUITAR, .5f, 10);
						}
					}
					for(Player sp:spectators){
						if(sp.getGameMode()!=GameMode.CREATIVE){
							sp.setGameMode(GameMode.CREATIVE);
						}
					}
					if(time%m.getDropFreq()==0){
						drop=new Timer(this,2);
						drop.start(1);int drops = (r.nextInt(10)+1);
						for(int x = 0;x<drops;x++){
							dropPackage();
						}
						for(Player p:players){
							a(p);p.sendMessage(m.sd()+m.i()+"✦ There Are "+drops);
							p.sendMessage(m.sd()+m.i()+"✦ Drop Package(s) To Find!");
							p.sendMessage(m.sd()+m.i()+"✦ Now.. Survive Another "+m.getDropFreq()+" Sec!");b(p);
						}
						for(Player p:spectators){
							a(p);p.sendMessage(m.sd()+m.i()+"✦ There Are "+drops);
							p.sendMessage(m.sd()+m.i()+"✦ Drop Package(s) To Find!");
							p.sendMessage(m.sd()+m.i()+"✦ Now.. Survive Another "+m.getDropFreq()+" sec!");b(p);
						}
					}
					if((time>360)&(dm==false)){
						announceDeathMatch();
						dm=true;
					}
				}
				if(players.size()<2){finish();}
				if(dm==false){
					if((t%m.getDmSpd()==0)){
						for(DarkMatter d:darkM){
							d.tick();
						}	
					}
				} else {
					for(DarkMatter d:darkM){
						d.tick();
					}
				}	
			} else {
				t++;
				if(end > 0){
					end--;
					if(end%10==0){
						m.sTF(m.setTarget(getSpawnPoints().get(r.nextInt(getSpawnPoints().size())).getSpawnPoint(slotNumber)));sA();
					}
				} else {
					try{
						for(DarkMatter d:darkM){
							d.tick();
						}
					}catch(Exception e1){}
					reset(0);
				}
			}
		}
		try{
			if(t%25==0)
			scoreboard();
		}catch(Exception e1){}
		try{
			g.specMenu(players,this);
		}catch(Exception e1){}
	}
	
	
	
	/**
	 * DropTick Method, Called By An External Runnable,
	 * Which Is Responsible For Dropping Care Packages
	 * On The Map For The Tributes To Load Up On Gear
	 * 
	 */
	public void dropTick(){
		t3++;
		if(t3%8==0){
			t2++;
			for(Player p:players){
				p.playSound(p.getLocation(),Sound.FIREWORK_TWINKLE,2.0f,1f);
			}
			for(Player p:spectators){
				p.playSound(p.getLocation(),Sound.FIREWORK_TWINKLE,2.0f,1f);
			}
		}
		if(t2>20){
			drop.stop();
			drop=null;
		}
	}
	
	
	
	/**
	 * cntTick, Called During The Arena Countdown
	 * Before The Game Has Begun.
	 * 
	 */
	public void cntTick(){
		if(t4>.4){
			for(Player p:players){
				p.playSound(p.getLocation(),Sound.NOTE_BASS,.5f,t4);
			}
			t4-=.1;
		}else{
			cnt.stop();cnt=null;
		}
	}
	
	/**
	 * Reset Method, Called When A Game Is Over. Updates
	 * All Involved Players' States Finishes The Game And
	 * Destroys This Instance Of A Micro SG Match.
	 * 
	 * @param reason
	 * 
	 */
	public void reset(int reason){
		m.cReport("A Game Has Ended");
		time = 0;
		t = 0;
		end = 150;
		finished = true;
		try{
			for(Player p:players){
				rH(p);
			}
			if(reason==0){
				Player p = players.get(0);
				Stats s = m.checkProfile(p);
				int mult=m.getMult(p);
				s.setWins(s.getWins(),1,(mult*m.getMult()));
				a(p);p.sendMessage(m.sd()+m.s()+"CONGRATULATIONS!!!");
				if(m.getMult()<=1){
					m.cReport(p.getName()+" Was Victorious On Arena: "+getName()) ;
					p.sendMessage(m.sd()+m.w()+">> +(100x"+mult+"): "+(100*mult)+" Points For That Win!");b(p);
				}else{
					p.playSound(players.get(0).getLocation(), Sound.LEVEL_UP,1,.5f);
					p.sendMessage(m.sd()+m.s()+">> "+getMultWord(m.getMult())+" Point Weekend!");
					p.sendMessage(m.sd()+m.s()+">> +(100x"+(mult*m.getMult())+"): "+(100*(mult*m.getMult()))+" Points For That Win!");b(p);
				}
			}
			players.clear();	
		}catch(Exception e1){}
		try{
			for(Player p:spectators){
				rH(p);
			}
			spectators.clear();
		}catch(Exception e1){}
		try{
			for(DarkMatter d:darkM){
				d.tick();
			}
		}catch(Exception e1){}
		darkM.clear();
		try{canRevive.clear();}catch(Exception e1){}
		try{timers.clear();}catch(Exception e1){}
		try{chests.clear();}catch(Exception e1){}
		chests=null;
		g=null;
		try{
			drop.stop();
			drop=null;
		}catch(Exception e1){}
		try{
			cnt.stop();
			cnt=null;
		}catch(Exception e1){}
		m.destroyMatch(this);
	}
	
	/**
	 * Returns The CountDown
	 * 
	 * @return
	 * 
	 */
	public int getCdn(){
		return cdn;
	}
	
	/**
	 * This Method Stands For Reset Helper
	 * Assist The Reset Method And Is Meant
	 * To Keep The Code As Clean As Possible.
	 * 
	 * @param p
	 * 
	 */
	private void rH(Player p){
		try{
			ScoreBoardManager.removeScoreBoard(p);
			p.setGameMode(GameMode.SURVIVAL);
			m.showPlayer(p);heal(p);p.teleport(m.getLobby());
			p.getInventory().setBoots(null);p.getInventory().setLeggings(null);
			p.getInventory().setChestplate(null);p.getInventory().setHelmet(null);
			m.resetXP(p);m.addToClear(p);m.addToRefresh(p);
		}catch(Exception e1){}
	}
	
	
	
	/**
	 * sA Method, Used By The Lobby Fireworks
	 * 
	 */
	private void sA(){
		for(Player p:players){
			p.playSound(p.getLocation(),Sound.FIREWORK_LARGE_BLAST,1,1);
		}
		for(Player p:spectators){
			p.playSound(p.getLocation(),Sound.FIREWORK_LARGE_BLAST,1,1);
		}
	}
	
	
	
	/**
	 * Returns The List Of Players That Are In
	 * This Match.
	 * 
	 * @return
	 * 
	 */
	public ArrayList<Player> getPlayers(){
		return this.players;
	}
	
	
	
	/**
	 * Returns The List Of Spectators That Are
	 * Currently Watching This Game.
	 * 
	 * @return
	 * 
	 */
	public ArrayList<Player> getSpectators(){
		return this.spectators;
	}
	
	
	
	/**
	 * Returns The List Of Names Of Thos Spectators
	 * That Are Eligable For Revival. If The Player
	 * Joined In To Spectate After The Game Has Started,
	 * Their Name Will Not Be On This List. This List
	 * Will Only Contain The Names Of Those Who Have Fallen
	 * In Battle In THIS Arena. Moving Between Arenas Invalidates
	 * A Player Ability To Revive Back Into A Game That They Were
	 * Playing.
	 * 
	 * @return
	 * 
	 */
	public ArrayList<String> getSpecList(){
		return this.canRevive;
	}
	
	
	
	/**
	 * Returns The List Of Active Dark Matters
	 * 
	 * @return
	 * 
	 */
	public ArrayList<DarkMatter> getDarkMatter(){
		return this.darkM;
	}
	
	
	
	/**
	 * Returns The Time Elapsed For This Game
	 * So Far.
	 * 
	 * @return
	 * 
	 */
	public int getTime(){
		return this.time;
	}
	
	
	
	/**
	 * Restores The Arena Template Structure When
	 * The Plugin Is Starting Up.
	 * 
	 * @param t
	 * 
	 */
	public void setArena(ArrayList<TBlock> t){
		if(t != null){
			this.b = new BlockNode(t.get(0),null);
			BlockNode head = b;
			for(int x = 1;x<t.size();x++){
				b = b.addNodeAfter(t.get(x));
			}
			this.b = head;
		} else {
			this.b = null;
		}
		
	}
	
	
	
	/**
	 * Sets One Of The Pods In This Arena Template
	 * Up To The Max Number Of Players.
	 * 
	 * @param l
	 * @return
	 * 
	 */
	public int addSpawnPoint(Location l){
		if(sp.size()<m.getMaxPlayers()){
			sp.add(new SpawnPoint(this,l));
			return 0;
		} else {
			return -2;
		}
	}
	
	
	
	/**
	 * Sends A Message To All Spectators And
	 * Active Players.
	 * 
	 * @param message
	 * 
	 */
	public void sendMessageToAll(String message){
		for(Player p:players){
			p.sendMessage(message);
		}
		for(Player p:spectators){
			p.sendMessage(message);
		}
	}
	
	
	
	/**
	 * Drops A Care Package On To This Map
	 * 
	 */
	@SuppressWarnings("deprecation")
	public void dropPackage(){
		t2=15;
		t3=0;
		Location l = darkM.get(0).setTarget();
    	FallingBlock fb = l.getWorld().spawnFallingBlock(new Location(l.getWorld(),l.getBlockX(),(l.getBlockY()+10),l.getBlockZ()), Material.CHEST, (byte)0);
    	fb.setDropItem(false);
	}
	
	
	
	/**
	 * Returns The Worded Representation Of The
	 * Points Multiplier That Players Will Receive
	 * For Kills And Wins.
	 * 
	 * @param i
	 * @return
	 * 
	 */
	public String getMultWord(int i){
		String[]s=new String[]{"Double","Triple","Quadruple","Quintuple"};
		return s[i-2];
	}
	
	
	
	/**
	 * Allows A Player To Leave The Game
	 * 
	 * @param pla
	 * 
	 */
	public void leave(Player pla){
		if(players.contains(pla)){
			l(pla);	this.players=remove(players,pla);
			if(players.size()>1){
				for(Player p:players){
					a(p);p.sendMessage(m.sd()+m.w()+">> TRIBUTE, "+pla.getName()+" Fled!");b(p);
				}
				for(Player p:spectators){
					a(p);p.sendMessage(m.w()+">> TRIBUTE, "+pla.getName()+" Fled!");b(p);
				}
			}else{
				finish();
			}
		}else if(spectators.contains(pla)){
			l(pla);this.spectators=removeS(spectators,pla);
		}
	}
	
	
	
	/**
	 * A Helper Method For The Above Leave
	 * Method.
	 * 
	 * @param p
	 * 
	 */
	private void l(Player p){
		try{ScoreBoardManager.removeScoreBoard(p);}catch(Exception e1){}
		p.getInventory().clear();
		p.teleport(m.getLobby());
		a(p);p.sendMessage(m.sd()+m.s()+"You Have Left The Game!");b(p);
		m.addToClear(p);m.addToRefresh(p);m.showPlayer(p);
	}
	
	
	
	/**
	 * Brings An Outside Player Into The Arena
	 * To Spectate The Remaining Tributes. All
	 * Spectators Are Undetectable To The Tributes.
	 * 
	 * @param p
	 * 
	 */
	public void spectate(Player p){
		m.addToClear(p);m.addToRefresh(p);
		if(!spectators.contains(p)){
			if(m.getQueue().contains(p)){
				m.setQueue(m.remove(m.getQueue(), p));
			}
			spectators.add(p);m.hidePlayer(p);heal(p);			
		} else {
			a(p);p.sendMessage(m.sd()+m.e()+"You're Already Watching This Game!");b(p);
		}
	}
	
	
	
	/**
	 * A Message Formatting Method.
	 * 
	 * @param p
	 * 
	 */
	private void a(Player p){
		p.sendMessage("\n"+m.hT());
	}
	
	
	
	/**
	 * A Message Formatting Method.
	 * 
	 * @param p
	 * 
	 */
	private void b(Player p){	
		p.sendMessage(m.hB());
	}
	
	
	
	/**
	 * This Method Stands For Game End
	 * 
	 * @param p
	 * 
	 */
	public void gE(Player p){
		a(p);p.sendMessage(m.sd()+m.e()+"This Match Has Finished!");
		p.sendMessage(m.sd()+m.e()+"If You Liked This Game Please");
		p.sendMessage(m.sd()+m.e()+"Support The Server!");
		p.sendMessage(m.sd());b(p);
	}
	
	
	
	/**
	 * Prevents An Eliminated Players From
	 * Performing Certain Action At Certain Times
	 * Of The Game.
	 * 
	 * @param p
	 * 
	 */
	public void revH(Player p){
		if(finished==false){
			if(dm==false){
				revive(p);
			}else {
				a(p);p.sendMessage(m.sd()+m.e()+"Can't Use This Now!");
				p.sendMessage(m.sd()+m.w()+"DeathMatch Has Started");b(p);
			}
		} else{
			a(p);p.sendMessage(m.sd()+m.e()+"Can't Use This Now!");
			p.sendMessage(m.sd()+m.w()+"This Match Has Finished");b(p);
		}
	}
	
	
	
	/**
	 * Revives This Player
	 * 
	 * @param pla
	 * 
	 */
	@SuppressWarnings("deprecation")
	public void revive(Player pla){
		m.cReport(pla.getName()+" Revived Themselves");
		m.addToClear(pla);
		announceRevive(pla);
		spectators=removeS(spectators,pla);players.add(pla);
		canRevive.remove(pla.getName());
		m.showPlayer(pla);heal(pla);
		pla.setGameMode(GameMode.SURVIVAL);
		m.addGiveChest(pla);
		timers.add(new Timer(this,pla,4));
		timers.get(timers.size()-1).start(1);
		Stats s = m.checkProfile(pla);
		s.setElixer(s.getElixers(),-1);
		s.setRevives(s.getRevives(),1);
		pla.playEffect(pla.getLocation(), Effect.POTION_BREAK, 3);
		a(pla);pla.sendMessage(m.sd()+m.s()+">> You've Been Revived!");
		pla.sendMessage(m.sd()+m.w()+">> Score -2 For That Revive!");
		pla.sendMessage(m.sd()+m.e()+">> You've Received 1 Chest!");
		pla.sendMessage(m.sd()+m.e()+">> You Must Place It To Get Other Items!");b(pla);
	}
	
	
	
	/**
	 * Alerts Everyone That A Player Has Used
	 * A Dark Matter FlashBang.
	 * 
	 * @param pl
	 */
	public void dmfbm(Player pl){
		pl.playSound(pl.getLocation(),Sound.AMBIENCE_THUNDER, 1, 2.0f);
		pl.sendMessage("\n"+wh+bo+">>"+gr+bo+" Someone Used A Dark Matter Flash Bang!");
		pl.sendMessage(wh+bo+">>"+gr+bo+" All Dark Matters Have Lost Their Targets!");
		pl.sendMessage(wh+bo+">>"+gr+bo+" 20 Second Dark Matter Grace Period, STARTS NOW!");
	}
	
	
	
	/**
	 * Announces To Everyone That A Player Died Horribly
	 * By Some Reason Other Than Dark Matter Or Another
	 * Tribute.
	 * 
	 * @param pl
	 * 
	 */
	public void announceNoobDeath(Player pl){
		for(Player p:players){
			a(p);p.sendMessage(m.sd()+m.w()+pl.getName()+" Just");
			p.sendMessage(m.sd()+m.w()+"Died... Theeee End");b(p);
		}
		for(Player p:spectators){
			a(p);p.sendMessage(m.sd()+m.w()+pl.getName()+" Just");
			p.sendMessage(m.sd()+m.w()+"Died... Theeee End");b(p);
		}
	}
	
	
	
	/**
	 * Announces The Death Of A Tribute At The Hand
	 * Or Another Tribute.
	 * 
	 * @param pl
	 * @param kl
	 * 
	 */
	public void announceDeath(Player pl,Player kl){
		for(Player p:players){
			a(p);p.sendMessage(m.sd()+m.w()+pl.getName()+" Was");
			p.sendMessage(m.sd()+m.w()+"Conquered By "+ kl.getName());b(p);
		}
		for(Player p:spectators){
			a(p);p.sendMessage(m.sd()+m.w()+pl.getName()+" Was");
			p.sendMessage(m.sd()+m.w()+"Conquered By "+ kl.getName());b(p);
		}
	}
	
	
	
	/**
	 * Announces The Beginning Of This Arena's DeathMatch
	 * 
	 */
	public void announceDeathMatch(){
		for(int x=0;x<players.size();x++){
			players.get(x).sendMessage(go+bo+"Oh Yeah By The Way... "+re+bo+"DEATHMATCH!");
			darkM.get(x).setVictim(players.get(x));
		}
		for(Player p:spectators){
			p.sendMessage(go+bo+"Oh Yeah By The Way... "+re+bo+"DEATHMATCH!");
		}
	}
	
	
	
	/**
	 * Announces To The Other Tributes That
	 * A Once Fallen Tribute Is Back From The Dead.
	 * 
	 * @param pla
	 * 
	 */
	public void announceRevive(Player pla){
		for(Player p:players){
			p.playSound(p.getLocation(),Sound.ENDERMAN_SCREAM,1,2);
			a(p);p.sendMessage(m.sd()+m.w()+">> Tribute "+pla.getName());
			p.sendMessage(m.sd()+m.w()+">> Has Been Revived!");b(p);
		}
		for(Player p:spectators){
				p.playSound(p.getLocation(),Sound.ENDERMAN_SCREAM,1,2);
				a(p);p.sendMessage(m.sd()+m.w()+">> Tribute "+pla.getName());
				p.sendMessage(m.sd()+m.w()+">> Has Been Revived!");b(p);
		}
	}
	
	
	
	/**
	 * The Sounds That The Revived Tributes Hears
	 * 
	 * @param p
	 * @param flag
	 * 
	 */
	public void ReviveSound(Player p,int flag){
		if(flag>30){
			timers.get(0).stop();
			timers.remove(0);
		} else {
			p.playSound(p.getLocation(),Sound.VILLAGER_DEATH,1,.5f+((flag%15)*.1f));
		}
	}
	
	
	
	/**
	 * Sets Up And Displays A Scoreboard.
	 * 
	 * @return
	 * 
	 */
	public void scoreboard(){
		ArrayList<String> s = new ArrayList<>();
		if(started==false){
			String[]ss=new String[]{"",gr+"✦ Tributes: ",wh+bo+">> "+players.size()+"","  ",ye+"✦ Map: ",wh+bo+">> "+getName(),"   ",go+"✦ Count-Down: ",wh+bo+">> "+(cdn-time)};
			for(String sss:ss){
				s.add(sss);
			}
			if(this.ready==false){
				if(!m.getProgress().contains("100%")){
					s.add("    ");
					s.add(re+"✦ Preparing:");
					s.add(wh+bo+">> "+m.getProgress());
				} else {
					s.add("    ");
					s.add(re+"✦ Ok Ready!");
					this.ready=true;
				}
			}
		} else {
			if(finished==false){
				String[]ss=new String[]{"",gr+"✦ Tributes: ",wh+bo+">> "+players.size()+"","  ",ye+"✦ Watchers: ",wh+bo+">> "+spectators.size(),"   ",re+"✦ Dark Mobs: ",wh+bo+">> "+darkM.size(),"    ",go+"✦ Time: ",wh+bo+">> "+getFormattedTime()};
				for(String sss:ss){
					s.add(sss);
				}
			} else {
				try{
					String[]ss=new String[]{"",gr+"✦ VICTOR: ",wh+bo+">> "+players.get(0).getName().substring(0, 6)+"...","  ",ye+"✦ If You",ye+"✦ Liked This",ye+"✦ Game,",ye+"✦ Please",ye+"✦ Support",ye+"✦ Us!"};
					for(String sss:ss){
						s.add(sss);
					}
				}catch(Exception e1){}
			}	
		}
		ScoreBoardManager.updateScoreBoard(players, s);
		ScoreBoardManager.updateScoreBoard(spectators, s);
	}
	
	

	/**
	 * Returns A Formatted Version Of The Current
	 * Arena Time Elapsed.
	 * 
	 * @return
	 * 
	 */
	public String getFormattedTime(){
		return ((int)time/60)+"m : "+(time%60)+"s";
	}
	
	
	
	/**
	 * Heals A Player
	 * 
	 * @param p
	 * 
	 */
	public void heal(Player p){
		p.getInventory().clear();
		p.setHealth(20);
		p.setFoodLevel(20);
	}

	
	
	/**
	 * Returns An Error Message If Something
	 * Is Preventing An Arena From Being Used.
	 * 
	 * @return
	 * 
	 */
	public String checkStatus(){
		String error=wh+bo+"Code: ";
		if(getSpawnPoints().size()<m.getMaxPlayers()){
			error+=re+bo+"-1"+re+" >> Max Pod Error!";
		} else if(m.getLobby()==null){
			error+=re+bo+"-2"+re+" >> Lobby Not Set!";
		} else if(m.getJoinSpot()==null){
			error+=re+bo+"-3"+re+" >> Join Area Not Set!";
		} else if(state==false){
			error+=re+bo+"-4"+re+" >> Arena Disabled!";
		} else {
			error+=gr+bo+"0 >> Ok!";
		}
		return error;
	}
	
	
	
	/**
	 * Returns The State Of This Arena
	 * 
	 * @return
	 * 
	 */
	public boolean getState(){
		return state;
	}
	
	
	
	/**
	 * Toggles This Arena As Playable
	 * Or Un-Playable
	 * 
	 * @return
	 * 
	 */
	public String toggle(){
		if(state==false){
			this.state=true;
			return wh+bo+"Arena \""+getName()+"\" - "+gr+bo+"ENABLED!";
		}else{
			this.state=false;
			return wh+bo+"Arena \""+getName()+"\" - "+re+bo+"DISABLED!";
		}
	}
	
	
	
	/**
	 * Sets The List Of Pods For This Arena.
	 * 
	 * @param sp
	 * 
	 */
	public void setSpawns(ArrayList<SpawnPoint> sp){
		this.sp = sp;
	}
	
	
	
	/**
	 * Returns The List Of Pods For This Arena.
	 * 
	 * @return
	 * 
	 */
	public ArrayList<SpawnPoint> getSpawnPoints(){
		return sp;
	}
	
	
	
	/**
	 * Returns Reference To The ArenaManager.
	 * 
	 * @return
	 * 
	 */
	public ArenaManager getM(){
		return m;
	}
	
	
	
	/**
	 * Returns First Block Node In Th Linked
	 * List That Makes Up This Arena.
	 * 
	 * @return
	 * 
	 */
	public BlockNode getArena(){
		return b;
	}
	
	
	
	/**
	 * Sets The Name Of This Arena.
	 * 
	 * @param newName
	 * 
	 */
	public void setName(String newName){
		this.name = newName;
	}
	
	
	
	/**
	 * Returns The Name Of This Arena
	 * 
	 * @return
	 * 
	 */
	public String getName(){
		return name;
	}
	
	
	
	/**
	 * Assigns A Slot Number To This Arena, For
	 * Where It Will Be Generated At.
	 * 
	 * @param slot
	 * 
	 */
	public void setSlotNumber(int slot){
		this.slotNumber = slot;
	}
	
	
	
	/**
	 * Returns The Assigned Slot Number.
	 * 
	 * @return
	 * 
	 */
	public int getSlotNumber(){
		return slotNumber;
	}
	
	
	
	/**
	 * This Is Called When The Last Opponent Of
	 * A Tribute Is Defeated. Then A Celebration
	 * Begins!
	 * 
	 */
	public void finish(){
		try{
			this.finished=true;
			players.get(0).setGameMode(GameMode.CREATIVE);
		}catch(Exception e1){}
	}
	
	
	
	/**
	 * Returns A Boolean Value On Whether
	 * Or Not This Arena Has Finished Its
	 * Game Yet.
	 * 
	 * @return
	 * 
	 */
	public boolean isFinished(){
		return finished;
	}
	
	
	
	/**
	 * Returns If This Arena Is Underway Yet.
	 * 
	 * @return
	 * 
	 */
	public boolean hasStarted(){
		return started;
	}
	
	
	
	/**
	 * Returns The GUI
	 * 
	 * @return
	 * 
	 */
	public GUI GUI(){
		return g;
	}
	
	
	
	/**
	 * Removes A Player From The List Of
	 * Active Tributes.
	 * 
	 * @param source
	 * @param p
	 * @return
	 * 
	 */
	public ArrayList<Player> remove(ArrayList<Player> source,Player p){
		ArrayList<Player> pl=new ArrayList<>();
		for(Player pla:source){
			if(pla.equals(p)){}else{
				pl.add(pla);
			}
		}
		this.players=new ArrayList<>();
		return pl;
	}
	
	
	
	/**
	 * Removes A Spectator
	 * 
	 * @param source
	 * @param p
	 * @return
	 * 
	 */
	public ArrayList<Player> removeS(ArrayList<Player> source,Player p){
		ArrayList<Player> spt=new ArrayList<>();
		for(Player pla:source){
			if(pla.equals(p)){}else{
				spt.add(pla);
			}
		}
		this.spectators=new ArrayList<>();
		return spt;
	}
	
	
	
	/**
	 * Determines The Nearest Player.
	 * 
	 * @param p
	 * @return
	 * 
	 */
	public String getNearest(Player p){
		double dist=1000;
		double temp=0;
		String closest="";
		for(Player pl:players){
			temp=m.calcDist(p.getLocation(),pl.getLocation());
			if(temp>0&temp<dist){
				dist=temp;closest=pl.getName()+" > Dist: "+(int)dist;
			}
		}
		return closest;
	}
	
	
	
	/**
	 * Finds An Active In Game Tribute By
	 * Their Name.
	 * 
	 * @param name
	 * @return
	 * 
	 */
	public Player findPlayerByName(String name){
		for(Player p:players){
			if(name.equalsIgnoreCase(wh+bo+p.getName())){
				return p;
			}
		}
		return null;
	}
	
	
	
	/**
	 * Determines If A Particular Player Is Eligible
	 * To Be Revived.
	 * 
	 * @param p
	 * @return
	 * 
	 */
	public boolean checkCanRevive(Player p){
		for(String s:canRevive){
			if(p.getName().equalsIgnoreCase(s)){
				return true;
			}
		}
		return false;
	}
}
