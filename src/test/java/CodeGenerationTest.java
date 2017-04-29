import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;

@Test(dataProviderClass = Providers.class)
public class CodeGenerationTest {

    @Test(dataProvider = "benchmarkFiles", enabled = true)
    void testAllBenchmarks(File f) throws IOException {
        try { // remove the class file corresponding to this file if it's been already generated
            Files.delete(Paths.get(f.getPath().replace(".emj", ".class")));
            Files.delete(Paths.get(f.getPath().replace(".emj", ".j")));
        } catch(NoSuchFileException e ) {} //ignored

        // run program with the --cgen flag
        System.out.println("Generating class files for : " + f.getName());
        Emjc.main(new String[] {"--cgen", f.getPath()});

        // TODO: check that a class file exists for this file
        // TODO: run class file, compare output with javac class file version
    }

}
