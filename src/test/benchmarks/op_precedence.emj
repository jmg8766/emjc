/*
    Author: Brian Mansfield

    Goal: Test operator precedence and left-associative-ness of binary operators
    + multi-line comments!
*/

class op_precedence {
    public static void main(String[] args){
        sidef(new A().method());
    }
}

class A {
    public int method() {
       int operation1;
       boolean operation2;
       boolean operation3;
       boolean operation4;
	   operation1 = 4 + 6 * 2 - 4 + 8 / 2;         // 16
       operation2 = false || !false && true;   // true
       operation3 = 4 * 2 - 5 == 10 / 5 + 1;   // true
       operation4 = false && true == false;    // false

       System.out.println(operation1);
       System.out.println(operation2);
       System.out.println(operation3);
       System.out.println(operation4);
       return 0;
    }
}
