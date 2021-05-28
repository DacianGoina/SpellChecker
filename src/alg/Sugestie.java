package alg;

/**
 * 
 * @author darius
 *
 */
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

import db.WordObj;
import db.WordObj_rev;

public class Sugestie {
	public static List<ObjSugestie> getCorrectionSuggestions(String word,TreeMap<String,WordObj>dict){
		List<ObjSugestie> rez = new LinkedList<>();
		
		String floor1 = dict.lowerKey(word);
		String floor2 = dict.lowerKey(floor1);
		String floor3 = dict.lowerKey(floor1);
		String ceil1 = dict.higherKey(word);
		String ceil2 = dict.higherKey(ceil1);
		String ceil3 = dict.higherKey(ceil2);
		while(ceil3.length()>=2&&word.codePointAt(0)==ceil3.codePointAt(0))
		{
			if((ceil3 != null)&&(Math.abs(ceil3.length()-word.length())<3))
			{
			ObjSugestie objSugestieCeil3=new ObjSugestie(Algoritmi.distanta(ceil3,word).IDistantaDeEditare,ceil3,dict.get(ceil3).getFrecventa());
			rez.add(objSugestieCeil3);
			}
			ceil3=dict.higherKey(ceil3);
		}
		while(floor3.length()>=2&& word.codePointAt(0)==floor3.codePointAt(0))
		{
			if((floor3 != null)&&(Math.abs(floor3.length()-word.length())<3))
			{
			ObjSugestie objSugestiefloor3=new ObjSugestie(Algoritmi.distanta(floor3,word).IDistantaDeEditare,floor3,dict.get(floor3).getFrecventa());
			rez.add(objSugestiefloor3);
			}
			floor3=dict.lowerKey(floor3);
		}
		if(floor1 != null)
		{	ObjSugestie objSugestiefloor1=new ObjSugestie(Algoritmi.distanta(floor1,word).IDistantaDeEditare,floor1,dict.get(floor1).getFrecventa());
		
			rez.add(objSugestiefloor1);
			
		}
			else
			rez.add(null);
		if(floor2 != null)
			{
			ObjSugestie objSugestiefloor2=new ObjSugestie(Algoritmi.distanta(floor2,word).IDistantaDeEditare,floor2,dict.get(floor2).getFrecventa());
			rez.add(objSugestiefloor2);			
			}
		else
			rez.add(null);
		
		if(ceil1 != null)
			{
			ObjSugestie objSugestieCeil1=new ObjSugestie(Algoritmi.distanta(ceil1,word).IDistantaDeEditare,ceil1,dict.get(ceil1).getFrecventa());
			rez.add(objSugestieCeil1);
			}
			
		else
			rez.add(null);
		
		if(ceil2 != null)
			{
			
			ObjSugestie objSugestieCeil2=new ObjSugestie(Algoritmi.distanta(ceil2,word).IDistantaDeEditare,ceil2,dict.get(ceil2).getFrecventa());
			rez.add(objSugestieCeil2);
			}
		else
			rez.add(null);
		//Collections.sort(rez);
		
		return rez;
	}
	public static List<ObjSugestie> getCorrectionSuggestions1(String word,TreeMap<String,WordObj_rev>dict){
		List<ObjSugestie> rez = new LinkedList<>();
		
		String floor1 = dict.lowerKey(word);
		String floor2 = dict.lowerKey(floor1);
		String floor3 = dict.lowerKey(floor1);
		String ceil1 = dict.higherKey(word);
		String ceil2 = dict.higherKey(ceil1);
		String ceil3 = dict.higherKey(ceil2);
		while(ceil3.length()>=2&&word.codePointAt(0)==ceil3.codePointAt(0))
		{
			if((ceil3 != null)&&(Math.abs(ceil3.length()-word.length())<3))
			{
			ObjSugestie objSugestieCeil3=new ObjSugestie(Algoritmi.distanta(ceil3,word).IDistantaDeEditare,reverseIt(ceil3),dict.get(ceil3).getFrecventa());
			rez.add(objSugestieCeil3);
			}
			ceil3=dict.higherKey(ceil3);
		}
		while(floor3.length()>=2&& word.codePointAt(0)==floor3.codePointAt(0))
		{
			if((floor3 != null)&&(Math.abs(floor3.length()-word.length())<3))
			{
			ObjSugestie objSugestiefloor3=new ObjSugestie(Algoritmi.distanta(floor3,word).IDistantaDeEditare,reverseIt(floor3),dict.get(floor3).getFrecventa());
			rez.add(objSugestiefloor3);
			}
			floor3=dict.lowerKey(floor3);
		}
		if(floor1 != null)
		{	ObjSugestie objSugestiefloor1=new ObjSugestie(Algoritmi.distanta(floor1,word).IDistantaDeEditare,reverseIt(floor1),dict.get(floor1).getFrecventa());
		
			rez.add(objSugestiefloor1);
			
		}
			else
			rez.add(null);
		if(floor2 != null)
			{
			ObjSugestie objSugestiefloor2=new ObjSugestie(Algoritmi.distanta(floor2,word).IDistantaDeEditare,reverseIt(floor2),dict.get(floor2).getFrecventa());
			rez.add(objSugestiefloor2);			
			}
		else
			rez.add(null);
		
		if(ceil1 != null)
			{
			ObjSugestie objSugestieCeil1=new ObjSugestie(Algoritmi.distanta(ceil1,word).IDistantaDeEditare,reverseIt(ceil1),dict.get(ceil1).getFrecventa());
			rez.add(objSugestieCeil1);
			}
			
		else
			rez.add(null);
		
		if(ceil2 != null)
			{
			
			ObjSugestie objSugestieCeil2=new ObjSugestie(Algoritmi.distanta(ceil2,word).IDistantaDeEditare,reverseIt(ceil2),dict.get(ceil2).getFrecventa());
			rez.add(objSugestieCeil2);
			}
		else
			rez.add(null);
		//Collections.sort(rez);
		
		return rez;
	}
	public static String reverseIt(String source) {
        int i, len = source.length();
        StringBuilder dest = new StringBuilder(len);

        for (i = (len - 1); i >= 0; i--){
            dest.append(source.charAt(i));
        }

        return dest.toString();
    }
	
}