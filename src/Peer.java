import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Set;

/**
 * TODO
 */
public interface Peer extends Remote {

    Set<Peer> getPeers() throws RemoteException;

    byte[] getFile(String fileName) throws RemoteException;

    Boolean isRoot() throws RemoteException;

    void addPeer(Peer peer) throws RemoteException, PeerAlreadyExistException;

    void init() throws RemoteException;

    String getNodeName() throws RemoteException;

}
