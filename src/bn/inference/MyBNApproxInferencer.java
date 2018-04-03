package bn.inference;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import bn.core.*;
import bn.parser.*;

public class MyBNApproxInferencer {

	public static void main(String[] args) {
		RejectionSampling rs = new RejectionSampling();
		LikelihoodWeighting lw = new LikelihoodWeighting();
		
		if (args[1].contains(".xml")){
			
			XMLBIFParser xparser = new XMLBIFParser();
			
			try {
				int samples = Integer.parseInt(args[0]);
				BayesianNetwork bn = xparser.readNetworkFromFile(args[1]);
				Assignment asgmt = new Assignment();
				for (int i=3; i<args.length; i+=2) {
					asgmt.put(bn.getVariableByName(args[i]), args[i+1]);
				}
				
				long startTime = System.currentTimeMillis();
				Distribution d = rs.rejectSamp(samples, bn, bn.getVariableByName(args[2]), asgmt);
				Distribution d2 = lw.likelihoodWeighting(samples, bn, bn.getVariableByName(args[2]), asgmt);
				long endTime = System.currentTimeMillis();
				
				System.out.println("By Rejection Sampling: " + d);
				System.out.println("By Likelihood Weighting: " + d2);
				System.out.println("Runtime: " + (endTime-startTime) + "ms");
			} catch (IOException | ParserConfigurationException | SAXException e) {
				e.printStackTrace();
			}
			
		} else {
			BIFParser bparser;
			try {
				bparser = new BIFParser(new FileInputStream(args[1]));
				int samples = Integer.parseInt(args[0]);
				BayesianNetwork bn = bparser.parseNetwork();
				Assignment asgmt = new Assignment();
				for (int i=3; i<args.length; i+=2) {
					asgmt.put(bn.getVariableByName(args[i]), args[i+1]);
				}
				
				long startTime = System.currentTimeMillis();
				Distribution d = rs.rejectSamp(samples, bn, bn.getVariableByName(args[2]), asgmt);
				Distribution d2 = lw.likelihoodWeighting(samples, bn, bn.getVariableByName(args[2]), asgmt);
				long endTime = System.currentTimeMillis();
				
				System.out.println("By Rejection Sampling: " + d);
				System.out.println("By Likelihood Weighting: " + d2);
				System.out.println("Runtime: " + (endTime-startTime) + "ms");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}


	}

}
