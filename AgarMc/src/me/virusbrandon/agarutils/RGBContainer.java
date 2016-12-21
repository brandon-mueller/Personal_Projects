package me.virusbrandon.agarutils;

public class RGBContainer {
	private float R;
	private float G;
	private float B;
	
	/**
	 * RGB Container Constructor
	 * 
	 * @param R
	 * @param G
	 * @param B
	 */
	public RGBContainer(float R, float G, float B){
		this.R=R;
		this.G=G;
		this.B=B;
	}
	
	/**
	 * Sets R Value
	 * 
	 * @param R
	 * @return
	 */
	public RGBContainer SR(int R){
		this.R=R;
		return this;
	}
	
	
	/**
	 * Returns R Value
	 * 
	 * @return
	 */
	public float R(){
		float r = (R/255.00f);
		return ((r!=0)?r:.01f);
	}
	
	/**
	 * Returns Int R Value
	 * 
	 */
	public int IR(){
		return (int)R;
	}
	
	/**
	 * Sets G Value
	 * 
	 * @param G
	 * @return
	 */
	public RGBContainer SG(int G){
		this.G=G;
		return this;
	}
	
	/**
	 * Returns G Value
	 * 
	 * @return
	 */
	public float G(){
		float r = (G/255.00f);
		return ((r!=0)?r:.01f);
	}
	
	/**
	 * Returns Int G Value
	 * 
	 */
	public int IG(){
		return (int)G;
	}
	
	/**
	 * Sets B Value
	 * 
	 * @param B
	 * @return
	 */
	public RGBContainer SB(int B){
		this.B=B;
		return this;
	}
	
	/**
	 * Returns B Value
	 * 
	 * @return
	 */
	public float B(){
		float r = (B/255.00f);
		return ((r!=0)?r:.01f);
	}
	
	/**
	 * Returns Int B Value
	 * 
	 */
	public int IB(){
		return (int)B;
	}
}
