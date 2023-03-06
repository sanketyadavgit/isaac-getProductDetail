package com.wmg.issac;

import java.io.IOException;
import java.io.Reader;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetProductDetail {

	private static final Logger logger = LoggerFactory.getLogger(GetProductDetail.class);

	public String getProductDetailRequest(String gpid) throws Exception {

		logger.info("getProductDetailRequest() : START : gpid : " + gpid);
		long startTime = System.currentTimeMillis();
		try {
			startTime = System.currentTimeMillis();
			Connection connection = DBUtil.getConnection();
			String gpidCheckQuery = "select 1 from gcdm_app_core.product where gpid = ?";
			CallableStatement cs1 = connection.prepareCall(gpidCheckQuery);
			cs1.setString(1, gpid);
			ResultSet gpidCheck = cs1.executeQuery();

			if (gpidCheck.next() == true) {
				logger.info("GPID: " + gpid + " is valid ");
				String procCallQuery = "CALL sp_product_details_test(?,?)";
				CallableStatement cs2 = connection.prepareCall(procCallQuery);
				cs2.setString(1, gpid);
				cs2.registerOutParameter(2, Types.CLOB);
				cs2.execute();
				logger.info("getProductDetailRequest() : DB Executed : ElapsedTime : "
						+ (System.currentTimeMillis() - startTime) + " msec. ");

				Clob clobOutput = cs2.getClob(2);
				Reader clobReader = clobOutput.getCharacterStream();
				StringBuffer buffer = new StringBuffer();
				int ch;
				try {
					while ((ch = clobReader.read()) != -1) {
						buffer.append("" + (char) ch);
					}
					logger.info("bufferString : " + new String(buffer));

				} catch (IOException e) {
					throw new SQLException(e.getMessage());
				}
				logger.info("getProductDetailRequest() : SUCCESS : Final ElapsedTime : "
						+ (System.currentTimeMillis() - startTime) + " msec. ");
				return buffer.toString();
			} else {
				logger.info("GPID: " + gpid + " is invalid");
				return ("GPID: " + gpid + " is invalid.");
			}
		} catch (Exception e) {
			logger.error("getProductDetailRequest() : EXCEPTION : ElapsedTime : "
					+ (System.currentTimeMillis() - startTime) + " msec. " + e.getMessage(), e);
			return ("Exception Occured. " + e.getMessage());
		}
	}

}
