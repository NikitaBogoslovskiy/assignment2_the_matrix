import mmcs.assignment2.*

fun main() {
    val m1 = createMatrix(2, 3, 1)
    println("Created matrix №1:\n$m1")
    println("Transposed matrix:\n${transpose(m1)}")
    println("Rotated matrix:\n${rotate(m1)}")

    val m2 = createMatrix(2, 3, -3)
    println("Created matrix №2:\n$m2")
    println("Sum of matrices №1 and №2:\n${m1 + m2}")
    println("Trying to sum matrices of different sizes...")
    try {
        transpose(m1) + m2
        println("Success")
    } catch (e: IllegalArgumentException) {
        println("Exception: " + e.message)
    }
    println()

    println("Inverted matrix №2:\n${-m2}")
    println("Production of matrices №1 and its transposed variant:\n${m1 * transpose(m1)}")
    println("Trying to product matrix with itself...")
    try {
        m1 * m2
        println("Success")
    } catch (e: IllegalArgumentException) {
        println("Exception: " + e.message)
    }
    println()

    val m3 = createMatrix(5, 4, 0)
    m3[0, 0] = 1
    m3[0, 2] = 1
    m3[1, 2] = 1
    m3[2, 0] = 1
    m3[3, 2] = 1
    println("Created matrix №3 with holes:\n$m3")
    val holes = findHoles(m3)
    println("Rows with holes: ${holes.rows.joinToString(", ")}")
    println("Columns with holes: ${holes.columns.joinToString(", ")}")
    println()

    val lock = createMatrix(3, 3, 1)
    lock[0, 1] = 0
    lock[1, 0] = 0
    lock[1, 2] = 0
    println("Created matrix-lock:\n$lock")
    val key = createMatrix(2, 2, 1)
    key[0, 1] = 0
    key[1, 0] = 0
    println("Created matrix-key:\n$key")
    val result = canOpenLock(key, lock)
    if (result.first) {
        println("Can open lock. You need to shift key by ${result.second} row(s) and ${result.third} column(s).")
    } else {
        println("Cannot open lock")
    }
}