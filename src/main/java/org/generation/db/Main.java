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
			
		} catch (SQLException ex) {
			
			ex.printStackTrace();
		} finally{
			
			try {
				con.close();
			} catch (Exception e) { }
		}
	}
}
