/*
    Author: Brian Mansfield

    Goal: Test operator precedence and left-associative-ness of binary operators
    + multi-line comments!
*/

class precedence {
    public static void main(String[] args){
	   new C().checkPrecedence();
    }
}

class C {

    public int checkPrecedence(){
        int operation1;
        boolean operation2;
        boolean operation3;
        boolean operation4;

        operation1 = 4 - 6 * 2 - 4 - 8 / 2;      // 16
        System.out.println(operation1);

        return 0;
    }
}
