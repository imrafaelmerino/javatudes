import fun.gen.Gen;
import fun.gen.IntGen;
import fun.gen.PairGen;
import jio.test.pbt.PropBuilder;
import jio.test.pbt.TestFailure;
import jio.test.pbt.TestResult;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import types.Range;

import java.util.List;

public class RangeTest {

    @Test
    public void test_Difference_Intervals() {


        Assertions.assertEquals(List.of(new Range(62, 69)),
                                new Range(61, 69).difference(new Range(61, 61)));
        Assertions.assertEquals(List.of(new Range(61, 68)),
                                new Range(61, 69).difference(new Range(69, 69)));

        Assertions.assertEquals(List.of(new Range(1,2)),
                                new Range(1,4).difference(new Range(3,4)));
        Assertions.assertEquals(List.of(new Range(3,4)),
                                new Range(1,4).difference(new Range(1,2)));

        Assertions.assertEquals(List.of(),
                                new Range(1,2).difference(new Range(1,2)));
    }

    @Test
    public void test_Overlap_Intersection() {

        Gen<Range> rangeGen =
                PairGen.of(IntGen.arbitrary(0, 10),
                           IntGen.arbitrary(0, 10)
                          )
                       .suchThat(p -> p.first() <= p.second())
                       .map(p -> new Range(p.first(), p.second()));


        var builder = PropBuilder.of("intersection must be coherent with overlap",
                                     PairGen.of(rangeGen, rangeGen),
                                     pair -> {
                                         var a = pair.first();
                                         var b = pair.second();
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
