package me.virusbrandon.HoloBeam;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import me.virusbrandon.localapis.ActionBarAPI;
import me.virusbrandon.localapis.GUIFactory;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import de.inventivegames.hologram.HologramAPI;


public class Main extends JavaPlugin implements Listener{
	private HoloManager man;
	private String bl = ChatColor.BLACK+"",re=ChatColor.RED+"",gr=ChatColor.GREEN+"",ye=ChatColor.YELLOW+"",bo=ChatColor.BOLD+"",ul=ChatColor.UNDERLINE+"",wh=ChatColor.WHITE+"",db=ChatColor.DARK_BLUE+"",dg=ChatColor.DARK_GREEN+"",da=ChatColor.DARK_AQUA+"",dr=ChatColor.DARK_RED+"",dp=ChatColor.DARK_PURPLE+"",go=ChatColor.GOLD+"",ga=ChatColor.GRAY+"",gg=ChatColor.DARK_GRAY+"",bu=ChatColor.BLUE+"",aq=ChatColor.AQUA+"",lp=ChatColor.LIGHT_PURPLE+"";
	private ArrayList<Action> actions = new ArrayList<>();
	private GUIFactory f = new GUIFactory();
	private ActionBarAPI b = new ActionBarAPI();
	private YamlConfiguration config;
	private List<Integer> bLB= new ArrayList<>();
	private ArrayList<CoolDown> cd = new ArrayList<>();
	private Timer t;
	private DecimalFormat df = new DecimalFormat("####.#");
	private ArrayList<Profile> pf = new ArrayList<>();
	
	/**
	 * The Interact Event:
	 * Responds To Your Interacting With Your HoloBeam Device.
	 * 
	 * @param e
	 */
	@SuppressWarnings("deprecation")
	@EventHandler
	public void interact(PlayerInteractEvent e){
		try{
			if(!actions.contains(e.getAction())){return;}
			Player p = e.getPlayer();
			List<String>l = new ArrayList<String>();
			ItemMeta met = p.getItemInHand().getItemMeta();
			if(met.hasLore()){l = met.getLore();}else{return;}
			if((l.get(l.size()-1).equalsIgnoreCase(bl+"#LASER"))&p.hasPermission("Holobeam.use")){
				CoolDown c = gCD(p,cd.size()-1);
				if(c!=null){return;}
				Location ll = p.getLocation();
				beam(new Location(ll.getWorld(),ll.getX(),(ll.getY()),ll.getZ()),p.getLocation().getDirection(),l.get(l.size()-2),config.getInt("BeamLength"),p.hasPermission("Holobeam.blockdamage"),p.hasPermission("Holobeam.harmfulbeam"),p);
				int i = Integer.parseInt(l.get(l.size()-6).substring(4,l.get(l.size()-6).length()));
				if(i<=1){
					p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 5, 1);
					p.setItemInHand(null);
				} else {
					met.setDisplayName(wh+bo+"HoloBeam - Ammo: "+gr+i);
					l.set(l.size()-6,gr+bo+(i-(p.hasPermission("HoloBeam.InfAmmo")?0:1))+"");
					met.setLore(l);
					p.getItemInHand().setItemMeta(met);
				}
				if(config.getInt("CoolDown")!=0){
					cd.add(new CoolDown(p,config.getInt("CoolDown"),this,wh+bo+"HoloBeam - Ammo: "+gr+(p.hasPermission("HoloBeam.InfAmmo")?"Infinite":i)));
				} else {
					b.sendActionBar(p, wh+bo+"HoloBeam - Ammo: "+gr+(p.hasPermission("HoloBeam.InfAmmo")?"Infinite":i));
				}
			}
		}catch(Exception e1){/* The Item Has Invalid Lore */}
	}
	
	/**
	 * The Inventory Click Event:
	 * 
	 */
	@EventHandler
	public void click(InventoryClickEvent e){
		try{
			ItemStack st = e.getCurrentItem();
			ItemMeta met = st.getItemMeta();
			List<String> l = new ArrayList<>();
			l = met.getLore();
			try{
				if(l.get(l.size()-1).equalsIgnoreCase(bl+"#BEAM")){
					e.setCancelled(true);
				}
			}catch(Exception e1){}
			Player p = (Player)e.getWhoClicked();
			if(l.get(l.size()-2).equalsIgnoreCase(bl+"#CHOOSE")){
				give(p,l.get(l.size()-3).substring(0,3));
				p.closeInventory();
				p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 1, 2);
			} else if(l.get(l.size()-2).equalsIgnoreCase(bl+"#RIGHT")){
				findProfile(p).gui().scroll(1);
			} else if(l.get(l.size()-2).equalsIgnoreCase(bl+"#LEFT")){
				findProfile(p).gui().scroll(-1);
			}
		}catch(Exception e1){}
	}
	
	
	/**
	 * Initiates Special Components, If Any Exist
	 * 
	 */
	@Override
	public void onEnable(){
		if(startupReq()){
			config();
			man = new HoloManager();
			Bukkit.getServer().getPluginManager().registerEvents(this, this);
			t = new Timer(this,1);
			t.start(1);
			actions.add(Action.LEFT_CLICK_AIR);
			actions.add(Action.RIGHT_CLICK_AIR);
			actions.add(Action.LEFT_CLICK_BLOCK);
			actions.add(Action.RIGHT_CLICK_BLOCK);
		} else {
			Bukkit.getConsoleSender().sendMessage(re+bo+"Holobeams Only Supports 1.9 And 1.10 ... SORRY O_O");
			try{
				throw new InvalidServerException(re+"This Server Is Not Supported!");
			}catch(Exception e1){}
			this.getServer().getPluginManager().disablePlugin(Main.getPlugin(Main.class));
		}
	}
	
	/**
	 * Performs Any Operations Needed Before Shutdown
	 * 
	 */
	@Override
	public void onDisable(){
		man.clearHolos();
		for(Player p:Bukkit.getOnlinePlayers()){
			if(p.getOpenInventory().getTopInventory().getTitle().equalsIgnoreCase(bl+bo+"Beam Color")){
				p.closeInventory();
			}
		}
		t.stop();
	}
	
	/**
	 * Checks Profiles When A Player Joins.
	 * Creates A Profile For The Players, When One Does Not Exist
	 * 
	 */
	@EventHandler
	public void join(PlayerJoinEvent e){
		Player p = e.getPlayer();
		Profile pp = new Profile(p,this);
		if(!pf.contains(pp)){
			pf.add(pp);
		}
	}
	
	
	/**
	 * On Command Function
	 * 
	 */
	@Override
	public boolean onCommand(CommandSender se, Command c, String lbl, String[] args){
		try{
			if(lbl.equalsIgnoreCase("Beam")|lbl.equalsIgnoreCase("B")&se.hasPermission("Holobeam.use")){
				if(se instanceof Player){
					Player p = (Player)se;
					try{
						if(args.length==0){findProfile(p).gui().open(p);return true;}
						if(args[0].equalsIgnoreCase("Sound")){
							config.set("SoundEnabled", config.getBoolean("SoundEnabled")?false:true);
							config.save(new File("plugins/HoloBeam/Config.yml"));
							p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 1, 2);
							p.sendMessage(gr+">> Sound Toggled - Now "+(config.getBoolean("SoundEnabled")?"ENABLED":"DISABLED"));
						} else if(p.hasPermission("Beam.Admin")&&args[0].equalsIgnoreCase("Reload")){
							config();
							p.sendMessage(gr+"HoloBeam Config Reloaded!");
							p.playSound(p.getLocation(),Sound.BLOCK_NOTE_PLING,1,2);
						} else {
							String s = cmdColor(args[0]);
							if(!s.equalsIgnoreCase("meh")){
								give(p,s+"█");
							} else {
								findProfile(p).gui().open(p);
							}
						}
					}catch(Exception e1){p.sendMessage(re+"HoloBeam - Invalid Command Syntax");}
				}
			} else {
				se.sendMessage(gr+bo+"Laser Beams Will Be Available For You Soon!");
			}
		}catch(Exception e1){return false;}
		return true;
	}
	
	/**
	 * Gives User Their HoloRifle
	 * 
	 */
	private void give(Player p, String bc){
		chkInv(p);
		ItemStack stack = new ItemStack(Material.BLAZE_ROD);
		ItemMeta met = stack.getItemMeta();
		met.setDisplayName(re+bo+"HOLO-BEAM");
		List<String>l = new ArrayList<String>();
		l.add(f.draw(wh+bo+ul+"_",15,""));
		l.add(wh+"Ammo:");
		l.add(gr+bo+config.getInt("AmmoPack"));
		l.add(f.draw(wh+bo+ul+"_",15,""));
		l.add(" ");
		l.add(wh+bo+"Beam Color:");
		l.add(bc);
		l.add(bl+"#LASER");
		met.setLore(l);
		stack.setItemMeta(met);
		p.getInventory().addItem(stack);
	}
	
	
	/**
	 * The Check Inventory Function:
	 * 
	 * Checks And Removes Existing Holo Rifles...
	 * Can Only Have One At A Time!
	 * 
	 */
	private void chkInv(Player p){
		for(int x=0;x<p.getInventory().getSize();x++){
			ItemStack i = p.getInventory().getItem(x);
			try{
				ItemMeta met = i.getItemMeta();
				List<String> l = new ArrayList<>();
				l = met.getLore();
				if(l.get(l.size()-1).equalsIgnoreCase(bl+"#LASER")){
					p.getInventory().remove(i);
				}
			}catch(Exception e1){}
		}
	}
	
	/**
	 * Generates HoloBeam, STAND CLEAR!
	 * 
	 * @param loc
	 * @param d
	 * @param bC
	 * @param bL
	 */
	@SuppressWarnings("deprecation")
	private void beam(Location loc,Vector d,String bC, int bL,boolean blkdmg,boolean hrm,Player p){
		if(bL>0){
			man.addHolo(HologramAPI.createHologram(loc,bC));
			if(blkdmg){
				int id = loc.getWorld().getBlockAt(loc).getTypeId();
				if(((id!=0)&(!(bLB.contains(id))&(!bLB.contains(-1))))){
					loc.getWorld().getBlockAt(loc).setType(Material.AIR);
				}
			}
			if(hrm){
				dmgEnt(loc,p,bL,blkdmg);
			}
			Location l = new Location(loc.getWorld(),(loc.getX()+(d.getX()*.3)),(loc.getY()+(d.getY()*.3)),(loc.getZ()+(d.getZ()*.3)));
			l.getWorld().playSound(l, config.getBoolean("SoundEnabled")?Sound.ENTITY_HORSE_HURT:null, .5f, 2f);
			beam(l,d,bC,bL-1,blkdmg,hrm,p);
		}
	}
	
	/**
	 * Entities Ignite If To Close To
	 * Extremely Dangerous Holo Beam
	 * 
	 * @param loc
	 */
	private void dmgEnt(Location loc,Player p,int bL,boolean blkdmg){
		for(Entity e:loc.getWorld().getEntities()){
			double d = (calcDist(loc,e.getLocation()));
			if((d<3)&(!e.getUniqueId().equals(p.getUniqueId()))){
				e.setFireTicks(config.getInt("BeamFireStrength")*bL); /* Fire Strength Based On Remaining Beam Energy On Impact */
				if(blkdmg&d<1){
					e.setVelocity(new Vector(e.getVelocity().getX(),e.getVelocity().getY()+.5,e.getVelocity().getZ()));
					e.getLocation().getWorld().createExplosion(e.getLocation(), config.getInt("DirectHitDamage"));
				}
			} 
		}
	}
	
	/**
	 * The Get Factory Function:
	 * 
	 * Returns The GUI Factory Instance To
	 * Be Used By User Interface Classes.
	 * 
	 * 
	 */
	public GUIFactory gF(){
		return f;
	}
	
	@SuppressWarnings("unchecked")
	private void config(){
		try{
			File file = new File(getDataFolder(),"Config.yml");
			if(!file.exists()){
				file = new File("plugins/HoloBeam");
				file.mkdir();
				file = new File("plugins/HoloBeam/Config.yml");
				file.createNewFile();
				config = YamlConfiguration.loadConfiguration(file);
				setConfig(config,"SoundEnabled", true).setConfig(config,"AmmoPack", 1000).setConfig(config, "DirectHitDamage",1).setConfig(config, "BeamFireStrength",3).setConfig(config, "BeamLength",100).setConfig(config, "CoolDown",1000);
				for(int x=0;x<5;x++){bLB.add(7);}
				config.set("ProtectedBlockIDs", bLB);
				config.save(file);
			}
			config = YamlConfiguration.loadConfiguration(file);
			bLB = (List<Integer>) config.getList("ProtectedBlockIDs");
		}catch(Exception e1){e1.printStackTrace();}
	}
	
	/**
	 * Config Setting Function
	 * 
	 */
	private Main setConfig(YamlConfiguration config,String pth,Object val){
		config.set(pth, val);
		return this;
	}
	
	
	/**
	 * The Color Function
	 * 
	 * 
	 */
	private String cmdColor(String c){
		return (c.equalsIgnoreCase("BL")?bl:c.equalsIgnoreCase("DB")?db:c.equalsIgnoreCase("DG")?dg:c.equalsIgnoreCase("DA")?da:c.equalsIgnoreCase("DR")?dr:c.equalsIgnoreCase("DP")?dp:c.equalsIgnoreCase("GO")?go:c.equalsIgnoreCase("GA")?ga:c.equalsIgnoreCase("GG")?gg:c.equalsIgnoreCase("BU")?bu:c.equalsIgnoreCase("GR")?gr:c.equalsIgnoreCase("AQ")?aq:c.equalsIgnoreCase("RE")?re:c.equalsIgnoreCase("LP")?lp:c.equalsIgnoreCase("YE")?ye:c.equalsIgnoreCase("WH")?wh:"meh");
	}
	

	/**
	 * The FindCoolDown Function
	 * 
	 */
	private CoolDown gCD(Player p,int i){
		if(cd.size()<1){
			return null;
		} else {
			if(cd.get(i).getPlayer().equals(p)){
				return cd.get(i);
			}
			if(i>0){
				return gCD(p,i-1);
			} else {
				return null;
			}
		}
	}
	
	
	/**
	 * The Find Profile Function
	 * 
	 */
	private Profile findProfile(Player p){
		for(Profile ppp:pf){
			if(ppp.uuid().equalsIgnoreCase(p.getUniqueId().toString())){
				return ppp;
			}
		}
		Profile pp = new Profile(p,this);
		pf.add(pp);
		return pp;
		
	}
	
	
	/**
	 * The Check CoolDown Function
	 * 
	 */
	public void checkCoolDown(){
		try{
			for(CoolDown c:cd){
				if(System.currentTimeMillis()>c.getTime()){
					b.sendActionBar(c.getPlayer(), c.getCM());
					cd.remove(c);
					break;
				} else {
					b.sendActionBar(c.getPlayer(), aq+bo+"CoolDown - "+c.getProgress()+aq+" - "+df.format(((float)(c.getTime()-System.currentTimeMillis())/1000f))+"Sec");
				}
			}
		}catch(Exception e1){}
		
	}
	
	/**
	 * The Get GUIFactory Function
	 * 
	 */
	public GUIFactory getFact(){
		return f;
	}
	
	/**
	 * Distance Calculation Function:
	 * 
	 * @param loc1
	 * @param loc2
	 * @return
	 */
	private double calcDist(Location loc1, Location loc2){
		return Math.sqrt(Math.pow(loc1.getBlockX()-loc2.getBlockX(),2)+Math.pow(loc1.getBlockY()- 
		loc2.getBlockY(),2)+Math.pow(loc1.getBlockZ()-loc2.getBlockZ(),2));
	}
	

	/**
	 * Determines Whether Or Not
	 * This Software Is Being Run
	 * On A Support Server Version
	 * 
	 */
	private boolean startupReq(){
		String nmsver = Bukkit.getServer().getClass().getPackage().getName();
		nmsver = nmsver.substring(nmsver.lastIndexOf(".") + 1);
		return(nmsver.startsWith("v1_9_")|nmsver.startsWith("v1_10_"));
	}
}