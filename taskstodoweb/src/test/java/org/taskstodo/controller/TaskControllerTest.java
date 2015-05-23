package org.taskstodo.controller;


import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.taskstodo.dao.ITaskDAO;
import org.taskstodo.dao.impl.TaskDAO;
import org.taskstodo.service.ITaskService;
import org.taskstodo.service.impl.TaskService;

import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


public class TaskControllerTest {

    public void shouldFindAllTasks() throws Exception {
        ITaskDAO mockDao = mock(TaskDAO.class);
        ITaskService mockService = mock(TaskService.class);
        TaskController controller = new TaskController(mockService);
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        mockMvc.perform(get("/spitter/register"))
                .andExpect(view().name("registerForm"));
    }
}