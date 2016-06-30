package peer2peer.model;

import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class BufferImpl extends UnicastRemoteObject implements IBuffer {

    protected RmiBuffer bis;

    protected final static Integer BLOCKSIZE = 1000;

    public BufferImpl(FileInputStream in) throws RemoteException {
        this.bis = new RmiBuffer(in);
    }

    @Override
    public byte[] read(int size) throws IOException {
        byte[] buffer = new byte[size];
        bis.read(buffer, 0, size);
        bis.mark(size);
        return buffer;
    }

    @Override
    public int available() throws IOException {
        return bis.available();
    }

    @Override
    public void close() throws IOException {
        bis.close();
    }
}
