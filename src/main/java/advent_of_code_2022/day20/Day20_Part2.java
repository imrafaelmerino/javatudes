package advent_of_code_2022.day20;

import types.FileParsers;

import java.util.List;

public class Day20_Part2 {


    static class Number {
        long n;
        Number next;
        Number previous;


        Number(long n) {
            this.n = n;
        }

        @Override
        public String toString() {
            return "" + n;
        }


        /**
         * prev  ->   n    -> next  -> afterNext
         * prev  ->   next  ->  n   -> afterNext
         *
         * @return
         */
        Number moveOneRight() {
            var next = this.next;
            var previous = this.previous;
            previous.next = next;
            var afterNext = next.next;
            afterNext.previous = this;
            this.next = afterNext;
            this.previous = next;
            next.next = this;
            next.previous = previous;
            return this;
        }

        void move(long period) {
            if (n != 0) {
                var x = n > 0 ? n % period : (period - (-n) % period);
                var c = this;
                for (int i = 0; i < x; i++) c = c.moveOneRight();
            }
        }


        public static void main(String[] args) {

            String input = "/Users/rmerino/Projects/javatudes/src/main/java/advent_of_code_2022/day20/input.txt";

            List<Number> numbers = FileParsers.toListOfLines(input)
                                              .stream()
                                              .map(it -> new Number(811589153L*Integer.parseInt(it)))
                                              .toList();

            var head = numbers.get(0);
            var last = numbers.get(1);
            head.next = last;
            last.previous = head;
            for (int i = 2; i < numbers.size(); i++) {
                var ith = numbers.get(i);
                last.next = ith;
                ith.previous = last;
                last = ith;
            }
            last.next = head;
            head.previous = last;

            for (int i = 0; i < 10; i++) {
                for (Number number : numbers) number.move(numbers.size() - 1);
            }


            var n = numbers.stream()
                           .filter(it -> it.n == 0)
                           .findFirst()
                           .get();


            var c = n;
            for (int i = 0; i < 1000; i++) {
                c = c.next;
            }
            var d = n;
            for (int i = 0; i < 2000; i++) {
                d = d.next;
            }

            var e = n;
            for (int i = 0; i < 3000; i++) {
                e = e.next;
            }
            System.out.println(c.n + d.n + e.n);
        }

    }
}






