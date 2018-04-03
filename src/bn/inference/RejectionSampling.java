package bn.inference;

import java.util.List;
import bn.core.*;

public class RejectionSampling {

	public Distribution rejectSamp(int N, BayesianNetwork bn, RandomVariable X, Assignment e) {
		Distribution D = new Distribution(X);
		D.initialize(X); // initialize x with random values for the variables in the nonevidence variables in bn
		for (int j=1; j<=N; j++) {
			Assignment sample = priorSample(bn);
			if (isConsistent(e, sample)) {
				D.put(sample.get(X), D.get(sample.get(X))+1);
			}
		}
		D.normalize();;
		return D;
	}
	
	public Assignment priorSample(BayesianNetwork bn) {
		Assignment A = new Assignment();
		List<RandomVariable> list = bn.getVariableListTopologicallySorted();
		for (RandomVariable rv : list) {
			Distribution d = new Distribution();
			for (int i=0; i<rv.getDomain().size(); i++) {
				Assignment temp = new Assignment(A);
				temp.set(rv, rv.getDomain().get(i));
				double rest = bn.getProb(rv, temp);
				d.put(rv.getDomain().get(i), rest);
			}
			d.normalize();
			double random = Math.random();
			double test = 0.0;
			for (int j=0; j<rv.getDomain().size(); j++) {
				test += d.get(rv.getDomain().get(j));
				if (random<=test) {
					A.set(rv, rv.getDomain().get(j));
					break;
				}
			}
		}
		return A;
	}
	
	public boolean isConsistent(Assignment a, Assignment b) {
		for (RandomVariable rva : a.keySet()) {
			for (RandomVariable rvb : b.keySet()) {
				if ((rva.getName()).equals(rvb.getName()) && !(a.get(rva)).equals(b.get(rvb))) {
					return false;
				}
			}
		}
		return true; // no contradictions return true
	}

}
