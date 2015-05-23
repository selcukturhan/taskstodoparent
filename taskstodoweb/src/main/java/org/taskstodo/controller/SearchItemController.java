package org.taskstodo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.taskstodo.service.ISearchItemService;
import org.taskstodo.service.ITaskService;
import org.taskstodo.to.SearchItemTO;
import org.taskstodo.to.TaskContextSearchResultTO;

import taskcontextsearch.IRankingService;

@RestController
@RequestMapping("/searchitem")

public class SearchItemController {

    public static final Logger logger = LoggerFactory
            .getLogger(SearchItemController.class);

    @Autowired
    private ISearchItemService searchItemService;
    @Autowired
    private IRankingService rankingService;
    @Autowired
    private ITaskService taskService;

    @RequestMapping(value = "/create/{taskId}", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public SearchItemTO create(@PathVariable("taskId") Long taskId,
                               @RequestBody SearchItemTO searchItemTO) {
        return searchItemService.create(searchItemTO, taskId);
    }

    @RequestMapping(value = "/delete/{searchItemId}", method = RequestMethod.DELETE, consumes = "application/json")
    public ResponseEntity delete(@PathVariable("searchItemId") Long searchItemId) {
        searchItemService.delete(searchItemId);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT, consumes = "application/json")
    public ResponseEntity update(@RequestBody SearchItemTO searchItemTO) {
        searchItemService.update(searchItemTO);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/search/{taskId}/{searchTerm}", method = RequestMethod.GET, consumes = "application/json", produces = "application/json")
    public TaskContextSearchResultTO search(@PathVariable("taskId") long taskId, @PathVariable("searchTerm") String searchTerm) {
        return rankingService.getSearchResultForTerm(searchTerm, taskService.inmprofindByTaskId(taskId));
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Exception handleException(Exception exception) {
        return exception;
    }
}