package expanded;

public class Constants {

    public static final boolean LOG_INDIVIDUAL_TESTS = true;

    public static final boolean USING_COLOURS = true;

    public static final String TEST_PASSED_COLOUR = "\u001B[32m";  // Green

    public static final String TEST_FAILED_COLOUR = "\u001B[31m";  // Red

    public static final String RESET_COLOUR = "\u001B[0m";  // White

    public static final String TEST_PASSED = colour(TEST_PASSED_COLOUR) + "Test passed!" + colour(RESET_COLOUR);

    public static final String TEST_FAILED = colour(TEST_FAILED_COLOUR) + "Test failed!" + colour(RESET_COLOUR);

    public static final String TEST_RUNNING = "Running test \"%s\"... Inputs: %s, Expected: %s\n";

    public static final String BREAKDOWN_DELIM = " | ";

    public static final int REPORT_REPEATS = 2;

    public static final String REPORT_SECTION_DIVIDER = "\n";

    public static String colour(String colour) {
        return USING_COLOURS ? colour : "";
    }
}
