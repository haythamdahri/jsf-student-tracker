package com.hightech.tracking;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.annotation.Resource;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {

	/*
	 * Inject DataSource from configured options in context.xml and reference in
	 * web.xml
	 */
	@Resource(name = "jdbc/student_tracker")
	private DataSource dataSource;

	/*
	 * Handle GET requests
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/plain");
		PrintWriter writer = null;
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		String sql = "";
		Student student = null;
		try {
			writer = response.getWriter();
			// Get connection from injected resource DataSource
			connection = this.dataSource.getConnection();
			// Construct sql query
			sql = "select *from student;";
			// Create statement and retrieve result set
			statement = connection.createStatement();
			resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				student = new Student(resultSet.getLong(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString("email"));
				writer.println(student);
			}
			// Close connection and other related variables
			resultSet.close();
			statement.close();
			connection.close();
		} catch (Exception ex) {
			writer.println("Exception: " + ex.getMessage());
		}

	}

}
