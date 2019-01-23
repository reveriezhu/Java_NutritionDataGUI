//AndrewID: yuhaozhu Name: Yuhao Zhu

package hw3;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

public class Product {
	private StringProperty ndbNumber = new SimpleStringProperty();
	private StringProperty productName = new SimpleStringProperty();
	private StringProperty manufacturer = new SimpleStringProperty();
	private StringProperty ingredients = new SimpleStringProperty();
	private FloatProperty servingSize = new SimpleFloatProperty();
	private StringProperty servingUom = new SimpleStringProperty();
	private FloatProperty householdSize = new SimpleFloatProperty();
	private StringProperty householdUom = new SimpleStringProperty();
	private ObservableMap<String, ProductNutrient> productNutrients = FXCollections.observableHashMap();

	public Product() {
		ndbNumber.set("");
		productName.set("");
		manufacturer.set("");
		ingredients.set("");
		servingUom.set("");
		householdUom.set("");
	}

	public Product(String ndbNumber, String productName, String manufacturer, String ingredients) {
		this.ndbNumber.set(ndbNumber);
		this.productName.set(productName);
		this.manufacturer.set(manufacturer);
		this.ingredients.set(ingredients);

	}

	@Override
	public String toString() {
		return getProductName();
	}

	public class ProductNutrient {
		private StringProperty nutrientCode = new SimpleStringProperty();
		private FloatProperty nutrientQuantity = new SimpleFloatProperty();

		public ProductNutrient() {
			nutrientCode.set("");
		}

		public ProductNutrient(String nutrientCode, Float nutrientQuantity) {
			this.nutrientCode.set(nutrientCode);
			this.nutrientQuantity.set(nutrientQuantity);
		}

		public final void setNutrientCode(String nutrientCode) {
			this.nutrientCode.set(nutrientCode);
		}

		public final void setNutrientCode(Float nutrientQuantity) {
			this.nutrientQuantity.set(nutrientQuantity);
		}

		public final String getNutrientCode() {
			return nutrientCode.get();
		}

		public final Float getNutrientQuantity() {
			return nutrientQuantity.get();
		}

		public StringProperty nutrientCodeProperty() {
			return nutrientCode;
		}

		public FloatProperty nutrientQuantityProperty() {
			return nutrientQuantity;
		}
	}

	public final String getNdbNumber() {
		return ndbNumber.get();
	}

	public final String getProductName() {
		return productName.get();
	}

	public final String getManufacturer() {
		return manufacturer.get();
	}

	public final String getIngredients() {
		return ingredients.get();
	}

	public final Float getServingSize() {
		return servingSize.get();
	}

	public final String getServingUom() {
		return servingUom.get();
	}

	public final Float getHouseholdSize() {
		return householdSize.get();
	}

	public final String getHouseholdUom() {
		return householdUom.get();
	}

	public final ObservableMap<String, ProductNutrient> getProductNutrients() {
		return productNutrients;
	}

	public final void setNdbNumber(String ndbNumber) {
		this.ndbNumber.set(ndbNumber);
	}

	public final void setProductName(String productName) {
		this.productName.set(productName);
	}

	public final void setManufacturer(String manufacturer) {
		this.manufacturer.set(manufacturer);
	}

	public final void setIngredients(String ingredients) {
		this.ingredients.set(ingredients);
	}

	public final void setServingSize(Float servingSize) {
		this.servingSize.set(servingSize);
	}

	public final void setServingUom(String servingUom) {
		this.servingUom.set(servingUom);
	}

	public final void setHouseholdSize(Float householdSize) {
		this.householdSize.set(householdSize);
	}

	public final void setHouseholdUom(String householdUom) {
		this.householdUom.set(householdUom);
	}

	public final void setProductNutrients(ObservableMap<String, ProductNutrient> productNutrients) {
		this.productNutrients = productNutrients;
	}

	public StringProperty ndbNumberProperty() {
		return ndbNumber;
	}

	public StringProperty productNameProperty() {
		return productName;
	}

	public StringProperty manufacturerProperty() {
		return manufacturer;
	}

	public StringProperty ingredientsProperty() {
		return ingredients;
	}

	public FloatProperty servingSizeProperty() {
		return servingSize;
	}

	public StringProperty servingUomProperty() {
		return servingUom;
	}

	public FloatProperty householdSizeProperty() {
		return householdSize;
	}

	public StringProperty householdUomProperty() {
		return householdUom;
	}

}