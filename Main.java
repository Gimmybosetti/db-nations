package nations;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {

	private static final String DB_URL = "jdbc:mysql://localhost:3306/db-nations";
	private static final String DB_USER = "root";
	private static final String DB_PW = "Roottazz0!";
	
	public static void main(String[] args) {
		
		Scanner scan = new Scanner(System.in);
		
		try(Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PW)){
			
			String selectNations = "select c.name as Nome_Nazione, c.country_id as Id, r.name as Nome_Regione,"
					+ "con.name as Nome_Continente from countries c \r\n"
					+ "inner join regions r on r.region_id = c.region_id \r\n"
					+ "inner join continents con on r.continent_id = con.continent_id\r\n"
					+ "order by c.name asc;";
			
			try (PreparedStatement psNations = con.prepareStatement(selectNations)){
				
				try(ResultSet rsNations = psNations.executeQuery()){
					while (rsNations.next()) {
						System.out.print(rsNations.getString(1) + "\t - \t");
						System.out.print(rsNations.getInt(2) + "\t - \t");
						System.out.print(rsNations.getString(3) + "\t - \t");
						System.out.println(rsNations.getString(4));
					}
				}
			}
			
		}catch(SQLException e) {
			System.out.println("OOOPS an error occurred");
			System.out.println(e.getMessage());
		}
		scan.close();
	}

}
