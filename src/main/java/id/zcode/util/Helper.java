package id.zcode.util;

public class Helper {
    public static <E extends Enum<E>> E lookupEnum(Class<E> e, String id) {
        try {
            E result = Enum.valueOf(e, id);
            return result;
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    public static int add(int a, int b) {
        return a + b;
    }
}
