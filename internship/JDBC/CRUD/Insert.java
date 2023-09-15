package gmx.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Insert {
	public static void insertValue(Connection connect) {
		Statement state = null;

		System.out.println("\n=========================Insert Values=========================\n");

		// 데이터 삽입 위한 SQL 쿼리
		String[] insertQuery = {
				"INSERT INTO exam.team VALUES ('K05','전북','현대모터스','CHUNBUK HYUNDAI MOTORS FC','1995','D03','560','190','전북 전주시 덕진구 반월동 763-1 전주월드컵경기장 내','063','273-1763','273-1762','http://www.hyundai-motorsfc.com','')",
				"INSERT INTO exam.team VALUES ('K08','성남','일화천마','SEONGNAM ILHWA CHUNMA FC','1988','B02','462','130','경기도 성남시 분당구 야탑동 486번지 성남 제2종합운동장 내','031','753-3956','753-4443','http://www.esifc.com','')",
				"INSERT INTO exam.team VALUES ('K03','포항','스틸러스','FC POHANG STEELERS','1973','C06','790','050','경북 포항시 죽도동 614-8 동양빌딩 7층','054','282-2002','282-5955','http://www.steelers.co.kr','')",
				"INSERT INTO exam.team VALUES ('K07','전남','드래곤즈','CHUNNAM DRAGONS FC','1994','D01','544','010','전남 광양시 중동 1318-5 신한은행빌딩 2층','061','792-5600','792-5605','http://www.dragons.co.kr','')",
				"INSERT INTO exam.team VALUES ('K09','서울','FC서울','FOOTBALL CLUB SEOUL','1983','B05','138','221','서울 마포구 성산동 515 월드컵 경기장 내','02','2005-5746','2005-5802','http://www.fcseoul.com','')",
				"INSERT INTO exam.team VALUES ('K04','인천','유나이티드','INCHEON UNITED FC','2004','B01','110','728','인천광역시 남구 문학동 482 인천월드컵경기장 내','032','2121-5271','2121-5276','http://www.incheonutd.com','')",
				"INSERT INTO exam.team VALUES ('K11','경남','경남FC','GYEONGNAM FC','2006','C05','111','222','경남 창원시 두대동 145 창원종합운동장','055','6644-8468','6644-8488','http://www.gsndfc.co.kr','')",
				"INSERT INTO exam.team VALUES ('K01','울산','울산현대','ULSAN HYUNDAI FC','1986','C04','682','060','울산광역시 동구 서부동 산137-1 현대스포츠클럽하우스','052','230-6141','230-6145','http://www.uhfc.tv','')",
				"INSERT INTO exam.team VALUES ('K10','대전','시티즌','DAEJEON CITIZEN FC','1996','D02','301','030','대전광역시 유성구 노은동 270 대전월드컵경기장 서관 3층','042','252-2002','221-0669','http://www.dcfc.co.kr','')",
				"INSERT INTO exam.team VALUES ('K02','수원','삼성블루윙즈','SUWON SAMSUNG BLUEWINGS FC','1995','B04','440','220','수원시 팔달구 우만1동 228 수원월드컵경기장 4층','031','247-2002','257-0766','http://www.bluewings.kr','')",
				"INSERT INTO exam.team VALUES ('K12','광주','광주상무','GWANGJU SANGMU FC','1984','A02','450','419','광주광역시 서구 풍암동 423-2 광주월드컵경기장 내','062','777-5180','777-5181','http://www.gwangjusmfc.co.kr','')",
				"INSERT INTO exam.team VALUES ('K06','부산','아이파크','BUSAN IPARK FC','1983','C02','570','050','부산광역시 강서구 대저1동 2155번지 강서체육공원 내','051','555-7101','555-7103','http://www.busanipark.co.kr','')",
				"INSERT INTO exam.team VALUES ('K13','강원','강원FC','GANGWON FC','2008','A03','333','444','강원 춘천시 중앙로 7 강원도 개발공사 빌딩 내','033','459-3631','459-3630','http://www.gangwon-fc.com','')",
				"INSERT INTO exam.team VALUES ('K14','제주','제주유나이티드FC','JEJU UNITED FC','1982','A04','555','666','제주 서귀포시 강정동 3355-5','064','3631-2460','3631-2468','http://www.jeju-utd.com','')",
				"INSERT INTO exam.team VALUES ('K15','대구','대구FC','DAEGU FC','2002','A05','777','888','대구광역시 수성구 대흥동 504 대구스타디움 내','053','5566-3967','5566-3970','http://www.daegufc.co.kr','')" };

		try {
			state = connect.createStatement(); // Statement 객체 생성

			// 데이터 삽입
			for (int i = 0; i < insertQuery.length; i++) {
				String query = insertQuery[i];
				try {
					state.executeUpdate(query); // SQL 쿼리 실행
					System.out.println("쿼리문 성공: " + query);
				} catch (SQLException e) {
					System.out.println("쿼리문 실패: " + query);

				}
			}
			System.out.println("데이터가 성공적으로 삽입되었습니다."); // 삽입 성공
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// Statement 닫기
			try {
				if (state != null) {
					state.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

			// Connection 닫기
			try {
				if (connect != null) {
					connect.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
