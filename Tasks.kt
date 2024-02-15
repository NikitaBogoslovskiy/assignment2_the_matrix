@file:Suppress("UNUSED_PARAMETER")
package mmcs.assignment2

import mmcs.assignment2.Matrix
import mmcs.assignment2.createMatrix

/**
 * Пример
 *
 * Транспонировать заданную матрицу matrix.
 */
fun <E> transpose(matrix: Matrix<E>): Matrix<E> {
    if (matrix.width < 1 || matrix.height < 1) return matrix
    val result = createMatrix(height = matrix.width, width = matrix.height, e = matrix[0, 0])
    for (i in 0 until matrix.width) {
        for (j in 0 until matrix.height) {
            result[i, j] = matrix[j, i]
        }
    }
    return result
}

fun <E> rotate(matrix: Matrix<E>): Matrix<E> = transpose(matrix)

/**
 * Сложить две заданные матрицы друг с другом.
 * Складывать можно только матрицы совпадающего размера -- в противном случае бросить IllegalArgumentException.
 * При сложении попарно складываются соответствующие элементы матриц
 */
operator fun Matrix<Int>.plus(other: Matrix<Int>): Matrix<Int> {
    if (height != other.height || width != other.width) {
        throw IllegalArgumentException()
    }
    val mat = createMatrix(height, width, 0)
    for (i in 0..<height) {
        for (j in 0..<width) {
            mat[i, j] = this[i, j] + other[i, j]
        }
    }
    return mat
}

/**
 * Инвертировать заданную матрицу.
 * При инвертировании знак каждого элемента матрицы следует заменить на обратный
 */
operator fun Matrix<Int>.unaryMinus(): Matrix<Int> {
    val mat = createMatrix(height, width, 0)
    for (i in 0..<height) {
        for (j in 0..<width) {
            mat[i, j] = this[i, j] * -1
        }
    }
    return mat
}

/**
 * Перемножить две заданные матрицы друг с другом.
 * Матрицы можно умножать, только если ширина первой матрицы совпадает с высотой второй матрицы.
 * В противном случае бросить IllegalArgumentException.
 * Подробно про порядок умножения см. статью Википедии "Умножение матриц".
 */
operator fun Matrix<Int>.times(other: Matrix<Int>): Matrix<Int> {
    if (width != other.height) {
        throw IllegalArgumentException()
    }
    val mat = createMatrix(height, other.width, 0)
    for (i in 0..<mat.height) {
        for (j in 0..<mat.width) {
            for (k in 0..<width) {
                mat[i, j] += this[i, k] * other[k, j]
            }
        }
    }
    return mat
}


/**
 * Целочисленная матрица matrix состоит из "дырок" (на их месте стоит 0) и "кирпичей" (на их месте стоит 1).
 * Найти в этой матрице все ряды и колонки, целиком состоящие из "дырок".
 * Результат вернуть в виде Holes(rows = список дырчатых рядов, columns = список дырчатых колонок).
 * Ряды и колонки нумеруются с нуля. Любой из спискоов rows / columns может оказаться пустым.
 *
 * Пример для матрицы 5 х 4:
 * 1 0 1 0
 * 0 0 1 0
 * 1 0 0 0 ==> результат: Holes(rows = listOf(4), columns = listOf(1, 3)): 4-й ряд, 1-я и 3-я колонки
 * 0 0 1 0
 * 0 0 0 0
 */

fun findHoles(matrix: Matrix<Int>): Holes {
    val rows = mutableListOf<Int>()
    val columns = mutableListOf<Int>()

    for (i in 0..<matrix.height) {
        var onlyHoles = true
        for (j in 0..<matrix.width) {
            if (matrix[i, j] == 1) {
                onlyHoles = false
                break
            }
        }
        if (onlyHoles) {
            rows.add(i)
        }
    }

    for (j in 0..<matrix.width) {
        var onlyHoles = true
        for (i in 0..<matrix.height) {
            if (matrix[i, j] == 1) {
                onlyHoles = false
                break
            }
        }
        if (onlyHoles) {
            columns.add(j)
        }
    }

    return Holes(rows, columns)
}

/**
 * Класс для описания местонахождения "дырок" в матрице
 */
data class Holes(val rows: List<Int>, val columns: List<Int>)

/**
 * Даны мозаичные изображения замочной скважины и ключа. Пройдет ли ключ в скважину?
 * То есть даны две матрицы key и lock, key.height <= lock.height, key.width <= lock.width, состоящие из нулей и единиц.
 *
 * Проверить, можно ли наложить матрицу key на матрицу lock (без поворота, разрешается только сдвиг) так,
 * чтобы каждой единице в матрице lock (штырь) соответствовал ноль в матрице key (прорезь),
 * а каждому нулю в матрице lock (дырка) соответствовала, наоборот, единица в матрице key (штырь).
 * Ключ при сдвиге не может выходить за пределы замка.
 *
 * Пример: ключ подойдёт, если его сдвинуть на 1 по ширине
 * lock    key
 * 1 0 1   1 0
 * 0 1 0   0 1
 * 1 1 1
 *
 * Вернуть тройку (Triple) -- (да/нет, требуемый сдвиг по высоте, требуемый сдвиг по ширине).
 * Если наложение невозможно, то первый элемент тройки "нет" и сдвиги могут быть любыми.
 */
fun canOpenLock(key: Matrix<Int>, lock: Matrix<Int>): Triple<Boolean, Int, Int> {
    val heightDifference = lock.height - key.height
    val widthDifference = lock.width - key.width
    for (heightShift in 0..heightDifference) {
        for (widthShift in 0..widthDifference) {
            var areCompatible = true
            for (i in 0..<key.height) {
                for (j in 0..<key.width) {
                    if (key[i, j] == lock[i + heightShift, j + widthShift]) {
                        areCompatible = false
                        break
                    }
                }
                if (!areCompatible) {
                    break
                }
            }
            if (areCompatible) {
                return Triple(true, heightShift, widthShift)
            }
        }
    }
    return Triple(false, -1, -1)
}