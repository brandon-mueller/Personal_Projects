package me.virusbrandon.fileio;

import java.io.File;

import me.virusbrandon.bottomlesschests.Chest;

import org.bukkit.configuration.file.YamlConfiguration;

public class Writer{
	private Chest chest;
	private boolean method;
	private YamlConfiguration Config;
	
	/**
	 * FileIO.Writer Constructor
	 * 
	 * @param chest
	 * @param method
	 * 
	 * 
	 */
	public Writer(Chest chest,boolean method){
		this.chest = chest;
		this.method = method;
	}
	
	/**
	 * save Method, Begins Writting Contents
	 * To The Server Hard Disk
	 * 
	 * 
	 */
	public void save(boolean isAutosave){
		String cL = "C.";
		try{
			File config = null;
			if(!method){
				try{
					config = new File("plugins/BottomLessChests/Chests/Name", chest.getName()+".yml");
				} catch(Exception e1){
					File file = new File("plugins/BottomLessChests/Chests/Name");
					file.mkdirs();
					config.createNewFile();
					save(isAutosave);
				}
			} else {
				try{
					config = new File("plugins/BottomLessChests/Chests/UUID", chest.UUID()+".yml");
				} catch(Exception e1){
					File file = new File("plugins/BottomLessChests/Chests/UUID");
					file.mkdirs();
					config.createNewFile();
					save(isAutosave);
				}
			}
			Config = YamlConfiguration.loadConfiguration(config);
			Config.set(cL+"S", chest.getChest().size());
			Config.set(cL+"N", chest.getName());
			for(int x = 0;x<chest.getChest().size();x++){
				try{
					if(chest.getChest().get(x).getItemId()!=0){
						try{
							Config.set(cP(cL+x+".STK"),chest.getChest().get(x).getItemStack());
						}catch(Exception e1){}
						try{
							Config.set(cP(cL+x+".OW"),chest.getChest().get(x).getOrgWorld());
						}catch(Exception e1){}
					} else {
						cP(cL+x);
					}
				}catch(Exception e1){}
			}
			Config.set(cP(cL+"F.T"), chest.getFriends().size());
			try{
				for(int y = 0;y<chest.getFriends().size();y++){
					Config.set(cP(cL+"F."+y+".N"), chest.getFriends().get(y).getName());
					Config.set(cP(cL+"F."+y+".U"), chest.getFriends().get(y).getUUID());
				}
			}catch(Exception e1){}
			Config.set(cP(cL+"TimeStamp"), System.currentTimeMillis());
			Config.save(config);
			if(!isAutosave){
				chest.shutdown();
			}
		}catch(Exception e1){}
	}
	
	/**
	 * cP - Stands For Clear Path
	 * 
	 * Clear Contents In This Section Of The Chest
	 * Configuration File
	 * 
	 * 
	 */
	private String cP(String path){
		Config.set(path, null);
		return path;
	}
	
	/*
	 * © 2016 Brandon Mueller
	 * DO NOT DE-COMPILE THIS SOFTWARE OR ATTEMPT ANY FORM OF REVERSE ENGINEERING!
	 */
}
