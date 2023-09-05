package set.animal.list.animal;

import set.animal.Animal;

public class Dog extends Animal {

    public Dog(String species, String name, int age) {
        super(species, name, age);

    }

    public void bite() {
        System.out.println(super.species + " " + super.name + "가 뭅니다.\n");
    }

}
