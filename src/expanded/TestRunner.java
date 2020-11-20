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

        logTestRunning(annotation, method.getName());

        try {
            invoke(method);
            passTest();
        } catch (InvocationTargetException e) { // thrown if the method invoked throws an exception itself
            failTest(annotation, method.getName(), e.getTargetException());
        } catch (IllegalAccessException ignored) {

        }
    }

    private void logTestRunning(Test annotation, String methodName) {
        if (Constants.LOG_INDIVIDUAL_TESTS)
            System.out.printf(Constants.TEST_RUNNING, getName(annotation, methodName), Util.listToStr(annotation.input()), annotation.expected());
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

    private void failTest(Test annotation, String methodName, Throwable target) {
        if (Constants.LOG_INDIVIDUAL_TESTS)
            System.out.println(Constants.TEST_FAILED);

        handleFailedTest(annotation, methodName, target);
    }

    private void handleFailedTest(Test annotation, String methodName, Throwable target) {
        if (target instanceof AssertionFailedException) // if its thrown deliberately
            registerFailedTest(annotation, methodName, ((AssertionFailedException) target).getActual());
        else  // it was a mistake :/
            target.printStackTrace();
    }

    private void registerFailedTest(Test annotation, String methodName, Object result) {
        breakdown.failTest(new Failure(annotation, methodName, result));
    }

    private String getName(Test annotation, String methodName) {
        return annotation.name().equals("") ? methodName : annotation.name();
    }

    public TestSuiteBreakdown getBreakdown() {
        return breakdown;
    }

}