//AndrewID: yuhaozhu Name: Yuhao Zhu

package hw2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

public class Model {
	Product[] products;
	Nutrient[] nutrients;

	static ObservableMap<String, Product> productsMap = FXCollections.observableHashMap();
	static ObservableMap<String, Nutrient> nutrientsMap = FXCollections.observableHashMap();

	ObservableList<Product> searchResultsList = FXCollections.observableArrayList();

	public void readProducts(String filename) {
		String[] productEntries = fileEntries(filename); // Stores all entries read from the file
		products = new Product[productEntries.length];

		for (int i = 0; i < productEntries.length; i++) {
			products[i] = new Product(productEntries[i].split("\"")[1].trim(), productEntries[i].split("\"")[3].trim(),
					productEntries[i].split("\"")[9].trim(), productEntries[i].split("\"")[15].trim());
			productsMap.put(products[i].getNdbNumber(), products[i]);
		}
	}

	public void readNutrients(String filename) {
		String[] nutrientEntries = fileEntries(filename);
//		productNutrients = new ProductNutrient[nutrientEntries.length];
//
////		First store the productNutrients array (all nutrients from the file), same as above
//		for (int i = 0; i < nutrientEntries.length; i++) {
//			productNutrients[i] = new ProductNutrient(nutrientEntries[i].split("\"")[3].trim(),
//					Float.parseFloat(nutrientEntries[i].split("\"")[9].trim()));
//		}

//		Then store the nutrients array (all uniqiue nutrients), same as above
		StringBuilder nutrientUnique = new StringBuilder();
		boolean isUnique = true;

		for (int i = 0; i < nutrientEntries.length; i++) {
			for (int j = 0; j < i; j++) {
				if (nutrientEntries[i].split("\"")[3].equals(nutrientEntries[j].split("\"")[3])) {
					isUnique = false;
					break; // if found not unique, break this check loop
				} else {
					isUnique = true; // must write this to refresh in case isUnique is set as false in the unique
										// case
				}
			}
			if (isUnique == true) {
				nutrientUnique.append(nutrientEntries[i] + "\n");
			}
		}

		String[] nutrientUniqueEntries = nutrientUnique.toString().split("\n");
		nutrients = new Nutrient[nutrientUniqueEntries.length];

//		Store the unique nutrients
		for (int i = 0; i < nutrientUniqueEntries.length; i++) {
			nutrients[i] = new Nutrient(nutrientUniqueEntries[i].split("\"")[3].trim(),
					nutrientUniqueEntries[i].split("\"")[5].trim(), nutrientUniqueEntries[i].split("\"")[11].trim());
			nutrientsMap.put(nutrients[i].getNutrientCode(), nutrients[i]);
		}
	}

	public void readServingSizes(String filename) {
		String[] sizeEntries = fileEntries(filename);

//		As those four variables are not set in the constructor, we can simply store them in products by iteration
		for (int i = 0; i < products.length; i++) {
			for (int j = 0; j < sizeEntries.length; j++) {
				if (products[i].getNdbNumber().equals(sizeEntries[j].split("\"")[1])) { // In order to find the
																						// correspondent products[i]
					products[i].setServingSize(Float.parseFloat(sizeEntries[j].split("\"")[3]));
					products[i].setServingUom(sizeEntries[j].split("\"")[5]);
					products[i].setHouseholdSize(Float.parseFloat(sizeEntries[j].split("\"")[7]));
					products[i].setHouseholdUom(sizeEntries[j].split("\"")[9]);
					break; // break to ensure uniqueness
				}
			}
		}
	}

//	0. Read files and store all entries (each row) - Apply to all three required methods
	String[] fileEntries(String filename) {
		StringBuilder fileContent = new StringBuilder();
		Scanner fileScanner = null;

		try {
			fileScanner = new Scanner(new File(filename));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		fileScanner.nextLine();

		while (fileScanner.useDelimiter("\n").hasNext()) {
			fileContent.append(fileScanner.next() + "\n");
		}
		fileScanner.close();
		return fileContent.toString().split("\n");
	}

	public boolean readProfile(String filename) {
		CSVFiler cf = new CSVFiler();
		XMLFiler xf = new XMLFiler();
		if (filename.contains("csv")) {
			cf.readFile(filename);
		}
		if (filename.contains("xml")) {
			xf.readFile(filename);
		}
		return false;
	}

	public void writeProfile() {

	}

}