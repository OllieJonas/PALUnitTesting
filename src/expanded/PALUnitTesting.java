package expanded;

import expanded.report.Report;
import expanded.report.TestSuiteReport;
import expanded.runner.TestSuiteRunner;
import expanded.testmeta.TestSuite;

import java.util.ArrayList;
import java.util.List;

public class PALUnitTesting {

    private final List<TestSuite> testSuites;

    public PALUnitTesting() {
        this(new ArrayList<>());
    }

    public PALUnitTesting(List<TestSuite> suites) {
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

            report.addTestSuiteReport(s.getClass().getSimpleName(), runner.getReport());
        });
        report.print();
    }

    private void printBreakdown(TestSuiteReport breakdown) {
        System.out.println();
        breakdown.print();
    }
}
