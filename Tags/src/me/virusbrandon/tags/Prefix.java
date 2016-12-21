package me.virusbrandon.tags;

public class Prefix implements java.io.Serializable{
	private static final long serialVersionUID = 175788993L;
	private String prefix="";
	private String perms="";
	
	Prefix(String prefix,String perms){
		this.prefix=prefix;
		this.perms=perms;
	}
	
	public String getPrefix(){
		return prefix;
	}
	
	public String getPerms(){
		return perms;
	}
	
	public void setPerms(String perm){
		this.perms=perm;
	}
}
