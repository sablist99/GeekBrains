public class MyArrayDataException extends NumberFormatException {
    private String mes;

    public String getMes() {
        return mes;
    }

    public MyArrayDataException(String s) {
        mes = s;
    }
}
