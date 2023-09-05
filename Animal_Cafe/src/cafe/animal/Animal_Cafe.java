package cafe.animal;

import java.util.*;


import set.animal.*;
import set.animal.list.animal.*;

public class Animal_Cafe {

    ArrayList<Animal> animal_cafe = new ArrayList<>();

    void in(Animal e) {
        if (e instanceof Dog) {
            animal_cafe.add(e);
            System.out.print(((Animal) e).getName() + " 강아지가 입장하였습니다. \n현재 입장한 강아지: ");
            for (int i = 0; i < animal_cafe.size(); i++) {
                System.out.print(animal_cafe.get(i).getName() + " ");
            }
            System.out.println("\n");
        } else {
            System.out.println("죄송합니다 강아지만 입장 가능합니다.\n");
        }
    }

    void out(Animal e) {
        boolean isContainsDog = animal_cafe.contains(e);
        if (isContainsDog) {
            animal_cafe.remove(e);
            System.out.print(((Animal) e).getName() + " 강아지가 퇴장하였습니다.\n현재 입장한 강아지: ");
            for (int i = 0; i < animal_cafe.size(); i++) {
                System.out.print(animal_cafe.get(i).getName() + " ");
            }
            System.out.println("\n");
        } else {
            System.out.println("현재 " + ((Animal) e).getName() + "는 저희 카페 안에 없습니다.\n");
        }
    }

    void finish() {
        animal_cafe.clear();
        System.out.println("영업을 종료합니다.\n");
    }
}