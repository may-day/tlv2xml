package norman.tools.bm.plugins.bm;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import norman.tools.bm.DocumentException;
import norman.tools.bm.DocumentPartException;
import norman.tools.bm.JBMDocumentNames;
import norman.tools.bm.document.AbstractDocumentPart;
import norman.tools.bm.document.DocumentPart;
import norman.tools.bm.document.DocumentPartMissingException;
import norman.tools.bm.document.PropertyMissingException;
import norman.tools.bm.plugins.FormatAdapterException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

class BaumanFormatVersion implements Comparable<BaumanFormatVersion>{
	static Pattern markeridx = Pattern.compile("\\D*(\\d*)");
	public String valid_for, valid_ql, valid_fieldname;
	public int valid_fieldindex;
	public HashMap<Pattern, String> qline_partname;
	public HashMap<Pattern, String[]> qline_linedef;
	public HashMap<String, HashMap<String, String>> qline_partname_para;
	public BaumanFormatVersion(BufferedReader def) throws IOException{
		qline_partname= new HashMap<Pattern, String>();
		qline_linedef= new HashMap<Pattern, String[]>();
		qline_partname_para = new HashMap<>();
		valid_fieldindex=0;
		String l;
		while ((l=def.readLine())!=null){
			if (l.startsWith("@VALID:")){
				String parts[] = l.split(":",2)[1].split(",");
				valid_for = parts[0];
				valid_ql = parts[1];
				valid_fieldname = parts[2];
			}
			else if (l.startsWith("!")){
				String parts[] = l.split(":",3);
				String partname = parts[0].substring(1);
				for(String ql: parts[1].split(",")){
					qline_partname.put(Pattern.compile(ql), partname);
				}
				if (parts.length == 3){
				  var para=parts[2];
				  for(String p :para.split(";")){
					var pparts=p.split("=");
					var pname=pparts[0];
					var pval=pparts[1];
					if (!qline_partname_para.containsKey(pname)){
						qline_partname_para.put(partname, new HashMap<>());
					}
					qline_partname_para.get(partname).put(pname, pval);
				  }
				}
			}
			else if (l.startsWith("#")){
				String parts[] = l.split(",", 2);
				String ql = parts[0];
				if (parts.length>1){
					parts = parts[1].split(",");
				}else{
					parts = new String[]{};
				}
				Pattern pat=Pattern.compile(ql);
				qline_linedef.put(pat, parts);
				if (pat.matcher(valid_ql).matches()){
					valid_fieldindex=1;
					for(String field:parts){
						if (field.equals(valid_fieldname)){
							break;
						}
						valid_fieldindex++;
					}
				}
			}
		}
	}
	String getPartname(String marker){
		// System.out.println("getPartname by marker: " + marker);
		for(Pattern pat:qline_partname.keySet()){
			if (pat.matcher(marker).matches()){
				String partname= qline_partname.get(pat);
				if (partname.contains("%1$")){
					Matcher m = markeridx.matcher(marker);
					if (m.matches()){
						String markernum=m.group(1);
						if (markernum.length()>0){
							var offset = 0;
							if (qline_partname_para.containsKey(partname)){
								var paras = qline_partname_para.get(partname);
								if (paras.containsKey("offset")){
									offset = Integer.valueOf(paras.get("offset"));
								}
							}
							return String.format(partname, Integer.valueOf(markernum) + offset);
						}
					}
				}
				return partname;
			}
		}
		return null;
	}
	
	String [] getLinedef(String marker){
		
		for(Pattern pat:qline_linedef.keySet()){
			if (pat.matcher(marker).matches()){
				return qline_linedef.get(pat);
			}
		}
		return null;
	}
	
	int index(String[] fields, String fieldname){
		int idx = 0;
		for (String fname:fields){
			++idx;
			if (fname.equals(fieldname)) return idx;
		}
		return -1;
	}
	
	@Override
	public int compareTo(BaumanFormatVersion arg0) {
		return this.valid_for.compareToIgnoreCase(arg0.valid_for);
	}
}

class ParseContext {

	public int lineCounter;
	public int lastCounter, infiniLoop;
	
	public String pushbackLine;
	public DocumentPart lastDocPart;
	public int expectTextLines;
	public int expectShortTextLines;
	public String requestDocPart, requestField;
	public String lastOZ;
	boolean usePushbackLine;
	String fallbackVersion;
	
	public BaumanFormatVersion bmfv;
	public ArrayList<BaumanFormatVersion> bmversions;
	BufferedReader reader;
	BufferedWriter writer;
	public DocumentPart doc;
	public StringBuffer line;
	
	ParseContext(ArrayList<BaumanFormatVersion> bmversions, String fallbackVersion, DocumentPart doc, BufferedReader reader, BufferedWriter writer){
		this.bmversions = bmversions;
		this.doc = doc;
		this.reader = reader;
		this.writer = writer;
		this.fallbackVersion = fallbackVersion; 
		usePushbackLine = false;
		bmfv = null;
		lineCounter = 0;
		lastCounter = infiniLoop = 0;
		lastProcessedPosition (null, -1, -1);
		
	}
	public ParseContext(ArrayList<BaumanFormatVersion> bmversions, String fallbackVersion, DocumentPart doc, BufferedReader reader){
		this(bmversions, fallbackVersion, doc, reader, null);
	}
	
	public ParseContext(ArrayList<BaumanFormatVersion> bmversions, String fallbackVersion, DocumentPart doc, BufferedWriter writer){
		this(bmversions, fallbackVersion, doc, null, writer);
	}
	
	public void lastProcessedPosition (DocumentPart docPart, int countTextLinesAhead, int countShortTextLinesAhead)
	{
		lastDocPart = docPart;
		expectTextLines = countTextLinesAhead;
		expectShortTextLines = countShortTextLinesAhead;
	}

	public String nextLine () throws BMFormatException {
		String line;

		if (usePushbackLine) {
			usePushbackLine = false;
			line = pushbackLine;
		}
		else
		{
			try {
				line = reader.readLine();

				if (line != null)
					lineCounter++;
			} catch (IOException exc) {
				line = null;
			}
		}
		if (lastCounter == lineCounter) {
			infiniLoop++;
			if (infiniLoop > 10)
			{
				//String[] parts = line.split(AbstractBaumanAdapter.FIELDSEPARATOR,-1);
				//System.out.println(line+"\n");
				//for (int i=0; i < parts.length; i++)
				//	System.out.println(i+".: " + parts[i]+"\n");
				throw new BMFormatException ("infiniloop at line" + lineCounter);
			}
		}
		else {
			lastCounter = lineCounter;
			infiniLoop = 0;
		}
		//	System.out.println ("line >" + lineCounter + "<");
		String[] parts=null;
		if (bmfv == null){
			parts = line.split(AbstractBaumanAdapter.FIELDSEPARATOR,-1);
			for(BaumanFormatVersion v:bmversions){
				if (v.valid_ql.equals(parts[0])){
					if (v.valid_for.equals(parts[v.valid_fieldindex])){
						bmfv = v;
						break;
					}
				}
			}
		}
		if (bmfv == null){
			String bmversion="???";
			if (parts != null){
				bmversion = parts[1];
				if (fallbackVersion != null && !fallbackVersion.isEmpty()){
					ArrayList<BaumanFormatVersion> filtered = new ArrayList<BaumanFormatVersion>();
					for(BaumanFormatVersion v:bmversions){
						if (fallbackVersion.equalsIgnoreCase("<") && v.valid_for.compareToIgnoreCase(bmversion) < 0) filtered.add(v);
						else if (fallbackVersion.equalsIgnoreCase(">") && v.valid_for.compareToIgnoreCase(bmversion) > 0) filtered.add(v);
						else if (fallbackVersion.equalsIgnoreCase(v.valid_for)) filtered.add(v);
					}
					if (filtered.size()>0){
						filtered.sort(null);
						if (fallbackVersion.equalsIgnoreCase("<")) Collections.reverse(filtered);
						bmfv = filtered.get(0);
						
					}
					
				}
			}
			
			if (bmfv == null){
				throw new BMFormatException("No valid Baumanager Format Definition found for:" + bmversion);
			}
		}
		
		return line;
	}

	public void pushBack (String line) {
		usePushbackLine = true;
		pushbackLine = line;
	}

	public void writeln (String line) throws IOException
	{
		//System.out.println (line);
		writer.write (line);
		//writer.flush();
		lineCounter++;
	}

}

public abstract class AbstractBaumanAdapter {
    static final String FIELDSEPARATOR = ";";
    static final char LINETYPEMARKER = '#';
    static final String LINESEPARATOR = "\r\n";

    /* Kopfdatenmarker */
    static final String k01 = "#K01";
    static final String k02 = "#K02";
    static final String k03 = "#K03";
    static final String k04 = "#K04";
    static final String k05 = "#K05";
    static final String k06 = "#K06";
    static final String k07 = "#K07";
    static final String k08 = "#K08";
    static final String k09 = "#K09";
    static final String k10 = "#K10";
    static final String k11 = "#K11";
    static final String k12 = "#K12";
    static final String k13 = "#K13";

    /* Kalkblatt diverse Faktoren, Zu-und Abschläge*/
    static final String a1 = "#A1 ";
    static final String a2 = "#A2 ";
    static final String a3 = "#A3 ";
    static final String a4 = "#A4 ";
    /* Materialgruppenkalk */
    static final String a01 = "#A01";
    static final String a02 = "#A02";
    static final String a03 = "#A03";
    static final String a04 = "#A04";
    static final String a05 = "#A05";
    static final String a06 = "#A06";
    static final String a07 = "#A07";
    static final String a08 = "#A08";
    static final String a09 = "#A09";
    static final String a10 = "#A10";
    static final String a11 = "#A11";
    static final String a12 = "#A12";
    static final String a13 = "#A13";
    static final String a14 = "#A14";
    static final String a15 = "#A15";
    static final String a16 = "#A16";
    static final String a17 = "#A17";
    static final String a18 = "#A18";
    static final String a19 = "#A19";
    /* Lohngruppenkalk */
    static final String a21 = "#A21";
    static final String a22 = "#A22";
    static final String a23 = "#A23";
    static final String a24 = "#A24";
    static final String a25 = "#A25";
    static final String a26 = "#A26";
    static final String a27 = "#A27";
    static final String a28 = "#A28";
    static final String a29 = "#A29";
    static final String a30 = "#A30";
    static final String a31 = "#A31";
    static final String a32 = "#A32";
    static final String a33 = "#A33";
    static final String a34 = "#A34";
    static final String a35 = "#A35";
    static final String a36 = "#A36";
    static final String a37 = "#A37";
    static final String a38 = "#A38";
    static final String a39 = "#A39";
    static final String a40 = "#A40";
    static final String a41 = "#A41";
    static final String a42 = "#A42";
    static final String a43 = "#A43";
    static final String a44 = "#A44";
    static final String a45 = "#A45";
    static final String a46 = "#A46";
    static final String a47 = "#A47";
    static final String a48 = "#A48";
    static final String a49 = "#A49";
    static final String a50 = "#A50";
    static final String a51 = "#A51";
    static final String a52 = "#A52";
    static final String a53 = "#A53";
    static final String a54 = "#A54";


    /* Text Projekt */
    static final String tpx = "#TPX";
    /* Text T1 */
    static final String tt1 = "#TT1";
    /* Text T2 */
    static final String tt2 = "#TT2";
    /* Text T3 */
    static final String tt3 = "#TT3";
    /* Text TE */
    static final String tte = "#TTE";
    /* Text Briefkopf */
    static final String tbk = "#TBK";
    /* Text Firmenstempel */
    static final String tfs = "#TFS";
    /* Text Firmenstempel */
    static final String efb = "#EFB";
    /* Positionsarten */
    static final String pp = "#PP"; // P-Position
    static final String pi = "#PI"; // I-Position
    static final String pa = "#PA"; // A-Position
    static final String pe = "#PE"; // E-Position
    static final String pm = "#PM"; // M-Position
    static final String pu = "#PU"; // U-Position
    static final String pq = "#PQ"; // Q-Position
    static final String pr = "#PR"; // R-Position
    static final String ps = "#PS"; // S-Position
    static final String pz = "#PZ"; // Z-Position
    static final String p1 = "#P1"; // 1-Position
    static final String p2 = "#P2"; // 2-Position
    static final String p3 = "#P3"; // 3-Position
    static final String p4 = "#P4"; // 4-Position
    static final String px = "#PX"; // X-Position
    static final String py = "#PX"; // Memofelds
    static final String f1 = "#F1"; // erweiterte Artikeldaten (nicht bei 1234X)
    static final String f2 = "#F2"; // erweiterte Artikeldaten (nicht bei 1234X)
    static final String X = "#X"; // Ende Marker
    static final String DEFAULT_VERSION = "BauManWin V5.40";

	ArrayList<BaumanFormatVersion> bm_formats;
    
    protected AbstractBaumanAdapter(String bm_versions[]) throws IOException{
		bm_formats = new ArrayList<BaumanFormatVersion>();
		ClassLoader ctxldr = Thread.currentThread().getContextClassLoader();
		for(String bm_version:bm_versions){
			// bm_version ist ein Pfad zu einer Resource/File, die entweder eine .def Datei ist oder
			// ein Verzeichnis (dann werden alle .def dateien darin glelesen)
			File file = new File(bm_version);
			// folder in filesystem
			if( file.isDirectory() ){
				for(File f: FileUtils.listFiles(file, new String[]{"def"}, false)){
					BaumanFormatVersion bmfv = loadFromDefStream(new FileInputStream(f));
					//System.out.println("mapping " + f.getAbsolutePath() + " to BM:" + bmfv.valid_for);
					bm_formats.add(bmfv);
				}
			}
			// file in filesystem
			else if (file.isFile()){
				BaumanFormatVersion bmfv = loadFromDefStream(new FileInputStream(file));
				//System.out.println("mapping " + f.getAbsolutePath() + " to BM:" + bmfv.valid_for);
				bm_formats.add(bmfv);
			}else{
				URL url = ctxldr.getResource(bm_version);
				if (url != null){
					try (FileSystem fs = FileSystems.newFileSystem(URI.create(url.toString()), Collections.emptyMap())) {
						Files.walk(fs.getPath(bm_version))
								.filter(Files::isRegularFile)
								.filter(f -> f.toString().endsWith(".def"))
								.forEach(f -> {
									try{
										var bmvf=loadFromDefStream(ctxldr.getResourceAsStream(f.toString()));
										bm_formats.add(bmvf);
									}catch(IOException x){}
								});
					}					
										
				}
		}
		}
    }
    protected BaumanFormatVersion loadFromDefStream(InputStream defs) throws IOException{
		BufferedReader br=new BufferedReader(new InputStreamReader(defs));
		BaumanFormatVersion bmfv = new BaumanFormatVersion(br);
		br.close();
		return bmfv;
    }

    protected BaumanFormatVersion getBestVersion(String bmversion) throws FormatAdapterException{
    	
		BaumanFormatVersion res=null;
		String useversion = bmversion != null && !bmversion.isEmpty() ? bmversion : DEFAULT_VERSION;
		for(BaumanFormatVersion v:bm_formats){
			if (v.valid_for.equals(useversion)){
				res = v;
				break;
			}
		}
		if (res == null && !useversion.equalsIgnoreCase(DEFAULT_VERSION)){
			useversion = DEFAULT_VERSION;
			for(BaumanFormatVersion v:bm_formats){
				if (v.valid_for.equals(useversion)){
					res = v;
					break;
				}
			}
			
		}
		if (res == null){
			throw new FormatAdapterException(bmversion + " not available!");
		}
    	return res;
    }
    
	public void write(DocumentPart doc, ParseContext ctx, String bmversion) throws FormatAdapterException
	{
			try {
				ctx.bmfv = getBestVersion(bmversion);
				emitStart(ctx);
				ctx.lineCounter = 0;
				ArrayList<String> linemarkers = new ArrayList<String>();
				for(int i=1; i <= 16; i++) linemarkers.add(String.format("#K%1$02d", i));
				for(int i=0; i <= 2; i++) linemarkers.add(String.format("#B%1$02d", i));
				for(int i=0; i <= 54; i++) linemarkers.add(String.format("#A%1$02d", i));
				for(int i=0; i <= 40; i++) linemarkers.add(String.format("#M%1$02d", i));
				for(int i=0; i <= 40; i++) linemarkers.add(String.format("#L%1$02d", i));
				for(int i=0; i <= 40; i++) linemarkers.add(String.format("#G%1$02d", i));
				for (String marker: linemarkers){
					
					writeLine (marker, ctx);
				}
				writeMemo (tpx, ctx);
				writeMemo (tt1, ctx);
				writeMemo (tt2, ctx);
				writeMemo (tt3, ctx);
				writeMemo (tte, ctx);
				writeMemo (tbk, ctx);
				writeMemo (tfs, ctx);
				writePositions (JBMDocumentNames.POSITIONS, ctx);
				emitDone(ctx);
			} catch (DocumentPartException e) {
				throw new BMFormatException (ctx.requestDocPart + " was not found!", e);
			} catch (PropertyMissingException e) {
				throw new BMFormatException (ctx.requestDocPart + ": Property " + ctx.requestField + " was not found!", e);
			} catch (Exception e) {
				throw new BMFormatException (e);
			}
	}

	private DocumentPart getDocPart (String partName, ParseContext ctx)
	throws DocumentException
	{
		ctx.requestDocPart = partName;
		return ctx.doc.getPart (partName);
	}
	
	private void assembleLine (String marker, String[] fields, DocumentPart docpart, String fieldSep, String lineSep, int shorter, ParseContext ctx)
	throws Exception
	{
		String field;
		if (fields != null){
			for (int i=0; i < fields.length-shorter; i++) {
				ctx.requestField = fields[i];
				//System.out.println(requestField);
				field = docpart.getProperty (ctx.requestField).getValue().toString();
				emitField(marker, ctx.requestField, field, ctx);
			}
		}
	}

	private void writeLine (String marker, ParseContext ctx) 
	throws Exception
	{
		String partName = ctx.bmfv.getPartname(marker);
		if (partName != null){
			String[] fields = ctx.bmfv.getLinedef(marker);
			// System.out.println("MArker: " + marker);
			emitLineStart(marker, partName, fields, ctx);
			try{
				DocumentPart docpart = getDocPart (partName, ctx);
				assembleLine (marker, fields, docpart, FIELDSEPARATOR, LINESEPARATOR, 0, ctx);
			}catch(DocumentPartMissingException dpme){
				// System.out.println("DocumentPart "+partName+" not found ... skip writing it");
			}
			emitLineEnd(marker, partName, fields, ctx);
		}
	}

	private void writeMemo (String marker, ParseContext ctx)
	throws Exception
	{
		String partName = ctx.bmfv.getPartname(marker);
		String[] fields = ctx.bmfv.getLinedef(marker);
		
		emitMemoStart(marker, partName, fields, ctx);
		DocumentPart docpart = getDocPart (partName, ctx);
		ctx.requestField = fields[0];
		String memo = docpart.getProperty (ctx.requestField).getValue().toString();
		emitMemoField(marker, ctx.requestField, memo, ctx);
		emitMemoEnd(marker, partName, fields, ctx);
	}

	private void writePositions (String partName, ParseContext ctx)
	throws Exception
	{
		emitPositionlistStart(ctx);
		DocumentPart dp = getDocPart (partName, ctx);
		ArrayList<? extends AbstractDocumentPart> poslist = dp.getPartList(JBMDocumentNames.POSITIONLIST);

		for (DocumentPart docpart : poslist )
		{
			ctx.requestField = "poskind";
			String kennung = docpart.getProperty (ctx.requestField).getValue().toString();
			String marker = "#P"+kennung;
			String posPartName = ctx.bmfv.getPartname(marker);
			String [] fields = ctx.bmfv.getLinedef(marker);
			if (fields == null)
				throw new BMFormatException ("unbekanntes Positionsfeld '"+kennung+"' gefunden.");

			emitPositionStart(marker, posPartName, fields, ctx);
			String shortText="";
			String longText="";
			String[] shortTextLines={""};
			String[] longTextLines={""};
			int textlen;

			textlen = 0;

			// haben wir text?

			try {

				try {
					ctx.requestField = "shortText";
					shortText = docpart.getProperty (ctx.requestField).getValue().toString();
					shortTextLines = shortText.split("\n", -1);
				} catch (PropertyMissingException ex) {}

				try {
					ctx.requestField = "longText";
					longText = docpart.getProperty (ctx.requestField).getValue().toString();
					longTextLines = longText.split("\n", -1);
				} catch (PropertyMissingException ex) {}

				textlen = shortTextLines.length-1 + longTextLines.length-1;
				emitPositionMandatoryStart(marker, posPartName, fields, ctx);
				int lenless = 0;
				boolean shortCount=false;
				boolean allCount=false;
				if (Arrays.asList(fields).contains("shortTextLinesCount")){
					lenless++;
					shortCount=true;
				}
				if (Arrays.asList(fields).contains("allTextLinesCount")){
					lenless++;
					allCount=true;
				}
				assembleLine (marker, fields, docpart, FIELDSEPARATOR, "", lenless, ctx); // ohne textlänge
				if (shortCount)
					emitField(marker, "shortTextLinesCount", (shortTextLines.length < 2 ? "" : ""+(shortTextLines.length-1)), ctx);
				if (allCount)
					emitField(marker, "allTextLinesCount", (textlen == 0 ? "" : ""+textlen), ctx);
				emitPositionMandatoryEnd(marker, posPartName, fields, ctx);

				// haben wir #F{1|2} Positionen?
				if ("PIAEMUQRS".indexOf (kennung) != -1)
				{
					String[] f = ctx.bmfv.getLinedef(f1);
					emitF1Start(marker, posPartName, f, ctx);
					assembleLine (f1, f, docpart, FIELDSEPARATOR, LINESEPARATOR, 0, ctx);
					emitF1End(marker, posPartName, f, ctx);

					f = ctx.bmfv.getLinedef(f2);
					emitF2Start(marker, posPartName, f, ctx);
					assembleLine (f2, f, docpart, FIELDSEPARATOR, LINESEPARATOR, 0, ctx);
					emitF2End(marker, posPartName, f, ctx);
				}

				if (textlen > 0) {
					emitPositionShortText(marker, "shortText", shortText, ctx);
					emitPositionLongText(marker, "longText", longText, ctx);
				}

				emitPositionEnd(marker, posPartName, fields, ctx);
				
			} catch (OutOfMemoryError ex) {
				System.out.println ("gotcha at line" + ctx.lineCounter);
				throw ex;
			}
		}
		emitPositionlistEnd(ctx);

	}
	
	protected void emitLineStart(String marker, String partName, String[] fields, ParseContext ctx)throws Exception{}
	protected void emitField(String marker, String fieldname, String fieldvalue, ParseContext ctx)throws Exception{}
	protected void emitLineEnd(String marker, String partName, String[] fields, ParseContext ctx) throws Exception{}

	protected void emitMemoStart(String marker, String partName, String[] fields, ParseContext ctx)throws Exception{}
	protected void emitMemoField(String marker, String fieldname, String fieldvalue, ParseContext ctx)throws Exception{}
	protected void emitMemoEnd(String marker, String partName, String[] fields, ParseContext ctx)throws Exception{}
	
	protected void emitStart(ParseContext ctx) throws Exception{}
	protected void emitDone(ParseContext ctx) throws Exception{}
	

	protected void emitPositionlistStart(ParseContext ctx) throws Exception{}
	protected void emitPositionlistEnd(ParseContext ctx) throws Exception{}

	protected void emitPositionStart(String marker, String partName, String[] fields, ParseContext ctx) throws Exception{}
	protected void emitPositionEnd(String marker, String partName, String[] fields, ParseContext ctx) throws Exception{}
	protected void emitPositionMandatoryStart(String marker, String partName, String[] fields, ParseContext ctx) throws Exception{}
	protected void emitPositionMandatoryEnd(String marker, String partName, String[] fields, ParseContext ctx) throws Exception{}

	protected void emitF1Start(String marker, String partName, String[] fields, ParseContext ctx) throws Exception{}
	protected void emitF1End(String marker, String partName, String[] fields, ParseContext ctx) throws Exception{}
	protected void emitF2Start(String marker, String partName, String[] fields, ParseContext ctx) throws Exception{}
	protected void emitF2End(String marker, String partName, String[] fields, ParseContext ctx) throws Exception{}

	protected void emitPositionShortText(String marker, String fieldname, String fieldvalue, ParseContext ctx) throws Exception{}
	protected void emitPositionLongText(String marker, String fieldname, String fieldvalue, ParseContext ctx) throws Exception{}
	
}
