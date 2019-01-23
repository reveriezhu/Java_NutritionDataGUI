//AndrewID: yuhaozhu Name: Yuhao Zhu

package hw3;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

public class Model {

	static ObservableMap<String, Product> productsMap = FXCollections.observableHashMap();
	static ObservableMap<String, Nutrient> nutrientsMap = FXCollections.observableHashMap();
	ObservableList<Product> searchResultsList = FXCollections.observableArrayList();

	// I. Populate the productsMap
	public void readProducts(String filename) {
		String[] productEntries = fileEntries(filename); // Stores all entries read from the file

		for (String productEntry : productEntries) {

			String ndbNumber = productEntry.split("\",\"")[0].replace("\"", "");
			String productName = productEntry.split("\",\"")[1];
			String manufaturer = productEntry.split("\",\"")[4];
			String ingredients = productEntry.split("\",\"")[7].replace("\"", "");

			productsMap.put(ndbNumber, new Product(ndbNumber, productName, manufaturer, ingredients));
		}
	}

	// II. Store ProductNutrients of Products in the productsMap and Populate the
	// nutrientsMap
	public void readNutrients(String filename) {
		String[] nutrientEntries = fileEntries(filename);

		for (String nutrientEntry : nutrientEntries) {

			String nutrientCode = nutrientEntry.split("\",\"")[1];
			String nutrientName = nutrientEntry.split("\",\"")[2];
			String nutrientUom = nutrientEntry.split("\",\"")[5].replace("\"", "");
			String ndbNumber = nutrientEntry.split("\",\"")[0].replace("\"", "");
			float quantity = 0;
			if (nutrientEntry.split("\",\"")[4].length() > 0) {
				quantity = Float.parseFloat(nutrientEntry.split("\",\"")[4].trim());
			}

			// 1. Populate the ProductNutrients of Products in the productsMap
			if (quantity != 0) {
				productsMap.get(ndbNumber).getProductNutrients().put(nutrientCode,
						productsMap.get(ndbNumber).new ProductNutrient(nutrientCode, quantity));
			}

			// 2. Populate the nutrientsMap
			if (!nutrientsMap.containsKey(nutrientCode)) {
				nutrientsMap.put(nutrientCode, new Nutrient(nutrientCode, nutrientName, nutrientUom));
			}
		}
	}

	// II. Store ServingSizes of Products in the productsMap
	public void readServingSizes(String filename) {
		String[] sizeEntries = fileEntries(filename);

		for (String sizeEntry : sizeEntries) {

			String ndbNumber = sizeEntry.split("\",\"")[0].replace("\"", "").trim();
			float servingSize = 0;
			if (sizeEntry.split("\",\"")[1].length() > 0) {
				servingSize = Float.parseFloat(sizeEntry.split("\",\"")[1].trim());
			}
			String servingUom = sizeEntry.split("\",\"")[2].trim();
			float householdSize = 0;
			if (sizeEntry.split("\",\"")[3].length() > 0) {
				householdSize = Float.parseFloat(sizeEntry.split("\",\"")[3].trim());
			}
			String householdUom = sizeEntry.split("\",\"")[4].replace("\"", "").trim();

			productsMap.get(ndbNumber).setServingSize(servingSize);
			productsMap.get(ndbNumber).setServingUom(servingUom);
			productsMap.get(ndbNumber).setHouseholdSize(householdSize);
			productsMap.get(ndbNumber).setHouseholdUom(householdUom);
		}
	}

	// O. Helper: Read files and store all entries (each row) - Apply to all three
	// required methods
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

	public void writeProfile(String filename) {
		CSVFiler cf = new CSVFiler();
		cf.writeFile(filename);
	}

}