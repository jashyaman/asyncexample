package io.example.app.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Callable;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MultiThreadedDBInsertExecutor implements Callable<String> {
	
	private static final Logger log = LoggerFactory.getLogger(DBInsertService.class);

	private String responseTitle;
	private DataSource datasource;
	
	
	public MultiThreadedDBInsertExecutor(String responseTitle, DataSource dataSource) {
		this.responseTitle = responseTitle;
		this.datasource = dataSource;
	}
	

	@Override
	public String call() throws Exception {
		return insertRecord(responseTitle);
	}
	
	
	

	//THIS APPROACH DOES NOT USE THE CONNECTION POOL. TOTAL WASTE OF TIME.
	// change how connections are being made. It is slow this way.
	public String insertRecord(String responseTitle) throws SQLException {
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			con = datasource.getConnection();
			
		//	log.info("processing insert for '" + responseTitle + "'");

			ps = con.prepareStatement("SELECT COUNT(1) FROM TITLE_STORE");
			rs = ps.executeQuery();
			rs.next();
			int count = rs.getInt(1);

		//	log.info("number of records " + count);
			rs.close();
			ps.close();
			Integer id = 0;
			if (count == 0) {
		//		log.info("count is zero, id is 1");
				id = 1;
			} else {
				ps = con.prepareStatement("SELECT MAX(ID) FROM TITLE_STORE");
				rs = ps.executeQuery();
				rs.next();
				id = rs.getInt(1);
		//		log.info("id value from DB " + id);
				id = id + 1;
		//		log.info("id value for current operation is " + id);
			}
			rs.close();
			ps.close();
			LocalDateTime now = LocalDateTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yy HH:mm:ss");
			String date = now.format(formatter);

			ps = con.prepareStatement("INSERT INTO TITLE_STORE (ID, TITLE, TIME) VALUES  (?,?,?)");
			ps.setInt(1, id);
			ps.setString(2, responseTitle);
			ps.setString(3, date);
			Integer recInsCount = ps.executeUpdate();

			if (recInsCount > 0) {
				con.commit();
				return " insert success. id of record : " + id;
			} else {
				con.rollback();
				return "record insert fails";
			}
		} finally {
			rs.close();
			ps.close();
			con.close();
			
			
		}
	}
	
	

}
