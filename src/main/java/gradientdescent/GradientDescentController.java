package gradientdescent;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GradientDescentController {

    private final GradientDescentCalculator calculator;

    @GetMapping("/solve")
    public Answer solve() {
        return calculator.calculateDefault();
    }
}
