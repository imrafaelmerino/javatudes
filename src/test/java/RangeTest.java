import fun.gen.Gen;
import fun.gen.IntGen;
import fun.gen.PairGen;
import jio.test.pbt.PropBuilder;
import jio.test.pbt.TestFailure;
import jio.test.pbt.TestResult;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import types.LongRange;

import java.util.List;

public class RangeTest {

    @Test
    public void test_Difference_Intervals() {


        Assertions.assertEquals(List.of(new LongRange(62, 69)),
                                new LongRange(61, 69).difference(new LongRange(61, 61)));
        Assertions.assertEquals(List.of(new LongRange(61, 68)),
                                new LongRange(61, 69).difference(new LongRange(69, 69)));

        Assertions.assertEquals(List.of(new LongRange(1, 2)),
                                new LongRange(1, 4).difference(new LongRange(3, 4)));
        Assertions.assertEquals(List.of(new LongRange(3, 4)),
                                new LongRange(1, 4).difference(new LongRange(1, 2)));

        Assertions.assertEquals(List.of(),
                                new LongRange(1, 2).difference(new LongRange(1, 2)));
    }

    @Test
    public void test_Overlap_Intersection() {

        Gen<LongRange> rangeGen =
                PairGen.of(IntGen.arbitrary(0, 10),
                           IntGen.arbitrary(0, 10)
                          )
                       .suchThat(p -> p.first() <= p.second())
                       .map(p -> new LongRange(p.first(), p.second()));


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
