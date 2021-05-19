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
	public void insertCuvantNou(WordObj word) throws SQLException {
		
		Connection conn = this.connection();
		
		String sql = "INSERT INTO DICTIONAR(CUVANT,TIP,FRECVENTA,ACTIV,ADAUGAT,DATA_ADAUGARII) VALUES(?,?,?,?,?,?)";
		
		
		final LocalDateTime dt = LocalDateTime.now(); 
		final java.sql.Date sqlDate = java.sql.Date.valueOf(dt.toLocalDate());
		
			
		st = conn.prepareStatement(sql);
		st.setString(1,word.getCuvant());
		st.setString(2, word.getTip());
		st.setInt(3, word.getFrecventa());
		st.setBoolean(4, word.isActiv());
		st.setBoolean(5, word.isAdaugat());
		st.setDate(6, sqlDate);
		st.executeUpdate();
			
		
		conn.close();
		
		
		
	}
	
	public int getId(String cuvant) throws SQLException {
		
		Connection conn = this.connection();
		String sql = "Select id from dictionar where cuvant =?";
		int id = 0;
		try {
			
			st =  conn.prepareStatement(sql);
			st.setString(1, cuvant);
			rs = st.executeQuery();
			if(rs.next()) {
				id = rs.getInt("ID");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		conn.close();
		return id;
		
		
	}
	
	//cautare daca un anumit cuvant exista in baza de date
	public WordObj cautareCuvant(String cuvant) throws SQLException {
		
		Connection conn = this.connection();
		String sql = "Select * from dictionar where cuvant = ?"; 
		WordObj obj = null;
		
	
			
		st =  conn.prepareStatement(sql);
		st.setString(1, cuvant);
		rs = st.executeQuery();
		if(rs.next()) {
				
			int id = rs.getInt("ID");
			String cuv = rs.getString("CUVANT");
			String tip = rs.getString("TIP");
			int frecventa = rs.getInt("FRECVENTA");
			boolean activ = rs.getBoolean("ACTIV");
			boolean adaugat = rs.getBoolean("ADAUGAT");
			Date date = rs.getDate("DATA_ADAUGARII");
				
			obj = new WordObj(id,cuv,tip,frecventa,activ,adaugat,date);
				
			}
		
		conn.close();
		return obj;
			
		
		
		
		
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
	public void crestereFrecventa(String cuvant) throws SQLException {
		
		
		String update ="UPDATE DICTIONAR SET FRECVENTA=? WHERE CUVANT =?";
		
		WordObj obj = cautareCuvant(cuvant);
		
		Connection conn = this.connection();
		
		int frecv = obj.getFrecventa();
		int id = obj.getId();
		int frecv1 ;
		frecv1 = frecv + 1;
		
		st =  conn.prepareStatement(update);
		st.setInt(1, frecv1);
		st.setInt(2, id);
		st.execute();
		st.close();
		
		conn.close();
		
		
		
	}
	
	//stergere cuvant = punere flag_activ pe false
	public void stergereCuvant(final int idCuvant) throws SQLException {
		
		
		String sql = "UPDATE DICTIONAR SET ACTIV=? WHERE ID =?";
		
		Connection conn = this.connection();
		
														
		st =  conn.prepareStatement(sql);
		st.setBoolean(1, false);
		st.setInt(2, idCuvant);
		st.execute();
		
		
		conn.close();
		
		
	}
	
	// fct adaugare cuvinte, vector de wordObj in care adaug toate cuv, folosesc batch.update 
	// citire toata baza de date fct -> vector/ treemap 
	
	//punere flag_activ = true
	public void reAdaugareCuvant(final int idCuvant) throws SQLException {
		
		
		String sql = "UPDATE DICTIONAR SET ACTIV=? WHERE ID =?";
		
		Connection conn = this.connection();
			
		st =  conn.prepareStatement(sql);
		st.setBoolean(1, true);
		st.setInt(2, idCuvant);
		st.execute();
			
		conn.close();
	}
	
	public void adaugareCuvinte(final Vector<WordObj> vCuvinte) {

		final Connection conn = this.connection();

		/*final String sql = ...; // vezi insertCuvantNou()
		final PreparedStatement stmt = conn.prepareStatement(sql);

		for(final WordObj word : vWords) {

			if(word.ID == null) {

				// cuvantul e nou: inserezi cuvantul nou;
			} else {

				// TODO: ne gandim in ce conditii apare aceasta situatie;
			}
		}*/


		
	}
	
public TreeMap<String, WordObj> getlistaCuvinte(){
		
		Connection conn = this.connection();
		
		TreeMap<String, WordObj> cuvinte = new TreeMap<String,WordObj>();
		
		String sql = "Select * from dictionar where activ = TRUE";
		
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
				Date date = rs.getDate("DATA_ADAUGARII");
				
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
