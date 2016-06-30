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
    public void init(Root root) throws RemoteException{
        this.root = root;
        this.peers = root.getPeers();

        peers.forEach( peer -> {
            try {
                peer.register(this);
            } catch (RemoteException e) {
                e.printStackTrace(); //TODO
            }
        });

        this.register(this);
        root.register(this);

    }

    public Set<Peer> getPeers() throws RemoteException {
        return this.peers;
    }

    @Override
    public void register(Peer peer) throws RemoteException {
        peers.add(peer);
    }

    @Override
    public void unregister(Peer peer) throws RemoteException {
        root.unregister(peer);
    }

    @Override
    public File[] getLocalFiles() throws RemoteException {
        return this.directory.listFiles();
    }

    public String getPeerName() throws RemoteException {
        if(this.peerName == null){
            //TODO this is bad :P
            this.peerName = "Peer" + (this.getPeers().size() - 1);
            return this.getPeerName();
        } else {
            return this.peerName;
        }
    }

    @Override
    public boolean hasRoot() throws RemoteException {
        return root.hasRoot();
    }

    @Override
    public IBuffer getBuffer(File file) throws IOException {
        return new BufferImpl(new FileInputStream(directory + "/" + file.getName()));
    }

    @Override
    public void download(File file, Peer peer) throws IOException {

        File destination = new File(directory + "/" + file.getName());
        FileOutputStream fos = new FileOutputStream(destination);
        BufferedOutputStream bos = new BufferedOutputStream(fos);

        IBuffer bis = peer.getBuffer(file);

        while(bis.available() > 0)
        {
            if(bis.available() > BufferImpl.BLOCKSIZE) {
                bos.write(bis.read(BufferImpl.BLOCKSIZE));
            } else {
                bos.write(bis.read(bis.available()));
            }
            bos.flush();
        }
        bis.close();
    }

}
