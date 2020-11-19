package example;

import expanded.PALUnitTesting;

public class ExampleMain {

    public static void main(String[] args) {
        PALUnitTesting tester = new PALUnitTesting();

        tester.registerTestSuite(new ExampleTestSuite());
        tester.runTests();
    }
}
