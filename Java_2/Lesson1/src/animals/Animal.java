package animals;

public abstract class Animal {
    protected String name;
    protected String result;
    protected int run_limit;

    public abstract String voice();

    public boolean run(int length) {
        return run_limit >= length;
    }

    public String Get_name() {
        return name;
    }

    public String Get_result() {
        return result;
    }

    @Override
    public String toString() {
        return this.getClass().getName() + " " + name;
    }

    public void Add_result(String s) {
        result += s;
    }
}
