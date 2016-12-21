package me.virusbrandon.powerblock;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Sound;

public class Ticket {
	private String wh=ChatColor.WHITE+"",re=ChatColor.RED+"",bo=ChatColor.BOLD+"",kk=ChatColor.MAGIC+"";
	private String status = "";
	private String prizeAmt = "0.00";
	private Random rand = new Random();
	private ArrayList<Integer>numbers = new ArrayList<>();
	private ArrayList<Integer>wWhiteBlocks = new ArrayList<>();
	private ArrayList<Integer>powerBlocks = new ArrayList<>();
	private int powerBlock = 0;
	private int prizeLvl = 0;
	private int ID = -1;
	private int onNumber = 0;
	private TPlayer owner;
	private boolean isOld = false;
	private Date drawn = null;
	private Main main;
	private SimpleDateFormat[]ddd = new SimpleDateFormat[]{new SimpleDateFormat("MM.dd.YYYY-HH.mm.ss"),new SimpleDateFormat("MM.dd.YYYY"),new SimpleDateFormat("HH.mm.ss")};

	
	/**
	 * The Ticket Object Constructor:
	 * 
	 * Sets up the numbers for the White Blocks
	 * and for the Power Blocks Based On The Server
	 * Administartors Custom Configuration
	 * 
	 * @param hWhiteBlock
	 * @param hPowerBlock
	 */
	public Ticket(Main main,TPlayer owner, int ID){
		for(int x=1;x<main.getHWhiteBlock()+1;x++){
			numbers.add(x);
		}
		for(int x=1;x<main.getHPowerBlock()+1;x++){
			powerBlocks.add(x);
		}
		this.owner = owner;
		this.ID = ID;
		this.main = main;
		if(owner!=null){
			drawWhiteBlock(5);
			this.powerBlock = drawPowerBlock();
		} else {
			for(int x=0;x<5;x++){
				wWhiteBlocks.add(0);
			}
		}
	}
	
	
	/**
	 * The Ticket Object Constructor
	 * 
	 * This Constructor Is For Creating
	 * A Custom Ticket.
	 * 
	 */
	public Ticket(Main main, TPlayer owner, int[]numbers, int ID){
		this.owner = owner;
		for(int x=0;x<5;x++){
			wWhiteBlocks.add(numbers[x]);
		}
		this.powerBlock = numbers[5];
		this.ID = ID;
		this.main = main;
		sort();
	}
	
	
	/**
	 * The DrawWhiteBlock Function (Recursive):
	 * 
	 * Chooses the winning white block numbers
	 * 
	 * @param toDraw
	 */
	private void drawWhiteBlock(int toDraw){
		int index = rand.nextInt(numbers.size());
		wWhiteBlocks.add(numbers.get(index));
		numbers.remove(index);
		toDraw--;
		if(toDraw>0){ /* Recursive Base Case - Ain't Nobody Got Time For Stack Overflows :O */
			drawWhiteBlock(toDraw);
		} else {
			sort();
		}
	}
	
	/**
	 * The DrawPowerBlock Function:
	 * 
	 * Chooses the winning power block number
	 * 
	 * @return
	 */
	private int drawPowerBlock(){
		return powerBlocks.get(rand.nextInt(powerBlocks.size()));
	}
	
	
	/**
	 * 
	 * 
	 */
	public Sound drawNext(boolean official){
		if(onNumber<5){
			int index = rand.nextInt(numbers.size());
			wWhiteBlocks.set(onNumber,numbers.get(index));
			if(official){
				numbers.remove(index);
			}
		} else {
			this.powerBlock = powerBlocks.get(rand.nextInt(powerBlocks.size()));
		}
		return Sound.BLOCK_NOTE_PLING;
	}
	
	/**
	 * 
	 * 
	 */
	public void setOnNumber(int i){
		this.onNumber = i;
	}
	
	
	/**
	 * 
	 * 
	 */
	public int getOnNumber(){
		return onNumber;
	}
	
	
	/**
	 * The Sort Function:
	 * 
	 * Sorts The Newly Drawn White Blocks
	 * 
	 */
	public void sort(){
		int[] i = new int[wWhiteBlocks.size()];
		for(int x=0;x<i.length;x++){
			i[x]=wWhiteBlocks.get(x);
		}
		Arrays.sort(i);
		wWhiteBlocks.clear();
		for(int ii:i){
			wWhiteBlocks.add(ii);
		}
	}
	
	
	/**
	 * The Get White Blocks Function:
	 * 
	 * Retrieves The White Blocks For This Ticket
	 * @return
	 * 
	 */
	public ArrayList<Integer> getWhiteBlocks(){
		return wWhiteBlocks;
	}
	
	
	/**
	 * The Get Power Block Function:
	 * 
	 * Retrieves The Power Block For This Ticket
	 * @return
	 * 
	 */
	public int getPowerBlock(){
		return powerBlock;
	}
	
	
	/**
	 * The Is Old Function:
	 * 
	 * This Ticket Gets Marked
	 * As Old After The Drawing
	 * That This Ticket Was Purcahsed
	 * For Has Already Been Conducted.
	 * 
	 */
	public boolean isOld(){
		return isOld;
	}
	
	
	/**
	 * The Set Old Function:
	 * 
	 * Marks The Ticket As Old
	 * So It Will Not BE Considered Again
	 * In A Future Drawing. Tickets Are Only
	 * Good For A Single Drawing.
	 * 
	 */
	public Ticket setOld(){
		this.isOld = true;
		return this;
	}
	
	
	/**
	 * The Set Winnings Function:
	 * Sets The Prize (If Any) That Was
	 * Won With This Ticket.
	 *  
	 */
	public void setWinnings(String winnings){
		this.prizeAmt = winnings;
		owner.addWinningTicket(this);
	}
	
	
	/**
	 * The Get Winnings Function:
	 * Returns The Winnings For This Ticket
	 * In Order To Be Display In The Ticket
	 * Summary Window.
	 * 
	 */
	public String getWinnings(){
		return (!prizeAmt.equalsIgnoreCase("JACKPOT!")?"$":"")+prizeAmt;
	}
	
	
	/**
	 * The Get Winnings Function:
	 * This One Returns The Numerical
	 * Winning Amount.
	 * 
	 * When The Jackpot Is Won By This
	 * Ticket, The GetWinningsAmt Function
	 * Returns The Word "Jackpot" Which Does
	 * Not work. Instead It Returns -1;
	 * 
	 * 
	 */
	public double getWinningsAmt(){
		try{
			return Double.parseDouble(prizeAmt);
		}catch(Exception e1){
			return 0;
		}
	}
	
	
	/**
	 * The Set Draw Date Function:
	 * 
	 * @param date
	 * 
	 */
	public Ticket setDrawDate(Date date){
		this.drawn = date;
		return this;
	}
	
	
	/**
	 * The Get Draw Date Function:
	 * 
	 * Returns The Drawing Date Of This
	 * Ticket In Full Detail.
	 * 
	 * @return
	 * 
	 */
	public String getDrawDate(){
		if(drawn != null){
			return "Drawing Date: "+ddd[0].format(drawn);
		} else {
			return "Drawing Pending...";
		}
	}
	
	
	/**
	 * The Get Date Function:
	 * 
	 * Returns The Date Object
	 * 
	 */
	public Date getDate(){
		return drawn;
	}
	
	
	/**
	 * The Get Day Function:
	 * 
	 * Returns The Date That
	 * This Ticket Was Drawn, No Other
	 * Details Or Decorative Text.
	 * 
	 */
	public String getDay(){
		return ddd[1].format(drawn);
	}
	
	
	/**
	 * The Get Time Function:
	 * 
	 * Returns The Exact Time That
	 * This Ticket Was Drawn.
	 * 
	 */
	public String getTime(){
		return ddd[2].format(drawn);
	}
	
	
	/**
	 * Retrieves The Owner Of This Ticket
	 * 
	 * @return
	 * 
	 */
	public TPlayer getOwner(){
		return owner;
	}
	
	
	/**
	 * The Get ID Function:
	 * 
	 * Returns The ID Of This Ticket.
	 * Generally Used For Recording Purposes.
	 * 
	 */
	public String getID(){
		return ((ID<100000)&(ID>=10000))?"0"+ID:((ID<10000)&(ID>=1000))?"00"+ID:((ID<1000)&(ID>=100))?"000"+ID:((ID<100)&(ID>=10))?"0000"+ID:"00000"+ID;
	}
	
	
	/**
	 * The To String Function:
	 * 
	 * Returns A String Representation Of This Ticket
	 * 
	 */
	public String toString(){
		String i = "Numbers:  "+wh+kk+"|"+wh+"  ";
		for(int x = 0;x<5;x++){
			if(wWhiteBlocks.get(x)<10){i+="0";}
			i+=wh+wWhiteBlocks.get(x)+" ";
		}
		i+=" "+wh+kk+"|"+re+"   Power Block: ";
		if(powerBlock<10){i+=re+bo+"0";}
		i+=re+bo+powerBlock;
		return i;
	}
	
	
	/**
	 * The Simple To String Function:
	 * 
	 * Returns A String Representation Of
	 * Just The Most Necessary Text With No
	 * Decoration Or Coloring Effects.
	 * 
	 */
	public String simpleToString(){
		String wb = "";
		String pb = "";
		for(int i:wWhiteBlocks){
			if(i<10){wb+="0";}
			wb+=i+" ";
		}
		if(powerBlock<10){pb+="0";}
		pb+=powerBlock+"";
		return ((owner!=null)?"ID: "+getID()+"   ":"")+"[[WB] "+wb+" - [PB] "+pb+" ]";
	}
	
	
	/**
	 * The Check Ticket Function:
	 * 
	 * Checks A Player's Ticket And Compares
	 * It To The Winning Numbers.
	 * 
	 * Returns True If All Numbers Including The
	 * Power Block Number Match.
	 * 
	 * @param other
	 * @return
	 * 
	 */
	public Ticket checkTicket(Ticket other){
		int lvl = 0;
		if(getPowerBlock()==other.getPowerBlock()){
			lvl++;
			other.setPrizeLvl(lvl);
		} else {
			other.setPrizeLvl(lvl);
			return other; /* You Must Match At Least The PowerBlock Before Other Prizes Can Be Won */
		}
		for(int x=0;x<5;x++){
			if(!(getWhiteBlocks().get(x)==other.getWhiteBlocks().get(x))){
				lvl+=pL(other);
				other.setPrizeLvl(lvl);
				return other;
			}
		}
		other.setPrizeLvl(lvl+5);
		return other;
	}
	
	/**
	 * Set Prize Level Function:
	 * 
	 */
	public void setPrizeLvl(int lvl){
		this.prizeLvl = lvl;
	}
	
	
	/**
	 * The Get Prize Level Function:
	 * 
	 */
	public int getPrizeLvl(){
		return prizeLvl;
	}
	
	
	/**
	 * THe Set Status Function:
	 * 
	 */
	public void setStatus(){
		this.status = (prizeLvl>0)?"  <  WON "+((prizeLvl<6)?"$"+main.getPrizes().get(getPrizeLvl()-1):"JACKPOT"):(owner!=null)?"  <  X":"";
	}
	
	
	/**
	 * The Get Status Function:
	 * 
	 */
	public String getStatus(){
		return status;
	}
	
	
	/**
	 * The Prize Level Function:
	 * 
	 * @param other
	 * @return
	 */
	private int pL(Ticket other){
		int lvl=0;
		for(int i:getWhiteBlocks()){
			if(other.contains(i,0)){
				lvl++;
			}
		}
		return lvl;
	}
	
	
	/**
	 * The Contains Function:
	 *  
	 */
	private boolean contains(int number,int index){
		if(getWhiteBlocks().get(index)==number){
			return true;
		} else {
			if(index<4){
				return contains(number,index+1);
			} else {
				return false;
			}
		}
	}
}
