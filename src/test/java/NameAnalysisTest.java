import org.testng.annotations.Test;

import java.io.File;

@Test(dataProviderClass = Providers.class)
public class NameAnalysisTest {

	@Test(dataProvider = "benchmarkFiles", enabled = true)
	void testGenAstFile(File f) {
		System.out.println("Performing name analysis on file: " + f.getName());
		Emjc.main(new String[] {"--name", f.getPath()});
		System.out.println("Finished name analysis");
	}

	@Test(enabled = false)
	void testAstSingleFile() {
		File f = new File("src/test/benchmarks/Gottshall, Justin - MergeSort.emj");
		System.out.println("Performing name analysis on file: " + f.getName());
		Emjc.main(new String[] {"--name", f.getPath()});
		System.out.println("Finished name analysis");
	}
}
