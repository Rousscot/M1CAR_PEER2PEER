package peer2peer.model;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

public interface Peer extends Remote{

    void init(Root root) throws RemoteException;

    Map<String, Peer> getPeers() throws RemoteException;

    void register(Peer node) throws RemoteException;

    void unregister(String idNode) throws RemoteException;

    String[] getLocalFiles() throws RemoteException;

    String getPeerName() throws RemoteException;

    boolean rootIsConnected() throws RemoteException;

    IBuffer getBuffer(String fileName) throws IOException;

    void download(String fileName, String nodeName) throws IOException;
}
