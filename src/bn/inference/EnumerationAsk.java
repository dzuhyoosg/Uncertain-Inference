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
		for (int i = 0; i < X.getDomain().size(); i++) {
			Assignment temp = new Assignment();
			temp.set(X, X.getDomain().get(i));
			Q.put(X.getDomain().get(i), enumerateAll(bn, bn.getVariableListTopologicallySorted(), temp));
		}
		Q.normalize();
		return Q;
	}
	
	public double enumerateAll(BayesianNetwork bn, List<RandomVariable> rv, Assignment e) {
		List<RandomVariable> vars = new ArrayList<RandomVariable>();
		if (vars.size()==0) return 1.0;
		
		for(int i=0; i<vars.size();i++) {
			vars.add(rv.get(i));
		}
		
		RandomVariable Y = vars.get(0);
		if (e.containsKey(Y)) {
			e = e.copy();
			return bn.getProb(Y, e)*enumerateAll(bn, vars, e);
		} else {
			double rest=0;
			for (int i = 0; i < Y.getDomain().size(); i++) {
				Assignment tempA = new Assignment();
				tempA.set(Y, Y.getDomain().get(i));
				rest+=(bn.getProb(Y, tempA)*enumerateAll(bn, vars, tempA));
			}
			return rest;
		}
	}

}
