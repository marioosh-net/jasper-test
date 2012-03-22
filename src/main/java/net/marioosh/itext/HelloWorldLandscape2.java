package net.marioosh.itext;


import java.io.FileOutputStream;
import java.io.IOException;
 
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
 
/**
 * A possible way to create a document in the landscape format.
 */
public class HelloWorldLandscape2 {
 
    /** Path to the resulting PDF file. */
    public static final String RESULT
        = "hello_landscape2.pdf";
 
    /**
     * Creates a PDF file: hello_landscape2.pdf
     * @param    args    no arguments needed
     */
    public static void main(String[] args)
        throws DocumentException, IOException {
        // step 1
    	// landscape format because width > height
        Document document = new Document(new Rectangle(792, 612));
        // step 2
        PdfWriter.getInstance(document, new FileOutputStream(RESULT));
        // step 3
        document.open();
        // step 4
        document.add(new Paragraph("Hello World"));
        // step 5
        document.close();
    }
}