package expanded;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TestSuiteBreakdown {

    static final String HEADER_CHAR = "=";

    static final String SECTION_DIVIDER = "\n\n";

    static final int REPEATS = 0;

    private final TestSuite suite;

    private final List<Failure> failed;

    private int testsFailed;

    private int noTests;

    public TestSuiteBreakdown(TestSuite suite) {
        this.suite = suite;
        this.failed = new ArrayList<>();
    }

    public void print() {
        System.out.println(getBreakdownAsString());
    }

    public String getBreakdownAsString() {
        return getHeader() +
                SECTION_DIVIDER +
                getMetrics() +
                SECTION_DIVIDER +
                getFailedTests() +
                SECTION_DIVIDER +
                getFooter();
    }

    public void passTest() {
        noTests++;
    }

    public void failTest(Failure failure) {
        failed.add(failure);
        noTests++;
        testsFailed++;
    }

    private String getHeader() {
        return HEADER_CHAR.repeat(REPEATS) +
                "Test Breakdown for " +
                suite.getClass().getSimpleName() +
                " " +
                HEADER_CHAR.repeat(REPEATS);
    }

    private String getMetrics() {
        return getMetricsHeader() +
                "\n\n" +
                getTestsPassed() +
                "\n" +
                getTestsFailed() +
                "\n" +
                getTestsTotal() +
                "\n" +
                getPercentageOfTestsPassed();
    }

    private String getFailedTests() {
        return getFailHeader() + buildFailContent();
    }

    private String getFooter() {
        return HEADER_CHAR.repeat(REPEATS * 4);
    }

    private String getMetricsHeader() {
        return "Metrics:";
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

    private String getFailHeader() {
        return "Failed Tests for " +
                suite.getClass().getSimpleName() +
                ":\n\n" +
                Constants.colour(Constants.TEST_FAILED_COLOUR);
    }

    private String buildFailContent() {
        return failed.stream()
                .sorted(Comparator.comparingInt(f -> f.getTestAnno().id()))
                .map(Failure::toString)
                .collect(Collectors.joining("\n"))
                + Constants.colour(Constants.colour(Constants.RESET_COLOUR));
    }

}
