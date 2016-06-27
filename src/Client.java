import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * TODO
 */
public class Client {

    public Client(String url, String directoryPath){

        try {
            Root root = this.getRoot(url);
            File directory = new File(directoryPath);
            if(!directory.exists()){
                throw new FileNotFoundException();
            }
            Peer peer = new PeerImpl(root, directory);
            this.log("Peer created.");
        } catch (MalformedURLException e) {
            this.log("Bad URL. Please try again with a better argument.");
            System.exit(100);
        } catch (RemoteException e) {
            this.log("There was an error during the lookup :(");
            System.exit(101);
        } catch (FileNotFoundException e) {
            //TODO check that we do not get this for the root.
            this.log("Your pathname for the local folder is incorrect. Please correct the path.");
            System.exit(102);
        }

    }

    public Root getRoot(String url) throws MalformedURLException, RemoteException {
        try {
            return (Root) Naming.lookup(url);
        } catch (NotBoundException e) {
            return this.createRoot();
        }
    }

    public Root createRoot() throws MalformedURLException, RemoteException {
        Root root = new Root();
        Naming.rebind("peer2peer", root);
        this.log("Root registered.");
        return root;
    }

    public void log(String message) {
        System.out.println("Client: " + message);
    }

}
