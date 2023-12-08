# Introducción

I love programming and dedicate time to refine my skills regularly. As part of my learning journey, I choose captivating
problems and algorithms to solve independently, without relying on external libraries. My focus is on Java, and I enjoy
the challenge of implementing solutions from scratch. This hands-on approach not only deepens my understanding of the
programming language but also hones my problem-solving skills. It's a gratifying process that allows me to explore and
apply concepts, fostering a stronger foundation in both programming and algorithmic thinking.

"An étude (a French word meaning study) is an instrumental musical composition, usually short, of considerable
difficulty, and designed to provide practice material for perfecting a particular musical skill." — Wikipedia

This project contains javatudes—Java programs, usually short, for perfecting particular programming skills.

Admittedly, this idea isn't original; I borrowed it from Peter Norvig's Github repository. I simply swapped Java for
Python :)

# Advent of Code

- **[2021 (in Scala)](https://adventofcode.com/2021)**
- **[2022 (in Java)](https://adventofcode.com/2022)**
- **[2023 (in Java)](https://adventofcode.com/2023)**

Beginning in 2023, I've incorporated statistics and scripts to facilitate the compilation and execution of puzzles from
any specific day or the entire calendar. For instance, by running the following commands:

```shell

./compile.sh
./aoc.sh --year 2023 --day all --times 100

```

You will obtain results for all puzzles released up to today, which is the 8th of December. Consequently, this covers a
total of 8 published puzzles.

```text

Advent of Code 2023 @ https://adventofcode.com/2023

- Day 1: Trebuchet?!
  . Part 1:
       Solution: 55260 calibration values
       Stats: avg: 4,263 ms | min: 2,528 ms | max: 49,551 ms | acc: 426,285 ms | times: 100
  . Part 2:
       Solution: 55123 calibration values
       Stats: avg: 1,141 ms | min: 864,250 µs | max: 7,589 ms | acc: 114,082 ms | times: 100

- Day 2: Cube Conundrum
  . Part 1:
       Solution: 2207 sum of IDs
       Stats: avg: 656,942 µs | min: 546,958 µs | max: 6,943 ms | acc: 65,694 ms | times: 100
  . Part 2:
       Solution: 62241 cubes
       Stats: avg: 906,395 µs | min: 481,459 µs | max: 4,224 ms | acc: 90,640 ms | times: 100

- Day 3: Gear Ratios
  . Part 1:
       Solution: 507214 sum of all of the part numbers
       Stats: avg: 8,694 ms | min: 4,430 ms | max: 86,502 ms | acc: 869,395 ms | times: 100
  . Part 2:
       Solution: 72553319 sum of all of the gear ratios
       Stats: avg: 3,168 ms | min: 2,700 ms | max: 7,075 ms | acc: 316,796 ms | times: 100

- Day 4: ScratchCards
  . Part 1:
       Solution: 27845 points
       Stats: avg: 1,913 ms | min: 911,500 µs | max: 12,262 ms | acc: 191,268 ms | times: 100
  . Part 2:
       Solution: 9496801 scratchcards
       Stats: avg: 162,952 ms | min: 158,211 ms | max: 213,829 ms | acc: 16,295 sg | times: 100

- Day 5: If You Give A Seed A Fertilizer
  . Part 1:
       Solution: 196167384 lowest location number
       Stats: avg: 555,529 µs | min: 294,208 µs | max: 7,868 ms | acc: 55,553 ms | times: 100
  . Part 2:
       Solution: 125742456 lowest location number
       Stats: avg: 541,970 µs | min: 379,917 µs | max: 9,169 ms | acc: 54,197 ms | times: 100

- Day 6: Wait For it
  . Part 1:
       Solution: 275724 product of the number of ways you can beat the record
       Stats: avg: 66,302 µs | min: 30,000 µs | max: 2,316 ms | acc: 6,630 ms | times: 100
  . Part 2:
       Solution: 37286485 product of the number of ways you can beat the record
       Stats: avg: 33,116 ms | min: 32,414 ms | max: 85,364 ms | acc: 3,312 sg | times: 100

- Day 7: Camel Cards
  . Part 1:
       Solution: 256448566 total winnings
       Stats: avg: 4,433 ms | min: 2,976 ms | max: 14,966 ms | acc: 443,333 ms | times: 100
  . Part 2:
       Solution: 254412181 total winnings
       Stats: avg: 6,533 ms | min: 4,830 ms | max: 16,636 ms | acc: 653,290 ms | times: 100

- Day 8: Haunted Wasteland
  . Part 1:
       Solution: 20569 steps
       Stats: avg: 7,581 ms | min: 6,610 ms | max: 27,098 ms | acc: 758,054 ms | times: 100
  . Part 2:
       Solution: 21366921060721 steps
       Stats: avg: 9,832 ms | min: 8,869 ms | max: 23,431 ms | acc: 983,159 ms | times: 100


```

[Advent of Code](https://adventofcode.com/) is an annual coding event with daily programming puzzles.

# Javatudes

A collection of Java-based projects demonstrating various algorithmic and puzzle-solving concepts.

## Puzzle Implementations

1. **[Four Colour Map](https://en.wikipedia.org/wiki/Four_color_theorem):** Solving the historic Four Color Map Problem,
   a classic conundrum in graph theory and cartography. Notably, this problem was first conquered
   using [artificial intelligence techniques](https://en.wikipedia.org/wiki/Four_color_theorem#Proof) by mathematicians
   Kenneth Appel and Wolfgang Haken in 1976. The solution involved an exhaustive computer search, marking a pioneering
   application of AI in mathematical problem-solving.
2. **[8 Sliding Puzzle](https://en.wikipedia.org/wiki/15_puzzle):** Implementation of the classic 8-puzzle game.
3. **[15 Sliding Puzzle](https://en.wikipedia.org/wiki/15_puzzle):** A variation using walking distance heuristic
   from [Kenichiro Takahashi](https://computerpuzzle.net/english/15puzzle/wd.gif).
4. **[Bloxorz](https://bloxorz.io/):** A puzzle game involving a rectangular block that must fall into
   a square hole.
5. **[Bridge And Torch](https://en.wikipedia.org/wiki/Bridge_and_torch_problem):** Solving the classic bridge and torch
   problem.
6. **[Game of Life](https://en.wikipedia.org/wiki/Conway%27s_Game_of_Life):** Implementing Conway's Game of Life
   simulation.
7. **[Missionaries and Cannibals](https://en.wikipedia.org/wiki/Missionaries_and_cannibals_problem):** Solving the
   missionaries
   and cannibals river crossing problem.
8. **[Sudoku](https://en.wikipedia.org/wiki/Sudoku_solving_algorithms):** Implementation of a Sudoku solver.
9. **[Water Pouring:](https://en.wikipedia.org/wiki/Water_pouring_puzzle)** Solving a problem related to pouring water
   between jugs to measure a specific quantity.
10. **[Zebra Puzzle](https://en.wikipedia.org/wiki/Zebra_Puzzle):** The Zebra Puzzle, also known as Einstein's Riddle,
    is a classic logic puzzle that involves deducing the characteristics of a set of houses and their occupants. The
    puzzle provides a set of clues, each offering information about the relationships between the houses and the people
    living in them. The challenge is to use deductive reasoning to solve the puzzle and determine the specific
    attributes
    of each house and occupant.
11. **Task Scheduling:** The task scheduling problem is the problem of
    assigning the tasks in the system in a manner that will optimize the overall performance of the application, while
    assuring the correctness of the result.
12. **[Wold Goat And Cabbage](https://en.wikipedia.org/wiki/Wolf,_goat_and_cabbage_problem):** Solving the classic
    river-crossing puzzle with a wolf, goat, and cabbage.

## Algorithms Developed

1. **[Depth First Search (DFS)](https://en.wikipedia.org/wiki/Depth-first_search):** A graph traversal algorithm to find
   the shortest path.
2. **[Breath First Search (BFS)](https://en.wikipedia.org/wiki/Breadth-first_search):** Another graph traversal
   algorithm to find the shortest path.
3. **[Branch and Bound](https://en.wikipedia.org/wiki/Branch_and_bound):** An algorithm for finding the optimal path
   (not necessarily the shortest).
4. **[A Star (A*)](https://en.wikipedia.org/wiki/A*_search_algorithm):** A* is an informed search algorithm based in
   heuristics.
5. **[Floyd and Warshall](https://en.wikipedia.org/wiki/Floyd%E2%80%93Warshall_algorithm):** Algorithms for finding the
   shortest paths in a weighted graph.
6. **[CSP (Constraint Satisfaction Problem)](https://en.wikipedia.org/wiki/Constraint_satisfaction_problem):**
   Implementing various techniques such as backtracking and forward checking
   for solving CSPs.

Feel free to explore the individual projects and algorithms to gain insights into their implementations and usage!
