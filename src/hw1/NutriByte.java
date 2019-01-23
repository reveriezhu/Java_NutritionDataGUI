//AndrewID: yuhaozhu Name: Yuhao Zhu

package hw1;

import java.util.Scanner;

public class NutriByte {
	Model model = new Model(); // will handle all data read from the data files
	Scanner input = new Scanner(System.in); // to be used for all console i/o in this class

	static final String PRODUCT_FILE = "data/SampleProducts.csv";
	static final String NUTRIENT_FILE = "data/SampleNutrients.csv";
	static final String SERVING_SIZE_FILE = "data/SampleServingSize.csv";

	// do not change this method
	public static void main(String[] args) {
		NutriByte nutriByte = new NutriByte();
		nutriByte.model.readProducts(PRODUCT_FILE);
		nutriByte.model.readNutrients(NUTRIENT_FILE);
		nutriByte.model.readServingSizes(SERVING_SIZE_FILE);
		switch (nutriByte.getMenuChoice()) {
		case 1: {
			nutriByte.printSearchResults(
					nutriByte.searchProductsWithSelectedIngredients(nutriByte.getIngredientChoice()));
			break;
		}
		case 2: {
			int nutrientChoice = nutriByte.getNutrientChoice();
			nutriByte.printSearchResults(nutriByte.searchProductsWithSelectedNutrient(nutrientChoice), nutrientChoice);
			break;
		}
		case 3:
		default:
			System.out.println("Good Bye!");
			break;
		}
	}

	// do not change this method
	int getMenuChoice() {
		System.out.println("*** Welcome to NutriByte ***");
		System.out.println("--------------------------------------------------");
		System.out.println("1. Find ingredient(s)");
		System.out.println("2. Find a nutrient");
		System.out.println("3. Exit");
		input = new Scanner(System.in);
		return input.nextInt();
	}

	// do not change this method
	String getIngredientChoice() {
		input.nextLine(); // Because getMenuChoice() invokes input.nextInt(), the rest of line will be
							// read if this invokes
		System.out.println("Type the keywords to search for the ingredients");
		System.out.println("--------------------------------------------------");
		return input.nextLine();
	}

//	Uses the nutrients[] array in model to display the list of nutrients in three columns.
//	Returns the choice number selected by the user. (see Figure 5)
	int getNutrientChoice() {
		System.out.println("Select the nutrient you are looking for");
		System.out.println("--------------------------------------------------");
		for (int i = 0; i < model.nutrients.length; i++) {
			if (i > 0 && i % 3 == 0) {
				System.out.printf("%n");
			}
			System.out.printf("%-2d. %-35s", i + 1, model.nutrients[i].nutrientName);
		}
		System.out.printf("%n--------------------------------------------------%n");
		return input.nextInt(); // q: Did not consider Input Errors
	}

//	Takes a search string as parameter and searches case-insensitively
//	for products that have the ingredients matching the words in the search
//	string (see Figure 2, Figure 3, and Figure 4). Returns the search result
//	as an array of products that have the ingredient(s). If no match is found,
//	returns an array with size 0 (not null)
	Product[] searchProductsWithSelectedIngredients(String searchString) {
		Product[] searchResults;
		boolean hasKeyword;
		StringBuilder countArray = new StringBuilder(); // to store which -th item in the array is found
		String[] keywordSet = searchString.split(" "); // consider multiple search items in a searchString

		for (int i = 0; i < model.products.length; i++) { // iterate all products items
			for (String searchKey : keywordSet) { // first iterate the multiple search items in a searchString
				hasKeyword = false; // set hasKeyword as false since the last true item will intefere with the next
									// one
				Scanner scanner = new Scanner(model.products[i].ingredients);
				Scanner productIngredients = scanner.useDelimiter(",");
				// Consider the multiple ingredients in a single ingredients variable

				// One keyword found in all levels of loops, break the entire loop instantly
				while (productIngredients.hasNext()) { // then iterate the multiple keyword items in an ingredients
					Scanner keywordScan = new Scanner(productIngredients.next());
					while (keywordScan.hasNext()) {
						String keyword = keywordScan.next().trim();
						if (keyword.toLowerCase().contains(searchKey.toLowerCase())) {
//							"[searchString]", "(searchString)",
//							"[searchString", "searchString]",
//							"(searchString", "searchString)"
							// q: milk strings?? "buttermilk"
							// q: directly check the countArray.toString to avoid duplicates: error
							// q: StringBuilder .toString() what about null or ""
							countArray.append(i + " ");
							hasKeyword = true;
							break; // break to ensure uniqueness
						}
					}
					keywordScan.close();
					if (hasKeyword == true) {
						break; // break to ensure uniqueness
					}
				}
				if (hasKeyword == true) {
					break; // break to ensure uniqueness
				}
				productIngredients.close();
				scanner.close();
			}
		}

		// return Product[0] if searchString not found
		if (countArray.toString().equals(null) || countArray.toString().equals("")) {
			return new Product[0];
		} else {
			String[] countStr = countArray.toString().split(" ");
			searchResults = new Product[countStr.length];
			for (int i = 0; i < countStr.length; i++) {
				searchResults[i] = model.products[Integer.parseInt(countStr[i])]; // to store which -th item in the
																					// array is found
			}
			return searchResults;
		}
	}

//	Returns an array of Products that have the selected nutrient as per the menu choice.
	Product[] searchProductsWithSelectedNutrient(int menuChoice) {
		Product[] searchResults;
		StringBuilder countArray = new StringBuilder(); // to store which -th item in the products array is found

		for (int i = 0; i < model.productNutrients.length; i++) {
			if (model.productNutrients[i].nutrientCode.equals(model.nutrients[menuChoice - 1].nutrientCode)) { // to
																												// find
																												// the
																												// nutrientChoice
																												// in
																												// the
																												// productNutrients
				if (model.productNutrients[i].quantity.equals(0f)) {
					continue; // skip this 0 item
				}
				for (int j = 0; j < model.products.length; j++) { // to find the ndbNumber that matches the
																	// productNutrients and searchResults
					if (model.productNutrients[i].ndbNumber.equals(model.products[j].ndbNumber)) {
						countArray.append(j + " "); // to store which -th item in the products array is found
					}
				}
			}
		}

		// return Product[0] if menuChoice not found
		if (countArray.toString().equals(null) || countArray.toString().equals("")) {
			return new Product[0];
		} else {
			String[] countStr = countArray.toString().split(" ");
			searchResults = new Product[countStr.length];
			for (int i = 0; i < countStr.length; i++) {
				searchResults[i] = model.products[Integer.parseInt(countStr[i])]; // store the -th item found in
																					// products
			}
			return searchResults;
		}
	}

//	This is an overloaded method. Prints the output as shown in Figure 2 ,
//	Figure 3, Figure 4, Figure 5. When a single parameter of type Product[]
//	array is passed, it means that the ingredient search results are to be printed.
//	When a Product[] array along with an int x is passed, it means that the nutrient
//	search results are to be printed for the nutrient at index x in the nutrients array.
	void printSearchResults(Product[] searchResults) {
		System.out.printf("*** %d products found ***%n", searchResults.length);

		// print in format as required
		for (int i = 0; i < searchResults.length; i++) {
			System.out.printf("%d. %s from %s%n", i + 1, searchResults[i].productName, searchResults[i].manufacturer);
		}
	}

	// q: print formats are slightly different between these two methods
	void printSearchResults(Product[] searchResults, int nutrientChoice) {
		System.out.printf("*** %d products found ***%n", searchResults.length);
		int[] nutrientSequence = new int[searchResults.length];

//		match the searchResults with the productNutrients variables that include the nutrientChoice
		for (int i = 0; i < model.productNutrients.length; i++) {
			if (model.productNutrients[i].nutrientCode.equals(model.nutrients[nutrientChoice - 1].nutrientCode)) {
				for (int j = 0; j < searchResults.length; j++) {
					if (model.productNutrients[i].ndbNumber.equals(searchResults[j].ndbNumber)) { // then find the
																									// ndbNumber that
																									// matches the
																									// productNutrients
																									// and searchResults
						nutrientSequence[j] = i;
						break; // break to ensure uniqueness
					}
				}
			}
		}

		// print in format as required
		for (int i = 0; i < searchResults.length; i++) {
			System.out.printf("%2d. %.2f %s of %s: %s has %.2f %s of %s%n", i + 1, searchResults[i].householdSize,
					searchResults[i].householdUom, searchResults[i].ndbNumber, searchResults[i].productName,
					model.productNutrients[nutrientSequence[i]].quantity * searchResults[i].householdSize
							* searchResults[i].servingSize * 0.02,
					model.productNutrients[nutrientSequence[i]].nutrientUom,
					model.nutrients[nutrientChoice - 1].nutrientName);
		}
	}
}