package me.virusbrandon.database;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import me.virusbrandon.powerblock.Main;
import me.virusbrandon.powerblock.Ticket;

public class dbManager {
	private MysqlDataSource ds = new MysqlDataSource();
	private String[] inf = null;
	
	public dbManager(Main main){
		this.inf = new String[]{main.getConfig().getString("DATABASE_ADDRESS"),main.getConfig().getString("DATABASE_USER"),main.getConfig().getString("DATABASE_PASS")};
		ds.setServerName(inf[0]);
		ds.setUser(inf[1]);
		ds.setPassword(inf[2]);
	}
	
	
	/**
	 * The Update Numbers Function:
	 * 
	 * Will Create / Use A Connection To A Database and
	 * record the most recent winning numbers to the server
	 * website.
	 * 
	 * @param ticket
	 */
	public void updateNumbers(Ticket tic){
		String st = "INSERT INTO Winning_Numbers"+"(DrawDateAndTime,FirstNumber,SecondNumber,ThirdNumber,FourthNumber,FifthNumber,PowerBlock)"+"VALUES"+"("+tic.getDay()+" - "+tic.getTime()+","+tic.getWhiteBlocks().get(0)+","+tic.getWhiteBlocks().get(1)+","+tic.getWhiteBlocks().get(2)+","+tic.getWhiteBlocks().get(3)+","+tic.getWhiteBlocks().get(4)+","+tic.getPowerBlock()+")";
		try{
			ds.getConnection().createStatement().executeUpdate(st);
		}catch(Exception e1){
			/* It Didn't Work For Some Reason... Well Shiit.. */
			e1.printStackTrace();
		}
	}
}
