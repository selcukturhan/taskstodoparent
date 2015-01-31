package org.taskstodo.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.taskstodo.dao.IFileUploadDAO;
import org.taskstodo.dao.ITaskDAO;
import org.taskstodo.entity.FileUpload;
import org.taskstodo.entity.Task;
import org.taskstodo.service.IFileloadService;
import org.taskstodo.to.FileLoadTO;
import org.taskstodo.to.ImageTO;

@Service(value = "FileloadService")
public class FileloadService extends AbstractService implements IFileloadService {
    @Autowired
    private IFileUploadDAO fileUploadDAO;
    @Autowired
    private ITaskDAO taskDAO;
    @Autowired
    private IDataSink<String> dataSink;
    
    
    public static final Logger logger = LoggerFactory.getLogger(FileloadService.class);

    @Override
    public FileLoadTO create(Long taskId, FileLoadTO file) {
        FileUpload fileUpload = null;
        if (!"".equals(file.getTitle())) {
            try {
                //TODO: Filenaming
                String uri = dataSink.writeData(file.getImage().getBytes(), file.getFilename());
                
                fileUpload = new FileUpload();
                fileUpload.setUri(uri);
                fileUpload.setTitle(file.getTitle());
                fileUpload.setDescription(file.getImage().getContentType());
                
                Task owningTask = taskDAO.findById(taskId);
                fileUpload.setTask(owningTask);
                owningTask.getFileUploads().add(fileUpload);
                fileUploadDAO.create(fileUpload);
                
            } catch (Exception e) {
                logger.error(e.getLocalizedMessage());
            }
        }
        return mapEntity2TO(FileLoadTO.class, fileUpload);
    }

    public FileLoadTO getFileDownload(Long fileId){
        FileLoadTO fileLoadTO = new FileLoadTO();
        FileUpload fileUpload = null;
        fileUpload = fileUploadDAO.findById(fileId);
        try{
            byte[] image = dataSink.readData(fileUpload.getUri());
            mapEntity2TO(FileLoadTO.class, fileUpload);
            fileLoadTO.setImage(new ImageTO(image,fileUpload.getDescription()));
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }
        return fileLoadTO;
    }
    
    public void delete(Long fileUploadId){
        FileUpload fileUpload = fileUploadDAO.findById(fileUploadId);
        fileUploadDAO.remove(fileUpload);
        dataSink.delete(fileUpload.getUri());
    }
}