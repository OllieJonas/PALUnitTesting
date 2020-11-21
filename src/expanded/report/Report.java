package expanded.report;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Report {

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
