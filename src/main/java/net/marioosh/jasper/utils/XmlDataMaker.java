package net.marioosh.jasper.utils;

import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import net.marioosh.jasper.model.Page;
import net.marioosh.jasper.model.PageFactory;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.basic.NullConverter;

public class XmlDataMaker {
	private static String out;
	private static int count = 10; 
	
	public static void main(String[] args) {
		
		Options options = new Options();
		options.addOption("h", false, "help");
		options.addOption("n", true, "how much records");
		CommandLineParser parser = new PosixParser();

		try {
			CommandLine cmd = parser.parse(options, args);

			if (cmd.hasOption("h")) {
				HelpFormatter formatter = new HelpFormatter();
				System.out.println("Jasper Reports Test tool");
				formatter.printHelp(90, "java -jar jasper-data.jar [-n <rows count>] [<out.xml>]", "If out.xml is ommited, output xml will be written to stdout", options, "");
				System.out.println("");
				return;
			}
			if (cmd.hasOption("n")) {
				count = Integer.parseInt(cmd.getOptionValue("n"));
			}
			if (cmd.getArgs().length > 0) {
				out = cmd.getArgs()[0];
			}

			if(out != null) {
				new XmlDataMaker().prepareXstream(count, new FileOutputStream(out));
			} else {
				new XmlDataMaker().prepareXstream(count, System.out);
			}
			
		} catch (ParseException e) {
			System.err.println(e.getMessage());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}		
	}

	public void prepare(int count, String fileName) {
		XMLEncoder encoder;
		try {
			encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(fileName)));
			for (int i = 0; i < count; i++) {
				Page p = PageFactory.createPage();
				encoder.writeObject(p);
			}
			encoder.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}
	
	public void prepareXstream(int count, OutputStream out) {
		XStream xstream = new XStream();
		xstream.registerConverter(new NullConverter());
		xstream.alias("page", Page.class);
		xstream.alias("pages", List.class);
	
		List<Page> pages = new ArrayList<Page>();
		for(int i = 0; i< count; i++) {
			Page p = PageFactory.createPage();
			pages.add(p);
		}
		xstream.toXML(pages, new BufferedOutputStream(out));

	}
}
