package peer2peer.model;

import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashSet;
import java.util.Set;

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

        //TODO FileAlreadyExist ?
        String destination = this.directory + "/" + file.getName();
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(destination)));
        IBuffer bis = new BufferImpl(new FileInputStream(destination));

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
