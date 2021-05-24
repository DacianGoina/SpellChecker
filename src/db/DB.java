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
	static String basePath ="././dictionar.db";
	
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
	
	// inserarea unui cuvant in baza de date
	public void insertCuvantNou(WordObj word) {
		
		Connection conn = this.connection();
		
		String sql = "INSERT INTO DICTIONAR(CUVANT,TIP,FRECVENTA,ACTIV,ADAUGAT,DATA_ADAUGARII) VALUES(?,?,?,?,?,?)";
		
		final LocalDateTime dt = LocalDateTime.now(); 
		final java.sql.Date sqlDate = java.sql.Date.valueOf(dt.toLocalDate());
		String date1 = String.valueOf(sqlDate);
		try {
			
			st = conn.prepareStatement(sql);
			st.setString(1,word.getCuvant());
			st.setString(2, word.getTip());
			st.setInt(3, word.getFrecventa());
			st.setBoolean(4, word.isActiv());
			st.setBoolean(5, word.isAdaugat());
			st.setString(6, date1);
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
	
	// cautare daca un anumit cuvant exista in baza de date
	public WordObj cautareCuvant(String cuvant) {
		
		Connection conn = this.connection();
		String sql = "Select * from dictionar where cuvant = ?"; 
		WordObj obj = null;
		
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
				String date = rs.getString("DATA_ADAUGARII");
				
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
	/*public void crestereFrecventa(final int idCuvant, int frecv) {
		
		String update ="UPDATE DICTIONAR SET FRECVENTA=? WHERE ID =?";
		
		Connection conn = this.connection();
	
		int frecv1 ;
		frecv1 = frecv + 1;
		
	
		try {	
		st =  conn.prepareStatement(update);
		st.setInt(1, frecv1);
		st.setInt(2, idCuvant);
		st.execute();
		st.close();
		conn.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}*/
	
	//stergere cuvant = punere flag_activ pe false
	// folosire functie pentru connection
	public void stergereCuvant(final int idCuvant) {
		
		
		String sql = "UPDATE DICTIONAR SET ACTIV=? WHERE ID =?";
		
		
		Connection conn = this.connection();
	
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
	/*public void reAdaugareCuvant(final int idCuvant) {
		
		
		String sql = "UPDATE DICTIONAR SET ACTIV=? WHERE ID =?";
		
		
		Connection conn = this.connection();
	
		
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
	}*/
	
	public void adaugareCuvinte(final Vector<WordObj> vCuvinte) {

			final Connection conn = this.connection();

			final String sql = "INSERT INTO DICTIONAR(CUVANT,TIP,FRECVENTA,ACTIV,ADAUGAT,DATA_ADAUGARII) VALUES(?,?,?,?,?,?)";
			try {
				final PreparedStatement stmt = conn.prepareStatement(sql);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			for(final WordObj word : vCuvinte) {

				if(word.getId() <= -1) {

					// cuvantul e nou: inserezi cuvantul nou;
					final LocalDateTime dt = LocalDateTime.now(); 
					final java.sql.Date sqlDate = java.sql.Date.valueOf(dt.toLocalDate());
					String date = String.valueOf(sqlDate);
					
					try {
						st.setString(1,word.getCuvant());
						st.setString(2, word.getTip());
						st.setInt(3, word.getFrecventa());
						st.setBoolean(4, word.isActiv());
						st.setBoolean(5, word.isAdaugat());
						st.setString(6, date);
				        st.addBatch();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
				} else {

						// TODO: ne gandim in ce conditii apare aceasta situatie;
					if(gasireCuvant(word) == true) {
						crestereFrecventa(word.getId(),word.getFrecventa());
					}
				}
				try {
					int[] affectedRecords = st.executeBatch();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	
	public TreeMap<String, WordObj> getlistaCuvinte(){
		
		Connection conn = this.connection();
		
		TreeMap<String, WordObj> cuvinte = new TreeMap<String,WordObj>();
		
		String sql = "Select * from dictionar where activ = true";
		
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
	public void adaugare(String cuvant) {
			
			WordObj word = new WordObj(cuvant);
			
			Connection conn = this.connection();
			
			String sql = "INSERT INTO DICTIONAR(CUVANT,TIP,FRECVENTA,ACTIV,ADAUGAT,DATA_ADAUGARII) VALUES(?,?,?,?,?,?)";
			
			final LocalDateTime dt = LocalDateTime.now(); 
			final java.sql.Date sqlDate = java.sql.Date.valueOf(dt.toLocalDate());
			String date1 = String.valueOf(sqlDate);
			if(gasireCuvant(word) == false) {
			try {
				
				st = conn.prepareStatement(sql);
				st.setString(1,word.getCuvant());
				st.setString(2, word.getTip());
				st.setInt(3, word.getFrecventa());
				st.setBoolean(4, word.isActiv());
				st.setBoolean(5, word.isAdaugat());
				st.setString(6, date1);
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
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
	
	
	////////////////////////////////////
	
	
	public void crestereFrecventa(final int idCuvant, int frecv) {
			
			String update ="UPDATE DICTIONAR SET FRECVENTA=? WHERE ID =?";
			
			Connection conn = this.connection();
		
			int frecv1 ;
			frecv1 = frecv + 1;
			
		
			try {	
			st =  conn.prepareStatement(update);
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
	
	
	//////////////////////////////////////////
	
	public void reAdaugareCuvant(final int idCuvant) {
			
			
			String sql = "UPDATE DICTIONAR SET ACTIV=? WHERE ID =?";
			
			
			Connection conn = this.connection();
		
			
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
	
}
