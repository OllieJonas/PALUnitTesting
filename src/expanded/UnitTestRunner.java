package expanded;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class UnitTestRunner {

    public static class TestStatus {

        private final Exception thrown;

        private final PassFail passFail;

        public static TestStatus passed() {
            return new TestStatus();
        }

        public static TestStatus failed(Exception thrown) {
            return new TestStatus(PassFail.FAILED, thrown);
        }

        private TestStatus() {
            this(PassFail.PASSED, null);
        }
        private TestStatus(PassFail passFail, Exception thrown) {
            this.passFail = passFail;
            this.thrown = thrown;
        }

        public Exception getThrown() {
            return thrown;
        }

        public boolean didPass() {
            return passFail == PassFail.PASSED;
        }

        enum PassFail {
            PASSED,
            FAILED;
        }
    }

    private final TestSuite suite;

    private final Method method;

    public UnitTestRunner(TestSuite suite, Method method) {
        this.suite = suite;
        this.method = method;
    }
    /** Assumes we've already filtered the list of declared methods for the annotation {@link Test} */
    public TestStatus run() {
        try {
            invoke(method);
            return TestStatus.passed();
        } catch (Exception e) {
            return TestStatus.failed(e);
        }
    }

    private void invoke(Method method) throws IllegalAccessException, InvocationTargetException {
        method.setAccessible(true);
        method.invoke(suite);
    }
}
