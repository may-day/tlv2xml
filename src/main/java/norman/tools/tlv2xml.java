package norman.tools;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import norman.tools.bm.LV;
import norman.tools.bm.plugins.bm.BaumanFormatAdapter;
import norman.tools.bm.plugins.bm.XMLBaumanFormatAdapter;

public class tlv2xml {

	public static String [] knownVersions = new String[]{"tlvdef"};
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		if (args.length != 3){
			System.out.println("1. parameter must be  a tlv file, the 2nd the (to be created) xml file and the 3rd the BM Version to use for the xml file.");
		}else{
			FileInputStream fis=new FileInputStream (args[0]);
			convert( fis, args[1], knownVersions, args[2]);
			fis.close();
		}
	}
	
	public static void convert(InputStream xmlstream, String outfile, String [] defVersions, String outversion) throws Exception {
			OutputStream os;
			if ("/dev/null".equals(outfile))
			{
				os = new OutputStream() {
				    	public void write(int i) throws IOException {
				    }
				};
			}else
				os = new FileOutputStream(outfile);
			convert(xmlstream, os, defVersions, outversion);
			os.close();
	}

	public static void convert(InputStream xmlstream, OutputStream os, String [] defVersions, String outversion) throws Exception {
		LV lv = new LV();
		
		BaumanFormatAdapter bfa = new BaumanFormatAdapter ( true, //skipJunk,
				true, //read Text that has too many lines
				true, //skipUnknownLineTypes,
				true //useDummies
				, defVersions
				, ">" 
		);
		XMLBaumanFormatAdapter xbfa = new XMLBaumanFormatAdapter (defVersions);
		bfa.read ( xmlstream, lv);
		xbfa.write ( os, lv, outversion);
	}
}
