//Jiahui Cai (jiahuic)
package victoria;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import javafx.scene.control.TextField;
import victoria.NutriProfiler.PhysicalActivityEnum;

public class CSVFiler extends DataFiler {
	/*
	 * to do: implement writeFile() method save profile and diet products data
	 */
	@Override
	public boolean writeFile(String filename) throws InvalidProfileException {
		System.out.println(filename);
		File file = new File(filename);
		String content = null;
		StringBuilder sb = new StringBuilder();
		// populate content's person part using GUI component
		float age = 0, weight = 0, height = 0, physicalActivityLevel = 1;
		String ingredientsToWatch = "";
		TextField textField = NutriByte.view.ageTextField;
		// String gender = NutriByte.view.genderComboBox.valueProperty().get();
		String gender = NutriByte.view.genderComboBox.valueProperty().get();
		age = Float.parseFloat(textField.getText().trim());
		textField = NutriByte.view.weightTextField;
		weight = Float.parseFloat(textField.getText().trim());
		textField = NutriByte.view.heightTextField;
		height = Float.parseFloat(textField.getText().trim());
		if (NutriByte.view.physicalActivityComboBox.getSelectionModel().getSelectedIndex() >= 0) {
			PhysicalActivityEnum[] physicAct = PhysicalActivityEnum.values();
			for (int i = 0; i < physicAct.length; i++) {
				if (physicAct[i].getName() == NutriByte.view.physicalActivityComboBox.valueProperty().get()) {
					physicalActivityLevel = physicAct[i].getPhysicalActivityLevel();
					break;
				}
			}
		}
		sb.append(gender + ",");
		sb.append(age + ",");
		sb.append(weight + ",");
		sb.append(height + ",");
		sb.append(physicalActivityLevel);
		if (NutriByte.view.ingredientsToWatchTextArea.getText() != null) {
			ingredientsToWatch = NutriByte.view.ingredientsToWatchTextArea.getText();
			sb.append("," + ingredientsToWatch);
		}
		if (NutriByte.person.dietProductsList.size() > 0) {
			for (Product p : NutriByte.person.dietProductsList) {
				sb.append("\n" + p.getNdbNumber() + ",");
				sb.append(p.getServingSize() + ",");
				sb.append(p.getHouseholdSize());
			}
		}
		content = sb.toString();
		if (file != null) {
			try {
				// System.out.println("printwriter is invoked");
				PrintWriter writer;
				writer = new PrintWriter(file);
				writer.println(content);
				writer.close();
			} catch (IOException ex) {
				return false;
			}
		}
		return true;

	}

	/*
	 * to do: handle exceptions while reading data from csv file if there is invalid
	 * or missing data read the file and only read the first line to populate the
	 * profile data, and create a Male/Female object the IngredientsToWatch,one
	 * element of profile data is a String storing ingredients separated by comma
	 */
	@Override
	public boolean readFile(String filename) {
		// clear the list and map if a valid profile was created before
		if (NutriByte.person != null) {
			NutriByte.person.dietProductsList.clear();
			NutriByte.person.dietNutrientsMap.clear();
		}
		Scanner sc = null;
		try {
			sc = new Scanner(new FileReader(filename));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			return false;
		}
		StringBuilder input = new StringBuilder();
		while (sc.hasNextLine()) {
			input.append(sc.nextLine() + "\n");
		}
		// System.out.println("input: "+ input);
		String[] inputs = input.toString().split("\n");

		if (inputs.length != 0) {// read the first line for person profile(if existing)
			try {
				if (validatePersonData(inputs[0]) != null) {
					NutriByte.person = validatePersonData(inputs[0]);
					System.out.println("NutriByte.person.phy" + NutriByte.person.physicalActivityLevel);
				} else {
					sc.close();
					return false;
				}
			} catch (InvalidProfileException e) {
				System.out.println(
						"an exception thrown in profile building part, no profile built. See the alert window");
				sc.close();
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
				return false;
			}
		}

		for (int i = 1; i < inputs.length; i++) {// read the rest line for product info
			// System.out.println("inputs[i]"+inputs[i]);
			// System.out.println(validateProductData(inputs[i]).getNdbNumber());
			try {
				if (validateProductData(inputs[i]) != null) {
					NutriByte.person.dietProductsList.add(validateProductData(inputs[i]));
				}
			} catch (InvalidProfileException e) {
				System.out.println("an exception thrown in product part, see the alert window");
			}
		}
		// System.out.println("good until populate");
		NutriByte.person.populateDietNutrientMap();
		sc.close();
		return true;

	}

	/*
	 * to do: helper method take the first string with gender,age,weight,etc. and
	 * checks each value one by one. If some data is missing or invalid, throws
	 * InvalidProfileException with appropriate message
	 */
	Person validatePersonData(String data) throws InvalidProfileException {
		// System.out.println(data);
		String[] contents = data.split(",");
		// System.out.println(contents[0]);
		if (contents[0].length() == 0
				|| !contents[0].equalsIgnoreCase("Female") && !contents[0].equalsIgnoreCase("Male")) {
			throw new InvalidProfileException("The profile must have gender:Female or Male as first word");
		}
		if (contents[1].length() == 0 || !contents[1].trim().matches("^[0-9|.]+$")) {
			throw new InvalidProfileException(
					String.format("Invalid data for Age: %s %nAge must be a number", contents[1]));
		}
		if (contents[2].length() == 0 || !contents[2].trim().matches("^[0-9|.]+$")) {
			throw new InvalidProfileException(
					String.format("Invalid data for Weight: %s %nWeight must be a number", contents[2]));
		}
		if (contents[3].length() == 0 || !contents[3].trim().matches("^[0-9|.]+$")) {
			throw new InvalidProfileException(
					String.format("Invalid data for Height: %s %nHeight must be a number", contents[3]));
		}
		if (contents[4].length() == 0 || !contents[4].trim().matches("^[0-9|.]+$")) {
			throw new InvalidProfileException(String
					.format("Invalid physical activity level: %s %nMust be 1.0, 1.1, 1.25, or 1.48", contents[4]));
		} else if (!contents[4].trim().equals("1.0") && !contents[4].trim().equals("1")
				&& !contents[4].trim().equals("1.1") && !contents[4].trim().equals("1.25")
				&& !contents[4].trim().equals("1.48")) {
			throw new InvalidProfileException(String
					.format("Invalid physical activity level: %s %nMust be 1.0, 1.1, 1.25, or 1.48", contents[4]));
		}
		String gender = contents[0];
		float age = 0, weight = 0, height = 0, physicalActivityLevel = 0;
		age = Float.parseFloat(contents[1].trim());
		weight = Float.parseFloat(contents[2].trim());
		height = Float.parseFloat(contents[3].trim());
		physicalActivityLevel = Float.parseFloat(contents[4].trim());
		// System.out.println(physicalActivityLevel);
		StringBuilder ingredientsToWatch = new StringBuilder();
		for (int i = 5; i < contents.length; i++) {
			ingredientsToWatch.append(contents[i].trim() + ",");
		}

		// remove the last comma in the StringBuilder that store ingredientToWatch
		// input(s)
		if (ingredientsToWatch.length() > 0) {
			ingredientsToWatch.replace(ingredientsToWatch.toString().length() - 1,
					ingredientsToWatch.toString().length(), "");
		}
		switch (gender) {// populate NutriByte.person with an object of it's child class
		// dynamic binding based on the gender data
		case "Female":
			return new Female(age, weight, height, physicalActivityLevel, ingredientsToWatch.toString());
		case "Male":
			return new Male(age, weight, height, physicalActivityLevel, ingredientsToWatch.toString());
		default:
			return null;

		}
	}

	/*
	 * to do: helper method take each product listed after the first line of
	 * person-data check each value one by one if invalid or missing data,skip the
	 * product and reads the next product Note: even if no products are listed, the
	 * person-data,if valid should be displayed
	 */
	Product validateProductData(String data) {
		String[] contents = data.split(",");
		boolean flag1 = true, flag2 = true, flag3 = true;
		Product changedProd = null;
		try {
			flag1 = Model.productsMap.get(contents[0]) == null;// true if the ndbNumber is not in current productsMap
			flag2 = contents[1].length() == 0 || !contents[1].trim().matches("^[0-9|.]+$");
			flag3 = contents[2].length() == 0 || !contents[2].trim().matches("^[0-9|.]+$");
		} catch (RuntimeException e) {
			throw new InvalidProfileException(String.format(
					"Cannot read: %s%nThe data must be-String,number,number-for ndb number,serving size, household size",
					data));
		}
		if (flag1) {
			throw new InvalidProfileException(String.format("No product found with this code: %s", contents[0]));
		} else if (flag2 || flag3) {
			throw new InvalidProfileException(String.format(
					"Cannot read: %s%nThe data must be-String,number,number-for ndb number,serving size, household size",
					data));
		} else {
			String ndb = contents[0];
			Product selectedProd = Model.productsMap.get(ndb);
			changedProd = new Product(selectedProd.getNdbNumber(), selectedProd.getProductName(),
					selectedProd.getManufacturer(), selectedProd.getIngredients());
			changedProd.setProductNutrients(selectedProd.getProductNutrients());
			changedProd.setServingUom(selectedProd.getServingUom());
			changedProd.setHouseholdUom(selectedProd.getHouseholdUom());
			changedProd.setServingSize(Float.parseFloat(contents[1].trim()));
			changedProd.setHouseholdSize(Float.parseFloat(contents[2].trim()));
			// System.out.println("a product is parsed");
			return changedProd;
		}

	}
}
