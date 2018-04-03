package bn.inference;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import bn.core.*;
import bn.parser.*;

public class MyBNInferencer {

	public static void main(String[] args) {
		
		EnumerationAsk ea = new EnumerationAsk();
		
		if (args[0].contains(".xml")){
			XMLBIFParser xparser = new XMLBIFParser();
			try {
				BayesianNetwork bn = xparser.readNetworkFromFile(args[0]);
				Assignment asgmt = new Assignment();
				for (int i=2; i<args.length; i+=2) {
					asgmt.put(bn.getVariableByName(args[i]), args[i+1]);
				}
				
				long startTime = System.currentTimeMillis();
				Distribution d = ea.ask(bn, bn.getVariableByName(args[1]), asgmt);
				long endTime = System.currentTimeMillis();
				
				System.out.println(d);
				System.out.println("Runtime: " + (endTime-startTime) + "ms");
			} catch (IOException | ParserConfigurationException | SAXException e) {
				e.printStackTrace();
			}
			
		} else {
			BIFLexer blexer;
			BIFParser bparser;
			try {
				blexer = new BIFLexer(new FileInputStream(args[0]));
				bparser = new BIFParser(blexer);
				BayesianNetwork bn = bparser.parseNetwork();
				Assignment asgmt = new Assignment();
				for (int i=2; i<args.length; i+=2) {
					asgmt.put(bn.getVariableByName(args[i]), args[i+1]);
				}
				
				long startTime = System.currentTimeMillis();
				Distribution d = ea.ask(bn, bn.getVariableByName(args[1]), asgmt);
				long endTime = System.currentTimeMillis();
				
				System.out.println(d);
				System.out.println("Runtime: " + (endTime-startTime) + "ms");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}

	}

}