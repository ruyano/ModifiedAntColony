package operators.crossover

import objects.Ant
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random

class PMX(shouldPrint: Boolean = false) : Crossover(shouldPrint) {

    override fun execute(father1: Ant, father2: Ant): ArrayList<Ant> {

        myPrint("Crossover com:\npai 1 = $father1\npai2 = $father2")

        val maxSize = father1.genes.size -1

        var firstCut = Random.nextInt(maxSize)
        var secondCut = Random.nextInt(maxSize)

        // Garante que os pontos de corte não são iguais.
        while (firstCut == secondCut) {
            secondCut = Random.nextInt(maxSize)
        }

        // Garante que o primeiro ponto de corte sempre será maior que o segundo.
        if (firstCut > secondCut) {
            val aux = firstCut
            firstCut = secondCut
            secondCut = aux
        }

        myPrint("Firts cut = $firstCut - Second cut = $secondCut")

        val son1 = geraFilho(firstCut, secondCut, father1, father2)
        val son2 = geraFilho(firstCut, secondCut, father2, father1)

        myPrint("Filho 1 = $son1")
        myPrint("Filho 2 = $son2")

        val sons = ArrayList<Ant>()
        sons.add(son1)
        sons.add(son2)
        return sons
    }

    private fun geraFilho(
            firstCut: Int,
            secondCut: Int,
            receptorFather: Ant,
            donorFather: Ant
    ): Ant {
        val sonGenes = ArrayList<Int>()
        sonGenes.addAll(receptorFather.genes.dropLast(1))
        for (index in firstCut..secondCut) {
            val newVal = donorFather.genes[index]
            Collections.swap(sonGenes, sonGenes.indexOf(newVal), index)
        }
        sonGenes.add(sonGenes[0])
        return Ant(receptorFather.distanceMatrix, sonGenes)
    }

}