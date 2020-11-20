package expanded.runner;

import expanded.Constants;
import expanded.Util;
import expanded.report.TestSuiteReport;
import expanded.report.TestSuiteReporter;
import expanded.tests.Test;
import expanded.tests.TestSuite;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestSuiteRunner {

    private final TestSuite suite;

    private final TestSuiteReporter reporter;

    private List<Method> targetMethods;

    public TestSuiteRunner(TestSuite suite) {
        this.suite = suite;
        this.reporter = new TestSuiteReporter(suite);
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
        reporter.report(annotation, methodName, status);
    }

    private void logTestRunning(Test annotation, String methodName) {
        if (Constants.LOG_INDIVIDUAL_TESTS)
            System.out.printf(Constants.TEST_RUNNING, getName(annotation, methodName), Util.listToStr(annotation.input()), annotation.expected());
    }

    private String getName(Test annotation, String methodName) {
        return annotation.name().equals("") ? methodName : annotation.name();
    }

    public TestSuiteReport getReport() {
        return reporter.getReport();
    }
}