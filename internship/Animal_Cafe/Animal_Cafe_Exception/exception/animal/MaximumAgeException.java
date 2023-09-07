package gmx.exception.animal;

import java.util.HashMap;

import gmx.set.animal.Animal;

public class MaximumAgeException extends Exception {
	HashMap<String, Animal> anicafe_map = new HashMap<String, Animal>();

	public void in(Animal e) throws MaximumAgeException {
		if ((e.getAge() > 30)) {
			System.out.println("\n30살 미만의 강아지만 입장이 가능합니다.");
		} else {
			anicafe_map.put(e.getName(), e);
			System.out.print(e.getName() + " 강아지가 입장하였습니다. \n현재 입장한 강아지: ");

			for (String key : anicafe_map.keySet()) {
				System.out.print(key + " ");
			}
		}
		System.out.println("");

	}

	public void finish() { // 영업 종료
		anicafe_map.clear();
		System.out.println("영업을 종료합니다.\n");
	}
}
