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
import org.taskstodo.service.ISearchItemService;
import org.taskstodo.service.ITaskService;
import org.taskstodo.to.SearchItemTO;
import org.taskstodo.to.TaskContextSearchResultTO;

import taskcontextsearch.IRankingService;

@Controller
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
	public @ResponseBody
	SearchItemTO create(@PathVariable("taskId") Long taskId,
			@RequestBody SearchItemTO searchItemTO) {
		return searchItemService.create(searchItemTO, taskId);
	}

	@RequestMapping(value = "/delete/{searchItemId}", method = RequestMethod.DELETE, consumes = "application/json", produces = "application/json")
	public @ResponseBody
	void delete(@PathVariable("searchItemId") Long searchItemId) {
		searchItemService.delete(searchItemId);
	}

	@RequestMapping(value = "/update", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
	public @ResponseBody
	void update(@RequestBody SearchItemTO searchItemTO) {
		searchItemService.update(searchItemTO);
	}

	@RequestMapping(value = "/search/{taskId}/{searchTerm}", method = RequestMethod.GET, consumes = "application/json", produces = "application/json")
	public @ResponseBody
	TaskContextSearchResultTO search(@PathVariable("taskId") long taskId, @PathVariable("searchTerm") String searchTerm) {
		return rankingService.getSearchResultForTerm(searchTerm, taskService.inmprofindByTaskId(taskId));
	}

	@ExceptionHandler(Exception.class)
	private Exception handleException(Exception exception) {
		return exception;
	}
}