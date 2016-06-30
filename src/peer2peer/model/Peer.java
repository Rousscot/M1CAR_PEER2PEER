package peer2peer.model;

import java.io.File;
import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Set;

public interface Peer extends Remote {

    void init(Root root) throws RemoteException;

    Set<Peer> getPeers() throws RemoteException;

    void register(Peer node) throws RemoteException;

    void unregister(Peer peer) throws RemoteException;

    File[] getLocalFiles() throws RemoteException;

    String getPeerName() throws RemoteException;

    boolean hasRoot() throws RemoteException;

    IBuffer getBuffer(File file) throws IOException;

    void download(File file, Peer peer) throws IOException;
}
