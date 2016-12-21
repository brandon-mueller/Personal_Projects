package me.virusbrandon.util;

import java.io.File;

import org.bukkit.Bukkit;

import me.virusbrandon.powerblock.Main;
import me.virusbrandon.powerblock.Timer;

public class UpdateDetector {
	private Timer checker;
	private Long timeStamp = ((long)0.0);
	private File file;
	private Main main;
	
	public UpdateDetector(Main main){
		this.checker = new Timer(this,5);
		this.main = main;
		checker.start(100);
		check();
	}
	
	public int check(){
		this.file = new File("Plugins/PowerBlock.jar");
		if(timeStamp == 0.0f){
			this.timeStamp = file.lastModified();
		} else if(timeStamp!=file.lastModified()){
			Bukkit.broadcastMessage(main.pr()+"An Update Has Been Installed, Restarting...");
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "Plugman reload Powerblock");
		}
		return 0;
	}
	
	public void cancel(){
		checker.stop();
		this.checker = null;
	}
}
