package peer2peer.model.peers;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Set;

/**
 * I am an interface to define the comportment of a Peer.
 */
public interface Peer extends Remote {

    /**
     * I init the Peer with a Root.
     * @param root the root of the community.
     * @throws RemoteException raised if there is a remote error.
     */
    void init(Root root) throws RemoteException;

    /**
     * I return the list of the Peers of the community.
     * @return The peers of the community.
     * @throws RemoteException raised if there is a remote error.
     */
    Set<Peer> getPeers() throws RemoteException;

    /**
     * I register a Peer to the community.
     * @param peer the peer to register.
     * @throws RemoteException raised if there is a remote error.
     */
    void register(Peer peer) throws RemoteException;

    /**
     * I unregister a Peer of the community.
     * @param peer the peer to unregister.
     * @throws RemoteException raised if there is a remote error.
     */
    void unregister(Peer peer) throws RemoteException;

    /**
     * I return the list of the files I can share.
     * @return the files I can share.
     * @throws RemoteException raised if there is a remote error.
     */
    File[] getLocalFiles() throws RemoteException;

    /**
     * I return the name of the peer.
     * @return the name of the peer.
     * @throws RemoteException raised if there is a remote error.
     */
    String getPeerName() throws RemoteException;

    /**
     * I check that the root is still connected.
     * @throws RemoteException raised if the root is disconnected.
     */
    void checkRoot() throws RemoteException;

    /**
     * I download a file of a peer of the community.
     * @param peer The peer containing the file.
     * @param file The file to download.
     * @throws IOException raised if there is a error during the reading/writing of the file.
     * @throws RemoteException raised if there is a remote error.
     * @throws FileAlreadyExistsException raised if I already poccess a file of this name.
     */
    void downloadFrom(Peer peer, File file) throws IOException;

}
