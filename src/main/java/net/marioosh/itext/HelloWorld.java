package net.marioosh.itext;

import java.io.FileOutputStream;
import java.io.IOException;
import net.marioosh.jasper.model.Page;
import net.marioosh.jasper.model.PageFactory;
 
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Header;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Element;
 
/**
 * First iText example: Hello World.
 */
public class HelloWorld {
 
    /** Path to the resulting PDF file. */
    public static final String RESULT = "hello.pdf";
 
    /**
     * Creates a PDF file: hello.pdf
     * @param    args    no arguments needed
     */
    public static void main(String[] args)
    	throws DocumentException, IOException {
    	new HelloWorld().createPdf(RESULT);
    }
 
    /**
     * Creates a PDF document.
     * @param filename the path to the new PDF document
     * @throws    DocumentException 
     * @throws    IOException 
     */
    public void createPdf(String filename)
	throws DocumentException, IOException {
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
        writer.setInitialLeading(16);
        
        /**
         * 595,842 
         */
        document.setPageSize(PageSize.A4);
        
        document.open();
        long start = System.currentTimeMillis();
        BaseFont bf = BaseFont.createFont(BaseFont.TIMES_ROMAN, BaseFont.CP1250, BaseFont.NOT_EMBEDDED);
        Font f = new Font(bf, 10, Font.NORMAL, BaseColor.BLACK);
        PdfContentByte dc = writer.getDirectContent();
        for(int i = 0; i< 10; i++) {
        	
        	Page p = PageFactory.createPage();
        	document.newPage();
        	
        	/**
        	 * draw to direct content
        	 * 
        	dc.beginText();
        	dc.setFontAndSize(bf, 10);
        	ColumnText column = new ColumnText(dc);
        	Rectangle rect = new Rectangle(30,800,550,100,0);
        	column.setSimpleColumn(new Paragraph(p.getT1(),f),
        			rect.getLeft(), rect.getBottom(),
        			rect.getRight(), rect.getTop(),
        			18, Element.ALIGN_JUSTIFIED);
        	column.go();

        	column = new ColumnText(dc);
        	rect = new Rectangle(30,600,550,100,0);
        	column.setSimpleColumn(new Paragraph(p.getT2(),f),
        			rect.getLeft(), rect.getBottom(),
        			rect.getRight(), rect.getTop(),
        			18, Element.ALIGN_JUSTIFIED);
        	column.go();
        	
        	column = new ColumnText(dc);
        	rect = new Rectangle(30,400,550,100,0);
        	column.setSimpleColumn(new Paragraph(p.getT3(),f),
        			rect.getLeft(), rect.getBottom(),
        			rect.getRight(), rect.getTop(),
        			18, Element.ALIGN_JUSTIFIED);
        	column.go();
        	
        	column = new ColumnText(dc);
        	rect = new Rectangle(30,200,550,100,0);
        	column.setSimpleColumn(new Paragraph(p.getT4(),f),
        			rect.getLeft(), rect.getBottom(),
        			rect.getRight(), rect.getTop(),
        			18, Element.ALIGN_JUSTIFIED);
        	column.go();
        	// ColumnText.showTextAligned(dc, Element.ALIGN_LEFT, p1, 30, 172, 0);
        	// dc.showTextAligned(Element.ALIGN_LEFT, p.getT1(), 30, 788, 0);
        	dc.endText();
        	*/
        	
        	/**
        	 * using PdfTable
        	 */
        	PdfPTable table = new PdfPTable(3);
        	// table.setWidths(new float[]{300,100,200});
        	table.setTotalWidth(520);
        	
        	PdfPCell cell;

        	cell = new PdfPCell(new Phrase(p.getI1(),f));
        	cell.setColspan(3);
        	cell.setBorder(Rectangle.NO_BORDER);
        	cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
        	table.addCell(cell);

        	cell = new PdfPCell(new Phrase(p.getI2(),f));
        	cell.setBorder(Rectangle.NO_BORDER);
        	cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
        	cell.setRowspan(2);
        	table.addCell(cell);
        	
        	cell = new PdfPCell(new Phrase(p.getI3(),f));
        	cell.setBorder(Rectangle.NO_BORDER);
        	cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
        	table.addCell(cell);
        	
        	cell = new PdfPCell(new Phrase(p.getII1(),f));
        	cell.setBorder(Rectangle.NO_BORDER);
        	cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
        	table.addCell(cell);
        	
        	cell = new PdfPCell(new Phrase(p.getII2(),f));
        	cell.setBorder(Rectangle.NO_BORDER);        	
        	cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
        	table.addCell(cell);
        	
        	cell = new PdfPCell(new Phrase(p.getII3(),f));
        	cell.setBorder(Rectangle.NO_BORDER);
        	cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
        	table.addCell(cell);
        	
        	table.writeSelectedRows(0, -1, 40, 820, dc);

        	/*
        	Paragraph p2 = new Paragraph(p.getT2(),f);
        	p1.setAlignment(Element.ALIGN_JUSTIFIED);
        	document.add(p2);
        	Paragraph p3 = new Paragraph(p.getT3(),f);
        	p1.setAlignment(Element.ALIGN_JUSTIFIED);
        	document.add(p3);
        	Paragraph p4 = new Paragraph(p.getT4(),f);
        	p1.setAlignment(Element.ALIGN_JUSTIFIED);
        	document.add(p4);
        	document.addHeader("inspired by", "William Shakespeare");
        	*/
        }
        document.close();
		long seconds = (System.currentTimeMillis() - start)/1000;
		System.out.printf("%-23s: %s\n", "TIME", seconds/60+" minutes, " + seconds%60 + " seconds"); 
        
    }
}