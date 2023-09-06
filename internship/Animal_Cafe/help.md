// 커스텀 예외 클래스 정의
public class CustomException extends Exception {
    // 예외 메시지를 저장할 멤버 변수
    private String message;

    // 생성자를 정의하여 예외 메시지를 받아 멤버 변수에 저장
    public CustomException(String message) {
        this.message = message;
    }

    // 예외 메시지를 반환하는 메서드
    @Override
    public String getMessage() {
        return message;
    }
}

// 예외를 발생시키는 클래스
public class ExceptionThrower {
    public void doSomething() throws CustomException {
        // 특정 조건을 확인하고, 조건이 만족되지 않으면 커스텀 예외 발생
        if (/* 조건 */) {
            throw new CustomException("커스텀 예외 발생: 특정 조건이 만족되지 않음");
        }
        // 그 외 로직 처리
    }

    public static void main(String[] args) {
        ExceptionThrower thrower = new ExceptionThrower();
        try {
            thrower.doSomething();
        } catch (CustomException e) {
            System.out.println("커스텀 예외가 발생했습니다: " + e.getMessage());
        }
    }
}
