class Simple {

    public static void main(String[] args) {
        System.out.println(new TT().asdf(0, 2));
    }

}

class TT {
    // test if kill works
    public String asdf(int b, int c) {
        int b2;
        BB bb;
        bb = new BB();
        bb.count();
        b2 = 10 * 10;
        b = 10 * 10 + 10 * 10;
        if(!(10 * 10 == 120 + 10 * 10)) {
            System.out.println(b);
        }
        return "asdf";
    }
}


class BB {

    int a;
    int b;
    int c;

    public int setValues() {
            a = 10;
            b = 10;
            c = a + b;
            return 0;
    }

    public int count() {
        this.setValues();
        if(!( a == 0))
            System.out.println(a + b);
        else
            System.out.println(a+b+c);
        return 0;
    }

}

