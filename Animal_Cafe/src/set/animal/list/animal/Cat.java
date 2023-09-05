package set.animal.list.animal;

import set.animal.Animal;

public class Cat extends Animal {

    public Cat(String species, String name, int age) {
        super(species, name, age);

    }

    public void scratch() {
        System.out.println(super.species + " " + super.name + "가 할큅니다.\n");
    }
}