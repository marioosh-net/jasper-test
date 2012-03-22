package net.marioosh.jasper;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.HashMap;
import net.marioosh.jasper.datasource.HSQLDBDataSource;
import net.marioosh.jasper.datasource.PGSqlDataSource;
import net.marioosh.jasper.datasource.SQLiteDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import net.sf.jasperreports.engine.fill.JRFileVirtualizer;
import net.sf.jasperreports.engine.fill.JRGzipVirtualizer;
import net.sf.jasperreports.engine.fill.JRSwapFileVirtualizer;
import net.sf.jasperreports.engine.query.JRXPathQueryExecuterFactory;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRSwapFile;
import net.sf.jasperreports.engine.util.JRXmlUtils;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;

public class JasperTest {

	private final Logger log = Logger.getLogger(JasperTest.class);
	
	static String dataSource;
	static String jrxml;
	static String pdf;
	static String xpath;
	static boolean virtualizer;

	public static void main(String[] args) {

		// sax parser for jasper
		System.setProperty(
			    "org.xml.sax.driver",
			    "org.apache.xerces.parsers.SAXParser"
			    );
		
		Options options = new Options();
		options.addOption("ds", true, "XML datasource (empty datasource by default)");
		options.addOption("h", false, "help");
		options.addOption("xp", true, "xpath for datasource (/pages/page as default)");
		options.addOption("v", false, "use swap virtualizer");
		CommandLineParser parser = new PosixParser();

		try {
			CommandLine cmd = parser.parse(options, args);

			if (cmd.hasOption("h") || cmd.getArgs().length < 1) {
				HelpFormatter formatter = new HelpFormatter();
				System.out.println("Jasper Reports Test tool");
				formatter.printHelp(90, "java -jar jasper.jar [-v] [-xp <xpath>] [-ds <xml_datasource>] <jrxml_file> [<pdf_file>]", "If pdf_file is ommited, pdf will be written to stdout", options, "");
				System.out.println("");
				return;
			}
			if (cmd.hasOption("ds")) {
				dataSource = cmd.getOptionValue("ds");
			}
			if (cmd.hasOption("v")) {
				virtualizer = true;
			}
			if (cmd.hasOption("xp")) {
				xpath = cmd.getOptionValue("xp");
			}
			jrxml = cmd.getArgs()[0];
			if (cmd.getArgs().length > 1) {
				pdf = cmd.getArgs()[1];
			}

			new JasperTest();
			
		} catch (ParseException e) {
			System.err.println(e.getMessage());
		}
	}

	public JasperTest() {
		long start = System.currentTimeMillis();
		System.out.printf("%-23s: %s\n", "START", new Date()); 

		JasperPrint jasperPrint;
		try {
			JasperReport jasperReport = JasperCompileManager.compileReport(new FileInputStream(jrxml));
			if (dataSource == null) {
				System.err.println("WARNING: Empty data source!");
				jasperPrint = JasperFillManager.fillReport(jasperReport, null, new JREmptyDataSource());
			} else {
				HashMap<String, Object> params = new HashMap<String,Object>();
				
				// 1
				JRXmlDataSource ds1 = new JRXmlDataSource(new FileInputStream(dataSource), xpath!=null ? xpath : "/pages/page");
				// szybkie szukanie nawet w 100tys rekordow
				// JRXmlDataSource ds1 = new JRXmlDataSource(new FileInputStream(dataSource), "/pages/page[imie-nazwisko='Tomasz Kowalski']");
				
				// 2
				//Document document = JRXmlUtils.parse(JRLoader.getLocationInputStream(dataSource));
				//params.put(JRXPathQueryExecuterFactory.PARAMETER_XML_DATA_DOCUMENT, document);
				
				// 3
				//JRResultSetDataSource ds2 = new JRResultSetDataSource(new PGSqlDataSource("pdf").getAll(100000)); 
				
				if(virtualizer) {
					// virtualizer
					
					// JRGzipVirtualizer virtualizer = new JRGzipVirtualizer(300); 
					JRSwapFileVirtualizer virtualizer = new JRSwapFileVirtualizer(2, new JRSwapFile(".", 1024, 1));
					// JRFileVirtualizer virtualizer = new JRFileVirtualizer(2);
					virtualizer.setReadOnly(false);
					params.put(JRParameter.REPORT_VIRTUALIZER, virtualizer);
				}
		
				long start1 = System.currentTimeMillis();
				long seconds = (System.currentTimeMillis() - start)/1000;
				System.out.printf("%-23s: %s\n", "dataSource", seconds/60+" minutes, " + seconds%60 + " seconds"); 
				
				// 1	
				jasperPrint = JasperFillManager.fillReport(jasperReport, params, ds1);
				// JasperFillManager.fillReportToFile(jasperReport, "report.jprint", params, ds1); // zapis do pliku, potem mozna laczyc
				
				// 2
				//jasperPrint = JasperFillManager.fillReport(jasperReport, params);
				
				// 3
				//jasperPrint = JasperFillManager.fillReport(jasperReport, params, ds2);
				
				seconds = (System.currentTimeMillis() - start1)/1000;
				System.out.printf("%-23s: %s\n", "fillReport", seconds/60+" minutes, " + seconds%60 + " seconds");
			}
			long start2 = System.currentTimeMillis();
			if(pdf != null) {
				JasperExportManager.exportReportToPdfFile(jasperPrint, pdf);
			} else {
				JasperExportManager.exportReportToPdfStream(jasperPrint, System.out);
			}
			long seconds = (System.currentTimeMillis() - start2)/1000;
			System.out.printf("%-23s: %s\n", "exportReportToPdfFile", seconds/60+" minutes, " + seconds%60 + " seconds");
			
			seconds = (System.currentTimeMillis() - start)/1000;
			System.out.printf("%-23s: %s\n", "FULL TIME", seconds/60+" minutes, " + seconds%60 + " seconds");
		} catch (JRException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
