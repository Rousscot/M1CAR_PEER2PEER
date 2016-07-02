package peer2peer.model.peers;

import java.io.File;
import java.rmi.RemoteException;
import java.util.Iterator;

/**
 * I am an implementation of the Root interface.
 */
public class RootImpl extends PeerImpl implements Root {

    protected Integer peerCounter;

    public RootImpl(File directory) throws RemoteException {
        super(null, directory);
        this.peerName = "root";
        this.peerCounter = 0;
    }

    @Override
    public void init(Root root) throws RemoteException {
        super.init(this);
    }

    @Override
    public void checkRoot() throws RemoteException {
        //I am just a stupid method that will throw an error to a Peer if I was disconnected.
    }

    @Override
    public void register(Peer peer) throws RemoteException {
        this.peers.forEach(p -> {
            try {
                p.getPeers().add(this);
            } catch (RemoteException ignore) {

            }
        });
        this.peers.add(peer);
    }

    @Override
    public void unregister(Peer peer) throws RemoteException {
        //Do not replace with a forEach to avoid concurrency error
        Iterator<Peer> peerIterator = this.getPeers().iterator();
        while (peerIterator.hasNext()) {
            Peer p = peerIterator.next();
            if (p != peer) {
                p.getPeers().remove(peer);
            }
        }
    }

    @Override
    public String nextPeerName() throws RemoteException {
        this.peerCounter += 1;
        return "Peer" + this.peerCounter;
    }

}
