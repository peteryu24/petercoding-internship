package animal.animal_list;

import animal.set_animal.Set_Animal;

public class Dog extends Set_Animal {

    public Dog(String species, String name, int age) {
        super(species, name, age);

    }

    public void bite() {
        System.out.println(super.species + " " + super.name + "가 뭅니다.\n");
    }

}