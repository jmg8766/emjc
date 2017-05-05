class Simple {

    public static void main(String[] args) {
        System.out.println(new TT().asdf());
    }

}

class TT {
    int a;
    String b;

    public String foo() {
        return "hey";
    }

    public String asdf() {
        if(!(3 < 4)) b = "3 is less than 4";
        if(!(5 < 3)) b = "5 is less than 3";
        System.out.println(this.foo());

        return b;
    }
}

