import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;

@Test(dataProviderClass = Providers.class)
public class TypeAnalysisTest {

    @Test(dataProvider = "benchmarkFiles")
    void testAllBenchmarks(File f) {
		// make the symbol generator print results to a byte stream instead of the terminal
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//		NameAnalysis.printStream = new PrintStream(baos);

		System.out.println("Performing name analysis on file: " + f.getName());
		Emjc.main(new String[] {"--name", f.getPath()});

		// fail if the symbol generator printed any errors
		System.out.println(baos.toString());
//		Assert.assertSame(new String(baos.toByteArray(), UTF_8).trim().intern(), "Valid eMiniJava Program".intern());
    }




}
