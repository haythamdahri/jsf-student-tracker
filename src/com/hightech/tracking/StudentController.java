package com.hightech.tracking;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class StudentController {

	private List<Student> students;
	private StudentDbUtil studentDbUtil;
	private Logger logger = Logger.getLogger(getClass().getName());

	public StudentController() throws Exception {
		students = new ArrayList<>();

		studentDbUtil = StudentDbUtil.getInstance();
	}

	public List<Student> getStudents() {
		return students;
	}

	public void loadStudents() {

		logger.info("Loading students");

		students.clear();

		try {
			// get all students from database
			students = studentDbUtil.getStudents();

		} catch (Exception exc) {
			// send this to server logs
			logger.log(Level.SEVERE, "Error loading students", exc);

			// add error message for JSF page
			addErrorMessage(exc);
		}
	}

	// Load a specified student to be updated
	public String loadStudent(Long studentId) {

		logger.info("Loading student");

		try {
			// get a specific students from database
			Student student = this.studentDbUtil.getStudent(studentId);
			
			// Put in the request attribute ... so we can use it on the form page
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			
			Map<String, Object> requestMap = externalContext.getRequestMap();
			requestMap.put("student", student);

		} catch (Exception exc) {
			// send this to server logs
			logger.log(Level.SEVERE, "Error loading students", exc);

			// add error message for JSF page
			addErrorMessage(exc);
		}
		// Redirect user to home page for students listing
		return "update-student-form";
	}
	
	// Add new student to the database
	public String addStudent(Student student) {
		try {
			logger.info("Persisting student into database");
			this.studentDbUtil.addStudent(student);
			this.addMessage("Student has been added successfully");
		} catch (Exception ex) {
			this.addErrorMessage(ex);
		}
		// Redirect user to home page for students listing
		return "list-students?faces-redirect=true";
	}
	
	// Update a specific student from database
	public String updateStudent(Student student) {
		try {
			// Logging
			logger.info("Updating existing student in the database");
			// Performing update in the database
			this.studentDbUtil.updateStudent(student);
			// Add a new user message
			this.addMessage("Student has been updated successfully");
		}
		catch(Exception ex) {
			this.addErrorMessage(ex);
		}
		// Redirect user to home page for students listing
		return "list-students?faces-redirect=true";
	}

	// Delete a student to the database
	public String deleteStudent(Long studentId) {
		try {
			logger.info("Deleting student fromdatabase");
			this.studentDbUtil.deleteStudent(studentId);
			this.addMessage("Student has been deleted successfully");
		} catch (Exception ex) {
			this.addErrorMessage(ex);
		}
		// Redirect user to home page for students listing
		return "list-students?faces-redirect=true";
	}

	// Add error message to be displayed later in the JSF page
	private void addErrorMessage(Exception exc) {
		FacesMessage message = new FacesMessage("Error: " + exc.getMessage());
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	// Add error message to be displayed later in the JSF page
	private void addMessage(String text) {
		FacesMessage message = new FacesMessage(text);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

}
