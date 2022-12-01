package org.generation.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {

	
	public static void main(String[] args) {
		
		String url = "jdbc:mysql://localhost:3306/nations";
		String user = "root";
		String password = "root";
		 
		Connection con = null;
		try {
			
			con = DriverManager.getConnection(url, user, password);
			
			final String sql = " SELECT countries.name, countries.country_id, regions.name, continents.name\r\n"
							+  " FROM nations.countries\r\n"
							+  "	JOIN regions\r\n"
							+  "		ON countries.region_id = regions.region_id\r\n"
							+  "	JOIN continents\r\n"
							+  "		ON regions.continent_id = continents.continent_id\r\n"
							+  " WHERE countries.name LIKE ?";
			
			PreparedStatement ps = con.prepareStatement(sql);
			
			Scanner sc = new Scanner(System.in);
			System.out.print("inserisci il nome del Paese da ricercare: ");
			String nationName = sc.nextLine();
			
			ps.setString(1,nationName + "%");
			
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				
				final String name = rs.getString(1);
				final int id = rs.getInt(2);
				final String regionName = rs.getString(3);
				final String continentName = rs.getString(4);
				
				System.out.println(id + " - " + name + " - " + regionName + " - " + continentName);
			}
			
			ps.close();
			
			// ****************************
			// con = DriverManager.getConnection(url, user, password);
			final String sql2 = "SELECT DISTINCT languages.language\r\n"
							+ "FROM nations.country_stats\r\n"
							+ "	JOIN countries\r\n"
							+ "		ON country_stats.country_id = countries.country_id\r\n"
							+ "	JOIN country_languages\r\n"
							+ "		ON countries.country_id = country_languages.country_id\r\n"
							+ "	JOIN languages\r\n"
							+ "		ON country_languages.language_id = languages.language_id\r\n"
							+ "WHERE countries.country_id = ? \r\n";
			
			final String sql3 = "SELECT DISTINCT languages.language, country_stats.population, country_stats.gdp, country_stats.year\r\n"
							+ "FROM nations.country_stats\r\n"
							+ "	JOIN countries\r\n"
							+ "		ON country_stats.country_id = countries.country_id\r\n"
							+ "	JOIN country_languages\r\n"
							+ "		ON countries.country_id = country_languages.country_id\r\n"
							+ "	JOIN languages\r\n"
							+ "		ON country_languages.language_id = languages.language_id\r\n"
							+ "WHERE countries.country_id = ? \r\n"
							+ "ORDER BY country_stats.year DESC LIMIT 1";
			
			PreparedStatement ps1 = con.prepareStatement(sql2);
			PreparedStatement ps2 = con.prepareStatement(sql3);
			
			
			System.out.print("inserisci L'ID del Paese da ricercare: ");
			String nationId = sc.nextLine();
			
			int id = Integer.parseInt(nationId);
			ps1.setInt(1, id);
			ps2.setInt(1, id);
			
			ResultSet rs1 = ps1.executeQuery();
			
			while(rs1.next()) {
				
				final String language = rs1.getString(1);
				
				System.out.println("Languages: " + language + " / ");
			}
			ResultSet rs2 = ps2.executeQuery();
			while(rs2.next()) {
				
				final int population = rs2.getInt(2);
				final String gdp = rs2.getString(3);
				final int year = rs2.getInt(4);
				System.out.println("\nPopulation: " + population + "\nGDP: " + gdp + "\nYear: " + year);
			}
			ps1.close();
			ps2.close();
			
			
		} catch (SQLException ex) {
			
			ex.printStackTrace();
		} finally{
			
			try {
				con.close();
			} catch (Exception e) { }
		}
	}
}
