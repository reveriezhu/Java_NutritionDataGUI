//Jiahui Cai (jiahuic)
package victoria;

import java.io.File;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import victoria.NutriProfiler.PhysicalActivityEnum;

public class Controller {
	/*
	 * to do: with added exception handling
	 *
	 * populate the recommendedNutrientList based on data input in GUI component
	 * whenever the recommendNutrientsButton gets clicked
	 */
	class RecommendNutrientsButtonHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			String fieldname = "age";
			// populate content's person part using GUI component
			float age = 0, weight = 0, height = 0;
			TextField textField = NutriByte.view.ageTextField;
			// validate the person data
			try {
				if (NutriByte.view.genderComboBox.valueProperty().get() == null) {
					// question: is it null, or Gender?
					// !gender.equals("Female")&&!gender.equals("Male")
					throw new InvalidProfileException("Missing gender information");
				}
				if (textField.getText().length() == 0) {
					throw new InvalidProfileException("Missing age information");
				}
				try {
					age = Float.parseFloat(textField.getText().trim());
					if (age < 0) {
						throw new InvalidProfileException("Age must be a positive number");
					}
					textField = NutriByte.view.weightTextField;
					fieldname = "weight";
					if (textField.getText().length() == 0) {
						throw new InvalidProfileException("Missing weight information");
					}
					weight = Float.parseFloat(textField.getText().trim());
					if (weight < 0) {
						throw new InvalidProfileException("Weight must be a positive number");
					}
					fieldname = "height";
					textField = NutriByte.view.heightTextField;
					if (textField.getText().length() == 0) {
						throw new InvalidProfileException("Missing height information");
					}
					height = Float.parseFloat(textField.getText().trim());
					if (height < 0) {
						throw new InvalidProfileException("Height must be a positive number");
					}

				} catch (NumberFormatException e) {
					String message = String.format("Incorrect %s input. Must be a number", fieldname);
					throw new InvalidProfileException(message);
				}

				// (1) create a profile with NutriProfiler.createNutriProfile(person)
				// first, using the data of person to instantiate a child type of person
				Person person = null;
				String gender = NutriByte.view.genderComboBox.valueProperty().get();
				age = Float.parseFloat(NutriByte.view.ageTextField.getText());
				weight = Float.parseFloat(NutriByte.view.weightTextField.getText());
				height = Float.parseFloat(NutriByte.view.heightTextField.getText());
				float physicalActivityLevel = 0;// just to initialize the variable
				String ingredientsToWatch = NutriByte.view.ingredientsToWatchTextArea.getText();

				// iterate through the PhysicalActivityEnum
				// to get the physicalActivityLevel matched with physicalActivityName
				PhysicalActivityEnum[] physicAct = PhysicalActivityEnum.values();
				// set the default physicalActivityLevel to the first value in Enum, SEDENTARY
				if (NutriByte.view.physicalActivityComboBox.getSelectionModel().getSelectedIndex() < 0) {
					NutriByte.view.physicalActivityComboBox.getSelectionModel().select(0);
				}
				for (int i = 0; i < physicAct.length; i++) {
					if (physicAct[i].getName() == NutriByte.view.physicalActivityComboBox.valueProperty().get()) {
						physicalActivityLevel = physicAct[i].getPhysicalActivityLevel();
						break;
					}
				}

				switch (gender) {
				case "Female": {
					person = new Female(age, weight, height, physicalActivityLevel, ingredientsToWatch);
					break;
				}
				case "Male": {
					person = new Male(age, weight, height, physicalActivityLevel, ingredientsToWatch);
					break;
				}
				default:
					break;
				}
				// then populate the NutriProfiler.recommendedNutrientsList based on it
				// using NutriProfiler.createNutriProfile(person)
				NutriProfiler.createNutriProfile(person);
				// (2)(binding model with view)
				// populated the items in table view using
				// NutriProfiler.recommendedNutrientsList
				NutriByte.view.recommendedNutrientsTableView.setItems(person.recommendedNutrientsList);
			} catch (InvalidProfileException e) {
				System.out.println("The profile has not been successfully saved because the invalid person data");
			}
		}
	}

	/*
	 * to do: with added exception handling
	 *
	 * start a stage for user to chose file and then pass the filename of chosen
	 * file to the NutriByte.model.readProfile method. the method read the file,and
	 * pass a male/female object to NutriByte.person we can then display the profile
	 * data of NutiByte.person, populate recommendedNutrientList, and bind the list
	 * to the tableView
	 */
	class OpenMenuItemHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			/* 0:clear the GUI component */

			// set up a new stage for a fileChooser
			Stage temp = new Stage();
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("select file");
			fileChooser.getExtensionFilters().addAll(new ExtensionFilter("csv files", "*.csv"),
					new ExtensionFilter("xml files", "*.xml"));
			File file = null;
			if ((file = fileChooser.showOpenDialog(temp)) != null) {
				String filename = file.getAbsolutePath();
				// read the file,and pass a male/female object to NutriByte.person
				boolean flag = NutriByte.model.readProfiles(filename);
				// System.out.println("readfile flag "+flag);
				if (flag) {
					NutriByte.view.root.setCenter(NutriByte.view.nutriTrackerPane);
					NutriByte.view.initializePrompts();
					/*
					 * 0:store the data in NutriByte.person by createdP , cause it will be changed
					 * once we start to parse value to GUI component
					 */
					Person createdP = null;
					if (NutriByte.person instanceof Female) {
						createdP = new Female(NutriByte.person.age, NutriByte.person.weight, NutriByte.person.height,
								NutriByte.person.physicalActivityLevel, NutriByte.person.ingredientsToWatch);
					} else {
						createdP = new Male(NutriByte.person.age, NutriByte.person.weight, NutriByte.person.height,
								NutriByte.person.physicalActivityLevel, NutriByte.person.ingredientsToWatch);
					}
					createdP.nutriConstantsTable = NutriByte.person.nutriConstantsTable;
					createdP.ageGroup = NutriByte.person.ageGroup;
					createdP.recommendedNutrientsList = NutriByte.person.recommendedNutrientsList;
					createdP.dietProductsList = NutriByte.person.dietProductsList;
					createdP.dietNutrientsMap = NutriByte.person.dietNutrientsMap;

					/* 1: setting the GUI component with profile data of NutriByte.person */
					PhysicalActivityEnum[] physicAct = PhysicalActivityEnum.values();
					System.out.println(NutriByte.person.physicalActivityLevel);

					for (int i = 0; i < physicAct.length; i++) {
						if (physicAct[i].getPhysicalActivityLevel() == createdP.physicalActivityLevel) {
							NutriByte.view.physicalActivityComboBox.getSelectionModel().select(i);
							break;
						}
					}
					if (createdP instanceof Female) {
						// System.out.println("createdP is Female");
						NutriByte.view.genderComboBox.getSelectionModel().select(0);
					} else {
						// System.out.println("createdP is Male");
						NutriByte.view.genderComboBox.getSelectionModel().select(1);
					}
					NutriByte.view.ageTextField.setText(Float.toString(createdP.age));
					NutriByte.view.weightTextField.setText(Float.toString(createdP.weight));
					NutriByte.view.heightTextField.setText(Float.toString(createdP.height));
					NutriByte.view.ingredientsToWatchTextArea.setText(createdP.ingredientsToWatch);
					// invoke NutriProfiler.createNutriProfile
					/* : populate the field of NutriByte.person for following usage */
					NutriByte.person.nutriConstantsTable = createdP.nutriConstantsTable;
					NutriByte.person.ageGroup = createdP.ageGroup;
					NutriByte.person.recommendedNutrientsList = createdP.recommendedNutrientsList;
					NutriByte.person.dietProductsList = createdP.dietProductsList;
					NutriByte.person.dietNutrientsMap = createdP.dietNutrientsMap;
					// NutriProfiler.recommendedNutrientsList will be populated accordingly;
					NutriProfiler.createNutriProfile(NutriByte.person);
					// show the NutriProfiler.recommendedNutrientsList by binding it to tableView
					NutriByte.view.recommendedNutrientsTableView.setItems(NutriByte.person.recommendedNutrientsList);
					/*
					 * 2: setting the GUI component with profile data of
					 * NutriByte.person.dietNutrientsMap
					 */
					NutriByte.view.dietProductsTableView.setItems(NutriByte.person.dietProductsList);
					// NutriByte.person.populateDietNutrientMap();
					NutriByte.view.nutriChart.updateChart();
				} else {
					new InvalidProfileException("Could not read profile data");
				}
			}

		}
	}

	/*
	 * to do : clear up all data from the previous user interaction,including
	 * nutrientChart
	 *
	 * switches the screen from welcome screen to NutriByte.view.nutriTrackerPane
	 * invoke initializePrompts to clear prompts clear the item stored in
	 * recommendedNutrientsTableView
	 */
	class NewMenuItemHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			NutriByte.view.root.setCenter(NutriByte.view.nutriTrackerPane);
			NutriByte.view.initializePrompts();
		}
	}

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

	/*
	 * to do: open up file chooser when a file name is entered by the users, take
	 * the person-data and diet data and save it into a new profile file perform all
	 * required validation and exception handling for person data: if
	 * gender,age,weight,or height is missing/invalid before saving
	 */
	class SaveMenuItemHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			String fieldname = "age";
			// populate content's person part using GUI component
			float age = 0, weight = 0, height = 0;
			TextField textField = NutriByte.view.ageTextField;
			try {
				if (NutriByte.view.genderComboBox.valueProperty().get() == null) {
					// question: is it null, or Gender?
					// !gender.equals("Female")&&!gender.equals("Male")
					throw new InvalidProfileException("Missing gender information");
				}
				if (textField.getText().length() == 0) {
					throw new InvalidProfileException("Missing age information");
				}
				try {
					age = Float.parseFloat(textField.getText().trim());
					textField = NutriByte.view.weightTextField;
					fieldname = "weight";
					if (textField.getText().length() == 0) {
						throw new InvalidProfileException("Missing weight information");
					}
					weight = Float.parseFloat(textField.getText().trim());
					fieldname = "height";
					textField = NutriByte.view.heightTextField;
					if (textField.getText().length() == 0) {
						throw new InvalidProfileException("Missing height information");
					}
					height = Float.parseFloat(textField.getText().trim());
				} catch (NumberFormatException e) {
					String message = String.format("Incorrect %s input. Must be a number", fieldname);
					throw new InvalidProfileException(message);
				}
				if (age < 0) {
					throw new InvalidProfileException("Age must be a positive number");
				}

				if (weight < 0) {
					throw new InvalidProfileException("Weight must be a positive number");
				}

				if (height < 0) {
					throw new InvalidProfileException("Height must be a positive number");
				}

				Stage temp = new Stage();
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("save file");
				// Set extension filter for text files
				FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
				fileChooser.getExtensionFilters().add(extFilter);

				// Show save file dialog
				File file = fileChooser.showSaveDialog(temp);
				String filename = file.getAbsolutePath();
				boolean flag = NutriByte.model.writeProfile(filename);
				if (!flag) {
					System.out.println("The profile has not been successfully saved because the filechoosing");
				}
			} catch (InvalidProfileException e) {
				System.out.println("The profile has not been successfully saved because the invalid person data");
			}

		}
	}

	/*
	 * to do: handles all search functionality
	 */
	class SearchButtonHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			NutriByte.model.searchResultsList.clear();// ?-----------
			String prod = "", nut = "", ing = "";// Set the default value of search string as empty string
			// set each search string based on user input(if existing)
			if (NutriByte.view.productSearchTextField.getText() != null) {
				prod = NutriByte.view.productSearchTextField.getText();
			}
			if (NutriByte.view.nutrientSearchTextField.getText() != null) {
				nut = NutriByte.view.nutrientSearchTextField.getText();
			}
			if (NutriByte.view.ingredientSearchTextField.getText() != null) {
				ing = NutriByte.view.ingredientSearchTextField.getText();
			}
			// populate NutriByte.model.searchResultsList
			// iterate through Model.productsMap(key: ndbNumber, Value: Product object)
			boolean flag = false;// if the product meets the requirement
			for (String str : Model.productsMap.keySet()) {
				flag = false;
				String prodName = Model.productsMap.get(str).getProductName();
				String ingredients = Model.productsMap.get(str).getIngredients();
				if ((ingredients == null || ingredients.length() == 0) && ing.length() == 0) {

				}
				if (prodName.toLowerCase().indexOf(prod.toLowerCase()) != -1
						&& (ingredients.toLowerCase().indexOf(ing.toLowerCase()) != -1
								|| (ingredients == null || ingredients.length() == 0) && ing.length() == 0)) {
					if (Model.productsMap.get(str).getProductNutrients().keySet().size() > 0) {
						for (String key : Model.productsMap.get(str).getProductNutrients().keySet()) {
							String nutName = Model.nutrientsMap.get(key).getNutrientName();
							if (nutName.toLowerCase().indexOf(nut.toLowerCase()) != -1) {
								flag = true;
								break;
							}
						}
					} else if (nut.length() == 0) {
						flag = true;
					}
				}
				if (flag) {
					NutriByte.model.searchResultsList.add(Model.productsMap.get(str));
				}
			}
			if (NutriByte.model.searchResultsList.size() > 0) {
				// populate productsComboBox and select the first product by default
				NutriByte.view.productsComboBox.setItems(NutriByte.model.searchResultsList);
				if (NutriByte.view.productsComboBox.getSelectionModel().getSelectedIndex() < 0) {
					NutriByte.view.productsComboBox.getSelectionModel().select(0);
				}
				// get selection
				int index = NutriByte.view.productsComboBox.getSelectionModel().getSelectedIndex();
				Product selectedProd = NutriByte.model.searchResultsList.get(index);
				// populate searchResultSizeLabel, servingSizeLabel,householdSizeLabel
				NutriByte.view.searchResultSizeLabel
						.setText(String.format("%d product(s) found", NutriByte.model.searchResultsList.size()));
				NutriByte.view.servingSizeLabel.setText(
						String.format("%8.2f %s", selectedProd.getServingSize(), selectedProd.getServingUom()));
				NutriByte.view.householdSizeLabel.setText(
						String.format("%8.2f %s", selectedProd.getHouseholdSize(), selectedProd.getHouseholdUom()));
				// populate productIngredientsTextArea
				NutriByte.view.productIngredientsTextArea.setText(selectedProd.getIngredients());
				// populate productNutrientsTableView
				// SHOULD I SET THE BINDING HERE?
				NutriByte.view.productNutrientsTableView
						.setItems(FXCollections.observableArrayList(selectedProd.getProductNutrients().values()));
			} else {
				NutriByte.view.searchResultSizeLabel.setText("");
				NutriByte.view.servingSizeLabel.setText("0.00");
				NutriByte.view.householdSizeLabel.setText("0.00");
				NutriByte.view.productIngredientsTextArea.setText("");
				NutriByte.view.productNutrientsTableView.setItems(null);

			}
		}
	}

	/*
	 * to do: clears product, nutrient and ingredient search boxes and all products
	 * from the productsComboBox
	 */
	class ClearButtonHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			NutriByte.view.productSearchTextField.setText("");
			NutriByte.view.nutrientSearchTextField.setText("");
			NutriByte.view.ingredientSearchTextField.setText("");
			NutriByte.view.searchResultSizeLabel.setText("");
			NutriByte.view.servingSizeLabel.setText("0.00");
			NutriByte.view.householdSizeLabel.setText("0.00");
			NutriByte.view.dietServingUomLabel.setText("");
			NutriByte.view.dietHouseholdUomLabel.setText("");
			NutriByte.view.productIngredientsTextArea.setText("");
			NutriByte.view.productNutrientsTableView.setItems(null);
			NutriByte.view.productsComboBox.setItems(null);
			NutriByte.view.productsComboBox.setPromptText("");
		}
	}

	/*
	 * to do: selecting any product in the combo-box will display the product's
	 * nutrients in the productNutrientsTable view It's ingredients are displayed in
	 * the productIngredientsTextArea It's serving size with serving Uom and
	 * household size are displayed in region3 It's Uom are also shown next to the
	 * dietServingSizeTextField and dietHouseholdSizeTextField
	 */
	class ProductComboBoxListener implements ChangeListener<Product> {

		@Override
		public void changed(ObservableValue<? extends Product> observable, Product oldValue, Product newValue) {
			if (newValue != null) {
				NutriByte.view.productNutrientsTableView
						.setItems(FXCollections.observableArrayList(newValue.getProductNutrients().values()));
				NutriByte.view.servingSizeLabel
						.setText(String.format("%8.2f %s", newValue.getServingSize(), newValue.getServingUom()));
				NutriByte.view.householdSizeLabel
						.setText(String.format("%8.2f %s", newValue.getHouseholdSize(), newValue.getHouseholdUom()));
				NutriByte.view.productIngredientsTextArea.setText(newValue.getIngredients());
				NutriByte.view.dietServingUomLabel.setText(newValue.getServingUom());
				NutriByte.view.dietHouseholdUomLabel.setText(newValue.getHouseholdUom());
			}
		}

	}

	/*
	 * to do:
	 *
	 * Neelam Dwivedi Neelam Dwivedi Yesterday Nov 22 at 1:27pm If profile data is
	 * not provided then the recommended nutrients table should remain blank, and
	 * adding new diet products should make no change in the nutri-chart. You can do
	 * this anyway you want. The app should not throw any error.
	 *
	 *
	 * The product selected in the productsComboBox is added to the
	 * dietProductsTableView. Three scenarios:  User doesn’t enter anything in the
	 * two textfields - dietServingSizeTextField and dietHouseholdSizeTextField. In
	 * this case, use product standard serving size as the quantity in the diet. 
	 * User enters only in dietServingSizeTextField: Use the quantity entered by
	 * user as the diet-quantity, and calculate the household size based on the
	 * ratio of serving size vs. house hold size in the master product in
	 * Model.productsMap  User enters only in dietHouseholdSizeTextField: Use the
	 * quantity entered by user as the diet-quantity, and calculate the serving size
	 * based on the ratio of serving size vs. house hold size in the master product
	 * in Model.productsMap  User enters both: Give preference to the data in
	 * dietServingSizeTextField over dietHouseholdSizeTextField and compute
	 * household size based on the ratio of serving vs. household size in
	 * Model.productsMap. After calculating the quantities, add the product to
	 * Model’s dietProductsList, add up all nutrients to dietNutrientsMap, and
	 * update the nutriChart.
	 */
	class AddDietButtonHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			if (NutriByte.view.productsComboBox.getSelectionModel().getSelectedItem() != null) {
				Product selectedProd = NutriByte.view.productsComboBox.getSelectionModel().getSelectedItem();
				Product changedProd = new Product(selectedProd.getNdbNumber(), selectedProd.getProductName(),
						selectedProd.getManufacturer(), selectedProd.getIngredients());
				Float houseQ, servQ;// the actual householdSize/servingSize
				Float stdHouseQ, stdServQ;
				stdHouseQ = selectedProd.getHouseholdSize();
				stdServQ = selectedProd.getServingSize();
				boolean flag1 = NutriByte.view.dietServingSizeTextField.getText().length() == 0;
				boolean flag2 = NutriByte.view.dietHouseholdSizeTextField.getText().length() == 0;

				if (flag1 && flag2) {
					servQ = stdServQ;
					houseQ = stdHouseQ;
				} else if (flag2) {
					// no need to handle garbage input here.
					servQ = Float.parseFloat(NutriByte.view.dietServingSizeTextField.getText().trim());
					if (stdServQ == 0) {
						houseQ = stdHouseQ;
					} else {
						houseQ = servQ * stdHouseQ / stdServQ;
					}
				} else if (flag1) {
					houseQ = Float.parseFloat(NutriByte.view.dietHouseholdSizeTextField.getText().trim());
					if (stdHouseQ == 0) {
						servQ = stdServQ;
					} else {
						servQ = houseQ * stdServQ / stdHouseQ;
					}
				} else {
					servQ = Float.parseFloat(NutriByte.view.dietServingSizeTextField.getText().trim());
					if (stdServQ == 0) {
						houseQ = stdHouseQ;
					} else {
						houseQ = servQ * stdHouseQ / stdServQ;
					}
				}
				// store the calculated servQ and houseQ in a Product type object, selectedProd
				// System.out.printf("s serve%f, std serve
				// %f%n",servQ,selectedProd.getServingSize());
				changedProd.setProductNutrients(selectedProd.getProductNutrients());
				changedProd.setServingUom(selectedProd.getServingUom());
				changedProd.setHouseholdUom(selectedProd.getHouseholdUom());
				changedProd.setServingSize(servQ);
				changedProd.setHouseholdSize(houseQ);
				// System.out.printf("changed:s serve%f, std serve
				// %f%n",houseQ,servQ,selectedProd.getServingSize());
				try {
					NutriByte.person.dietProductsList.add(changedProd);
					NutriByte.person.populateDietNutrientMap();
					NutriByte.view.dietProductsTableView.setItems(NutriByte.person.dietProductsList);
					NutriByte.view.nutriChart.updateChart();
				} catch (RuntimeException e) {
					new InvalidProfileException("The person profile is not valid, could not add diet!");
				}
			}
		}
	}

	/*
	 * to do: Clicking this button should reverse the process done in
	 * AddDietButtonHandler
	 */
	class RemoveDietButtonHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			if (NutriByte.view.dietProductsTableView.getSelectionModel().getSelectedIndex() >= 0) {
				int index = NutriByte.view.dietProductsTableView.getSelectionModel().getSelectedIndex();
				Product p = NutriByte.view.dietProductsTableView.getSelectionModel().getSelectedItem();
				NutriByte.person.dietProductsList.remove(index);
				for (String nutCod : p.getProductNutrients().keySet()) {
					if (NutriByte.person.dietNutrientsMap.get(nutCod) != null) {
						{// if the nutrient exists in the map
							Float q = NutriByte.person.dietNutrientsMap.get(nutCod).getNutrientQuantity()
									- p.getProductNutrients().get(nutCod).getNutrientQuantity() * p.getServingSize()
											/ 100;
							NutriByte.person.dietNutrientsMap.get(nutCod).setNutrientQuantity(q);
						}
					}
				}
				NutriByte.view.dietProductsTableView.setItems(NutriByte.person.dietProductsList);
				NutriByte.view.nutriChart.updateChart();
			}
		}
	}

	/* set the center to welcome scene */
	class CloseMenuItemHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			NutriByte.view.root.setCenter(NutriByte.view.setupWelcomeScene());
			NutriByte.view.initializePrompts();
			// clear the table view
			NutriByte.view.recommendedNutrientsTableView.setItems(null);
			NutriByte.view.productNutrientsTableView.setItems(null);
			NutriByte.view.dietProductsTableView.setItems(null);
			// clear the label/textfield
			NutriByte.view.productSearchTextField.setText("");
			NutriByte.view.nutrientSearchTextField.setText("");
			NutriByte.view.ingredientSearchTextField.setText("");
			NutriByte.view.searchResultSizeLabel.setText("");
			NutriByte.view.servingSizeLabel.setText("0.00");
			NutriByte.view.householdSizeLabel.setText("0.00");
			NutriByte.view.dietHouseholdSizeTextField.setText("");
			NutriByte.view.dietServingSizeTextField.setText("");
			NutriByte.view.dietServingUomLabel.setText("");
			NutriByte.view.dietHouseholdUomLabel.setText("");
			NutriByte.view.productIngredientsTextArea.setText("");
			NutriByte.view.productNutrientsTableView.setItems(null);
			NutriByte.view.productsComboBox.setItems(null);
			// clear the nutrichart
			NutriByte.view.nutriChart.clearChart();
		}
	}
}
