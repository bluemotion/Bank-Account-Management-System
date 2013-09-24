package csc309.A4;

import javax.servlet.ServletConfig;
import javax.servlet.http.*;

import org.apache.tomcat.dbcp.dbcp.cpdsadapter.DriverAdapterCPDS;
import org.apache.tomcat.dbcp.dbcp.datasources.SharedPoolDataSource;

public class Initialization extends HttpServlet {
	public void init(ServletConfig config) {
		try {
		    //Initialize connection pool

			String dbDriver = config.getInitParameter("dbDriver");
    		String dbURL = config.getInitParameter("dbURL");
    		String dbUser = config.getInitParameter("dbUser");
    		String dbPassword = config.getInitParameter("dbPassword");
    		
		    DriverAdapterCPDS ds = new DriverAdapterCPDS();
		    ds.setDriver(dbDriver);
		    ds.setUrl(dbURL);
		    
		    ds.setUser(dbUser);
		    ds.setPassword(dbPassword);
		    
		    SharedPoolDataSource dbcp = new SharedPoolDataSource();
		    dbcp.setConnectionPoolDataSource(ds);

		    config.getServletContext().setAttribute("dbpool",dbcp);

		}
		catch (Exception ex) {
		    getServletContext().log("csc309Security Error: " + ex.getMessage());
		}
	}
}


