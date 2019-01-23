//AndrewID: yuhaozhu Name: Yuhao Zhu

package hw1;


//unique nutrient objects
public class ProductNutrient {
	String ndbNumber; 
	String nutrientCode;
	String nutrientName;
	String nutrientUom;
	Float quantity;
	
	public ProductNutrient(String ndbNumber, String nutrientCode, String nutrientName, Float quantity, String nutrientUom){
		this.ndbNumber = ndbNumber;
		this.nutrientCode = nutrientCode;
		this.nutrientName = nutrientName;
		this.quantity = quantity;
		this.nutrientUom = nutrientUom;
	}
}	
