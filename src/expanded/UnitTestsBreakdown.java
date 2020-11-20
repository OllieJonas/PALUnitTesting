package expanded;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class UnitTestsBreakdown {

    private final Map<String, TestSuiteBreakdown> breakdowns;

    public UnitTestsBreakdown() {
        this.breakdowns = new HashMap<>();
    }

    public void addBreakdown(String suiteName, TestSuiteBreakdown breakdown) {
        breakdowns.put(suiteName, breakdown);
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
        return breakdowns.values().stream().map(TestSuiteBreakdown::getBreakdownAsString).collect(Collectors.joining("\n"));
    }

    private String getHeader() {
        return "UNIT TEST BREAKDOWN";
    }
}
