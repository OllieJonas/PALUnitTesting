package expanded.internal;

public class Util {

    private Util() {
    }

    public static String listToStr(String[] str) {
        return "[" + String.join(", ", str) + "]";
    }
}
