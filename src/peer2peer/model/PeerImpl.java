package peer2peer.model;

import peer2peer.BufferImpl;
import peer2peer.IBuffer;

import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PeerImpl extends UnicastRemoteObject implements Peer {

    protected String peerName;
    protected Peer root;
    protected File directory;
    protected Map<String, Peer> peers;

    public PeerImpl(Peer root, File directory) throws RemoteException {
        super();
        this.peers = new ConcurrentHashMap<>();
        this.directory = directory;
        this.init(root);
    }

    @Override
    public void init(Peer root) throws RemoteException{
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
    public void unregister(String idNode) throws RemoteException {
        peers.remove(idNode);
    }

    @Override
    public String[] getLocalFiles() throws RemoteException {
        File[] fList = directory.listFiles();
        String[] localFiles = new String[fList.length];

        for (int i=0;i<fList.length;i++){
           localFiles[i]=fList[i].getName();
        }
        return localFiles;
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
    public IBuffer getBuffer(String filename) throws IOException {
        FileInputStream fis = new FileInputStream(directory + "/" + filename);
        return new BufferImpl(fis);
    }

    @Override
    public void download(String filename, String peerName) throws IOException {

        File destination = new File(directory + "/" + filename);
        FileOutputStream fos=new FileOutputStream(destination);
        BufferedOutputStream bos = new BufferedOutputStream(fos);

        IBuffer bis =  this.getPeers().get(peerName).getBuffer(filename);

        int taillePaquets = 1000;

        while(bis.available() > 0)
        {
            if(bis.available() > taillePaquets) {
                bos.write(bis.lire(taillePaquets));
            } else {
                bos.write(bis.lire(bis.available()));
            }
            bos.flush();
        }
        bis.close();
    }

    public Boolean isRoot() {
        return false;
    }

}
