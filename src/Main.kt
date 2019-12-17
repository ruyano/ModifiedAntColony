import operators.crossover.PMX
import operators.mutation.BitFlipMutation
import operators.selection.Roulette

object Main {

    @JvmStatic
    fun main(args: Array<String>) {

        AntColony(
                epochsNumber = 50,
                numFormigas = 100,
                crossOverAmount = 25,
                selectionMethod = Roulette(false),
                crossoverMethod = PMX(false),
                mutation = BitFlipMutation(0.1,false),
                alpha = 3.0,
                beta = 2.0,
                reducaoFeromonio = 0.1,
                aumentoFeromonio = 1.0
        ).execute()

    }

}