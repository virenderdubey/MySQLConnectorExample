package com.example.connector.MysqlConnectorExample;

import java.util.Properties;
import java.util.HashMap;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

import com.example.connector.MysqlConnectorExample.MySQLConnection;
import com.example.connector.MysqlConnectorExample.MySQLC3P0Connection;


public class MySQL5 {

	public static void main( String[] args ) throws Exception
    {
		String configFile = System.getProperty("configFile");
		if (configFile == null) {
			File jarPath=new File(MySQL5.class.getProtectionDomain().getCodeSource().getLocation().getPath());
	        String propertiesPath=jarPath.getParentFile().getAbsolutePath();
			// configFile = propertiesPath+"/db.properties";
			configFile = "db.properties";
		}

		Properties props = new Properties();
		System.out.println("Reading Configs from Config File - " + configFile);
	    InputStream is = MySQL5.class.getClassLoader().getResourceAsStream(configFile);
	    System.out.println(is);
	    props.load(is);
	    String host = props.getProperty("host", "localhost");
	    String port = props.getProperty("port", "3306");
	    String username = props.getProperty("username", "read");
	    String password = props.getProperty("password", "r3@d");
	    String database = props.getProperty("database", "test");
	    String query = props.getProperty("query", "select  2+2");

	    //MySQLConnection obj = new MySQLConnection(host, port, username, password, database);
	    MySQLC3P0Connection obj = new MySQLC3P0Connection(host, port, username, password, database);
	    obj.execute("show  tables");
	    obj.execute("show  tables");
	    obj.execute("show  tables");
	    // obj.execute("show  tables");
	    // obj.execute("show  tables");
	    // obj.execute("show  tables");
	    
    }
}
