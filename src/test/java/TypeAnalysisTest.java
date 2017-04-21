import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.nio.file.Paths;

import static java.nio.charset.StandardCharsets.UTF_8;

@Test(dataProviderClass = Providers.class)
public class TypeAnalysisTest {
	private ByteArrayOutputStream baos;

	public TypeAnalysisTest() {
		// make the symbol generator print results to a byte stream instead of the terminal
		baos = new ByteArrayOutputStream();
		Emjc.output = new PrintStream(baos);
	}

    @Test(dataProvider = "benchmarkFiles", enabled = false)
    void testAllBenchmarks(File f) {
		System.out.println("Performing type analysis on file: " + f.getName());
		Emjc.main(new String[] {"--type", f.getPath()});

		// fail if the symbol generator printed any errors
		System.out.println(baos.toString());
		Assert.assertSame(new String(baos.toByteArray(), UTF_8).trim().intern(), "Valid eMiniJava Program".intern());
		Emjc.errors = new StringBuilder();
		baos.reset();
    }

    @Test(enabled = true)
    void testSingleFile() {
	    File f = new File("/home/justin/IdeaProjects/emjc1/src/test/benchmarks/Reed, Nathan - primality.emj");
		System.out.println("Performing type analysis on file: " + f.getName());
		Emjc.main(new String[] {"--type", f.getPath()});

		// fail if the symbol generator printed any errors
		System.out.println(baos.toString());
		Assert.assertSame(new String(baos.toByteArray(), UTF_8).trim().intern(), "Valid eMiniJava Program".intern());
		Emjc.errors = new StringBuilder();
		baos.reset();
	}

    @Test(enabled = false)
	void testBasicMethodOverriding() {
		System.out.println("Testing basic method overriding");
		Emjc.main(new String[] {"--type", "src/test/IntentionallyBrokenBenchmarks/BasicMethodOverriding.emj"});

		System.out.println(baos.toString());
		//TODO: check output
	}

}
