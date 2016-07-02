package peer2peer.model.buffers;

import java.io.*;

/**
 * I am a serializable buffer input stream.
 */
public class RmiBuffer extends BufferedInputStream implements Serializable{

    public RmiBuffer(FileInputStream in) {
        super(in);
    }

}
