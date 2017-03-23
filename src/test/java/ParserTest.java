import org.testng.annotations.Test;

import java.io.File;

@Test(dataProviderClass = Providers.class)
public class ParserTest {

	@Test(dataProvider = "benchmarkFiles")
	void testGenAstFile(File f) {
		System.out.println("Generating ast file for " + f.getName());
		Emjc.main(new String[] {"--ast", f.getPath()});
	}

	@Test
	public void testParseProgram() throws Exception {
		//TODO
	}

}