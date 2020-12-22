import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PALUnitTestingCompacted {

    /**
     * All the constants used in the application. Feel free to change these as you please!
     */
    private static class Constants {
        /**
         * Controls whether individual tests will be printed as they're being run.
         */
        public static final boolean LOG_INDIVIDUAL_TESTS = false;

        /**
         * Whether colours are used in the application.
         */
        public static final boolean USING_COLOURS = true;

        /**
         * The colour if you have enabled logging of individual tests and the test passes. (Green)
         */
        public static final String TEST_PASSED_COLOUR = "\u001B[32m";  // Green

        /**
         * The colour if you have enabled logging of individual tests and the test fails. (Red)
         */
        public static final String TEST_FAILED_COLOUR = "\u001B[31m";  // Red (test)

        /**
         * The colour to use after using any other colour (white)
         */
        public static final String RESET_COLOUR = "\u001B[0m";  // White

        /**
         * The string to be printed if you have enabled logging of individual tests and the test passes.
         */
        public static final String TEST_PASSED = colour(TEST_PASSED_COLOUR) + "Test passed!" + colour(RESET_COLOUR);

        /**
         * The string to be printed if you have enabled logging of individual tests and the test fails.
         */
        public static final String TEST_FAILED = colour(TEST_FAILED_COLOUR) + "Test failed!" + colour(RESET_COLOUR);

        /**
         * The string to be printed if you have enabled logging of individual tests and the test is about to be run.
         */
        public static final String TEST_RUNNING = "Running test \"%s\"... Inputs: %s, Expected: %s\n";

        /**
         * The separating string between information contained in the Failed Tests section of the report.
         */
        public static final String FAILURE_DELIM = " | ";

        /**
         * The start of a failed test in the Failed Tests section of the report.
         */
        public static final String FAILURE_START_STR = " ==> " + FAILURE_DELIM;

        /**
         * The multiplier for indentation in the section report.
         */
        public static final int REPORT_REPEATS = 2;

        /**
         * The section divider between different sections in the report.
         */
        public static final String REPORT_SECTION_DIVIDER = "\n";

        /**
         * Adds colour to a string if {@link #USING_COLOURS} is true.
         *
         * @param colour The string to add colour to
         * @return The coloured string (if {@link #USING_COLOURS} is true).
         */
        public static String colour(String colour) {
            return USING_COLOURS ? colour : "";
        }
    }

    public static class Assertions {

        /**
         * Don't want people using the constructor since it's a static utility class.
         */
        private Assertions() {
        }

        public static void assertEqual(Object actual, Object expected) {
            if (!actual.equals(expected))
                throw new AssertionFailedException(expected, actual);
        }

        public static void assertFalse(boolean bool) {
            if (bool)
                throw new AssertionFailedException(true, false);
        }

        public static void assertTrue(boolean bool) {
            if (!bool)
                throw new AssertionFailedException(false, true);
        }

        public static void assertNotNull(Object obj) {
            if (obj == null)
                throw new AssertionFailedException(null, null);
        }

        public static void assertNull(Object obj) {
            if (obj != null)
                throw new AssertionFailedException(null, obj);
        }

        public static void assertThrowsException(Runnable runnable) {
            assertThrows(runnable, Exception.class);
        }

        public static void assertDoesntThrowException(Runnable runnable) {
            assertDoesntThrow(runnable, Exception.class);
        }

        public static void assertDoesntThrow(Runnable runnable, Class<? extends Throwable> throwable) {
            try {
                runnable.run();
            } catch (Throwable t) {
                if (t.getClass().isAssignableFrom(throwable))
                    throw new AssertionFailedException(throwable, "No Exception");
            }
        }

        public static void assertThrows(Runnable runnable, Class<? extends Throwable> throwable) {
            try {
                runnable.run();
                throw new AssertionFailedException(throwable, null);
            } catch (Throwable t) {
                if (!throwable.isAssignableFrom(t.getClass()))
                    throw new AssertionFailedException(throwable, t.getClass());
            }
        }
    }

    // -------------------------------- INTERNAL -------------------------------- \\

    private final List<TestSuite> testSuites;

    public PALUnitTestingCompacted() {
        this(new ArrayList<>());
    }

    public PALUnitTestingCompacted(TestSuite... suites) {
        this(Arrays.asList(suites));
    }

    public PALUnitTestingCompacted(List<TestSuite> suites) {
        this.testSuites = suites;
    }

    public void registerTestSuite(TestSuite suite) {
        testSuites.add(suite);
    }

    public void runTests() {
        Report report = new Report();
        testSuites.forEach(s -> {
            TestSuiteRunner runner = new TestSuiteRunner(s);

            s.beforeAllTests();
            runner.run();
            s.afterAllTests();

            report.addTestSuiteReport(s.name(), runner.getReport());
        });
        report.print();
    }

    // ---------------- EXCEPTIONS ---------------- \\

    private static class AssertionFailedException extends RuntimeException {

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

    // ---------------- PUBLIC FACING ---------------- \\

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface Test {
        int id() default -1;

        String name() default "";

        String[] input() default "";

        String expected() default "";
    }

    public interface TestSuite {

        default String name() {
            return getClass().getSimpleName();
        }

        default void beforeAllTests() {
        }

        default void afterAllTests() {
        }

        default void beforeEachTest() {
        }

        default void afterEachTest() {
        }
    }

    // ---------------- RUNNERS ---------------- \\

    private static class TestSuiteRunner {

        private final TestSuite suite;

        private final TestSuiteReporter reporter;

        private List<Method> targetMethods;

        public TestSuiteRunner(TestSuite suite) {
            this.suite = suite;
            this.reporter = new TestSuiteReporter();
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

        private void runTest(Test annotation, String methodName, PALUnitTestingCompacted.UnitTestRunner runner) {
            logTestRunning(annotation, methodName);
            PALUnitTestingCompacted.UnitTestRunner.TestResult status = runner.run();
            reporter.report(annotation, methodName, status);
        }

        private void logTestRunning(Test annotation, String methodName) {
            if (Constants.LOG_INDIVIDUAL_TESTS)
                System.out.printf(Constants.TEST_RUNNING, getName(annotation, methodName), listToStr(annotation.input()), annotation.expected());
        }

        private String getName(Test annotation, String methodName) {
            return annotation.name().equals("") ? methodName : annotation.name();
        }

        public TestSuiteReport getReport() {
            return reporter.getReport();
        }
    }

    private static class UnitTestRunner {

        private final TestSuite suite;

        private final Method method;

        public UnitTestRunner(TestSuite suite, Method method) {
            this.suite = suite;
            this.method = method;
        }

        /**
         * Assumes we've already filtered the list of declared methods for the annotation {@link Test}
         */
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

            private final TestResult.Status status;

            public static TestResult passed() {
                return new PALUnitTestingCompacted.UnitTestRunner.TestResult(TestResult.Status.PASSED,null);
            }

            public static PALUnitTestingCompacted.UnitTestRunner.TestResult failed(Exception thrown) {
                return new PALUnitTestingCompacted.UnitTestRunner.TestResult(TestResult.Status.FAILED, thrown);
            }

            /**
             * Private constructor here to enforce the static factory constructors (see
             * {@link TestResult#passed()} and {@link TestResult#failed(Exception)}) user can't make any weird combinations
             * (eg. Test passed but also threw an exception)
             */
            private TestResult(TestResult.Status status, Exception thrown) {
                this.status = status;
                this.thrown = thrown;
            }

            public Exception getThrown() {
                return thrown;
            }

            public boolean didPass() {
                return status == TestResult.Status.PASSED;
            }

            private enum Status {
                PASSED,
                FAILED;
            }
        }
    }

    public static class Failure {

        private final Test test;

        private final String methodName;

        private final Object result;

        public Failure(Test test, String methodName, Object result) {
            this.test = test;
            this.methodName = methodName;
            this.result = result;
        }

        public Object getResult() {
            return result == null ? "" : result;
        }

        public Test getTest() {
            return test;
        }

        @Override
        public String toString() {
            return Constants.FAILURE_START_STR +
                    getId() +
                    getName() +
                    getInputs() +
                    getExpected() +
                    getActual();
        }

        private String getId() {
            return test.id() == -1 ?
                    "" :
                    "ID: " + test.id() + Constants.FAILURE_DELIM;
        }

        private String getName() {
            return "Name: " + (test.name().equals("") ? methodName : test.name()) + Constants.FAILURE_DELIM;
        }

        private String getInputs() {
            return test.input().length < 2 ?
                    "" :
                    "Inputs: " + listToStr(test.input()) + Constants.FAILURE_DELIM;
        }

        private String getExpected() {
            return test.expected().equals("") ?
                    "" :
                    "Expected: " + test.expected() + Constants.FAILURE_DELIM;
        }

        private String getActual() {
            return getResult().equals("") ?
                    "" :
                    "Actual: " + result + Constants.FAILURE_DELIM;
        }
    }

    // ---------------- REPORTS ---------------- \\

    private static class Report {

        private final Map<String, TestSuiteReport> reports;

        public Report() {
            this.reports = new HashMap<>();
        }

        public void addTestSuiteReport(String suiteName, TestSuiteReport breakdown) {
            reports.put(suiteName, breakdown);
        }

        public void print() {
            System.out.println();
            System.out.println(getReport());
        }

        public String getReport() {
            return getHeader() + "\n\n" + getContent() + "\n" + getFooter();
        }

        private String getFooter() {
            return "";
        }

        private String getContent() {
            return reports.entrySet().stream()
                    .map((entry) -> entry.getKey() + "\n" + entry.getValue().getReportAsString())
                    .collect(Collectors.joining("\n"));
        }

        private String getHeader() {
            return "UNIT TESTS REPORTS";
        }
    }

    private static class TestSuiteReport {

        private final List<Failure> failed;

        private int testsFailed;

        private int noTests;

        public TestSuiteReport() {
            this.failed = new ArrayList<>();
        }

        public void print() {
            System.out.println(getReportAsString());
        }

        public String getReportAsString() {
            return getMetrics() +
                    Constants.REPORT_SECTION_DIVIDER +
                    getFailedTests();
        }

        public void passTest() {
            noTests++;
        }

        public void failTest(Failure failure) {
            failed.add(failure);
            noTests++;
            testsFailed++;
        }

        private String getMetrics() {
            return getMetricsHeader() + getMetricsContent();
        }

        private String getMetricsHeader() {
            return indent(1) + "Metrics:\n";
        }

        private String getMetricsContent() {
            return indent(2) + getTestsPassed() +
                    "\n" +
                    indent(2) + getTestsFailed() +
                    "\n" +
                    indent(2) + getTestsTotal() +
                    "\n" +
                    indent(2) + getPercentageOfTestsPassed();
        }

        private String getFailedTests() {
            return getFailHeader() + buildFailContent();
        }

        private String getFailHeader() {
            return indent(1) + "Failed Tests" +
                    ":\n" +
                    Constants.colour(Constants.TEST_FAILED_COLOUR);
        }

        private String buildFailContent() {
            return failed.stream()
                    .sorted(Comparator.comparingInt(f -> f.getTest().id()))
                    .map(f -> indent(2) + f.toString())
                    .collect(Collectors.joining("\n"))
                    + Constants.colour(Constants.RESET_COLOUR);
        }


        private String getTestsPassed() {
            return "Tests Passed: " + (noTests - testsFailed);
        }

        private String getTestsFailed() {
            return "Tests Failed: " + testsFailed;
        }

        private String getTestsTotal() {
            return "Total Tests: " + noTests;
        }

        private String getPercentageOfTestsPassed() {
            return "Percentage Passed: " + Math.round(((float) (noTests - testsFailed) / (float) noTests) * 100) + " %";
        }

        private String indent(int times) {
            return " ".repeat(times * Constants.REPORT_REPEATS);
        }
    }

    private static class TestSuiteReporter {

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

    public static String listToStr(String[] str) {
        return "[" + String.join(", ", str) + "]";
    }
}
