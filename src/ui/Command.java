package ui;

/**
 * 
 * @author Dacian
 *
 */

public class Command {
	
	private MainPage mainPage;
	private MenuBarInitializer MBI;
	private MenuBarEvents MBE;
	public Command(MainPage mainPage, MenuBarInitializer mBI, MenuBarEvents mBE) {
		super();
		this.mainPage = mainPage;
		MBI = mBI;
		MBE = mBE;
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
	
	
	
}
