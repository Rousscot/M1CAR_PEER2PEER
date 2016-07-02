package peer2peer.model.peers;

import java.rmi.RemoteException;

/**
 * I am an interface for the special peer that is the root of the community.
 */
public interface Root extends Peer {

    /**
     * I return the name of the next peer.
     * @return the name of the next peer.
     * @throws RemoteException raised if there is a remote error.
     */
    String nextPeerName() throws RemoteException;

}
