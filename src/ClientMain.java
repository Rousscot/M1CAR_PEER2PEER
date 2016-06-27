/**
 * TODO
 */
public class ClientMain {

    public static void main(String[] args) {

        String url = "rmi://localhost/"+args[0];
        String directoryPath = args[1];

        Client client = new Client(url, directoryPath);
    }

}
