import br.com.pedromagno.Calculator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CalculatorTests {
    @Test
    public void testAdd() {
        Calculator calculator = new Calculator();
        Assertions.assertEquals(2, calculator.add(1,1), "1 + 1 should equal 2");
    }
}
