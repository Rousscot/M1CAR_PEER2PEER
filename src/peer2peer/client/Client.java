package peer2peer.client;

import peer2peer.gui.NodeUI;
import peer2peer.model.peers.Peer;
import peer2peer.model.peers.PeerImpl;
import peer2peer.model.peers.Root;
import peer2peer.model.peers.RootImpl;

import java.io.File;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * I am a Client that will launch the peer 2 peer client.
 * If there is already a community, I will register to it. Else I will create the root of the community.
 */
public class Client {

    public Client(String url, String directoryPath) {

        File directory = new File(directoryPath);
        if (!directory.exists()) {
            this.log("Your pathname for the local folder is incorrect. The directory does not exist. Please correct the path.");
            System.exit(102);
        }

        try {
            new NodeUI(this.createPeer(url, directory));
        } catch (MalformedURLException e) {
            this.log("Bad URL. Please try again with a better argument.");
            System.exit(100);
        } catch (RemoteException e) {
            //TODO maybe this should not catch all the remote exception. We should catch it somewhere else to give better feedback to the user.
            this.log("There was an error during the lookup or the binding :(");
            System.exit(101);
        }

    }

    /**
     * I create a peer for the client. If there is no community I will create a root.
     * @param url The url of the root.
     * @param directory the directory to share.
     * @return The created peer.
     * @throws MalformedURLException raised if the url is bad.
     * @throws RemoteException raised if there is a problem during the lookup or binding of the root.
     */
    public Peer createPeer(String url, File directory) throws MalformedURLException, RemoteException {

        try {
            Root root = (Root) Naming.lookup(url);
            return new PeerImpl(root, directory);
        } catch (NotBoundException e) {
            return this.createRoot(directory);
        }

    }

    /**
     * I create a root for the community.
     * @param directory The directory to share.
     * @return The root created.
     * @throws RemoteException raised if there is an error during the binding.
     * @throws MalformedURLException raised if the url is bad.
     */
    public Root createRoot(File directory) throws RemoteException, MalformedURLException {
        Root root = new RootImpl(directory);
        Naming.rebind("peer2peer", root);
        return root;
    }

    /**
     * I am a simple method to log some messages in the stdout.
     *
     * @param message The message to log.
     */
    public void log(String message) {
        System.out.println("Client: " + message);
    }

}
