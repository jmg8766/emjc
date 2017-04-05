import org.testng.annotations.Test;

import java.io.File;

@Test(dataProviderClass = Providers.class)
public class ParserTest {

	@Test(dataProvider = "benchmarkFiles")
	void testGenAstFile(File f) {
		System.out.println("Generating oldast file for " + f.getName());
		Emjc.main(new String[] {"--oldast", f.getPath()});
	}

	@Test(enabled = false)
	void testAstSingleFile() {
		File f = new File("src/test/benchmarks/Gottshall, Justin - MergeSort.emj");
		System.out.println("Generating oldast file for " + f.getName());
		Emjc.main(new String[] {"--oldast", f.getPath()});
	}
}