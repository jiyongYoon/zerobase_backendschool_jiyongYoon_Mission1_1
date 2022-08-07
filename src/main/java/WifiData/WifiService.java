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
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

public class WifiService {
	public int beforeTotalWifiInfoCnt;
	public final String HistoryDbFile = "Load-Histroy.db";
	public final String HistoryDbTable = "History";
	
	public final String ListDbFile = "Wifi-List.db";
	public final String ListDbTable = "WifiList";
	
	public String getAbsolutePath() {
		return "C:\\Users\\Yoon jiyong\\Desktop\\develop\\Zerobase_Backend_School\\db_test\\eclipse-workspace\\Mission1_1\\";
	}
	
	
	// Wifi-History관련
	public int readHistoryDataCnt() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		int a = 0;
		
		// 1. 드라이버 로드
		try {
			Class.forName("org.sqlite.JDBC");
		
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		// 2. 커넥션 객체 생성
		try {
	        connection = DriverManager.getConnection("jdbc:sqlite:" + getAbsolutePath() + HistoryDbFile);
	
	        String sql = " select count(*) "
	        		+ " from " + HistoryDbTable + " ; ";

	        // sql문 실행
	        preparedStatement = connection.prepareStatement(sql);
	        rs = preparedStatement.executeQuery();
	 
	        // 5. 결과 수행
	        while(rs.next()) {
	            a = rs.getInt("count(*)");
	
	            System.out.println(a);
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
		return a;
	}
	
	// 조회이력 list로 출력하는 메서드
	public ArrayList<SearchData> readHistoryData() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		
		int num = 0;
		String lat = "";
		String lnt = "";
		String loadDate = "";
		boolean delete = false;
		
		ArrayList<SearchData> list = new ArrayList<>();
		
		// 1. 드라이버 로드
		try {
			Class.forName("org.sqlite.JDBC");
		
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		// 2. 커넥션 객체 생성
		try {
	        connection = DriverManager.getConnection("jdbc:sqlite:" + getAbsolutePath() + HistoryDbFile);
	
	        String sql = " select * "
	        		+ " from " + HistoryDbTable + " ; ";
	        
	        // sql문 실행
	        preparedStatement = connection.prepareStatement(sql);
	        rs = preparedStatement.executeQuery();
	 
	        // 5. 결과 수행
	        while(rs.next()) {
	        	num = rs.getInt("num");
	    		lat = rs.getString("LAT");
	    		lnt = rs.getString("LNT");
	    		loadDate = rs.getString("loadDate");
	    		delete = rs.getBoolean("note");
	    		SearchData sd = new SearchData(num, lat, lnt, loadDate, delete);
	    		list.add(sd);
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
		return list;
	}

	
	public void insertHistory(String inputLat, String inputLnt, String getTime) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		
		SearchData searchData = new SearchData(readHistoryDataCnt() + 1, inputLat, inputLnt, getTime, false);
		
		// 1. 드라이버 로드
		try {
			Class.forName("org.sqlite.JDBC");
		
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		// 2. 커넥션 객체 생성
		try {
	        connection = DriverManager.getConnection("jdbc:sqlite:" + getAbsolutePath() + HistoryDbFile);
	
	        String sql = " insert into " + HistoryDbTable + 
	        		" (num, LAT, LNT, loadDate, note) " +
	        		" values " + 
	        		" (?, ?, ?, ?, ?); ";
            
            preparedStatement = connection.prepareStatement(sql);
            
            preparedStatement.setInt(1, searchData.getId());
            preparedStatement.setString(2, searchData.getLAT());
            preparedStatement.setString(3, searchData.getLNT());
            preparedStatement.setString(4, searchData.getSearchDateTime());
            preparedStatement.setBoolean(5, false);

	        // sql문 실행
            int affected = preparedStatement.executeUpdate();

            // 5. 결과 수행
            if(affected > 0) {
                System.out.println("입력 성공. 입력 Data 개수: " + affected);
            } else {
                System.out.println("입력 실패");
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
	
	public void deleteHistory(String loadDate) {
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
	        connection = DriverManager.getConnection("jdbc:sqlite:" + getAbsolutePath() + HistoryDbFile);

            String sql = " delete " +
                    " from " + HistoryDbTable +
                    " where loadDate = ? ; ";

            preparedStatement = connection.prepareStatement(sql);
// 수정필요   
            preparedStatement.setString(1, loadDate);

            // sql문 실행
            // 여기도 preparedStatement로 바뀜
            int affected = preparedStatement.executeUpdate();

            // 5. 결과 수행
            if(affected > 0) {
                System.out.println("삭제 성공. 삭제 Data 개수: " + affected);
            } else {
                System.out.println("삭제 실패");
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
	
	// Wifi-List관련
	// List 데이터 개수 리턴
	public int readDataCnt() {
    	Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		int a = 0;
		
		// 1. 드라이버 로드
		try {
			Class.forName("org.sqlite.JDBC");
		
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		// 2. 커넥션 객체 생성
		try {
	        connection = DriverManager.getConnection("jdbc:sqlite:" + getAbsolutePath() + ListDbFile);
	
	        String sql = " select count(*) "
	        		+ " from " + ListDbTable + " ; ";

	        // sql문 실행
	        preparedStatement = connection.prepareStatement(sql);
	        rs = preparedStatement.executeQuery();
	 
	        // 5. 결과 수행
	        while(rs.next()) {
	            a = rs.getInt("count(*)");
	
	            System.out.println(a);
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
		return a;
    }
	
	// 테이블 지우기(이전과 데이터가 다르면 테이블 지우고 다시 불러오기)
	public void deleteWifiInfo() {
		
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
	        connection = DriverManager.getConnection("jdbc:sqlite:" + getAbsolutePath() + ListDbFile);

            String sql = " delete " +
                    " from " + ListDbTable + " ; ";

            preparedStatement = connection.prepareStatement(sql);

            // sql문 실행
            // 여기도 preparedStatement로 바뀜
            int affected = preparedStatement.executeUpdate();

            // 5. 결과 수행
            if(affected > 0) {
                System.out.println("삭제 성공. 삭제 Data 개수: " + affected);
            } else {
                System.out.println("삭제 실패");
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
	
	/*
	public static void getWifiInfoFromAPI() {
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
				insertWifiInfoToTable(list);
				
				System.out.println(end + "까지의 데이터 작업완료");
				cnt++;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	*/
	
	// Batch를 통해 와이파이정보 가져와서 Table에 넣기
	public int getWifiInfoFromAPIBatch() {		
		// batch를 위해 객체 미리 생성
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		
		String key = "7047526c4d747769313030696244484b";
		
		try {
			deleteWifiInfo();
			
			Gson gson = new Gson();
			
			URL url = new URL("http://openapi.seoul.go.kr:8088/" + key + "/json/TbPublicWifiInfo/1/5/");
			
// 데이터 개수 확인
			BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
			String result = br.readLine(); // Json 데이터는 한줄짜리네
			
			JsonParser jsonParser = new JsonParser();
			JsonObject jsonObject = (JsonObject)jsonParser.parse(result);
			JsonObject jsonWifiInfo = (JsonObject)jsonObject.get("TbPublicWifiInfo");
			
			Integer totalDataCnt = Integer.valueOf(jsonWifiInfo.get("list_total_count").toString()); // 총 데이터 개수
			beforeTotalWifiInfoCnt = totalDataCnt;
			
// 마지막 개수까지 데이터 불러오기		
			int loopCnt = (totalDataCnt / 1000) + 1;
			int cnt = 0;
			int batchCnt = 0;
			System.out.println("불러오기 시작");
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
				insertWifiInfoToTableBatch(list, batchCnt, totalDataCnt);
				
				System.out.println(end + "까지의 데이터 작업완료");
				cnt++;
			}	
			
			System.out.println("불러오기 성공. 불러온 Data 개수: " + totalDataCnt);
			
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
		return beforeTotalWifiInfoCnt;
	}
	
	/*
	public static void insertWifiInfoToTable(List<Data> list) {
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
            
            int affected = preparedStatement.executeUpdate();

            // 5. 결과 수행
            if(affected > 0) {
                System.out.println("저장 성공. 변경 Data 개수: " + affected);
            } else {
                System.out.println("저장 실패");
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
	*/

	// insert. getWifiInfoFromAPIBatch 메서드 내부에서 활용
	public void insertWifiInfoToTableBatch(List<Data> list, int batchCnt, int totalDataCnt) {
		
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
            connection = DriverManager.getConnection("jdbc:sqlite:" + getAbsolutePath() + ListDbFile);

            String sql = " insert into " + ListDbTable
            		+ " (controlNum, jachigu, wifiName, roadAddress, detailAddress, installLocation, installType, installOrg, serviceType, webType, installYear, inAndOut, wifiProp, LAT, LNT, workDate) "
            		+ " values "
            		+ " (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?); ";
            connection.setAutoCommit(false);
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
	            preparedStatement.setString(14, item.getLNT());
	            preparedStatement.setString(15, item.getLAT());
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

	public ArrayList<Data> getLocation(String Lat, String Lnt) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		
		ArrayList<Data> list = new ArrayList<>();
		String distance = "";
		String controlNum = "";
		String jachigu = "";
		String wifiName = "";
		String roadAddress = "";
		String detailAddress = "";
		String installLocation = "";
		String installType = "";
		String installOrg = "";
		String serviceType = "";
		String webType = "";
		String installYear = "";
		String inAndOut = "";
		String wifiProp = "";
		String LAT = "";
		String LNT = "";
		String workDate = "";
		
		// 1. 드라이버 로드
		try {
			Class.forName("org.sqlite.JDBC");
		
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		// 2. 커넥션 객체 생성
		try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + getAbsolutePath() + ListDbFile);

            String sql = " SELECT "
            		+ "  ROUND(( "
            		+ "   6371 * acos ( "
            		+ "   cos ( radians( " + Lat + ") ) "
            		+ "   * cos( radians( wl.LAT  ) ) "
            		+ "   * cos( radians( wl.LNT ) - radians(" + Lnt + ") ) "
            		+ "   + sin ( radians(	" + Lat + ") ) "
            		+ "   * sin( radians( wl.LAT) ) "
            		+ "  )), 4)AS getDistance, * "
            		+ "	FROM WifiList wl "
            		+ " ORDER BY getDistance asc limit 20;";
            
            preparedStatement = connection.prepareStatement(sql);
            rs = preparedStatement.executeQuery();
       	 
	        // 5. 결과 수행
	        while(rs.next()) {
	        	
	        	distance = rs.getString("getDistance");        	
	            controlNum = rs.getString("controlNum");
	    		jachigu = rs.getString("jachigu");
	    		wifiName = rs.getString("wifiName");
	    		roadAddress = rs.getString("roadAddress");
	    		detailAddress = rs.getString("detailAddress");
	    		installLocation = rs.getString("installLocation");
	    		installType = rs.getString("installType");
	    		installOrg = rs.getString("installOrg");
	    		serviceType = rs.getString("serviceType");
	    		webType = rs.getString("webType");
	    		installYear = rs.getString("installYear");
	    		inAndOut = rs.getString("inAndOut");
	    		wifiProp = rs.getString("wifiProp");
	    		LAT = rs.getString("LAT");
	    		LNT = rs.getString("LNT");
	    		workDate = rs.getString("workDate");
	            
	        	Data d = new Data(distance, controlNum, jachigu, wifiName, roadAddress, detailAddress, installLocation, installType, installOrg, serviceType, webType, installYear, inAndOut, wifiProp, LAT, LNT, workDate);
	    		list.add(d);
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
		return list;
	}
	
	public static void main(String[] args) {
		
		WifiService wifiService = new WifiService();

//		long beforeTime = System.currentTimeMillis();
//		getWifiInfoFromAPIBatch();
//		long afterTime = System.currentTimeMillis();
//		long secDiffTime = (afterTime - beforeTime) / 1000;
//		System.out.println(secDiffTime);
		
//		wifiService.readDataCnt();
//		wifiService.getWifiInfoFromAPIBatch();
//		wifiService.readDataCnt();
		
//		wifiService.deleteHistory("Wed Jul 27 23:32:59 KST 2022");
		
		
	}
}
