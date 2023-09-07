package gmx.cafe.animal;

import gmx.exception.animal.AnimalTypeException;
import gmx.exception.animal.MaximumAgeException;
import gmx.exception.animal.thrower.AnimalTypeExceptionThrower;
import gmx.set.animal.list.Cat;
import gmx.set.animal.list.Dog;

public class Action {
	public static void main(String args[]) {

		AnimalCafe ani_caf = new AnimalCafe();
		AnimalTypeExceptionThrower type_error = new AnimalTypeExceptionThrower();
		MaximumAgeException age_eror = new MaximumAgeException();

		Dog dog1 = new Dog("시츄", "마루", 3);
		Dog dog2 = new Dog("허스키", "백구", 6);
		Dog dog3 = new Dog("도베르만", "흑구", 31);
		Dog dog4 = new Dog("닥스훈트", "황구", 9);

		Cat cat1 = new Cat("아비니시안", "체리", 2);

		// dog1.bite();
		// dog1.bark();
		// dog1.walk();
		// dog1.run();

		// cat1.scratch();
		// cat1.bark();
		// cat1.walk();
		// cat1.run();

		try {
			type_error.in(dog1);
			type_error.in(cat1);

		} catch (AnimalTypeException e) { // 고양이 입장 불가
			System.out.println("커스텀 예외 발생!! \n" + e.getMessage());
		} finally {
			type_error.finish();
		}

		try {
			age_eror.in(dog1);
			age_eror.in(dog3);
		} catch (MaximumAgeException e) { // 30 초과 입장 불가
			System.out.println("예외 클래스 정의와 예외 발생시키는 클래스를 따로 만들지 않을 때 왜 출력 안 되는지?");
		} finally {
			age_eror.finish();
		}
		try {
			int num = Integer.parseInt(dog4.getName()); // NumerFormatException 예외 발생
			System.out.println(num);
		} catch (NumberFormatException e) {
			System.out.println("string -> int 강제 형변환");
		}

		// ani_caf.in(dog1);
		// ani_caf.in(dog2);
		// ani_caf.in(dog3);
		// ani_caf.in(cat1);
		// ani_caf.out(dog1);
		// ani_caf.out(dog1);

		// ani_caf.finish();

	}
}
