package constrainteEtTransactions;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.print.attribute.standard.ReferenceUriSchemesSupported;

public abstract class SqlQuery {
	
	private static Connection connection= null;
	
	public static Connection getConnection() {		
		
		
		if(SqlQuery.connection != null) { return SqlQuery.connection; }
		
		System.out.println("-------- PostgreSQL JDBC Connection Testing ------------");
		
		try {
			
			ResourceBundle rs 	=	ResourceBundle.getBundle("config");
			String driver 		=	rs.getString("sgbd.driver");
			String dsn 			=	rs.getString("sgbd.dsn");
			String login 		=	rs.getString("sgbd.login");
			String password 	=	rs.getString("sgbd.password");
			
			try {
				
					Class.forName(driver);					
	
					try {
						SqlQuery.connection = DriverManager.getConnection(dsn, login, password);
					} catch (SQLException e) {
	
						System.out.println("Connection Failed! Check output console");
						e.printStackTrace();
						return null;
	
					}
	
					if (SqlQuery.connection != null) {
						System.out.println("You made it, take control your database now!");
						return SqlQuery.connection;
					} else {
						System.out.println("Failed to make connection!");
					}
					return SqlQuery.connection;			
				
				
				} catch (ClassNotFoundException e) {
					System.out.println("Where is your PostgreSQL JDBC Driver? Include in your library path!");
					e.printStackTrace();
					return null;
				}	
						
			}
			catch(MissingResourceException e) {
			  System.out.println("Hoho ! il faut v�rifier le chemin du fichier properties");		 
			}		
		

		System.out.println("PostgreSQL JDBC Driver Registered!");
		return null;

		
	}
}
