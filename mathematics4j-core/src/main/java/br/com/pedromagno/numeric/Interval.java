package br.com.pedromagno.numeric;

import java.util.Objects;

public final class Interval {

    public enum IntervalType{
        OPEN, CLOSED;
    }

    private final double lower;
    private final double upper;
    private final IntervalType lowerType;
    private final IntervalType upperType;
    private final boolean empty;

    private static final Interval EMPTY = new Interval(
            Double.NaN, Double.NaN,
            IntervalType.OPEN, IntervalType.OPEN,
            true
    );

    private Interval(double lower, double upper,
                     IntervalType lowerType, IntervalType upperType,
                     boolean empty) {
        this.lower = lower;
        this.upper = upper;
        this.lowerType = lowerType;
        this.upperType = upperType;
        this.empty = empty;
    }

    public static Interval empty(){
        return EMPTY;
    }

    private static Interval create(double lower, double upper,
                                   IntervalType lowerType, IntervalType upperType){
        if(Double.isNaN(lower) || Double.isNaN(upper)){
            throw new IllegalArgumentException("NaN endpoints not allowed");
        }
        if(lower > upper){
            throw new IllegalArgumentException("upper endpoint must be greater than lower endpoint");
        }

        if(lower == upper){
            if(lowerType == IntervalType.CLOSED && upperType == IntervalType.CLOSED){
                return new Interval(lower, upper, lowerType, upperType, false);
            }
            return EMPTY;
        }

        return new Interval(lower, upper, lowerType, upperType, false);
    }

    public static Interval closed(double lower, double upper) {
        return create(lower, upper, IntervalType.CLOSED, IntervalType.CLOSED);
    }

    public static Interval open(double lower, double upper){
        return create(lower, upper, IntervalType.OPEN, IntervalType.OPEN);
    }

    public static Interval openClosed(double lower, double upper) {
        return create(lower, upper, IntervalType.OPEN, IntervalType.CLOSED);
    }

    public static Interval closedOpen(double lower, double upper){
        return create(lower, upper, IntervalType.CLOSED, IntervalType.OPEN);
    }

    public double getLower() {
        return empty ? Double.NaN : lower;
    }

    public double getUpper() {
        return empty ? Double.NaN : upper;
    }

    public IntervalType getLowerType() {
        return lowerType;
    }

    public IntervalType getUpperType() {
        return upperType;
    }

    public boolean isEmpty() {
        return empty;
    }

    public boolean isDegenerate(){
        return !empty && lower == upper && lowerType == IntervalType.CLOSED && upperType == IntervalType.CLOSED;
    }

    public boolean isClosedLeft(){
        return lowerType == IntervalType.CLOSED;
    }

    public boolean isClosedRight(){
        return upperType == IntervalType.CLOSED;
    }

    public boolean isOpenLeft(){
        return lowerType == IntervalType.OPEN;
    }

    public boolean isOpenRight(){
        return upperType == IntervalType.OPEN;
    }

    public boolean contains(double number){
        if(empty || Double.isNaN(number)){
            return false;
        }

        boolean leftOk = (lowerType == IntervalType.CLOSED) ? number >= lower : number > lower;

        boolean rightOk = (upperType == IntervalType.CLOSED) ? number <= upper : number < upper;

        return leftOk && rightOk;
    }

    public double length(){
        if(empty) return 0.0;

        if(Double.isInfinite(lower) || Double.isInfinite(upper)){
            if(lower == upper) return 0.0;
            return Double.POSITIVE_INFINITY;
        }
        return Math.max(0.0, upper - lower);
    }

    public double midPoint(){
        if(empty) return Double.NaN;
        if(Double.isInfinite(lower) || Double.isInfinite(upper)){
            return Double.NaN;
        }
        return lower + (upper - lower) / 2.0;
    }

    public Interval intersect(Interval other) {
        if (this.isEmpty() || other.isEmpty()) {
            return EMPTY;
        }

        double newLower;
        IntervalType newLowerType;
        if (this.lower > other.lower) {
            newLower = this.lower;
            newLowerType = this.lowerType;
        } else if (this.lower < other.lower) {
            newLower = other.lower;
            newLowerType = other.lowerType;
        } else {
            newLower = this.lower;
            newLowerType = (this.lowerType == IntervalType.CLOSED && other.lowerType == IntervalType.CLOSED) ? IntervalType.CLOSED : IntervalType.OPEN;
        }

        double newUpper;
        IntervalType newUpperType;

        if (this.upper < other.upper) {
            newUpper = this.upper;
            newUpperType = this.upperType;
        } else if (this.upper > other.upper) {
            newUpper = other.upper;
            newUpperType = other.upperType;
        } else {
            newUpper = this.upper;
            newUpperType = (this.upperType == IntervalType.CLOSED && other.upperType == IntervalType.CLOSED) ? IntervalType.CLOSED : IntervalType.OPEN;
        }

        if(newLower > newUpper) return EMPTY;

        if(newLower == newUpper){
            if(newLowerType == IntervalType.CLOSED && newUpperType == IntervalType.CLOSED){
                return new Interval(newLower, newUpper, IntervalType.CLOSED, IntervalType.CLOSED, false);
            }
            return EMPTY;
        }
        return new Interval(newLower, newUpper, newLowerType, newUpperType, false);
    }

    @Override
    public String toString() {
        if(empty){
            return "∅";
        }

        String left =  (lowerType == IntervalType.CLOSED) ? "[": "(";
        String right = (upperType == IntervalType.CLOSED) ? "]": ")";

        return left + lower + ", " + upper + right;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Interval)) return false;
        if (o == null || getClass() != o.getClass()) return false;
        Interval other = (Interval) o;
        if (this.empty && other.empty) return true;

        return Double.doubleToLongBits(lower) == Double.doubleToLongBits(other.lower) &&
                Double.doubleToLongBits(upper) == Double.doubleToLongBits(other.upper) &&
                lowerType == other.lowerType &&
                upperType == other.upperType
                && empty == other.empty;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                Double.doubleToLongBits(lower),
                Double.doubleToLongBits(upper),
                lowerType, upperType, empty);
    }
}