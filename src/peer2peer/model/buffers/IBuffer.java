package peer2peer.model.buffers;

import java.io.IOException;
import java.rmi.Remote;

/**
 * I am an interface to describe a Buffer used transfer a file.
 */
public interface IBuffer extends Remote {

    byte[] read(int size) throws IOException;

    int available() throws IOException;

    void close() throws IOException;

}
