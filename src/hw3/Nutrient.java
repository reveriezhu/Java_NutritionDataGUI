//AndrewID: yuhaozhu Name: Yuhao Zhu

package hw3;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Nutrient {
	private StringProperty nutrientCode = new SimpleStringProperty();
	private StringProperty nutrientName = new SimpleStringProperty();
	private StringProperty nutrientUom = new SimpleStringProperty();

	public Nutrient() {
		nutrientCode.set("");
		nutrientName.set("");
		nutrientUom.set("");
	}

	public Nutrient(String nutrientcode, String nutrientname, String nutrientuom) {
		nutrientCode.set(nutrientcode);
		nutrientName.set(nutrientname);
		nutrientUom.set(nutrientuom);
	}

	public final void setNutrientCode(String nutrientCode) {
		this.nutrientCode.set(nutrientCode);
	}

	public final void setNutrientName(String nutrientName) {
		this.nutrientName.set(nutrientName);
	}

	public final void setNutrientUom(String nutrientUom) {
		this.nutrientUom.set(nutrientUom);
	}

	public final String getNutrientCode() {
		return nutrientCode.get();
	}

	public final String getNutrientName() {
		return nutrientName.get();
	}

	public final String getNutrientUom() {
		return nutrientUom.get();
	}

	public StringProperty nutrientCodeProperty() {
		return nutrientCode;
	}

	public StringProperty nutrientNameProperty() {
		return nutrientName;
	}

	public StringProperty nutrientUomProperty() {
		return nutrientUom;
	}
}
