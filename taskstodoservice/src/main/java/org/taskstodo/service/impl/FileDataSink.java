package org.taskstodo.service.impl;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;


@Service(value="FileDataSink")
public class FileDataSink implements IDataSink<String> {

//    TODO:
//    @Value("${fileload.targetPath}")
    private String targetPath = "/Users/selcukturhan/Desktop/uploads"; 
    
    public static final Logger logger = LoggerFactory.getLogger(FileDataSink.class);

    
    @Override
    public void delete(String uri) {
        try {
            new File(uri).delete();
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }
    }

    @Override
    public byte[] readData(String uri) throws IOException {
        File file = new File(uri);
        if (file != null && file.exists()) {
            return FileCopyUtils.copyToByteArray(file);
        }
        return null;  
    }

    @Override
    public String writeData(byte[] data, String uri) throws IOException {
        final String computedUri = targetPath + "/" +uri;
        FileCopyUtils.copy(data, new File(computedUri));
        return computedUri;
    }

    public String getTargetPath() {
        return targetPath;
    }

    public void setTargetPath(String targetPath) {
        this.targetPath = targetPath;
    }

   
}
