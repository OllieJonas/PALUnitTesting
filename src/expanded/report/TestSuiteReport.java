package expanded.report;

import expanded.Constants;
import expanded.Failure;
import expanded.testmeta.TestSuite;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TestSuiteReport {

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
                + Constants.colour(Constants.colour(Constants.RESET_COLOUR));
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
