package operators.mutation

import objects.Anty
import java.util.*
import kotlin.random.Random

class BitFlipMutation(
    val mutationRate: Double = 0.1,
    val shouldPrint: Boolean = false
) : Mutation(mutationRate, shouldPrint) {

    override fun mutate(anty: Anty) : Anty {

        myPrint("Sujeito para mutação: $anty")

        val shouldMudate = Random.nextDouble(1.0)
        if (shouldMudate > mutationRate) {
            myPrint("Não mutou")
            return anty
        }

        val genes = anty.genes.dropLast(1)
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

        anty.genes.clear()
        anty.genes.addAll(genes)
        anty.genes.add(genes[0])

        myPrint("Sujeito depois da mutação: $anty")

        return anty
    }
}