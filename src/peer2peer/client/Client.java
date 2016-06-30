package peer2peer.client;

import peer2peer.model.Peer;
import peer2peer.model.PeerImpl;
import peer2peer.model.Root;
import peer2peer.gui.NodeUI;
import peer2peer.model.RootImpl;

import java.io.File;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

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

    public Peer createPeer(String url, File directory) throws MalformedURLException, RemoteException {

        try {
            Root root = (Root) Naming.lookup(url);
            return new PeerImpl(root, directory);
        } catch (NotBoundException e) {
            return this.createRoot(directory);
        }

    }

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
