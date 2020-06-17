import animals.*;
import obstances.*;

public class Main {

    public static void main(String[] args) {
        Animal[] zoo;
        zoo = new Animal[4];
        zoo[0] = new Cat("Murzik");
        zoo[1] = new Hen("Izzy");
        zoo[2] = new Hippo("Hippopo");
        zoo[3] = new Cat("Vasiliy");

        Team team = new Team("DreamTeam", zoo);

        team.Display_team();
        System.out.println();

        Course course = new Course(new Barrier[]{
                new Track(80),
                new Wall(3),
                new Water(10)
        });

        course.doit(team);

        team.Show_result();

    }
}