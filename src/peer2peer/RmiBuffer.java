package peer2peer;

import java.io.*;

/**
 * Created by sylvie on 17/06/16.
 */
public class RmiBuffer extends BufferedInputStream implements Serializable{

    public RmiBuffer(FileInputStream in) {
        super(in);
    }

}
