package me.virusbrandon.util;

import java.io.File;

import me.virusbrandon.powerblock.Main;

import org.bukkit.configuration.file.YamlConfiguration;

public class Auth {
	private Main main;
	
	public Auth(Main main){
		this.main = main;
	}
	
	/**
	 * The Check Auth Function:
	 * 
	 * Checks For A Auth File And Creates It
	 * If It Does Not Exist.
	 * 
	 */
	public boolean checkAuth(String name){
		try{
			File file = new File(main.getPaths().get(0));
			if(!file.exists()){
				main.mkPbDir();
				file = new File(main.getPaths().get(0));
				file.createNewFile();
			}
			YamlConfiguration auth = YamlConfiguration.loadConfiguration(file);
			return auth.getBoolean(name);
		}catch(Exception e1){}
		return false;
	}
}
