package me.virusbrandon.agarmc;


import me.virusbrandon.agargameboard.BorderEffect;
import me.virusbrandon.agargameboard.*;
import me.virusbrandon.agarutils.*;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.potion.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Cell {
	Runnable timer = new Runnable() {
		public void run() {
			if(pr.isRandom()){
				getCell().setCellColor(ChatColor.values()[(int)(Math.random()*15)]);
			}
			dispCell();
			Location b = bat.getLocation();
			main.gD().tCell(getCell());
			if(!host.isAI()){
				Location pl = player.getLocation();
				double d = new Location(pl.getWorld(),pl.getX(),(main.gB().getOrigin().getY()+main.gB().getCellPadding()),pl.getZ()).distance(b);
				host.corDir(bat, pl, 1, (d*.5));
				if((pl.getX()<main.gB().getBoardMinX())|(pl.getX()>main.gB().getBoardMaxX())|(pl.getZ()<main.gB().getBoardMinZ())|(pl.getZ()>main.gB().getBoardMaxZ())){
					Location l = main.gB().getOrigin();
					host.corDir(player,main.gB().getGameboardCenter(), .002);
					new BorderEffect(getCell(),new Location(pl.getWorld(),pl.getX(),(l.getY()+main.gB().getCellPadding()),pl.getZ()));
				}
			} else {
				decide();
			}
		}	
	};
	
	Runnable meta = new Runnable() { /* Metabolism - Gradually Lose Mass */
		public void run() {
			if(getMass()>250){
				if(getMass()>250 && getMass()<=(main.gB().getMaxMass())){
					setMass((int)(getMass()*.98));
				} else if(getMass()>(main.gB().getMaxMass())){
					setMass((int)(getMass()*.95));
				}
			}
		}	
	};
	
	private Player player;
	private String killedBy;
	private Pref pr;
	private AgarPlayer host;
	private Bat bat;
	private ChatColor cellColor = ChatColor.DARK_BLUE;
	private int id,id2;
	private int mass = 250;
	private double radius;
	private boolean isDead;
	private Main main;
	private HoloObject name;
	private String bo = ChatColor.BOLD+"",bl=ChatColor.BLACK+"",dg=ChatColor.DARK_GREEN+"";
	private Vector t;
	private double last,dd;
	private int trs;
	private boolean isTargeted;
	
	/**
	 * Main Cell Constuctor.
	 * 
	 * Call when a new player is joining
	 * into the competition.
	 * 
	 * @param main
	 * @param host
	 */
	protected Cell(Main main, AgarPlayer host, Pref pr){
		this.main = main;
		this.host = host;
		this.pr = pr;
		this.mass = main.gB().getStartMass();
		if(!pr.isRandom()){
			this.cellColor = ChatColor.valueOf(pr.getCellColor());
		}
		Location loc = main.gB().rndLoc();
		if(!host.isAI()){
			this.player = host.getPlayer();
			player.teleport(new Location(loc.getWorld(),loc.getX(),loc.getY()+10,loc.getZ(),loc.getYaw(),90));
		}
		start(4);start1(main.gB().getCellMetaRate()*20);
		this.name = main.hM().addHolo(new Hologram(loc, bl+bo+(host.isAI()?host.getName():pr.isNameOn()?player.getName():"")), 0);
		spawnEntity(loc);
	}
	
	/**
	 * This constructor is to be called
	 * when the host cell splits.
	 * 
	 * @param cell
	 */
	private Cell(Cell cell, int dir){
		try{
			this.main = cell.getMain();
			this.host = cell.getHost();
			this.pr = cell.getPrefs();
			this.t= cell.getFace();
			if(!pr.isRandom()){
				this.cellColor = ChatColor.valueOf(cell.getCellColor().name());
			}
			this.player = cell.getPlayer();
			start(4);start1(main.gB().getCellMetaRate()*20);
			this.name = main.hM().addHolo(new Hologram(cell.getEntity().getLocation(), bl+bo+(host.isAI()?host.getName():pr.isNameOn()?player.getName():"")), 0);
			spawnEntity(cell.getEntity().getLocation());
			bat.setVelocity((dir==-1)?(host.isAI()?t:calcDir(player.getLocation())).multiply(15):new Vector(-1+(Math.random()*2),0,-1+(Math.random()*2)).multiply(15));
		}catch(Exception e1){}
	}
	
	/**
	 * This constructor is to be called
	 * when a pair of cells merge.
	 * 
	 */
	private Cell(Cell c1, Cell c2){
		try{
			this.main = c1.getMain();
			this.host = c1.getHost();
			this.pr = c1.getPrefs();
			this.mass = (c1.death()+c2.death());
			if(!pr.isRandom()){
				this.cellColor = ChatColor.valueOf(c1.getCellColor().name());
			}
			this.player = c1.getPlayer();
			start(4);start1(main.gB().getCellMetaRate()*20);
			this.name = main.hM().addHolo(new Hologram(c1.getEntity().getLocation(), bl+bo+(host.isAI()?host.getName():pr.isNameOn()?player.getName():"")), 0);
			spawnEntity(c1.getEntity().getLocation());
			host.setTarget(null);
		}catch(Exception e1){}
	}
	
	/**
	 * Spawns The Villager Entity
	 * So This Cell Can Track It's
	 * Movement.
	 * 
	 */
	private void spawnEntity(Location l){
		this.bat  = (Bat) l.getWorld().spawnEntity(l, EntityType.BAT);
		bat.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,Integer.MAX_VALUE,1));
		bat.setAI(false);
		bat.setAwake(false);
		bat.setSilent(true);
		bat.setInvulnerable(true);
		bat.setRemoveWhenFarAway(false);
	}
	
	/**
	 * Returns The Bat Entity.
	 * 
	 * @return
	 */
	public Bat getEntity(){
		return bat;
	}
	
	/**
	 * Returns This Cell's Color.
	 * 
	 * @return
	 */
	public ChatColor getCellColor(){
		return cellColor;
	}
	
	/**
	 * Sets This Cell's Color.
	 * 
	 */
	private void setCellColor(ChatColor cellColor){
		this.cellColor = cellColor;
	}
	
	/**
	 * Returns Reference To Main.
	 * 
	 * @return
	 */
	public Main getMain(){
		return main;
	}
	
	/**
	 * Returns The Host AgarPlayer That
	 * Is In Charge Of This Are Any Other
	 * Cells That Have Split Off From The
	 * Host Cell.
	 * 
	 * @return
	 */
	public AgarPlayer getHost(){
		return host;
	}
	
	/**
	 * Returns The Preferences Object
	 * For This Cell.
	 * 
	 */
	private Pref getPrefs(){
		return pr;
	}
	
	/**
	 * Returns The Player In Charge
	 * Of This Cell.
	 * 
	 * @return
	 */
	public Player getPlayer(){
		return player;
	}
	
	/**
	 * Returns The Player Who Was Responsible
	 * For Killing This Cell.
	 * 
	 */
	protected String gotKilledBy(){
		return killedBy;
	}
	
	/**
	 * Returns The Current Radius
	 * Of This Cell.
	 * 
	 * @return
	 */
	public double getRadius(){
		return radius;
	}
	
	/**
	 * Returns The Face Direction
	 * Of This Cell.
	 * 
	 */
	public Vector getFace(){
		return t;
	}
	
	/**
	 * I Get To Throw My mASS At You
	 * Because I'm Such A Nice Person,
	 * I Mean CELL!
	 * 
	 */
	@SuppressWarnings("deprecation")
	protected void eject(){
		if(mass >= 250){
			if(!host.isAI()){
				host.getPlayer().playSound(host.getPlayer().getLocation(), Sound.BLOCK_SLIME_HIT, .3f, .5f);
			}
			mass-=5;
			Location l = getEntity().getLocation();
			int[] i = main.gB().getBBlocks()[(int)(Math.random()*main.gB().getBBlockLen())];
			FallingBlock fb = l.getWorld().spawnFallingBlock(l,i[0],(byte)i[1]);
			fb.setVelocity(calcDir((host.isAI()?bat:player).getLocation()).multiply(3));
	    	fb.setDropItem(false);
		}
	}
	
	/**
	 * Splits Your Cell Down Into
	 * Two Smaller Cells.
	 * 
	 * This is a risky move as other
	 * players will now target you since
	 * you are smaller now.
	 * 
	 * You will be able to
	 * rejoin after awhile.
	 * 
	 */
	public Cell split(int dir){
		if((getMass()>250)&&(host.getCells().size()<main.gB().getMaxCells())){
			Cell cell = new Cell(this,dir);
			setMass(getMass()/2);
			cell.setMass(getMass());
			host.addCell(cell);
			if(!host.isAI()){
				player.playSound(player.getLocation(), Sound.ENTITY_SLIME_SQUISH, 1, 2);
			}
		}
		return this;
	}
	
	/**
	 * Returns This Cell's Current
	 * Mass.
	 * 
	 * @return
	 */
	public int getMass(){
		return mass;
	}
	
	/**
	 * Sets This Cell's Mass.
	 * 
	 * Mass Increases Will Be Rejected
	 * When Cells Get Too Enormous.
	 * 
	 * @param mass
	 */
	public void setMass(int mass){
		if((mass > this.mass) & host.getTotalMass() > main.gB().getMaxMass()*2){
			return;
		}
		this.mass = mass;
	}
	
	/**
	 * This Is When This Cell Rejoins With,
	 * The Other Cells That Are Still Alive, Back
	 * Into A Larger More Threatening Force.
	 * 
	 * @param other
	 */
	public Cell merge(Cell other){
		if(!host.isAI()){
			player.playSound(player.getLocation(), Sound.ENTITY_SLIME_SQUISH, 1, .5f);
		}
		return new Cell(this,other);
	}
	
	/**
	 * Returns Me To... Wherever.
	 * 
	 * @return
	 */
	private Cell getCell(){
		return this;
	}
	
	/**
	 * Destroys This Cell And Surrenders It's
	 * Mass To This Cell's Consumer.
	 * 
	 * @param c
	 * @return
	 */
	public int death(Cell ... killedBy){
		if(killedBy.length > 0){
			this.killedBy = killedBy[0].getHost().getName();
		}
		stop();stop1();
		main.hM().remHolo(name);
		bat.setHealth(0);
		setDead();
		return getMass();
	}
	
	/**
	 * Makes Me Visible To You!
	 * 
	 */
	@SuppressWarnings("deprecation")
	private void dispCell(){
		Location loc = bat.getLocation();
		this.radius = calcRadius();		
		RGBContainer c = main.gB().getColor(cellColor.name());
		for(Location l:main.gD().getCircle(loc,radius,((int)(radius*20)))){
			l.getWorld().spigot().playEffect(l, Effect.COLOURED_DUST, 0, 0, c.R(), c.G(), c.B(), 1f, 0, 200);
		}
		name.setLocation(new Location(loc.getWorld(),loc.getX(),loc.getY(),loc.getZ()));
	}
	
	/**
	 * Gives Me The Ability To See
	 * Where The Hell I'm Going So
	 * I Don't Starve To Death!
	 * 
	 */
	public void checkForMass(){
		new BukkitRunnable() {
            @Override
            public void run() {
            	Location l = getEntity().getLocation();
        		for(int x=(int)(l.getBlockX()-getRadius()),z=(int)(l.getBlockZ()-getRadius());z<(l.getBlockZ()+getRadius());x++){
        			Germ g = main.gB().getAllGerms().get(x+""+z);
        			if((!host.isSick()) && (g != null)){
        				Location gl = g.getLocation();
        				if(new Location(gl.getWorld(),x,gl.getY(),z).distance(l)<=getRadius()){
        					if(host.canGetSick()){
                    			host.setSick(true);
            					getSick(g);
                				main.broadcastMessage(getEntity().getWorld().getName(),main.pfx()+host.getName()+" Got "+dg+"Germed!");
                				return;
            				}
        				}
        			}
        			Mass m = getHost().getMain().gB().getAllMass().get(x+""+z);
        			if(m != null){
        				if(m.getLocation().distance(getEntity().getLocation())<=getRadius()){
        					if(!host.isAI()){
            					player.playSound(player.getLocation(), Sound.ENTITY_SLIME_ATTACK, .1f, 2);
        					}
        					setMass(getMass() + m.getMass());
        					m.reMass();
        					main.gB().getAllMass().remove(x+""+z);
        					m.relocate();
        					if(main.gB().getAllMass().size()<=main.gB().getMaxMass()){
        						main.gB().getAllMass().put(((int)m.getLocation().getBlockX())+""+((int)m.getLocation().getBlockZ()),m);
        					}
        				}
        			}
        			if(x>=(l.getBlockX()+getRadius())){
        				x = (int)(l.getBlockX()-getRadius());
        				z++;
        			}
        		}
            }
        }.runTaskLater(main, 1);
	}
	
	/**
	 * AI Use Only
	 * 
	 * Cells Make Decision Based On Whether
	 * The Surrounding Environment Is Safe, Dangerous,
	 * Or Beneficial, then will either proceed, retreat,
	 * or pursue based on that information.
	 * 
	 */
	public void decide(){
		this.t = calcDir(bat.getLocation(),host.getTarget());	
		if((host.getNumCells() > 1) && (host.getSplitTime() <= 0)){
			host.setTarget(host.getCells().get(0).getEntity().getLocation());
		} else {
			Location l = bat.getLocation();			
			dd = l.distance(host.getTarget());
			if((last==0)|Math.abs(last-dd)>=1){
				last = dd;
				trs = 0;
			} else {
				trs++;
				if(trs>25){
					host.setTarget(null);
				}
			}
			for(Location ll:main.gD().getCircle(bat.getLocation(),radius+1,(int)(radius*10))){
				String s = ((int)ll.getX())+""+((int)ll.getZ());
				if(main.gB().getAllGerms().get(s)!=null){
					Vector re = calcDir(bat.getLocation(),ll).multiply(-1.5+Math.random());
					host.corDir(bat,new Location(l.getWorld(),l.getX()+(re.getX()*(radius*3)),l.getY(),l.getZ()+(re.getZ()*(radius*3))),1,.5);	
					return;
				}
			}
			for(Location ll:main.gD().getCircle(bat.getLocation(),radius+1,(int)(radius*10))){
				String s = ((int)ll.getX())+""+((int)ll.getZ());
				if(main.gB().getAllMass().get(s)!=null){
					Vector d = calcDir(bat.getLocation(),ll).multiply(1.5);
					host.corDir(bat,new Location(l.getWorld(),l.getX()+d.getX(),l.getY(),l.getZ()+d.getZ()),1,1+(Math.random()*2));	
					return;
				}
			}
			for(Cell c:main.getAllCells()){
				if(!c.UUID().equalsIgnoreCase(UUID())){
					if(l.distance(c.getEntity().getLocation())<Math.max(getRadius(), c.getRadius())*5){
						if(c.getMass()<=(getMass()*.75)){
							host.setCellTarget(c);
							host.setTarget(c.getEntity().getLocation());
							isTargeted = false;
						} else if(getMass()<(c.getMass()*.75)){
							Vector v = calcDir(l,c.getEntity().getLocation()).multiply(-.5+(Math.random()*-1));
							Location RUN = new Location(l.getWorld(),l.getX()+(v.getX()*20),l.getY(),l.getZ()+(v.getZ()*20));
							host.setCellTarget(null);
							host.setTarget(RUN);
							isTargeted = true;
						} else {
							isTargeted = false;
						}
					}
				}
			}
			if(host.getTarget() == null || (bat.getLocation().distance(host.getTarget())<1)){
				host.setCellTarget(null);
				host.setTarget(main.gB().rndLoc());
			}
		}
		host.corDir(bat,host.getTarget(), 1, host.getCellTarget()!=null?2:isTargeted?2:1);	
	}
	
	/**
	 * Cell Ran Over A Germ, Got Infected,
	 * And Breaks Into Small Pieces For Awhile.
	 * 
	 */
	public void getSick(Germ g){
		g.relocate();
		for(int x=0;x<5;x++){
			host.split(0);
		}
	}
	
	/**
	 * Returns Whether This Cell
	 * Is Dead Or Not
	 * 
	 */
	public boolean isDead(){
		return isDead;
	}
	
	/**
	 * Sets This Cell As Dead
	 * 
	 */
	public void setDead(){
		this.isDead = true;
	}
	
	/**
	 * Determines This Cell's Radius
	 * Based On Current Mass.
	 * 
	 * @return
	 */
	private double calcRadius(){
		double d = (.005*mass);
		return ((d<1)?1:d);
	}
	
	/**
	 * Calculates A Vector Using
	 * Only The Yaw
	 * 
	 */
	public Vector calcDir(Location l){
		double rad = Math.toRadians(l.getYaw());
		return new Vector(-Math.sin(rad), 0, Math.cos(rad));
	}
	
	/**
	 * Calculates A Vector Where
	 * Location 1 Is Looking Towards
	 * Location 2
	 * 
	 */
	public Vector calcDir(Location source, Location target){
		double rad = (Math.toRadians(Math.toDegrees(Math.atan2((source.getZ() - target.getZ()), (source.getX() - target.getX())))+90));
		return new Vector(-Math.sin(rad), 0, Math.cos(rad));
	}
	
	/**
	 * Returns The String Unique Id Of The
	 * Player In Charge Of This Cell.
	 * 
	 * For AIs, We Return Their Name
	 * (They Have No UUID)
	 * 
	 * @return
	 */
	public String UUID(){
		return (player != null)?player.getUniqueId().toString():host.getName();
	}
	
	/**
	 * Starts The Timer
	 * 
	 * @param delay
	 * 
	 * 
	 */
	private void start(int delay){
		id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(Main.class), timer, delay, delay);
	}

	/**
	 * Stops The Timer
	 * 
	 * 
	 */
	private void stop(){
		Bukkit.getScheduler().cancelTask(id);
	}
	
	/**
	 * Starts The Timer
	 * 
	 * @param delay
	 * 
	 * 
	 */
	private void start1(int delay){
		id2 = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(Main.class), meta, delay, delay);
	}

	/**
	 * Stops The Timer
	 * 
	 * 
	 */
	protected void stop1(){
		Bukkit.getScheduler().cancelTask(id2);
	}
}

/*
 * © 2016 Brandon Mueller
 * DO NOT DE-COMPILE THIS SOFTWARE OR ATTEMPT ANY FORM OF REVERSE ENGINEERING!
 */