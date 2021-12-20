import java.util.*
import kotlin.math.ln
import kotlin.random.Random

typealias jRandom = java.util.Random

object StatUtils {

    fun exp(lambda: Double): Double {
        return ln(1 - Random.nextDouble()) / -lambda
    }

    fun <T> choice(list: List<Pair<T, Double>>): T {
        val collection = RandomCollection<T>()
        list.forEach {
            collection.add(it.second, it.first)
        }

        return collection.next()
    }
}

internal class RandomCollection<E>(private val random: jRandom = jRandom()) {
    private val map: NavigableMap<Double, E> = TreeMap()
    private var total = 0.0

    fun add(weight: Double, result: E): RandomCollection<E> {
        if (weight <= 0) return this
        total += weight
        map[total] = result
        return this
    }

    fun next(): E {
        val value = random.nextDouble()
        return map.higherEntry(value).value
    }
}
