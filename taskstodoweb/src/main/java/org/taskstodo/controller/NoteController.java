package org.taskstodo.controller;

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
import org.taskstodo.service.INoteService;
import org.taskstodo.to.NoteTO;

@Controller
@RequestMapping("/note")
public class NoteController {
    
  public static final Logger logger = LoggerFactory.getLogger(NoteController.class);
  
  @Autowired
  private INoteService noteService;
  
  @RequestMapping( value="/create/{taskId}",
                   method=RequestMethod.POST,
                   consumes="application/json",
                   produces="application/json"
                   )
  public @ResponseBody NoteTO create(@PathVariable("taskId") Long taskId, @RequestBody NoteTO note){
     return noteService.create(note, taskId);
  }
   
  @RequestMapping( value="/delete/{noteId}",
                   method=RequestMethod.DELETE,
                   consumes="application/json",
                   produces="application/json"
                   )
  public @ResponseBody void delete(@PathVariable("noteId") Long noteId){
      noteService.delete(noteId);
  }
  
  @RequestMapping( value="/update",
                   method=RequestMethod.PUT,
                   consumes="application/json",
                   produces="application/json"
                   )
  public @ResponseBody void update(@RequestBody NoteTO noteTO){
	  noteService.update(noteTO);
  }
  
  @ExceptionHandler(Exception.class)
  private Exception handleException(Exception exception){
      return exception;
  }
}