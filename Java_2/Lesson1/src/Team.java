import animals.Animal;
import obstances.Barrier;
import java.lang.Class;

public class Team {
    private Animal[] zoo;
    private String name;

    public Team(String n, Animal[] zoo) {
        this.zoo = new Animal[zoo.length];
        this.zoo = zoo;
        name = n;
    }

    public void Display_team() {
        System.out.println("Team`s name: " + name);

        for (Animal animal : zoo) {
            System.out.println("Name: " + animal.Get_name());
            System.out.println("\tsay: " + animal.voice());
        }
    }

    public void doIt(Barrier barrier) {
        for (Animal animal : zoo) {
            Class c = barrier.getClass();

            if(!barrier.doIt(animal))
                animal.Add_result(c.getSimpleName() + " - YES\n");
            else
                animal.Add_result(c.getSimpleName() + " - NO\n");
        }
    }

    public void Show_result() {
        for (Animal animal : zoo) {
            System.out.println(animal.Get_result());
        }
    }

}
