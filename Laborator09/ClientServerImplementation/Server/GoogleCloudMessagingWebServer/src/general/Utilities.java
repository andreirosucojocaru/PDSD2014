package general;

public class Utilities {
    public static boolean isSystemFunction(String record) {
        for (String function:Constants.SYSTEM_FUNCTION)
            if (record.contentEquals(function))
                return true;
        return false;
    }
}
