import javax.rmi.PortableRemoteObject;
import java.io.File;
import java.rmi.RemoteException;
import java.util.Set;

/**
 * TODO
 */
public class PeerImpl extends PortableRemoteObject implements Peer {

    protected String nodeName;
    protected Root root;
    protected Set<Peer> peers;
    protected File directory;

    protected PeerImpl(Root root, File directory) throws RemoteException {
        super();
        this.setRoot(root);
        this.setDirectory(directory);
        this.init();
    }

    public void init() throws RemoteException {

        this.setPeers(this.getRoot().getPeers());

        this.getPeers().forEach(peer -> {
            try {
                peer.addPeer(this);
            } catch (RemoteException | PeerAlreadyExistException e) {
                //TODO
                e.printStackTrace();
            }
        });

        try {
            this.getRoot().addPeer(this);
        } catch (PeerAlreadyExistException e) {
            //TODO
            e.printStackTrace();
        }
    }

    @Override
    public byte[] getFile(String fileName) {

        //TODO

        return new byte[0];
    }

    @Override
    public Boolean isRoot() {
        return false;
    }

    @Override
    public void addPeer(Peer peer) throws RemoteException, PeerAlreadyExistException {
        if(this.getPeers().contains(peer)){
            throw new PeerAlreadyExistException(peer);
        }
        this.getPeers().add(peer);
    }

    public Root getRoot() {
        return this.root;
    }

    public void setRoot(Root root) {
        this.root = root;
    }

    public String getNodeName() throws RemoteException {
         if(this.nodeName == null){
             this.setNodeName(this.root.nextNodeName());
             return this.nodeName;
         } else {
             return this.nodeName;
         }
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    @Override
    public Set<Peer> getPeers() throws RemoteException{
        return this.peers;
    }

    public void setPeers(Set<Peer> peers) {
        this.peers = peers;
    }

    public File getDirectory() {
        return directory;
    }

    public void setDirectory(File directory) {
        this.directory = directory;
    }
}
