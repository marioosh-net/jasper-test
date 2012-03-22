package net.marioosh.jasper.datasource;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;


public class PGSqlDataSource {

	private SecureRandom random = new SecureRandom();
	private Random randomGenerator = new Random();
	private String dbname;
	
	public PGSqlDataSource(String dbname) {
		this.dbname = dbname;
	}
	
	public int prepareDB(int count) {
		Connection conn = null;
		int j = 0;		
		try {
			Class.forName("org.postgresql.Driver");
			conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/"+dbname+"?charSet=UTF-8","pdf","pdf");

			int r = 0;

			for(int i = 0; i < count; i++) {
				Page page = randomPage();
			Statement stmt = conn.createStatement();
			
			r = stmt.executeUpdate("insert into pages (imieNazwisko, adres1, adres2, text) " +
					"values('"+page.getImieNazwisko()+"','"+page.getAdres1()+"','"+page.getAdres2()+"','"+page.getText()+"')");
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
	
	public ResultSet getAll(int limit) {
		Connection conn = null;
		try {
			Class.forName("org.postgresql.Driver");
			conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/"+dbname+"?charSet=UTF-8","pdf","pdf");
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select * from pages limit "+limit);
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
			Class.forName("org.postgresql.Driver");
			conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/"+dbname+"?charSet=UTF-8","pdf","pdf");
			
			Statement stmt = conn.createStatement();
			stmt.executeUpdate("create table pages(imieNazwisko VARCHAR(256), adres1 VARCHAR(256), adres2 VARCHAR(256), text VARCHAR(1256))");

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

	private Page randomPage() {
		return new Page(randomString() + " " + randomString(), randomString(), randomString(), lorem()[randomGenerator.nextInt(3)]);
	}
	
	class Page {
		String imieNazwisko;
		String adres1;
		String adres2;
		String text;
		
		public Page(String imieNazwisko, String adres1, String adres2, String text) {
			super();
			this.imieNazwisko = imieNazwisko;
			this.adres1 = adres1;
			this.adres2 = adres2;
			this.text = text;
		}

		public String getImieNazwisko() {
			return imieNazwisko;
		}
		
		public void setImieNazwisko(String imieNazwisko) {
			this.imieNazwisko = imieNazwisko;
		}
		
		public String getAdres1() {
			return adres1;
		}
		
		public void setAdres1(String adres1) {
			this.adres1 = adres1;
		}
		
		public String getAdres2() {
			return adres2;
		}
		
		public void setAdres2(String adres2) {
			this.adres2 = adres2;
		}
		
		public String getText() {
			return text;
		}
		
		public void setText(String text) {
			this.text = text;
		}
		
		@Override
		public String toString() {
			return "{"+imieNazwisko+", "+adres1+", "+adres2+", "+text+"}";
		}
	}

	private String randomString() {
		return new BigInteger(130, random).toString(32);
	}
	
	private String[] lorem() {
		return new String[]{
				"Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt. Neque porro quisquam est, qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit, sed quia non numquam eius modi tempora incidunt ut labore et dolore magnam aliquam quaerat voluptatem. Ut enim ad minima veniam, quis nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid ex ea commodi consequatur? Quis autem vel eum iure reprehenderit qui in ea voluptate velit esse quam nihil molestiae consequatur, vel illum qui dolorem eum fugiat quo voluptas nulla pariatur?",
				"Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt. Neque porro quisquam est, qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit, sed quia non numquam eius modi tempora incidunt ut labore et dolore magnam aliquam quaerat voluptatem. Ut enim ad minima veniam, quis nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid ex ea commodi consequatur? Quis autem vel eum iure reprehenderit qui in ea voluptate velit esse quam nihil molestiae consequatur, vel illum qui dolorem eum fugiat quo voluptas nulla pariatur?",
				"At vero eos et accusamus et iusto odio dignissimos ducimus qui blanditiis praesentium voluptatum deleniti atque corrupti quos dolores et quas molestias excepturi sint occaecati cupiditate non provident, similique sunt in culpa qui officia deserunt mollitia animi, id est laborum et dolorum fuga. Et harum quidem rerum facilis est et expedita distinctio. Nam libero tempore, cum soluta nobis est eligendi optio cumque nihil impedit quo minus id quod maxime placeat facere possimus, omnis voluptas assumenda est, omnis dolor repellendus. Temporibus autem quibusdam et aut officiis debitis aut rerum necessitatibus saepe eveniet ut et voluptates repudiandae sint et molestiae non recusandae. Itaque earum rerum hic tenetur a sapiente delectus, ut aut reiciendis voluptatibus maiores alias consequatur aut perferendis doloribus asperiores repellat"
		};
	}
	
	public static void main(String[] args) {
		PGSqlDataSource sqlite = new PGSqlDataSource("pdf");
		sqlite.initDB();
		System.out.println(sqlite.prepareDB(100000) + " records added.");
		
		/*
		sqlite = new SQLiteDataSource("db10000.db");
		sqlite.initDB();
		System.out.println(sqlite.prepareDB(10000) + " records added.");
		*/
	}
}
