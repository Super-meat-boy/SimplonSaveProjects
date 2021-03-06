package server;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
  * 
  * Classe de connection JDBC � n'importe quel Driver (MySql, Postgresql,....)
  * @author Xavier Tagliarino <xavier.tagliarino@gmail.com>
  * @name SqlQuery
  * @access public abstract
  *
  */

public abstract class SqlQuery {
	
	/**
	 * 
	 * Ressource de connection � la base de donn�es
	 * 
	 * @access private static 
	 * @var Connection
	 * @name connection
	 */
	
	private static Connection connection= null;
	
	/**
	 * M�thode (Singleton) qui tente de se connecter au moteur de base de donn�es
	 * 
	 * 
	 * @access public static
	 * @name getConnection
	 * @return Connecion : Ressource de connection
	 * 
	 */	
	
	public static Connection getConnection() {		
		
		
		//Si on a d�j� une connection on la renvoie pour ne maintenir qu'une seule connection
		if(SqlQuery.connection != null) { return SqlQuery.connection; }
		
		//Message de bienvenu
		System.out.println("-------- PostgreSQL JDBC Connection Test ------------");
		
		//On r�cup�re les infos dans le fichier de config "config.properties"
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
	
						System.err.println("Pb de connection");
						e.printStackTrace();
						return null;
	
					}
	
					if (SqlQuery.connection != null) {
						System.out.println("Acc�s � la base de donn�es ouvert");
						return SqlQuery.connection;
					} else {
						System.err.println("Aucune connexion � la base de donn�es");
					}
					return SqlQuery.connection;			
				
				
				} catch (ClassNotFoundException e) {
					System.out.println("Le driver PosqtGreSQL est introuvable");
					e.printStackTrace();
					return null;
				}	
						
			}
			catch(MissingResourceException e) {
			  System.out.println("il faut v�rifier le chemin du fichier properties, il est introuvable");		 
			}		
		

		return null;		
	}
}
