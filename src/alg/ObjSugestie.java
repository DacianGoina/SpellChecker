package alg;

/**
 * 
 * @author darius
 *
 */
import java.util.Comparator;

public  class ObjSugestie implements Comparable<ObjSugestie> {
private int levDistance;
private String word;
private int frequency;

public ObjSugestie(int levDistance, String word, int frequency) {
	super();
	this.levDistance = levDistance;
	this.word = word;
	this.frequency = frequency;
}
public int getLevDistance() {
	return levDistance;
}
public void setLevDistance(int levDistance) {
	this.levDistance = levDistance;
}
public String getWord() {
	return word;
}
public void setWord(String word) {
	this.word = word;
}
public int getFrequency() {
	return frequency;
}
public void setFrequency(int frequency) {
	this.frequency = frequency;
}
public int compareTo(ObjSugestie o) {
	if(this.levDistance==o.levDistance)
		if(this.frequency==o.frequency)
			return this.word.compareTo(o.word);
		else
		return this.frequency-o.frequency;
	else
		return this.levDistance-o.levDistance;
}

}
