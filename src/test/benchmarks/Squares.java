//author: Ziwei Ye

class Squares {
    public static void main(String[] a){
	System.out.println(new CountSquares().countSquares(6 , 40)) ;
    }
}

class CountSquares {

    public int countSquares(int a, int b) { // calculates number of squares between a and b - the very naive way
        int c ;
	int i ;
	int j ;
	i = a ;
	j = 1 ;
	c = 0 ;

	while (i < (b + 1)) {
	    while (j * j < (i + 1)) {
		if (j * j == i)
		    c = c + 1 ;
		j = j + 1 ;
	    }
	    i = i + 1 ;
	}
        return c ;
    }

}
