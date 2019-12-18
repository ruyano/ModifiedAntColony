package operators.mutation

import objects.Ant
import java.util.*
import kotlin.random.Random

class BitFlipMutation(
    val mutationRate: Double = 0.1,
    val shouldPrint: Boolean = false
) : Mutation(mutationRate, shouldPrint) {

    override fun mutate(ant: Ant) : Ant {

        myPrint("Sujeito para mutação: $ant")

        val shouldMudate = Random.nextDouble(1.0)
        if (shouldMudate > mutationRate) {
            myPrint("Não mutou")
            return ant
        }

        val genes = ant.genes.dropLast(1)
        val firstPositionToMutate = Random.nextInt(genes.size)
        var secondPositionToMutate = Random.nextInt(genes.size)

        // Garantir que não são iguais
        while (firstPositionToMutate == secondPositionToMutate) {
            secondPositionToMutate = Random.nextInt(genes.size)
        }

        myPrint("Antes da mutação: $genes")
        myPrint("Posição 1 = $firstPositionToMutate | Posição 2 = $secondPositionToMutate")

        Collections.swap(genes, firstPositionToMutate, secondPositionToMutate)

        myPrint("depois da mutação: $genes")

        ant.genes.clear()
        ant.genes.addAll(genes)
        ant.genes.add(genes[0])

        myPrint("Sujeito depois da mutação: $ant")

        return ant
    }
}