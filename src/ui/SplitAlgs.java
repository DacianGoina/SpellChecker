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
	
	// Primeste un char, verifica daca este separator
	// Daca este separator returneaza false, altfel returneaza true
	public boolean isSeparator(char a) {
		int n = separatori.length;
		for(int i=0;i<n;i++)
			if(a == separatori[i])
				return true;
		return false;
	}
	
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
	
	public static void main(String args[]) {
		String a = "!!!! Acesta     este  \n un \n\n\ntext,,, ";
		SplitAlgs obj = new SplitAlgs();
		List<List<Integer>> l = obj.splitString(a);
		for(int i=0;i<l.get(0).size();i++) // l.get(0), l.get(1) au aceeasi lungime - fiecare begin are si end
			System.out.println(l.get(0).get(i) + " | " + l.get(1).get(i) + " : " + a.substring(l.get(0).get(i), l.get(1).get(i)));
		
	}
	
}
