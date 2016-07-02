package peer2peer.model;

import java.rmi.RemoteException;

/**
 * Created by Cyril on 30/06/2016.
 */
public interface Root extends Peer{

    String nextPeerName() throws RemoteException;

}
