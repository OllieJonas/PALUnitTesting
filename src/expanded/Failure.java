package expanded;

public class Failure {

    private final Test test;

    private final Object result;

    public Failure(Test test, Object result) {
        this.test = test;
        this.result = result;
    }

    public Object getResult() {
        return result == null ? "null" : result;
    }

    public Test getTest() {
        return test;
    }
}
