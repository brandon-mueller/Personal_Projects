package me.virusbrandon.bc_utils;

public class BannerPattern {
	private String PATTERN_COLOR;
	private String PATTERN_TYPE;	
		
	/**
	 * BannerPattern Constructor
	 * 
	 * @param PATTERN_COLOR
	 * @param PATTERN_TYPE
	 * 
	 * 
	 */
	public BannerPattern(String PATTERN_COLOR, String PATTERN_TYPE){
		this.PATTERN_COLOR = PATTERN_COLOR;
		this.PATTERN_TYPE = PATTERN_TYPE;
	}
	
	/**
	 * Returns The Color Of This
	 * Banner Pattern
	 * 
	 * 
	 */
	public String getPatternColor(){
		return PATTERN_COLOR;
	}
	
	/**
	 * Sets The Color Of This
	 * Banner Pattern
	 * 
	 * 
	 */
	public void setPatternColor(String PATTERN_COLOR){
		this.PATTERN_COLOR = PATTERN_COLOR;
	}
	
	/**
	 * Returns The Type Of This
	 * Banner Pattern
	 * 
	 * 
	 */
	public String getPatternType(){
		return PATTERN_TYPE;
	}
	
	/**
	 * Sets The Type Of This
	 * Banner Pattern
	 * 
	 * 
	 */
	public void setPatternType(String PATTERN_TYPE){
		this.PATTERN_TYPE = PATTERN_TYPE;
	}
	
	/**
	 * The BannerPattern Equals Methods
	 * 
	 * 
	 */
	public boolean equals(BannerPattern other){
		if(!getPatternColor().equals(other.getPatternColor())){
			return false;
		}
		if(!getPatternType().equals(other.getPatternType())){
			return false;
		}
		return true;
	}
	
	/*
	 * © 2016 Brandon Mueller
	 * DO NOT DE-COMPILE THIS SOFTWARE OR ATTEMPT ANY FORM OF REVERSE ENGINEERING!
	 */
}
