package expanded.testmeta;

public interface TestSuite {

    default String name() {
        return getClass().getSimpleName();
    }

    default void beforeAllTests() {}

    default void afterAllTests() {}

    default void beforeEachTest() {}

    default void afterEachTest() {}
}
