package expanded;

/**
 * This exception should be thrown whenever a test fails.
 */
public class AssertionFailedException extends RuntimeException {

    private final Object expected;

    private final Object actual;

    public AssertionFailedException(Object expected, Object actual) {
        this.expected = expected;
        this.actual = actual;
    }

    @Override
    public String toString() {
        return "Assertion Failed! Expected: " + expected.toString() + ". Actual: " + actual.toString();
    }

    public Object getActual() {
        return actual;
    }
}