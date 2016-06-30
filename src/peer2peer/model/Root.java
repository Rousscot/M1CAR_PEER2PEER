package peer2peer.model;

import java.io.File;
import java.rmi.RemoteException;

/**
 * Created by Cyril on 29/06/2016.
 */
public class Root extends PeerImpl {

    public Root(File directory) throws RemoteException {
        super(null, directory);
        this.peerName = "root";
    }

    @Override
    public void init(Peer root) throws RemoteException {
        super.init(this);
    }

    @Override
    public boolean rootIsConnected() throws RemoteException {
        return true;
    }

    @Override
    public Boolean isRoot(){
        return true;
    }



}
