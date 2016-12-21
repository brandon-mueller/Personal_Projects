package me.virusbrandon.threads;

import me.virusbrandon.powerblock.Main;
import me.virusbrandon.powerblock.Timer;

public class UIThread extends Thread{
	private Main main;
	
	public void run(){
		Timer counter = new Timer(main,0);
		counter.start(20);
		Timer frmTimer = new Timer(main.getRes(),1);
		frmTimer.start(2);
	}
	
	public UIThread(Main main){
		this.main = main;
	}
}
