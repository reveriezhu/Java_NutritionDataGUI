//Jiahui Cai (jiahuic)
package victoria;


import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Nutrient {
	private StringProperty nutrientCode = new SimpleStringProperty();
	private StringProperty nutrientName = new SimpleStringProperty();
	private StringProperty nutrientUom = new SimpleStringProperty();
	/*a default constructor initialize all string properties to empty strings*/
	Nutrient(){

		setNutrientCode("");
		setNutrientName("");
		setNutrientUom("");

	}
	/* a non-default constructor initialize all string properties*/
	Nutrient(String nutreintCode, String nutrientName, String nutrientUom){
		setNutrientCode(nutreintCode);
		setNutrientName(nutrientName);
		setNutrientUom(nutrientUom);
	}
	/*getter,setter, and property methods of the private property of Nutrient*/
	public final String getNutrientUom() {
		return nutrientUom.get();
	}
	public final void setNutrientUom(String nutrientUom) {
		this.nutrientUom.set(nutrientUom);
	}
	public final String getNutrientCode() {
		return nutrientCode.get();
	}
	public final void setNutrientCode(String nutrientCode) {
		this.nutrientCode.set(nutrientCode);
	}
	public final String getNutrientName() {
		return nutrientName.get();
	}
	public final void setNutrientName(String nutrientName) {
		this.nutrientName.set(nutrientName);
	}
	public final StringProperty nutrientCodeProperty() { return nutrientCode; }
	public final StringProperty nutrientNameProperty() { return nutrientName; }
	public final StringProperty nutrientUomProperty() { return nutrientUom; }
}

