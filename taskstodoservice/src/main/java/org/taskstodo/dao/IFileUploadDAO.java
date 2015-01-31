package org.taskstodo.dao;

import org.taskstodo.entity.FileUpload;

public interface IFileUploadDAO extends IGenericDAO<FileUpload, Long>{

   
    public abstract FileUpload create(FileUpload fileUpload);

    public abstract FileUpload update(FileUpload fileUpload);

    public abstract FileUpload findById(Long fileUploadId);

    public abstract void remove(FileUpload fileUpload);
}
