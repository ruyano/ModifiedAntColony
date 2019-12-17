package operators.crossover

import operators.Operator
import objects.Anty

abstract class Crossover(shouldPrint: Boolean) : Operator(shouldPrint) {
    abstract fun execute(father1: Anty, father2: Anty) : ArrayList<Anty>
}