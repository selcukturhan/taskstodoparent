package org.taskstodo.service.impl;

import java.io.IOException;

public interface IDataSink<T> {
    public void delete(T uri);
    public byte[] readData(T uri) throws IOException;
    public String writeData(byte[] data, String uri) throws IOException;
}
