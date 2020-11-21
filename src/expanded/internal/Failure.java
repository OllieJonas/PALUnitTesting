package expanded.internal;

import expanded.Constants;
import expanded.testmeta.Test;

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
                getId() +
                getName() +
                getInputs() +
                getExpected() +
                getActual();
    }

    private String getId() {
        return test.id() == -1 ?
                "" :
                "ID: " + test.id() + Constants.FAILURE_DELIM;
    }

    private String getName() {
        return "Name: " + (test.name().equals("") ? methodName : test.name()) + Constants.FAILURE_DELIM;
    }

    private String getInputs() {
        return test.input().length < 2 ?
                "" :
                "Inputs: " + Util.listToStr(test.input()) + Constants.FAILURE_DELIM;
    }

    private String getExpected() {
        return test.expected().equals("") ?
                "" :
                "Expected: " + test.expected() + Constants.FAILURE_DELIM;
    }

    private String getActual() {
        return getResult().equals("") ?
                "" :
                "Actual: " + result + Constants.FAILURE_DELIM;
    }

}
