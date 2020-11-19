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
        testSuites.forEach(s -> {
            TestRunner runner = new TestRunner(s);

            s.initSetup();
            runner.run();
            s.initTeardown();

            printBreakdown(runner.getBreakdown());
        });
    }

    private void printBreakdown(TestSuiteBreakdown breakdown) {
        System.out.println();
        breakdown.print();
    }
}
