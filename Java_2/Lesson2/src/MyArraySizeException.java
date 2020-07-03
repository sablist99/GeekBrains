public class MyArraySizeException extends Exception {
    private String s;

    public MyArraySizeException (String s) {
        this.s = s;
    }

    public String getS() { return s; }

}
