package com.smart.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name="User")
public class User {
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public boolean getEnabled() {
		return enabled;
	}
	public void setEnabled(boolean b) {
		this.enabled = b;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public String getAbout() {
		return about;
	}
	public void setAbout(String about) {
		this.about = about;
	}
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
private int id;
@NotBlank(message="Name field is required!!")
@Size(min=2,max=50,message="Min 2 and Maximum 50 charcters are important.")
private String name;
@Column(unique=true)
private String email;
@NotBlank(message="Password field is required!!")
private String password;
private String role;
private boolean enabled;
private String imgUrl;
@Column(length=500)
private String about;
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="user")
private List<Contact> contacts=new ArrayList<>();
public User() {
	super();
	// TODO Auto-generated constructor stub
}
@Override
public String toString() {
	return "User [id=" + id + ", name=" + name + ", email=" + email + ", password=" + password + ", role=" + role
			+ ", enabled=" + enabled + ", imgUrl=" + imgUrl + ", about=" + about + ", contacts=" + contacts + "]";
}
public List<Contact> getContacts() {
	return contacts;
}
public void setContacts(List<Contact> contacts) {
	this.contacts = contacts;
}
}
