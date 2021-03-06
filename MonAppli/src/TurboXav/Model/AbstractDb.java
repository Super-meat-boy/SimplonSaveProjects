//regarder Hibernet pour un ORM
//continuer sur Jswing
package TurboXav.Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public abstract class AbstractDb {
	/**
	  * Connexion � la base de donn�e
	  *
	  * @access private static
	  * @name cn
	  * @var Connection
	  *	   
	  **/
	
	private static Connection cn = null;
	
	/**
	  * Ferme la connection
	  * 
	  * @access public static
	  * @name closeConnection
	  * @return boolean
	  * 
	  */
	
	public static void closeConnection() {
		if(AbstractDb.cn != null) {
	
		try {
			AbstractDb.cn.close();	
			System.out.println("Connection ferm�e");
		} catch (SQLException e) {			
			e.printStackTrace();	}
		}
	}
	
	/**
	  *
	  * R�cup�ration de la connexion
	  * 
	  * @access private static
	  * @name getCn
	  * @return void
	  * 
	  **/
	
	private static Connection __getConnexion()
	{			
		if (AbstractDb.cn == null) 
		{ 
			
			try {
			
			ResourceBundle rs 	=	ResourceBundle.getBundle("Properties.config");
			String driver 		=	rs.getString("sgbd.driver");
			String dsn 			=	rs.getString("sgbd.dsn");
			String login 		=	rs.getString("sgbd.login");
			String password 	=	rs.getString("sgbd.password");
			
			
			try {
				  
				Class.forName(driver);
				AbstractDb.cn = DriverManager.getConnection(dsn,login,password);
			}catch(ClassNotFoundException e) {
				System.out.println(e.getMessage());
			}
			catch(SQLException e) {
				e.printStackTrace();
			}
			}
			catch(MissingResourceException e) {
			  System.out.println("Hoho ! il faut v�rifier le chemin du fichier properties");		 
			}
		}
		
		return AbstractDb.cn; 
	}
	
	/**
	  * Ex�cute une requ�te SQL
	  *  
	  * 
	  * @access protected
	  * @name __executeSqlSelect
	  * @param String : Requ�te SQL � ex�cuter
	  * @return ResultSet
	  * 
	  */
	
	protected static ResultSet __executeSqlSelect(String Sql, String... params) {
		
		try {
			
			Connection cn			= AbstractDb.__getConnexion();		
			
			java.sql.PreparedStatement stmt	= cn.prepareStatement(Sql);	
			
			for(int i = 0 ; i < params.length ; i++) { stmt.setString(i+1, params[i]);	}
			
			ResultSet rs			= stmt.executeQuery();	
			
			return rs;
			
		}
		catch(Exception e) {
			System.out.println("Sql = "+Sql);
			e.printStackTrace();
			
		}
		
		return null;
		
	}
	
	/**
	  * Ex�cute une requ�te SQL
	  *  
	  * 
	  * @access protected
	  * @name __executeSqlUpdate
	  * @param String : Requ�te SQL � ex�cuter
	  * @return int : nombre d'enregistrements manipul�s
	  * 
	  */
	
	protected static int __executeSqlUpdate(String Sql, String... params) {
		
		try {
			
			Connection cn = AbstractDb.__getConnexion();		
			
			PreparedStatement stmt = cn.prepareStatement(Sql);
			
			for(int i = 0 ; i < params.length ; i++) {	stmt.setString(i+1, params[i]);	}
		    
			return stmt.executeUpdate();
			
		}
		catch(Exception e) {
			
			System.out.println("Sql = "+Sql);
			e.printStackTrace();
			
		}
		return 0;		
	}
}
