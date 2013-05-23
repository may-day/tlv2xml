
package norman.tools.bm.plugins.bm;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

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
	int lineCounter;
	int lastCounter, infiniLoop;
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

	boolean usePushbackLine;
	String pushbackLine;
	DocumentPart lastDocPart;
	int expectTextLines;
	int expectShortTextLines;
	String requestDocPart, requestField;
	String lastOZ;

	public BaumanFormatAdapter(boolean skipJunk,
			boolean appendTextTooMany,
			boolean skipUnknownLineTypes, 
			boolean useDummies
	)
	{
		doSkipJunk = skipJunk;
		doSkipUnknownLineTypes = skipUnknownLineTypes;
		doUseDummies = useDummies;
		this.appendTextTooMany = appendTextTooMany;
		usePushbackLine = false;
		lastProcessedPosition (null, -1, -1);
	}

	public void read(InputStream is, DocumentPart doc) throws FormatAdapterException
	{
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "ISO-8859-1"));
			String line;

			lineCounter = 0;
			lastCounter = infiniLoop = 0;

			while ((line = nextLine (reader)) != null)
			{
				if (!isQualifiedLine(line))
				{
					// entweder junk oder wir sind gerade in einem (mehrzeiligen) Text
					pushBack (line);
					readUnqualifiedLines(reader);
				}
				else
					doParse (line, reader, doc);
			}

		} catch (UnsupportedEncodingException ex) {
			throw new BMFormatException ("Kein ISO-8859-1 auf diesem System vorhanden - wird benötight um .tlv Dateien zu lesen!");
		} catch (DocumentException ex) {
			throw new BMFormatException (ex);
		}

	}


	public void write(java.io.OutputStream os, DocumentPart doc) throws FormatAdapterException
	{
		try {
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "ISO-8859-1"));

			//setupDocumentVersion (DEFAULT_VERSION);
			lineCounter = 0;
			writeLine (k01, JBMDocumentNames.HEADER, k01fields, writer, doc);
			writeLine (k02, JBMDocumentNames.HEADER, k02fields, writer, doc);
			writeLine (k03, JBMDocumentNames.HEADER, k03fields, writer, doc);
			writeLine (k04, JBMDocumentNames.HEADER, k04fields, writer, doc);
			writeLine (k05, JBMDocumentNames.HEADER, k05fields, writer, doc);
			writeLine (k06, JBMDocumentNames.HEADER, k06fields, writer, doc);
			writeLine (k07, JBMDocumentNames.HEADER, k07fields, writer, doc);
			writeLine (k08, JBMDocumentNames.HEADER, k08fields, writer, doc);
			writeLine (k09, JBMDocumentNames.HEADER, k09fields, writer, doc);
			writeLine (k10, JBMDocumentNames.HEADER, k10fields, writer, doc);
			writeLine (k11, JBMDocumentNames.HEADER, k11fields, writer, doc);
			writeLine (k12, JBMDocumentNames.HEADER, k12fields, writer, doc);
			writeLine (a1, JBMDocumentNames.CALCPAGE, a1fields, writer, doc);
			writeLine (a2, JBMDocumentNames.CALCPAGE, a2fields, writer, doc);
			writeLine (a3, JBMDocumentNames.CALCPAGE, a3fields, writer, doc);
			writeLine (a4, JBMDocumentNames.CALCPAGE, a4fields, writer, doc);
			writeLine (a01, JBMDocumentNames.CALCPAGE+".mg01", a01fields, writer, doc);
			writeLine (a02, JBMDocumentNames.CALCPAGE+".mg02", a02fields, writer, doc);
			writeLine (a03, JBMDocumentNames.CALCPAGE+".mg03", a03fields, writer, doc);
			writeLine (a04, JBMDocumentNames.CALCPAGE+".mg04", a04fields, writer, doc);
			writeLine (a05, JBMDocumentNames.CALCPAGE+".mg05", a05fields, writer, doc);
			writeLine (a06, JBMDocumentNames.CALCPAGE+".mg06", a06fields, writer, doc);
			writeLine (a07, JBMDocumentNames.CALCPAGE+".mg07", a07fields, writer, doc);
			writeLine (a08, JBMDocumentNames.CALCPAGE+".mg08", a08fields, writer, doc);
			writeLine (a09, JBMDocumentNames.CALCPAGE+".mg09", a09fields, writer, doc);
			writeLine (a10, JBMDocumentNames.CALCPAGE+".mg10", a10fields, writer, doc);
			writeLine (a11, JBMDocumentNames.CALCPAGE+".mg11", a11fields, writer, doc);
			writeLine (a12, JBMDocumentNames.CALCPAGE+".mg12", a12fields, writer, doc);
			writeLine (a13, JBMDocumentNames.CALCPAGE+".mg13", a13fields, writer, doc);
			writeLine (a14, JBMDocumentNames.CALCPAGE+".mg14", a14fields, writer, doc);
			writeLine (a15, JBMDocumentNames.CALCPAGE+".mg15", a15fields, writer, doc);
			writeLine (a16, JBMDocumentNames.CALCPAGE+".mg16", a16fields, writer, doc);
			writeLine (a17, JBMDocumentNames.CALCPAGE+".mg17", a17fields, writer, doc);
			writeLine (a18, JBMDocumentNames.CALCPAGE+".mg18", a18fields, writer, doc);
			writeLine (a19, JBMDocumentNames.CALCPAGE+".mg19", a19fields, writer, doc);
			writeLine (a21, JBMDocumentNames.CALCPAGE+".lg01", a21fields, writer, doc);
			writeLine (a22, JBMDocumentNames.CALCPAGE+".lg02", a22fields, writer, doc);
			writeLine (a23, JBMDocumentNames.CALCPAGE+".lg03", a23fields, writer, doc);
			writeLine (a24, JBMDocumentNames.CALCPAGE+".lg04", a24fields, writer, doc);
			writeLine (a25, JBMDocumentNames.CALCPAGE+".lg05", a25fields, writer, doc);
			writeLine (a26, JBMDocumentNames.CALCPAGE+".lg06", a26fields, writer, doc);
			writeLine (a27, JBMDocumentNames.CALCPAGE+".lg07", a27fields, writer, doc);
			writeLine (a28, JBMDocumentNames.CALCPAGE+".lg08", a28fields, writer, doc);
			writeLine (a29, JBMDocumentNames.CALCPAGE+".lg09", a29fields, writer, doc);
			writeLine (a30, JBMDocumentNames.CALCPAGE+".lg10", a30fields, writer, doc);
			writeLine (a31, JBMDocumentNames.CALCPAGE+".lg11", a31fields, writer, doc);
			writeLine (a32, JBMDocumentNames.CALCPAGE+".lg12", a32fields, writer, doc);
			writeLine (a33, JBMDocumentNames.CALCPAGE+".lg13", a33fields, writer, doc);
			writeLine (a34, JBMDocumentNames.CALCPAGE+".lg14", a34fields, writer, doc);
			writeLine (a35, JBMDocumentNames.CALCPAGE+".lg15", a35fields, writer, doc);
			writeLine (a36, JBMDocumentNames.CALCPAGE+".lg16", a36fields, writer, doc);
			writeLine (a37, JBMDocumentNames.CALCPAGE+".lg17", a37fields, writer, doc);
			writeLine (a38, JBMDocumentNames.CALCPAGE+".lg18", a38fields, writer, doc);
			writeLine (a39, JBMDocumentNames.CALCPAGE+".lg19", a39fields, writer, doc);
			writeMemo (tpx, JBMDocumentNames.TEXT, tpxfields, writer, doc);
			writeMemo (tt1, JBMDocumentNames.TEXT, tt1fields, writer, doc);
			writeMemo (tt2, JBMDocumentNames.TEXT, tt2fields, writer, doc);
			writeMemo (tt3, JBMDocumentNames.TEXT, tt3fields, writer, doc);
			writeMemo (tte, JBMDocumentNames.TEXT, ttefields, writer, doc);
			writeMemo (tbk, JBMDocumentNames.TEXT, tbkfields, writer, doc);
			writeMemo (tfs, JBMDocumentNames.TEXT, tfsfields, writer, doc);
			writePositions (JBMDocumentNames.POSITIONS, writer, doc);
			writeln(X+LINESEPARATOR, writer); // und tsch�ss
			writer.flush();
		} catch (DocumentPartException e) {
			System.err.println (requestDocPart + " was not found!");
			throw new BMFormatException (e.toString());
		} catch (PropertyMissingException e) {
			System.err.println (requestDocPart + ": Property " + requestField + " was not found!");
		} catch (UnsupportedEncodingException ex) {
			throw new BMFormatException ("Kein ISO-8859-1 auf diesem System vorhanden - wird ben�tight um .tlv Dateien zu lesen!");
		} catch (Exception e) {
			throw new BMFormatException (e.toString());
		}
	}

	private void writeln (String line, BufferedWriter writer) throws IOException
	{
		//System.out.println (line);
		writer.write (line);
		//writer.flush();
		lineCounter++;
	}

	private DocumentPart getDocPart (String partName, DocumentPart doc)
	throws DocumentException
	{
		requestDocPart = partName;
		return doc.getPart (partName);
	}

	private String assembleLine (String marker, String[] fields, DocumentPart docpart, String fieldSep, String lineSep, int shorter)
	throws DocumentException
	{
		StringBuffer line = new StringBuffer(marker == null ? "" : (marker+fieldSep));
		String field;
		for (int i=0; i < fields.length-shorter; i++) {
			requestField = fields[i];
			//System.out.println(requestField);
			field = docpart.getProperty (requestField).getValue().toString();
			line.append(field+fieldSep);
		}
		line.append(lineSep);
		return line.toString();
	}

	private void writeLine (String marker, String partName, String[] fields, BufferedWriter writer, DocumentPart doc) 
	throws DocumentException, IOException
	{
		DocumentPart docpart = getDocPart (partName, doc);
		String line = assembleLine (marker, fields, docpart, FIELDSEPARATOR, LINESEPARATOR, 0);
		writeln(line, writer);
	}

	private void writeMemo (String marker, String partName, String[] fields, BufferedWriter writer, DocumentPart doc)
	throws DocumentException, IOException
	{
		DocumentPart docpart = getDocPart (partName, doc);
		requestField = fields[0];
		String[] lines = {};
		String memo = docpart.getProperty (requestField).getValue().toString();
		lines = memo.split("\n", -1);
		String line = marker + FIELDSEPARATOR + (lines.length < 2 ? "" : ""+(lines.length-1)) + FIELDSEPARATOR+LINESEPARATOR;
		writeln(line, writer);
		for (int i=0; i < lines.length-1; i++)
			writeln(lines[i] + LINESEPARATOR, writer);
	}

	private void writePositions (String partName, BufferedWriter writer, DocumentPart doc)
	throws DocumentException, BMFormatException, IOException
	{
		DocumentPart dp = getDocPart (partName, doc);
		ArrayList<? extends AbstractDocumentPart> poslist = dp.getPartList(JBMDocumentNames.POSITIONLIST);
		String line;

		for (DocumentPart docpart : poslist )
		{
			requestField = "poskind";
			String kennung = docpart.getProperty (requestField).getValue().toString();
			String [] fields;
			if (kennung.equals("P")) fields = ppfields;
			else if (kennung.equals("I")) fields = pifields;
			else if (kennung.equals("A")) fields = pafields;
			else if (kennung.equals("E")) fields = pefields;
			else if (kennung.equals("M")) fields = pmfields;
			else if (kennung.equals("U")) fields = pufields;
			else if (kennung.equals("Q")) fields = pqfields;
			else if (kennung.equals("R")) fields = prfields;
			else if (kennung.equals("S")) fields = psfields;
			else if (kennung.equals("Z")) fields = pzfields;
			else if (kennung.equals("1")) fields = p1fields;
			else if (kennung.equals("2")) fields = p2fields;
			else if (kennung.equals("3")) fields = p3fields;
			else if (kennung.equals("4")) fields = p4fields;
			else if (kennung.equals("X")) fields = pxfields;
			else throw new BMFormatException ("unbekanntes Positionsfeld '"+kennung+"' gefunden.");

			String shortText;
			String longText;
			String[] shortTextLines={""};
			String[] longTextLines={""};
			int textlen;

			textlen = 0;

			// haben wir text?

			try {

				try {
					requestField = "shortText";
					shortText = docpart.getProperty (requestField).getValue().toString();
					shortTextLines = shortText.split("\n", -1);
				} catch (PropertyMissingException ex) {}

				try {
					requestField = "longText";
					longText = docpart.getProperty (requestField).getValue().toString();
					longTextLines = longText.split("\n", -1);
				} catch (PropertyMissingException ex) {}

				textlen = shortTextLines.length-1 + longTextLines.length-1;
				line = assembleLine ("#P"+kennung, fields, docpart, FIELDSEPARATOR, "", 2); // ohne textl�nge
				line = line + (shortTextLines.length < 2 ? "" : ""+(shortTextLines.length-1)) + FIELDSEPARATOR;
				line = line + (textlen == 0 ? "" : ""+textlen) + FIELDSEPARATOR + LINESEPARATOR;
				writeln(line, writer);

				// haben wir #F{1|2} Positionen?
				if ("PIAEMUQRS".indexOf (kennung) != -1)
				{
					line = assembleLine ("#F1", f1fields, docpart, FIELDSEPARATOR, LINESEPARATOR, 0);
					writeln(line, writer);

					line = assembleLine ("#F2", f2fields, docpart, FIELDSEPARATOR, LINESEPARATOR, 0);
					writeln(line, writer);
				}

				if (textlen > 0) {
					String[] lines;
					lines = shortTextLines;
					for (int i=0; i < lines.length-1; i++)
						writeln(lines[i] + LINESEPARATOR, writer);
					lines = longTextLines;
					for (int i=0; i < lines.length-1; i++)
						writeln(lines[i] + LINESEPARATOR, writer);
				}

			} catch (OutOfMemoryError ex) {
				System.out.println ("gotcha at line" + lineCounter);
				throw ex;
			}
		}

	}

	private void skipJunk (BufferedReader reader) 
	throws BMFormatException
	{
		String line=null;
		// lies weiter bis wir eine Markerzeile finden
		while ((line = nextLine(reader)) != null && !isQualifiedLine(line))
			;
		pushBack(line);
	}

	private void doParse (String line, BufferedReader reader, DocumentPart doc)
	throws BMFormatException, DocumentException
	{
		// splitte String, um den Marker zu extrahieren
		String[] parts = line.split(FIELDSEPARATOR, -1);

		// schauen wir mal, ob wir den Marker kennen
		String marker = parts[0];
		//	System.out.println ("checking marker "+marker);
		//System.out.println ("line >" + lineCounter + "<");
		if (marker.equals (k01)) {
			// im 1. Feld finden wir die Versionsnummer
			// anhand dieser bestimmen wir die Strukturen der anderen Zeilen
			setupDocumentVersion (parts[1]);
			parseLine (k01fields, JBMDocumentNames.HEADER, parts, reader, doc);
		}
		else if (marker.equals (k02)) {
			//showParts (k02fields, parts);
			parseLine (k02fields, JBMDocumentNames.HEADER, parts, reader, doc);
		}
		else if (marker.equals (k03))
			parseLine (k03fields, JBMDocumentNames.HEADER, parts, reader, doc);
		else if (marker.equals (k04))
			parseLine (k04fields, JBMDocumentNames.HEADER, parts, reader, doc);
		else if (marker.equals (k05))
			parseLine (k05fields, JBMDocumentNames.HEADER, parts, reader, doc);
		else if (marker.equals (k06))
			parseLine (k06fields, JBMDocumentNames.HEADER, parts, reader, doc);
		else if (marker.equals (k07))
			parseLine (k07fields, JBMDocumentNames.HEADER, parts, reader, doc);
		else if (marker.equals (k08))
			parseLine (k08fields, JBMDocumentNames.HEADER, parts, reader, doc);
		else if (marker.equals (k09))
			parseLine (k09fields, JBMDocumentNames.HEADER, parts, reader, doc);
		else if (marker.equals (k10))
			parseLine (k10fields, JBMDocumentNames.HEADER, parts, reader, doc);
		else if (marker.equals (k11))
			parseLine (k11fields, JBMDocumentNames.HEADER, parts, reader, doc);
		else if (marker.equals (k12))
			parseLine (k12fields, JBMDocumentNames.HEADER, parts, reader, doc);
		else if (marker.equals (a1))
		{
			parseLine (a1fields, JBMDocumentNames.CALCPAGE, parts, reader, doc);
		}
		else if (marker.equals (a2))
			parseLine (a2fields, JBMDocumentNames.CALCPAGE, parts, reader, doc);
		else if (marker.equals (a3)) {
			parseLine (a3fields, JBMDocumentNames.CALCPAGE, parts, reader, doc);
		}else if (marker.equals (a4))
			parseLine (a4fields, JBMDocumentNames.CALCPAGE, parts, reader, doc);
		else if (marker.equals (a01))
			parseLine (a01fields, JBMDocumentNames.CALCPAGE+".mg01", parts, reader, doc);
		else if (marker.equals (a02))
			parseLine (a02fields, JBMDocumentNames.CALCPAGE+".mg02", parts, reader, doc);
		else if (marker.equals (a03))
			parseLine (a03fields, JBMDocumentNames.CALCPAGE+".mg03", parts, reader, doc);
		else if (marker.equals (a04))
			parseLine (a04fields, JBMDocumentNames.CALCPAGE+".mg04", parts, reader, doc);
		else if (marker.equals (a05))
			parseLine (a05fields, JBMDocumentNames.CALCPAGE+".mg05", parts, reader, doc);
		else if (marker.equals (a06))
			parseLine (a06fields, JBMDocumentNames.CALCPAGE+".mg06", parts, reader, doc);
		else if (marker.equals (a07))
			parseLine (a07fields, JBMDocumentNames.CALCPAGE+".mg07", parts, reader, doc);
		else if (marker.equals (a08))
			parseLine (a08fields, JBMDocumentNames.CALCPAGE+".mg08", parts, reader, doc);
		else if (marker.equals (a09))
			parseLine (a09fields, JBMDocumentNames.CALCPAGE+".mg09", parts, reader, doc);
		else if (marker.equals (a10))
			parseLine (a10fields, JBMDocumentNames.CALCPAGE+".mg10", parts, reader, doc);
		else if (marker.equals (a11))
			parseLine (a11fields, JBMDocumentNames.CALCPAGE+".mg11", parts, reader, doc);
		else if (marker.equals (a12))
			parseLine (a12fields, JBMDocumentNames.CALCPAGE+".mg12", parts, reader, doc);
		else if (marker.equals (a13))
			parseLine (a13fields, JBMDocumentNames.CALCPAGE+".mg13", parts, reader, doc);
		else if (marker.equals (a14))
			parseLine (a14fields, JBMDocumentNames.CALCPAGE+".mg14", parts, reader, doc);
		else if (marker.equals (a15))
			parseLine (a15fields, JBMDocumentNames.CALCPAGE+".mg15", parts, reader, doc);
		else if (marker.equals (a16))
			parseLine (a16fields, JBMDocumentNames.CALCPAGE+".mg16", parts, reader, doc);
		else if (marker.equals (a17))
			parseLine (a17fields, JBMDocumentNames.CALCPAGE+".mg17", parts, reader, doc);
		else if (marker.equals (a18))
			parseLine (a18fields, JBMDocumentNames.CALCPAGE+".mg18", parts, reader, doc);
		else if (marker.equals (a19))
			parseLine (a19fields, JBMDocumentNames.CALCPAGE+".mg19", parts, reader, doc);
		else if (marker.equals (a21))
			parseLine (a21fields, JBMDocumentNames.CALCPAGE+".lg01", parts, reader, doc);
		else if (marker.equals (a22))
			parseLine (a22fields, JBMDocumentNames.CALCPAGE+".lg02", parts, reader, doc);
		else if (marker.equals (a23))
			parseLine (a23fields, JBMDocumentNames.CALCPAGE+".lg03", parts, reader, doc);
		else if (marker.equals (a24))
			parseLine (a24fields, JBMDocumentNames.CALCPAGE+".lg04", parts, reader, doc);
		else if (marker.equals (a25))
			parseLine (a25fields, JBMDocumentNames.CALCPAGE+".lg05", parts, reader, doc);
		else if (marker.equals (a26))
			parseLine (a26fields, JBMDocumentNames.CALCPAGE+".lg06", parts, reader, doc);
		else if (marker.equals (a27))
			parseLine (a27fields, JBMDocumentNames.CALCPAGE+".lg07", parts, reader, doc);
		else if (marker.equals (a28))
			parseLine (a28fields, JBMDocumentNames.CALCPAGE+".lg08", parts, reader, doc);
		else if (marker.equals (a29))
			parseLine (a29fields, JBMDocumentNames.CALCPAGE+".lg09", parts, reader, doc);
		else if (marker.equals (a30))
			parseLine (a30fields, JBMDocumentNames.CALCPAGE+".lg10", parts, reader, doc);
		else if (marker.equals (a31))
			parseLine (a31fields, JBMDocumentNames.CALCPAGE+".lg11", parts, reader, doc);
		else if (marker.equals (a32))
			parseLine (a32fields, JBMDocumentNames.CALCPAGE+".lg12", parts, reader, doc);
		else if (marker.equals (a33))
			parseLine (a33fields, JBMDocumentNames.CALCPAGE+".lg13", parts, reader, doc);
		else if (marker.equals (a34))
			parseLine (a34fields, JBMDocumentNames.CALCPAGE+".lg14", parts, reader, doc);
		else if (marker.equals (a35))
			parseLine (a35fields, JBMDocumentNames.CALCPAGE+".lg15", parts, reader, doc);
		else if (marker.equals (a36))
			parseLine (a36fields, JBMDocumentNames.CALCPAGE+".lg16", parts, reader, doc);
		else if (marker.equals (a37))
			parseLine (a37fields, JBMDocumentNames.CALCPAGE+".lg17", parts, reader, doc);
		else if (marker.equals (a38))
			parseLine (a38fields, JBMDocumentNames.CALCPAGE+".lg18", parts, reader, doc);
		else if (marker.equals (a39))
			parseLine (a39fields, JBMDocumentNames.CALCPAGE+".lg19", parts, reader, doc);
		else if (marker.equals (tpx))
			parseMemo (tpxfields, JBMDocumentNames.TEXT, parts, reader, doc);
		else if (marker.equals (tt1))
			parseMemo (tt1fields, JBMDocumentNames.TEXT, parts, reader, doc);
		else if (marker.equals (tt2))
			parseMemo (tt2fields, JBMDocumentNames.TEXT, parts, reader, doc);
		else if (marker.equals (tt3))
			parseMemo (tt3fields, JBMDocumentNames.TEXT, parts, reader, doc);
		else if (marker.equals (tte))
			parseMemo (ttefields, JBMDocumentNames.TEXT, parts, reader, doc);
		else if (marker.equals (tbk))
			parseMemo (tbkfields, JBMDocumentNames.TEXT, parts, reader, doc);
		else if (marker.equals (tfs))
			parseMemo (tfsfields, JBMDocumentNames.TEXT, parts, reader, doc);
		else if (marker.equals (pp))
			parsePosition (ppfields, JBMDocumentNames.POSITIONS, parts, reader, doc);
		else if (marker.equals (pi))
			parsePosition (pifields, JBMDocumentNames.POSITIONS, parts, reader, doc);
		else if (marker.equals (pa))
			parsePosition (pafields, JBMDocumentNames.POSITIONS, parts, reader, doc);
		else if (marker.equals (pe))
			parsePosition (pefields, JBMDocumentNames.POSITIONS, parts, reader, doc);
		else if (marker.equals (pm))
			parsePosition (pmfields, JBMDocumentNames.POSITIONS, parts, reader, doc);
		else if (marker.equals (pu))
			parsePosition (pufields, JBMDocumentNames.POSITIONS, parts, reader, doc);
		else if (marker.equals (pq))
			parsePosition (pqfields, JBMDocumentNames.POSITIONS, parts, reader, doc);
		else if (marker.equals (pr))
			parsePosition (prfields, JBMDocumentNames.POSITIONS, parts, reader, doc);
		else if (marker.equals (ps))
			parsePosition (psfields, JBMDocumentNames.POSITIONS, parts, reader, doc);
		else if (marker.equals (pz))
			parsePosition (pzfields, JBMDocumentNames.POSITIONS, parts, reader, doc);
		else if (marker.equals (p1))
			parsePosition (p1fields, JBMDocumentNames.POSITIONS, parts, reader, doc);
		else if (marker.equals (p2))
			parsePosition (p2fields, JBMDocumentNames.POSITIONS, parts, reader, doc);
		else if (marker.equals (p3))
			parsePosition (p3fields, JBMDocumentNames.POSITIONS, parts, reader, doc);
		else if (marker.equals (p4))
			parsePosition (p4fields, JBMDocumentNames.POSITIONS, parts, reader, doc);
		else if (marker.equals (px))
			parsePosition (pxfields, JBMDocumentNames.POSITIONS, parts, reader, doc);
		else if (marker.equals (f1))
			parseLine (f1fields, null, parts, reader, doc);
		else if (marker.equals (f2))
			parseLine (f2fields, null, parts, reader, doc);
		else if (marker.length() > 4) {
			// wir sind wahrscheinlich im Text gelandet un der f�ngt zum Beispile mit einem #Format Zeichen an
			System.out.println("leeching " + marker);
			pushBack(line);
			readUnqualifiedLines(reader);
		} else if (marker.equals (X)) {
			// das Ende naht
			;
		} else {
			storeVerbatim (marker, "unrecognized", line, reader, doc);
		}
	}

	private void setupDocumentVersion (String createdBy) {
		double version = Double.parseDouble(createdBy.split(" ")[1].substring(1));

		if (version <= 3.52) {
			ppfields = pifields = pafields = pefields = pmfields = 
				pufields = pqfields = prfields = psfields = posfields3_52;
			p1fields = p2fields = p3fields = p4fields = titlefields3_52;
			pzfields = pzfields3_52;
			pxfields = pxfields3_52;
		} else {
			ppfields = pifields = pafields = pefields = pmfields = 
				pufields = pqfields = prfields = psfields = pzfields = posfields;
			p1fields = p2fields = p3fields = p4fields = titlefields;
			pxfields = pxfieldsDefault;
			pzfields = pzfieldsDefault;
		}
	}

	private void parseLine(String[] fields, String partName, String[] parts, 
			BufferedReader reader, DocumentPart doc)
	throws DocumentException
	{
		// extrahiere den relevanten part
		// partName sieht so aus: part.subpart.subsubpart etc
		DocumentPart docpart;
		int i=0;
		if (lastDocPart != null)
			docpart = lastDocPart;
		else {
			docpart = doc.addPart (partName);
		}

		i=0;	
		try {
			for (i=0; i < fields.length; i++) {
				docpart.putProperty (fields[i], 
						parts[i+1], // 0.te ist marker
						true, // lasse dirty state unver�ndert
						true); // Wert kam aud Datenstrom
			}

		} catch (java.lang.IndexOutOfBoundsException e) {
			// hier kommen wir hin, falls wir zu wenig felder in parts haben
			if (doUseDummies) {
				for (; i < fields.length; i++) {
					System.out.println("use dummy for " + fields[i]);
					docpart.putProperty (fields[i], 
							"",  // dem DocumentPart ist es �berlassen, ein sinnvollen defaultwert zu w�hlen
							false, // lasse dirty state unver�ndert
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

	private void parseMemo (String[] fields, String partName, String[] parts, BufferedReader reader, DocumentPart doc)
	throws BMFormatException, DocumentException
	{
		// extrahiere den relevanten part
		// partName sieht so aus: part.subpart.subsubpart etc
		int i=0;
		DocumentPart docpart = doc.addPart (partName);

		int textlines = -1;


		try {
			// an pos 1 steht die Anzahl der folgenden Textzeilen
			textlines = Integer.parseInt(parts[i+1]);

		} catch (NumberFormatException e) {
			textlines = -1; // relaxed mode
		} catch (Exception e) {
			// hier kommen wir hin, falls wir zu wenig felder in parts haben
			if (doUseDummies) {
				for (; i < fields.length; i++) {
					docpart.putProperty (fields[i], 
							null,  // dem DocumentPart ist es �berlassen, ein sinnvollen defaultwert zu w�hlen
							false, // lasse dirty state unver�ndert
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
			line = nextLine(reader);
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
			pushBack (line);

	}

	private void parsePosition (String[] fields, String partName, String[] parts, BufferedReader reader, DocumentPart doc)
	throws DocumentException
	{
		lastProcessedPosition (null, -1, -1);

		// extrahiere den relevanten part
		// partName sieht so aus: part.subpart.subsubpart etc
		int i;
		DocumentPart docpart = doc.addPart (partName);

		docpart = docpart.addPartToList(JBMDocumentNames.POSITIONLIST, JBMDocumentNames.POSITION);

		// positionskennung
		docpart.putProperty ("poskind", parts[0].substring(2,3), true, true);
		i=0;
		try {
			for (i=0; i < fields.length; i++) {
				docpart.putProperty (fields[i], 
						parts[i+1], // 0.te ist marker
						true, // lasse dirty state unver�ndert
						true); // Wert kam aus Datenstrom
			}
			lastOZ = parts[2];
			//System.out.println ("OZ: "+lastOZ);
		} catch (java.lang.IndexOutOfBoundsException e) {
			// hier kommen wir hin, falls wir zu wenig felder in parts haben
			if (doUseDummies) {
				for (; i < fields.length; i++) {
					System.out.println("use dummy for " + fields[i]);
					docpart.putProperty (fields[i], 
							null,  // dem DocumentPart ist es �berlassen, ein sinnvollen defaultwert zu w�hlen
							false, // lasse dirty state unver�ndert
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
				System.out.println("adding cruft:>"+parts[i+1]+"< at oz:"+lastOZ);
			}
		}

		// checken wir mal, ob wir noch Texte erwarten d�rfen ...

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
		lastProcessedPosition (docpart, allTextLines, shortTextLines);

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

	private void storeVerbatim (String marker, String partName, String line, BufferedReader reader, DocumentPart doc)
	throws BMFormatException, DocumentException
	{
		// extrahiere den relevanten part
		// partName sieht so aus: part.subpart.subsubpart etc
		DocumentPart docpart = doc.addPart (partName);

		// text lesen und im docPart setzen
		StringBuffer text = new StringBuffer();
		while (line != null) {
			text.append('\n').append(line);
			line = nextLine(reader);
			if (isQualifiedLine(line))
				break;
		}

		docpart.putProperty (marker, text, true, false); 
		pushBack (line);
	}

	private void lastProcessedPosition (DocumentPart docPart, int countTextLinesAhead, int countShortTextLinesAhead)
	{
		lastDocPart = docPart;
		expectTextLines = countTextLinesAhead;
		expectShortTextLines = countShortTextLinesAhead;
	}

	private String nextLine (BufferedReader reader) throws BMFormatException {
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
				String[] parts = line.split(FIELDSEPARATOR,-1);
				System.out.println(line+"\n");
				for (int i=0; i < parts.length; i++)
					System.out.println(i+".: " + parts[i]+"\n");
				throw new BMFormatException ("infiniloop at " + lineCounter);
			}
		}
		else {
			lastCounter = lineCounter;
			infiniLoop = 0;
		}
		//	System.out.println ("line >" + lineCounter + "<");
		return line;
	}

	private void pushBack (String line) {
		usePushbackLine = true;
		pushbackLine = line;
	}

	private void readUnqualifiedLines (BufferedReader reader) throws BMFormatException
	{
		//	System.out.println ("reading unqualified lines, textlines/short="+expectTextLines + "/" +expectShortTextLines);
		if (lastDocPart == null)
		{
			if (doSkipJunk)
				skipJunk(reader);
			else {
				throw new BMFormatException ("Bei Zeile " + (lineCounter+1) + " den Marker '"+ 
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
			int textlines = expectTextLines;
			boolean readAsMuchAsPossible = textlines == -1 || appendTextTooMany;

			while (i < textlines || readAsMuchAsPossible) {
				//System.out.println ((i+1) + " of " + textlines);
				line = nextLine(reader);
				i++;
				if (!isQualifiedLine(line)) {
					text.append(line).append('\n');
					if (expectShortTextLines == i) {
						shortText = text;
						text = longText;
					}
				}

				if (readAsMuchAsPossible && isQualifiedLine(line)) {
					pushBack (line);
					break;
				}
			}

			if (expectShortTextLines < 1) {
				longText = text;
			}

			lastDocPart.putProperty ("shortText", shortText, true, true); 
			lastDocPart.putProperty ("longText", longText, true, true); 
			/*
	    if (lastOZ.equals("48. 7. 720"))
		System.out.println ("textlines="+textlines+"\ni="+i+"\nlongtext:\n"+longText.toString()+"\nshortText:\n"+shortText.toString());
			 */

			//	    System.out.println (longText.toString());
			expectTextLines = 0;
			expectShortTextLines = 0;

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
