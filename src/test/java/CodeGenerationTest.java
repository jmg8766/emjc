import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CodeGenerationTest extends Providers {

    @DataProvider
    public Iterator<Object[]> singleFile() {
//        return Stream.of(new File("src/test/benchmarks/Gottshall, Justin - MergeSort.emj")).map(f -> new Object[] {f}).iterator();
//        return Stream.of(new File("src/test/benchmarks/TreeVisitor.emj")).map(f -> new Object[] {f}).iterator();
        return Stream.of(new File("src/test/benchmarks/MergSort.emj")).map(f -> new Object[] {f}).iterator();
//        return Stream.of(new File("src/test/benchmarks/BinaryTree.emj")).map(f -> new Object[] {f}).iterator();
//        return Stream.of(new File("src/test/benchmarks/BubbleSort.emj")).map(f -> new Object[] {f}).iterator();
    }

//    @Test(dataProvider = "singleFile")
    @Test(dataProvider = "benchmarkFiles")
    void test(File f) throws IOException, InterruptedException {
        try { // remove the class file corresponding to this file if it's been already generated
            Files.delete(Paths.get(f.getPath().replace(".emj", ".class")));
            Files.delete(Paths.get(f.getPath().replace(".emj", ".j")));
        } catch (NoSuchFileException e) {} //ignored


        // generate class files with emjc
        long time = -System.currentTimeMillis();
        System.out.println("Generating class files with [emjc] for : [" + f.getName() + "]");
        Emjc.main(new String[]{"--cgen", f.getPath()});
        System.out.println("class files generated in " + (time + System.currentTimeMillis()) + " ms");

        // attempt to run generated class file with java and store the output
        String path = f.getPath().substring(0, f.getPath().lastIndexOf("/"));
        String file = f.getPath().substring(f.getPath().lastIndexOf("/") + 1).replace(".emj", "").replace(" ", "\\ ").replace(",", "\\,");
        System.out.println("Running [emjc] compiled program\n");
        Process p1 = Runtime.getRuntime().exec("java -cp " + path + " " + file);
        p1.waitFor();
        // print any errors that happen while running the emjc compiled program
        new BufferedReader(new InputStreamReader(p1.getErrorStream())).lines().forEach(System.out::println);
        String emjcCompiledProgramOutput = new BufferedReader(new InputStreamReader(p1.getInputStream())).lines().collect(Collectors.joining("\n"));


        String newPath = f.getPath().replace(".emj", ".java").replace(" ", "\\ ").replace(",", "\\,");
        Runtime.getRuntime().exec("cp " + f.getPath() + " " + newPath).waitFor();

        time = -System.currentTimeMillis();
        System.out.println("Generating class files with [javac] for : [" + f.getName().replace(".emj", ".java")+ "]");
        Process p2 = Runtime.getRuntime().exec("javac " + newPath);
        p2.waitFor();
        System.out.println("class files generated in " + (time + System.currentTimeMillis()) + " ms");
        // print any errors generated compiling the program with javac
        new BufferedReader(new InputStreamReader(p2.getErrorStream())).lines().forEach(System.err::println);

        System.out.println("Running [javac] compiled program\n");
        Process p3 = Runtime.getRuntime().exec("java -cp " + path + " " + file);
        p3.waitFor();
        // print any errors that happen while running the javac compiled program
        new BufferedReader(new InputStreamReader(p3.getErrorStream())).lines().forEach(System.out::println);
        String javacCompiledProgramOutput = new BufferedReader(new InputStreamReader(p3.getInputStream())).lines().collect(Collectors.joining("\n"));

//        System.out.println("Expected: " + javacCompiledProgramOutput);
//        System.out.println("Actual: " + emjcCompiledProgramOutput);

        // assert that the output of both programs is equal
        System.out.println("Comparing the output");
        Assert.assertEquals(emjcCompiledProgramOutput, javacCompiledProgramOutput);
        System.out.println("Output is the same!!!");
    }


}
