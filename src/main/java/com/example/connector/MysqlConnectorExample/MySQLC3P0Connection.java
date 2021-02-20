package com.example.connector.MysqlConnectorExample;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import javax.sql.ConnectionPoolDataSource;
import javax.sql.PooledConnection;

import com.mchange.v2.c3p0.*;

public class MySQLC3P0Connection {
	
	private Connection conn = null;
    private Statement statement = null;
    private ResultSet results = null;
    
    private String host = null;
    private String port = null;
    private String username = null;
    private String password = null;
    private String database = null;
    private String query = null;
    ComboPooledDataSource cpds = null;
    
    
	MySQLC3P0Connection(String host, String port, String username, String password, String database) throws PropertyVetoException {
		this.host = host;
		this.port = port;
		this.username = username;
		this.password = password;
		this.database = database;
		this.cpds = this.connect();
	}

	public String getConnectionString()  {
		return "jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database;
	}

	private ComboPooledDataSource connect() throws PropertyVetoException {
		ComboPooledDataSource cpds = new ComboPooledDataSource();
		cpds.setDriverClass( "com.mysql.jdbc.Driver" ); //loads the jdbc driver            
		cpds.setJdbcUrl( this.getConnectionString() );
		cpds.setUser(this.username);                                  
		cpds.setPassword(this.password);
		// some default pooling
		cpds.setInitialPoolSize(5);
		cpds.setMaxConnectionAge(100);
		cpds.setMaxIdleTime(300);
		cpds.setMinPoolSize(3);                                     
		cpds.setAcquireIncrement(2);
		cpds.setMaxPoolSize(10);
		return cpds;
	}

	public HashMap<String, ArrayList> readDatabase(String query) throws SQLException, ClassNotFoundException, PropertyVetoException {
		HashMap<String, ArrayList> response = new HashMap<String, ArrayList>();
		PooledConnection cn = cpds.getConnectionPoolDataSource().getPooledConnection();
		this.conn = cn.getConnection();
		// this.conn = cpds.getConnection();
        statement = this.conn.createStatement();
        results = statement.executeQuery(query);
        ResultSetMetaData rsmd = results.getMetaData();
        ArrayList<String> columns = new ArrayList<String>();
        ArrayList<ArrayList<String>> values = new ArrayList<ArrayList<String>>();
        // Columns Names
        // Column Values
        for (int i = 1; i <= rsmd.getColumnCount(); i++) {
        	columns.add(rsmd.getColumnName(i));
        }
        // System.out.println(columns);
        while (results.next()) {
        	ArrayList<String> tmp = new ArrayList<String>();
        	for (int i = 1; i <= rsmd.getColumnCount(); i++) {
        		tmp.add(results.getString(i));
        	}
        	values.add(tmp);
        }
        // System.out.println(values);
        response.put("columns", columns);
        response.put("values", values);
        //System.out.println(response);
    	return response;
	}

	public void execute(String query) throws PropertyVetoException, ClassNotFoundException, SQLException {
	    System.out.println("Creating Connection to Mysql Host - " + this.getConnectionString());
		HashMap <String, ArrayList> response = this.readDatabase(query);

		ArrayList <String> columns = response.get("columns");
	    ArrayList <ArrayList<String>> values = response.get("values");

	    // System.out.println(values.size());
	    System.out.println("Response for the Query is - ");
	    for (int  i=1; i<=values.size() ; i++) {
	    	for (int j=1; j <= columns.size(); j++) {
	    		System.out.println(columns.get(j-1) + ":"  + values.get(i-1).get(j-1));
	    	}
	    }
	}
}
