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

	public static String [] knownVersions = new String[]{
			 "norman/tools/bm/plugins/bm/bauman_v347.def"
			,"norman/tools/bm/plugins/bm/bauman_v352.def"
			,"norman/tools/bm/plugins/bm/bauman_v367.def"
			,"norman/tools/bm/plugins/bm/bauman_v370.def"
			,"norman/tools/bm/plugins/bm/bauman_v373.def"
			,"norman/tools/bm/plugins/bm/bauman_v376.def"
			,"norman/tools/bm/plugins/bm/bauman_v379.def"
			,"norman/tools/bm/plugins/bm/bauman_v380.def"
			,"norman/tools/bm/plugins/bm/bauman_v384.def"
			,"norman/tools/bm/plugins/bm/bauman_v389.def"
			,"norman/tools/bm/plugins/bm/bauman_v392.def"
			,"norman/tools/bm/plugins/bm/bauman_v396.def"
			,"norman/tools/bm/plugins/bm/bauman_v397.def"
			,"norman/tools/bm/plugins/bm/bauman_v399.def"
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
			convert( new FileInputStream (args[0]), args[1]);
		}
	}
	
	public static void convert(InputStream xmlstream, String outfile) throws Exception {
			OutputStream os;
			if ("/dev/null".equals(outfile))
			{
				os = new OutputStream() {
				    	public void write(int i) throws IOException {
				    }
				};
			}else
				os = new FileOutputStream(outfile);
			convert(xmlstream, os);
	}

	public static void convert(InputStream xmlstream, OutputStream os) throws Exception {
		LV lv = new LV();
		BaumanFormatAdapter bfa = new BaumanFormatAdapter ( true, //skipJunk,
				true, //read Text that has too many lines
				true, //skipUnknownLineTypes,
				true //useDummies
				, knownVersions
		);
		XMLBaumanFormatAdapter xbfa = new XMLBaumanFormatAdapter (knownVersions);
		bfa.read ( xmlstream, lv);
		xbfa.write ( os, lv);
	}
}
