package br.com.pedromagno.numeric;

public class Epsilon {
    public static final double DEFAULT_EPSILON = 1e-12;

    public static boolean nearlyEqual(double firstValue, double secondValue, double tolerance){
        if(firstValue == secondValue){
            return true;
        }

        if(Double.isNaN(firstValue) || Double.isNaN(secondValue)){
            return false;
        }

        if(Double.isInfinite(firstValue) || Double.isInfinite(secondValue)){
            return false;
        }

        final double diff = Math.abs(firstValue - secondValue);
        final double maxAbs = Math.max(Math.abs(firstValue), Math.abs(secondValue));

        final double scaledTolerance = Math.max(tolerance, tolerance * maxAbs);
        return diff <= scaledTolerance;
    }

    public static boolean nearlyEqual(double firstValue, double secondValue){
        return nearlyEqual(firstValue, secondValue, DEFAULT_EPSILON);
    }

    public static boolean isZero(double value, double tolerance){
        return Math.abs(value) <= tolerance;
    }

    public static boolean isZero(double value){
        return nearlyEqual(value, DEFAULT_EPSILON);
    }

    public static long unitInTheLastPlaceDiff(double firstValue, double secondValue){
        if(Double.isNaN(firstValue) || Double.isNaN(secondValue)){
            return Long.MAX_VALUE;
        }

        if(firstValue == secondValue){
            return 0L;
        }

        if(Double.isInfinite(firstValue) || Double.isInfinite(secondValue)){
            return Long.MAX_VALUE;
        }

        long a = Double.doubleToLongBits(firstValue);
        long b = Double.doubleToLongBits(secondValue);

        a = (a >= 0L) ? a : 0x8000_0000_0000_0000L - a;
        b = (b >= 0L) ? b : 0x8000_0000_0000_0000L - b;

        long diff = a - b;
        return (diff >= 0) ? diff : -diff;
    }
}
