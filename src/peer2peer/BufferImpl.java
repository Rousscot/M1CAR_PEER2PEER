package peer2peer;

import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by sylvie on 17/06/16.
 */
public class BufferImpl extends UnicastRemoteObject implements IBuffer {

    protected RmiBuffer bis;

    public BufferImpl(FileInputStream in) throws RemoteException {
        this.bis = new RmiBuffer(in);
    }

    @Override
    public byte[] lire(int tailleBuffer) throws IOException, RemoteException {
        byte[] buffer = new byte[tailleBuffer];
        bis.read(buffer, 0,tailleBuffer);
        bis.mark(tailleBuffer);
        return buffer;
    }

    @Override
    public int available() throws IOException, RemoteException {
        return bis.available();
    }

    @Override
    public void close() throws IOException, RemoteException {
        bis.close();
    }
}
