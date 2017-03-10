import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.util.Arrays;
import java.util.Iterator;

public class LexerTest {

    @DataProvider
    public Iterator<Object[]> benchmarkFiles() {
        return Arrays
                .stream(new File("src/test/benchmarks").listFiles())
                .filter(f -> !f.getName().endsWith(".lexed"))
                .map(f -> new Object[] {f})
                .iterator();
    }

    @Test(dataProvider = "benchmarkFiles")
    public void testGenLexFile(File f) {
        System.out.println("Generating lex file for" + f.getName());
        // test if the file can be lexed without any errors
        new Lexer(f.getPath()).genLexFile();
    }

    @DataProvider
    public Iterator<Object[]> randomlyGeneratedFiles() {
        return null; //TODO
    }

    @Test(dataProvider="randomlyGeneratedFiles", enabled = false)
    public void testRandomlyGeneratedFiles(File f, String expectedOuput) {
        //TODO
    }

    @Test
    public void testNext() {

    }
}