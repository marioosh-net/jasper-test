package net.marioosh.jasper.datasource;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

import net.marioosh.jasper.model.Page;
import net.marioosh.jasper.model.PageFactory;


public class SQLiteDataSource {

	private String dbname;
	
	public SQLiteDataSource(String dbname) {
		this.dbname = dbname;
	}
	
	public int prepareDB(int count) {
		Connection conn = null;
		int j = 0;		
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:"+dbname);

			int r = 0;

			for(int i = 0; i < count; i++) {
				Page page = PageFactory.createPage();
			Statement stmt = conn.createStatement();
			
			r = stmt.executeUpdate("insert into 'pages' (imieNazwisko, adres1, adres2, text) " +
					"values('"+page.getImieNazwisko()+"','"+page.getAdres1()+"','"+page.getAdres2()+"','"+page.getI1()+"')");
			j++;
			// System.out.println(page);
			if(j%100 == 0)
				System.out.println(j);
			}
			return j;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				System.err.println(e);
			}
		}
		return j;
	}
	
	public ResultSet getAll() {
		Connection conn = null;
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:"+dbname);
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select * from 'pages'");
			return rs;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				System.err.println(e);
			}
		}
		return null;
	}
	
	public void initDB() {
		Connection conn = null;
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:"+dbname);
			
			Statement check = conn.createStatement();
			ResultSet rs = check.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='pages';");
			if(!rs.next()) {
				Statement stmt = conn.createStatement();
				stmt.executeUpdate("create table pages(imieNazwisko, adres1, adres2, text)");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				System.err.println(e);
			}
		}
	}

	public static void main(String[] args) {
		SQLiteDataSource sqlite = new SQLiteDataSource("db100000.db");
		sqlite.initDB();
		System.out.println(sqlite.prepareDB(100000) + " records added.");
		
		/*
		sqlite = new SQLiteDataSource("db10000.db");
		sqlite.initDB();
		System.out.println(sqlite.prepareDB(10000) + " records added.");
		*/
	}
}
