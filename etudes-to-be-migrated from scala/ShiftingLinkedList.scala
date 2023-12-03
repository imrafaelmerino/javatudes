package javatudes

import scala.collection.immutable.Seq

object ShiftingLinkedList:

  class LinkedList(val head: Int):
    var next: LinkedList = null
    override def toString: String = 
      if next == null 
      then s"$head" 
      else s"$head -> $next"

    override def equals(obj: Any): Boolean =
      obj match
        case other:LinkedList =>
          if next == null then
            if other.next !=null then false
            else head == other.head
          else
            if other.next == null  then false
            else head == other.head && next == other.next

        case _ => false
  end LinkedList    
  
  object LinkedList:
    def from(n: Int*) =
      def appendAll(head: LinkedList, seq: Seq[Int]): LinkedList =
        if seq.isEmpty then head 
        else
          head.next = new LinkedList(seq.head)
          appendAll(head.next, seq.tail)
  
      val head = new LinkedList(n.head)
      appendAll(head, n.tail)
      head
  end LinkedList


  // the problem beings here. Previous code is supposed to be given
  def shift(head: LinkedList, k: Int): LinkedList = 
    if k == 0 then return head
  
    def getLast(head: LinkedList): LinkedList = if head.next == null then head else getLast(head.next)
  
    def getPrevious(head: LinkedList, current: LinkedList): LinkedList = 
     if current == head 
     then null 
     else 
       if head.next == current 
       then head 
        else getPrevious(head.next, current)
  
    if k > 0 then //shift to the right 
      val last = getLast(head)
      val secondLast = getPrevious(head, last)
      last.next = head
      secondLast.next = null
      shift(last, k - 1) 
    else //shift to the left 
      val last = getLast(head)
      val second = head.next
      head.next = null
      last.next = head
      shift(second, k + 1)


