//author: Nathan Reed (nmr7393)
//name  : Primality
//goal  : Checks if a number is prime

class primality {
	public static void main(String[] a){
		System.out.println(new PrimalityTester().is_prime(6700417)); // it is prime.
	}
}

class PrimalityTester {

	// calculates a % b
	public int mod(int a, int b) {
		return ( a - ( b * ( a / b ) ) ) ;
	}

	// algorithm via https://en.wikipedia.org/wiki/Primality_test#Pseudocode
	public boolean is_prime(int num) {
        boolean retValue;
        int i;
        retValue = true;

		// evaluates as (num < (1 || (num == 1)))    operator precedence: == || <
		// should be (num < 1) || (num == 1)
		if (num < 1 || num == 1)
			retValue = false ;
		else if (num < 3 || num == 3)
			retValue = true ;
		else if ( ( this.mod(num, 2) == 0 ) || ( this.mod(num, 3) == 0 ) )
			retValue = false ;
		else {
		    i = 5;
		    while (!retValue || (i * i) < num || (i * i) == num) {
			    if ( ( this.mod(num, i) == 0 ) || ( this.mod(num, (i + 2)) == 0 ) )
				    retValue = false ;
			    i = i + 6;
		    }
        }
		return retValue;
	}

}
