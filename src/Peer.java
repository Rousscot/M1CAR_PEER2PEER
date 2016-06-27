import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Set;

/**
 * TODO
 */
public interface Peer extends Remote {

    Set<Peer> getNodes() throws RemoteException;

    byte[] getFile(String fileName);

    Boolean isRoot();



}
