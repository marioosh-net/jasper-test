package net.marioosh.itext;

import java.io.FileOutputStream;
import java.io.IOException;
 
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

public class StampText {
    /** A resulting PDF file. */
    public static final String RESULT1
        = "hello1.pdf";
    /** A resulting PDF file. */
    public static final String RESULT2
        = "hello2.pdf";
    /** A resulting PDF file. */
    public static final String RESULT3
        = "hello3.pdf";
 
    /**
     * Main method.
     * @param args no arguments needed
     * @throws DocumentException 
     * @throws IOException
     */
    public static void main(String[] args)
        throws DocumentException, IOException {
        HelloWorldLandscape1.main(args);
        HelloWorldLandscape2.main(args);
        stamp(HelloWorldLandscape1.RESULT, RESULT1);
        stampIgnoreRotation(HelloWorldLandscape1.RESULT, RESULT2);
        stamp(HelloWorldLandscape2.RESULT, RESULT3);
 
    }
 
    /**
     * Manipulates a PDF file src with the file dest as result
     * @param src the original PDF
     * @param dest the resulting PDF
     * @throws IOException
     * @throws DocumentException
     */
    public static void stamp(String src, String dest)
        throws IOException, DocumentException {
        PdfReader reader = new PdfReader(src);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
        PdfContentByte canvas = stamper.getOverContent(1);
        ColumnText.showTextAligned(canvas,
                Element.ALIGN_LEFT, new Phrase("Hello people!"), 36, 540, 0);
        stamper.close();
    }
 
    /**
     * Manipulates a PDF file src with the file dest as result
     * @param src the original PDF
     * @param dest the resulting PDF
     * @throws IOException
     * @throws DocumentException
     */
    public static void stampIgnoreRotation(String src, String dest)
        throws IOException, DocumentException {
        PdfReader reader = new PdfReader(src);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
        stamper.setRotateContents(false);
        PdfContentByte canvas = stamper.getOverContent(1);
        ColumnText.showTextAligned(canvas,
                Element.ALIGN_LEFT, new Phrase("Hello people!"), 36, 540, 0);
        stamper.close();
    }
}