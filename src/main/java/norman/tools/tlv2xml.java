package norman.tools;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import norman.tools.bm.LV;
import norman.tools.bm.plugins.bm.BaumanFormatAdapter;
import norman.tools.bm.plugins.bm.XMLBaumanFormatAdapter;

public class tlv2xml {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		if (args.length != 2){
			System.out.println("1. parameter must be  a tlv file and 2nd the (to be created) xml file.");
		}else{
			LV lv = new LV();
			BaumanFormatAdapter bfa = new BaumanFormatAdapter ( true, //skipJunk,
					true, //read Text that has too many lines
					true, //skipUnknownLineTypes,
					true //useDummies
			);
			XMLBaumanFormatAdapter xbfa = new XMLBaumanFormatAdapter ();
			bfa.read ( new FileInputStream (args[0]), lv);
			xbfa.write ( new FileOutputStream(args[1]), lv);
		}
	}

}
