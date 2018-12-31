package taxipark

/*
 * Task #1. Find all the drivers who performed no trips.
 */
fun TaxiPark.findFakeDrivers(): Set<Driver> {
    val driversWithTrips = this.trips.map { it.driver }.distinct()
    return this.allDrivers.filter { driver -> driver !in driversWithTrips }.toSet()
}

/*
 * Task #2. Find all the clients who completed at least the given number of trips.
 */
fun TaxiPark.findFaithfulPassengers(minTrips: Int): Set<Passenger> {
    return this.trips
            .flatMap { it.passengers }
            .groupingBy { it }
            .eachCount()
            .filter { it.value >= minTrips }
            .keys
}

/*
 * Task #3. Find all the passengers, who were taken by a given driver more than once.
 */
fun TaxiPark.findFrequentPassengers(driver: Driver): Set<Passenger> {
    return this.trips
            .filter { it.driver == driver }
            .flatMap { it.passengers }
            .groupingBy { it }
            .eachCount()
            .filter { it.value > 1 }
            .keys
}

/*
 * Task #4. Find the passengers who had a discount for majority of their trips.
 */
fun TaxiPark.findSmartPassengers(): Set<Passenger> {
    val passengersWithDiscount = this.trips
            .filter { it.discount != null && it.discount > 0.0 }
            .flatMap { it.passengers }
            .groupingBy { it }
            .eachCount()

    val passengersWithNoDiscount = this.trips
            .filter { it.discount == null || it.discount == 0.0 }
            .flatMap { it.passengers }
            .groupingBy { it }
            .eachCount()

    return passengersWithDiscount
            .filter { it.value > passengersWithNoDiscount[it.key] ?: 0 }
            .keys
}

/*
 * Task #5. Find the most frequent trip duration among minute periods 0..9, 10..19, 20..29, and so on.
 * Return any period if many are the most frequent, return `null` if there're no trips.
 */
fun TaxiPark.findTheMostFrequentTripDurationPeriod(): IntRange? {
    val idx = this.trips
            .groupBy { it.duration / 10 }
            .map { Pair(it.key, it.value.count()) }
            .maxBy { p -> p.second }
            ?.first

    val min = idx?.times(10)
    val max = min?.plus(9) ?: 0
    return min?.rangeTo(max)
}

/*
 * Task #6.
 * Check whether 20% of the drivers contribute 80% of the income.
 */
fun TaxiPark.checkParetoPrinciple(): Boolean {
    if (this.trips.isEmpty())
        return false

    val totalCost = this.trips.sumByDouble { it.cost }
    val moreSuccessfulDrivers = this.trips
            .groupBy { it.driver }
            .map { it -> Pair(it.key, it.value.sumByDouble { trip -> trip.cost }) }
            .sortedByDescending { it.second }
            .toList()
    val twentyPercentOfDrivers = (this.allDrivers.count() * 20) / 100
    val topTwentyDrivers = moreSuccessfulDrivers
            .take(twentyPercentOfDrivers)

    val eightyPercentCost = (totalCost * 80) / 100
    val topTwentyDriversCost = topTwentyDrivers.sumByDouble { it.second }

    return topTwentyDriversCost >= eightyPercentCost
    }
