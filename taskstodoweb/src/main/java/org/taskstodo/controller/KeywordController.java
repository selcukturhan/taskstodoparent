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
import org.taskstodo.service.IKeywordService;
import org.taskstodo.to.KeywordTO;

@Controller
@RequestMapping("/keyword")
public class KeywordController {
    
  public static final Logger logger = LoggerFactory.getLogger(KeywordController.class);
  
  @Autowired
  private IKeywordService keywordService;
  
  @RequestMapping( value="/create/{taskId}",
                   method=RequestMethod.POST,
                   consumes="application/json",
                   produces="application/json"
                   )
  public @ResponseBody KeywordTO create(@PathVariable("taskId") Long taskId, @RequestBody KeywordTO keywordTO){
      return keywordService.create(keywordTO, taskId);
  }
  
  @RequestMapping( value="/delete/{keywordId}",
                   method=RequestMethod.DELETE,
                   consumes="application/json",
                   produces="application/json"
                   )
  public @ResponseBody void delete(@PathVariable("keywordId") Long keywordId){
      keywordService.delete(keywordId);
  }
  
  @RequestMapping( value="/update",
                   method=RequestMethod.PUT,
                   consumes="application/json",
                   produces="application/json"
                   )
   public @ResponseBody void update(@RequestBody KeywordTO keywordTO){
	  keywordService.update(keywordTO);
   }

  @RequestMapping(	value="/getSuggestionsForDomain/{domain}", 
			method=RequestMethod.GET,
			consumes="application/json",
			produces="application/json")
  public @ResponseBody List<String> getTypeahead(@PathVariable("domain") String domain){
	  return keywordService.getTypeahead(domain);
  }
  
  
  @ExceptionHandler(Exception.class)
  private Exception handleException(Exception exception){
      return exception;
  }
}