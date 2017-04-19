import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;

import static java.nio.charset.StandardCharsets.UTF_8;

@Test(dataProviderClass = Providers.class)
public class NameAnalysisTest {

	@Test(dataProvider = "benchmarkFiles", enabled = true)
	void testAllBenchmarks(File f) {
		// make the symbol generator print results to a byte stream instead of the terminal
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		SymbolGenerator.printStream = new PrintStream(baos);

		System.out.println("Performing name analysis on file: " + f.getName());
		Emjc.main(new String[] {"--name", f.getPath()});
		System.out.println("Finished name analysis");

		// fail if the symbol generator printed any errors
		Assert.assertSame(new String(baos.toByteArray(), UTF_8).trim().intern(), "Valid eMiniJava Program".intern());
	}

	@Test(enabled = false)
	void testSingleFile() {
		File f = new File("src/test/benchmarks/Gottshall, Justin - MergeSort.emj");
		System.out.println("Performing name analysis on file: " + f.getName());
		Emjc.main(new String[] {"--name", f.getPath()});
		System.out.println("Finished name analysis");
	}

	@Test(enabled = false)
	void testNonExistentInputFile() {
		System.out.println("Performing name analysis on non-existent file");
		Emjc.main(new String[] {"--name", "not a file"});
		System.out.println("Finished name analysis");
	}

	@Test
	void testClassExtendingMainClass() {
		System.out.println("Performing name analysis on non-existent file");
		Emjc.main(new String[] {"--name", "not a file"});
		System.out.println("Finished name analysis");
	}

}
