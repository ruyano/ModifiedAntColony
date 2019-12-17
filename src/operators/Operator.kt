package operators

abstract class Operator(private val shouldPrint: Boolean) {

    fun myPrint(text: String) {
        if (shouldPrint) {
            print("$text \n")
        }
    }
}