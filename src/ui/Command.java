package ui;
import java.util.TreeMap;

import db.*;
/**
 * 
 * @author Dacian
 *
 */

public class Command {
	
	private MainPage mainPage;
	private MenuBarInitializer MBI;
	private MenuBarEvents MBE;
	private TreeMap<String,WordObj>cuvinte;
	public Command(MainPage mainPage, MenuBarInitializer mBI, MenuBarEvents mBE) {
		super();
		this.mainPage = mainPage;
		MBI = mBI;
		MBE = mBE;
	}
	public Command(MainPage mainPage, MenuBarInitializer mBI, MenuBarEvents mBE,TreeMap<String,WordObj>cuvinte){
		super();
		this.mainPage = mainPage;
		MBI = mBI;
		MBE = mBE;
		this.cuvinte=cuvinte;
	}
	public Command() {
		
	}
	public MainPage getMainPage() {
		return mainPage;
	}
	public void setMainPage(MainPage mainPage) {
		this.mainPage = mainPage;
	}
	public MenuBarInitializer getMBI() {
		return MBI;
	}
	public void setMBI(MenuBarInitializer mBI) {
		MBI = mBI;
	}
	public MenuBarEvents getMBE() {
		return MBE;
	}
	public void setMBE(MenuBarEvents mBE) {
		MBE = mBE;
	}
	public TreeMap<String, WordObj> getCuvinte() {
		return cuvinte;
	}
	public void setCuvinte(TreeMap<String, WordObj> cuvinte) {
		this.cuvinte = cuvinte;
	}
	
	
	
}
