package peer2peer.model.buffers;

import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * I am an implementation of an IBuffer.
 * In fact I am just a decorator of a Buffer Input Stream that is serializable for RMI.
 */
public class BufferImpl extends UnicastRemoteObject implements IBuffer {

    public final static Integer BLOCKSIZE = 4000;
    protected RmiBuffer bis;

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
