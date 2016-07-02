package peer2peer;

import peer2peer.client.Client;

/**
 * I am a main class to launch a peer to peer client. I take two parameters.
 * The first one is the URL of the Server (Example: `localhost`).
 * The second one is the directory you want to share (Example: `/Users/Cyril/rmiFolder` or `/home/rmiFolder`).
 */
public class ClientMain {

    public static void main(String[] args) {
        if ((args.length) != 2) {
            System.out.println("Sorry but I take two arguments in parameter.");
            System.out.println("The first one is the URL of the Server (Example: `localhost`).");
            System.out.println("The second one is the directory you want to share (Example: `/Users/Cyril/rmiFolder` or `/home/rmiFolder`).");
        } else {
            String url = "rmi://" + args[0] + "/peer2peer";
            String directoryPath = args[1];
            new Client(url, directoryPath);
        }
    }

}
