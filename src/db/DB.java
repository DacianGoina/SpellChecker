package db;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DB {
	static PreparedStatement st =null;
	static Connection conn = null;
	Statement statement = null;
	ResultSet rs = null;
	
	public Connection connection(){
		Connection conn =null;
		String sql = "org.sqlite.JDBC";
		String path ="jdbc:sqlite:C:/Users/anama/eclipse-workspace/Pb/SpellChecker/dictionar.db";
		
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
		return id;
		
		
	}
	
	//cautare daca un anumit cuvant exista in baza de date
	public boolean cautareCuvant(String cuvant) {
		
		Connection conn = this.connection();
		String sql = "Select cuvant from dictionar";
		try {
			
			statement = conn.createStatement();
			rs = statement.executeQuery(sql);
			while(rs.next()) {
				String cuv = rs.getString("CUVANT");
				if(cuv.equals(cuvant)) {
					return true;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
		
	}
	
	//cresterea frecventei unui cuvant daca acesta apare de mai multe ori
	public void crestereFrecventa(String cuvant) {
		
		Connection conn = this.connection();
		String sql = "Select cuvant,frecventa from dictionar";
		int frecv1 = 0;
	
		try {
			
			statement = conn.createStatement();
			rs = statement.executeQuery(sql);
			while(rs.next()) {
				String cuv = rs.getString("CUVANT");
				int frecv = rs.getInt("FRECVENTA");
				if(cuv.equals(cuvant)) {
					frecv1 = frecv;
				}
			}
			frecv1= frecv1+1;
			int id = getId(cuvant);
			String update ="UPDATE DICTIONAR SET FRECVENTA=? WHERE ID =?";
			
			st =  conn.prepareStatement(update);
			st.setInt(1, frecv1);
			st.setInt(2, id);
			st.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	//stergere cuvant = punere flag_activ pe false
	// folosire functie pentru connection
	public void stergereCuvant(String cuvant) {
		
		Connection conn = this.connection();
		String sql = "UPDATE DICTIONAR SET ACTIV=? WHERE ID =?";
		
		try {
																// de inlocuit cu WHERE ID = ?
														// in general operatii bazate pe ID
			int id = getId(cuvant);
			st =  conn.prepareStatement(sql);
			st.setBoolean(1, false);
			st.setInt(2, id);
			st.execute();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	//punere flag_activ = true
	// connection si ID
	public void reAdaugareCuvant(String cuvant) {
		
		Connection conn = this.connection();
		String sql = "UPDATE DICTIONAR SET ACTIV=? WHERE ID =?";
		connection();
		try {
			
			int id = getId(cuvant);
			st =  conn.prepareStatement(sql);
			st.setBoolean(1, true);
			st.setInt(2, id);
			st.execute();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public List<String> listaCuvinte(){
		
		Connection conn = this.connection();
		List<String> cuvinte = new ArrayList<String>();
		String sql = "Select distinct cuvant from dictionar";
		connection();
		try {
			
			statement = conn.createStatement();
			rs = statement.executeQuery(sql);
			while(rs.next()) {
				String cuv = rs.getString("CUVANT");
				cuvinte.add(cuv);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return cuvinte;
		
	}

}
