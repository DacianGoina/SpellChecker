package db;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
/**
 * 
 * @author Bica Anamaria
 *
 */
public class DB {
	static PreparedStatement st =null;
	static Connection conn = null;
	Statement statement = null;
	ResultSet rs = null;
	// String pt calea bazei de date
	//static String basePath ="C:/Users/anama/eclipse-workspace/Pb/SpellChecker/dictionar.db";
	//static String basePath ="C:\\Users\\Dacian\\git\\SpellChecker\\dictionar.db";
	static String basePath = "././dictionar.db";
	
	public DB() {
		
	}
	
	public DB(String path) {
		this.basePath = path;
	}
	
	public Connection connection(){
		Connection conn =null;
		String sql = "org.sqlite.JDBC";
		String path ="jdbc:sqlite:" + basePath;
		//string static
		//constructor clasa db cu String parametru, daca nu e cu cel harcodat deja + comentariu pt string
		try {
			Class.forName(sql);
			conn = DriverManager.getConnection(path);
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return conn;
		
		
	}
	
	//inserarea unui cuvant in baza de date
	// Clasa care sa tina toti membrii - parametrii, ex nume: wordObj,eventual data ca parametru separat
	public void insertCuvantNou(WordObj word) {
		
		Connection conn = this.connection();
		//adaugam id = null
		//word obj sa contina id
		String sql = "INSERT INTO DICTIONAR VALUES(?,?,?,?,?,?,?)";
		String cuv = word.getCuvant();
		String tip = word.getTip();
		int frecventa = word.getFrecventa();
		boolean activ = word.isActiv();
		boolean adaugat = word.isAdaugat();
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
        LocalDateTime now = LocalDateTime.now(); 
		String date1 = String.valueOf(now);
		try {
			
			st = conn.prepareStatement(sql);
			st.setString(2,cuv);
			st.setString(3, tip);
			st.setInt(4, frecventa);
			st.setBoolean(5, activ);
			st.setBoolean(6, adaugat);
			st.setString(7, date1);
			st.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public int getId(String cuvant) {
		
		Connection conn = this.connection();
		String sql = "Select id from dictionar where cuvant =?";
		int id = 0;
		try {
			
			st =  conn.prepareStatement(sql);
			st.setString(1, cuvant);
			rs = st.executeQuery();
			while(rs.next()) {
				id = rs.getInt("ID");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return id;
		
		
	}
	
	//cautare daca un anumit cuvant exista in baza de date
	public WordObj cautareCuvant(String cuvant) {
		
		Connection conn = this.connection();
		String sql = "Select * from dictionar where cuvant = ?"; 
		WordObj obj;
		// cautare directa in baza de date
		// return wordObj 
		// verificare dupa id
		// 2 fct una cu String, una cu wordObj, cautarea cu baza de date
		try {
			
			st =  conn.prepareStatement(sql);
			st.setString(1, cuvant);
			rs = st.executeQuery();
			while(rs.next()) {
				
				int id = rs.getInt("ID");
				String cuv = rs.getString("CUVANT");
				String tip = rs.getString("TIP");
				int frecventa = rs.getInt("FRECVENTA");
				boolean activ = rs.getBoolean("ACTIV");
				boolean adaugat = rs.getBoolean("ADAUGAT");
				String date = rs.getString("DATA_ADAUGARII");
				
				obj = new WordObj(id,cuv,tip,frecventa,activ,adaugat,date);
				conn.close();
				return obj;
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	public boolean gasireCuvant(WordObj obj) {
		
		Connection conn = this.connection();
		
		String sql = "Select * from dictionar where cuvant = ?";
		String cuvant = obj.getCuvant();
		
		try {
			
			st =  conn.prepareStatement(sql);
			st.setString(1, cuvant);
			rs = st.executeQuery();
			if(rs.next()) {
				conn.close();
				return true;
			}
			else {
				conn.close();
				return false;
			}
			
			
				
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
			
	}
	
	
	//cresterea frecventei unui cuvant daca acesta apare de mai multe ori
	public void crestereFrecventa(String cuvant) {
		
		
		String update ="UPDATE DICTIONAR SET FRECVENTA=? WHERE ID =?";
		
		WordObj obj = cautareCuvant(cuvant);
		
		Connection conn = this.connection();
		
		int frecv = obj.getFrecventa();
		int id = obj.getId();
		int frecv1 ;
		frecv1 = frecv + 1;
		
	
		try {
			
			// wordObj sa modific frecventa sau int frecventa ca parametru
			
		st =  conn.prepareStatement(update);
		st.setInt(1, frecv1);
		st.setInt(2, id);
		st.execute();
		st.close();
		conn.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	//stergere cuvant = punere flag_activ pe false
	// folosire functie pentru connection
	public void stergereCuvant(String cuvant) {
		
		
		String sql = "UPDATE DICTIONAR SET ACTIV=? WHERE ID =?";
		WordObj obj = cautareCuvant(cuvant);
		
		Connection conn = this.connection();
		int id = obj.getId(); // word object cu id 
		try {
																// de inlocuit cu WHERE ID = ?
														// in general operatii bazate pe ID
			st =  conn.prepareStatement(sql);
			st.setBoolean(1, false);
			st.setInt(2, id);
			st.execute();
			conn.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	// fct adaugare cuvinte, vector de wordObj in care adaug toate cuv, folosesc batch.update 
	// citire toata baza de date fct -> vector/ treemap 
	
	//punere flag_activ = true
	// connection si ID
	public void reAdaugareCuvant(String cuvant) {
		
		
		String sql = "UPDATE DICTIONAR SET ACTIV=? WHERE ID =?";
		WordObj obj = cautareCuvant(cuvant);
		
		Connection conn = this.connection();
		int id = obj.getId();
		
		try {
			
			st =  conn.prepareStatement(sql);
			st.setBoolean(1, true);
			st.setInt(2, id);
			st.execute();
			conn.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void adaugareCuvinte() {
		
		Connection conn = this.connection();
		Vector<WordObj> obj = new Vector<>();
		
	}
	
	public TreeMap<String, WordObj> getlistaCuvinte(){
		
		Connection conn = this.connection();
		
		TreeMap<String, WordObj> cuvinte = new TreeMap<String,WordObj>();
		
		String sql = "Select * from dictionar";
		
		try {
			
			statement = conn.createStatement();
			rs = statement.executeQuery(sql);
			while(rs.next()) {
				
				int id = rs.getInt("ID");
				String cuv = rs.getString("CUVANT");
				String tip = rs.getString("TIP");
				int frecventa = rs.getInt("FRECVENTA");
				boolean activ = rs.getBoolean("ACTIV");
				boolean adaugat = rs.getBoolean("ADAUGAT");
				String date = rs.getString("DATA_ADAUGARII");
				
				WordObj obj = new WordObj(id,cuv,tip,frecventa, activ,adaugat,date);
				if(obj.isActiv() == true) {
					cuvinte.put(cuv, obj);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return cuvinte;
		
	}

}
