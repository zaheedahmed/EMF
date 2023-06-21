package cmu.pasta.mu2.test;

import com.pholser.junit.quickcheck.generator.InRange;
import edu.berkeley.cs.jqf.fuzz.JQF;
import edu.berkeley.cs.jqf.fuzz.difffuzz.DiffFuzz;
import org.junit.runner.RunWith;
import org.apache.commons.lang.StringUtils;

@RunWith(JQF.class)
public class CommonsTest {

	@DiffFuzz
	public String testCapitalize(String str){
		return StringUtils.capitalize(str);
	}


}
