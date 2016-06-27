import java.rmi.RemoteException;

/**
 * TODO
 */
public class Root extends PeerImpl{


    protected Root() throws RemoteException {
        super(null, null);
        this.nodeName = "root";
    }

    @Override
    public Boolean isRoot() {
        return true;
    }

    @Override
    public Root getRoot(){
        return this;
    }

    public String nextNodeName(){
        return "Node" + getNodes().size() + 1;
    }
}
