package expanded;

/**
 * All the constants used in the application. Feel free to change these as you please!
 */
public class Constants {

    /**
     * Controls whether individual tests will be printed as they're being run.
     */
    public static final boolean LOG_INDIVIDUAL_TESTS = true;

    /**
     * Whether colours are used in the application.
     */
    public static final boolean USING_COLOURS = true;

    /**
     * The colour if you have enabled logging of individual tests and the test passes. (Green)
     */
    public static final String TEST_PASSED_COLOUR = "\u001B[32m";  // Green

    /**
     * The colour if you have enabled logging of individual tests and the test fails. (Red)
     */
    public static final String TEST_FAILED_COLOUR = "\u001B[31m";  // Red

    /**
     * The colour to use after using any other colour (white)
     */
    public static final String RESET_COLOUR = "\u001B[0m";  // White

    /**
     * The string to be printed if you have enabled logging of individual tests and the test passes.
     */
    public static final String TEST_PASSED = colour(TEST_PASSED_COLOUR) + "Test passed!" + colour(RESET_COLOUR);

    /**
     * The string to be printed if you have enabled logging of individual tests and the test fails.
     */
    public static final String TEST_FAILED = colour(TEST_FAILED_COLOUR) + "Test failed!" + colour(RESET_COLOUR);

    /**
     * The string to be printed if you have enabled logging of individual tests and the test is about to be run.
     */
    public static final String TEST_RUNNING = "Running test \"%s\"... Inputs: %s, Expected: %s\n";

    /**
     * The separating string between information contained in the Failed Tests section of the report.
     */
    public static final String FAILURE_DELIM = " | ";

    /**
     * The start of a failed test in the Failed Tests section of the report.
     */
    public static final String FAILURE_START_STR = " ==> " + FAILURE_DELIM;

    /**
     * The multiplier for indentation in the section report.
     */
    public static final int REPORT_REPEATS = 2;

    /**
     * The section divider between different sections in the report.
     */
    public static final String REPORT_SECTION_DIVIDER = "\n";

    /**
     * Adds colour to a string if {@link #USING_COLOURS} is true.
     *
     * @param colour The string to add colour to
     *
     * @return The coloured string (if {@link #USING_COLOURS} is true).
     */
    public static String colour(String colour) {
        return USING_COLOURS ? colour : "";
    }
}
