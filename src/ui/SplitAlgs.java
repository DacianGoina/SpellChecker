package ui;

import java.util.LinkedList;
import java.util.List;



/**
 * 
 * @author Dacian
 * Clasa pentru algoritmii de prelucrare text
 */
public class SplitAlgs {
		
	public char[] separatori = new char[] {' ', '.' , ',', '?', '!', '\n'};  
	public final int[] DIACRITICE = new int[] {259,226,238,537,539,536,206,538,258,194};
	// Primeste un char, verifica daca este separator
	// Daca este separator returneaza true, altfel returneaza false
	public boolean isSeparator(char a) {
		int n = separatori.length;
		for(int i=0;i<n;i++)
			if(a == separatori[i])
				return true;
		return false;
	}
	
	// Complexitatea aproximativ O(k*n), n - numarul de caractere, k - numarul de separatori
	// Primeste un String si scoate indicii cuvintelor (adica delimitarea)
	public List<List<Integer>> splitString(String text){
		List<List<Integer>> l = new LinkedList<>();
		List<Integer> a = new LinkedList<>(); // tine indexul de la begin
		List<Integer> b = new LinkedList<>(); // tine indexul de la end
		l.add(a);
		l.add(b);
		
		int i = 0;
		while(i < text.length()) { // pana ajunge la capatul textului
			while(i < text.length() && isSeparator(text.charAt(i)) == true) // cand va gasi primul non-separator acolo incepe cuvantul
				i++;
			int begin = i;
			while(i < text.length() && isSeparator(text.charAt(i)) == false) // cand timp nu mai gaseste separator inseamna ca suntem in cuvant
				i++;
			
			int end = i;
			if(begin != end) { // daca nu foloseam acest if erau probleme cand era separator la final - gasea interval de tipul [17,17]
			// am gasit un nou cuvant: text[begin,end]
				l.get(0).add(begin); // adauga indexul de inceput in lista corespunzatoare
				l.get(1).add(end); // adauga indexul de final in lista corespunzatoare
			}
		}
		
		return l;
		
	}
	
	// Complexitate apromativ O(n) - n fiind numarul de caractere
	// Verifica daca un caracter dat este caracter "normal" - daca este litera (a-z, A-Z sau diacritice) ADICA SA NU FIE SEPARATOR
	// Returneaza true daca este caracter normal, altfel returneaza false
	// Se poate folosi asta in loc de versiunea de mai sus deoarece majoritatea textelor contine litere, nu separatori
	// Daca folosim verisunea de mai sus atunci se vor face k comparatii pentru fiecare caracter
	// k fiind numarul de separatori
	// E mai eficient asa deoarece intr-un text majoritatea caracterelor nu sunt separatori, in loc sa facem 6 verificari pe cate un caracter
	// mai bine pornim cu idea ca este caracter - si putem obtine rezultatul doar dupa o verificare
	// Intai se compara cu litere normale (a-z, A-Z), apoi cu diacritice
	public boolean isNormalChar(char a) {
		int ch = (int)a;
		if((ch >= 97 && ch <= 122) || (ch >= 65 && ch <= 90) ) // verifica daca e caracter normal
			return true;
		int n = DIACRITICE.length;
		for(int i=0;i<n;i++) // incearca si cu diacritice
			if(ch == DIACRITICE[i])
				return true;
		return false;
	}
	
	// Versiune in care verificam dupa caractere normale, nu dupa separatori
	// Cel mai probabil este mai eficient asa
	public List<List<Integer>> splitString2(String text){
		List<List<Integer>> l = new LinkedList<>();
		List<Integer> a = new LinkedList<>(); // tine indexul de la begin
		List<Integer> b = new LinkedList<>(); // tine indexul de la end
		l.add(a);
		l.add(b);
		
		int i = 0;
		int len = text.length();
		while(i < len) { // pana ajunge la capatul textului
			while(i < len && !isNormalChar(text.charAt(i))) // cand va gasi primul non-separator acolo incepe cuvantul
				i++;
			int begin = i;
			while(i < len && isNormalChar(text.charAt(i))) // cand timp nu mai gaseste separator inseamna ca suntem in cuvant
				i++;
			
			int end = i;
			if(begin != end) { // daca nu foloseam acest if erau probleme cand era separator la final - gasea interval de tipul [17,17]
			// am gasit un nou cuvant: text[begin,end]
				l.get(0).add(begin); // adauga indexul de inceput in lista corespunzatoare
				l.get(1).add(end); // adauga indexul de final in lista corespunzatoare
			}
		}
		
		return l;
		
	}
	
	public int[] getIndiciCuvant(String text, int pos) {
		int rez[] = new int[]{-1,-1};
		if(!isNormalChar(text.charAt(pos))) // daca nu suntem intr-un cuvant nu are rost sa mai cautam
			return rez;
		int left = pos;
		int right = pos;
		int len = text.length();
		while(left >=0 && isNormalChar(text.charAt(left))) // deplasare pana la capatul stang al cuvantului
			left--;
		while(right < len  && isNormalChar(text.charAt(right))) // deplasare pana la capatul drept al cuvantului
			right++;
		rez[0] = left+1;
		rez[1] = right;
		return rez;
		
	}
	
	
	// Primeste un text si verifica daca acel text este un cuvant normal
	// Adica contine doar litere mari si mici (deci fara separatori)
	// Se foloseste de isNormalChar
	// Returneaza true daca word este cuvant normal, altfel (daca contine cel putin un separator) returneaza false
	public boolean isNormalWord(String word) {
		int n = word.length();
		for(int i=0;i<n;i++)
			if(isNormalChar(word.charAt(i)) == false)
				return false;
		return true;
	}
	
	public static void main(String args[]) {
		SplitAlgs obj = new SplitAlgs();
		//System.out.println((int)'ă' + " " +  (int)'â' + " "  + (int)'î' + " " + (int)'ș' + " " + (int)'ț' + " " + (int)'Ș' + " " + (int)'Î' + " " + (int)'Ț' + " " + (int)'Ă' + " " + (int)'Â');
		
		//String a = "!!!! Acesată și cât mai multe condiții țîră ȘUT     este  \n un \n\n\ntext,,, ";
		String a  = "!!  Acesta este   un   text   \n\n .. cu multe  ??? cuvinte ";
		// Prima versiune de alg
		/*List<List<Integer>> l = obj.splitString(a);
		for(int i=0;i<l.get(0).size();i++) // l.get(0), l.get(1) au aceeasi lungime - fiecare begin are si end
			System.out.println(l.get(0).get(i) + " | " + l.get(1).get(i) + " : " + a.substring(l.get(0).get(i), l.get(1).get(i)));
		*/
		// A doua versiune de alg
		System.out.println("-------------------------------");
		List<List<Integer>> l1 = obj.splitString2(a);
		for(int i=0;i<l1.get(0).size();i++) // l.get(0), l.get(1) au aceeasi lungime - fiecare begin are si end
			System.out.println(l1.get(0).get(i) + " | " + l1.get(1).get(i) + " : " + a.substring(l1.get(0).get(i), l1.get(1).get(i)));
		
		int rez[] = obj.getIndiciCuvant(a, a.length()-2);
		System.out.println(rez[0] + " " + rez[1]);
		if(rez[0] != -1 && rez[1] != -1)
			System.out.println(a.substring(rez[0],rez[1]));
	}
	
}
