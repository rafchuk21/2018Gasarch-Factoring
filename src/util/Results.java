package util;

/**
 * Object used to store result, duration, and operation for returning.
 */
public class Results {
    private long duration;
    private long operations;
    private Object result;
    private String operationType;
    private String resultType;

    public Results(long dur, long ops, Object r, String opType, String resType) {
        duration = dur;
        operations = ops;
        result = r;
        operationType = opType;
        resultType = resType;
    }

    public long getDuration() {
        return duration;
    }

    public long getOperations() {
        return operations;
    }

    public Object getResult() {
        return result;
    }

    public String getOperationType() { return operationType; }

    public String getResultType() { return resultType; }

    public String toString() {
        return this.toString(500);
    }

    /**
     * Turns the Result into a string, only displaying the first carLimit characters of the result value
     * @param carLimit number of characters of result to display
     * @return String form of the Result
     */
    public String toString(int carLimit) {
        return String.format("%d nanoseconds, %.3f seconds%n%d %s%n", duration,
                duration/1000000000.0, operations, operationType) + resultType + ": " + result.toString().substring(0,Math.min(result.toString().length(), carLimit)) + "\n";
    }
}
