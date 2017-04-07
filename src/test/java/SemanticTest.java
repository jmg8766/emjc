import org.testng.annotations.Test;

import java.io.File;

@Test(dataProviderClass = Providers.class)
public class SemanticTest {

    @Test(dataProvider = "benchmarkFiles", enabled = false)
    void testGenAstFile(File f) {
        System.out.println("Performing Name analysis: " + f.getName());
        Emjc.main(new String[] {"--name", f.getPath()});
    }

    @Test(enabled = true)
    void testAstSingleFile() {
        File f = new File("src/test/benchmarks/Factorial.emj");
        System.out.println("Performing Name analysis: " + f.getName());
        Emjc.main(new String[] {"--name", f.getPath()});
    }
}