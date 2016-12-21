package me.virusbrandon.ui;

import me.virusbrandon.powerblock.Main;
import me.virusbrandon.powerblock.TPlayer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class NumberChooser {
	private Inventory ch;
	private int onNumber = 0;
	private String current = "";
	private Main main;
	private int[] numbers = new int[]{-1,-1,-1,-1,-1,-1};
	private TPlayer player;
	
	@SuppressWarnings("deprecation")
	private Byte[] dc = new Byte[]{DyeColor.BLACK.getData(),DyeColor.YELLOW.getData(),DyeColor.WHITE.getData(),DyeColor.RED.getData(),DyeColor.GRAY.getData(),DyeColor.LIME.getData()};
	private int[] pad = new int[]{31,21,22,23,12,13,14,3,4,5};
	private int[] preview = new int[]{46,47,48,49,50,52};
	private int[] close = new int[]{0,9,18,8,17,26};
	private Material[] mt = new Material[]{Material.STAINED_GLASS_PANE,Material.WOOL};
	private String bl=ChatColor.BLACK+"",wh=ChatColor.WHITE+"",bo=ChatColor.BOLD+"",gr=ChatColor.GREEN+"",re=ChatColor.RED+"",ga=ChatColor.GRAY+"",ye=ChatColor.YELLOW+"",lp=ChatColor.LIGHT_PURPLE+"",aq=ChatColor.AQUA+"",kk=ChatColor.MAGIC+"";
	
	/**
	 * The Number Chooser Constructor:
	 * 
	 * Initializes All Necessary Fields
	 * And Objects That Allow This User
	 * Interface To Work Properly
	 * 
	 * @author Brandon
	 *
	 */
	public NumberChooser(Main main,TPlayer player){
		this.main = main;
		this.player = player;
		this.ch = Bukkit.createInventory(null, 54,main.pr());
		refresh();
	}
	
	
	/**
	 * The Refresh Function:
	 * 
	 * Re-Draws The Window To Reflect
	 * Any Change In The Information That
	 * Is Displayed There.
	 * 
	 */
	private void refresh(){
		for(int x=0;x<54;x++){
			main.gF().setUpItem(ch,x,new ItemStack(mt[0],1,dc[0])," ",bl+"#UI");
		}
		for(int x=0;x<pad.length;x++){
			main.gF().setUpItem(ch,pad[x],new ItemStack(mt[0],x,dc[1]),wh+bo+x+"",wh+"Click To Select","",aq+bo+"Block Range:",lp+"White Blocks: "+gr+bo+"[ "+re+"1 - "+main.getHWhiteBlock()+gr+bo+" ]",lp+"Power Block: "+gr+bo+"[ "+re+"1 - "+main.getHPowerBlock()+gr+bo+" ]",bl+"#NUM",bl+"#UI");
		}
		main.gF().setUpItem(ch,30,new ItemStack(mt[0],1,(clrBtn()|onNumber>0)?dc[3]:dc[2]),clrBtn()?txt(6):(onNumber>0)?re+bo+"✖ Backup To Prev Number":re+bo+"✖ Nothing To Clear","",((clrBtn()|onNumber>0)?bl+"#CLEAR":""),bl+"#UI");
		main.gF().setUpItem(ch,32,new ItemStack(mt[0],1,accBtn()?dc[5]:dc[2]),(onNumber<5)?(((numbers[onNumber]<=main.getHWhiteBlock()))?((numbers[onNumber]>0)?(contains(numbers,0,0,0)?txt(5):txt(2)):wh+bo+"Please Pick A Number."):txt(3)):(((numbers[onNumber]<=main.getHPowerBlock()))?((numbers[onNumber]>0)?gr+bo+"✔ BUY TICKET":wh+bo+"Please Pick A Number."):txt(3)),(onNumber<5)?(((numbers[onNumber]>main.getHWhiteBlock())|(numbers[onNumber]<=0))?(numbers[onNumber]<main.getHWhiteBlock()?gr:re+bo)+txt(4,0)+" White Block: "+wh+bo+main.getHWhiteBlock():(contains(numbers,0,0,0)?bl+"#NEXT":ga+"Click \""+txt(6)+ga+"\" To Re-enter")):(onNumber==5)?(((numbers[onNumber]>main.getHPowerBlock())|(numbers[onNumber]<=0))?((numbers[onNumber]<main.getHPowerBlock())?gr:re+bo)+txt(4,0)+" PowerBlock: "+re+bo+main.getHPowerBlock():""):"",(accBtn()?bl+"#NEXT":(contains(numbers,0,0,0))?(((numbers[onNumber]>0)|(numbers[onNumber]==-1))?gr:re+bo)+txt(4,1)+((onNumber<5)?" White Block: "+wh+bo+"1":" PowerBlock: "+re+bo+"1"):""),bl+"#UI");
		for(int x=0;x<preview.length;x++){
			if(numbers[x]==-1){
				if((x!=onNumber)&(numbers[x]==-1)){
					main.gF().setUpItem(ch,preview[x],new ItemStack(mt[1],0,dc[4]),ga+bo+((x<5)?"Your "+pre(x):"Your Powerblock")+" Is DISABLED",ga+"Please Pick Your "+pre(onNumber),bl+"#UI");
				} else {
					main.gF().setUpItem(ch,preview[x],new ItemStack(mt[1],0),wh+bo+"Choose Your "+((onNumber<5)?pre(onNumber):re+bo+"PowerBlock")+wh+bo+" NOW!",bl+"#UI");
				}
			} else {
				main.gF().setUpItem(ch,preview[x],new ItemStack(mt[1],numbers[x],(accBtn()|onNumber>x)?dc[5]:dc[3]),(x!=5)?(wh+bo+"Your "+pre(x)+" Is: "+gr+bo+numbers[x]):wh+bo+"Your "+re+bo+"PowerBlock "+wh+bo+"Is: "+re+bo+numbers[x],((x!=onNumber)&x!=5)?(onNumber!=5)?ga+"Now, Please Pick Your "+pre(onNumber):ga+"Now, Please Pick Your "+re+bo+"PowerBlock":(x!=5)?((numbers[x]>main.getHWhiteBlock())|numbers[x]<=0|!contains(numbers,0,0,0))?txt(1):ga+"Please Click \""+txt(5)+ga+"\" To Continue, OR":(numbers[x]<=main.getHPowerBlock()&(numbers[x]>0))?ga+"Please Click \""+gr+bo+"✔ BUY TICKET"+ga+"\" To Continue, OR":txt(1),(x==onNumber)?ga+"Click \""+txt(6)+ga+"\" To Re-enter":"",bl+"#UI");
			}
		}
		for(int i:close){
			main.gF().setUpItem(ch,i,new ItemStack(mt[0],1,dc[3]),wh+kk+"|"+re+bo+"  Back To Results Window  "+wh+kk+"|",re+"If You Do Not Finish",re+"Your Numbers Here, Your",re+"Changes Will Be Lost!",bl+"#BACK",bl+"#UI");
		}
	}
	
	
	/**
	 * The Get Window Function:
	 * 
	 * Returns The Window View Of This
	 * User Interface.
	 * 
	 * @return
	 */
	public Inventory getWin(){
		return ch;
	}
	
	
	/**
	 * The Add Number Function:
	 * 
	 * Adds A Chosen Number To User's
	 * List Of Numbers For Their Next Ticket
	 * Purchase.
	 * 
	 * @param num
	 */
	public Sound addNum(String num){
		if(current.length()<2){
			current+=num;
			numbers[onNumber]=Integer.parseInt(current);
			refresh();
			return main.getSounds()[0];
		} else {
			return null;
		}
	}
	
	
	/**
	 * The Accept Function:
	 * 
	 * Submits The Next Number For Your
	 * Ticket And As Long As The Input Is
	 * Valid, We Will Move Onto The Next Number
	 * Or Proceed To Purchase The Ticket.
	 * 
	 * @param p
	 */
	public Sound accept(Player p){
		int i = Integer.parseInt(current);
		if(onNumber<5){
			numbers[onNumber]=i;
			onNumber++;
		} else {
			numbers[onNumber]=i;
			main.buyTickets(1, p, player, true);
			main.getRes().getWin(p);
		}
		clearCurrent();
		refresh();
		return main.getSounds()[0];
	}
	
	
	/**
	 * The Clear Current Function:
	 * 
	 * Clears The Cursor Of Your Input
	 * If You Made A Mistake Or Decided
	 * That You Want A Diffenr Number There.
	 * 
	 */
	public Sound clearCurrent(){
		if(current.length()>0){
			this.current = "";
			numbers[onNumber]=-1;
			refresh();
			return main.getSounds()[0];
		} else {
			if(onNumber>0){
				onNumber--;
				numbers[onNumber]=-1;
				refresh();
			}
			return main.getSounds()[0];
		}
	}
	
	
	/**
	 * The Get Numbers Function:
	 * 
	 * Returns The List Of Numbers Chosen
	 * By The User For Their Next Ticket.
	 * 
	 * @return
	 */
	public int[] getNumbers(){
		return numbers;
	}
	
	/**
	 * The Contains Function: (Recursive)
	 * 
	 * Checks For Duplicate Numbers On
	 * The Ticket; We Do Not Allow A Number
	 * To Be Chosen More Than Once On The Same
	 * Ticket.
	 * 
	 * This Function Returns TRUE When You
	 * DO NOT Have Any Duplicate Numbers Currently.
	 *  
	 * @param i
	 * @param ii
	 * @param iii
	 * @param count
	 * @return
	 * 
	 */
	private boolean contains(int[] i,int ii,int iii,int count){
		if(ii<(i.length-1)&&(i[ii]!=-1)){
			if((i[ii]==i[iii])&(i[iii]!=-1)){
				count++;
			}
			iii++;
			if(iii==(i.length-1)){
				iii=0;
				ii++;
			}
			return contains(i,ii,iii,count);
		}
		return (count<=(onNumber+1));
	}
	
	/**
	 * Prefix Function
	 * 
	 */
	private String pre(int oN){
		String res = (oN+1)+"";
		res+=((oN%10)==0)?"st":((oN%10)==1)?"nd":((oN%10)==2)?"rd":"th";
		return res+" Number";
	}
	
	
	/**
	 * No Worky Function:
	 * 
	 */
	private String txt(int ... i){
		return((i[0]==1)?ye+bo+"["+re+bo+" ! "+ye+bo+"] "+ye+"This Won't Work.":(i[0]==2)?ye+bo+"["+re+bo+" ! "+ye+bo+"] "+ye+"No Duplicate Entries Allowed":(i[0]==3)?ye+bo+"["+re+bo+" ! "+ye+bo+"] "+ye+"Invalid Entry":(i[0]==4)?(i[1]==0?"Max":"Min")+" Allowable":((i[0]==5)?gr+bo+"✔ NEXT NUMBER":(i[0]==6)?re+bo+"✖ CLEAR":""));
	}
	
	private boolean clrBtn(){
		return(current.length()>0);
	}
	
	private boolean accBtn(){
		return((numbers[onNumber]>0)&(onNumber<5?numbers[onNumber]<=main.getHWhiteBlock():numbers[onNumber]<=main.getHPowerBlock())&((onNumber<5)?(contains(numbers,0,0,0)):true));
	}
}
