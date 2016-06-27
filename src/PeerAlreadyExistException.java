import java.rmi.RemoteException;

/**
 * TODO
 */
public class PeerAlreadyExistException extends Throwable {

    protected Peer peer;

    public Peer getPeer() {
        return peer;
    }

    public void setPeer(Peer peer) {
        this.peer = peer;
    }

    public PeerAlreadyExistException(Peer peer) {
        this.peer = peer;
    }

    @Override
    public String getMessage(){
        try {
            return "In Peer " + this.getPeer().getNodeName() + " " + super.getMessage();
        } catch (RemoteException e) {
            return super.getMessage();
        }
    }

}
