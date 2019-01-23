//Jiahui Cai (jiahuic)
package victoria;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class RecommendedNutrient {
	private StringProperty nutrientCode = new SimpleStringProperty();
	private FloatProperty nutrientQuantity = new SimpleFloatProperty();
	/*a default constructor initialize all string properties to empty strings*/
	RecommendedNutrient(){
		setNutrientCode("");
	}
	/* a non-default constructor initialize all properties*/
	RecommendedNutrient(String nutrientCode, float nutrientQuantity){
		setNutrientCode(nutrientCode);
		setNutrientQuantity(nutrientQuantity);
	}
	/*getter,setter, and property methods of the private property of RecommendedNutrient*/
	public final String getNutrientCode() {
		return nutrientCode.get();
	}
	public final void setNutrientCode(String nutrientCode) {
		this.nutrientCode.set(nutrientCode);
	}
	public final Float getNutrientQuantity() {
		return nutrientQuantity.get();
	}
	public final void setNutrientQuantity(float nutrientQuantity) {
		this.nutrientQuantity.set(nutrientQuantity);
	}
	public final StringProperty nutrientCodeProperty(){
        return nutrientCode;
	}
	public final FloatProperty nutrientQuantityProperty(){
        return nutrientQuantity;
	}


}
