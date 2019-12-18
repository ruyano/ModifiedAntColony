package operators.selection

import objects.Ant
import kotlin.random.Random

class Roulette(shouldPrint: Boolean = false) : Selection(shouldPrint) {

    override fun select(): Ant? {
        var fitnessSum = 0.0
        var probabilidadeTotal = 0.0

        // Calculate the sum of a finesses.
        population.forEach {
            fitnessSum += it.fitness()
        }

        // calcula a probabilidade de seleção
        population.forEach {
            it.calculateSelectionProbability(fitnessSum)
            probabilidadeTotal += it.selectionProbability
            myPrint("${it.toString()}")
        }
        myPrint("Total da probabilidade = ${probabilidadeTotal * 100}%")

        // Generate a random number between 0 and fitnessSum.
        val randomNumber = Random.nextDouble(probabilidadeTotal)

        myPrint("Numero randomico = ${randomNumber * 100}")

        // Starting from the top of the population, keep adding the finesses to the partial sum P, till P > random.
        var p = 0.0
        population.forEachIndexed() { index, subject ->
            p += subject.selectionProbability
            if (p > randomNumber) {
                myPrint("Item selecionado = subject $index = $subject")
                return subject
            }
        }
        return null
    }

}