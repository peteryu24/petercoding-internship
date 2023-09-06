package cafe.animal;

import java.util.*;


import set.animal.*;
import set.animal.list.animal.*;

public class AnimalCafe {

    ArrayList<Animal> animal_cafe = new ArrayList<>();

    void in(Animal e) { 
        if (e instanceof Dog) { // 강아지만 입장 가능
            animal_cafe.add(e);
            System.out.print(e.getName() + " 강아지가 입장하였습니다. \n현재 입장한 강아지: ");
            for (int i = 0; i < animal_cafe.size(); i++) { // 현재 현황 출력
                System.out.print(animal_cafe.get(i).getName() + " ");
            }
            System.out.println("\n");
        } else { // 고양이가 입장시
            System.out.println("죄송합니다 강아지만 입장 가능합니다.\n");
        }
    }

    void out(Animal e) {
        boolean isContainsDog = animal_cafe.contains(e); 
        if (isContainsDog) { // 현재 카페에 해당 강아지가 있을 경우
            animal_cafe.remove(e);
            System.out.print(e.getName() + " 강아지가 퇴장하였습니다.\n현재 입장한 강아지: ");
            for (int i = 0; i < animal_cafe.size(); i++) { // 현재 현황 출력
                System.out.print(animal_cafe.get(i).getName() + " ");
            }
            System.out.println("\n");
        } else { // // 현재 카페에 해당 강아지가 없을 경우
            System.out.println("현재 " + e.getName() + "는 저희 카페 안에 없습니다.\n");
        }
    }

    void finish() { // 영업 종료
        animal_cafe.clear();
        System.out.println("영업을 종료합니다.\n");
    }
}

/*
package gmx.cafe.animal;

import java.util.*;

import gmx.set.animal.*;
import gmx.set.animal.list.animal.*;

public class AnimalCafe {

	// 1. HashMap을 이용하여 동물을 담는다.
	// 2. K : 동물 이름 / V : 클래스 형태로 담는다.
	// 로직은 동일하게 유지한다.

	HashMap<String, Animal> anicafe_map = new HashMap<String, Animal>();

	void in(Animal e) {
		if (e instanceof Dog) { // 강아지만 입장 가능
			anicafe_map.put(e.getName(), e);
			System.out.print(e.getName() + " 강아지가 입장하였습니다. \n현재 입장한 강아지: ");

			for (String key : anicafe_map.keySet()) {
				System.out.print(key + " ");
				System.out.println(anicafe_map.get(key).getName());
			}

			System.out.println("\n");
		} else { // 고양이가 입장시
			System.out.println("죄송합니다 강아지만 입장 가능합니다.\n");
		}
	}

	void out(Animal e) {

		if (anicafe_map.containsKey(e.getName())) { // 강아지가 있는 경우
			anicafe_map.remove(e.getName());

			System.out.print(e.getName() + " 강아지가 퇴장하였습니다.\n현재 입장한 강아지: ");

			for (String key : anicafe_map.keySet()) {
				System.out.print(key + " ");
			}

			System.out.println("\n");

		} else { // 강아지가 없는 경우
			System.out.println("현재 " + e.getName() + "는 저희 카페 안에 없습니다.\n");

		}
	}

	void finish() { // 영업 종료
		anicafe_map.clear();
		System.out.println("영업을 종료합니다.\n");
	}
}


*/
