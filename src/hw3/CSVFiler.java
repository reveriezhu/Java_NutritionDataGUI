//AndrewID: yuhaozhu Name: Yuhao Zhu

package hw3;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import hw3.NutriProfiler.PhysicalActivityEnum;

public class CSVFiler extends DataFiler {

	// I. Write File
	@Override
	public void writeFile(String filename) throws InvalidProfileException {
		String gender = "", physicalActivityText = "", ingredients = "";
		Float age = 0f, weight = 0f, height = 0f, physicalActivityLevel = 0f;

		// 1. Setup all person's attributes
		gender = NutriByte.view.genderComboBox.valueProperty().get();
		age = Float.parseFloat(NutriByte.view.ageTextField.getText().trim());
		weight = Float.parseFloat(NutriByte.view.weightTextField.getText().trim());
		height = Float.parseFloat(NutriByte.view.heightTextField.getText().trim());
		physicalActivityText = NutriByte.view.physicalActivityComboBox.getSelectionModel().getSelectedItem().trim();
		for (PhysicalActivityEnum physicalActivity : PhysicalActivityEnum.values()) {
			if (physicalActivityText.equalsIgnoreCase(physicalActivity.getName())) {
				physicalActivityLevel = physicalActivity.getPhysicalActivityLevel();
				break;
			}
		}
		ingredients = NutriByte.view.ingredientsToWatchTextArea.getText().trim();

		try (BufferedWriter bw = new BufferedWriter(new FileWriter("filename"));) {

			// 2.1 Write person's profile
			bw.write(String.format("%s,%.2f,%.2f,%.2f,%.2f,%s%n", gender, age, weight, height, physicalActivityLevel,
					ingredients));

			// 2.2 Write dietProducts
			if (NutriByte.person.dietProductsList.size() > 0) {
				for (Product product : NutriByte.person.dietProductsList) {
					bw.write(String.format("%s,%.2f,%.2%n", product.getNdbNumber(), product.getServingSize(),
							product.getHouseholdSize()));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// II. Read File: Using Helper1 + Helper2
	@Override
	public boolean readFile(String filename) {
		// 0. Clear last person profile and comboBox
		if (NutriByte.person != null) {
			NutriByte.person.dietProductsList.clear();
			NutriByte.person.dietNutrientsMap.clear();
		}
		NutriByte.model.searchResultsList.clear();

		try (Scanner s = new Scanner(new BufferedReader(new FileReader(filename)))) {
			String profileLine = null;

			// 1. Read the person profile from the first line
			try {
				if ((profileLine = s.nextLine()) != null) {
					NutriByte.person = validatePersonData(profileLine);
				}
			} catch (InvalidProfileException e1) {

				// 0. Initiate the Scene
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

				return false;
			}

			// 2. Read the dietProducts from the rest lines
			try {
				while ((profileLine = s.nextLine()) != null) {
					Product dietProduct = validateProductData(profileLine);
					NutriByte.person.dietProductsList.add(dietProduct);
					NutriByte.model.searchResultsList.add(dietProduct);
				}
			} catch (InvalidProfileException e) {
				e.printStackTrace();
			}

			// 3. populateDietNutrientsMap here since it's irrelevant with profileBinding
			NutriByte.person.populateDietNutrientsMap();
			return true;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return false;
	}

	// Helper1: throws InvalidProfileException
	public Person validatePersonData(String data) throws InvalidProfileException {

		// 1. Initiate all person's attributes and helpers
		String gender = "", ingredients = "";
		Float age = 0f, weight = 0f, height = 0f, physicalActivityLevel = 0f;
		Person person = null;
		String[] values = data.split(",");
		StringBuilder ingredientsBuilder = new StringBuilder();

		// 2. Throw all InvalidProfileExceptions
		if (!gender.equalsIgnoreCase("male") && !gender.equalsIgnoreCase("female")) {
			throw new InvalidProfileException("The profile must have gender. Female or Male as first word");
		}
		if (values[0].trim().length() == 0
				|| !values[0].trim().equalsIgnoreCase("female") && !values[0].trim().equalsIgnoreCase("male")) {
			throw new InvalidProfileException("The profile must have gender:Female or Male as first word");
		}
		if (values[1].trim().length() == 0) {
			throw new InvalidProfileException(
					String.format("Invalid data for Age: %s %nAge must be a number", values[1]));
		}
		if (values[2].trim().length() == 0) {
			throw new InvalidProfileException(
					String.format("Invalid data for Weight: %s %nWeight must be a number", values[2]));
		}
		if (values[3].trim().length() == 0) {
			throw new InvalidProfileException(
					String.format("Invalid data for Height: %s %nHeight must be a number", values[3]));
		}
		if (values[4].trim().length() == 0) {
			throw new InvalidProfileException(
					String.format("Invalid physical activity level: %s %nMust be 1.0, 1.1, 1.25, or 1.48", values[4]));
		}

		// 3. Create the new person
		gender = values[0].trim();
		age = Float.parseFloat(values[1].trim());
		weight = Float.parseFloat(values[2].trim());
		height = Float.parseFloat(values[3].trim());
		physicalActivityLevel = Float.parseFloat(values[2].trim());

		// 3.1 Another InvalidProfileException
		if (physicalActivityLevel != 1.0 && physicalActivityLevel != 1.1 && physicalActivityLevel != 1.25
				&& physicalActivityLevel != 1.48) {
			throw new InvalidProfileException("Invalid Data for PhysicalActivityLevel: " + values[4].trim()
					+ "\nMust be: 1.0, 1.1, 1.25 or 1.48");
		}

		for (int i = 5; i < values.length; i++) {
			ingredientsBuilder.append(values[i].trim() + ",");
		}
		ingredients = ingredientsBuilder.toString().trim();
		ingredients = ingredients.substring(0, ingredients.length() - 1);

		if (gender.equalsIgnoreCase("male")) {
			person = new Male(age, weight, height, physicalActivityLevel, ingredients);
		} else if (gender.equalsIgnoreCase("female")) {
			person = new Female(age, weight, height, physicalActivityLevel, ingredients);
		}

		return person;
	}

	// Helper2: throws InvalidProfileException
	public Product validateProductData(String data) throws InvalidProfileException {

		// 1. All Error-type judgments & RuntimeException
		String[] values = data.split(",");
		boolean noProduct = false, Error1 = false, Error2 = false;
		Product dietProduct = null;

		try {
			if (Model.productsMap.get(values[0]) == null) {
				noProduct = false;
			} else {
				noProduct = true;
			}

			if (values[1].trim().length() == 0) {
				Error1 = true;
			} else {
				Error1 = false;
			}

			if (values[2].trim().length() == 0) {
				Error2 = true;
			} else {
				Error2 = false;
			}

		} catch (RuntimeException e) {
			throw new InvalidProfileException(String.format(
					"Cannot read: %s%nThe data must be-String,number,number-for ndb number,serving size, household size",
					data));
		}

		// 2. Throw InvalidProfileExceptions
		if (noProduct) {
			throw new InvalidProfileException(String.format("No product found with this code: %s", values[0]));
		} else if (Error1 || Error2) {
			throw new InvalidProfileException(String.format(
					"Cannot read: %s%nThe data must be-String,number,number-for ndb number,serving size, household size",
					data));
		} else {

			// 3. Create the new Product
			Product productSelected = Model.productsMap.get(values[0].trim());

			dietProduct = new Product(productSelected.getNdbNumber(), productSelected.getProductName(),
					productSelected.getManufacturer(), productSelected.getIngredients());
			dietProduct.setProductNutrients(productSelected.getProductNutrients());
			dietProduct.setServingUom(productSelected.getServingUom());
			dietProduct.setHouseholdUom(productSelected.getHouseholdUom());
			dietProduct.setServingSize(Float.parseFloat(values[1].trim()));
			dietProduct.setHouseholdSize(Float.parseFloat(values[2].trim()));

			return dietProduct;
		}
	}
}
