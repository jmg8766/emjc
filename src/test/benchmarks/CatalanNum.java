//author: Ziwei Ye

class CatalanNum {
    public static void main(String[] a){
	System.out.println(new Catalan().computeCatalan(4));
    }
}

class Catalan {

    public int computeCatalan(int n){
	int n_aux ;
	int i ;
	n_aux = 0 ;
	i = 0 ;
	if (n < 2) n_aux = 1 ;
	else {
	    while (i < n) {
		   //n_aux = n_aux + this.computeCatalan(i) * this.computeCatalan(n - i - 1);
		    i = i + 1 ;
	    }
	}
	return n_aux ;
    }

}
