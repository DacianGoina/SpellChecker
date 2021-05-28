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

import db.WordObj;

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
	
	Connection conn = null;
	Statement statement = null;
	// String pt calea bazei de date
	static String basePath ="././database.db";
	
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
	
	public void createTable() {
		Connection conn =this.connection();
		String sql = "CREATE TABLE IF NOT EXISTS Dictionar(\n"
                + "	ID integer PRIMARY KEY AUTOINCREMENT,\n"
                + "	CUVANT text NOT NULL,\n"
                + "	TIP text,\n"
                + "	FRECVENTA integer,\n"
                + "	ACTIV numeric,\n"
                + "	ADAUGAT numeric,\n"
                + "	DATA_ADAUGARII Date\n"
                + ");";
		try {
			statement = conn.createStatement();
			statement.execute(sql);
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
	
	// inserarea unui cuvant in baza de date
	public void insertCuvantNou(WordObj word) {
		
		Connection conn = this.connection();
		
		String sql = "INSERT INTO DICTIONAR(CUVANT,TIP,FRECVENTA,ACTIV,ADAUGAT,DATA_ADAUGARII) VALUES(?,?,?,?,?,?)";
		
		PreparedStatement st = null;
		
		String cap ;
		if(Character.isLowerCase(word.getCuvant().charAt(0))) {
			 cap = word.getCuvant().substring(0, 1).toUpperCase() + word.getCuvant().substring(1);
		}
		else {
		    cap = word.getCuvant().substring(0, 1).toLowerCase() + word.getCuvant().substring(1);
		}
		
		WordObj w = new WordObj(cap);
		
		final LocalDateTime dt = LocalDateTime.now(); 
		final java.sql.Date sqlDate = java.sql.Date.valueOf(dt.toLocalDate());
		//String date1 = String.valueOf(sqlDate);
		if(gasireCuvant(word) == false) {
		try {
			st = conn.prepareStatement(sql);
			st.setString(1,word.getCuvant());
			st.setString(2, word.getTip());
			st.setInt(3, word.getFrecventa());
			st.setBoolean(4, word.isActiv());
			st.setBoolean(5, word.isAdaugat());
			st.setDate(6, sqlDate);
			st.executeUpdate();	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		else {
			WordObj word1 = cautareCuvant(word.getCuvant());
			crestereFrecventa(word1.getId(),word1.getFrecventa());
			reAdaugareCuvant(word.getId());
		}
		

		if(gasireCuvant(w) == false){
			try {
			st.setString(1, w.getCuvant());
			st.setString(2, w.getTip());
			st.setInt(3, w.getFrecventa());
			st.setBoolean(4, w.isActiv());
			st.setBoolean(5, w.isAdaugat());
			st.setDate(6, sqlDate);
			st.executeUpdate();
			
		}
	 catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		}
	else {
		WordObj word1 = cautareCuvant(w.getCuvant());
		crestereFrecventa(word1.getId(),word1.getFrecventa());
		reAdaugareCuvant(word.getId());
	}
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public void adaugare(String cuvant) {
		
		WordObj word = new WordObj(cuvant);
		
		Connection conn = this.connection();
		
		String sql = "INSERT INTO DICTIONAR(CUVANT,TIP,FRECVENTA,ACTIV,ADAUGAT,DATA_ADAUGARII) VALUES(?,?,?,?,?,?)";
		PreparedStatement st = null;
	
		String cap ;
		if(Character.isLowerCase(cuvant.charAt(0))) {
			 cap = cuvant.substring(0, 1).toUpperCase() + cuvant.substring(1);
		}
		else {
		    cap = cuvant.substring(0, 1).toLowerCase() + cuvant.substring(1);
		}
		
		WordObj w = new WordObj(cap);
		
		final LocalDateTime dt = LocalDateTime.now(); 
		final java.sql.Date sqlDate = java.sql.Date.valueOf(dt.toLocalDate());
		//String date1 = String.valueOf(sqlDate);
		if(gasireCuvant(word) == false) {
		try {
			
			st = conn.prepareStatement(sql);
			st.setString(1,word.getCuvant());
			st.setString(2, word.getTip());
			st.setInt(3, word.getFrecventa());
			st.setBoolean(4, word.isActiv());
			st.setBoolean(5, word.isAdaugat());
			st.setDate(6, sqlDate);
			st.executeUpdate();
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		else {
			WordObj word1 = cautareCuvant(word.getCuvant());
			crestereFrecventa(word1.getId(),word1.getFrecventa());
			reAdaugareCuvant(word.getId());
		}
		
		if(gasireCuvant(w) == false){
			try {
			st.setString(1, w.getCuvant());
			st.setString(2, w.getTip());
			st.setInt(3, w.getFrecventa());
			st.setBoolean(4, w.isActiv());
			st.setBoolean(5, w.isAdaugat());
			st.setDate(6, sqlDate);
			st.executeUpdate();
			
		}
	 catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		}
	else {
		WordObj word1 = cautareCuvant(w.getCuvant());
		crestereFrecventa(word1.getId(),word1.getFrecventa());
		reAdaugareCuvant(word.getId());
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
		PreparedStatement st = null;
		ResultSet rs = null;
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
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return id;
	}
	
	// cautare daca un anumit cuvant exista in baza de date
	public WordObj cautareCuvant(String cuvant) {
		
		Connection conn = this.connection();
		String sql = "Select * from dictionar where cuvant = ?"; 
		WordObj obj = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
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
		return obj;
		
	}
	
	public boolean gasireCuvant(WordObj obj) {
		
		Connection conn = this.connection();
		
		String sql = "Select * from dictionar where cuvant = ?";
		if(cautareCuvant(obj.getCuvant()) != null ) {
			
		
		String cuvant = obj.getCuvant() ;
		
		System.out.print(cuvant+"+++");
		
		try {
			PreparedStatement st =  conn.prepareStatement(sql);
			st.setString(1, cuvant);
			ResultSet rs = st.executeQuery();
			if(rs.next()) {
				conn.close();
				return true;
			}
			
				
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		else {
		return false;
		}
		return false;
			
	}
	
	
	//cresterea frecventei unui cuvant daca acesta apare de mai multe ori
	public void crestereFrecventa(final int idCuvant, int frecv) {
		
		String update ="UPDATE DICTIONAR SET FRECVENTA=? WHERE ID =?";
		
		Connection conn = this.connection();
		
		int frecv1 ;
		frecv1 = frecv + 1;
	
		try {	
		PreparedStatement st =  conn.prepareStatement(update);
		st.setInt(1, frecv1);
		st.setInt(2, idCuvant);
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
	public void stergereCuvant(final int idCuvant) {
		
		String sql = "UPDATE DICTIONAR SET ACTIV=? WHERE ID =?";
	
		Connection conn = this.connection();
		PreparedStatement st = null;
		try {
			st =  conn.prepareStatement(sql);
			st.setBoolean(1, false);
			st.setInt(2, idCuvant);
			st.execute();
		} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
		}
		
		try {
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
	public void reAdaugareCuvant(final int idCuvant) {
		String sql = "UPDATE DICTIONAR SET ACTIV=? WHERE ID =?";
		
		Connection conn = this.connection();
		PreparedStatement st = null;
		try {
			st =  conn.prepareStatement(sql);
			st.setBoolean(1, true);
			st.setInt(2, idCuvant);
			st.execute();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void adaugareCuvinteLiteraMare(final Vector<WordObj> vCuvinte) {

			final Connection conn = this.connection();
			final String sql = "INSERT INTO DICTIONAR(CUVANT,TIP,FRECVENTA,ACTIV,ADAUGAT,DATA_ADAUGARII) VALUES(?,?,?,?,?,?)";
			int counter = 0;
			try {
			final PreparedStatement stmt = conn.prepareStatement(sql);
		
			for(final WordObj word : vCuvinte) {
				
				String cap ;
				if(Character.isLowerCase(word.getCuvant().charAt(0))) {
					 cap = word.getCuvant().substring(0, 1).toUpperCase() + word.getCuvant().substring(1);
				}
				else {
				    cap = word.getCuvant().substring(0, 1).toLowerCase() + word.getCuvant().substring(1);
				}
				
				word.setCuvant(cap);

				if(gasireCuvant(word) == false) {
					// cuvantul e nou: inserezi cuvantul nou;
					final LocalDateTime dt = LocalDateTime.now(); 
					final java.sql.Date sqlDate = java.sql.Date.valueOf(dt.toLocalDate());
					String date = String.valueOf(sqlDate);
					
					
					
					try {
						//counter = 1000 executeBatch
						stmt.setString(1,word.getCuvant());
						stmt.setString(2, word.getTip());
						stmt.setInt(3, word.getFrecventa());
						stmt.setBoolean(4, word.isActiv());
						stmt.setBoolean(5, word.isAdaugat());
						stmt.setDate(6, sqlDate);
						stmt.addBatch();
						counter++;
						if(counter%1000==0) {
							stmt.executeBatch();
							counter = 0;
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					if(gasireCuvant(word) == true) {
						crestereFrecventa(word.getId(),word.getFrecventa());
					}
				}
				stmt.executeBatch();
				
			}
			}catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			//commit si inchis conexiunea
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	
	public void adaugareCuvinte(final Vector<WordObj> vCuvinte) {

		final Connection conn = this.connection();
		final String sql = "INSERT INTO DICTIONAR(CUVANT,TIP,FRECVENTA,ACTIV,ADAUGAT,DATA_ADAUGARII) VALUES(?,?,?,?,?,?)";
		int counter = 0;
		try {
		final PreparedStatement stmt = conn.prepareStatement(sql);
	
		for(final WordObj word : vCuvinte) {

			if(gasireCuvant(word) == false) {
				// cuvantul e nou: inserezi cuvantul nou;
				final LocalDateTime dt = LocalDateTime.now(); 
				final java.sql.Date sqlDate = java.sql.Date.valueOf(dt.toLocalDate());
				String date = String.valueOf(sqlDate);
				
				try {
					//counter = 1000 executeBatch
					stmt.setString(1,word.getCuvant());
					stmt.setString(2, word.getTip());
					stmt.setInt(3, word.getFrecventa());
					stmt.setBoolean(4, word.isActiv());
					stmt.setBoolean(5, word.isAdaugat());
					stmt.setDate(6, sqlDate);
					stmt.addBatch();
					counter++;
					if(counter%1000==0) {
						stmt.executeBatch();
						counter = 0;
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				if(gasireCuvant(word) == true) {
					crestereFrecventa(word.getId(),word.getFrecventa());
				}
			}
			stmt.executeBatch();
			
		}
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//commit si inchis conexiunea
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
}
	
	public TreeMap<String, WordObj> getlistaCuvinte(){
		
		Connection conn = this.connection();
		
		TreeMap<String, WordObj> cuvinte = new TreeMap<String,WordObj>();
		
		String sql = "Select * from dictionar where activ = true";
		ResultSet rs = null;
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
		
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return cuvinte;
		
	}

}
