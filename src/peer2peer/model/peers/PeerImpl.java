package peer2peer.model.peers;

import peer2peer.model.buffers.BufferImpl;
import peer2peer.model.buffers.IBuffer;

import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashSet;
import java.util.Set;

/**
 * I am an implementation of the Peer interface.
 */
public class PeerImpl extends UnicastRemoteObject implements Peer {

    protected String peerName;
    protected Root root;
    protected File directory;
    protected Set<Peer> peers;

    public PeerImpl(Root root, File directory) throws RemoteException {
        super();
        this.peers = new HashSet<>();
        this.directory = directory;
        this.init(root);
    }

    @Override
    public void init(Root root) throws RemoteException {
        this.root = root;
        this.register(this);
        this.peers = root.getPeers();
    }

    public Set<Peer> getPeers() throws RemoteException {
        return this.peers;
    }

    @Override
    public void register(Peer peer) throws RemoteException {
        this.root.register(peer);
    }

    @Override
    public void unregister(Peer peer) throws RemoteException {
        this.root.unregister(peer);
    }

    @Override
    public File[] getLocalFiles() throws RemoteException {
        return this.directory.listFiles();
    }

    public String getPeerName() throws RemoteException {
        if (this.peerName == null) {
            this.peerName = this.root.nextPeerName();
            return this.getPeerName();
        } else {
            return this.peerName;
        }
    }

    @Override
    public void checkRoot() throws RemoteException {
        root.checkRoot();
    }

    @Override
    public void downloadFrom(Peer peer, File file) throws IOException {

        File newFile = new File(this.directory + "/" + file.getName());

        if (newFile.exists()) {
            throw new FileAlreadyExistsException(file.getName());
        }

        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(newFile));
        IBuffer bis = new BufferImpl(new FileInputStream(newFile));

        while (bis.available() > 0) {
            byte[] toSend;
            if (bis.available() > BufferImpl.BLOCKSIZE) {
                toSend = bis.read(BufferImpl.BLOCKSIZE);
            } else {
                toSend = bis.read(bis.available());
            }
            bos.write(toSend);
            bos.flush();
        }
        bis.close();
    }

}
