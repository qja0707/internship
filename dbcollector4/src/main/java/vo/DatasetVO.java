package vo;

public class DatasetVO {

	private String CategoryType;
	private String pcServiceYn;
	private String person;
	private String mobileServiceYn;
	private String objectName;
	private String categoryName;
	private long objectId;
	private long categoryId;
	public String getCategoryType() {
		return CategoryType;
	}
	public void setCategoryType(String categoryType) {
		CategoryType = categoryType;
	}
	public String getPcServiceYn() {
		return pcServiceYn;
	}
	public void setPcServiceYn(String pcServiceYn) {
		this.pcServiceYn = pcServiceYn;
	}
	public String getPerson() {
		return person;
	}
	public void setPerson(String person) {
		this.person = person;
	}
	public String getMobileServiceYn() {
		return mobileServiceYn;
	}
	public void setMobileServiceYn(String mobileServiceYn) {
		this.mobileServiceYn = mobileServiceYn;
	}
	public String getObjectName() {
		return objectName;
	}
	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public long getObjectId() {
		return objectId;
	}
	public void setObjectId(long objectId) {
		this.objectId = objectId;
	}
	public long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(long categoryId) {
		this.categoryId = categoryId;
	}
	
	
}
