
package norman.tools.bm.plugins.bm;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import norman.tools.bm.DocumentException;
import norman.tools.bm.DocumentPartException;
import norman.tools.bm.JBMDocumentNames;
import norman.tools.bm.document.AbstractDocumentPart;
import norman.tools.bm.document.DocumentPart;
import norman.tools.bm.document.PropertyMissingException;
import norman.tools.bm.plugins.DocumentFormatAdapter;
import norman.tools.bm.plugins.FormatAdapterException;


public class BaumanFormatAdapter extends AbstractBaumanAdapter
implements DocumentFormatAdapter
{
	/* Kopfdatenmarker */

	/**
	 * Creates an Adapter aware to read Baumanager Version 3.89 formated input.
	 *
	 * @param skipJunk             Set to true to ignore junk lines before or beyond marked
	 *                             lines (does not ignore text lines). Otherwise an exception
	 *                             will be thrown if such junk is encountered.
	 * @param appendTextTooMany    Set this to true to append those textlines that are beyond
	 *                             the announced number of lines to come.
	 *                             lines (does not ignore text lines). Otherwise an exception
	 *                             will be thrown if such junk is encountered.
	 * @param skipUnknownLineTypes Set to true to ignore lines which are properly marked but
	 *                             unknown to this implementation. Otherwise an exception is
	 *                             thrown.
	 * @param useDummies           Set to true if you want default values in fields which cannot
	 *                             be found by this parser. This will also create default lines
	 *                             if missing.
	 */

	boolean doSkipJunk;
	boolean appendTextTooMany;
	boolean doSkipUnknownLineTypes;
	boolean doUseDummies;

	
	public BaumanFormatAdapter(boolean skipJunk,
			boolean appendTextTooMany,
			boolean skipUnknownLineTypes, 
			boolean useDummies,
			String bm_versions[])
	 throws IOException
	{
		super(bm_versions);
		doSkipJunk = skipJunk;
		doSkipUnknownLineTypes = skipUnknownLineTypes;
		doUseDummies = useDummies;
		this.appendTextTooMany = appendTextTooMany;
		
	}

	public void read(InputStream is, DocumentPart doc) throws FormatAdapterException
	{
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "ISO-8859-1"));
			ParseContext ctx = new ParseContext(doc, reader);
			
			String line;

			while ((line = ctx.nextLine()) != null)
			{
				if (!isQualifiedLine(line))
				{
					// entweder junk oder wir sind gerade in einem (mehrzeiligen) Text
					ctx.pushBack (line);
					readUnqualifiedLines(ctx);
				}
				else{
					doParse (line, ctx);
				}
			}

		} catch (UnsupportedEncodingException ex) {
			throw new BMFormatException ("Kein ISO-8859-1 auf diesem System vorhanden - wird benötight um .tlv Dateien zu lesen!");
		} catch (DocumentException ex) {
			throw new BMFormatException (ex);
		}

	}


	public void write(java.io.OutputStream os, DocumentPart doc) throws FormatAdapterException
	{
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(os, "ISO-8859-1"));
			ParseContext ctx = new ParseContext(doc, writer);
			write(doc, ctx);
			writer.flush();
			
		} catch (UnsupportedEncodingException ex) {
			throw new BMFormatException ("Kein ISO-8859-1 auf diesem System vorhanden - wird benötigt um .tlv Dateien zu lesen!");
		} catch (IOException e) {
			throw new BMFormatException (e.toString());
		}
	}

	protected void emitDone(ParseContext ctx) throws Exception{
		ctx.writeln(X+LINESEPARATOR); // und tschüss
	}

	protected void emitLineStart(String marker, String partName, String[] fields, ParseContext ctx)throws Exception{
		ctx.line = new StringBuffer(marker == null ? "" : (marker+FIELDSEPARATOR));
	}

	protected void emitField(String marker, String fieldname, String fieldvalue, ParseContext ctx)throws Exception{
		ctx.line.append(fieldvalue+FIELDSEPARATOR);
	}
	
	protected void emitLineEnd(String marker, String partName, String[] fields, ParseContext ctx) throws Exception{
		ctx.line.append(LINESEPARATOR);
		ctx.writeln(ctx.line.toString());
	}

	protected void emitMemoField(String marker, String fieldname, String fieldvalue, ParseContext ctx)throws Exception{
		String[] lines = fieldvalue.split("\n", -1);
		
		String line = marker + FIELDSEPARATOR + (lines.length < 2 ? "" : ""+(lines.length-1)) + FIELDSEPARATOR+LINESEPARATOR;
		ctx.writeln(line);

		for (int i=0; i < lines.length-1; i++)
			ctx.writeln(lines[i] + LINESEPARATOR);
		
	}
	
	protected void emitPositionStart(String marker, String partName, String[] fields, ParseContext ctx) throws Exception{
		ctx.line = new StringBuffer(marker == null ? "" : (marker+FIELDSEPARATOR));
	}

	protected void emitPositionMandatoryEnd(String marker, String partName, String[] fields, ParseContext ctx) throws Exception{
		ctx.line.append(LINESEPARATOR);
		ctx.writeln(ctx.line.toString());
	}

	protected void emitF1Start(String marker, String partName, String[] fields, ParseContext ctx) throws Exception{
		emitLineStart(marker, partName, fields, ctx);
	}
	protected void emitF1End(String marker, String partName, String[] fields, ParseContext ctx) throws Exception{
		emitLineEnd(marker, partName, fields, ctx);
	}
	protected void emitF2Start(String marker, String partName, String[] fields, ParseContext ctx) throws Exception{
		emitLineStart(marker, partName, fields, ctx);
	}
	protected void emitF2End(String marker, String partName, String[] fields, ParseContext ctx) throws Exception{
		emitLineEnd(marker, partName, fields, ctx);
	}
	
	protected void emitPositionShortText(String marker, String fieldname, String fieldvalue, ParseContext ctx) throws Exception{
		String[] lines = fieldvalue.split("\n", -1);
		for (int i=0; i < lines.length-1; i++)
			ctx.writeln(lines[i] + LINESEPARATOR);
	}
	protected void emitPositionLongText(String marker, String fieldname, String fieldvalue, ParseContext ctx) throws Exception{
		emitPositionShortText(marker, fieldname, fieldvalue, ctx);
	}
	

	private void skipJunk (ParseContext ctx) 
	throws BMFormatException
	{
		String line=null;
		// lies weiter bis wir eine Markerzeile finden
		while ((line = ctx.nextLine()) != null && !isQualifiedLine(line))
			;
		ctx.pushBack(line);
	}

	private void doParse (String line, ParseContext ctx)
	throws BMFormatException, DocumentException
	{
		// splitte String, um den Marker zu extrahieren
		String[] parts = line.split(FIELDSEPARATOR, -1);

		if (ctx.bmfv == null){
			for(BaumanFormatVersion v:bm_formats){
				if (v.valid_ql.equals(parts[0])){
					if (v.valid_for.equals(parts[v.valid_fieldindex])){
						ctx.bmfv = v;
						break;
					}
				}
			}
		}
		if (ctx.bmfv == null){
			throw new BMFormatException("No valid Baumanager Format Definition found!");
		}
		
		// schauen wir mal, ob wir den Marker kennen
		String marker = parts[0];
		//	System.out.println ("checking marker "+marker);
		//System.out.println ("line >" + lineCounter + "<");
		String partname = ctx.bmfv.getPartname(marker);
		String fields[] = ctx.bmfv.getLinedef(marker);
		
		if (fields != null){
			if (JBMDocumentNames.TEXT.equals(partname))
				parseMemo (fields, JBMDocumentNames.TEXT, parts, ctx);
			else if (JBMDocumentNames.POSITIONS.equals(partname))
				parsePosition (fields, JBMDocumentNames.POSITIONS, parts, ctx);
			else if ("POSITIONEXTENSIONS".equals(partname))
				parseLine (fields, null, parts, ctx);
			else if ("END".equals(partname)){
				// the end is nigh
			}
			else if (partname != null)
				parseLine (fields, partname, parts, ctx);
			else if (marker.length() > 4) {
				// wir sind wahrscheinlich im Text gelandet un der fängt zum Beispile mit einem #Format Zeichen an
				System.out.println("leeching " + marker);
				ctx.pushBack(line);
				readUnqualifiedLines(ctx);
			}
			else {
				storeVerbatim (marker, "unrecognized", line, ctx);
			}
		}else{
			// unrecognized line
			throw new BMFormatException("unrecognized line:" + line);
		}
		
	}

	private void parseLine(String[] fields, String partName, String[] parts, 
			ParseContext ctx)
	throws DocumentException
	{
		// extrahiere den relevanten part
		// partName sieht so aus: part.subpart.subsubpart etc
		DocumentPart docpart;
		int i=0;
		if (ctx.lastDocPart != null)
			docpart = ctx.lastDocPart;
		else {
			docpart = ctx.doc.addPart (partName);
		}

		i=0;	
		try {
			for (i=0; i < fields.length; i++) {
				docpart.putProperty (fields[i], 
						parts[i+1], // 0.te ist marker
						true, // lasse dirty state unverändert
						true); // Wert kam aud Datenstrom
			}

		} catch (java.lang.IndexOutOfBoundsException e) {
			// hier kommen wir hin, falls wir zu wenig felder in parts haben
			if (doUseDummies) {
				for (; i < fields.length; i++) {
					System.out.println("use dummy for " + fields[i]);
					docpart.putProperty (fields[i], 
							"",  // dem DocumentPart ist es überlassen, ein sinnvollen defaultwert zu wählen
							false, // lasse dirty state unverändert
							false); // wert kommt nicht aus Datenstrom
				}
			}
		}

		// falls wir noch mehr Daten anstehen haben, lagern wir die jetzt zwischen
		// length-1 .. letzter part ist der rechts vom schliessenden ";" => brauchen wir also nicht
		if (i+1 < parts.length-1) { 
			// lege einen neuen Subpart an
			DocumentPart cruft;
			cruft = docpart.addPart (parts[0]+"_unknown");

			for (; i+1 < parts.length-1; i++) {
				cruft.putProperty ("srcField"+(i+1), parts[i+1], true, true);
				System.out.println("adding cruft:>"+parts[i+1]+"<");
			}
		}
	}

	private void parseMemo (String[] fields, String partName, String[] parts, ParseContext ctx)
	throws BMFormatException, DocumentException
	{
		// extrahiere den relevanten part
		// partName sieht so aus: part.subpart.subsubpart etc
		int i=0;
		DocumentPart docpart = ctx.doc.addPart (partName);

		int textlines = -1;


		try {
			// an pos 1 steht die Anzahl der folgenden Textzeilen
			textlines = Integer.parseInt(parts[1]);

		} catch (NumberFormatException e) {
			textlines = -1; // relaxed mode
		} catch (Exception e) {
			// hier kommen wir hin, falls wir zu wenig felder in parts haben
			if (doUseDummies) {
				for (; i < fields.length; i++) {
					docpart.putProperty (fields[i], 
							null,  // dem DocumentPart ist es überlassen, ein sinnvollen defaultwert zu wählen
							false, // lasse dirty state unverändert
							false); // wert kommt nicht aus Datenstrom
				}
			}
		}
		/*
	// falls wir noch mehr Daten anstehen haben, lagern wir die jetzt zwischen
	if (i+1 < parts.length-1) {
	    // lege einen neuen Subpart an
	    DocumentPart cruft;
	    cruft = docpart.addPart (parts[0]+"_unknown");

	    for (; i+1 < parts.length-1; i++) {
		cruft.putProperty ("srcField"+(i+1), parts[i+1], true, true); 
		System.out.println("adding cruft:>"+parts[i+1]+"<lastOZ:"+lastOZ);
	    }
	}
		 */
		i = 0;
		StringBuffer text = new StringBuffer();
		String line=null;
		while (i < textlines || textlines == -1) {
			line = ctx.nextLine();
			i++;
			if (textlines != -1 || !isQualifiedLine(line)) {
				text.append(line).append('\n');
			}
			if (textlines == -1 && isQualifiedLine(line))
				break;
		}

		//System.out.println (fields[0]+"-textlines="+textlines+"\ntext:>"+text.toString()+"<");
		docpart.putProperty (fields[0], text, true, true); 

		if (textlines == -1 && line != null)
			ctx.pushBack (line);

	}

	private void parsePosition (String[] fields, String partName, String[] parts, ParseContext ctx)
	throws DocumentException
	{
		ctx.lastProcessedPosition (null, -1, -1);

		// extrahiere den relevanten part
		// partName sieht so aus: part.subpart.subsubpart etc
		int i;
		DocumentPart docpart = ctx.doc.addPart (partName);

		docpart = docpart.addPartToList(JBMDocumentNames.POSITIONLIST, JBMDocumentNames.POSITION);

		// positionskennung
		docpart.putProperty ("poskind", parts[0].substring(2,3), true, true);
		i=0;
		try {
			for (i=0; i < fields.length; i++) {
				docpart.putProperty (fields[i], 
						parts[i+1], // 0.te ist marker
						true, // lasse dirty state unverändert
						true); // Wert kam aus Datenstrom
			}
			ctx.lastOZ = parts[ctx.bmfv.index(fields, "oz")];
			//System.out.println ("OZ: "+lastOZ);
		} catch (java.lang.IndexOutOfBoundsException e) {
			// hier kommen wir hin, falls wir zu wenig felder in parts haben
			if (doUseDummies) {
				for (; i < fields.length; i++) {
					System.out.println("use dummy for " + fields[i]);
					docpart.putProperty (fields[i], 
							null,  // dem DocumentPart ist es überlassen, ein sinnvollen defaultwert zu wählen
							false, // lasse dirty state unverändert
							false); // wert kommt nicht aus Datenstrom
				}
			}
		}

		// falls wir noch mehr Daten anstehen haben, lagern wir die jetzt zwischen
		if (i+1 < parts.length-1) {
			// lege einen neuen Subpart an
			DocumentPart cruft;
			cruft = docpart.addPart (parts[0]+"_unknown");

			for (; i+1 < parts.length-1; i++) {
				cruft.putProperty ("srcField"+(i+1), parts[i+1], true, true); 
				System.out.println("adding cruft:>"+parts[i+1]+"< at oz:"+ctx.lastOZ);
			}
		}

		// checken wir mal, ob wir noch Texte erwarten dürfen ...

		int allTextLines = -1;
		int shortTextLines = -1;

		try {
			allTextLines = Integer.parseInt(parts[fields.length]);
		} catch (Exception e) {}

		try {
			shortTextLines = Integer.parseInt(parts[fields.length -1]);
		} catch (Exception e) {}

		// die Position merken wir uns, falls noch #F1, #F2 oder Text kommt
		/*
	System.out.println ( "expect " + allTextLines + " lines, including " + shortTextLines + " st-lines");
	for (i=0; i < parts.length; i++)
	    System.out.println(i+".: " + parts[i]+"\n");
		 */
		ctx.lastProcessedPosition (docpart, allTextLines, shortTextLines);

	}

	private void showParts (String[] fields, String[] parts)
	{
		for (int i=0; i < fields.length; i++) {
			if (parts[+1] == null)
				System.out.println(fields[i]+".: is null");
			else
				System.out.println(fields[i]+".: " + parts[i+1]);
		}
	}

	private void storeVerbatim (String marker, String partName, String line, ParseContext ctx)
	throws BMFormatException, DocumentException
	{
		// extrahiere den relevanten part
		// partName sieht so aus: part.subpart.subsubpart etc
		DocumentPart docpart = ctx.doc.addPart (partName);

		// text lesen und im docPart setzen
		StringBuffer text = new StringBuffer();
		while (line != null) {
			text.append('\n').append(line);
			line = ctx.nextLine();
			if (isQualifiedLine(line))
				break;
		}

		docpart.putProperty (marker, text, true, false); 
		ctx.pushBack (line);
	}


	private void readUnqualifiedLines (ParseContext ctx) throws BMFormatException
	{
		//	System.out.println ("reading unqualified lines, textlines/short="+expectTextLines + "/" +expectShortTextLines);
		if (ctx.lastDocPart == null)
		{
			if (doSkipJunk)
				skipJunk(ctx);
			else {
				throw new BMFormatException ("Bei Zeile " + (ctx.lineCounter+1) + " den Marker '"+ 
						LINETYPEMARKER + 
				"' erwartet aber nicht gefunden!");
			}
		}
		else {
			// text lesen und im docPart setzen
			int i = 0;
			StringBuffer text = new StringBuffer();
			StringBuffer shortText = new StringBuffer();
			StringBuffer longText = new StringBuffer();
			String line=null;
			int textlines = ctx.expectTextLines;
			boolean readAsMuchAsPossible = textlines == -1 || appendTextTooMany;

			while (i < textlines || readAsMuchAsPossible) {
				//System.out.println ((i+1) + " of " + textlines);
				line = ctx.nextLine();
				i++;
				if (!isQualifiedLine(line)) {
					text.append(line).append('\n');
					if (ctx.expectShortTextLines == i) {
						shortText = text;
						text = longText;
					}
				}

				if (readAsMuchAsPossible && isQualifiedLine(line)) {
					ctx.pushBack (line);
					break;
				}
			}

			if (ctx.expectShortTextLines < 1) {
				longText = text;
			}

			ctx.lastDocPart.putProperty ("shortText", shortText, true, true); 
			ctx.lastDocPart.putProperty ("longText", longText, true, true); 
			/*
	    if (lastOZ.equals("48. 7. 720"))
		System.out.println ("textlines="+textlines+"\ni="+i+"\nlongtext:\n"+longText.toString()+"\nshortText:\n"+shortText.toString());
			 */

			//	    System.out.println (longText.toString());
			ctx.expectTextLines = 0;
			ctx.expectShortTextLines = 0;

		}
	}

	private boolean isQualifiedLine (String line) {
		boolean isQual = false;
		if (line != null && line.length()>0 && line.charAt(0) == LINETYPEMARKER) {
			String[] parts = line.split(FIELDSEPARATOR);
			isQual = parts[0].length() < 5;
		}
		return isQual;
	}
}
