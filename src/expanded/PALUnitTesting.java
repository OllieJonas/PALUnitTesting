package expanded;

import expanded.internal.report.Report;
import expanded.internal.runner.TestSuiteRunner;
import expanded.testmeta.TestSuite;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Main class for testing framework.
 *
 * This class is responsible for orchestrating the running of test suites that you give it.
 *
 * To use, you need to register the test suites you want to run using the {@link #registerTestSuite(TestSuite)}. Once
 * you're done, you can then call the {@link #runTests()} method to run all the tests you've registered.
 */
public class PALUnitTesting {

    /**
     * A list of all test suites that will be run when calling {@link #runTests()}.
     */
    private final List<TestSuite> testSuites;

    /**
     * Default constructor for PALUnitTesting. Initialises with an empty list of test suites.
     */
    public PALUnitTesting() {
        this(new ArrayList<>());
    }

    /**
     * Constructor which creates a List out of varargs, which will be run when calling {@link #runTests()}.
     * You can still register more test suites after this using {@link #registerTestSuite(TestSuite)}.
     *
     * @param suites The test suites to use
     */
    public PALUnitTesting(TestSuite... suites) {
        this(Arrays.asList(suites));
    }

    /**
     * Constructor which uses an already created list of test suites, which will be run when calling {@link #runTests()}.
     * You can still register more test suites after this using {@link #registerTestSuite(TestSuite)}.
     *
     * @param suites The test suites to use
     */
    public PALUnitTesting(List<TestSuite> suites) {
        this.testSuites = suites;
    }

    /**
     * Registers a test suite to be run when calling {@link #runTests()}.
     *
     * @param suite The test suite to register
     */
    public void registerTestSuite(TestSuite suite) {
        testSuites.add(suite);
    }

    /**
     * Runs all tests given to it from the list {@link #testSuites}.
     *
     * This will call the {@link TestSuite#beforeAllTests()} and the {@link TestSuite#afterAllTests()} methods, then
     * run the test suites using the {@link TestSuiteRunner}.
     *
     * This method is also responsible for building the report, which will be printed after all test suites have been
     * tested.
     */
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
}
