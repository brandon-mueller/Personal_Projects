package me.virusbrandon.BuildManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;

/**
 * Build Manager, By Brandon Mueller
 * COPYRIGHT UNDERLORDPVP.COM, 2014
 * 
 * Don't Not Distribute This Code! It Take Time To Write The Information You
 * See Here. If You Use The Code In The Plugin, Please Give Me Credit.
 * 
 * Support Our Awesome Server By Purchasing My Plugins And Referring New Players
 * To Our Server!
 *
 */

public class BuildManager extends JavaPlugin{
	private static final Logger log = Logger.getLogger("Build Manager");
	int rS = -1;
	private String[] args2;
	private CommandSender sender2;
	private int sP;
	private static Confirmation conf = new Confirmation(0,0);
	private boolean v;
	private ArrayList<Integer> blocks = new ArrayList<>();
	private ArrayList<Byte> data = new ArrayList<>();
	private static Build[] builds = new Build[5]; //Once Builds Are Saved Here, DO NOT CHANGE THIS!!
	
	@Override
	public void onEnable(){
		loadBuilds();
		log.info("[Build Manager] Has Been Enabled!");
		for(int x = 0;x<builds.length;x++){
			if(builds[x] == null){
				builds[x] = new Build(null,null,"nothing");
			}
		}
	}
	
	@Override
	public void onDisable(){
		saveBuilds();
		log.info("[Build Manager] Has Been Disabled!");
	}
	
	//Acknowledge Command, Plugin Method Calls
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandlbl, String[] args){
		if(commandlbl.equalsIgnoreCase("BuildArea") & args.length != 0){
			v = false;
			if(args[0].equalsIgnoreCase("show")){
				conf.setConf(0);
				showRegion(sender, cmd, commandlbl,args,-2,-2);
				v = true;
			}
			
			if(args[0].equalsIgnoreCase("save")){
				conf.setConf(0);
				saveRegion(sender, cmd, commandlbl,args);
				v = true;
			}
			
			if(args[0].equalsIgnoreCase("const")){
				conf.setConf(0);
				constructRegion(sender, cmd, commandlbl,args);
				v = true;
			}
			
			if(args[0].equalsIgnoreCase("clear")){
				conf.setConf(0);
				clearRegion(sender, cmd, commandlbl,args);
				v = true;
			}
			
			if(args[0].equalsIgnoreCase("move") | args[0].equalsIgnoreCase("copy")){
				conf.setConf(0);
				changeRegion(sender, cmd, commandlbl, args);
				v = true;
			}
			
			if(args[0].equalsIgnoreCase("confirm")){
				deleteBuild(conf, sender);
				v = true;
			}
			
			if(args[0].equalsIgnoreCase("help")){
				sender.sendMessage("========Build Manager v1.0========");
				sender.sendMessage(ChatColor.GOLD + "Available Commands:");
				sender.sendMessage(ChatColor.AQUA + "- /BuildArea Show (Generate Build Region Scaffold)");
				sender.sendMessage(ChatColor.AQUA + "- /BuildArea Save (Save A Build)");
				sender.sendMessage(ChatColor.AQUA + "- /BuildArea const (Construct A Build From Memory)");
				sender.sendMessage(ChatColor.AQUA + "- /BuildArea Clear (Clean A Build Region)");
				sender.sendMessage(ChatColor.AQUA + "- /BuildArea Move (Moves A Build To Another World)");
				sender.sendMessage(ChatColor.AQUA + "- /BuildArea Copy (Copy A Build To Another World)");
				sender.sendMessage(ChatColor.AQUA + "- /BuildArea Confirm");
				sender.sendMessage(ChatColor.WHITE +"   > Usage: Only When Over-Writing");
				sender.sendMessage(ChatColor.WHITE +"   > An Existing Build.");
				v = true;
			}
			
			if(v ==  false){
				sender.sendMessage("ERROR: Unknown Sub Command: " + args[0]);
			}
		} else {
			sender.sendMessage("\nSub-Command Required");
			sender.sendMessage("/BuildArea show    >  Creates A Build Region");
			sender.sendMessage("/BuildArea save    >  Saves Build Region");
			sender.sendMessage("/BuildArea const   >  Constructs Your Saved Build");
			sender.sendMessage("/BuildArea clear   >  Resets Build Area");
		}
		return true;
	}
	
	//Show Region Method, Creates a Scaffold To Build In.
	@SuppressWarnings("deprecation")
	public void showRegion(CommandSender sender, Command cmd, String commandlbl, String[] args,int blk,int rSS){
			int x = 0;
			int y = 100;
			int z = 0;
			int x1,y1,z1 = 0;			//Increment / Decrement Controllers
			int bT = -1; 				//Block Type (Required)
			try{
			World world = Bukkit.getServer().getWorlds().get(Integer.parseInt(args[1]));
			if (blk == -1){
				bT = 1;
			} else {
				bT = (Integer.parseInt(args[2]));
			}
				
			if(rSS != -2){
				rS = rSS;
			} else {
				rS = Integer.parseInt(args[3]);
				if(rS > 150){
					sender.sendMessage(ChatColor.RED + "\nYour Build Region Size");
					sender.sendMessage(ChatColor.RED + "Cannot Exceed 150 Blocks!");
					return;
				}
			}
	
			//WARNING, Modifying this could produce UnWanted Results
			//Always RUN on a test server when changing this!
			int[] list = new int[]{1,0,0,0,0,1,-1,0,0,0,0,-1,0,1,0,1,0,0,0,0,1,-1,0,0,0,0,-1,1,-1,1,0,1,0,0,0,-1,0,-1,0,-1,1,1,0,-1,0,1,0,0,0,1,0,-1,-1,-1,1,0,0,0,1,0,-1,-1,1};
			for(int c = 0;c<(list.length/3);c++){
				x1 = (list[c*3]);
				y1 = (list[(c*3)+1]);
				z1 = (list[(c*3)+2]);
				for(int cc=0;cc <= rS;x+=x1,y+=y1,z+=z1){
					world.getBlockAt(x,y,z).setTypeId(bT);
					cc++;
					}
				}
			sender.sendMessage("Region Marked In World: " + Bukkit.getServer().getWorlds().get(Integer.parseInt(args[1])).getName() + " At 0, 100, 0");
			} catch (Exception e1){
				sender.sendMessage("\nType The Commmand Like This: /BuildArea <Action>");
				sender.sendMessage("<World ID> <BlockID> <Region Size>");
				sender.sendMessage("\nAvailable Worlds:");
				for(x = 0;x<Bukkit.getServer().getWorlds().size();x++){
					sender.sendMessage("ID: " + x + "  >>>  " + Bukkit.getServer().getWorlds().get(x).getName());
				}
				//e1.printStackTrace(); //Un-Comment The Statement To The Left To See Exception Information
				
			}
		}
	
	//Saves Everything Within The Build Region Scaffold
	@SuppressWarnings("deprecation")
	public void saveRegion(CommandSender sender, Command cmd, String commandlbl, String[] args){
		args2 = args;
		sender2 = sender;
		try{
			blocks = new ArrayList<Integer>();
			World world = Bukkit.getServer().getWorlds().get(Integer.parseInt(args[1]));
			rS = Integer.parseInt(args[2]);
			sP = Integer.parseInt(args[3]);
			if(rS > 150){
				sender.sendMessage(ChatColor.RED + "\nYour Build Region Size");
				sender.sendMessage(ChatColor.RED + "Cannot Exceed 150 Blocks!");
				return;
			}
			
			if(sP > 4){
				sender.sendMessage(ChatColor.YELLOW + "\nUse Save Slots 0 - 4");
				sender.sendMessage(ChatColor.YELLOW + "\n<<<Save Slot Summary>>>");
				for(int x = 0;x<builds.length;x++){
					if(builds[x].getBlocks() == null){
						sender.sendMessage(ChatColor.GREEN + "Slot: " + x + " >> AVAILABLE!");
					} else {
						sender.sendMessage(ChatColor.YELLOW + "Slot: " + x + " >> IN USE! >> (OverWritable)");
					}
				}
				return;
			}
			
			if(builds[sP] == null){
				builds[sP] =  new Build(null,null,"nothing");
			}
			
			if(builds[sP].getName().equals("nothing")){
				for(int ele = 100,cnt = 0; cnt <= rS+1 ; ele++,cnt++){
					for(int dep = 0; dep <= rS+1;dep++){
						for(int wid = 0; wid <= rS+1;wid++){
							blocks.add(world.getBlockTypeIdAt(dep,ele,wid));
							data.add(world.getBlockAt(dep,ele,wid).getData());
						}
					}
				}
				builds[sP] = new Build(blocks,data,"Build #" + (args[3]) + ", By: " + sender.getName());
				sender.sendMessage("Build Region Saved");
				} else {
					sender.sendMessage("\nThis Slot Contains: " + builds[sP].getName());
					sender.sendMessage("Do /BuildArea Confirm To Remove This Build");
					sender.sendMessage(ChatColor.YELLOW + "WARNING: THIS ACTION CANNOT BE UNDONE!");
					conf.setConf(1);
					conf.setSP(sP);
				}
			} catch(Exception e2){
				sender.sendMessage("\nType The Commmand Like This: /BuildArea <Action> <World ID>");
				sender.sendMessage("<Region Size> <Save Location>");
				sender.sendMessage("Available Worlds:");
				for(int x = 0;x<Bukkit.getServer().getWorlds().size();x++){
					sender.sendMessage("ID: " + x + "  >>>  " + Bukkit.getServer().getWorlds().get(x).getName());
				}
				e2.printStackTrace(); //Un-Comment The Statement To The Left To See Exception Information
			}
		}
	
	//Loads And Rebuilds A Structure Inside The Build Area
	@SuppressWarnings("deprecation")
	public void constructRegion(CommandSender sender, Command cmd, String commandlbl, String[] args){
		try{
			World world = Bukkit.getServer().getWorlds().get(Integer.parseInt(args[1]));
			rS = Integer.parseInt(args[2]);
			sP = Integer.parseInt(args[3]);
			if(rS > 150){
				sender.sendMessage(ChatColor.RED + "\nYour Build Region Size");
				sender.sendMessage(ChatColor.RED + "Cannot Exceed 150 Blocks!");
				return;
			}
			int c = 0;
			blocks = builds[sP].getBlocks();
			data = builds[sP].getData();
			for(int ele = 100,cnt = 0; (cnt <= rS+1); ele++,cnt++){
				for(int dep = 0; dep <= rS + 1;dep++){
					if(cnt <= rS){
						for(Player player : Bukkit.getOnlinePlayers()){
						    player.sendMessage(args[0].toUpperCase() + ": (" + cnt + "," + dep + ")");
						}
					}
					for(int wid = 0; wid <= rS + 1;wid++){
						world.getBlockAt(dep,ele,wid).setTypeId(blocks.get(c));
						world.getBlockAt(dep,ele,wid).setData(data.get(c));
						c++;
					}
				}
			}
			sender.sendMessage("\nYour Build Region Has Been Loaded!");
			} catch(Exception e3){
				sender.sendMessage("\nType The Commmand Like This: /BuildArea <Action> <World ID>");
				sender.sendMessage("<Region Size> <Save Location>");
				sender.sendMessage("\nAvailable Worlds:");
				for(int x = 0;x<Bukkit.getServer().getWorlds().size();x++){
					sender.sendMessage("ID: " + x + "  >>>  " + Bukkit.getServer().getWorlds().get(x).getName());
				}
				//e3.printStackTrace(); //Un-Comment The Statement To Th Left To See Exception Information
			}
		}
	
	@SuppressWarnings("deprecation")
	public void clearRegion(CommandSender sender, Command cmd, String commandlbl, String[] args){
		try{
			World world = Bukkit.getServer().getWorlds().get(Integer.parseInt(args[1]));
			rS = Integer.parseInt(args[2]);
			if(rS > 150){
				sender.sendMessage(ChatColor.RED + "\nYour Build Region Size");
				sender.sendMessage(ChatColor.RED + "Cannot Exceed 150 Blocks!");
				return;
			} else {
				for(int ele = 100,cnt = 0; (cnt <= rS+1); ele++,cnt++){
					if(cnt <= rS){
						for(Player player : Bukkit.getOnlinePlayers()){
						    player.sendMessage(args[0].toUpperCase() + ": " + cnt + "/" + rS + " Rows");
						}
					}
					for(int dep = 0; dep <= rS + 1;dep++){
						for(int wid = 0; wid <= rS + 1;wid++){
							world.getBlockAt(dep,ele,wid).setTypeId(0);
						}
					}
				}
				showRegion(sender,cmd,commandlbl,args,-1,Integer.parseInt(args[2]));
			}
		} catch(Exception e4){
			sender.sendMessage("\nType The Commmand Like This: /BuildArea <Action> <World ID> <Region Size>");
			sender.sendMessage("<Region Size>");
			sender.sendMessage("Available Worlds:");
			for(int x = 0;x<Bukkit.getServer().getWorlds().size();x++){
				sender.sendMessage("ID: " + x + "  >>>  " + Bukkit.getServer().getWorlds().get(x).getName());
			}
			//e4.printStackTrace(); //Un-Comment The Statement To The Left To See Exception Information
		}
	}
	
	@SuppressWarnings("deprecation")
	public void changeRegion(CommandSender sender, Command cmd, String commandlbl, String[] args){
		try{
			World source = Bukkit.getServer().getWorlds().get(Integer.parseInt(args[1]));
			World dest = Bukkit.getServer().getWorlds().get(Integer.parseInt(args[2]));
			rS = Integer.parseInt(args[3]);
			if(rS > 150){
				sender.sendMessage(ChatColor.RED + "\nYour Build Region Size");
				sender.sendMessage(ChatColor.RED + "Cannot Exceed 150 Blocks!");
				return;
			} else {
				for(int ele = 100,cnt = 0; (cnt <= rS+1); ele++,cnt++){
					if(cnt <= rS){
						for(Player player : Bukkit.getOnlinePlayers()){
							player.sendMessage(args[0].toUpperCase() + ": " + cnt + "/" + rS + " Rows");
						}
					}
					for(int dep = 0; dep <= rS + 1;dep++){
						for(int wid = 0; wid <= rS + 1;wid++){
							dest.getBlockAt(dep,ele,wid).setTypeId(source.getBlockTypeIdAt(dep, ele, wid));
							if(args[0].equalsIgnoreCase("move")){
								source.getBlockAt(dep,ele,wid).setTypeId(0);
							}
						}
					}
				}
				if(args[0].equalsIgnoreCase("move")){
					showRegion(sender,cmd,commandlbl,args,-1,rS);
				}
				 sender.sendMessage(ChatColor.GREEN + args[0].toUpperCase() + " Finished Successfully!");
			}
		} catch(Exception e5){
			sender.sendMessage("\nType The Commmand Like This: /BuildArea <Action>");
			sender.sendMessage("<Source World> <Dest World> <Region Size>");
			sender.sendMessage("\nAvailable Worlds:");
			for(int x = 0;x<Bukkit.getServer().getWorlds().size();x++){
				sender.sendMessage("ID: " + x + "  >>>  " + Bukkit.getServer().getWorlds().get(x).getName());
			}
			//e5.printStackTrace(); //Un-Comment The Statement To The Left To See Exception Information
		}
	}
	
	public void deleteBuild(Confirmation c, CommandSender sender){
		if(c.getConf() != 0){
			builds[c.getSP()].setOverWritable();
			sender.sendMessage(ChatColor.GREEN + "\nBuild Removed");
			sender.sendMessage(ChatColor.GREEN + "This Slot Is Now Free For Use!");
			conf.setConf(0);
			saveRegion(sender2,null,null,args2);
		} else {
			sender.sendMessage(ChatColor.RED + "\nThis Option Is Not Available Now");
		}
	}
	
	public static void saveBuilds(){
		try {
			File file = new File("Plugins/BuildManager/builds.data");	
	            FileOutputStream fileOutput = new FileOutputStream(file);
	            ObjectOutputStream objectOutput = new ObjectOutputStream(fileOutput);
	            objectOutput.writeObject(builds);
	            objectOutput.close();
	        } catch (Exception e6) {
	        	//e6.printStackTrace(); //Un-Comment The Statement To The Left To See Exception Information
	        }
	}
	
	public static void loadBuilds(){
		try{
		ObjectInputStream read = new ObjectInputStream(new FileInputStream("Plugins/BuildManager/builds.data"));
		builds = (Build[])read.readObject();
		read.close();
		System.out.println("<INFO> Loaded Available Information\n");
		} catch(Exception e7){
			//e7.printStackTrace(); //Un-Comment The Statement To The Left To See Exception Information
			File file = new File("Plugins/BuildManager");
			file.mkdir();
		}
	}
}
