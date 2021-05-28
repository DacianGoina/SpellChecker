package alg;

public class Distanta{
	int IDistantaDeEditare;
	int ICuvantFaraDiacritice;

	public Distanta(int iDistantaDeEditare, int iCuvantFaraDiacritice) {
		
		IDistantaDeEditare = iDistantaDeEditare;
		ICuvantFaraDiacritice = iCuvantFaraDiacritice;
	}

	public int getIDistantaDeEditare() {
		if(IDistantaDeEditare!=0)
		return IDistantaDeEditare+ICuvantFaraDiacritice/2;
		return IDistantaDeEditare;
	}

	public void addDiactritice() {
		this.ICuvantFaraDiacritice = this.ICuvantFaraDiacritice + 1;
	}

	public void setIDistantaDeEditare(int iDistantaDeEditare) {
		IDistantaDeEditare = iDistantaDeEditare;
	}

	public int getICuvantFaraDiacritice() {
		return ICuvantFaraDiacritice;
	}

	public void setICuvantFaraDiacritice(int iCuvantFaraDiacritice) {
		ICuvantFaraDiacritice = iCuvantFaraDiacritice;
	}

	@Override
	public String toString() {
		if (IDistantaDeEditare == 0 && ICuvantFaraDiacritice == 0)
			return "Cuvintele sunt similare";
		else if (IDistantaDeEditare == 0 && ICuvantFaraDiacritice != 0)
			return "Cuvintele sunt similare,dar nu unul dintre ele nu are diacritice";
		else {
			int a = IDistantaDeEditare + ICuvantFaraDiacritice;
			return "Distanta de editare este: " + a;
		}
	}
}
