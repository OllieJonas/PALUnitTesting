package expanded;

public class Failure {

    static final String STARTING = "==> ";

    private final Test testAnno;

    private final String methodName;

    private final Object result;

    public Failure(Test testAnno, String methodName, Object result) {
        this.testAnno = testAnno;
        this.methodName = methodName;
        this.result = result;
    }

    public Object getResult() {
        return result == null ? "" : result;
    }

    public Test getTestAnno() {
        return testAnno;
    }

    @Override
    public String toString() {
        return STARTING +
                Constants.BREAKDOWN_DELIM +
                getId() +
                getName() +
                getInputs() +
                getExpected() +
                getActual() +
                Constants.BREAKDOWN_DELIM;
    }

    private String getId() {
        return testAnno.id() == -1 ? "" : "ID: " + testAnno.id() + Constants.BREAKDOWN_DELIM;
    }

    private String getName() {
        return testAnno.name().equals("") ? "Name: " + methodName + Constants.BREAKDOWN_DELIM : "Name: " + testAnno.name() + Constants.BREAKDOWN_DELIM;
    }

    private String getInputs() {
        return testAnno.input().length < 2 ? "" : "Inputs: " + Util.listToStr(testAnno.input()) + Constants.BREAKDOWN_DELIM;
    }

    private String getExpected() {
        return testAnno.expected().equals("") ? "" : "Expected: " + testAnno.expected() + Constants.BREAKDOWN_DELIM;
    }

    private String getActual() {
        return getResult().equals("") ? "" : "Actual: " + result;
    }

}
