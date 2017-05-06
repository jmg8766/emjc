class ArrayFold {
     public static void main ( String [ ] args ) {
        sidef(new start().start());
     }
}

class start {
  public int start() {
        Arrays af; int[] arr; Lt lt; int[] filArr;
        Add addOp; int sum; Mult multOp; int prod;

            arr = new int[6];
            af = new Arrays();
          arr[0] = 54; arr[1] = 73; arr[2] = 7; arr[3] = 42; arr[4] = 33; arr[5] = 7; 
          System.out.println ("array: " + af.toString(arr)) ;

          lt = new Lt(); sidef(lt.setB(41));
          filArr = af.filter(arr, lt);
          System.out.println ("filtered array: " + af.toString(filArr)) ;

          addOp = new Add();
          sum = af.fold(arr, addOp, 0);
          System.out.println ("array sum: " + sum) ;

          multOp = new Mult();
          prod = af.fold(arr, multOp, 1);
          System.out.println ("array prod: " + prod) ;
          return 0;
  } 
}
class Arrays {

    public String toString(int[] arr) {
        String arrString; int i;

        arrString = "";
        i = 0;
        while (i < arr.length) {
            arrString = arrString + arr[i] + ",";
            i = i + 1;
        }
        return arrString;
        
    }

    public int[] filter(int[] arr, IntToBool fil) {
        int i; int[] tmpArr; int x;
        int[] finalArr; int y;

        i = 0;
        tmpArr = new int[arr.length];
        x = 0;
        while (i < arr.length) {
            if (fil.f(arr[i])) {
                tmpArr[x] = arr[i];
                x = x + 1;    
            }
            i = i + 1;
        }

        finalArr = new int[x];
        y = 0;
        while (y < x) {
            finalArr[y] = tmpArr[y];
            y = y + 1;
       }
        
        return finalArr;
        
    }

    public int fold(int[] arr, BinOp op, int base) {
        int i;
        int acc;

        i = 0;
        acc = base;
        while (i < arr.length) {
            acc = op.f(acc, arr[i]);
            i = i + 1;
        }
        return acc;
    } 
}


class BinOp {

    public int f(int a, int b) {
        return 0;
    }
    
}

class IntToBool {

    public boolean f(int a) {
        return false;
    }
    
}

class Add extends BinOp {
    public int f(int a, int b)   {
        return a + b;
    }
}

class Mult extends BinOp {
    public int f(int a, int b)   {
        return a * b;
    }
}

class Lt extends IntToBool {
    int b;
    public boolean f(int a) {
        return a < b;
    }  
    public boolean setB(int c) {
      b = c;
      return true;
    }

    public int getB() {
      return b;
    }
}
