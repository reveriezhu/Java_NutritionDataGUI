//AndrewID: yuhaozhu Name: Yuhao Zhu

package hw3;

import hw3.NutriProfiler.PhysicalActivityEnum;
import hw3.Product.ProductNutrient;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;

public class NutriByte extends Application {
	static Model model = new Model(); // made static to make accessible in the controller
	static View view = new View(); // made static to make accessible in the controller
	static Person person = null; // made static to make accessible in the controller

	Controller controller = new Controller(); // all event handlers

	/**
	 * Uncomment the following three lines if you want to try out the full-size data
	 * files
	 */
//	static final String PRODUCT_FILE = "data/Products.csv";
//	static final String NUTRIENT_FILE = "data/Nutrients.csv";
//	static final String SERVING_SIZE_FILE = "data/ServingSize.csv";

	/**
	 * The following constants refer to the data files to be used for this
	 * application
	 */
	static final String PRODUCT_FILE = "data/Nutri2Products.csv";
	static final String NUTRIENT_FILE = "data/Nutri2Nutrients.csv";
	static final String SERVING_SIZE_FILE = "data/Nutri2ServingSize.csv";

	static final String NUTRIBYTE_IMAGE_FILE = "NutriByteLogo.png"; // Refers to the file holding NutriByte logo image

	static final String NUTRIBYTE_PROFILE_PATH = "profiles"; // folder that has profile data files
	static final String PROFILE_CSV_FILE = "profiles/Profile1.csv";

	static final int NUTRIBYTE_SCREEN_WIDTH = 1015;
	static final int NUTRIBYTE_SCREEN_HEIGHT = 675;

	// Realtime Profile Creations:
	static ObjectBinding<Person> profileBinding = new ObjectBinding<Person>() {
		{
			super.bind(view.genderComboBox.valueProperty(), view.ageTextField.textProperty(),
					view.weightTextField.textProperty(), view.heightTextField.textProperty(),
					view.physicalActivityComboBox.valueProperty(), view.ingredientsToWatchTextArea.textProperty());
		}

		@Override
		protected Person computeValue() {
			String gender = "", physicalActivityText = "", ingredients = "";
			Float age = 0f, weight = 0f, height = 0f, physicalActivityLevel = 0f;
			TextField textField = NutriByte.view.ageTextField;
			Person person = null;

			try {
				if (NutriByte.view.genderComboBox.getSelectionModel().getSelectedIndex() >= 0) {
					gender = view.genderComboBox.valueProperty().get();
				}

				// NumberFormatException1
				textField.setStyle("-fx-text-inner-color: black;");
				age = Float.parseFloat(view.ageTextField.getText().trim());
				if (age <= 0 || age > 150) {
					throw new NumberFormatException();
				}

				// NumberFormatException2
				textField = view.weightTextField;
				textField.setStyle("-fx-text-inner-color: black;");
				weight = Float.parseFloat(view.weightTextField.getText().trim());
				if (weight <= 0) {
					throw new NumberFormatException();
				}

				// NumberFormatException3
				textField = view.heightTextField;
				textField.setStyle("-fx-text-inner-color: black;");
				height = Float.parseFloat(view.heightTextField.getText().trim());
				if (height <= 0) {
					throw new NumberFormatException();
				}

				if (view.physicalActivityComboBox.getSelectionModel().getSelectedIndex() >= 0) {
					physicalActivityText = view.physicalActivityComboBox.valueProperty().get();
				}
				for (PhysicalActivityEnum physicalActivity : PhysicalActivityEnum.values()) {
					if (physicalActivityText.equalsIgnoreCase(physicalActivity.getName())) {
						physicalActivityLevel = physicalActivity.getPhysicalActivityLevel();
						break;
					}
				}

				if (view.ingredientsToWatchTextArea.getText() != null
						&& view.ingredientsToWatchTextArea.getText().trim().length() != 0) {
					ingredients = view.ingredientsToWatchTextArea.getText().trim();
				}

				// All 3 numbers passed, the object finally gets created
				if (gender.equalsIgnoreCase("male")) {
					person = new Male(age, weight, height, physicalActivityLevel, ingredients);
				}
				if (gender.equalsIgnoreCase("female")) {
					person = new Female(age, weight, height, physicalActivityLevel, ingredients);
				}

				return person;
			} catch (NumberFormatException e) {
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
		view.root.setCenter(view.setupWelcomeScene());
		Background b = new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY));
		view.root.setBackground(b);
		Scene scene = new Scene(view.root, NUTRIBYTE_SCREEN_WIDTH, NUTRIBYTE_SCREEN_HEIGHT);
		view.root.requestFocus(); // this keeps focus on entire window and allows the textfield-prompt to be
									// visible
		setupBindings();
		stage.setTitle("NutriByte 3.0");
		stage.setScene(scene);
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

	void setupBindings() {

		// 1. MenuItems: 5
		view.newNutriProfileMenuItem.setOnAction(controller.new NewMenuItemHandler());
		view.openNutriProfileMenuItem.setOnAction(controller.new OpenMenuItemHandler());
		view.saveNutriProfileMenuItem.setOnAction(controller.new SaveMenuItemHandler());
		view.closeNutriProfileMenuItem.setOnAction(controller.new CloseMenuItemHandler());
		view.exitNutriProfileMenuItem.setOnAction(event -> Platform.exit());
		view.aboutMenuItem.setOnAction(controller.new AboutMenuItemHandler());

		// 2. Function Buttons: 5
		view.createProfileButton.setOnAction(controller.new RecommendNutrientsButtonHandler());
		view.searchButton.setOnAction(controller.new SearchButtonHandler());
		view.clearButton.setOnAction(controller.new ClearButtonHandler());
		view.addDietButton.setOnAction(controller.new AddDietButtonHandler());
		view.removeDietButton.setOnAction(controller.new RemoveDietButtonHandler());

		// 3. productsComboBox: 1
		view.productsComboBox.setItems(model.searchResultsList);
		view.productsComboBox.getSelectionModel().selectedItemProperty()
				.addListener(controller.new ProductsComboBoxListener());

		// 4. TableColumns: 3 + 3 + 5
		view.recommendedNutrientNameColumn.setCellValueFactory(recommendedNutrientNameCallback);
		view.recommendedNutrientQuantityColumn.setCellValueFactory(recommendedNutrientQuantityCallback);
		view.recommendedNutrientUomColumn.setCellValueFactory(recommendedNutrientUomCallback);

		view.productNutrientNameColumn.setCellValueFactory(productNutrientNameCallback);
		view.productNutrientQuantityColumn.setCellValueFactory(productNutrientQuantityCallback);
		view.productNutrientUomColumn.setCellValueFactory(productNutrientUomCallback);

		view.dietProductNameColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("productName"));
		view.dietServingSizeColumn.setCellValueFactory(new PropertyValueFactory<Product, Float>("servingSize"));
		view.dietServingUomColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("servingUom"));
		view.dietHouseholdSizeColumn.setCellValueFactory(new PropertyValueFactory<Product, Float>("householdSize"));
		view.dietHouseholdUomColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("householdUom"));

		// 5. profileBinding: 1
		profileBinding.addListener((observable, oldValue, newValue) -> {
			if (newValue != null) {
				NutriByte.person = newValue;
				if (newValue.age > 0 && newValue.age <= 150 && newValue.weight > 0 && newValue.height > 0) {
					NutriProfiler.createNutriProfile(newValue);
					view.recommendedNutrientsTableView.setItems(person.recommendedNutrientsList);
				} else {
					view.recommendedNutrientsTableView.setItems(null);
				}
			} else {
				view.recommendedNutrientsTableView.setItems(null);
			}
		});
	}

	// Callbacks for TableColumns: 3 + 3
	Callback<CellDataFeatures<RecommendedNutrient, String>, ObservableValue<String>> recommendedNutrientNameCallback = new Callback<CellDataFeatures<RecommendedNutrient, String>, ObservableValue<String>>() {
		@Override
		public ObservableValue<String> call(CellDataFeatures<RecommendedNutrient, String> arg0) {
			Nutrient nutrient = Model.nutrientsMap.get(arg0.getValue().getNutrientCode());
			return nutrient.nutrientNameProperty();
		}
	};

	Callback<CellDataFeatures<RecommendedNutrient, String>, ObservableValue<String>> recommendedNutrientQuantityCallback = new Callback<CellDataFeatures<RecommendedNutrient, String>, ObservableValue<String>>() {
		@Override
		public ObservableValue<String> call(CellDataFeatures<RecommendedNutrient, String> arg0) {
			return Bindings.format("%.2f", arg0.getValue().NutrientQuantityProperty());
		}
	};

	Callback<CellDataFeatures<RecommendedNutrient, String>, ObservableValue<String>> recommendedNutrientUomCallback = new Callback<CellDataFeatures<RecommendedNutrient, String>, ObservableValue<String>>() {
		@Override
		public ObservableValue<String> call(CellDataFeatures<RecommendedNutrient, String> arg0) {
			Nutrient nutrient = Model.nutrientsMap.get(arg0.getValue().getNutrientCode());
			return nutrient.nutrientUomProperty();
		}
	};

	Callback<CellDataFeatures<ProductNutrient, String>, ObservableValue<String>> productNutrientNameCallback = new Callback<CellDataFeatures<ProductNutrient, String>, ObservableValue<String>>() {
		@Override
		public ObservableValue<String> call(CellDataFeatures<ProductNutrient, String> arg0) {
			Nutrient nutrient = Model.nutrientsMap.get(arg0.getValue().getNutrientCode());
			return nutrient.nutrientNameProperty();
		}
	};

	Callback<CellDataFeatures<ProductNutrient, String>, ObservableValue<String>> productNutrientQuantityCallback = new Callback<CellDataFeatures<ProductNutrient, String>, ObservableValue<String>>() {
		@Override
		public ObservableValue<String> call(CellDataFeatures<ProductNutrient, String> arg0) {
			return Bindings.format("%.2f", arg0.getValue().nutrientQuantityProperty());
		}
	};

	Callback<CellDataFeatures<ProductNutrient, String>, ObservableValue<String>> productNutrientUomCallback = new Callback<CellDataFeatures<ProductNutrient, String>, ObservableValue<String>>() {
		@Override
		public ObservableValue<String> call(CellDataFeatures<ProductNutrient, String> arg0) {
			Nutrient nutrient = Model.nutrientsMap.get(arg0.getValue().getNutrientCode());
			return nutrient.nutrientUomProperty();
		}

	};
}
