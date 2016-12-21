package me.virusbrandon.util;

import java.io.File;

import me.virusbrandon.powerblock.Main;

import org.bukkit.configuration.file.YamlConfiguration;

public class Bank {
	private Main main;
	private YamlConfiguration Banks;
	
	public Bank(Main main){
		this.main = main;
	}
	
	/**
	 * Init Function:
	 * 
	 * Checks For And Creates The
	 * Necessary Files And Folders
	 * 
	 */
	public void init(){
		try{
			File file = new File(main.getPaths().get(1));
			if(!file.exists()){
				main.mkPbDir();
				file = new File(main.getPaths().get(1));
				file.createNewFile();
			}
			Banks = YamlConfiguration.loadConfiguration(file);
		}catch(Exception e1){}
	}
	
	/**
	 * The Bank Function:
	 * 
	 * Manages Player Balances
	 * For Power Block.
	 * 
	 * Pass In A Negative Amount For
	 * Withdraw Or Pass In A Positive
	 * Amount For Deposit.
	 * 
	 * In Order To Manually Add Or Remove
	 * Money To Or From A Player's Account,
	 * That Player Must Be Online.
	 * 
	 * Otherwise, If An External Service
	 * Such As Enjin Or Buycraft, Is Completing
	 * A Currency Purchase, This Request Will
	 * Finished Successfully.
	 * 
	 * The Only Way A Retarded User Account Can Be
	 * Created In This Simple ECO System Is If The
	 * Operator Of The Server Who Is In Charge Deposits
	 * Money Into An "<Insert Retarded Name Here>"'s Account.
	 * 
	 * When Usable Credit Is Purchased Thru The Server Store,
	 * The Player Is Verified Thru Mojang's Servers. This Is Why
	 * No Player Verification Code Exists Here.
	 * 
	 */
	public Result process(String name,double amount,boolean performAction){
		if(performAction){
			if(amount<0){
				if(Banks.getDouble(name)>=(Math.abs(amount))){
					Banks.set(name,Math.floor(Banks.getDouble(name)+amount));save();
					return new Result(name,Math.floor(Banks.getDouble(name)));
				}
			} else {
				Banks.set(name, Math.floor(Banks.getDouble(name)+amount));save();
				return new Result(name,Math.floor(Banks.getDouble(name)));
			}
			return new Result(name,-1);
		} else {
			return new Result(name,Math.floor(Banks.getDouble(name)));
		}
	}
	
	/**
	 * The Save Banks Function:
	 * 
	 */
	private void save(){
		try{
			Banks.save(main.getPaths().get(1));
		}catch(Exception e1){}
	}
}
