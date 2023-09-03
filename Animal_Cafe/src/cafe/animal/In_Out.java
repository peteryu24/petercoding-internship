package cafe.animal;

import set.animal.list.animal.Cat;
import set.animal.list.animal.Dog;

public class In_Out {
    public static void main(String args[]) {

        Animal_Cafe ani_caf = new Animal_Cafe();

        Dog dog1 = new Dog("시츄", "마루", 3);
        Dog dog2 = new Dog("허스키", "백구", 6);
        Dog dog3 = new Dog("도베르만", "흑구", 9);

        Cat cat1 = new Cat("아비니시안", "체리", 2);

        dog1.bite();
        dog1.bark();
        dog1.walk();
        dog1.run();

        cat1.scratch();
        cat1.bark();
        cat1.walk();
        cat1.run();

        ani_caf.in(dog1);
        ani_caf.in(dog2);
        ani_caf.in(dog3);
        ani_caf.in(cat1);
        ani_caf.out(dog1);
        ani_caf.out(dog1);

        ani_caf.finish();

    }
}