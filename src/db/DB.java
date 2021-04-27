package db;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.util.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DB {
	Statement statement = null;
	Connection conn =null;
	
	public static Connection connection(){
		Connection conn =null;
		Statement statement = null;
		try {
			Class.forName("org.sqlite.JDBC"); // sa fie intr-un String
			conn = DriverManager.getConnection("jdbc:sqlite:C:/Users/anama/eclipse-workspace/Pb/SpellChecker_DB/db.db");
			
			statement = conn.createStatement();
			
			String sql = "Select * from dictionar";
			ResultSet result =  statement.executeQuery(sql);
			while(result.next()) {
				String cuv = result.getString("CUVANT");
				System.out.println(cuv);
			}
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conn;
		
	}
	
	
	//inserarea unui cuvant in baza de date
	// Clasa care sa tina toti membrii - parametrii, ex nume: wordObj,eventual data ca parametru separat
	public static void insertData(String cuvant, String tip, int frecventa, boolean activ,boolean adaugat, Date data_adaugare ) {
		
		Connection conn = null;
		PreparedStatement st;
		String sql = "INSERT INTO DICTIONAR VALUES(?,?,?,?,?,?,?)";
		try {
			conn = DriverManager.getConnection("jdbc:sqlite:C:/Users/anama/eclipse-workspace/Pb/SpellChecker_DB/db.db");
			st = conn.prepareStatement(String.valueOf(sql));
			st.setString(2,cuvant);
			st.setString(3, tip);
			st.setInt(4, frecventa);
			st.setBoolean(5, activ);
			st.setBoolean(6, adaugat);
			st.setDate(7, (java.sql.Date) data_adaugare); 
			st.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	//cautare daca un anumit cuvant exista in baza de date
	public boolean cautareCuvant(String cuvant) {
		String sql = "Select cuvant from dictionar";
		try {
			ResultSet result = statement.executeQuery(sql);
			while(result.next()) {
				String cuv = result.getString("CUVANT");
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
		String sql = "Select cuvant,frecventa from dictionar";
		int frecv1 = 0;
		try {
			conn = DriverManager.getConnection("jdbc:sqlite:C:/Users/anama/eclipse-workspace/Pb/SpellChecker_DB/db.db");
			statement = conn.createStatement();
			ResultSet result = statement.executeQuery(sql);
			while(result.next()) {
				String cuv = result.getString("CUVANT");
				int frecv = result.getInt("FRECVENTA");
				if(cuv.equals(cuvant)) {
					frecv1 = frecv;
				}
			}
			frecv1= frecv1+1;
			
			PreparedStatement st =  conn.prepareStatement("UPDATE DICTIONAR SET FRECVENTA=? WHERE CUVANT =?");
			st.setInt(1, frecv1);
			st.setString(2, cuvant);
			st.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	//stergere cuvant = punere flag_activ pe false
	// folosire functie pentru connection
	public void stergereCuvant(String cuvant) {
		
		try {
			conn = DriverManager.getConnection("jdbc:sqlite:C:/Users/anama/eclipse-workspace/Pb/SpellChecker_DB/db.db");
			statement = conn.createStatement();
			
			PreparedStatement st =  conn.prepareStatement("UPDATE DICTIONAR SET ACTIV=? WHERE CUVANT =?");
																// de inlocuit cu WHERE ID = ?
														// in general operatii bazate pe ID
			st.setBoolean(1, false);
			st.setString(2, cuvant);
			st.execute();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	//punere flag_activ = true
	// connection si ID
	public void reAdaugareCuvant(String cuvant) {
		try {
			conn = DriverManager.getConnection("jdbc:sqlite:C:/Users/anama/eclipse-workspace/Pb/SpellChecker_DB/db.db");
			statement = conn.createStatement();
			
			PreparedStatement st =  conn.prepareStatement("UPDATE DICTIONAR SET ACTIV=? WHERE CUVANT =?");
			st.setBoolean(1, true);
			st.setString(2, cuvant);
			st.execute();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
