package bn.inference;

import java.util.List;

import bn.core.*;

public class LikelihoodWeighting {
	
	// store a weight and the given assignment created by a sample
	public class Sample {
		public double weight;
		public Assignment event;
	}
	
	public Distribution likelihoodWeighting(int N, BayesianNetwork bn, RandomVariable X, Assignment e) {
		Distribution D = new Distribution(X);
		D.initialize(X);
		for (int j=1; j<=N; j++) {
			Sample ws = weightedSample(bn, e);
			double w = D.get(ws.event.get(X));
			D.put(ws.event.get(X), ws.weight+w);
		}
		D.normalize(); // normalize the distribution
		return D;
	}
	
	public Sample weightedSample(BayesianNetwork bn, Assignment e) {
		Sample S = new Sample();
		S.event = new Assignment();
		S.weight = 1;
		List<RandomVariable> list = bn.getVariableListTopologicallySorted();
		for (RandomVariable rv : list) {
			if (e.containsKey(rv)) {
				S.event.put(rv, e.get(rv));
				S.weight *= bn.getProb(rv, S.event);
			} else {
				Distribution d = new Distribution();
				for (int i=0; i<rv.getDomain().size(); i++) {
					Assignment temp = new Assignment(S.event);
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
						S.event.set(rv, rv.getDomain().get(j));
						break;
					}
				}
			}
		}
		return S;
	}

}
