package me.virusbrandon.BuildManager;

public class Confirmation {
	int conf;
	int sP;
	
	Confirmation(int conf, int sP){
		this.conf = conf;
		this.sP = sP;
	}
	
	public int getConf(){
		return conf;
	}
	
	public int getSP(){
		return sP;
	}
	
	public void setConf(int conf){
		this.conf = conf;
	}
	
	public void setSP(int sP){
		this.sP = sP;
	}
}
