package WifiData;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import test.Data;

public class WifiService {
	    
    public static void testSelect() {
    	Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		
		// 1. 드라이버 로드
		try {
			Class.forName("org.sqlite.JDBC");
		
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		// 2. 커넥션 객체 생성
		try {
		String dbFile = "Wifi-List.db";
        connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile);

        String sql = " select * "
        		+ " from WifiList; ";

        // sql문 실행
        preparedStatement = connection.prepareStatement(sql);
        rs = preparedStatement.executeQuery();
 
        // 5. 결과 수행
        while(rs.next()) {
            String s1 = rs.getString("controlNum");
            String s2 = rs.getString("jachigu");
            String s3 = rs.getString("wifiName");

            System.out.println(s1 + ", " + s2 + ", " + s3);
        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        // 6. 객체 연결 해제(close)
	        try {
	            if (rs != null && !rs.isClosed()) {
	                rs.close();
	            }
	        } catch(SQLException e){
	            e.printStackTrace();
	        }
	
	        try {
	            if (preparedStatement != null && !preparedStatement.isClosed()) {
	                preparedStatement.close();
	            }
	        } catch(SQLException e){
	            e.printStackTrace();
	        }
	
	        try {
	            if (connection != null && !connection.isClosed()) {
	                connection.close();
	            }
	        } catch(SQLException e){
	            e.printStackTrace();
	        }
	    }								
    }
    
	public static void getWifiFromAPI() {
		String key = "7047526c4d747769313030696244484b";
		try {
			Gson gson = new Gson();
			
			URL url = new URL("http://openapi.seoul.go.kr:8088/" + key + "/json/TbPublicWifiInfo/1/5/");
			
// 데이터 개수 확인
			BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
			String result = br.readLine(); // Json 데이터는 한줄짜리네
			
			JsonParser jsonParser = new JsonParser();
			JsonObject jsonObject = (JsonObject)jsonParser.parse(result);
			JsonObject jsonWifiInfo = (JsonObject)jsonObject.get("TbPublicWifiInfo");
			
			Integer totalDataCnt = Integer.valueOf(jsonWifiInfo.get("list_total_count").toString()); // 총 데이터 개수

// 마지막 개수까지 데이터 불러오기		
			int loopCnt = (totalDataCnt / 1000) + 1;
			int cnt = 0;

			while(cnt < loopCnt) {

				int start = 1 + (cnt * 1000);
				int end = 1000 + (cnt * 1000);
				if(end > (int)totalDataCnt) {
					end = (int)totalDataCnt;
				}
				System.out.println(start + "부터의 데이터 작업중");
				URL url2 = new URL("http://openapi.seoul.go.kr:8088/" + key + "/json/TbPublicWifiInfo/" + start + "/" + end + "/");
				
				BufferedReader br2 = new BufferedReader(new InputStreamReader(url2.openStream(), "UTF-8"));
				result = br2.readLine();
				
				JsonParser jsonParser2 = new JsonParser();
				JsonObject jsonObject2 = (JsonObject)jsonParser2.parse(result);
				JsonObject jsonWifiInfo2 = (JsonObject)jsonObject2.get("TbPublicWifiInfo");
				
				// Json데이터를 Array로 받기 
				JsonArray data = (JsonArray)jsonWifiInfo2.get("row");
				
				// 배열을 리스트형태로 다루기
				List<Data> list = new ArrayList<>();
				String slistJson = gson.toJson(data);
				list = gson.fromJson(slistJson, new TypeToken<List<Data>>(){}.getType());
				
				// 받아온 데이터 테이블에 넣기
				insertWifiToTable(list);
				
				System.out.println(end + "까지의 데이터 작업완료");
				cnt++;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void getWifiFromAPIBatch() {		
		// batch를 위해 객체 미리 생성
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		
		String key = "7047526c4d747769313030696244484b";
		
		try {
			Gson gson = new Gson();
			
			URL url = new URL("http://openapi.seoul.go.kr:8088/" + key + "/json/TbPublicWifiInfo/1/5/");
			
// 데이터 개수 확인
			BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
			String result = br.readLine(); // Json 데이터는 한줄짜리네
			
			JsonParser jsonParser = new JsonParser();
			JsonObject jsonObject = (JsonObject)jsonParser.parse(result);
			JsonObject jsonWifiInfo = (JsonObject)jsonObject.get("TbPublicWifiInfo");
			
			Integer totalDataCnt = Integer.valueOf(jsonWifiInfo.get("list_total_count").toString()); // 총 데이터 개수

// 마지막 개수까지 데이터 불러오기		
			int loopCnt = (totalDataCnt / 1000) + 1;
			int cnt = 0;
			int batchCnt = 0;
			
			while(cnt < loopCnt) {

				int start = 1 + (cnt * 1000);
				int end = 1000 + (cnt * 1000);
				if(end > (int)totalDataCnt) {
					end = (int)totalDataCnt;
				}
				System.out.println(start + "부터의 데이터 작업중");
				URL url2 = new URL("http://openapi.seoul.go.kr:8088/" + key + "/json/TbPublicWifiInfo/" + start + "/" + end + "/");
				
				BufferedReader br2 = new BufferedReader(new InputStreamReader(url2.openStream(), "UTF-8"));
				result = br2.readLine();
				
				JsonParser jsonParser2 = new JsonParser();
				JsonObject jsonObject2 = (JsonObject)jsonParser2.parse(result);
				JsonObject jsonWifiInfo2 = (JsonObject)jsonObject2.get("TbPublicWifiInfo");
				
				// Json데이터를 Array로 받기 
				JsonArray data = (JsonArray)jsonWifiInfo2.get("row");
				
				// 배열을 리스트형태로 다루기
				List<Data> list = new ArrayList<>();
				String slistJson = gson.toJson(data);
				list = gson.fromJson(slistJson, new TypeToken<List<Data>>(){}.getType());
				
				// 받아온 데이터 테이블에 넣기
				insertWifiToTableBatch(list, batchCnt, totalDataCnt, connection, preparedStatement, rs);
				
				System.out.println(end + "까지의 데이터 작업완료");
				cnt++;
			}			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
            // 6. 객체 연결 해제(close)
            try {
                if (rs != null && !rs.isClosed()) {
                    rs.close();
                }
            } catch(SQLException e){
                e.printStackTrace();
            }

            try {
                if (preparedStatement != null && !preparedStatement.isClosed()) {
                    preparedStatement.close();
                }
            } catch(SQLException e){
                e.printStackTrace();
            }

            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch(SQLException e){
                e.printStackTrace();
            }
        }
	}
	
	public static void insertWifiToTable(List<Data> list) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		
		// 1. 드라이버 로드
		try {
			Class.forName("org.sqlite.JDBC");
		
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		// 2. 커넥션 객체 생성
		try {
			String dbFile = "Wifi-List.db";
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile);

            String sql = " insert into WifiList "
            		+ " (controlNum, jachigu, wifiName, roadAddress, detailAddress, installLocation, installType, installOrg, serviceType, webType, installYear, inAndOut, wifiProp, LAT, LNT, workDate) "
            		+ " values "
            		+ " (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?); ";


            preparedStatement = connection.prepareStatement(sql);
            
            for(Data item : list) {
		        // 변수 sql의 n번째 ? 위치에 값을 넣겠다는 뜻
		        preparedStatement.setString(1, item.getX_SWIFI_MGR_NO());
		        preparedStatement.setString(2, item.getX_SWIFI_WRDOFC());
		        preparedStatement.setString(3, item.getX_SWIFI_MAIN_NM());
		        preparedStatement.setString(4, item.getX_SWIFI_ADRES1());
		        preparedStatement.setString(5, item.getX_SWIFI_ADRES2());
		        preparedStatement.setString(6, item.getX_SWIFI_INSTL_FLOOR());
		        preparedStatement.setString(7, item.getX_SWIFI_INSTL_TY());
		        preparedStatement.setString(8, item.getX_SWIFI_INSTL_MBY());
		        preparedStatement.setString(9, item.getX_SWIFI_SVC_SE());
		        preparedStatement.setString(10, item.getX_SWIFI_CMCWR());
		        preparedStatement.setString(11, item.getX_SWIFI_CNSTC_YEAR());
		        preparedStatement.setString(12, item.getX_SWIFI_INOUT_DOOR());
		        preparedStatement.setString(13, item.getX_SWIFI_REMARS3());
		        preparedStatement.setString(14, item.getLAT());
		        preparedStatement.setString(15, item.getLNT());
		        preparedStatement.setString(16, item.getWORK_DTTM());
		        
		        preparedStatement.executeUpdate();
            
            }
            // sql문 실행
            // 여기도 preparedStatement로 바뀜
            /*
            int affected = preparedStatement.executeUpdate();

            // 5. 결과 수행
            if(affected > 0) {
                System.out.println("저장 성공. 변경 Data 개수: " + affected);
            } else {
                System.out.println("저장 실패");
            }
            */

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 6. 객체 연결 해제(close)
            try {
                if (rs != null && !rs.isClosed()) {
                    rs.close();
                }
            } catch(SQLException e){
                e.printStackTrace();
            }

            try {
                if (preparedStatement != null && !preparedStatement.isClosed()) {
                    preparedStatement.close();
                }
            } catch(SQLException e){
                e.printStackTrace();
            }

            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch(SQLException e){
                e.printStackTrace();
            }
        }
	}

	public static void insertWifiToTableBatch(List<Data> list, int batchCnt, int totalDataCnt, Connection connection, PreparedStatement preparedStatement, ResultSet rs) {
		
		// 1. 드라이버 로드
		try {
			Class.forName("org.sqlite.JDBC");
		
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		// 2. 커넥션 객체 생성
		try {
			String dbFile = "Wifi-List.db";
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile);

            String sql = " insert into WifiList "
            		+ " (controlNum, jachigu, wifiName, roadAddress, detailAddress, installLocation, installType, installOrg, serviceType, webType, installYear, inAndOut, wifiProp, LAT, LNT, workDate) "
            		+ " values "
            		+ " (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?); ";
            
            preparedStatement = connection.prepareStatement(sql);
            
            for(Data item : list) {
	            // 변수 sql의 n번째 ? 위치에 값을 넣겠다는 뜻
	            preparedStatement.setString(1, item.getX_SWIFI_MGR_NO());
	            preparedStatement.setString(2, item.getX_SWIFI_WRDOFC());
	            preparedStatement.setString(3, item.getX_SWIFI_MAIN_NM());
	            preparedStatement.setString(4, item.getX_SWIFI_ADRES1());
	            preparedStatement.setString(5, item.getX_SWIFI_ADRES2());
	            preparedStatement.setString(6, item.getX_SWIFI_INSTL_FLOOR());
	            preparedStatement.setString(7, item.getX_SWIFI_INSTL_TY());
	            preparedStatement.setString(8, item.getX_SWIFI_INSTL_MBY());
	            preparedStatement.setString(9, item.getX_SWIFI_SVC_SE());
	            preparedStatement.setString(10, item.getX_SWIFI_CMCWR());
	            preparedStatement.setString(11, item.getX_SWIFI_CNSTC_YEAR());
	            preparedStatement.setString(12, item.getX_SWIFI_INOUT_DOOR());
	            preparedStatement.setString(13, item.getX_SWIFI_REMARS3());
	            preparedStatement.setString(14, item.getLAT());
	            preparedStatement.setString(15, item.getLNT());
	            preparedStatement.setString(16, item.getWORK_DTTM());
	            
	            preparedStatement.addBatch(); // 배치에 넣어놓기
	//            preparedStatement.clearParameters(); // 객체 재사용을 위해 클리어
	            batchCnt++;
            }
            preparedStatement.executeBatch();
            /*
            if(batchCnt == 998) {
            	System.out.println(batchCnt);
            }
            // 배치에 올라가 있는거 한번에 쏘기
            if(batchCnt % 5000 == 0) {
            	preparedStatement.executeBatch();
            	System.out.println(batchCnt + "개의 데이터 insert함");
            }
            if(batchCnt == totalDataCnt) {
            	preparedStatement.executeBatch();
            	System.out.println(batchCnt + "개의 데이터 insert함");
            }
            */
            
            // sql문 실행
            // 여기도 preparedStatement로 바뀜
            /*
            int affected = preparedStatement.executeUpdate();

            // 5. 결과 수행
            if(affected > 0) {
                System.out.println("저장 성공. 변경 Data 개수: " + affected);
            } else {
                System.out.println("저장 실패");
            }
            */

        } catch (SQLException e) {
            e.printStackTrace();
        } 
	}

	
	public static void main(String[] args) {
//		Data d = new Data("3", "3", "3", "3", "3", "3", "3", "3", "3", "3", "3", "3", "3", "3", "3", "3");
//		insertWifiToTable(d);
		long beforeTime = System.currentTimeMillis();
		getWifiFromAPIBatch();
		long afterTime = System.currentTimeMillis();
		long secDiffTime = (afterTime - beforeTime) / 1000;
		System.out.println(secDiffTime);
		
//		testSelect();
		
	}
}
