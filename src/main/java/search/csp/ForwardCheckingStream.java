package search.csp;

//TODO

/**
case class ForwardCheckingStream[Variable, Value](vars: Seq[Variable],
                                                  domain: Map[Variable, Seq[Value]],
                                                  constraints: Iterable[Constraint[Variable, Value]],
                                                  get_neighbors: Map[Variable, Seq[Variable]],
                                                  select_var: (Seq[Variable], Map[Variable, Seq[Value]]) => (Variable, Seq[Variable]) = select_lrv[Variable, Value],
                                                  initial_assignments: Map[Variable, Value] = Map[Variable, Value](),
                                                  debug: Boolean = true) {

  type Assignments = Map[Variable, Value]
  type Return = LazyList[Assignments]
  type Domain = Map[Variable, Seq[Value]]

  val checker: (Variable, Map[Variable, Value]) => Boolean = consistency_checker(constraints)

  private def assign_value(variable: Variable, assignments: Assignments)(value: Value) =
    assignments.updated(variable, value)

  private def filter_domain(domain: Iterable[Value], predicate: Value => Boolean) =
    domain.filter(it => predicate(it))

  private val filter_values = fl(filter_domain, "filter domain values")

  private val c_assign_value: (Variable, Assignments) => Value => Assignments =
    if debug then c(assign_value, "assign value") else assign_value

  private val is_consistent: (Variable, Assignments) => Boolean =
    if debug then go(checker, "is consistent") else checker

  private val c_search: (Seq[Variable], Domain, Assignments) => Return =
    if debug then c(search, "search") else search

  private val backtrack: (Variable, Seq[Variable], Seq[Value], Domain, Assignments) => Return =
    if debug then c(assign_domain, "backtrack") else assign_domain

  private def reduce_domain(variable: Variable, domain: Domain, assignments: Assignments) =
    @tailrec
    def reduce_neighbors_domain(neighbors: Seq[Variable], assignments: Assignments, result: Domain): Domain =
      if neighbors.isEmpty then result
      else
        val neighbor = neighbors.head
        val values = domain(neighbor)
        val reduced_domain = filter_values(values, v => is_consistent(neighbor, c_assign_value(neighbor, assignments)(v)))
        if reduced_domain.isEmpty then Map.empty
        else reduce_neighbors_domain(neighbors.tail, assignments, result.updated(neighbor, reduced_domain.toSeq))

    val neighbors = get_neighbors(variable).filter(!assignments.contains(_))
    reduce_neighbors_domain(neighbors, assignments, domain)

  private def search(vars: Seq[Variable], domain: Domain, assignments: Assignments): Return =
    if vars.isEmpty then LazyList.empty
    else
      val (next, rest) = select_var(vars, domain)
      assign_domain(next, rest, domain(next), domain, assignments)

  private def assign_domain(v: Variable, rest: Seq[Variable], v_domain: Seq[Value], domain: Domain, v_assignments: Assignments): Return =
    if v_domain.isEmpty then LazyList.empty
    else
      val new_assignments = c_assign_value(v, v_assignments)(v_domain.head)
      if is_consistent(v, new_assignments) then
        new_assignments
          #:: {
          val reduced_domain = reduce_domain(v, domain, new_assignments)
          if reduced_domain.isEmpty then assign_domain(v, rest, v_domain.tail, domain, v_assignments)
          else search(rest, reduced_domain, new_assignments)
        }
          #::: backtrack(v, rest, v_domain.tail, domain, v_assignments)
      else assign_domain(v, rest, v_domain.tail, domain, v_assignments)

  def assignments: Return = c_search(vars, domain, initial_assignments)

  def find_first(): Assignments = assignments.filter(is_assignment_complete(vars)).head

  def debug_find_first(print_assignment: Assignments => Unit): Assignments =
    assignments.map(it => {
      print_assignment(it)
      it
    }
    )
      .filter(is_assignment_complete(vars))
      .head

}


**/

