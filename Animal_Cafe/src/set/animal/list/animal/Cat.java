package set.animal.list.animal;

import set.animal.Set_Animal;

public class Cat extends Set_Animal {

    public Cat(String species, String name, int age) {
        super(species, name, age);

    }

    public void scratch() {
        System.out.println(super.species + " " + super.name + "가 할큅니다.\n");
    }
}