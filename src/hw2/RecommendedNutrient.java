//AndrewID: yuhaozhu Name: Yuhao Zhu

package hw2;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class RecommendedNutrient {
	private StringProperty nutrientCode = new SimpleStringProperty();
	private FloatProperty nutrientQuantity = new SimpleFloatProperty();

	public RecommendedNutrient() {
		nutrientCode.set("");
	}

	public RecommendedNutrient(String nutrientCode, Float nutrientQuantity) {
		this.nutrientCode.set(nutrientCode);
		this.nutrientQuantity.set(nutrientQuantity);
	}

	public final void setNutrientCode(String nutrientCode) {
		this.nutrientCode.set(nutrientCode);
	}

	public final void setNutrientQuantity(Float nutrientQuantity) {
		this.nutrientQuantity.set(nutrientQuantity);
	}

	public final String getNutrientCode() {
		return nutrientCode.get();
	}

	public final Float getNutrientQuantity() {
		return nutrientQuantity.get();
	}

	public StringProperty NutrientCodeProperty() {
		return nutrientCode;
	}

	public FloatProperty NutrientQuantityProperty() {
		return nutrientQuantity;
	}

}
