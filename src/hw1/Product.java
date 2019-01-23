//AndrewID: yuhaozhu Name: Yuhao Zhu

package hw1;

public class Product {
//	private String ndbNumber;
//	private String productName;
//	private String manufacturer;
//	private String ingredients;
//	private Float servingSize;
//	private String servingUom;
//	private Float householdSize;
//	private String householdUom;
	
	String ndbNumber;
	String productName;
	String manufacturer;
	String ingredients;
	Float servingSize;
	String servingUom;
	Float householdSize;
	String householdUom;
	
//	Product():A non-default constructor to initialize its member 
//	variables: ndbNumber, productName, manufacturer, ingredients
	public Product(String ndbNumber, String productName, String manufacturer, String ingredients) {
		this.ndbNumber = ndbNumber;
		this.productName = productName;
		this.manufacturer = manufacturer;
		this.ingredients = ingredients;
	}
	
	public String getNdbNumber () {
		return ndbNumber;
	}
	
	public String getProductName() {
		return productName;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public String getIngredients() {
		return ingredients;
	}

	public Float getServingSize() {
		return servingSize;
	}

	public String getServingUom() {
		return servingUom;
	}

	public Float getHouseholdSize() {
		return householdSize;
	}

	public String getHouseholdUom() {
		return householdUom;
	}

	public void setNdbNumber(String ndbNumber) {
		this.ndbNumber = ndbNumber;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public void setIngredients(String ingredients) {
		this.ingredients = ingredients;
	}

	public void setServingSize (Float servingSize) {
		this.servingSize = servingSize;
	}
	
	public void setServingUom (String servingUom) {
		this.servingUom = servingUom;
	}
	
	public void setHouseholdSize (Float householdSize) {
		this.householdSize = householdSize;
	}
	
	public void setHouseholdUom (String householdUom) {
		this.householdUom = householdUom;
	}
}