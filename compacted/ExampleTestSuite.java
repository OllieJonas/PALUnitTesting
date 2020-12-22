public class ExampleTestSuite implements PALUnitTestingCompacted.TestSuite {

    private ExampleMaths maths;

    @Override
    public void beforeAllTests() {
        this.maths = new ExampleMaths();
    }

    @PALUnitTestingCompacted.Test
    public void testAdd_NoAnnoParams() {
        int result = maths.add(3, 6);
        PALUnitTestingCompacted.Assertions.assertEqual(result, 8);
    }

    @PALUnitTestingCompacted.Test(
            id = 1,
            name = "Addition Test (Correct)",
            input = {"3", "5"},
            expected = "8"
    )
    public void testAdd_Correctly() {
        int result = maths.add(3, 5);
        PALUnitTestingCompacted.Assertions.assertEqual(result, 8);
    }

    @PALUnitTestingCompacted.Test(
            id = 2,
            name = "Addition Test (Incorrect)",
            input = {"3", "5"},
            expected = "8"
    )
    public void testAdd_Incorrectly() {
        int result = maths.add(3, 5) + 5;
        PALUnitTestingCompacted.Assertions.assertEqual(result, 8);
    }

    @PALUnitTestingCompacted.Test(
            id = 3,
            name = "8 Greater Than 5 (Assert True)",
            input = {"8", "5"},
            expected = "true"
    )
    public void testGreaterThan_AssertsTrue() {
        PALUnitTestingCompacted.Assertions.assertTrue(maths.isBiggerThan(8, 5));
    }

    @PALUnitTestingCompacted.Test(
            id = 4,
            name = "5 Greater Than 8 (Assert False)",
            input = {"5", "8"},
            expected = "false"
    )
    public void testGreaterThan_AssertsFalse() {
        PALUnitTestingCompacted.Assertions.assertTrue(maths.isBiggerThan(5, 8));
    }

    @PALUnitTestingCompacted.Test(
            id = 5,
            name = "Division Test (Throws Exception)",
            input = {"8", "0"},
            expected = "8"
    )
    public void testDivide_ThrowsExceptionCorrectly() {
        PALUnitTestingCompacted.Assertions.assertThrows(() -> maths.divide(8, 0), ArithmeticException.class);
    }


    @PALUnitTestingCompacted.Test(
            id = 5,
            name = "Division Test (Doesn't Throws Exception)",
            input = {"8", "1"},
            expected = "8"
    )
    public void testDivide_DoesntThrowException() {
        PALUnitTestingCompacted.Assertions.assertDoesntThrowException(() -> maths.divide(8, 1));
    }

    @Override
    public void afterAllTests() {
        this.maths = null;
    }
}
