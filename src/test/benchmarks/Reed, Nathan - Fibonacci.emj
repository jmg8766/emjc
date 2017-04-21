//author: Nathan Reed (nmr7393)
//name  : Fibonacci
//goal  : Generates the n'th fibonacci number

class Fibonacci {
	public static void main(String[] a){
		System.out.println(new Fib().fibonacci(10));
	}
}

class Fib {

	public int fibonacci(int num){
	    int retValue;
		if (num == 1)
			retValue = 1 ;
		else if (num == 0)
			retValue = 0 ;
		else
			retValue = this.fibonacci(num - 1) + this.fibonacci(num - 2);

		return retValue;
	}

}
