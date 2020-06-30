package com.example.shopping.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "App_User", 
				uniqueConstraints = { @UniqueConstraint(name = "APP_USER_UK", columnNames = "User_Name") })
public class AppUser {
	@Id
	@GeneratedValue
	@Column(name = "User_Id", nullable = false)
	private Long userId;

	@Column(name = "User_Name", length = 36, nullable = false)
	private String userName;

	@Column(name = "Encryted_Password", length = 128, nullable = false)
	private String encrytedPassword;

	@Column(name = "Enabled", length = 1, nullable = false)
	private boolean enabled;
	
	@Column(name = "First_Name", length = 15)
	private String firstName;
	
	@Column(name = "Last_Name", length = 15)
	private String lastName;
	
	@Column(name = "Street", length = 50)
	private String street;
	
	@Column(name = "Town", length = 50)
	private String town;
	
	@Column(name = "Email", length = 50)
	private String email;
	
	@Column(name = "phone", length = 15)
	private String phone;
	
	
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEncrytedPassword() {
		return encrytedPassword;
	}

	public void setEncrytedPassword(String encrytedPassword) {
		this.encrytedPassword = encrytedPassword;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getTown() {
		return town;
	}

	public void setTown(String town) {
		this.town = town;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "AppUser [userId=" + userId + ", userName=" + userName + ", encrytedPassword=" + encrytedPassword
				+ ", enabled=" + enabled + ", firstName=" + firstName + ", lastName=" + lastName + ", street=" + street
				+ ", town=" + town + ", email=" + email + ", phone=" + phone + "]";
	}
	
	
	
	
}
