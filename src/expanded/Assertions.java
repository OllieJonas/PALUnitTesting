package expanded;

public class Assertions {
    public static void assertEqual(Object actual, Object expected) {
        if (!actual.equals(expected))
            throw new AssertionFailedException(expected, actual);
    }

    public static void assertFalse(boolean bool) {
        if (bool)
            throw new AssertionFailedException(true, false);
    }

    public static void assertTrue(boolean bool) {
        if (!bool)
            throw new AssertionFailedException(false, true);
    }

    public static void assertNotNull(Object obj) {
        if (obj == null)
            throw new AssertionFailedException(null, null);
    }

    public static void assertNull(Object obj) {
        if (obj != null)
            throw new AssertionFailedException(null, obj);
    }

    public static void assertThrowsException(Runnable runnable) {
        assertThrows(runnable, Exception.class);
    }

    public static void assertDoesntThrowException(Runnable runnable) {
        assertDoesntThrow(runnable, Exception.class);
    }

    public static <T extends Throwable> void assertDoesntThrow(Runnable runnable, Class<T> throwable) {
        try {
            runnable.run();
        } catch (Throwable t) {
            if (t.getClass().isAssignableFrom(throwable))
                throw new AssertionFailedException(throwable, "No Exception");
        }
    }
    public static <T extends Throwable> void assertThrows(Runnable runnable, Class<T> throwable) {
        try {
            runnable.run();
            throw new AssertionFailedException(throwable, null);
        } catch (Throwable t) {
            if (!throwable.isAssignableFrom(t.getClass()))
                throw new AssertionFailedException(throwable, t.getClass());
        }
    }
}
