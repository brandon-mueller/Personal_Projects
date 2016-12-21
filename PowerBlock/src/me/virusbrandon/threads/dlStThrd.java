package me.virusbrandon.threads;

import java.io.File;

import me.virusbrandon.powerblock.Main;

public class dlStThrd extends Thread{
	private Main main;
	
	public dlStThrd(Main main){
		this.main = main;
	}
	
	public void run(){
		clearStates();
	}
	
	private void clearStates(){
		try{
			del(new File(main.getPaths().get(2).substring(0,main.getPaths().get(2).length()-1)));
		}catch(Exception e1){}
	}
	
	private void del(File dir){
		for(File file:dir.listFiles()){
			if(file.isDirectory()){
				del(file);
				file.delete();
			} else {
				file.delete();
			}
		}
	}
}
