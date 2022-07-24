package WifiData;

import java.time.LocalDateTime;

public class SearchData {
	private int id;
	private String LAT; // X좌표
	private String LNT; // Y좌표
	private LocalDateTime searchDateTime; // 조회일자
	private boolean delete_yn; // 삭제 여부
	public SearchData(int id, String lAT, String lNT, LocalDateTime searchDateTime, boolean delete_yn) {
		this.id = id;
		this.LAT = lAT;
		this.LNT = lNT;
		this.searchDateTime = searchDateTime;
		this.delete_yn = delete_yn;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getLAT() {
		return LAT;
	}
	public void setLAT(String lAT) {
		LAT = lAT;
	}
	public String getLNT() {
		return LNT;
	}
	public void setLNT(String lNT) {
		LNT = lNT;
	}
	public LocalDateTime getSearchDateTime() {
		return searchDateTime;
	}
	public void setSearchDateTime(LocalDateTime searchDateTime) {
		this.searchDateTime = searchDateTime;
	}
	public boolean isDelete_yn() {
		return delete_yn;
	}
	public void setDelete_yn(boolean delete_yn) {
		this.delete_yn = delete_yn;
	}
	
}
