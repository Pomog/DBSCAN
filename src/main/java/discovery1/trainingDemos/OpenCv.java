package discovery1.trainingDemos;

import org.opencv.core.Core;




public class OpenCv {
    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        System.out.println(Core.VERSION);

        String version = System.getProperty("elki.version");
        System.out.println("ELKI version: " + version);
    }
}

