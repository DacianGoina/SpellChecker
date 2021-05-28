package db;

import java.util.Date;

public class WordObj_rev {
	
	private int id;
	private String cuvant;
	private String tip;
	private int frecventa;
	private int lungime;
	private boolean activ;
	private boolean adaugat;
	private Date date;
	
	public WordObj_rev(String cuvant,int lungime) {
		
		this.id = -1;
		this.cuvant = cuvant;
		this.tip = null;
		this.frecventa = 1;
		this.lungime = lungime;
		this.activ = true;
		this.adaugat = false;
		this.date = null;
	}
	
	public WordObj_rev(int id, String cuvant, String tip, int frecventa, int lungime, boolean activ, boolean adaugat,
			Date date) {
		super();
		this.id = id;
		this.cuvant = cuvant;
		this.tip = tip;
		this.frecventa = frecventa;
		this.lungime = lungime;
		this.activ = activ;
		this.adaugat = adaugat;
		this.date = date;
	}
	@Override
	public String toString() {
		return "WordObj_rev [id=" + id + ", cuvant=" + cuvant + ", tip=" + tip + ", frecventa=" + frecventa
				+ ", lungime=" + lungime + ", activ=" + activ + ", adaugat=" + adaugat + ", date=" + date + "]";
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public int getLungime() {
		return lungime;
	}
	public void setLungime(int lungime) {
		this.lungime = lungime;
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
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	
	

}
