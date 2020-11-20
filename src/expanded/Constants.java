package expanded;

public class Constants {

    static final boolean LOG_INDIVIDUAL_TESTS = true;

    static final boolean USING_COLOURS = true;

    static final String TEST_PASSED_COLOUR = "\u001B[32m";  // Green

    static final String TEST_FAILED_COLOUR = "\u001B[31m";  // Red

    static final String RESET_COLOUR = "\u001B[0m";  // White

    static final String TEST_PASSED = colour(TEST_PASSED_COLOUR) + "Test passed!" + colour(RESET_COLOUR);

    static final String TEST_FAILED = colour(TEST_FAILED_COLOUR) + "Test failed!" + colour(RESET_COLOUR);

    static final String TEST_RUNNING = "Running test \"%s\"... Inputs: %s, Expected: %s\n";

    static final String BREAKDOWN_DELIM = " | ";

    static final int REPORT_REPEATS = 2;

    static final String REPORT_SECTION_DIVIDER = "\n";

    public static String colour(String colour) {
        return USING_COLOURS ? colour : "";
    }
}
