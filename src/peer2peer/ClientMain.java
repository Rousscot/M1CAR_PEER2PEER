package peer2peer;

import peer2peer.client.Client;

/**
 * Created by Cyril on 28/06/2016.
 */
public class ClientMain {

    public static void main(String[] args) {

        String url = "rmi://"+args[0];
        String directoryPath = args[1];

        Client client = new Client(url, directoryPath);
    }

}
