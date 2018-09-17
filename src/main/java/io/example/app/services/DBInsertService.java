package io.example.app.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.sql.ConnectionPoolDataSource;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DBInsertService {
	
	private static final Logger log = LoggerFactory.getLogger(DBInsertService.class);

	@Autowired
	DataSource datasource;

	// TODO change how connections are being made. It is slow this way.
	public String insertRecord(String responseTitle) throws SQLException {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs  = null;
		try {
			con = datasource.getConnection();
			//log.info("processing insert for '" + responseTitle + "'");

			//TODO USE CONTANTS TO STORE SQL
			 ps = con.prepareStatement("SELECT COUNT(1) FROM TITLE_STORE");
			
			rs = ps.executeQuery();
			rs.next();
			int count = rs.getInt(1);
			rs.close();
			ps.close();
		//	log.info("number of records " + count);
			
			//TODO CHANGE TO STRING BUILDER
			Integer id = 0;
			if (count == 0) {
		//		log.info("count is zero, id is 1");
				id = 1;
			} else {
				//TODO USE CONSTANTS TO STORE SQL
				ps = con.prepareStatement("SELECT MAX(ID) FROM TITLE_STORE");
						
				rs = ps.executeQuery();
				rs.next();
				id = rs.getInt(1);
			//	log.info("id value from DB " + id);
				id = id + 1;
			//	log.info("id value for current operation is " + id);
			}
			rs.close();
			ps.close();
			LocalDateTime now = LocalDateTime.now();
			//TODO USE CONSTANTS
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yy HH:mm:ss");
			String date = now.format(formatter);

			//TODO USE CONSTANTS TO STORE SQL
			ps = con.prepareStatement("INSERT INTO TITLE_STORE (ID, TITLE, TIME) VALUES  (?,?,?)");
			ps.setInt(1, id);
			ps.setString(2, responseTitle);
			ps.setString(3, date);
			Integer recInsCount = ps.executeUpdate();

			//TODO CHANGE TO STRING BUILDER
			
			if (recInsCount > 0) {
				con.commit();
				return " insert success. id of record : " + id;
			} else {
				con.rollback();
				return "record insert fails";
			}
		} finally {
			// ULTRA IMPORTANT ESPECIALLY WHILE DEALING WITH THREADS
			ps.close();
			rs.close();
			con.close();
			
		}
	}

}
