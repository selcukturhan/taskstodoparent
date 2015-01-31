package org.taskstodo.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.taskstodo.service.IAdminService;
import org.taskstodo.to.RoleKeyValueTO;
import org.taskstodo.to.UserTO;

@Controller
@RequestMapping("/admin")
public class AdminController {
    
  public static final Logger logger = LoggerFactory.getLogger(AdminController.class);
  
  @Autowired
  private IAdminService adminService;
  
  @RequestMapping(	value="/findAll", 
          			method=RequestMethod.GET,
          			consumes="application/json",
          			produces="application/json"
          			)
  public @ResponseBody List<UserTO> findAll() {
      return adminService.findAll();
  }

  @RequestMapping(	value="/findAllRoles", 
          			method=RequestMethod.GET,
          			consumes="application/json",
          			produces="application/json"
          			)
  public @ResponseBody List<RoleKeyValueTO> findAllRoles(){
      return adminService.findAllRoles();
  }

  @RequestMapping(  value="findUserByUsername/{username}", 
	              	method=RequestMethod.GET,
          			consumes="application/json",
          			produces="application/json")
  public @ResponseBody UserTO findByUsername(@PathVariable String username){
      return adminService.findByUsername(username);
  }
  
  @RequestMapping(  value="/create",
		            method=RequestMethod.POST,
		            consumes="application/json",
		            produces="application/json"
                    )
  public @ResponseBody UserTO create(@RequestBody UserTO user){
      return adminService.create(user);
  }
 
  @RequestMapping(	value="/update",
		            method=RequestMethod.PUT,
          			consumes="application/json",
          			produces="application/json"
		            )
  public void update(@RequestBody UserTO user){
      adminService.update(user);
  }
  
  @RequestMapping(	value="/delete/{userId}", 
                    method=RequestMethod.DELETE,
          			consumes="application/json",
          			produces="application/json")
  public @ResponseBody void delete(@PathVariable Long userId){
      adminService.delete(userId);
  }

  @RequestMapping(	value="/addRoleToUser/{userId}/{roleId}", 
          			method=RequestMethod.POST,
          			consumes="application/json",
          			produces="application/json")
  public @ResponseBody void addRoleToUser(@PathVariable Long userId, @PathVariable Long roleId){
      adminService.addRoleToUser(userId, roleId);
  }
  
  @RequestMapping(	value="/deleteRoleFromUser/{userId}/{roleId}", 
			method=RequestMethod.DELETE,
			consumes="application/json",
			produces="application/json")
  public @ResponseBody void deleteRoleFromUser(@PathVariable Long userId, @PathVariable Long roleId){
      adminService.deleteRoleForUser(userId, roleId);
  }

  @ExceptionHandler(Exception.class)
  private Exception handleException(Exception exception){
      return exception;
  }
}
