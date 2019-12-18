package operators.crossover

import operators.Operator
import objects.Ant

abstract class Crossover(shouldPrint: Boolean) : Operator(shouldPrint) {
    abstract fun execute(father1: Ant, father2: Ant) : ArrayList<Ant>
}