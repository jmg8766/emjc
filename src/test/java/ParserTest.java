import org.testng.annotations.Test;

import java.io.File;

@Test(dataProviderClass = Providers.class)
public class ParserTest {

	@Test(dataProvider = "benchmarkFiles", enabled = false)
	void testGenAstFile(File f) {
		System.out.println("Generating oldast file for " + f.getName());
		Emjc.main(new String[] {"--oldast", f.getPath()});
	}

	@Test(enabled = true)
	void testAstSingleFile() {
		File f = new File("src/test/benchmarks/Simple.emj");
		System.out.println("Generating oldast file for " + f.getName());
		Emjc.main(new String[] {"--oldast", f.getPath()});
	}
}