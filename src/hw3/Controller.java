//AndrewID: yuhaozhu Name: Yuhao Zhu

package hw3;

import java.io.File;

import hw3.NutriProfiler.PhysicalActivityEnum;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class Controller {

	// I. MenuItems: 5
	class NewMenuItemHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {

			// 0. Initiate the Main BorderPane and functionalities
			NutriByte.view.root.setCenter(NutriByte.view.nutriTrackerPane);
			NutriByte.view.initializePrompts();

			// 1. TextFields + TextAreas: 6 + 2
			NutriByte.view.productSearchTextField.clear();
			NutriByte.view.nutrientSearchTextField.clear();
			NutriByte.view.ingredientSearchTextField.clear();
			NutriByte.view.dietServingSizeTextField.clear();
			NutriByte.view.dietHouseholdSizeTextField.clear();
			NutriByte.view.ingredientSearchTextField.clear();
			NutriByte.view.ingredientsToWatchTextArea.clear();
			NutriByte.view.productIngredientsTextArea.clear();

			// 2. ComboBoxes + TableViews + NutriChart: 3 + 3 + 1
			NutriByte.view.genderComboBox.setItems(null);
			NutriByte.view.physicalActivityComboBox.setItems(null);
			NutriByte.view.productsComboBox.setItems(null);
			NutriByte.view.dietProductsTableView.setItems(null);
			NutriByte.view.productNutrientsTableView.setItems(null);
			NutriByte.view.recommendedNutrientsTableView.setItems(null);
			NutriByte.view.nutriChart.clearChart();

			// 3. Labels: 6
			NutriByte.view.servingSizeLabel.setText("0.00");
			NutriByte.view.householdSizeLabel.setText("0.00");
			NutriByte.view.servingUom.setText("");
			NutriByte.view.dietServingUomLabel.setText("");
			NutriByte.view.householdServingUom.setText("");
			NutriByte.view.dietHouseholdUomLabel.setText("");
		}
	}

	class OpenMenuItemHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {

			Stage mainStage = new Stage();
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Select file");

			fileChooser.getExtensionFilters().addAll(new ExtensionFilter("CSV Files", "*.csv"),
					new ExtensionFilter("XML Files", "*.xml"));
			File file = null;

			// Show open file dialog - CSVFiler.readFile
			if ((file = fileChooser.showOpenDialog(mainStage)) != null) {
				if (NutriByte.model.readProfile(file.getAbsolutePath())) {

					// 1. Initiate the Main BorderPane and functionalities
					NutriByte.view.root.setCenter(NutriByte.view.nutriTrackerPane);
					NutriByte.view.initializePrompts();

					// 2. Store the read person into a tempPerson
					Person tempPerson = null;
					if (NutriByte.person instanceof Female) {
						tempPerson = new Female(NutriByte.person.age, NutriByte.person.weight, NutriByte.person.height,
								NutriByte.person.physicalActivityLevel, NutriByte.person.ingredientsToWatch);
					} else {
						tempPerson = new Male(NutriByte.person.age, NutriByte.person.weight, NutriByte.person.height,
								NutriByte.person.physicalActivityLevel, NutriByte.person.ingredientsToWatch);
					}
					tempPerson.nutriConstantsTable = NutriByte.person.nutriConstantsTable;
					tempPerson.ageGroup = NutriByte.person.ageGroup;
					tempPerson.recommendedNutrientsList = NutriByte.person.recommendedNutrientsList;
					tempPerson.dietProductsList = NutriByte.person.dietProductsList;
					tempPerson.dietNutrientsMap = NutriByte.person.dietNutrientsMap;

					for (int i = 0; i < PhysicalActivityEnum.values().length; i++) {
						if (tempPerson.physicalActivityLevel <= PhysicalActivityEnum.values()[i]
								.getPhysicalActivityLevel()) {
							NutriByte.view.physicalActivityComboBox.getSelectionModel().select(i);
							break;
						}
					}

					if (tempPerson instanceof Female) {
						// System.out.println("createdP is Female");
						NutriByte.view.genderComboBox.getSelectionModel().select(0);
					} else {
						// System.out.println("createdP is Male");
						NutriByte.view.genderComboBox.getSelectionModel().select(1);
					}

					// 3. Set all attributes in Area 1
					NutriByte.view.ageTextField.setText(String.format("%.2f", tempPerson.age));
					NutriByte.view.weightTextField.setText(String.format("%.2f", tempPerson.weight));
					NutriByte.view.heightTextField.setText(String.format("%.2f", tempPerson.height));
					NutriByte.view.ingredientsToWatchTextArea.setText(tempPerson.ingredientsToWatch);

					// 4. Invoke lists of NutriByte.person
					NutriByte.person.nutriConstantsTable = tempPerson.nutriConstantsTable;
					NutriByte.person.ageGroup = tempPerson.ageGroup;
					NutriByte.person.recommendedNutrientsList = tempPerson.recommendedNutrientsList;
					NutriByte.person.dietProductsList = tempPerson.dietProductsList;
					NutriByte.person.dietNutrientsMap = tempPerson.dietNutrientsMap;

					// 5.1 Initiate recommendedNutrientsTableView
					NutriProfiler.createNutriProfile(NutriByte.person);
					NutriByte.view.recommendedNutrientsTableView.setItems(NutriByte.person.recommendedNutrientsList);

					// 5.2 productsComboBox selects the first item
					if (NutriByte.model.searchResultsList.size() > 0) {
						NutriByte.view.productsComboBox.getSelectionModel().select(0);
					}

					// 5.3 Update NutriChart
//					NutriByte.person.populateDietNutrientsMap();
					NutriByte.view.dietProductsTableView.setItems(NutriByte.person.dietProductsList);
					NutriByte.view.nutriChart.updateChart();

				} else {
					new InvalidProfileException("Could not read profile data");
				}
			}
		}
	}

	// Similar to RecommendNutrientsButtonHandler
	class SaveMenuItemHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			String textField = "age";
			Float age = 0f, weight = 0f, height = 0f;

			// Confirm the validity of all person's attributes
			try {
				try {
					// Exception1
					if (NutriByte.view.genderComboBox.getSelectionModel().isEmpty()) {
						throw new InvalidProfileException("Missing gender information");
					}

					// Exception2
					if (NutriByte.view.ageTextField.getText().trim() == null
							|| NutriByte.view.ageTextField.getText().trim().length() == 0) {
						throw new InvalidProfileException("Missing age information");
					}
					age = Float.parseFloat(NutriByte.view.ageTextField.getText().trim());
					if (age <= 0) {
						throw new InvalidProfileException("Age must be a positive number");
					}

					// Exception3
					textField = "weight";
					if (NutriByte.view.weightTextField.getText().trim() == null
							|| NutriByte.view.weightTextField.getText().trim().length() == 0) {
						throw new InvalidProfileException("Missing weight information");
					}
					weight = Float.parseFloat(NutriByte.view.weightTextField.getText().trim());
					if (weight <= 0) {
						throw new InvalidProfileException("Weight must be a positive number");
					}

					// Exception4
					textField = "height";
					if (NutriByte.view.heightTextField.getText().trim() == null
							|| NutriByte.view.heightTextField.getText().trim().length() == 0) {
						throw new InvalidProfileException("Missing height information");
					}
					height = Float.parseFloat(NutriByte.view.heightTextField.getText().trim());
					if (height <= 0) {
						throw new InvalidProfileException("Height must be a positive number");
					}

				} catch (NumberFormatException | NullPointerException e) {
					throw new InvalidProfileException(String.format("Incorrect %s input. Must be a number", textField));
				}

				try {
					Stage mainStage = new Stage();
					FileChooser fileChooser = new FileChooser();
					fileChooser.setTitle("Save file");
					fileChooser.getExtensionFilters().addAll(new ExtensionFilter("CSV Files", "*.csv"));

					// Show save file dialog - CSVFiler.writeFile
					File file = fileChooser.showOpenDialog(mainStage);
					NutriByte.model.writeProfile(file.getAbsolutePath());

				} catch (RuntimeException e) {
					e.printStackTrace();
				}

			} catch (InvalidProfileException e) {
				e.printStackTrace();
			}
		}
	}

	// Clear Everything as the NewMenuItemHandler
	class CloseMenuItemHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {

			// 1. TextFields + TextAreas: 6 + 2
			NutriByte.view.productSearchTextField.clear();
			NutriByte.view.nutrientSearchTextField.clear();
			NutriByte.view.ingredientSearchTextField.clear();
			NutriByte.view.dietServingSizeTextField.clear();
			NutriByte.view.dietHouseholdSizeTextField.clear();
			NutriByte.view.ingredientSearchTextField.clear();
			NutriByte.view.ingredientsToWatchTextArea.clear();
			NutriByte.view.productIngredientsTextArea.clear();

			// 2. ComboBoxes + TableViews + NutriChart: 3 + 3 + 1
			NutriByte.view.genderComboBox.setItems(null);
			NutriByte.view.physicalActivityComboBox.setItems(null);
			NutriByte.view.productsComboBox.setItems(null);
			NutriByte.view.dietProductsTableView.setItems(null);
			NutriByte.view.productNutrientsTableView.setItems(null);
			NutriByte.view.recommendedNutrientsTableView.setItems(null);
			NutriByte.view.nutriChart.clearChart();

			// 3. Labels: 6
			NutriByte.view.servingSizeLabel.setText("0.00");
			NutriByte.view.householdSizeLabel.setText("0.00");
			NutriByte.view.servingUom.setText("");
			NutriByte.view.dietServingUomLabel.setText("");
			NutriByte.view.householdServingUom.setText("");
			NutriByte.view.dietHouseholdUomLabel.setText("");

			// 4. Set the WelcomeScene
			NutriByte.view.root.setCenter(NutriByte.view.setupWelcomeScene());
		}
	}

	// Pop-up Sample
	class AboutMenuItemHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("About");
			alert.setHeaderText("NutriByte");
			alert.setContentText(
					"Version 3.0 \nRelease 1.0\nCopyleft Java Nerds\nThis software is designed purely for educational purposes.\nNo commercial use intended");
			Image image = new Image(getClass().getClassLoader().getResource(NutriByte.NUTRIBYTE_IMAGE_FILE).toString());
			ImageView imageView = new ImageView();
			imageView.setImage(image);
			imageView.setFitWidth(300);
			imageView.setPreserveRatio(true);
			imageView.setSmooth(true);
			alert.setGraphic(imageView);
			alert.showAndWait();
		}
	}

	// II. Buttons: 5
	class RecommendNutrientsButtonHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			String gender = "", physicalActivityText = "", ingredients = "", textField = "age";
			Float age = 0f, weight = 0f, height = 0f, physicalActivityLevel = 0f;

			try {
				try {
					// Exception1
					if (NutriByte.view.genderComboBox.getSelectionModel().isEmpty()) {
						throw new InvalidProfileException("Missing gender information");
					}
					gender = NutriByte.view.genderComboBox.getSelectionModel().getSelectedItem();

					// Exception2
					if (NutriByte.view.ageTextField.getText().trim() == null
							|| NutriByte.view.ageTextField.getText().trim().length() == 0) {
						throw new InvalidProfileException("Missing age information");
					}
					age = Float.parseFloat(NutriByte.view.ageTextField.getText().trim());
					if (age <= 0) {
						throw new InvalidProfileException("Age must be a positive number");
					}

					// Exception3
					textField = "weight";
					if (NutriByte.view.weightTextField.getText().trim() == null
							|| NutriByte.view.weightTextField.getText().trim().length() == 0) {
						throw new InvalidProfileException("Missing weight information");
					}
					weight = Float.parseFloat(NutriByte.view.weightTextField.getText().trim());
					if (weight <= 0) {
						throw new InvalidProfileException("Weight must be a positive number");
					}

					// Exception4
					textField = "height";
					if (NutriByte.view.heightTextField.getText().trim() == null
							|| NutriByte.view.heightTextField.getText().trim().length() == 0) {
						throw new InvalidProfileException("Missing height information");
					}
					height = Float.parseFloat(NutriByte.view.heightTextField.getText().trim());
					if (height <= 0) {
						throw new InvalidProfileException("Height must be a positive number");
					}

					if (NutriByte.view.physicalActivityComboBox.getSelectionModel().getSelectedIndex() >= 0) {
						physicalActivityText = NutriByte.view.physicalActivityComboBox.getSelectionModel()
								.getSelectedItem().trim();
					}
					for (PhysicalActivityEnum physicalActivity : PhysicalActivityEnum.values()) {
						if (physicalActivityText.equalsIgnoreCase(physicalActivity.getName())) {
							physicalActivityLevel = physicalActivity.getPhysicalActivityLevel();
							break;
						}
					}

				} catch (NumberFormatException | NullPointerException e) {
					throw new InvalidProfileException(String.format("Incorrect %s input. Must be a number", textField));
				}

				// All 4 numbers passed, the object finally gets created
				if (gender.equalsIgnoreCase("male")) {
					NutriByte.person = new Male(age, weight, height, physicalActivityLevel, ingredients);
				}
				if (gender.equalsIgnoreCase("female")) {
					NutriByte.person = new Female(age, weight, height, physicalActivityLevel, ingredients);
				}

				NutriProfiler.createNutriProfile(NutriByte.person);
				NutriByte.view.recommendedNutrientsTableView.setItems(NutriByte.person.recommendedNutrientsList);
			} catch (InvalidProfileException e) {
				e.printStackTrace();
			}
		}
	}

	class SearchButtonHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {

			// 0. Clear all items in the searchResultsList
			NutriByte.model.searchResultsList.clear();

			String productSearch = NutriByte.view.productSearchTextField.getText().trim();
			String nutrientSearch = NutriByte.view.nutrientSearchTextField.getText().trim();
			String ingredientSearch = NutriByte.view.ingredientSearchTextField.getText().trim();

			// 1.0 Null search inputs result in the entire ProductsList
			if ((productSearch == null || productSearch.length() == 0)
					&& (nutrientSearch == null || nutrientSearch.length() == 0)
					&& (ingredientSearch == null || ingredientSearch.length() == 0)) {
				for (Product product : Model.productsMap.values()) {
					NutriByte.model.searchResultsList.add(product);
				}

			} else {
				// 1.1 productSearch
				if (NutriByte.model.searchResultsList.size() > 0) {
					for (Product product : NutriByte.model.searchResultsList) {
						if (!product.getProductName().toLowerCase().contains(productSearch.toLowerCase())) {
							NutriByte.model.searchResultsList.remove(product);
						}
					}
				} else {
					for (Product product : Model.productsMap.values()) {
						if (product.getProductName().toLowerCase().contains(productSearch.toLowerCase())
								&& productSearch != null && productSearch.length() > 0) {
							NutriByte.model.searchResultsList.add(product);
						}
					}
				}

				// 1.2 nutrientSearch
				String nutrientCode = "";
				for (Nutrient nutrient : Model.nutrientsMap.values()) {
					if (nutrient.getNutrientName().equalsIgnoreCase(nutrientSearch)) {
						nutrientCode = nutrient.getNutrientCode();
					}
				}

				if (nutrientCode.length() > 0) {
					if (NutriByte.model.searchResultsList.size() > 0) {
						for (Product product : NutriByte.model.searchResultsList) {
							if (!product.getProductNutrients().keySet().contains(nutrientCode)) {
								NutriByte.model.searchResultsList.remove(product);
							}
						}
					} else {
						for (Product product : Model.productsMap.values()) {
							if (product.getProductNutrients().keySet().contains(nutrientCode) && nutrientSearch != null
									&& nutrientSearch.length() > 0) {
								NutriByte.model.searchResultsList.add(product);
							}
						}
					}
				}

				// 1.3 ingredientSearch
				if (NutriByte.model.searchResultsList.size() > 0) {
					for (Product product : NutriByte.model.searchResultsList) {
						if (!product.getIngredients().toLowerCase().contains(ingredientSearch.toLowerCase())) {
							NutriByte.model.searchResultsList.remove(product);
						}
					}
				} else {
					for (Product product : Model.productsMap.values()) {
						if (product.getIngredients().toLowerCase().contains(ingredientSearch.toLowerCase())
								&& ingredientSearch != null && ingredientSearch.length() > 0) {
							NutriByte.model.searchResultsList.add(product);
						}
					}
				}
			}

			// 2. searchResultSizeLabel
			NutriByte.view.searchResultSizeLabel
					.setText(String.format("%d product(s) found", NutriByte.model.searchResultsList.size()));

			// 3. productsComboBox selects the first item
			if (NutriByte.model.searchResultsList.size() > 0) {
				NutriByte.view.productsComboBox.getSelectionModel().select(0);
			} else {

				// 4. No results found: set everything to default similar as the
				// ClearButtonHandler
				// 4.1 TextArea + TableView
				NutriByte.view.productIngredientsTextArea.clear();

				// 4.2 Labels
				NutriByte.view.servingSizeLabel.setText("0.00");
				NutriByte.view.householdSizeLabel.setText("0.00");
				NutriByte.view.dietServingUomLabel.setText("");
				NutriByte.view.dietHouseholdUomLabel.setText("");
				NutriByte.view.servingUom.setText("");
				NutriByte.view.householdServingUom.setText("");

			}
		}
	}

	class ClearButtonHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {

			// 1. TextFields + TextArea
			NutriByte.view.productSearchTextField.clear();
			NutriByte.view.nutrientSearchTextField.clear();
			NutriByte.view.ingredientSearchTextField.clear();
			NutriByte.view.productIngredientsTextArea.clear();

			// 2. ComboBox + TableView
			NutriByte.model.searchResultsList.clear();
			NutriByte.view.productNutrientsTableView.setItems(null);

			// 3. Labels
			NutriByte.view.servingSizeLabel.setText("0.00");
			NutriByte.view.householdSizeLabel.setText("0.00");
			NutriByte.view.dietServingUomLabel.setText("");
			NutriByte.view.dietHouseholdUomLabel.setText("");
			NutriByte.view.servingUom.setText("");
			NutriByte.view.householdServingUom.setText("");

		}
	}

	class AddDietButtonHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			if (NutriByte.view.productsComboBox.getSelectionModel().getSelectedIndex() >= 0) {

				// 0. Extract the selected product in the ProductsComboBox
				Product productSelected = NutriByte.view.productsComboBox.getSelectionModel().getSelectedItem();

				// 1. Calculate the quantities
				Float dietQuantity = 0f, dietHouseholdSize = 0f;
				String dietServingSizeText = NutriByte.view.dietServingSizeTextField.getText().trim();
				String dietHouseholdSizeText = NutriByte.view.dietServingSizeTextField.getText().trim();

				// * NumberFormatException may occur here
				try {
					// Case 1
					if ((dietServingSizeText == null || dietServingSizeText.length() == 0)
							&& (dietHouseholdSizeText == null || dietHouseholdSizeText.length() == 0)) {
						dietQuantity = productSelected.getServingSize();
						dietHouseholdSize = productSelected.getHouseholdSize();
					}

					// Case 2
					if (dietServingSizeText != null && dietServingSizeText.length() > 0
							&& (dietHouseholdSizeText == null || dietHouseholdSizeText.length() == 0)) {
						dietQuantity = Float.parseFloat(dietServingSizeText);
						dietHouseholdSize = dietQuantity * productSelected.getHouseholdSize()
								/ productSelected.getServingSize();
					}

					// Case 3
					if ((dietServingSizeText == null || dietServingSizeText.length() == 0)
							&& dietHouseholdSizeText != null && dietHouseholdSizeText.length() > 0) {
						dietHouseholdSize = Float.parseFloat(dietHouseholdSizeText);
						dietQuantity = dietHouseholdSize * productSelected.getServingSize()
								/ productSelected.getHouseholdSize();
					}

					// Case 4 (Same with Case 2)
					if (dietServingSizeText != null && dietServingSizeText.length() > 0 && dietHouseholdSizeText != null
							&& dietHouseholdSizeText.length() > 0) {
						dietQuantity = Float.parseFloat(dietServingSizeText);
						dietHouseholdSize = dietQuantity * productSelected.getHouseholdSize()
								/ productSelected.getServingSize();
					}

					// 2. Add the product to Person's dietProductsList
					productSelected.setServingSize(dietQuantity);
					productSelected.setHouseholdSize(dietHouseholdSize);

					NutriByte.person.dietProductsList.add(productSelected);
					NutriByte.view.dietProductsTableView.setItems(NutriByte.person.dietProductsList);

					// 3. Update dietNutrientsMap and NutriChart
					NutriByte.person.populateDietNutrientsMap();
					NutriByte.view.nutriChart.updateChart();
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
			}
		}
	}

	class RemoveDietButtonHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {

			// 1. Remove the selected product from the dietProductsList
			int index = NutriByte.view.dietProductsTableView.getSelectionModel().getFocusedIndex();
			if (NutriByte.person.dietProductsList.size() > index) {
				NutriByte.person.dietProductsList.remove(index);
			}

			// 2. Update the dietNutrientsMap and NutriChart
			NutriByte.person.populateDietNutrientsMap();
			NutriByte.view.nutriChart.updateChart();
		}
	}

	// III. ComboBoxListener: 1
	class ProductsComboBoxListener implements ChangeListener<Product> {
		@Override
		public void changed(ObservableValue<? extends Product> observable, Product oldValue, Product newValue) {
			if (newValue != null) {

				// 1. Populate the productNutrientsTableView
				NutriByte.view.productNutrientsTableView
						.setItems(FXCollections.observableArrayList(newValue.getProductNutrients().values()));

				// 2. Set TextArea and Labels
				NutriByte.view.productIngredientsTextArea.setText(newValue.getIngredients());
				NutriByte.view.dietServingUomLabel
						.setText(String.format("%.2f %s", newValue.getServingSize(), newValue.getServingUom()));
				NutriByte.view.dietHouseholdUomLabel
						.setText(String.format("%.2f %s", newValue.getHouseholdSize(), newValue.getHouseholdUom()));
				NutriByte.view.servingUom.setText(newValue.getServingUom());
				NutriByte.view.householdServingUom.setText(newValue.getHouseholdUom());
			}
		}
	}

//	// IV. Exceptions
//	class ErrorMessageHandler implements EventHandler<ActionEvent> {
//		@Override
//		public void handle(ActionEvent event) {
//			Alert alert = new Alert(AlertType.INFORMATION);
//			alert.setTitle("Exceptions");
//			alert.setHeaderText("NutriByte");
//			alert.setContentText("Exceptions");
//			alert.showAndWait();
//		}
//	}

}
