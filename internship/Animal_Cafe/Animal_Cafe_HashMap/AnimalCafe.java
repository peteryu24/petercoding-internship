package gmx.cafe.animal;

import java.util.*;

import gmx.set.animal.*;
import gmx.set.animal.list.animal.*;

public class AnimalCafe {
	HashMap<String, Animal> anicafe_map = new HashMap<String, Animal>();

	void in(Animal e) {
		if (e instanceof Dog) { // 강아지만 입장 가능
			anicafe_map.put(e.getName(), e);
			System.out.print(e.getName() + " 강아지가 입장하였습니다. \n현재 입장한 강아지: ");

			for (String key : anicafe_map.keySet()) {
				System.out.print(key + " ");
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

