public class Main {

    static final int size = 10000000;
    static final int h = size / 2;


    public static void main(String[] args) {
        first_method();
        second_method();
    }

    public static void first_method() {
        float[] arr = new float[size];
        for (int i = 0; i < size; i++)
            arr[i] = 1;
        long a = System.currentTimeMillis();
        for (int i = 0; i < size; i++)
            arr[i] = (float)(arr[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
        System.out.println(System.currentTimeMillis() - a);
    }

    public static void second_method() {
        float[] arr = new float[size];
        float[] a1 = new float[h];
        float[] a2 = new float[h];

        for (int i = 0; i < size; i++)
            arr[i] = 1;
        long a = System.currentTimeMillis();

        System.arraycopy(arr, 0, a1, 0, h);
        System.arraycopy(arr, h, a2, 0, h);

        MyThread mt1 = new MyThread();
        MyThread mt2 = new MyThread();

        mt1.set_arr(a1);
        mt2.set_arr(a2);

        mt1.start();
        mt2.start();

        try {
            mt1.join();
            mt2.join();
        } catch (InterruptedException e) {
            System.out.println("InterruptedException");
            return;
        }

        System.arraycopy(mt1.get_arr(), 0, arr, 0, h);
        System.arraycopy(mt2.get_arr(), 0, arr, h, h);


        System.out.println(System.currentTimeMillis() - a);
        //System.out.println("Main OFF");
    }

    static class MyThread extends Thread {
        float[] arr;

        MyThread () {
            arr = new float[h];
        }
        @Override
        public void run() {
            for (int i = 0; i < h; i++) {
                arr[i] = (float) (arr[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
                //System.out.print(" " + i);
            }
            //System.out.println("OFF");
        }

        public float[] get_arr() {
            return arr;
        }

        public void set_arr (float[] a) {
            System.arraycopy(a, 0, arr, 0, h);
        }
    }
}

