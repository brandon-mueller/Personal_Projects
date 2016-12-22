package me.virusbrandon.hudhb;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.*;

public class HUD {

	/**
	 * Open HUD Delay (.45 Sec)
	 * 
	 */
	Runnable timer = new Runnable() { 
		public void run() {
			del--;
			for(int x=0;x<4;x++){
				ArmorStand as = stands.get(del+(9*x));
				Location l = as.getLocation();
				as.teleport(new Location(l.getWorld(),l.getX(),l.getY()+.1,l.getZ()));
			}
			ArmorStand as = stands.get(del);
			Location l = as.getLocation();
			as.teleport(new Location(l.getWorld(),l.getX(),l.getY()+.1,l.getZ()));
			if(!mine.isSneaking()){
				setOpen(true);
				close();
			}	
			if(del <= 0){
				del = 9;
				stop();
				setOpen(true);
				setDefY(l.getY());
				inProg = false;
			}
		}
	};
	
	private Player mine;
	private Main main;
	private ArrayList<ArmorStand> stands = new ArrayList<>();
	private ArrayList<Double> yaws = new ArrayList<>();
	private HashMap<Integer,Double> pitches = new HashMap<>();
	private int[] keys = new int[]{0,1,2,3};
	private boolean open = false;
	private int indexUp = -1;
	private double defY, selY;
	private double spc = .35;
	private int adj = -4;
	private int id;
	private int del = 9;
	private boolean inProg = false;
	private boolean userEnabled = true;
	
	public HUD(Player mine, Main main){
		this.mine = mine;
		this.main = main;
	}
	
	/**
	 * Displays The HUD To
	 * The Player
	 * 
	 * @param v
	 */
	@SuppressWarnings("deprecation")
	public void open(Vector v){
		if(!userEnabled){main.getBar().sendActionBar(mine, "HUD DISABLED By User. Run /HUD Toggle To Enable Interface.");return;}
		if(!isOpen() & !inProg){
			Location l = mine.getLocation();
			Location stl = new Location(l.getWorld(),l.getX()+(v.getX()*1.5),l.getY()+v.getY(),l.getZ()+(v.getZ()*1.5));
			Vector res = calcDir((l.getYaw()+90)%360);
			int tr = (adj-1);
			int row = 4;
			for(int x=adj;x<(36+adj);x++){
				tr++;
				if(tr >= (9+adj)){
					tr = adj;
					row--;
				}
				Location ll = new Location(l.getWorld(),stl.getX()+(res.getX()*(tr*spc)),(stl.getY()+.5)+((x>=5)?(row*.5):0),stl.getZ()+(res.getZ()*(tr*spc)));
				ItemStack stt = mine.getInventory().getItem(x+Math.abs(adj));
				stands.add(new EncapArmorStand(ll.getWorld().spawn(ll, ArmorStand.class)).setVisible(false).setInvulnerable(true).setSmall(true).setGravity(false).setCollidable(false).setItemInHand(stt==null?new ItemStack(Material.STAINED_GLASS_PANE,1,DyeColor.YELLOW.getWoolData()):stt).setRightArmPose(new EulerAngle(Math.toRadians(l.getPitch()-90),Math.toRadians(l.getYaw()-180),0)).setCustomName((x+Math.abs(adj))+"").getResultingArmorStand());
				Location temp = stands.get(x+Math.abs(adj)).getLocation();
				Location t = temp.setDirection(calcDir(mine.getLocation(),temp));
				yaws.add((double)-180+t.getYaw());
				if((x+Math.abs(adj))%9==0){
					pitches.put(keys[pitches.size()],(double)t.getPitch());
				}
			}	
			defY = stands.get(0).getLocation().getY();
			selY = stands.get(0).getLocation().getY()+.5;
			this.indexUp = -1;
			start(1);
			main.getBar().sendActionBar(mine, "Look At Item To Raise It, Then Stop Crouching To Select It!");
		}
	}
	
	/**
	 * Closes The HUD
	 * 
	 * @return
	 */
	public int close(){
		if(isOpen()){
			stop();
			for(ArmorStand s:stands){
				s.remove();
			}
			stands.clear();
			yaws.clear();
			pitches.clear();
			setOpen(false);
			inProg = false;
			del = 9;
			main.getBar().sendActionBar(mine, "Crouch Again To Select Another Item!");
			return indexUp;
		}
		return 0;
	}
	
	/**
	 * Sets This HUD As
	 * Interactable
	 * 
	 * @param open
	 */
	public void setOpen(boolean open){
		this.open = open;
	}
	
	/**
	 * Tells Us Whether The HUD
	 * Is Current Open Or Not.
	 * 
	 * @return
	 */
	public boolean isOpen(){
		return open;
	}
	
	/**
	 * Calculates A Vector Using
	 * Only The Yaw
	 * 
	 */
	private Vector calcDir(double YAW){
		double rad = Math.toRadians(YAW);
		return new Vector(-Math.sin(rad), 0, Math.cos(rad));
	}
	
	/**
	 * Calculates A Vector Where
	 * Location 1 Is Looking Towards
	 * Location 2
	 * 
	 */
	public Vector calcDir(Location source, Location target){
		double dX = source.getX() - target.getX();
		double dY = source.getY() - target.getY();
		double dZ = source.getZ() - target.getZ();
		double yaw = Math.atan2(dZ, dX);
		double pitch = Math.atan2(Math.sqrt(dZ * dZ + dX * dX), dY) + Math.PI;
		double X = Math.sin(pitch) * Math.cos(yaw);
		double Y = Math.sin(pitch) * Math.sin(yaw);
		double Z = Math.cos(pitch);
		return new Vector(X, Z, Y);
		//double rad = (Math.toRadians(Math.toDegrees(Math.atan2((source.getZ() - target.getZ()), (source.getX() - target.getX())))+90));
		//return new Vector(-Math.sin(rad),0, Math.cos(rad));
	}
	
	/**
	 * Selects HUD Item Slot
	 * 
	 * @param index
	 */
	public void select(int index){
		int re = 0;
		int row = 4;
		for(int x=0;x<stands.size();x++){
			ArmorStand cur = stands.get(x);
			Location ll = cur.getLocation();
			if(cur.teleport(((x != index)?new Location(ll.getWorld(),ll.getX(),defY+((x>8)?(row*.5):0),ll.getZ()):new Location(ll.getWorld(),ll.getX(),selY+((x>8)?(row*.5):0),ll.getZ())))){/* BH */}
			this.indexUp = index;
			re++;
			if(re>=9){
				re=0;
				row--;
			}
		}
	}
	
	/**
	 * Returns Slot Selected
	 * Based On Player Yaw
	 * 
	 */
	public int getSelected(double YAW){
		double dist = 100;
		int index = 0;
		for(int x=0;x<yaws.size();x++){
			double c = Math.abs(yaws.get(x)-((YAW<-180)?YAW+360:YAW));
			if(c<dist){
				dist = c;
				index = x;
			}
		}
		dist = 100;
		int row = 0;
		for(int x=0;x<4;x++){
			double dd = Math.abs(mine.getLocation().getPitch()-pitches.get(x));
			if(dd < dist){
				dist = dd;
				row = x;
			}
		}
		return ((row*9)+index);
	}
	
	/**
	 * Sets defY
	 * 
	 */
	public void setDefY(double d){
		this.defY = d;
	}
	
	/**
	 * Toggles The Hud
	 * 
	 */
	public void toggleHud(){
		this.userEnabled = ((userEnabled)?false:true);
		main.getBar().sendActionBar(mine, (userEnabled)?"HUD Enabled!":"HUD Disabled!");
	}
	
	/**
	 * Starts The Timer
	 * 
	 * @param delay
	 * 
	 * 
	 */
	private void start(int delay){
		inProg = true;
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
}
