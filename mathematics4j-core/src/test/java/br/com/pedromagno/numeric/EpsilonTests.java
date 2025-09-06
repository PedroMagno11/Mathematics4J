package br.com.pedromagno.numeric;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EpsilonTests {
    @Test
    public void nearlyEquals_largeMagnitudes_relativeComparison_Test(){
        double tol = 1e-12;
        Assertions.assertTrue(Epsilon.nearlyEqual(1e16, 1e16 + 1, tol));
    }

    @Test
    void nearlyEqual_nan_isAlwaysFalse() {
        Assertions.assertFalse(Epsilon.nearlyEqual(Double.NaN, 0.0, 1e-12));
        Assertions.assertFalse(Epsilon.nearlyEqual(0.0, Double.NaN, 1e-12));
    }

    @Test
    void isZero_subnormal_true() {
        Assertions.assertTrue(Epsilon.isZero(4.9e-324, 1e-12));
    }

    @Test
    void infinities_behavior() {
        double tol = 1e-12;
        Assertions.assertTrue(Epsilon.nearlyEqual(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, tol));
        Assertions.assertTrue(Epsilon.nearlyEqual(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, tol));
        Assertions.assertFalse(Epsilon.nearlyEqual(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, tol));
        Assertions.assertFalse(Epsilon.nearlyEqual(Double.POSITIVE_INFINITY, 1e308, tol));
    }

    @Test
    void ulpDiff_adjacent() {
        Assertions.assertEquals(1L, Epsilon.unitInTheLastPlaceDiff(1.0, Math.nextUp(1.0)));
    }
}
