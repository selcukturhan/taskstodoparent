package org.taskstodo.to;

import java.util.HashSet;
import java.util.Set;

public class UserTO {

    private Long id;

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

    private Set<RoleTO> roles = new HashSet<RoleTO>();
    
    /**
     * Default constructor.
     */
    public UserTO() {
    }

    // --

    public UserTO(String username) {
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<RoleTO> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleTO> roles) {
        this.roles = roles;
    }

}
