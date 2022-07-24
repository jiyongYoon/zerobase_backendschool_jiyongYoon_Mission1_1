package test;
// 디팬던시 연결 됐는지 확인

import java.sql.Connection;
import java.sql.DriverManager;

public class test2 {
	public static void main(String[] args) {
	try {
		Connection connection = DriverManager.getConnection("jdbc:sqlite:test.db");
		
	
	} catch(Exception e) {
		e.printStackTrace();
	}
	
	}
}
