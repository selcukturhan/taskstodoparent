package org.taskstodo.to;

public class ImageTO {
    
    private byte[] bytes = null;
    private String contentType = null;

    
    
    public ImageTO() {
        super();
    }

    public ImageTO(byte[] bytes, String contentType) {
        super();
        this.bytes = bytes;
        this.contentType = contentType;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

}