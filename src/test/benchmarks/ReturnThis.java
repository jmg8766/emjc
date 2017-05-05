class ReturnThis{
    public static void main(String[] a){
        System.out.println(new Test9Valid().func());
    }
}

class A {}
class Test9Valid extends A{
        public int func(){
                A a;
                a = this.f();
                return 0;
        }
    public Test9Valid f(){
        return this;
    }
}