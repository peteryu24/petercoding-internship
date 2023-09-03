package animal.animal_list;

import animal.set_animal.Set_Animal;

public class Cat extends Set_Animal {

    public Cat(String species, String name, int age) {
        super(species, name, age);

    }

    public void scratch() {
        System.out.println(super.species + " " + super.name + "가 할큅니다.\n");
    }
}

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


package animal.set_animal;

public class Set_Animal {

    protected String species;
    protected String name;
    protected int age;

    protected Set_Animal(String species, String name, int age) {
        this.species = species;
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void bark() {
        System.out.println(this.species + " " + this.name + "가 짖습니다.\n");
    }

    public void walk() {
        System.out.println(this.species + " " + this.name + "가 걷습니다.\n");
    }

    public void run() {
        System.out.println(this.species + " " + this.name + "가 뜁니다.\n");
    }
}
package cafe;

import java.util.*;

import animal.animal_list.Dog;
import animal.set_animal.Set_Animal;

public class Animal_Cafe {

    ArrayList<Set_Animal> animal_cafe = new ArrayList<>();

    void in(Set_Animal e) {
        if (e instanceof Dog) {
            animal_cafe.add(e);
            System.out.print(((Set_Animal) e).getName() + " 강아지가 입장하였습니다. \n현재 입장한 강아지: ");
            for (int i = 0; i < animal_cafe.size(); i++) {
                System.out.print(animal_cafe.get(i).getName() + " ");
            }
            System.out.println("\n");
        } else {
            System.out.println("죄송합니다 강아지만 입장 가능합니다.\n");
        }
    }

    void out(Set_Animal e) {
        boolean isContainsDog = animal_cafe.contains(e);
        if (isContainsDog) {
            animal_cafe.remove(e);
            System.out.print(((Set_Animal) e).getName() + " 강아지가 퇴장하였습니다.\n현재 입장한 강아지: ");
            for (int i = 0; i < animal_cafe.size(); i++) {
                System.out.print(animal_cafe.get(i).getName() + " ");
            }
            System.out.println("\n");
        } else {
            System.out.println("현재 " + ((Set_Animal) e).getName() + "는 저희 카페 안에 없습니다.\n");
        }
    }

    void finish() {
        animal_cafe.clear();
        System.out.println("영업을 종료합니다.\n");
    }
}

package cafe;

import animal.animal_list.*;

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




