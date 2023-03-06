package com.wmg.issac;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DBUtil {

	private static final Logger logger = LoggerFactory.getLogger(DBUtil.class);

	private static final String DB_DRIVER_CLASS = "oracle.jdbc.driver.OracleDriver";
	private static final String DB_USERNAME = "GCDM_APP_CORE";
	private static final String DB_PASSWORD = "GCDM_APP_CORE";
	private static final String DB_URL = "jdbc:oracle:thin:@gcdmdc.aue1.wmg.com:1521/GCDMDC";

	private static Connection connection = null;
	static {
		try {
			Class.forName(DB_DRIVER_CLASS);
			connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
		} catch (ClassNotFoundException | SQLException e) {
			logger.info("Exception occurred in DBUtil ");
		}
	}

	public static Connection getConnection() {
		return connection;
	}

}
