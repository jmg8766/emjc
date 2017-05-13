import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.stream.Stream;

public class OptTest extends Providers {

    @DataProvider
    Iterator<Object[]> singleFile() {
//        return Stream.of(new File("src/test/benchmarks/ReturnThis.emj")).map(f -> new Object[] {f}).iterator();
//        return Stream.of(new File("src/test/benchmarks/ThisTest.emj")).map(f -> new Object[] {f}).iterator();
//        return Stream.of(new File("src/test/benchmarks/primality.emj")).map(f -> new Object[] {f}).iterator();
//        return Stream.of(new File("src/test/benchmarks/Simple.emj")).map(f -> new Object[] {f}).iterator();
//        return Stream.of(new File("src/test/benchmarks/LinkedList.emj")).map(f -> new Object[] {f}).iterator();
//        return Stream.of(new File("src/test/benchmarks/ArrayFold.emj")).map(f -> new Object[] {f}).iterator();
//        return Stream.of(new File("src/test/benchmarks/Factorial.emj")).map(f -> new Object[] {f}).iterator();
        return Stream.of(new File("src/test/benchmarks/Array.emj")).map(f -> new Object[] {f}).iterator();
    }

//    @Test(dataProvider = "singleFile", enabled = true)
    @Test(dataProvider = "benchmarkFiles", enabled = true)
    void optTest(File f) throws IOException, InterruptedException {
        Emjc.main(new String[] {"--optinfo", f.getPath()});
        System.out.println();
        CodeGenerationTest.test(f);

        // confirm that optimized emjc compiled program has same output as javac compiled program

        // attempt to run generated class file with java and store the output
//        String path = f.getPath().substring(0, f.getPath().lastIndexOf("/"));
//        String file = f.getPath().substring(f.getPath().lastIndexOf("/") + 1).replace(".emj", "");
//        System.out.println("Finished optimization in:  " + (time + System.currentTimeMillis()) + " ms");

//        System.out.println("Running [emjc] compiled program\n");

//        String newPath = f.getPath().replace(".emj", ".java");
//        String[] cmd1 = {"java", "-cp",path, file};
//        String[] createJavaFileCmd = {"cp",f.getPath(), newPath};
//        String[] classCmd = {"javac", newPath};
//        String[] runCmd = {"java", "-cp", path, file};

//        Process p1 = Runtime.getRuntime().exec(cmd1);
//        p1.waitFor();

        // print any errors that happen while running the emjc compiled program
//        AtomicBoolean failed = new AtomicBoolean(false);
//        new BufferedReader(new InputStreamReader(p1.getErrorStream())).lines().map(i -> {failed.set(true); return i;}).forEach(System.out::println);
//        Assert.assertFalse(failed.get());
//        String emjcCompiledProgramOutput = new BufferedReader(new InputStreamReader(p1.getInputStream())).lines().collect(Collectors.joining("\n"));


        // TODO - ENABLE IT
//        Runtime.getRuntime().exec(createJavaFileCmd).waitFor();

//        time = -System.currentTimeMillis();
        // generate class files with javac
//        System.out.println("Generating class files with [javac] for : [" + f.getName().replace(".emj", ".java")+ "]");
//        Process p2 = Runtime.getRuntime().exec(classCmd);
//        p2.waitFor();
//        System.out.println("class files generated in " + (time + System.currentTimeMillis()) + " ms");
//        new BufferedReader(new InputStreamReader(p2.getErrorStream())).lines().forEach(System.err::println);
//
//        // Run java compiled program
//        System.out.println("Running [javac] compiled program\n");
//        Process p3 = Runtime.getRuntime().exec(runCmd);
//        p3.waitFor();
//        new BufferedReader(new InputStreamReader(p3.getErrorStream())).lines().forEach(System.out::println);
//        String javacCompiledProgramOutput = new BufferedReader(new InputStreamReader(p3.getInputStream())).lines().collect(Collectors.joining("\n"));
//
//        System.out.println("Expected: " + javacCompiledProgramOutput);
//        System.out.println("Actual: " + emjcCompiledProgramOutput);

        // Assert that the output of both programs is equal
//        System.out.println("Comparing the output");
//        Assert.assertEquals(emjcCompiledProgramOutput, javacCompiledProgramOutput);
//        System.out.println("Output is the same!!!");
    }
}
