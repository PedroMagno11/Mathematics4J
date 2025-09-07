package br.com.pedromagno.algebra;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LinearFunctionTests {
    @Test
    public void testIncreasingFunction(){
        LinearFunction f = LinearFunction.of(2,3);
        Assertions.assertEquals(LinearFunction.LinearFunctionType.INCREASING, f.getType());
        Assertions.assertEquals(-1.5, f.root(), 1e-9);
        Assertions.assertEquals(7.0, f.apply(2), 1e-9);
    }

    @Test
    void testDecreasingFunction() {
        LinearFunction f = LinearFunction.of(-1, 5);
        Assertions.assertEquals(LinearFunction.LinearFunctionType.DECREASING, f.getType());
        Assertions.assertEquals(5.0, f.root(), 1e-9);
    }

    @Test
    void testConstantFunction() {
        LinearFunction f = LinearFunction.of(0, 7);
        Assertions.assertEquals(LinearFunction.LinearFunctionType.CONSTANT, f.getType());
        Assertions.assertEquals(7.0, f.root());
    }

    @Test
    void testZeroFunction() {
        LinearFunction f = LinearFunction.of(0, 0);
        Assertions.assertEquals(LinearFunction.LinearFunctionType.CONSTANT, f.getType());
        Assertions.assertEquals(0, f.root());
    }
}
