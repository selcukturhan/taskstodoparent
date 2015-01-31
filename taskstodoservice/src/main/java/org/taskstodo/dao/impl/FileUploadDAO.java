package org.taskstodo.dao.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.taskstodo.dao.IFileUploadDAO;
import org.taskstodo.entity.FileUpload;

@Repository
@Transactional
public class FileUploadDAO extends GenericDAO<FileUpload, Long> implements IFileUploadDAO {
    public FileUploadDAO() {
        super(FileUpload.class);
    }
    
}
