//Jiahui Cai (jiahuic)
package victoria;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;
import victoria.NutriProfiler.PhysicalActivityEnum;


public class NutriByte extends Application{
	static Model model = new Model();  	//made static to make accessible in the controller
	static View view = new View();		//made static to make accessible in the controller
	static Person person;				//made static to make accessible in the controller


	Controller controller = new Controller();	//all event handlers

	/**Uncomment the following three lines if you want to try out the full-size data files */
	//static final String PRODUCT_FILE = "data/Products.csv";
	//static final String NUTRIENT_FILE = "data/Nutrients.csv";
	//static final String SERVING_SIZE_FILE = "data/ServingSize.csv";

	/**The following constants refer to the data files to be used for this application */
	static final String PRODUCT_FILE = "data/Nutri2Products.csv";
	static final String NUTRIENT_FILE = "data/Nutri2Nutrients.csv";
	static final String SERVING_SIZE_FILE = "data/Nutri2ServingSize.csv";

	static final String NUTRIBYTE_IMAGE_FILE = "NutriByteLogo.png"; //Refers to the file holding NutriByte logo image

	static final String NUTRIBYTE_PROFILE_PATH = "profiles";  //folder that has profile data files

	static final int NUTRIBYTE_SCREEN_WIDTH = 1015;
	static final int NUTRIBYTE_SCREEN_HEIGHT = 675;
	static ObjectBinding<Person> personBinding = new ObjectBinding<Person>() {
		{
			super.bind(view.genderComboBox.valueProperty(),view.ageTextField.textProperty(),
					view.weightTextField.textProperty(),view.heightTextField.textProperty(),
					view.physicalActivityComboBox.valueProperty(),view.ingredientsToWatchTextArea.textProperty());
		}

		@Override
		protected Person computeValue() {
			float age=0, weight=0, height=0, physicalActivityLevel=1;
			String ingredientsToWatch ="";
			TextField textField =view.ageTextField;
			try {
				textField.setStyle("-fx-text-inner-color: black;");
				age = Float.parseFloat(textField.getText().trim());
                if(age<0){
                	throw new RuntimeException();
                	//throw new InvalidProfileException("Age must be a positive number");
                }
				textField = view.weightTextField;
				textField.setStyle("-fx-text-inner-color: black;");
				weight =Float.parseFloat(textField.getText().trim());
                if(weight<0){
                	throw new RuntimeException();
                	//throw new InvalidProfileException("Weight must be a positive number");
                }
				textField = view.heightTextField;
				textField.setStyle("-fx-text-inner-color: black;");
				height=Float.parseFloat(textField.getText().trim());
                if(height<0){
                	throw new RuntimeException();
                	//throw new InvalidProfileException("Height must be a positive number");
                }
				String gender = view.genderComboBox.valueProperty().get();
				if (view.physicalActivityComboBox.getSelectionModel().getSelectedIndex() >= 0){
					PhysicalActivityEnum[] physicAct = PhysicalActivityEnum.values();
					for (int i=0;i<physicAct.length;i++){
						if(physicAct[i].getName()==NutriByte.view.physicalActivityComboBox.valueProperty().get()){
							physicalActivityLevel=physicAct[i].getPhysicalActivityLevel();
							break;
						}
					}
				}
				if(view.ingredientsToWatchTextArea.getText()!=null){
					ingredientsToWatch =view.ingredientsToWatchTextArea.getText();
				}
				//System.out.println(gender);
				if(gender!=null){
					switch(gender){
					case "Female": {
						return new Female(age, weight, height, physicalActivityLevel, ingredientsToWatch);
					}
					case "Male":{
						return new Male(age, weight, height, physicalActivityLevel, ingredientsToWatch);
					}
					default:return null;
					}
				}else return null;
			} catch (RuntimeException e) {
				textField.setStyle("-fx-text-inner-color: red;");
				return null;
			}
		}
	};
	@Override
	public void start(Stage stage) throws Exception {
		model.readProducts(PRODUCT_FILE);
		model.readNutrients(NUTRIENT_FILE);
		model.readServingSizes(SERVING_SIZE_FILE);
		view.setupMenus();
		view.setupNutriTrackerGrid();
		view.root.setCenter(view.setupWelcomeScene());//
		Background b = new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY));
		view.root.setBackground(b);
		Scene scene = new Scene (view.root, NUTRIBYTE_SCREEN_WIDTH, NUTRIBYTE_SCREEN_HEIGHT);
		view.root.requestFocus();  //this keeps focus on entire window and allows the textfield-prompt to be visible
		setupBindings();
		stage.setTitle("NutriByte 3.0");
		stage.setScene(scene);
		NutriByte.personBinding.addListener((observable, oldValue, newValue) -> {
			if (newValue != null) {
				System.out.println("new person is parsed");
				NutriByte.person = newValue;
				if (!(NutriByte.person.age<0||NutriByte.person.age>150) &&
						!(NutriByte.person.weight < 0 ) &&
						!(NutriByte.person.height < 0)) {
					NutriByte.view.recommendedNutrientsTableView.setItems(NutriByte.person.recommendedNutrientsList);
				} else NutriByte.view.recommendedNutrientsTableView.setItems(null);
			} else NutriByte.view.recommendedNutrientsTableView.setItems(null);
		});
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
	/*set up binding between menuItem and menu Item controller
	 * populate each column of the table view by calling setCellValuFactury method
	 * binding the profileButton with RecommendNutrientsButtonHandler*/
	void setupBindings() {
		view.newNutriProfileMenuItem.setOnAction(controller.new NewMenuItemHandler());
		view.openNutriProfileMenuItem.setOnAction(controller.new OpenMenuItemHandler());
		view.exitNutriProfileMenuItem.setOnAction(event -> Platform.exit());
		view.saveNutriProfileMenuItem.setOnAction(controller.new SaveMenuItemHandler());
		view.aboutMenuItem.setOnAction(controller.new AboutMenuItemHandler());
		view.searchButton.setOnAction(controller.new SearchButtonHandler());
		view.clearButton.setOnAction(controller.new ClearButtonHandler());
		view.addDietButton.setOnAction(controller.new AddDietButtonHandler());
		view.removeDietButton.setOnAction(controller.new RemoveDietButtonHandler());
		view.closeNutriProfileMenuItem.setOnAction(controller.new CloseMenuItemHandler());
		view.productsComboBox.getSelectionModel().selectedItemProperty().addListener(controller.new ProductComboBoxListener());


		view.recommendedNutrientNameColumn.setCellValueFactory(recommendedNutrientNameCallback);
		view.recommendedNutrientQuantityColumn.setCellValueFactory(recommendedNutrientQuantityCallback);
		view.recommendedNutrientUomColumn.setCellValueFactory(recommendedNutrientUomCallback);

		view.productNutrientNameColumn.setCellValueFactory(productNutrientNameCallback);
		view.productNutrientQuantityColumn.setCellValueFactory(productNutrientQuantityCallback);
		view.productNutrientUomColumn.setCellValueFactory(productNutrientUomCallback);

		view.dietProductNameColumn.setCellValueFactory(dietProductNameCallback);
		view.dietServingSizeColumn.setCellValueFactory(dietServingSizeCallback);
		view.dietServingUomColumn.setCellValueFactory(dietServingUomCallback);
		view.dietHouseholdSizeColumn.setCellValueFactory(dietHouseholdSizeCallback);
		view.dietHouseholdUomColumn.setCellValueFactory(dietHouseholdUomCallback);

		view.createProfileButton.setOnAction(controller.new RecommendNutrientsButtonHandler());

	}


	/**
	 * recommendedNutrientsTableView
	 * /
	// custom property value factory
	// creating an object of Callback<CellDataFeatres,return value>,implementing call method in the anonymous member class
	// with CellDataFeatures<RecommendedNutrient, String> bean:RecommendedNutrient, data type in cell :String
	// return value type:ObservableValue<String>
	/*custom call method for the column NutrientName in the table view*/
	Callback<CellDataFeatures<RecommendedNutrient, String>, ObservableValue<String>> recommendedNutrientNameCallback = new Callback<CellDataFeatures<RecommendedNutrient, String>, ObservableValue<String>>() {
		@Override
		public ObservableValue<String> call(CellDataFeatures<RecommendedNutrient, String> arg0) {
			Nutrient nutrient = Model.nutrientsMap.get(arg0.getValue().getNutrientCode());
			return nutrient.nutrientNameProperty();
		}
	};
	/*custom call method for the column NutrientQuantity in the table view*/
	Callback<CellDataFeatures<RecommendedNutrient, String>, ObservableValue<String>> recommendedNutrientQuantityCallback = new Callback<CellDataFeatures<RecommendedNutrient, String>, ObservableValue<String>>() {
		@Override
		public ObservableValue<String> call(CellDataFeatures<RecommendedNutrient, String> arg0) {
			return Bindings.format("%.2f",arg0.getValue().nutrientQuantityProperty());
		}
	};
	/*custom call method for the column NutrientUom in the table view*/
	Callback<CellDataFeatures<RecommendedNutrient, String>, ObservableValue<String>> recommendedNutrientUomCallback = new Callback<CellDataFeatures<RecommendedNutrient, String>, ObservableValue<String>>() {
		@Override
		public ObservableValue<String> call(CellDataFeatures<RecommendedNutrient, String> arg0) {
			Nutrient nutrient = Model.nutrientsMap.get(arg0.getValue().getNutrientCode());
			return nutrient.nutrientUomProperty();
		}
	};
	/**
	 * productNutrientsTableView
	 * /
	// custom property value factory
	// creating an object of Callback<CellDataFeatres,return value>,implementing call method in the anonymous member class
	// with CellDataFeatures<RecommendedNutrient, String> bean:Product.ProductNutrient, data type in cell :String
	// return value type:ObservableValue<String>
	/*custom call method for the column ProductNutrients in the table view*/
	Callback<CellDataFeatures<Product.ProductNutrient, String>, ObservableValue<String>> productNutrientNameCallback = new Callback<CellDataFeatures<Product.ProductNutrient, String>, ObservableValue<String>>() {
		@Override
		public ObservableValue<String> call(CellDataFeatures<Product.ProductNutrient, String> arg0) {
			Nutrient nutrient = Model.nutrientsMap.get(arg0.getValue().getNutrientCode());
			return nutrient.nutrientNameProperty();
		}
	};
	/*custom call method for the column NutrientUom in the table view*/
	Callback<CellDataFeatures<Product.ProductNutrient, String>, ObservableValue<String>> productNutrientUomCallback = new Callback<CellDataFeatures<Product.ProductNutrient, String>, ObservableValue<String>>() {
		@Override
		public ObservableValue<String> call(CellDataFeatures<Product.ProductNutrient, String> arg0) {
			Nutrient nutrient = Model.nutrientsMap.get(arg0.getValue().getNutrientCode());
			return nutrient.nutrientUomProperty();
		}
	};
	/*custom call method for the column Quantity in the table view*/
	Callback<CellDataFeatures<Product.ProductNutrient, String>, ObservableValue<String>> productNutrientQuantityCallback = new Callback<CellDataFeatures<Product.ProductNutrient, String>, ObservableValue<String>>() {
		@Override
		public ObservableValue<String> call(CellDataFeatures<Product.ProductNutrient, String> arg0) {
			return Bindings.format("%4.2f",arg0.getValue().getNutrientQuantity());
		}
	};
	/**
	 * dietProductsView
	 * /
	// custom property value factory
	// creating an object of Callback<CellDataFeatres,return value>,implementing call method in the anonymous member class
	// with CellDataFeatures<Product, String/Float> bean:Product, data type in cell :String/Float
	// return value type:ObservableValue<String/Float>
	 *
	 * TableView<Product> dietProductsTableView = new TableView<>();
	TableColumn<Product, String> dietProductNameColumn = new TableColumn<>("Product");
	TableColumn<Product, Float> dietServingSizeColumn = new TableColumn<>("Serving Size");
	TableColumn<Product, String> dietServingUomColumn = new TableColumn<>("UOM");
	TableColumn<Product, Float> dietHouseholdSizeColumn = new TableColumn<>("Household Size");
	TableColumn<Product, String> dietHouseholdUomColumn = new TableColumn<>("UOM");

	 *
	 */
	/*custom call method for the column dietProductName in the table view*/
	Callback<CellDataFeatures<Product, String>, ObservableValue<String>> dietProductNameCallback = new Callback<CellDataFeatures<Product, String>, ObservableValue<String>>() {
		@Override
		public ObservableValue<String> call(CellDataFeatures<Product, String> arg0) {
			return arg0.getValue().productNameProperty();
		}
	};
	/*custom call method for the column dietServingSize in the table view*/
	Callback<CellDataFeatures<Product, Float>, ObservableValue<Float>> dietServingSizeCallback = new Callback<CellDataFeatures<Product, Float>, ObservableValue<Float>>() {
		@Override
		public ObservableValue<Float> call(CellDataFeatures<Product, Float> arg0) {
			Product p = arg0.getValue();
			return (p.servingSizeProperty()).asObject();
		}
	};

	/*custom call method for the column Quantity in the table view*/
	Callback<CellDataFeatures<Product, String>, ObservableValue<String>> dietServingUomCallback = new Callback<CellDataFeatures<Product, String>, ObservableValue<String>>() {
		@Override
		public ObservableValue<String> call(CellDataFeatures<Product, String> arg0) {
			return arg0.getValue().servingUomProperty();
		}
	};

	/*custom call method for the column Quantity in the table view*/
	Callback<CellDataFeatures<Product, Float>, ObservableValue<Float>> dietHouseholdSizeCallback = new Callback<CellDataFeatures<Product, Float>, ObservableValue<Float>>() {
		@Override
		public ObservableValue<Float> call(CellDataFeatures<Product, Float> arg0) {
			Product p = arg0.getValue();
			return (p.householdSizeProperty()).asObject();
		}
	};
	/*custom call method for the column Quantity in the table view*/
	Callback<CellDataFeatures<Product, String>, ObservableValue<String>> dietHouseholdUomCallback = new Callback<CellDataFeatures<Product, String>, ObservableValue<String>>() {
		@Override
		public ObservableValue<String> call(CellDataFeatures<Product, String> arg0) {
			return arg0.getValue().householdUomProperty();
		}
	};

}
