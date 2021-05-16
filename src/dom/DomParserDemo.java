package dom;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class DomParserDemo {
	
	private static void writeBuffered(List<String> records, int bufSize) throws IOException {
	    File file = new File("E:/Proiect_Colectiv/.metadata/Cuvinte_Temp.txt");
	    try {
	        FileWriter writer = new FileWriter(file);
	        BufferedWriter bufferedWriter = new BufferedWriter(writer, bufSize);
	        String absolutePath = file.getAbsolutePath();
	        System.out.println(absolutePath);
	        System.out.print("Writing buffered (buffer size: " + bufSize + ")... ");
	        write(records, bufferedWriter);
	    } finally {
	    }
	}

	private static void write(List<String> records, Writer writer) throws IOException {
	    long start = System.currentTimeMillis();
	    for (String record: records) {
	        writer.write(record);
	    }
	    writer.close(); 
	    long end = System.currentTimeMillis();
	    System.out.println((end - start) / 1000f + " seconds");
	}
   public static void main(String[] args) {

      try {
         File inputFile = new File("E:/Proiect_Colectiv/.metadata/rowiktionary-latest-pages-articles.xml");
         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
         Document doc = dBuilder.parse(inputFile);
         doc.getDocumentElement().normalize();
         NodeList nList = doc.getElementsByTagName("page");
         System.out.println("----------------------------");
         List<String> records = new ArrayList<String>();
         
         for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
           // Files.writeString(fileName,"\nCurrent Element :" + nNode.getNodeName());
            
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
               Element eElement = (Element) nNode;
               records.add( eElement
                  .getElementsByTagName("title")
                  .item(0)
                  .getTextContent());
               Map<String, Integer> freqMap=new HashMap<>();
               for(String record : records)
               {
            	   record=record.replaceAll(":"," ");
            	   String[] cuv= record.split(" ");
            	   for(int i=0;i<cuv.length;i++)
            	   {
            		   if(!freqMap.containsKey(cuv[i]))
            		   {
            			   freqMap.put(cuv[i], 1);
            		   }
            	   }
            	   
               }
               
               BufferedWriter bf = null;
               
               try
               {
            	   bf = new BufferedWriter(new FileWriter("E:/Proiect_Colectiv/.metadata/Cuvinte_Temp.txt"));
                   for (Map.Entry<String, Integer> entry :
                        freqMap.entrySet()) {

                       bf.write(entry.getKey());
                       bf.newLine();
               }
               }
               catch (IOException e) {
                   e.printStackTrace();
               }
            }
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
      finally {
    	  
          try {

          }
          catch (Exception e) {
          }
      }
      
   }
   
}
