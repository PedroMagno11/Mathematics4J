package br.com.pedromagno.algebra;

import java.util.Objects;

public class LinearFunction {
    public enum LinearFunctionType{
        INCREASING,
        DECREASING,
        CONSTANT
    }

    private final double angularCoefficient;
    private final double linearCoefficient;
    private LinearFunctionType type;

    private LinearFunction(double angularCoefficient, double linearCoefficient) {
        this.angularCoefficient = angularCoefficient;
        this.linearCoefficient = linearCoefficient;
        if(angularCoefficient == 0) {
            this.type = LinearFunctionType.CONSTANT;
        }
        if(angularCoefficient > 0) {
            this.type = LinearFunctionType.INCREASING;
        }
        if(angularCoefficient < 0) {
            this.type = LinearFunctionType.DECREASING;
        }
    }

    public static LinearFunction of(double a, double b) {
        return new LinearFunction(a, b);
    }

    public double apply(double x){
        return angularCoefficient * x + linearCoefficient;
    }

    public double root(){
        if(angularCoefficient == 0){
            return linearCoefficient;
        }
        return -linearCoefficient / angularCoefficient;
    }

    public double getAngularCoefficient() {
        return angularCoefficient;
    }

    public double getLinearCoefficient() {
        return linearCoefficient;
    }


    public LinearFunctionType getType(){
        return type;
    }

    @Override
    public String toString() {
        return String.format("%.2fx + %.2f = 0", angularCoefficient, linearCoefficient);
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(!(obj instanceof LinearFunction)) return false;
        LinearFunction other = (LinearFunction)obj;
        return Double.compare(other.angularCoefficient, this.angularCoefficient) == 0 && Double.compare(other.linearCoefficient, this.linearCoefficient) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(angularCoefficient, linearCoefficient);
    }
}
