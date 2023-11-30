package etudes
import decorators.d
import scala.collection.immutable.Seq
import scala.collection.mutable

object RiverSizes:

  def rivers(matrix: Seq[Seq[Int]]): Seq[Int] =
    val visited = mutable.Map[(Int, Int), Boolean]().withDefaultValue(false)

    def solve(positions: Seq[(Int, Int)], riverLengths: Seq[Int] = Seq.empty): Seq[Int] =

      def neighbors(pos: (Int, Int)): Seq[(Int, Int)] =
        val (x, y) = pos
        val allNeighbors = Seq((x, y + 1), (x, y - 1), (x + 1, y), (x - 1, y))
        def isInMatrix: ((Int, Int)) => Boolean =
          (column, row) => (row >= 0 && row < matrix.size) && 
                           (column >= 0 && column < matrix(row).size)
        
        allNeighbors.filter(isInMatrix)
                    .filter(!visited(_))

      def getRiverLength(pos: (Int, Int)): Int =
        visited.update(pos, true)
        val (x, y) = pos
        val value = matrix(y)(x)
        if value == 0
        then 0
        else 1 + neighbors(pos).map(getRiverLength).sum

      if positions.isEmpty then return riverLengths
      val river = positions.head
      if visited(river) then return solve(positions.tail, riverLengths)
      val length = getRiverLength(river)
      solve(positions.tail, if length == 0 then riverLengths else riverLengths :+ length)

    val positions = for
      (row, y) <- matrix.zipWithIndex
      (_, x) <- row.zipWithIndex
    yield (x, y)
    
    solve(positions)


