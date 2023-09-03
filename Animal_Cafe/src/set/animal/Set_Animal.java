package set.animal;

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