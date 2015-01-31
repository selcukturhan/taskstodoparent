package org.taskstodo.entity;

import java.util.HashSet;
import java.util.Set;

public class User {

    private Long userId;

    /* The users firstname. */
    private String firstname;

    /* The users surname. */
    private String surname;

    /* The users username. */
    private String username;

    /* The users mail address. */
    private String password;

    /* The users mail address. */
    private String mail;
    
    private Set<Task> tasks = new HashSet<Task>();

    private Set<Role> roles = new HashSet<Role>();
    /**
     * Default constructor.
     */
    public User() {
    }

    // --

    public User(String username) {
        super();
        this.username = username;
    }

    

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public Set<Task> getTasks() {
        return tasks;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
//    public Set<String> getRoles(){
//        return new HashSet<String>(Arrays.asList("ROLE_USER", "ROLE_ADMIN"));
//    }


    
    public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public Set<Role> getRoles() {
		return roles;
	}
}
