package expanded;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestRunner {

    private final TestSuite suite;

    private final TestSuiteBreakdown breakdown;

    private List<Method> targetMethods;

    public TestRunner(TestSuite suite) {
        this.suite = suite;
        this.breakdown = new TestSuiteBreakdown(suite);
    }

    public void run() {
        this.targetMethods = buildTargetMethods();
        runTests();
    }

    private List<Method> buildTargetMethods() {
        return this.targetMethods = Stream.of(suite.getClass().getDeclaredMethods())
                .filter(m -> m.isAnnotationPresent(Test.class))
                .collect(Collectors.toList());
    }

    private void runTests() {
        targetMethods.forEach(this::runTest);
    }

    /** Assumes we've already filtered the list of declared methods for the annotation {@link Test} */
    private void runTest(Method method) {
        Test annotation = method.getAnnotation(Test.class);

        logTestRunning(annotation);

        try {
            invoke(method);
            passTest();
        } catch (InvocationTargetException e) { // thrown if the method invoked throws an exception itself
            failTest(annotation, e.getTargetException());
        } catch (IllegalAccessException ignored) {

        }
    }

    private void logTestRunning(Test annotation) {
        if (Constants.LOG_INDIVIDUAL_TESTS)
            System.out.printf(Constants.TEST_RUNNING, annotation.name(), Util.listToStr(annotation.input()), annotation.expected());
    }

    private void invoke(Method method) throws IllegalAccessException, InvocationTargetException {
        method.setAccessible(true);
        method.invoke(suite);
    }

    private void passTest() {
        if (Constants.LOG_INDIVIDUAL_TESTS)
            System.out.println(Constants.TEST_PASSED);

        breakdown.passTest();
    }

    private void failTest(Test annotation, Throwable target) {
        if (Constants.LOG_INDIVIDUAL_TESTS)
            System.out.println(Constants.TEST_FAILED);

        handleFailedTest(annotation, target);
    }

    private void handleFailedTest(Test annotation, Throwable target) {
        if (target instanceof AssertionFailedException) // if its thrown deliberately
            registerFailedTest(annotation, ((AssertionFailedException) target).getActual());
        else  // it was a mistake :/
            target.printStackTrace();
    }

    private void registerFailedTest(Test annotation, Object result) {
        breakdown.failTest(new Failure(annotation, result));
    }

    public TestSuiteBreakdown getBreakdown() {
        return breakdown;
    }

}