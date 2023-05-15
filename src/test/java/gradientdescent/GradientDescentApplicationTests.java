package gradientdescent;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GradientDescentApplicationTests {

	@Autowired
	private GradientDescentCalculator gradientDescentCalculator;

	@Test
	void contextLoads() {
		gradientDescentCalculator.calculate();
	}

}
