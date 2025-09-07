package br.com.pedromagno.numeric;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class IntervalTests {
    @Test
    @DisplayName("closed(lower==upper) => ponto degenerado válido")
    void closed_equal_bounds_is_point(){
        Interval p = Interval.closed(1.0, 1.0);
        Assertions.assertFalse(p.isEmpty());
        Assertions.assertTrue(p.isDegenerate());
        Assertions.assertEquals(1.0, p.getLower());
        Assertions.assertEquals(1.0, p.getUpper());
        Assertions.assertEquals(Interval.IntervalType.CLOSED, p.getLowerType());
        Assertions.assertEquals(Interval.IntervalType.CLOSED, p.getUpperType());
    }

    @Test
    @DisplayName("lower > upper deve lançar IllegalArgumentException")
    void lower_greater_than_upper_throws(){
        Assertions.assertThrows(IllegalArgumentException.class, () -> Interval.closed(2.0, 1.0));
        Assertions.assertThrows(IllegalArgumentException.class, () -> Interval.closed(5.0, -1.0));
    }

    @Test
    @DisplayName("NaN em limites deve lançar IllegalArgumentException")
    void nan_endpoints_throw() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> Interval.closed(Double.NaN, 1.0));
        Assertions.assertThrows(IllegalArgumentException.class, () -> Interval.closed(0.0, Double.NaN));
    }

    @Test
    @DisplayName("Infinitos são aceitos")
    void infinities_allowed() {
        Interval i = Interval.closed(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        Assertions.assertFalse(i.isEmpty());
        Assertions.assertTrue(i.contains(0.0));
        Assertions.assertTrue(i.contains(1e300));
        Assertions.assertTrue(i.contains(-1e300));
    }

    @Test
    @DisplayName("contains respeita bordas abertas/fechadas")
    void contains_respects_bound_types() {
        Interval open = Interval.open(0.0, 1.0);
        Interval closed = Interval.closed(0.0, 1.0);
        Interval leftOpenRightClosed = Interval.openClosed(0.0, 1.0);
        Interval leftClosedRightOpen = Interval.closedOpen(0.0, 1.0);

        Assertions.assertFalse(open.contains(0.0));
        Assertions.assertFalse(open.contains(1.0));
        Assertions.assertTrue(open.contains(0.5));

        Assertions.assertTrue(closed.contains(0.0));
        Assertions.assertTrue(closed.contains(1.0));
        Assertions.assertTrue(closed.contains(0.5));

        Assertions.assertFalse(leftOpenRightClosed.contains(0.0));
        Assertions.assertTrue(leftOpenRightClosed.contains(1.0));

        Assertions.assertTrue(leftClosedRightOpen.contains(0.0));
        Assertions.assertFalse(leftClosedRightOpen.contains(1.0));
    }

    @Test
    @DisplayName("EMPTY nunca contém nada; NaN nunca é contido")
    void empty_and_nan_contains() {
        Interval e = Interval.empty();
        Assertions.assertTrue(e.isEmpty());
        Assertions.assertFalse(e.contains(0.0));
        Assertions.assertFalse(e.contains(Double.NaN));

        Interval i = Interval.closed(0.0, 1.0);
        Assertions.assertFalse(i.contains(Double.NaN));
    }

    @Test
    @DisplayName("contains com infinitos nos limites")
    void contains_with_infinities() {
        Interval leftInf = Interval.open(Double.NEGATIVE_INFINITY, 0.0);
        Assertions.assertTrue(leftInf.contains(-1e9));
        Assertions.assertFalse(leftInf.contains(0.0)); // aberto à direita

        Interval rightInf = Interval.closed(0.0, Double.POSITIVE_INFINITY);
        Assertions.assertTrue(rightInf.contains(0.0));
        Assertions.assertTrue(rightInf.contains(1e300));
    }

    @Test
    @DisplayName("length: regular, ponto, vazio, infinito")
    void length_cases() {
        Assertions.assertEquals(1.0, Interval.open(0.0, 1.0).length(), 0.0);
        Assertions.assertEquals(0.0, Interval.closed(1.0, 1.0).length(), 0.0);
        Assertions.assertEquals(0.0, Interval.empty().length(), 0.0);

        Assertions.assertEquals(Double.POSITIVE_INFINITY,
                Interval.closed(0.0, Double.POSITIVE_INFINITY).length());

        Assertions.assertEquals(Double.POSITIVE_INFINITY,
                Interval.closed(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY).length());
    }

    @Test
    @DisplayName("midpoint: regular, ponto, vazio, infinito")
    void midpoint_cases() {
        Assertions.assertEquals(0.5, Interval.closed(0.0, 1.0).midPoint(), 0.0);
        Assertions.assertEquals(1.0, Interval.closed(1.0, 1.0).midPoint(), 0.0);
        Assertions.assertTrue(Double.isNaN(Interval.empty().midPoint()));
        Assertions.assertTrue(Double.isNaN(Interval.open(0.0, Double.POSITIVE_INFINITY).midPoint()));
        Assertions.assertTrue(Double.isNaN(Interval.closed(Double.NEGATIVE_INFINITY, 5.0).midPoint()));
    }

    @Test
    @DisplayName("Interseção: disjuntos => vazio")
    void intersect_disjoint() {
        Interval a = Interval.closed(0.0, 1.0);
        Interval b = Interval.open(2.0, 3.0);
        Interval c = a.intersect(b);
        Assertions.assertTrue(c.isEmpty());
    }

    @Test
    @DisplayName("[0,1] ∩ (1,2) = ∅ (tocam em 1 mas abertas)")
    void intersect_touching_open_closed_rule() {
        Interval a = Interval.closed(0.0, 1.0);
        Interval b = Interval.open(1.0, 2.0);
        Interval r = a.intersect(b);
        Assertions.assertTrue(r.isEmpty());
    }

    @Test
    @DisplayName("[0,1] ∩ [1,2] = [1,1] (ponto)")
    void intersect_touching_point_closed_closed() {
        Interval a = Interval.closed(0.0, 1.0);
        Interval b = Interval.closed(1.0, 2.0);
        Interval r = a.intersect(b);
        Assertions.assertFalse(r.isEmpty());
        Assertions.assertTrue(r.isDegenerate());
        Assertions.assertEquals(1.0, r.getLower());
        Assertions.assertEquals(1.0, r.getUpper());
        Assertions.assertEquals("[1.0, 1.0]", r.toString());
    }

    @Test
    @DisplayName("(0,2) ∩ [1,3] = (1,2)")
    void intersect_partial_overlap() {
        Interval a = Interval.open(0.0, 2.0);
        Interval b = Interval.closed(1.0, 3.0);
        Interval r = a.intersect(b);
        Assertions.assertEquals("[1.0, 2.0)", r.toString());
        Assertions.assertFalse(r.isEmpty());
        Assertions.assertFalse(r.isDegenerate());
        Assertions.assertTrue(r.contains(1.5));
        Assertions.assertTrue(r.contains(1.0)); // fechado à esquerda
        Assertions.assertFalse(r.contains(2.0)); // aberto à direita
    }

    @Test
    @DisplayName("Interseção com infinitos")
    void intersect_with_infinities() {
        Interval a = Interval.open(Double.NEGATIVE_INFINITY, 0.0);
        Interval b = Interval.closed(-1.0, Double.POSITIVE_INFINITY);
        Interval r = a.intersect(b);
        // Deve ser [-1.0, 0.0)
        Assertions.assertEquals("[-1.0, 0.0)", r.toString());
        Assertions.assertTrue(r.contains(-0.5));
        Assertions.assertTrue(r.contains(-1.0));
        Assertions.assertFalse(r.contains(0.0));
    }

    @Test
    @DisplayName("EMPTY ∩ X = EMPTY")
    void empty_intersection_rules() {
        Interval e = Interval.empty();
        Interval x = Interval.closed(0.0, 1.0);
        Assertions.assertTrue(e.intersect(x).isEmpty());
        Assertions.assertTrue(x.intersect(e).isEmpty());
        Assertions.assertTrue(e.intersect(e).isEmpty());
    }

    @Test
    @DisplayName("toString formatos matemáticos e vazio")
    void toString_formats() {
        Assertions.assertEquals("[0.0, 1.0]", Interval.closed(0.0, 1.0).toString());
        Assertions.assertEquals("(0.0, 1.0]", Interval.openClosed(0.0, 1.0).toString());
        Assertions.assertEquals("[0.0, 1.0)", Interval.closedOpen(0.0, 1.0).toString());
        Assertions.assertEquals("(0.0, 1.0)", Interval.open(0.0, 1.0).toString());
        Assertions.assertEquals("∅", Interval.empty().toString());
        Assertions.assertEquals("(-Infinity, 2.0)", Interval.open(Double.NEGATIVE_INFINITY, 2.0).toString());
        Assertions.assertEquals("[0.0, Infinity)", Interval.closedOpen(0.0, Double.POSITIVE_INFINITY).toString());
    }

    @Test
    @DisplayName("equals/hashCode: EMPTY == EMPTY; ponto != EMPTY")
    void equals_and_hashcode() {
        Interval e1 = Interval.empty();
        Interval e2 = Interval.empty();
        Assertions.assertEquals(e1, e2);
        Assertions.assertEquals(e1.hashCode(), e2.hashCode());

        Interval p = Interval.closed(1.0, 1.0);
        Assertions.assertNotEquals(e1, p);

        Interval a = Interval.openClosed(0.0, 1.0);
        Interval b = Interval.openClosed(0.0, 1.0);
        Assertions.assertEquals(a, b);
        Assertions.assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    @DisplayName("Acessores em EMPTY: lower/upper = NaN; tipos OPEN por convenção")
    void accessors_on_empty() {
        Interval e = Interval.empty();
        Assertions.assertTrue(Double.isNaN(e.getLower()));
        Assertions.assertTrue(Double.isNaN(e.getUpper()));
        Assertions.assertEquals(Interval.IntervalType.OPEN, e.getLowerType());
        Assertions.assertEquals(Interval.IntervalType.OPEN, e.getUpperType());
    }

    @Test
    @DisplayName("Distinção -0.0 e +0.0 em equals/hashCode")
    void signed_zero_equality() {
        Interval a = Interval.closed(-0.0, 1.0);
        Interval b = Interval.closed(+0.0, 1.0);
        // doubleToLongBits trata -0.0 e +0.0 como diferentes
        Assertions.assertNotEquals(a, b);
    }
}