package peer2peer.model;

import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PeerImpl extends UnicastRemoteObject implements Peer {

    protected String peerName;
    protected Root root;
    protected File directory;
    protected Map<String, Peer> peers;

    public PeerImpl(Root root, File directory) throws RemoteException {
        super();
        this.peers = new ConcurrentHashMap<>();
        this.directory = directory;
        this.init(root);
    }

    @Override
    public void init(Root root) throws RemoteException{
        this.root = root;
        this.peers = root.getPeers();

        peers.forEach((k, v) -> {
            try {
                v.register(this);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });

        this.register(this);
        root.register(this);

    }

    public Map<String, Peer> getPeers() throws RemoteException {
        return this.peers;
    }

    @Override
    public void register(Peer peer) throws RemoteException {
        if(!peers.containsValue(peer)){
            peers.put(peer.getPeerName(),peer);
        }
        //TODO later I should raise an error here if the peer is already in the collection
    }

    @Override
    public void unregister(Peer peer) throws RemoteException {
        peers.remove(peer.getPeerName(), peer); //TODO
    }

    @Override
    public File[] getLocalFiles() throws RemoteException {
        return this.directory.listFiles();
    }

    public String getPeerName() throws RemoteException {
        if(this.peerName == null){
            //TODO this is bad :P
            this.peerName = "Peer" + this.getPeers().size();
            return this.getPeerName();
        } else {
            return this.peerName;
        }
    }

    @Override
    public boolean rootIsConnected() throws RemoteException {
        return root.rootIsConnected();
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
