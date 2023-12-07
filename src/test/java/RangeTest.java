import fun.gen.Gen;
import fun.gen.IntGen;
import fun.gen.PairGen;
import jio.test.pbt.PropBuilder;
import jio.test.pbt.TestFailure;
import jio.test.pbt.TestResult;
import org.junit.Test;
import types.Range;

public class RangeTest {


    @Test
    public void test_Overlap_Intersection() {

        Gen<Range> rangeGen =
                PairGen.of(IntGen.arbitrary(0, 10),
                           IntGen.arbitrary(0, 10)
                          )
                       .suchThat(p -> p.first() <= p.second())
                       .map(p -> new Range(p.first(), p.second()));


        var builder = PropBuilder.of("overlap-intersection",
                                     PairGen.of(rangeGen, rangeGen),
                                     pair -> {
                                         Range a = pair.first();
                                         Range b = pair.second();
                                         var intersection = a.intersection(b);
                                         if (a.overlap(b) && intersection == null)
                                             return TestFailure.reason("overlap is true but intersection is null");
                                         if (a.notOverlap(b) && intersection != null)
                                             return TestFailure.reason("overlap is false but intersection is not null: %s".formatted(intersection));
                                         return TestResult.SUCCESS;
                                     });

        builder.build().check().assertAllSuccess();


    }
}
