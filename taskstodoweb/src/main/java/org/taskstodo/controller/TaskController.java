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
import org.taskstodo.common.NodeCombination;
import org.taskstodo.service.ITaskService;
import org.taskstodo.to.TaskTO;




@Controller
@RequestMapping("/task")
public class TaskController {
    
  public static final Logger logger = LoggerFactory.getLogger(TaskController.class);
  
  @Autowired
  private ITaskService taskService;
  
  @RequestMapping(	value="/findAll", 
          			method=RequestMethod.GET,
          			consumes="application/json",
          			produces="application/json")
  public @ResponseBody List<TaskTO> findAll(){
     return taskService.findAll();
  }
  
  @RequestMapping(	value="findByTaskId/{taskId}",
		  			method=RequestMethod.GET,
		  			consumes="application/json",
		  			produces="application/json")
  public @ResponseBody TaskTO findByTaskId(@PathVariable Long taskId){
      return taskService.findByTaskId(taskId);
  }
  
  @RequestMapping(  value="/createTask",
		            method=RequestMethod.POST,
		            consumes="application/json",
		            produces="application/json"
                    )
  //TODO: solve null value problem, when Node is first created
 
  public @ResponseBody TaskTO create(@RequestBody TaskTO task){
      return taskService.create(task);
  }
  
  @RequestMapping(	value="/updateTask",
		            method=RequestMethod.PUT,
		            consumes="application/json",
	          		produces="application/json"
		            )
  public @ResponseBody void update(@RequestBody TaskTO task){
      taskService.update(task);
  }
  
  @RequestMapping(	value="/reassignNode", 
		            method=RequestMethod.PUT,
	          		consumes="application/json",
	          		produces="application/json"
	          		)
  public @ResponseBody void reassignNode(@RequestBody NodeCombination nodeCombination){
      taskService.reassignNode(nodeCombination);
  }
  
  @RequestMapping( value="/deleteByTaskId/{taskId}", 
                   method=RequestMethod.DELETE,
                   consumes="application/json",
                   produces="application/json")
  public @ResponseBody void delete(@PathVariable Long taskId){
      taskService.delete(taskId);
  }
  
  @ExceptionHandler(Exception.class)
  private Exception handleException(Exception exception){
      return exception;
  }
}