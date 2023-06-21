package cmu.pasta.mu2.test;

import cmu.pasta.mu2.Bisect;
import com.pholser.junit.quickcheck.generator.InRange;
import edu.berkeley.cs.jqf.fuzz.JQF;
import edu.berkeley.cs.jqf.fuzz.difffuzz.Comparison;
import edu.berkeley.cs.jqf.fuzz.difffuzz.DiffFuzz;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JQF.class)
public class BisectTest {

	private static final double EPSILON = 0.1;
	@DiffFuzz
	public Double testSqrt(@InRange(minDouble = 1.0, maxDouble = 200.0) Double n){
		Bisect bisect = new Bisect();
		bisect.setEpsilon(EPSILON);
		return bisect.sqrt(n);
	}

}
