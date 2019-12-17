package operators.selection

import operators.Operator
import objects.Anty

abstract class Selection(shouldPrint: Boolean) : Operator(shouldPrint) {

    val population = ArrayList<Anty>()

    fun setPopulation(population: ArrayList<Anty>) {
        this.population.clear()
        this.population.addAll(population)
    }

    abstract fun select() : Anty?
}