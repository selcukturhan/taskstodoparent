package org.taskstodo.service;

import org.taskstodo.to.FileLoadTO;

public interface IFileloadService {

    public void delete(Long fileUploadId);
    public FileLoadTO getFileDownload(Long fileId);
    public FileLoadTO create(Long taskId, FileLoadTO file);
}