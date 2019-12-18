package operators.selection

import operators.Operator
import objects.Ant

abstract class Selection(shouldPrint: Boolean) : Operator(shouldPrint) {

    val population = ArrayList<Ant>()

    fun setPopulation(population: ArrayList<Ant>) {
        this.population.clear()
        this.population.addAll(population)
    }

    abstract fun select() : Ant?
}