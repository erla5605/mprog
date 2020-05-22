package com.example.sparatillfill;

public class PrimeNrFinder {

    // finds the next primeNr after recentPrime.
    public long getNextPrime(long recentPrime){
        long nextPrimeNr = recentPrime + 2; // 2 since prime have to be odd
        while (!isPrime(nextPrimeNr)) {
            nextPrimeNr += 2;
        }
        return nextPrimeNr;
    }

    // Checks if the candidate is prime. Code from course page
    private boolean isPrime(long candidate) {
        long sqrt = (long) Math.sqrt(candidate);
        for (long i = 3; i <= sqrt; i += 2) {
            if (candidate % i == 0) {
                return false;
            }
        }
        return true;
    }
}
