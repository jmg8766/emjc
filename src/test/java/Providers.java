import org.testng.annotations.DataProvider;

import java.io.File;
import java.util.Arrays;
import java.util.Iterator;

public class Providers {

	@DataProvider(parallel = false)
    public static Iterator<Object[]> benchmarkFiles() {
        return Arrays
                .stream(new File("src/test/benchmarks").listFiles())
                .filter(f -> f.getName().endsWith(".emj"))
                .map(f -> new Object[] {f})
                .iterator();
    }


}
