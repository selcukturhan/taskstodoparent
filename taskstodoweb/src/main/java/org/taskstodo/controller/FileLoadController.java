package org.taskstodo.controller;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.taskstodo.service.IFileloadService;
import org.taskstodo.to.FileLoadTO;

@Controller
@RequestMapping("/load")
public class FileLoadController {
     
  public static final Logger logger = LoggerFactory.getLogger(FileLoadController.class);
  
  @Autowired
  private IFileloadService fileloadService;
  

  @RequestMapping(	value="/delete/{fileUploadId}",
		  			method=RequestMethod.DELETE,
          			consumes="application/json",
          			produces="application/json"
          			)
  public @ResponseBody void delete(@PathVariable Long fileUploadId) {
       fileloadService.delete(fileUploadId);
  }
  
  @RequestMapping( value="/upload/{taskId}",
          method=RequestMethod.POST,
          consumes="application/json",
          produces="application/json"
          )
    public @ResponseBody FileLoadTO upload(@PathVariable Long taskId, @RequestBody FileLoadTO fileUpload) {
      return fileloadService.create(taskId, fileUpload);
    }
  
  @RequestMapping(	value="/download/{fileId}",
		  			method=RequestMethod.GET)
  public void download(@PathVariable Long fileId, HttpServletResponse response){
      FileLoadTO fileLoadTO = fileloadService.getFileDownload(fileId);
      //TODO: rename to contenttype
      response.setContentType(fileLoadTO.getImage().getContentType());
      response.setContentLength(fileLoadTO.getImage().getBytes().length);
      try{
    	  FileCopyUtils.copy(fileLoadTO.getImage().getBytes(), response.getOutputStream());
      }catch(Exception  e){
    	  throw new RuntimeException(e);
      }
 }
  
  @ExceptionHandler(Exception.class)
  private Exception handleException(Exception exception){
      return exception;
  }
}