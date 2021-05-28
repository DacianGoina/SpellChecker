package db;

import java.util.Date;
/**
 * 
 * @author Bica Anamaria
 *
 */
public class WordObj {
	
	private int id;
	private String cuvant;
	private String tip;
	private int frecventa;
	private boolean activ;
	private boolean adaugat;
	private Date date;
	
	public WordObj(String cuvant) {
		this.id = -1;
		this.cuvant = cuvant;
		this.tip = null;
		this.frecventa = 1;
		this.activ = true;
		this.adaugat = true;
		this.date = null;
	}
	
	

	public WordObj(int id, String cuvant, String tip, int frecventa, boolean activ, boolean adaugat, Date date) {
		this.id = id;
		this.cuvant = cuvant;
		this.tip =   tip;
		this.frecventa = frecventa ;
		this.activ = activ;
		this.adaugat = adaugat;
		this.date = date;
		
	}
	
	@Override
	public String toString() {
		return "WordObj [id=" + id + ", cuvant=" + cuvant + ", tip=" + tip + ", frecventa=" + frecventa + ", activ="
				+ activ + ", adaugat=" + adaugat + ", date=" + date + "]";
	}

	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (activ ? 1231 : 1237);
		result = prime * result + (adaugat ? 1231 : 1237);
		result = prime * result + ((cuvant == null) ? 0 : cuvant.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + frecventa;
		result = prime * result + id;
		result = prime * result + ((tip == null) ? 0 : tip.hashCode());
		return result;
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WordObj other = (WordObj) obj;
		if (activ != other.activ)
			return false;
		if (adaugat != other.adaugat)
			return false;
		if (cuvant == null) {
			if (other.cuvant != null)
				return false;
		} else if (!cuvant.equals(other.cuvant))
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (frecventa != other.frecventa)
			return false;
		if (id != other.id)
			return false;
		if (tip == null) {
			if (other.tip != null)
				return false;
		} else if (!tip.equals(other.tip))
			return false;
		return true;
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
