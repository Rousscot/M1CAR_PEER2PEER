package peer2peer.model;

import java.io.File;
import java.rmi.RemoteException;
import java.util.Iterator;

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
    public boolean hasRoot() throws RemoteException {
        return true;
    }

    @Override
    public void unregister(Peer peer) throws RemoteException{
        //Do not replace with a forEach to avoid concurrency error
        Iterator<Peer> peerIterator = this.getPeers().iterator();
        while(peerIterator.hasNext()){
            Peer p = peerIterator.next();
            if(p != peer) {
                p.getPeers().remove(peer);
            }
        }
    }

}
