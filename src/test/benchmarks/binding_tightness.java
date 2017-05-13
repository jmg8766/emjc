/*
    Author: Brian Mansfield

    Goal: Test binding tightness
    + multiline comments!
*/

class binding_tightness {
    public static void main(String[] args){
       System.out.println(1 + new ContainerClass().makePrime().initPrimes()[3]); // 8
    }
}

class ContainerClass {
    public Primes makePrime(){
        return new Primes();
    }
}

class Primes {
    int[] primes;

    public int[] initPrimes(){  // returns an array of the first ten prime numbers

        primes = new int[10];

        primes[0] = 2;
        primes[1] = 3;
        primes[2] = 5;
        primes[3] = 7;
        primes[4] = 11;
        primes[5] = 13;
        primes[6] = 17;
        primes[7] = 19;
        primes[8] = 23;
        primes[9] = 29;

        return primes;
    }
}
