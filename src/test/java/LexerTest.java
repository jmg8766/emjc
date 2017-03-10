import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.util.Arrays;
import java.util.Iterator;

public class LexerTest {

    @DataProvider()
    public Iterator<Object[]> emjFiles() {
        return Arrays
                .stream(new File("src/test/benchmarks").listFiles())
                .filter(f -> !f.getName().endsWith(".lexed"))
                .map(f -> new Object[] {f})
                .iterator();
    }

    @Test(dataProvider = "emjFiles")
    public void testGenLexFile(File f) {
        System.out.println("Generating lex file for" + f.getName());
        Assert.assertEquals(true, new Lexer(f.getPath()).genLexFile());
    }

    @Test
    public void testNext() throws Exception {

    }

}