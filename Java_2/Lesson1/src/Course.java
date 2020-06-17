import obstances.*;

public class Course {
    private Barrier[] strip;

    public Course(Barrier[] current) {
        strip = new Barrier[current.length];
        strip = current;
    }

    public void doit(Team team) {
        for (Barrier current : strip) {
            team.doIt(current);
        }
    }

}
