import java.util.Scanner;


public class Main {

    static final int SIZE = 4;

    public static int my_method(String[][] arr) throws MyArraySizeException, MyArrayDataException{
        int result = 0;
        if (SIZE != 4)
            throw new MyArraySizeException("Неверный размер массива");

        for (int i = 0; i < SIZE; i++)
            if (arr[i].length != SIZE)
                throw new MyArraySizeException("Неверный размер массива");


        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                try {
                    int buf = Integer.parseInt(arr[i][j]);
                    result += buf;
                } catch (NumberFormatException e) {
                    throw new MyArrayDataException("Невозможно выполнить преобразование. Координаты (" + i + ", " + j + "). Работа прервана");
                }
            }
        }
        return result;
    }

    public static void main(String[] args) {
        String[][] string_array = new String[SIZE][SIZE];

        Scanner in = new Scanner(System.in);

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                string_array[i][j] = in.nextLine();
            }
        }

        try {
            System.out.println("Сумма массива: " + my_method(string_array));
        }
        catch (MyArraySizeException e) {
            System.out.println(e.getS());
        }
        catch (MyArrayDataException e) {
            System.out.println(e.getMes());
        }


    }


}
