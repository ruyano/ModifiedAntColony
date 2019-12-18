package operators.mutation

import objects.Ant
import operators.Operator

abstract class Mutation(mutationRate: Double, shouldPrint: Boolean) : Operator(shouldPrint) {

    abstract fun mutate(ant: Ant) : Ant

}