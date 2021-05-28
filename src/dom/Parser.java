package dom;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
/**
 * 
 * @author Nicolaescu David
 *
 */

public class Parser {

   public static void main(String[] args) {

      try {
         File inputFile = new File("E:/Proiect_Colectiv/.metadata/rowiktionary-latest-pages-articles (2).xml");
         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
         Document doc = dBuilder.parse(inputFile);
         doc.getDocumentElement().normalize();
         NodeList nList = doc.getElementsByTagName("page");
         System.out.println("----------------------------");
         ArrayList<String> source = new ArrayList<String>();
         for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
               Element eElement = (Element) nNode;
               String temp2=eElement
                       .getElementsByTagName("title")
                       .item(0)
                       .getTextContent();
               if(temp2.indexOf(":")==-1)
            	   {
            	   		temp2=temp2.replaceAll("[^a-zA-Z//ă//î//â//ș//ț//Ă//Î//Â//Ș//Ț]", ";");
            	   		if(temp2.indexOf(";")==-1)
            	   			source.add(temp2);
            	   }
            }
         }
         Collections.sort(source);
         Writer out = new BufferedWriter(new OutputStreamWriter(
        		    new FileOutputStream("E:/Proiect_Colectiv/.metadata/Test.txt"), "UTF-8"));
         int count=source.size();
         for(int i=0;i<count;i++)
         {
        	 out.write(source.get(i));
        	 out.write('\n');
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
   }
}
