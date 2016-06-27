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
    protected Set<Peer> nodes;
    protected File directory;

    protected PeerImpl(Root root, File directory) throws RemoteException {
        super();
        this.root = root;
        this.directory = directory;
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

    public Root getRoot() {
        return this.root;
    }

    public void setRoot(Root root) {
        this.root = root;
    }

    public String getNodeName() {
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
    public Set<Peer> getNodes() {
        return this.nodes;
    }

    public void setNodes(Set<Peer> nodes) {
        this.nodes = nodes;
    }
}
