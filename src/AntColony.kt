import objects.Ant
import operators.Operator
import operators.crossover.Crossover
import operators.mutation.Mutation
import operators.selection.Selection
import org.jfree.chart.ChartFactory
import org.jfree.chart.ChartPanel
import org.jfree.chart.plot.PlotOrientation
import org.jfree.data.xy.XYSeries
import org.jfree.data.xy.XYSeriesCollection
import java.lang.Math.pow
import java.util.LinkedHashMap
import javax.swing.JFrame
import kotlin.random.Random

class AntColony (
        val epochsNumber: Int = 50,
        val numFormigas: Int = 100,
        val crossOverAmount: Int = numFormigas / 4,
        val selectionMethod: Selection,
        val crossoverMethod: Crossover,
        val mutation: Mutation,
        val alpha: Double = 8.0, // peso do feromonio
        val beta: Double = 2.0, // peso da distancia
        val reducaoFeromonio: Double = 0.1, // 10% de perda de
        val aumentoFeromonio: Double = 1.0, // 200% de ganho de
        val shouldPrint : Boolean = false
) : Operator(shouldPrint) {

    private var formigas = ArrayList<Ant>()
    private var numCidades = 150
    private var distancias = ArrayList<ArrayList<Double>>()
    private var feromonios: Array<DoubleArray>? = null
    private var results: MutableMap<Int, Double> = LinkedHashMap()
    private var melhorDistancia: Double = 0.0
    private var melhorFormiga: Ant? = null

    init {
        readDistanciasFromFile()
        imprimeTabelaDistancias()
        distribuiFormigas()
        inicializaFeromonios()
        selectionMethod.setPopulation(formigas)
    }

    fun execute() {
        for (i in 0 until epochsNumber) {
            if (!shouldPrint) {
                println(i)
            }
            myPrint("Formigas iniciais na epoca $i:")
            imprimeFormigas()
            executeGeneration(i)
            atualizaFeromonios()
            if (i > 0) {
                atualizaRotasFormigas()
            }
            melhorRota()
            printMelhorRota(i)
            armazenaResultados(i)
        }
        apresentaGrafico()
    }

    private fun melhorRota() {
        myPrint("Formigas para selecionar a melhor:")
        imprimeFormigas()
        for (i in 0 until numFormigas) {
            val dist = formigas[i].totalDistance()
            if (melhorDistancia == 0.0 || dist < melhorDistancia) {
                melhorFormiga = Ant(distancias, formigas[i].genes)
                melhorDistancia = dist
            }
        }
    }

    fun printMelhorRota(epoca: Int) {
        println("Melhor rota da epoca $epoca = ")
        println(melhorFormiga.toString())
        println(" distancia = $melhorDistancia")
    }

    private fun armazenaResultados(numViagem: Int) {
        melhorFormiga?.let {
            results[numViagem + 1] = it.totalDistance()
        }
    }

    private fun executeGeneration(i: Int) {
        for (j in 0 until crossOverAmount) {
            generateSons()
        }
        oderPopulationByFitness()
        myPrint("Formigas ordenadas")
        imprimeFormigas()
        selectNextPopulation()
        myPrint("Formigas Para a geracao")
        imprimeFormigas()
    }

    private fun selectNextPopulation() {
        val bests = formigas.dropLast(crossOverAmount * 2)
        formigas.clear()
        formigas.addAll(bests.shuffled())
    }

    private fun oderPopulationByFitness() {
        formigas.sortByDescending {
            it.fitness()
        }
    }

    private fun generateSons() {
        val father1 = selectionMethod.select()
        val father2 = selectionMethod.select()
        father1?.let { f1 ->
            father2?.let { f2 ->
                val sons = crossoverMethod.execute(f1, f2)
                sons.forEach {
                    formigas.add(mutation.mutate(it))
                }
            }
        }
    }

    private fun readDistanciasFromFile() {
        distancias = readFromFile()
        numCidades = distancias.size
    }

    private fun distribuiFormigas() {
        var i = 0
        for (i in 0 until numFormigas) {
            formigas.add(Ant(distancias))
        }
        myPrint("Formigas iniciais")
        imprimeFormigas()
    }

    private fun isArraysEquals(array1: ArrayList<Int>, array2: ArrayList<Int>): Boolean {
        if (array1.size != array2.size) return false
        array1.forEachIndexed{index, value ->
            if (value != array2[index]) {
                return false
            }
        }
        return true
    }

    private fun inicializaFeromonios() {
        feromonios = Array(numCidades) { DoubleArray(numCidades) }
        for (i in 0 until numCidades) {
            for (j in 0 until numCidades) {
                feromonios!![i][j] = 0.01
            }
        }
    }

    private fun atualizaRotasFormigas() {
        for (i in 0 until numFormigas) {
            val cidade = Random.nextInt(numCidades)
            val ant = Ant(distancias, geraNovaRota(cidade))
            formigas[i] = ant
        }
    }

    private fun geraNovaRota(inicio: Int): ArrayList<Int> {
        val visitadas = BooleanArray(numCidades)
        val novaRota = ArrayList<Int>()
        novaRota.add(inicio)
        visitadas[inicio] = true
        for (i in 0 until numCidades - 1) {
            val origem = novaRota[i]
            val destino = proximaCidade(origem, visitadas)
            novaRota.add(destino)
            visitadas[destino] = true
        }
        novaRota.add(novaRota[0])
        return novaRota
    }

    private fun proximaCidade(origem: Int, visitadas: BooleanArray): Int {
        val aux = DoubleArray(numCidades)
        var soma = 0.0
        for (i in 0 until numCidades) {
            if (i != origem && !visitadas[i]) {
                aux[i] = pow(feromonios!![origem][i], alpha) * pow(0.1 / distancias!![origem][i], beta)
                soma += aux[i]
            }
        }
        for (i in 0 until numCidades) {
            aux[i] /= soma
        }
        val acum = DoubleArray(numCidades + 1)
        for (i in 0 until numCidades) {
            acum[i + 1] = acum[i] + aux[i]
        }
        val p = Random.nextDouble()

        for (i in 0 until acum.size - 1) {
            if (p >= acum[i] && p < acum[i + 1]) {
                return i
            }
        }
        return 0
    }

    private fun atualizaFeromonios() {
        for (i in 0 until numCidades) {
            for (j in 0 until numCidades) {
                if (j == i) {
                    continue
                }
                for (f in 0 until numFormigas) {
                    val dist = formigas[f].totalDistance()
                    val reducao = feromonios!![i][j] * (1 - reducaoFeromonio)
                    var aumento = 0.0
                    if (formigaFezCaminho(formigas[f], i, j)) {
                        aumento = aumentoFeromonio / dist
                    }
                    feromonios!![i][j] = reducao + aumento
                    if (feromonios!![i][j] < 0.0001) {
                        feromonios!![i][j] = 0.0001
                    }
                }
            }
        }
    }

    private fun formigaFezCaminho(formiga: Ant, origem: Int, destino: Int): Boolean {
        if (origem == destino) {
            return false
        }
        formiga.genes.forEachIndexed { i, gene ->
            if (origem == gene) {
                if (i == 0 && formiga.genes[i+1] == destino) {
                    return true
                } else if (i ==  numCidades -1 && formiga.genes[i-1] == destino) {
                    return true
                } else if (i > 0 && i < numCidades -1) {
                    if (formiga.genes[i-1] == destino || formiga.genes[i+1] == destino) {
                        return true
                    }
                }
                return false
            }
        }
        return false
    }

    private fun imprimeFormigas() {
        formigas.forEach {
            myPrint("$it")
        }
    }

    private fun imprimeTabelaDistancias() {
        if (!shouldPrint) {
            return
        }
        println("Distancias:")
        print("----")
        for (i in 0 until numCidades) {
            print("---")
        }
        println()
        print("  - ")
        for (i in 0 until numCidades) {
            print(" $i|")
        }
        println()
        for (i in 0 until numCidades) {
            print("$i - ")
            for (j in 0 until numCidades) {
                val dist = distancias!![i][j]
                if (dist < 10) {
                    print("0$dist|")
                } else {
                    print("$dist|")
                }
            }
            println()
        }
        print("----")
        for (i in 0 until numCidades) {
            print("---")
        }
        println()
    }

    private fun apresentaGrafico() {
        val jf = JFrame("Epoca X melhor distância")
        jf.defaultCloseOperation = JFrame.DISPOSE_ON_CLOSE
        val xy = XYSeries("Melhor distância")
        for (v in results.keys) {
            xy.add(v, results[v])
        }
        val col = XYSeriesCollection(xy)
        val jfc = ChartFactory.createXYLineChart("Epoca X melhor distância", "Epoca", "Distância", col, PlotOrientation.VERTICAL,
                true, true, false)
        val cp = ChartPanel(jfc)
        jf.add(cp)
        jf.pack()
        jf.setLocationRelativeTo(null)
        jf.isVisible = true
    }

}
