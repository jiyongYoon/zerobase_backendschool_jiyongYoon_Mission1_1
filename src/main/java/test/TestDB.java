package test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

public class TestDB {
	
	
	public static void main (String[] args) {
		String key = "7047526c4d747769313030696244484b";
		try {
			URL url = new URL("http://openapi.seoul.go.kr:8088/" + key + "/json/TbPublicWifiInfo/1/20/");
			
			BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
			String result = br.readLine(); // Json 데이터는 한줄짜리네
			
			Gson gson = new Gson();
		
			JsonParser jsonParser = new JsonParser();
			JsonObject jsonObject = (JsonObject)jsonParser.parse(result);
			JsonObject jsonWifiInfo = (JsonObject)jsonObject.get("TbPublicWifiInfo");
			
			Object totalDataCnt = jsonWifiInfo.get("list_total_count"); 
			System.out.println("총 데이터 개수는 " + totalDataCnt + "개 입니다.");
			JsonObject Result = (JsonObject)jsonWifiInfo.get("RESULT"); 
//			System.out.println(Result);
//			System.out.println(Result.get("CODE"));
//			System.out.println(Result.get("MESSAGE"));
			
			// Json데이터를 Array로 받기 
			JsonArray data = (JsonArray)jsonWifiInfo.get("row");
			
			// 배열을 리스트형태로 다루기
			List<Data> list = new ArrayList<>();
			String slistJson = gson.toJson(data);
			list = gson.fromJson(slistJson, new TypeToken<List<Data>>(){}.getType());
			
			for(Data item:list) {
				System.out.println(item);
			}
			
			// 배열을 하나씩 클래스로 받기			
			for(int i = 0; i < data.size(); i++) {
				
				JsonObject dataElement = (JsonObject)data.get(i);
				
				String s = gson.toJson(dataElement);			
				Data d = gson.fromJson(s, Data.class);
				System.out.println(d.toString());
			
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}