package norman.tools.bm.plugins;

import norman.tools.bm.document.*;

public interface DocumentFormatAdapter {
    public void read(java.io.InputStream is, DocumentPart doc) throws Exception;
    public void write(java.io.OutputStream os, DocumentPart doc, String bmversion) throws Exception;
}
