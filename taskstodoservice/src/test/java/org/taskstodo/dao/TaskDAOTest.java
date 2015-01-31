package org.taskstodo.dao;

import java.util.List;

import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.taskstodo.common.Priority;
import org.taskstodo.common.TaskType;
import org.taskstodo.entity.FileUpload;
import org.taskstodo.entity.Keyword;
import org.taskstodo.entity.Note;
import org.taskstodo.entity.Task;
import org.taskstodo.entity.User;

@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=false)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testContext.xml")
public class TaskDAOTest {
    @Autowired
    SessionFactory sessionFactory;
    @Autowired
    ITaskDAO taskDAO;
    
    @Autowired
    IUserDAO userDAO;
    
    Task task;
    
    @Before
    public void setUp() throws Exception {
//        ThreadLocalSessionContext.bind(sessionFactory.getCurrentSession());
    	testGetSubtree();
    }

    @After
    public void tearDown() throws Exception {
    }

    
    
    @Transactional
    public void testGetSubtree() {
    	task = taskDAO.findByIdWithParent(4L);
    	while(task != null){
    		System.out.println(task.getTitle());
    		for (Keyword keyword : task.getKeywords()) {
				System.out.println(keyword.getValue());
			}
    		task = task.getParentTask();
    	}
//    	Task newTask = new Task();
//    	newTask = taskDAO.create(newTask);
//        Task taskChain = taskDAO.findRootTask(newTask.getTaskId());
//        System.out.println(taskChain.toString());
    }
    
    @Test
    public void testTask() {
    	Task task = this.task;
    	while(task != null){
    		System.out.println(task.getTitle());
    		task = task.getParentTask();
    	}
//    	Task newTask = new Task();
//    	newTask = taskDAO.create(newTask);
//        Task taskChain = taskDAO.findRootTask(newTask.getTaskId());
//        System.out.println(taskChain.toString());
    }
    
    @Ignore
    @Test
    @Transactional
    public void insertFirstDataLine() {
        
        User selci = new User("selci");
        selci.setPassword("1");
        Task task = new Task();
        task.setPriority(Priority.LOW);
//        task.setTaskType(TaskType.SEARCHTASK);
        task.setOwner(selci);
        selci.getTasks().add(task);
        
        Keyword selcuk = new Keyword("selcuk");
        Note note = new Note("Selcuk's note", "Selcuk's note content!");
        FileUpload file = new FileUpload();
        file.setTask(task);
        note.setTask(task);
        selcuk.setTask(task);
        task.getKeywords().add(selcuk);
        task.getFileUploads().add(file);
        task.getNotes().add(note);
        
        userDAO.create(selci);
        taskDAO.create(task);
        System.out.println(task.toString());
        User user = userDAO.findById(1L);
        System.out.println( user);
    }
    
//    @Test
    public void reassignNodes() {
        createDataConstellation();
        reassign();
    }
    @Ignore
    @Test
    @Transactional
    public void createDataConstellation() {
//        User selci = new User("selci");
//        selci.setPassword("1");
//        userDAO.create(selci);
//        
//        Task movedTask = new Task();
//        movedTask.setTitle("1");
//        movedTask.setTaskType(TaskType.WORKTASK);
//        movedTask.setPriority(Priority.LOW);
//        movedTask.setOwner(selci);
//        selci.getTasks().add(movedTask);
//        
//        Note noteMovedTask = new Note("ddd","fff");
//        noteMovedTask.setContent("Content");
//        noteMovedTask.setTask(movedTask);
//        movedTask.getNotes().add(noteMovedTask);
//        
//        Keyword keyword = new Keyword("KeywordOne");
//        keyword.setTask(movedTask);
//        movedTask.getKeywords().add(keyword);
//        
//        FileUpload fileUpload = new FileUpload();
//        fileUpload.setDescription("bls");
//        fileUpload.setTask(movedTask);
//        movedTask.getFileUploads().add(fileUpload);
//
//        taskDAO.create(movedTask);
//        
//        Task sourceTask = new Task();
//        sourceTask.setTitle("2");
//        sourceTask.setTaskType(TaskType.WORKTASK);
//        sourceTask.setPriority(Priority.MEDIUM);
//        sourceTask.setOwner(selci);
//        selci.getTasks().add(sourceTask);
//        
//        sourceTask.getChildTasks().add(movedTask);
//        movedTask.setParentTask(sourceTask);
//        taskDAO.create(sourceTask);
//        
//        Task destinationTask = new Task();
//        destinationTask.setTitle("3");
//        destinationTask.setTaskType(TaskType.WORKTASK);
//        destinationTask.setPriority(Priority.URGENT);
//        destinationTask.setOwner(selci);
//        selci.getTasks().add(destinationTask);
//       
//        taskDAO.create(destinationTask);
    }
    @Ignore
    @Transactional
    protected void reassign() {
        Task sourceTask = null;
        Task movedTask = null;
        Task destinationTask= null; 
        
        List<Task> tasks = null;
            Session session = null;
            tasks = session.createCriteria(Task.class)
                    .setFetchMode("childTasks", FetchMode.JOIN)
                    .list();
            session.close();

        //weak assumptions    
        for (Task task : tasks) {
            if(task.getPriority().equals(Priority.LOW)){
                movedTask = task;
            }
            if(task.getPriority().equals(Priority.MEDIUM)){
                sourceTask = task;
            }
            if(task.getPriority().equals(Priority.URGENT)){
                destinationTask = task;
            }
        }
        
        sourceTask.getChildTasks().remove(movedTask);
        destinationTask.getChildTasks().add(movedTask);
        movedTask.setParentTask(destinationTask);
        
        taskDAO.update(sourceTask);
        taskDAO.update(movedTask);
        taskDAO.update(destinationTask);
    }
}