package gmx.exception.animal.thrower;

import java.util.HashMap;

import gmx.exception.animal.AnimalTypeException;
import gmx.set.animal.Animal;
import gmx.set.animal.list.Dog;

public class AnimalTypeExceptionThrower {
	HashMap<String, Animal> anicafe_map = new HashMap<String, Animal>(); 

	public void in(Animal e) throws AnimalTypeException {
		if (!(e instanceof Dog)) {
			throw new AnimalTypeException("예외발생: 강아지만 입장 가능");
		} else {
			anicafe_map.put(e.getName(), e);
			System.out.print(e.getName() + " 강아지가 입장하였습니다. \n현재 입장한 강아지: ");

			for (String key : anicafe_map.keySet()) {
				System.out.print(key + " ");
			}

		}
		System.out.println("\n");
	}

	public void finish() { // 영업 종료
		anicafe_map.clear();
		System.out.println("영업을 종료합니다.\n");
	}
}
