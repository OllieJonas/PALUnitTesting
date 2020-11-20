package expanded.report;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Report {

    private final Map<String, TestSuiteReport> reports;

    public Report() {
        this.reports = new HashMap<>();
    }

    public void addBreakdown(String suiteName, TestSuiteReport breakdown) {
        reports.put(suiteName, breakdown);
    }

    public void print() {
        System.out.println();
        System.out.println(getBreakdown());
    }

    public String getBreakdown() {
        return getHeader() + "\n\n" + getContent() + "\n" + getFooter();
    }

    private String getFooter() {
        return "";
    }

    private String getContent() {
        return reports.values().stream()
                .map(TestSuiteReport::getBreakdownAsString)
                .collect(Collectors.joining("\n"));
    }

    private String getHeader() {
        return "UNIT TESTS REPORTS";
    }
}
