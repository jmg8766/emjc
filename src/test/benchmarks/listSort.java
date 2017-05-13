class listSort {

    public static void main(String[] args) {
        new Start().start();

    }

}

class Start {
    public int start() {

        Maker m;
        List l;
        int[] arr;
        String arrString;
        int i;

        i = 4;
        m = new Maker();
        l = m.cons(54, m.cons(73, m.cons(7, m.cons(42, m.cons(33, m.cons(7, m.nil()))))));
        System.out.println("list: " + l.toString());
        System.out.println("sorted list: " + l.sort().toString());
        arr = l.toArray();

        arr[0] = 4;
        arrString = "";
        i = 0;
        while (i < arr.length) {
            arrString = arrString + arr[i] + ",";
            i = i + 1;
        }
        System.out.println("array: " + arrString);
        return 0;
    }
}

class Maker {

    public List cons(int x, List xs) {
        Cons cns;

        cns = new Cons();
        cns.setX(x);
        cns.setXs(xs);
        cns.setM(this);
        return cns;
    }

    public List nil() {
        return new Nil();
    }
}

class List {
    public String tag() {
        return "";
    }

    public int len() {
        return 0;
    }

    public List insert(int y) {
        return this;
    }

    public List sort() {
        return this;
    }

    public int[] toArray() {
        return new int[0];
    }

    public String toString() {
        return "";
    }

}

class Nil extends List {
    public String tag() {
        return "Nil";
    }

    public String toString() {
        return "nil";
    }
}

class Cons extends List {

    int x;
    List xs;
    Maker m;

    public String tag() {
        return "Cons";
    }

    public boolean setM(Maker n) {
        m = n;
        return true;
    }

    public Maker getM() {
        return m;
    }

    public boolean setX(int y) {
        x = y;
        return true;
    }

    public int getX() {
        return x;
    }

    public boolean setXs(List ys) {
        xs = ys;
        return true;
    }

    public List getXs() {
        return xs;
    }

    public int len() {
        return 1 + xs.len();
    }

    public List insert(int y) {
        List r;
        if (y < x + 1) {
            r = m.cons(y, this);
        } else {
            r = m.cons(x, xs.insert(y));
        }
        return r;
    }

    public List sort() {
        return xs.sort().insert(x);
    }

    public int[] toArray() {
        int l;
        int[] arr;
        int i;
        int[] tlarr;
        l = this.len();
        arr = new int[l];
        arr[0] = x;
        i = 1;
        tlarr = xs.toArray();
        while (i < l) {
            arr[i] = tlarr[i - 1];
            i = i + 1;
        }
        return arr;
    }

    public String toString() {
        return x + "::" + xs.toString();
    }

}
