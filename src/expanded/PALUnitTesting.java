package expanded;

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
        Report breakdown = new Report();
        testSuites.forEach(s -> {
            TestSuiteRunner runner = new TestSuiteRunner(s);

            s.beforeAllTests();
            runner.run();
            s.afterAllTests();

            breakdown.addBreakdown(s.getClass().getSimpleName(), runner.getReport());
        });
        breakdown.print();
    }

    private void printBreakdown(TestSuiteReport breakdown) {
        System.out.println();
        breakdown.print();
    }
}
