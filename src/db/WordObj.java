package db;

public class WordObj {
	
	private String cuvant;
	private String tip;
	private int frecventa;
	private boolean activ;
	private boolean adaugat;
	
	
	public WordObj(String cuvant) {
		this.cuvant=cuvant;
		this.tip = null;
		this.frecventa = 1;
		this.activ = true;
		this.adaugat = true;
	}
	
	public WordObj(String cuvant, String Tip, int frecventa, boolean activ, boolean adaugat) {
		this.cuvant=cuvant;
		this.tip = Tip;
		this.frecventa = 1;
		this.activ=activ;
		this.adaugat=adaugat;
		
	}

	public String getCuvant() {
		return cuvant;
	}

	public void setCuvant(String cuvant) {
		this.cuvant = cuvant;
	}

	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}

	public int getFrecventa() {
		return frecventa;
	}

	public void setFrecventa(int frecventa) {
		this.frecventa = frecventa;
	}

	public boolean isActiv() {
		return activ;
	}

	public void setActiv(boolean activ) {
		this.activ = activ;
	}

	public boolean isAdaugat() {
		return adaugat;
	}

	public void setAdaugat(boolean adaugat) {
		this.adaugat = adaugat;
	}

	public void crestereFrecventa() {
		frecventa = frecventa+1;
	}
	
	public void activareCuvant() {
		activ = true;
	}
	public void dezactivareCuvant() {
		activ = false;
	}
}
