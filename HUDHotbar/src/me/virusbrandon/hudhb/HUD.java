package me.virusbrandon.hudhb;

import java.util.ArrayList;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.*;

public class HUD {

	/**
	 * Open HUD Delay (~.5 Sec)
	 * 
	 */
	Runnable timer = new Runnable() { 
		public void run() {
			del--;
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
	private boolean open = false;
	private int indexUp = -1;
	private double defY, selY;
	private double spc = .35;
	private int adj = -4;
	private int id;
	private int del = 9;
	private boolean inProg = false;
	
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
		if(!isOpen() & !inProg){
			Location l = mine.getLocation();
			Location stl = new Location(l.getWorld(),l.getX()+(v.getX()*1.5),l.getY()+v.getY(),l.getZ()+(v.getZ()*1.5));
			Vector res = calcDir((l.getYaw()+90)%360);
			for(int x=adj;x<adj+9;x++){
				Location ll = new Location(l.getWorld(),stl.getX()+(res.getX()*(x*spc)),stl.getY()+.5,stl.getZ()+(res.getZ()*(x*spc)));
				ArmorStand st =ll.getWorld().spawn(ll, ArmorStand.class);
				st.setVisible(false);
				st.setInvulnerable(true);
				st.setSmall(true);
				st.setGravity(false);
				ItemStack stt = mine.getInventory().getItem(x+Math.abs(adj));
				st.setItemInHand(stt==null?new ItemStack(Material.STAINED_GLASS_PANE,1,DyeColor.RED.getWoolData()):stt);
				st.setRightArmPose(new EulerAngle(Math.toRadians(l.getPitch()-90),Math.toRadians(l.getYaw()-180),0));
				st.setCustomName((x+Math.abs(adj))+"");
				stands.add(st);
				Location temp = stands.get(x+Math.abs(adj)).getLocation();
				yaws.add((double)-180+temp.setDirection(calcDir(mine.getLocation(),temp)).getYaw());	
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
		double rad = (Math.toRadians(Math.toDegrees(Math.atan2((source.getZ() - target.getZ()), (source.getX() - target.getX())))+90));
		return new Vector(-Math.sin(rad), 0, Math.cos(rad));
	}
	
	/**
	 * Selects HUD Item Slot
	 * 
	 * @param index
	 */
	public void select(int index){
		for(int x=0;x<stands.size();x++){
			ArmorStand cur = stands.get(x);
			Location ll = cur.getLocation();
			if(cur.teleport(((x != index)?new Location(ll.getWorld(),ll.getX(),defY,ll.getZ()):new Location(ll.getWorld(),ll.getX(),selY,ll.getZ())))){/* BH */}
			this.indexUp = index;
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
		return index;
	}
	
	/**
	 * Sets defY
	 * 
	 */
	public void setDefY(double d){
		this.defY = d;
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
