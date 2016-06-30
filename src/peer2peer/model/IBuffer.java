package peer2peer.model;

import java.io.IOException;
import java.rmi.Remote;

public interface IBuffer extends Remote {

    byte[] read(int size) throws IOException;

    int available() throws IOException;

    void close() throws IOException;

}
