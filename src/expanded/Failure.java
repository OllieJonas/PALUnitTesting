package expanded;

import expanded.tests.Test;

public class Failure {

    private final Test test;

    private final String methodName;

    private final Object result;

    public Failure(Test test, String methodName, Object result) {
        this.test = test;
        this.methodName = methodName;
        this.result = result;
    }

    public Object getResult() {
        return result == null ? "" : result;
    }

    public Test getTest() {
        return test;
    }

    @Override
    public String toString() {
        return Constants.FAILURE_START_STR +
                Constants.BREAKDOWN_DELIM +
                getId() +
                getName() +
                getInputs() +
                getExpected() +
                getActual() +
                Constants.BREAKDOWN_DELIM;
    }

    private String getId() {
        return test.id() == -1 ?
                "" :
                "ID: " + test.id() + Constants.BREAKDOWN_DELIM;
    }

    private String getName() {
        return "Name: " + (test.name().equals("") ? methodName : test.name()) + Constants.BREAKDOWN_DELIM;
    }

    private String getInputs() {
        return test.input().length < 2 ?
                "" :
                "Inputs: " + Util.listToStr(test.input()) + Constants.BREAKDOWN_DELIM;
    }

    private String getExpected() {
        return test.expected().equals("") ?
                "" :
                "Expected: " + test.expected() + Constants.BREAKDOWN_DELIM;
    }

    private String getActual() {
        return getResult().equals("") ?
                "" :
                "Actual: " + result;
    }

}
