package expanded;

import java.lang.reflect.InvocationTargetException;

public class TestSuiteReporter {

    private final TestSuiteBreakdown breakdown;

    public TestSuiteReporter(TestSuite suite) {
        this.breakdown = new TestSuiteBreakdown(suite);
    }

    public void report(Test annotation, String methodName, UnitTestRunner.TestStatus status) {
        if (status.didPass())
            passTest();
        else
            failTest(annotation, methodName, status.getThrown());
    }

    private void passTest() {
        if (Constants.LOG_INDIVIDUAL_TESTS)
            System.out.println(Constants.TEST_PASSED);

        breakdown.passTest();
    }

    private void failTest(Test annotation, String methodName, Exception target) {
        if (Constants.LOG_INDIVIDUAL_TESTS)
            System.out.println(Constants.TEST_FAILED);

        handleFailedTest(annotation, methodName, target);
    }

    private void handleFailedTest(Test annotation, String methodName, Exception target) {
        if (target instanceof InvocationTargetException)
            handleInvocationException(annotation, methodName, ((InvocationTargetException) target).getTargetException());
        else  // ie. It's an IllegalAccessException
            target.printStackTrace();
    }

    private void handleInvocationException(Test annotation, String methodName, Throwable target) {
        if (target instanceof AssertionFailedException) // if its thrown deliberately
            registerFailedTest(annotation, methodName, ((AssertionFailedException) target).getActual());
        else  // it was a mistake :/
            target.printStackTrace();
    }

    private void registerFailedTest(Test annotation, String methodName, Object result) {
        breakdown.failTest(new Failure(annotation, methodName, result));
    }

    public TestSuiteBreakdown getBreakdown() {
        return breakdown;
    }
}
