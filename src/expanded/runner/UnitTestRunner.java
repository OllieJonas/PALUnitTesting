package expanded.runner;

import expanded.testmeta.Test;
import expanded.testmeta.TestSuite;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class UnitTestRunner {

    private final TestSuite suite;

    private final Method method;

    public UnitTestRunner(TestSuite suite, Method method) {
        this.suite = suite;
        this.method = method;
    }
    /** Assumes we've already filtered the list of declared methods for the annotation {@link Test} */
    public TestResult run() {
        try {
            invoke(method);
            return TestResult.passed();
        } catch (Exception e) {
            return TestResult.failed(e);
        }
    }

    private void invoke(Method method) throws IllegalAccessException, InvocationTargetException {
        method.setAccessible(true);
        method.invoke(suite);
    }

    public static class TestResult {

        private final Exception thrown;

        private final Status status;

        public static TestResult passed() {
            return new TestResult(Status.PASSED, null);
        }

        public static TestResult failed(Exception thrown) {
            return new TestResult(Status.FAILED, thrown);
        }

        /**
         * Private constructor here to enforce the static factory constructors (see
         * {@link TestResult#passed()} and {@link TestResult#failed(Exception)}) user can't make any weird combinations
         * (eg. Test passed but also threw an exception)
         */
        private TestResult(Status status, Exception thrown) {
            this.status = status;
            this.thrown = thrown;
        }

        public Exception getThrown() {
            return thrown;
        }

        public boolean didPass() {
            return status == Status.PASSED;
        }

        private enum Status {
            PASSED,
            FAILED;
        }
    }
}
