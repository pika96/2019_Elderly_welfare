

public class UserInfo {
	private String userID;
	private String userName;
	private String address;
	private String targetAdd;
	public UserInfo(){};

	public UserInfo(Object getUserInfo) {
	}

	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getTargetAdd() {
		return targetAdd;
	}
	public void setTargetAdd(String targetAdd) {
		this.targetAdd = targetAdd;
	}

	public UserInfo(String userID, String userName, String address, String targetAdd) {
		this.userID = userID;
		this.userName = userName;
		this.address = address;
		this.targetAdd = targetAdd;
	}
}
