import org.testng.annotations.Test;
import java.io.File;
import java.io.IOException;

@Test(dataProviderClass = Providers.class)
public class LexerTest {

    @Test(dataProvider = "benchmarkFiles")
    void testGenLexFile(File f) throws IOException{
        System.out.println("Generating lex file for " + f.getName());
        // test if the file can be lexed without any errors
        new Lexer(f.getPath()).genLexFile();
    }

}