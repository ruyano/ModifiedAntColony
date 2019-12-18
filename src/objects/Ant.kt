package objects

import kotlin.random.Random

data class Ant(
    val distanceMatrix: ArrayList<ArrayList<Double>>,
    var genes: ArrayList<Int> = ArrayList(),
    var selectionProbability: Double = 0.0
) {

    init {
        if (genes.isEmpty()) {
            initializeRamdonGenes()
        }
    }

    fun fitness() : Double {
        return  1 / totalDistance()
    }

    fun totalDistance(): Double {
        var totalDistance = 0.0
        for (i in 0 until genes.size - 1) {
            val firstCity = genes[i]
            val secondCity = genes[i + 1]
            val localDistance = distanceMatrix[firstCity][secondCity]
            totalDistance += localDistance
        }
        return totalDistance
    }

    override fun toString(): String {
        var string = "{ "
        genes.forEachIndexed() { index, gene ->
            string += "$gene"
            if (index != genes.size -1) {
                string += ", "
            }
        }
        string += " } distancia total = ${totalDistance()} fitness = ${fitness()}"
        return string
    }

    private fun initializeRamdonGenes() {
        for (i in 0 until distanceMatrix.size) {
            genes.add(i)
        }
        for (i in 0 until distanceMatrix.size) {
            val dest = Random.nextInt(distanceMatrix.size)
            if (dest != i) {
                val aux = genes[i]
                genes[i] = genes[dest]
                genes[dest] = aux
            }
        }
        genes.add(genes.first())
    }

    fun calculateSelectionProbability(totalFitness: Double) {
        selectionProbability = fitness() / totalFitness
    }
}