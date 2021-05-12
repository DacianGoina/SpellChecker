package alg;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.text.similarity.LevenshteinDistance;

import alg.Distanta;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;

public class test {
	public static int minim(int a, int b, int c) {
		if (a <= b && a <= c)
			return a;
		if (b <= c && b <= a)
			return b;
		return c;
	}

	public static boolean sufix(String sText1, String sText2) {
		// sText1 este cuvantul,iar stext2 este potentialul sufix;
		if (sText2.length() <= sText1.length()) {
			int pozitie = sText1.length() - sText2.length();
			String sText3 = "";
			for (int i = pozitie; i < sText1.length(); i++)
				sText3 = sText3 + sText1.charAt(i);
			Distanta z = distanta(sText3, sText2);
			if (z.getIDistantaDeEditare() == 0)
				return true;
		}
		return false;
	}

	public static boolean prefix(String sText1, String sText2) {
		// sText1 este cuvantul,iar stext2 este potentialul prefix;
		if (sText2.length() <= sText1.length()) {
			int pozitie = sText2.length();
			String sText3 = "";
			for (int i = 0; i < pozitie; i++)
				sText3 = sText3 + sText1.charAt(i);
			Distanta z = distanta(sText3, sText2);
			if (z.getIDistantaDeEditare() == 0)
				return true;
		}
		return false;

	}

	public static boolean verific(String lit1, String lit2) {
		if ((lit1.equals("a") && lit2.equals("ă")) || (lit1.equals("ă") && lit2.equals("a")))
			return true;
		if ((lit1.equals("a") && lit2.equals("â")) || (lit1.equals("â") && lit2.equals("a")))
			return true;
		if ((lit1.equals("î") && lit2.equals("i")) || (lit1.equals("i") && lit2.equals("î")))
			return true;
		if ((lit1.equals("s") && lit2.equals("ș")) || (lit1.equals("ș") && lit2.equals("s")))
			return true;
		if ((lit1.equals("t") && lit2.equals("ț")) || (lit1.equals("ț") && lit2.equals("t")))
			return true;

		return false;
	}

	public static int aparitie(String sText1, int nr, int litera) {
		int j, nr1 = 0;

		for (j = 0; j < nr; j++)
			if (sText1.codePointAt(j) == litera)
				nr1++;
		return nr1;
	}

	public static Distanta distanta(String sText1, String sText2) {
		sText1 = "\0" + sText1;
		sText2 = "\0" + sText2;
		ArrayList<Integer> v1 = new ArrayList<Integer>();
		ArrayList<Integer> v2 = new ArrayList<Integer>();
		Distanta d = new Distanta(0, 0);
		int k = 0;
		int i, ok = 0, j;
		for (i = 0; i < sText1.length(); i++) {
			v2.add(0);
			v1.add(k);
			k++;
		}
		for (i = 1; i < sText2.length(); i++) {
			for (j = 0; j < sText1.length(); j++) {
				if (j == 0) {
					int a = v1.get(j) + 1;
					v2.set(j, a);
				} else if (sText2.codePointAt(i) == sText1.codePointAt(j)
						&& (aparitie(sText2, i, sText2.codePointAt(i)) == aparitie(sText1, j, sText1.codePointAt(j)))) {
					v2.set(j, minim(v2.get(j - 1), v1.get(j), v1.get(j - 1)));

				}

				else {
					if (isDiacritics(sText2.codePointAt(i), sText1.codePointAt(j)) == true) {
						System.out.print(isDiacritics(sText2.codePointAt(i), sText1.codePointAt(j)));
						d.addDiactritice();
						int a = minim(v2.get(j - 1), v1.get(j), v1.get(j - 1));
						v2.set(j, a);
					} else {
						int a = minim(v2.get(j - 1), v1.get(j), v1.get(j - 1)) + 1;
						v2.set(j, a);
					}
				}
			}
			v1.clear();

			v1.addAll(v2);
			System.out.println(v1);
		}
		d.setIDistantaDeEditare(v1.get(v1.size() - 1));
		return d;
	}

	private static Integer min(Integer integer, Integer integer2) {
		if (integer < integer2)
			return integer;
		else
			return integer2;
	}

	public static boolean isDiacritics(final int chB, final int chS) {
		if ((chB == 'a' || chB == 'A')
				&& (chS == '\u0103' || chS == '\u0102' || chS == '\u00e2' || chS == '\u00c2' || chS == '\u01CE')) {
			return true;
		} else if ((chB == 's' || chB == 'S')
				&& (chS == '\u015f' || chS == '\u015e' || chS == '\u0219' || chS == '\u0218')) {
			return true;
		} else if ((chB == 't' || chB == 'T')
				&& (chS == '\u0163' || chS == '\u0162' || chS == '\u021b' || chS == '\u021a')) {
			return true;
		} else if ((chB == 'i' || chB == 'I') && (chS == '\u00ce' || chS == '\u00ee')) {
			return true;
		} else if ((chS == 'a' || chS == 'A') && (chB == '\u0103' || chB == '\u0102')) {
			return true;
		} else if ((chS == 'a' || chS == 'A') && (chB == '\u00e2' || chB == '\u00c2' || chB == '\u01CE')) {
			return true;
		} else if ((chS == 's' || chS == 'S') && (chB == '\u015f' || chB == '\u015e')) {
			return true;
		} else if ((chS == 's' || chS == 'S') && (chB == '\u0219' || chB == '\u0218')) {
			return true;
		} else if ((chS == 't' || chS == 'T')
				&& (chB == '\u0163' || chB == '\u0162' || chB == '\u021b' || chB == '\u021a')) {
			return true;
		} else if ((chS == 'i' || chS == 'I') && (chB == '\u00ce' || chB == '\u00ee')) {
			return true;
		}
		return false;
	}

	public static void ReadTest() {
		List words = new ArrayList<String>();
		// stocam si accesam
		try {
			File read = new File("test.txt");
			Scanner myReader = new Scanner(read);
			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();
				String[] Cuvinte = data.split(" ");
				for (String a : Cuvinte)
					words.add(data);
			}
			myReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		for (int i = 0; i < words.size(); i++) {
			System.out.println(words.get(i));
		}
		DistanceTest(words);
	}

	public static void DistanceTest(List<String> words) {
		// TODO Auto-generated method stub
		int x = 0;
		final LevenshteinDistance ldist = new LevenshteinDistance();
		Distanta distanta = new Distanta(0, 0);
		for (int i = 0; i < words.size() - 1; i++) {
			distanta = new Distanta(0, 0);
			distanta = distanta(words.get(i), words.get(i + 1));
			if (distanta.getIDistantaDeEditare() != ldist.apply(words.get(i), words.get(i + 1))) {
				x++;
				System.out.println(words.get(i) + " " + words.get(i + 1));
				System.out.println(
						distanta(words.get(i), words.get(i + 1)) + " " + ldist.apply(words.get(i), words.get(i + 1)));
			}
		}
		System.out.println(x);
	}

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		System.out.println("Introduceti primul cuvant");
		String text;
		text = scan.nextLine();
		System.out.println("Introduceti cel de al doilea cuvant cuvant");
		String text2;
		text2 = scan.nextLine();
		Distanta z = distanta(text, text2);
		System.out.println(z);
		System.out.println(z.getIDistantaDeEditare());
		final LevenshteinDistance ldist = new LevenshteinDistance();
		System.out.println(ldist.apply(text, text2));
		// ReadTest();
		// System.out.print(prefix(text, text2));
		// System.out.print(sufix(text, text2));

	}

}