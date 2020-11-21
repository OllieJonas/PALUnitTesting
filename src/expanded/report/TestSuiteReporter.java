package expanded.report;

import expanded.*;
import expanded.AssertionFailedException;
import expanded.Constants;
import expanded.runner.UnitTestRunner;
import expanded.testmeta.Test;
import expanded.testmeta.TestSuite;

import java.lang.reflect.InvocationTargetException;

public class TestSuiteReporter {

    private final TestSuiteReport report;

    public TestSuiteReporter() {
        this.report = new TestSuiteReport();
    }

    public void report(Test annotation, String invokedMethodName, UnitTestRunner.TestResult result) {
        if (result.didPass())
            passTest();
        else
            failTest(annotation, invokedMethodName, result.getThrown());
    }

    private void passTest() {
        if (Constants.LOG_INDIVIDUAL_TESTS)
            System.out.println(Constants.TEST_PASSED);

        report.passTest();
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
        report.failTest(new Failure(annotation, methodName, result));
    }

    public TestSuiteReport getReport() {
        return report;
    }
}
