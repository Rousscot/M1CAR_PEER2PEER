package peer2peer.model;

import java.io.File;
import java.rmi.RemoteException;

/**
 * Created by Cyril on 29/06/2016.
 */
public class RootImpl extends PeerImpl implements Root {

    public RootImpl(File directory) throws RemoteException {
        super(null, directory);
        this.peerName = "root";
    }

    @Override
    public void init(Root root) throws RemoteException {
        super.init(this);
    }

    @Override
    public boolean rootIsConnected() throws RemoteException {
        return true;
    }

}
