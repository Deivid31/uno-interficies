package random;

public class Logger {

    static private String project = "uno-interficies";

    public static void error(String error) {
        System.out.println("[ERROR] [ERROR - " + project + "] -  " + error);
    }

    public static void warn(String error) {
        System.out.println("[WARNING] [WARNING - " + project + "] -  " + error);
    }

    public static void info(String str) {
        System.out.println("[INFO - " + project + "] -  " + str);
    }

}
