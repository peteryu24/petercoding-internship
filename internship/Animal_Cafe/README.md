# 🐌 애견카페 🐫

### 디렉토리 :palm_tree:

```
📦Animal_Cafe                          
   └─ src
      ├─ cafe.animal
      │     ├─ AnimalCafe.java - 애견 카페 (입장 / 퇴장 메소드 구현)
      │     │
      │     └─ Action.java - 동물 생성자 호출 / 입장과 퇴장 메소드 호출
      │
      └─ set.animal
            │
            ├─ Animal.java - 동물 생성자 정의 (3가지 기능 구현 메소드)
            │
            └─ list.animal
                   ├─ Cat.java - 고양이 (1가지 기능 추가)
                   └─ Dog.java - 강아지 (1가지 기능 추가)
```

### 기능구현 🐸
- ArrayList / HashMap 이용한 애견카페 출입
  - 입장시 강아지 / 고양이 판별
    - 고양이 입장시 입장 불가 문구 출력
    - 강아지 입장시 이름과 함께 입장 문구 출력과 </br>현재 입장해 있는 강아지 리스트 출력
  - 퇴장시 현재 입장해 있는 강아지 리스트 출력
  - 퇴장하려는 강아지가 입장한 적이 없는 경우 </br>해당 강아지가 없다는 문구 출력
  - 영업 종료시 모든 강아지 퇴장

</br>

### 동물 종류 🐸

| 종 | 이름 | 나이 |
| :--: | :--: | :--: |
| 시츄 | 마루 | 3 |
| 허스키 | 백구 | 6 |
| 도베르만 | 흑구 | 9 |
| 아비니시안 | 체리 | 2 |
</br>

### 실행결과 📐
![실행](https://github.com/peteryu24/petercoding-java/assets/67302252/a6ee0569-b54b-4d86-93ce-1bd224bda711)



arraylist size 메소드 대신  int 변수 별도 선언으로 효율성 
이름이 null로 들어올 경우 처리