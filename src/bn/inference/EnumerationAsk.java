package bn.inference;

import java.util.ArrayList;
import java.util.List;

import bn.core.Assignment;
import bn.core.BayesianNetwork;
import bn.core.Distribution;
import bn.core.RandomVariable;

public class EnumerationAsk implements Inferencer {
	
	@Override
	public Distribution ask(BayesianNetwork bn, RandomVariable X, Assignment e) {
		Distribution Q = new Distribution(X);
		for (Object xi : X.getDomain()) {
			Assignment temp = e.copy();
			temp.set(X, xi);
			Q.put(xi, enumerateAll(bn, (ArrayList<RandomVariable>)bn.getVariableListTopologicallySorted(), temp));
		}
		Q.normalize();
		return Q;
	}
	
	@SuppressWarnings("unchecked")
	public double enumerateAll(BayesianNetwork bn, ArrayList<RandomVariable> vars, Assignment e) {
		ArrayList<RandomVariable> restVars = new ArrayList<RandomVariable>();
		restVars = (ArrayList<RandomVariable>) vars.clone();
		
		if (vars.size()==0) return 1.0;
		
		RandomVariable Y = vars.get(0);
		
		restVars.remove(0);
		
		if (e.variableSet().contains(Y)) {
			Assignment tempe = e.copy();
			double getProb=bn.getProb(Y, tempe);
			return getProb*enumerateAll(bn, restVars, tempe);
		} else {
			double rest=0;
			for (Object yi : Y.getDomain()) {
				Assignment tempe = e.copy();
				tempe.set(Y, yi);
				rest+=(bn.getProb(Y, tempe)*enumerateAll(bn, restVars, tempe));
			}
			return rest;
		}
	}
	
}

