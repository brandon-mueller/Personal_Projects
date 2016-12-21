package me.virusbrandon.bc_utils;

import java.io.File;

import org.bukkit.configuration.file.YamlConfiguration;

public class Language {
	private YamlConfiguration config;
	private File file;
	private String fileName;
	
	/**
	 * The Language File Constructor
	 * 
	 * @param file
	 */
	public Language(File file){
		this.file = file;
		this.fileName = file.getName();
		this.config = YamlConfiguration.loadConfiguration(file);
	}
	
	/**
	 * The Get Text Function
	 * 
	 * @param text
	 * @return
	 */
	public String gT(String text){
		String s = config.getString(text);
		return (s!=null?s:"Err [-1]");
	}
	
	/**
	 * Returns The Language File Name
	 * 
	 * @return
	 */
	public String getFileName(){
		return fileName;
	}
	
	/**
	 * Returns The Actual Language File
	 * 
	 * @return
	 */
	public File getFile(){
		return file;
	}
	
	/*
	 * © 2016 Brandon Mueller
	 * DO NOT DE-COMPILE THIS SOFTWARE OR ATTEMPT ANY FORM OF REVERSE ENGINEERING!
	 */
}
