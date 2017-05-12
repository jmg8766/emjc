import ast.Identifier;
import ast.expression.IdentifierExp;
import ast.expression.Plus;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CodeGenerationTest extends Providers {

    @DataProvider
    public Iterator<Object[]> singleFile() {
        return Stream.of(new File("src/test/benchmarks/listSort.emj")).map(f -> new Object[] {f}).iterator();
    }

    @Test(dataProvider = "singleFile")
//    @Test(dataProvider = "benchmarkFiles", enabled = true)
    void test(File f) throws IOException, InterruptedException {

        try { // remove the class file corresponding to this file if it's been already generated
            Files.delete(Paths.get(f.getPath().replace(".emj", ".class")));
            Files.delete(Paths.get(f.getPath().replace(".emj", ".j")));
        } catch (NoSuchFileException e) {} //ignored

        // generate class files with emjc
        long time = -System.currentTimeMillis();
        System.out.println("Generating class files with [emjc] for : [" + f.getName() + "]");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Emjc.output = new PrintStream(baos);
        Emjc.main(new String[]{"--cgen", f.getPath()});
        // fail if any errors were printed
        Assert.assertEquals(new String(baos.toByteArray()).trim(), "");
        System.out.println("class files generated in " + (time + System.currentTimeMillis()) + " ms");

        // attempt to run generated class file with java and store the output
        String path = f.getPath().substring(0, f.getPath().lastIndexOf("/"));
        String file = f.getPath().substring(f.getPath().lastIndexOf("/") + 1).replace(".emj", "");
        System.out.println("Running [emjc] compiled program\n");

        String newPath = f.getPath().replace(".emj", ".java");
        String[] cmd1 = {"java", "-cp",path, file};
        String[] createJavaFileCmd = {"cp",f.getPath(), newPath};
        String[] classCmd = {"javac", newPath};
        String[] runCmd = {"java", "-cp", path, file};

        Process p1 = Runtime.getRuntime().exec(cmd1);
        p1.waitFor();

        // print any errors that happen while running the emjc compiled program
        AtomicBoolean failed = new AtomicBoolean(false);
        new BufferedReader(new InputStreamReader(p1.getErrorStream())).lines().map(i -> {failed.set(true); return i;}).forEach(System.out::println);
        Assert.assertFalse(failed.get());
        String emjcCompiledProgramOutput = new BufferedReader(new InputStreamReader(p1.getInputStream())).lines().collect(Collectors.joining("\n"));


        // TODO - ENABLE IT TO CREATE CLASS FILE
//         Runtime.getRuntime().exec(createJavaFileCmd).waitFor();

        time = -System.currentTimeMillis();
        // generate class files with javac
        System.out.println("Generating class files with [javac] for : [" + f.getName().replace(".emj", ".java")+ "]");
        Process p2 = Runtime.getRuntime().exec(classCmd);
        p2.waitFor();
        System.out.println("class files generated in " + (time + System.currentTimeMillis()) + " ms");
        new BufferedReader(new InputStreamReader(p2.getErrorStream())).lines().forEach(System.err::println);

        // Run java compiled program
        System.out.println("Running [javac] compiled program\n");
        Process p3 = Runtime.getRuntime().exec(runCmd);
        p3.waitFor();
        new BufferedReader(new InputStreamReader(p3.getErrorStream())).lines().forEach(System.out::println);
        String javacCompiledProgramOutput = new BufferedReader(new InputStreamReader(p3.getInputStream())).lines().collect(Collectors.joining("\n"));

        System.out.println("Expected: " + javacCompiledProgramOutput);
        System.out.println("Actual: " + emjcCompiledProgramOutput);

        // Assert that the output of both programs is equal
        System.out.println("Comparing the output");
        Assert.assertEquals(emjcCompiledProgramOutput, javacCompiledProgramOutput);
        System.out.println("Output is the same!!!");
    }

}
