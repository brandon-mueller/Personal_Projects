package me.virusbrandon.ui;

import me.virusbrandon.localapis.GUIFactory;
import me.virusbrandon.powerblock.Main;
import me.virusbrandon.powerblock.Timer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ResultWindow {
	private Main main;
	private Inventory resWin;
	private Timer draw;
	private String wh=ChatColor.WHITE+"",bo=ChatColor.BOLD+"",re=ChatColor.RED+"",lp=ChatColor.LIGHT_PURPLE+"",bl=ChatColor.BLACK+"",gr=ChatColor.GREEN+"",aq=ChatColor.AQUA+"",ye=ChatColor.YELLOW+"",ga=ChatColor.GRAY+"",kk=ChatColor.MAGIC+"";
	private String board;
	private String st = "",sst = "";
	private int[] frm = new int[]{0,1,2,3,4,5,6,7,8,17,26,25,24,23,22,21,20,19,18,9};
	private int[] i = new int[]{0,1,2,3,4,5,6,7,8,10,11,12,13,14,15,16,17,18};
	private int[] v = new int[]{10,17,27,31,36,38,43,45,47,52,54};
	private Material[] mat = new Material[]{Material.PAPER,Material.EMPTY_MAP,Material.STAINED_GLASS_PANE,Material.WOOL};
	private DyeColor[] ddc = new DyeColor[]{DyeColor.LIGHT_BLUE,DyeColor.BLACK};
	@SuppressWarnings("deprecation")
	private Byte[] dc = new Byte[]{DyeColor.LIME.getData(),DyeColor.YELLOW.getData(),DyeColor.RED.getData(),DyeColor.BLACK.getData(),DyeColor.WHITE.getData(),DyeColor.GRAY.getData()};
	
	private int ct = 2;
	@SuppressWarnings("unused")
	private GUIFactory fjska;
	private boolean drawing = false;
	private int a = 0,dir = 1,phase = 1,tone = 0;
	
	/**
	 * The Result Window Constructor
	 * 
	 * @param main
	 */
	public ResultWindow(Main main){
		this.main = main;
		board = "         ✦ ✧ ✦ "+main.pr()+re+" ✦ ✧ ✦";
		resWin = Bukkit.createInventory(null, 54,main.pr());
		constString(23);
	}
	
	
	/**
	 * The Frame Tick Function:
	 * 
	 * The Animated Frame Around Where
	 * The Numbers Are Displayed Is Handled
	 * By This.
	 * 
	 */
	@SuppressWarnings("deprecation")
	public int frmTick(int md){
		if(((drawing)&(md==1))|((!drawing)&(md==0))){
			makeArt();
			for(int x=0;x<54;x++){
				main.gF().setUpItem(resWin,x,new ItemStack(mat[2],1,dc[3]),main.pr(),(x==31)?bl+"#NOW":"",bl+"#UI");
				fjska = ((x>=v[0])&(x<v[1]))?main.getWTicket()!=null?(x-10)<=main.getWTicket().getOnNumber()?(x<15)?main.gF().setUpItem(resWin,x,new ItemStack(mat[3],main.getWTicket().getWhiteBlocks().get(x-10),dc[4]),wh+"White Block #"+wh+bo+main.getWTicket().getWhiteBlocks().get(x-10),txt(23),"",aq+main.getWTicket().getDrawDate(),bl+"#UI"):(x!=15)?gray(x):null:((x==16)&(main.getWTicket().getOnNumber()==5))?main.gF().setUpItem(resWin,x,new ItemStack(mat[3],main.getWTicket().getPowerBlock(),dc[2]),txt(17)+re+bo+main.getWTicket().getPowerBlock(),txt(23),"",aq+main.getWTicket().getDrawDate(),bl+"#UI"):(x!=15)?gray(x):null:(x!=15)?gray(x):(x!=15)?gray(x):null:(((x>=v[2])&(x<v[3])?(main.gF().setUpItem(resWin,x,new ItemStack(mat[2],1,dc[0]),(drawing)?txt(1):txt(18),(drawing)?txt(2):txt(10),(drawing)?txt(3):txt(11),(drawing)?txt(4):txt(12),(drawing)?"":txt(13),bl+((!drawing)?"#BUY":""),bl+"#UI")):((x>=32)&(x<36))?(main.gF().setUpItem(resWin,x,new ItemStack(mat[2],2,dc[0]),(drawing)?txt(1):txt(5),(drawing)?txt(2):txt(6),(drawing)?txt(3):txt(7),(drawing)?txt(4):"",(drawing)?"":txt(8),(drawing)?"":txt(9),bl+((!drawing)?"#PICK":""),bl+"#UI")):((x>=v[4])&(x<v[5]))|((x>=v[6])&(x<v[7]))?(main.gF().setUpItem(resWin,x,new ItemStack(mat[2],1,dc[1]),txt(22),bl+"#VIEW",bl+"#UI")):(((x>=v[5])&(x<v[6]))|((x>=v[8])&(x<v[9])))?(main.gF().setUpItem(resWin,x,new ItemStack(((ct%2==0)?mat[0]:mat[1]),1)," ",re+board.substring(main.getT(), board.length()),"",st,"",txt(15),wh+"◆  "+aq+main.nd(),txt(20)+gr+bo+"$"+main.getPricePerTicket(),txt(21)+gr+bo+"$"+main.getDf().format(main.getPot()),txt(14)+gr+bo+main.getTicketsPurchased(),"",txt(16),prizeInfo(5,"JACKPOT"),prizeInfo(4,main.getPrizes().get(4)+""),prizeInfo(3,main.getPrizes().get(3)+""),prizeInfo(2,main.getPrizes().get(2)+""),prizeInfo(1,main.getPrizes().get(1)+""),prizeInfo(0,main.getPrizes().get(0)+""),"",sst,"",re+board.substring((main.getT()+1)%2),bl+"#UI")):((x>=v[7])&(x<v[8]))|((x>=v[9])&(x<v[10]))?(main.gF().setUpItem(resWin,x,new ItemStack(mat[2],1,dc[2]),txt(19),bl+"#CLOSE",bl+"#UI")):null));
			}
			if(!drawing){
				for(int x=0;x<i.length;x++){
					i[x]++;
					i[x]=i[x]%frm.length;
				}
				for(int ii:i){
					main.gF().setUpItem(resWin,frm[ii],new ItemStack(mat[2],1),main.pr(),"",bl+"#UI");
				}
			} 
			for(int ii:i){
				main.gF().setUpItem(resWin,frm[ii],new ItemStack(mat[2],1,(drawing?randDColor().getData():dc[4])),main.pr(),"",bl+"#UI");
			}
		}
		return 0;
	}
	
	
	/**
	 * Prize Info Helper Function:
	 * 
	 */
	private String prizeInfo(int whites,String amount){
		String s = (whites>0)?ye+"x"+whites+wh+bo+" Number"+wh+"("+wh+bo+"s"+wh+")"+ye+" + "+re+bo+"PB":wh+bo+"Just The "+re+bo+"PowerBlock";
		return wh+"◆  "+s+ye+" = "+((whites!=5)?"$":"")+gr+bo+amount+"!";
	}
	
	
	/**
	 * Test String Art
	 * 
	 */
	private void makeArt(){
		st+=st.substring(0,3);
		st=st.substring(3,st.length());
		sst=(sst.substring(sst.length()-3,sst.length())+sst);
		sst=sst.substring(0,sst.length()-3);
		ct=(ct%500)+1;
	}
	
	private void constString(int i){
		if(i>1){
			st+=wh+"▓";
			constString(i-1);
		}else if(i==1){
			st+=bl+"▓";
			sst=st;
			sst=(sst.substring(sst.length()-3,sst.length())+sst);
			sst=sst.substring(0,sst.length()-3);
		}
	}
	
	
	/**
	 * The Is Drawing Function:
	 * 
	 */
	public boolean isDrawing(){
		return drawing;
	}
	
	/**
	 * The Set Is Drawing Function:
	 * 
	 */
	public void setIsDrawing(boolean isDrawing){
		this.drawing = isDrawing;
		if(isDrawing){
			i = new int[]{0,1,2,3,4,5,6,7,8,10,11,12,13,14,15,16,17,18};
			this.draw = new Timer(this,main,3);
			main.getWTicket().drawNext(false);
			draw.start(1);
		}
	}
	
	/**
	 * The Draw Phase Function:
	 * 
	 */
	public int drawPhase(){
		if(phase < 7){
			frmTick(1);
			main.getWTicket().drawNext(false);
			tone+=dir;
			sendSoundToAll(Sound.BLOCK_NOTE_PLING,2,.5f+(.5f*tone));
			sendSoundToAll(Sound.BLOCK_NOTE_BASS,1,.5f+(.5f*tone));
			dir=(((tone>=3)&(dir==1))?-1:((tone<=0)&(dir==-1))?1:dir);
			a++;
			if(a>(40/phase)){
				phase++;
				a=0;
				draw.stop();
				main.getWTicket().drawNext(true);
				nextPhase();
				sendSoundToAll(Sound.ENTITY_FIREWORK_LARGE_BLAST,2,.5f);
			}
		} else {
			a = 0;
			dir = 1;
			phase = 1;
			tone = 0;
			draw.stop();
			main.getWTicket().sort();
			setIsDrawing(false);
			main.drawFinish();
			sendSoundToAll(Sound.ENTITY_FIREWORK_LARGE_BLAST_FAR,2,1);
			sendSoundToAll(Sound.ENTITY_PLAYER_LEVELUP,2,1);
		}
		return 0;
	}
	
	/**
	 * The Next Phase Function:
	 * 
	 */
	public int nextPhase(){
		draw.start(phase);
		if(phase<7){
			main.getWTicket().setOnNumber(main.getWTicket().getOnNumber()+1);
			main.getWTicket().drawNext(false);
		}
		return 0;
	}
	
	
	
	
	/**
	 * The Send Sound To All Function:
	 * 
	 * @param s
	 * @param v
	 * @param pitch
	 */
	
	private void sendSoundToAll(Sound s, float v, float pitch){
		for(Player p:Bukkit.getOnlinePlayers()){
			p.playSound(p.getLocation(), s, v, pitch);
		}
	}
	
	
	/**
	 * 
	 * 
	 */
	private GUIFactory gray(int i){
		main.gF().setUpItem(resWin,i, new ItemStack(mat[3],0,dc[5]),wh+"???",re+"There Hasn't Been A",re+"Drawing Yet.","",lp+"Once A Drawing Is",lp+"Conducted, You'll Start",lp+"Seeing Those Results",lp+"Right Here",bl+"#UI");
		return null;
	}
	
	
	/**
	 * The Text Function:
	 * 
	 */
	private String txt(int i){
		return((i==1)?re+bo+"A Drawing Is In Progress...":(i==2)?lp+"So Ticket Purchasing Is Temporarily":(i==3)?lp+"Suspended, But Hang In There, The Drawing":(i==4)?lp+"Will Be Finished In About 30 Seconds.":(i==5)?wh+kk+"|"+gr+bo+"  Choose Your Numbers  "+wh+kk+"|":(i==6)?lp+"Feel Like You Have Your":(i==7)?lp+"Own Set Of Lucky Numbers?":(i==8)?lp+"Enter Your Own Numbers":(i==9)?lp+"Right Here!":(i==10)?lp+"Left-Click = Buy 1":(i==11)?lp+"Right-Click = Buy 2":(i==12)?lp+"Shift-Left-Click = Buy 20":(i==13)?lp+"Shift-Right-Click = Buy 100":(i==14)?wh+"◆  "+aq+"Total Active Tickets: ":(i==15)?gr+bo+"Drawing Information:":(i==16)?ye+bo+"Prize Information:":(i==17)?re+"Power Block #":(i==18)?wh+kk+"|"+gr+bo+"  Quick Buy  "+wh+kk+"|":(i==19)?wh+kk+"|"+re+bo+"  Close This Window  "+wh+kk+"|":(i==20)?wh+"◆  "+aq+"Price Per Ticket: ":(i==21)?wh+"◆  "+aq+"Current Jackpot: ":(i==22)?wh+kk+"|"+ye+bo+"  View Your Tickets  "+wh+kk+"|":(i==23)?wh+kk+"|  "+ga+txt(5).substring(18,txt(5).length()):"");
	}
	
	
	/**
	 * The Random Dye Color
	 * 
	 */
	public DyeColor randDColor(){
		return ddc[((int)(Math.random()*ddc.length))];
	}
	

	
	/**
	 * The Get Window Function:
	 * 
	 * Returns The View Of This
	 * Inventory.
	 * 
	 * @return
	 */
	public Sound getWin(Player p){
		p.openInventory(resWin);
		return main.getSounds()[0];
	}
}
