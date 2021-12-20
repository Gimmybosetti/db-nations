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
		
		try(Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PW)){
			
			Scanner scan = new Scanner(System.in);
			System.out.print("Inserisci la parola da cercare:\n");
			String filtro = scan.nextLine();
			
			String selectNations = "select c.name as Nome_Nazione, c.country_id as Id,  r.name as Nome_Regione, \r\n"
					+ "con.name as Nome_Continente from countries c \r\n"
					+ "inner join regions r on r.region_id = c.region_id \r\n"
					+ "inner join continents con on r.continent_id = con.continent_id \r\n"
					+ "where c.name like ? \r\n"
					+ "order by c.name;";
			
			try (PreparedStatement psNations = con.prepareStatement(selectNations)){
				psNations.setString(1, "%" + filtro + "%");
				
				try(ResultSet rsNations = psNations.executeQuery()){
					while (rsNations.next()) {
						System.out.print(rsNations.getInt(2) + "\t - \t");	
						System.out.print(rsNations.getString(1) + "\t - \t");
						System.out.print(rsNations.getString(3) + "\t - \t");
						System.out.println(rsNations.getString(4));
					}
				}
			}
			
			System.out.print("\nInserisci l'ID nazione di cui vuoi sapere le lingue: ");
			
			String id = scan.nextLine();
			String selectedId = "select c.name from countries c where c.country_id = ?";
			try (PreparedStatement psCountry = con.prepareStatement(selectedId)){
				psCountry.setString(1, id);
				
				try(ResultSet rsCountry = psCountry.executeQuery()){
					if(rsCountry.next()) {
						System.out.print("\nDettagli della nazione: " + rsCountry.getString(1));
						System.out.print("\n");
					}
				}
			}
			
			String sql = "select l.language as Lingua from languages l\r\n"
					+ "inner join country_languages cl on cl.language_id = l.language_id \r\n"
					+ "inner join countries c on cl.country_id = c.country_id\r\n"
					+ "where c.country_id = ?;";
			try (PreparedStatement psCountry = con.prepareStatement(sql)){
				psCountry.setString(1, id);
				
				try(ResultSet rsCountry = psCountry.executeQuery()){
					System.out.print("Lingue: ");
					while (rsCountry.next()) {
						System.out.print(rsCountry.getString(1) + "\t");
					}
					System.out.print("\n");
				}
			}
			
			String countryStat = "select cs.year as Anno_dati, cs.population as Popolazione_totale, "
					+ "cs.gdp as GDP from country_stats cs \r\n"
					+ "inner join countries c on c.country_id = cs.country_id \r\n"
					+ "where c.country_id = 107\r\n"
					+ "order by cs.`year` desc\r\n"
					+ "limit 1;";
			try (PreparedStatement psCountryStat = con.prepareStatement(countryStat)){
				
				try(ResultSet rsCountryStat = psCountryStat.executeQuery()){
					while (rsCountryStat.next()) {
						System.out.print("Anno statistica: " + rsCountryStat.getString(1) + "\t - \t");
						System.out.print("Popolazione totale: " + rsCountryStat.getString(2) + "\t - \t");
						System.out.println("GDP: " + rsCountryStat.getString(3));
					}
				}
			}
			
			scan.close();
			
		}catch(SQLException e) {
			System.out.println("OOOPS an error occurred");
			System.out.println(e.getMessage());
		}
		
	}

}
