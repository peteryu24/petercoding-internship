package gmx.exception.animal;

public class AnimalTypeException extends Exception {
	private String message;

	public AnimalTypeException(String str) {
		this.message = message;
	}

	@Override
	public String getMessage() { // 강아지가 아닌 동물이 입장 희망시
		return "강아지만 입장 가능합니다\n";
	}

}
