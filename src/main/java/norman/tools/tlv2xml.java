package norman.tools;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import norman.tools.bm.LV;
import norman.tools.bm.plugins.bm.BaumanFormatAdapter;
import norman.tools.bm.plugins.bm.XMLBaumanFormatAdapter;

public class tlv2xml {

	public static String [] knownVersions = new String[]{
			"norman/tools/bm/plugins/bm/bauman_v399.def"
			,"norman/tools/bm/plugins/bm/bauman_v404.def"
			,"norman/tools/bm/plugins/bm/bauman_v410.def"
			,"norman/tools/bm/plugins/bm/bauman_v412.def"
			,"norman/tools/bm/plugins/bm/bauman_v421.def"
			,"norman/tools/bm/plugins/bm/bauman_v422.def"
			,"norman/tools/bm/plugins/bm/bauman_v450.def"
			,"norman/tools/bm/plugins/bm/bauman_v452.def"
			,"norman/tools/bm/plugins/bm/bauman_v460.def"
			,"norman/tools/bm/plugins/bm/bauman_v470.def"
			,"norman/tools/bm/plugins/bm/bauman_v472.def"
			,"norman/tools/bm/plugins/bm/bauman_v474.def"
			,"norman/tools/bm/plugins/bm/bauman_v480.def"
			,"norman/tools/bm/plugins/bm/bauman_v482.def"
	};
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
					, knownVersions
			);
			XMLBaumanFormatAdapter xbfa = new XMLBaumanFormatAdapter (knownVersions);
			bfa.read ( new FileInputStream (args[0]), lv);
			xbfa.write ( new FileOutputStream(args[1]), lv);
		}
	}
	
	public static void convert(InputStream xmlstream, String outfile) throws Exception {
			LV lv = new LV();
			BaumanFormatAdapter bfa = new BaumanFormatAdapter ( true, //skipJunk,
					true, //read Text that has too many lines
					true, //skipUnknownLineTypes,
					true //useDummies
					, knownVersions
			);
			XMLBaumanFormatAdapter xbfa = new XMLBaumanFormatAdapter (knownVersions);
			bfa.read ( xmlstream, lv);
			xbfa.write ( new FileOutputStream(outfile), lv);
	}
}
