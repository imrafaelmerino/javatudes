package javatudes

object Subsequence:
  
  def isSubsequence[T](sequence: Seq[T], subsequence: Seq[T]): Boolean = 
    if subsequence.isEmpty then return true
    if sequence.isEmpty then return false
    if sequence.head == subsequence.head 
    then isSubsequence(sequence.tail, subsequence.tail) 
    else isSubsequence(sequence.tail, subsequence)
    
    