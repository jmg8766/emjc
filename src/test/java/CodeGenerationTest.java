import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;

@Test(dataProviderClass = Providers.class)
public class CodeGenerationTest {

    @Test(dataProvider = "benchmarkFiles", enabled = false)
    void testAllBenchmarks(File f) throws IOException {
        try { // remove the class file corresponding to this file if it's been already generated
            Files.delete(Paths.get(f.getPath().replace(".emj", ".class")));
            Files.delete(Paths.get(f.getPath().replace(".emj", ".j")));
        } catch (NoSuchFileException e) {
        } //ignored

        // run program with the --cgen flag
        System.out.println("Generating class files for : " + f.getName());
        Emjc.main(new String[]{"--cgen", f.getPath()});

        // TODO: check that a class file exists for this file
        // TODO: run class file, compare output with javac class file version
    }

    @Test(enabled = true)
    void testSingleBenchmark() throws IOException {
        File f = new File("src/test/benchmarks/Simple.emj");
        try { // remove the class file corresponding to this file if it's been already generated
            Files.delete(Paths.get(f.getPath().replace(".emj", ".class")));
            Files.delete(Paths.get(f.getPath().replace(".emj", ".j")));
        } catch (NoSuchFileException e) {
        } //ignored

        // run program with the --cgen flag
        System.out.println("Generating class files for : " + f.getName());
        Emjc.main(new String[]{"--cgen", f.getPath()});

        // attempt to run generated class file with java
        String path = f.getPath().substring(0, f.getPath().lastIndexOf("/"));
        String file = f.getPath().substring(f.getPath().lastIndexOf("/") + 1).replace(".emj", "");

        String command = "java -cp " + path + " " + file;
        System.out.println("command being run: " + command);
        Process p = Runtime.getRuntime().exec(command);

        BufferedReader stdOutput = new BufferedReader(new InputStreamReader(p.getInputStream()));
        BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

        stdOutput.lines().forEach(System.out::println);
        stdError.lines().forEach(System.out::println);
    }


}
