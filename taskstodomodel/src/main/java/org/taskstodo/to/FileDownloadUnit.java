package org.taskstodo.to;


public class FileDownloadUnit {
    private byte[] image;
    private FileLoadTO fileUploadTO;
    
    public FileDownloadUnit() {
        super();
        // TODO Auto-generated constructor stub
    }

    public FileDownloadUnit(byte[] image, FileLoadTO fileUploadTO) {
        super();
        this.image = image;
        this.fileUploadTO = fileUploadTO;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public FileLoadTO getFileUploadTO() {
        return fileUploadTO;
    }

    public void setFileUploadTO(FileLoadTO fileUploadTO) {
        this.fileUploadTO = fileUploadTO;
    }
}
