class Tester{
    public static void main(String[] a){
        System.out.println(new Test4Valid().func(0));
    }
}

class A {}
class B extends A {}

class Test4Valid extends B {
        int x;
    public boolean func(int a){
        A x;
        return (x == this);
    }
}
