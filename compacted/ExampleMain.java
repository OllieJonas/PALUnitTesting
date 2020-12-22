public class ExampleMain {

    public static void main(String[] args) {
        PALUnitTestingCompacted tester = new PALUnitTestingCompacted();

        tester.registerTestSuite(new ExampleTestSuite());
        tester.runTests();
    }
}
