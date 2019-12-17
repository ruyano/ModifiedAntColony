package operators.mutation

import objects.Anty
import operators.Operator

abstract class Mutation(mutationRate: Double, shouldPrint: Boolean) : Operator(shouldPrint) {

    abstract fun mutate(anty: Anty) : Anty

}