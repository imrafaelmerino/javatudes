package javatudes
import scala.collection.immutable
import scala.collection.immutable.Seq
import GridB
import decorators.*
object RemoveIslands:

  type Matrix = Seq[Seq[Int]]
  type Pos = (Int,Int)

  def removeIslands(matrix:Matrix):Matrix =
    def nRows = matrix.size
    def rowSize(x:Int) = matrix(x).size
    def isEdge(pos:Pos):Boolean =
      val (x,y) = pos
      y == nRows - 1 || y == 0 || x == 0 || x == rowSize(y) - 1
    
    def neighbors(pos: Pos,visited:Map[Pos,Boolean]): Seq[Pos] =
      val (x, y) = pos
      val allneighbors = Seq((x, y + 1), (x, y - 1), (x + 1, y), (x - 1, y))
      val isInMatrix: Pos => Boolean = (x, y) => y >= 0 && y < matrix.size && x >= 0 & x < matrix(y).size
      allneighbors.filter(isInMatrix).filter(!visited(_))

    def anyNeighborConnected(pos:Pos,visited:Map[Pos,Boolean]):Boolean =
      neighbors(pos,visited).find(isConnected(_, visited.updated(pos,true)))
                            .isDefined
    
    def isConnected(pos:Pos, visited: Map[Pos, Boolean]=Map.empty.withDefaultValue(false)):Boolean =
      val (x,y) = pos
      //y is the index of the row matrix=Seq(row1, row2,...)
      matrix(y)(x) == 1 && (isEdge(pos) || anyNeighborConnected(pos, visited))


    def iterMatrix(matrix:Matrix):Matrix =
      for
        (row,y) <- matrix.zipWithIndex
      yield 
        row.indices.map( x => if isConnected((x,y)) then 1 else 0)
    
    iterMatrix(matrix)
   



