package org.taskstodo.service.impl;

import java.util.List;
import java.util.Set;
import java.util.HashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.taskstodo.dao.IUserDAO;
import org.taskstodo.entity.Role;

/**
 * Database user authentication service.
 */
@Component("restUserDetailsService")
public final class RestUserDetailsService implements UserDetailsService {

    @Autowired
    private IUserDAO userDAO;

    public static final Logger logger = LoggerFactory.getLogger(RestUserDetailsService.class);

    
    public RestUserDetailsService() {
        super();
    }

    // API - public

    /**
     * Loads the user from the datastore, by it's user name <br>
     */
    @Override
    public final UserDetails loadUserByUsername(final String username) {
    	//Consider to store md5 hash 
        final org.taskstodo.entity.User user = userDAO.findByName(username);
        if (user == null) {
            throw new UsernameNotFoundException("Username was not found: " + username);
        }
        Set<String> rolesOfUser = new HashSet<String>();
        for(Role role : user.getRoles()){
        	rolesOfUser.add(role.getTitle());
        }
        
        
        final String[] roleStringsAsArray = rolesOfUser.toArray(new String[rolesOfUser.size()]);
        final List<GrantedAuthority> auths = AuthorityUtils.createAuthorityList(roleStringsAsArray);

        return new User(user.getUsername(), user.getPassword(), auths);
    }

}
