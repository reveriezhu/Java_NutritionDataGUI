//Jiahui Cai (jiahuic)
package victoria;


import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;


public class Product {

	private StringProperty ndbNumber = new SimpleStringProperty();
	private StringProperty productName = new SimpleStringProperty();
	private StringProperty manufacturer= new SimpleStringProperty();
	private StringProperty ingredients = new SimpleStringProperty();
	private FloatProperty servingSize = new SimpleFloatProperty();
	private StringProperty servingUom = new SimpleStringProperty();
	private FloatProperty householdSize = new SimpleFloatProperty();
	private StringProperty householdUom = new SimpleStringProperty();
	//key:nutrintCode,value:ProductNutrient object
	private ObservableMap<String,ProductNutrient> productNutrients =FXCollections.observableHashMap();
	/*
	 * a overridden toString function*/
	@Override
	public String toString(){
		return productName.get()+" by "+manufacturer.get();
	}
	/*a default constructor initialize all string properties to empty strings*/
	public Product(){
		setNdbNumber("");
		setProductName("");
		setManufacturer("");
		setIngredients("");
	}
	/* a non-default constructor initialize all string properties*/
	public Product(String ndbNumber, String productName, String Manufacturer, String ingredients){
		setNdbNumber(ndbNumber);
		setProductName(productName);
		setManufacturer(Manufacturer);
		setIngredients(ingredients);
	}
	/*an inner class of Product,
	 * a java bean with nutrientCode and nutrientQuantity properties,
	 * a default constructor setting string property to empty strings,
	 *  and a non-default constructor setting both properties*/
	class ProductNutrient {
		private StringProperty nutrientCode = new SimpleStringProperty();
		private FloatProperty nutrientQuantity = new SimpleFloatProperty();
		/*a default constructor initialize all string properties to empty strings*/
		ProductNutrient(){
			setNutrientCode("");
		}
		/* a non-default constructor initialize all properties*/
		ProductNutrient(String nutrientCode, float nutrientQuantity){
			setNutrientCode(nutrientCode);
			setNutrientQuantity(nutrientQuantity);
		}
		/*getter,setter, and property methods of the private property of ProductNutrient*/
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
		public final StringProperty nutrientCodeProperty() { return nutrientCode; }
		public final FloatProperty nutrientQuantityProperty(){return nutrientQuantity;}

	}

	/*getter,setter, and property methods of the private property of Product*/
	public final String getManufacturer() {
		return manufacturer.get();
	}

	public final void setManufacturer(String manufacturer) {
		this.manufacturer.set(manufacturer);
	}

	public final String getNdbNumber() {
		return ndbNumber.get();
	}

	public final void setNdbNumber(String ndbNumber) {
		this.ndbNumber.set(ndbNumber);
	}

	public final String getProductName() {
		return this.productName.get();
	}

	public final void setProductName(String productName) {
		this.productName.set(productName);
	}

	public final String getIngredients() {
		return ingredients.get();
	}

	public final void setIngredients(String ingredients) {
		this.ingredients.set(ingredients);
	}

	public final Float getServingSize() {
		return servingSize.get();
	}

	public final void setServingSize(Float servingSize) {
		this.servingSize.set(servingSize);
	}

	public final  Float getHouseholdSize() {
		return householdSize.get();
	}

	public  final void setHouseholdSize(Float householdSize) {
		this.householdSize.set(householdSize);
	}

	public  final String getServingUom() {
		return servingUom.get();
	}

	public  final void setServingUom(String servingUom) {
		this.servingUom.set(servingUom);
	}

	public  final String getHouseholdUom() {
		return householdUom.get();
	}

	public  final void setHouseholdUom(String householdUom) {
		this.householdUom.set(householdUom);
	}
	public  final ObservableMap<String,ProductNutrient> getProductNutrients() {
		return productNutrients;
	}
	public  final void setProductNutrients(ObservableMap<String,ProductNutrient> productNutrients) {
		this.productNutrients = productNutrients;
	}

	public  final StringProperty ndbNumberProperty(){return ndbNumber;}
	public final  StringProperty productNameProperty(){return productName;	}
	public  final StringProperty manufacturerProperty(){return manufacturer;}
	public  final StringProperty ingredientsProperty(){return ingredients;}
	public  final FloatProperty servingSizeProperty(){return servingSize;}
	public  final StringProperty servingUomProperty(){return servingUom;}
	public  final FloatProperty householdSizeProperty(){return householdSize;}
	public  final StringProperty householdUomProperty(){return householdUom;}



}
