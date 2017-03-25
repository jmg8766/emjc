import org.testng.annotations.Test;

import java.io.File;

@Test(dataProviderClass = Providers.class)
public class ParserTest {

	@Test(dataProvider = "benchmarkFiles", enabled = true)
	void testGenAstFile(File f) {
		System.out.println("Generating ast file for " + f.getName());
		Emjc.main(new String[] {"--ast", f.getPath()});
	}

	@Test(enabled = false)
	void testAstSingleFile() {
		File f = new File("src/test/benchmarks/Gottshall, Justin - MergeSort.emj");
		System.out.println("Generating ast file for " + f.getName());
		Emjc.main(new String[] {"--ast", f.getPath()});
	}
}