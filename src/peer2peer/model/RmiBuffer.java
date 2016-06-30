package peer2peer.model;

import java.io.*;

public class RmiBuffer extends BufferedInputStream implements Serializable{

    public RmiBuffer(FileInputStream in) {
        super(in);
    }

}
