package peer2peer;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by sylvie on 17/06/16.
 */
public interface IBuffer extends Remote {

    byte[] lire(int tailleBuffer) throws IOException, RemoteException ;
    int available() throws IOException, RemoteException ;
    void close() throws IOException, RemoteException;

}
