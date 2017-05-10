import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.stream.Stream;

public class OptTest extends Providers {

    @DataProvider
    Iterator<Object[]> singleFile() {
//        return Stream.of(new File("src/test/benchmarks/Gottshall, Justin - MergeSort.emj")).map(f -> new Object[] {f}).iterator();
//        return Stream.of(new File("src/test/benchmarks/TreeVisitor.emj")).map(f -> new Object[] {f}).iterator();
//        return Stream.of(new File("src/test/benchmarks/MergSort.emj")).map(f -> new Object[] {f}).iterator();
//        return Stream.of(new File("src/test/benchmarks/BinaryTree.emj")).map(f -> new Object[] {f}).iterator();
        return Stream.of(new File("src/test/benchmarks/Simple.emj")).map(f -> new Object[] {f}).iterator();
    }

    @Test(dataProvider = "benchmarkFiles")
    void optTest(File f) {
        Emjc.main(new String[] {"--optinfo", f.getPath()});
    }
}
