package expanded;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestSuiteRunner {

    private final TestSuite suite;

    private final TestSuiteBreakdown breakdown;

    private List<Method> targetMethods;

    public TestSuiteRunner(TestSuite suite) {
        this.suite = suite;
        this.breakdown = new TestSuiteBreakdown(suite);
    }

    public void run() {
        this.targetMethods = getTargetMethods();
        runTests();
    }

    private List<Method> getTargetMethods() {
        return Stream.of(suite.getClass().getDeclaredMethods())
                .filter(m -> m.isAnnotationPresent(Test.class))
                .collect(Collectors.toList());
    }

    private void runTests() {
        targetMethods.forEach(method -> {
            suite.beforeEachTest();

            Test annotation = method.getAnnotation(Test.class);
            String methodName = method.getName();
            UnitTestRunner runner = new UnitTestRunner(suite, method);

            runTest(annotation, methodName, runner);

            suite.afterEachTest();
        });
    }

    private void runTest(Test annotation, String methodName, UnitTestRunner runner) {
        logTestRunning(annotation, methodName);
        UnitTestRunner.TestStatus status = runner.run();
        reportTestToBreakdown(annotation, methodName, status);
    }

    private void logTestRunning(Test annotation, String methodName) {
        if (Constants.LOG_INDIVIDUAL_TESTS)
            System.out.printf(Constants.TEST_RUNNING, getName(annotation, methodName), Util.listToStr(annotation.input()), annotation.expected());
    }

    private String getName(Test annotation, String methodName) {
        return annotation.name().equals("") ? methodName : annotation.name();
    }

    private void reportTestToBreakdown(Test annotation, String methodName, UnitTestRunner.TestStatus status) {
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