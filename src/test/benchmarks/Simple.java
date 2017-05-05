class Simple {

    public static void main(String[] args) {
        System.out.println(new TT().asdf(5, 3));
    }

}

class TT {
    int a;
    String b2;

    public String asdf(int b, int c) {
        String str;
        a = 10;
        b2 = "hello";
        str = "asd";
        System.out.println(c+b);
        b2 = "ans" + a;
        return "asdf";

    }
}

