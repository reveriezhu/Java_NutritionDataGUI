//AndrewID: yuhaozhu Name: Yuhao Zhu

package hw1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Model {
	Product[] products;
	Nutrient[] nutrients;
	ProductNutrient[] productNutrients;
	
	
//	1. Reads NutriByte.PRODUCT_FILE file to load product objects in 
//	the products array
	void readProducts(String filename) {
		String[] productEntries = fileEntries(filename);  	// Stores all entries read from the file
		products = new Product[productEntries.length];
		
//		Split each entry using \" instead of "," since the entries are stored as "AAA", "BBB", "CCC"
//		We can simply skip the "," part using the array sequence
		for(int i=0; i<productEntries.length; i++) 
			products[i] = new Product(productEntries[i].split("\"")[1].trim(),
				productEntries[i].split("\"")[3].trim(),
				productEntries[i].split("\"")[9].trim(),
				productEntries[i].split("\"")[15].trim());
	}

	
//	2. Reads NutriByte.NUTRIENT_FILE to load objects in the nutrients 
//	and productNutrients arrays. Note that the nutrients array will 
//	hold only unique nutrient objects, stored in the order in which 
//	they are read from the file
	void readNutrients(String filename) {
		String[] nutrientEntries = fileEntries(filename);
		productNutrients = new ProductNutrient[nutrientEntries.length];
		
//		First store the productNutrients array (all nutrients from the file), same as above
		for(int i=0; i<nutrientEntries.length; i++) 
			productNutrients[i] = new ProductNutrient(nutrientEntries[i].split("\"")[1].trim(),
					nutrientEntries[i].split("\"")[3].trim(),
					nutrientEntries[i].split("\"")[5].trim(),
					Float.parseFloat(nutrientEntries[i].split("\"")[9].trim()),
					nutrientEntries[i].split("\"")[11].trim());
		
		
//		Then store the nutrients array (all uniqiue nutrients), same as above
		StringBuilder nutrientUnique = new StringBuilder();
		boolean isUnique = true;
		
		//q: why search first for the array length and search again to store the array
		for(int i=0;i<nutrientEntries.length;i++) {
			for(int j=0;j<i;j++) 	//Check loop: iterate all unique nutrient entries stored before this current one to make sure the uniqueness
				if(nutrientEntries[i].split("\"")[3].equals(nutrientEntries[j].split("\"")[3])) {
					isUnique = false;
					break;	//if found not unique, break this check loop
				}
				else isUnique = true;	//must write this to refresh in case isUnique is set as false in the unique case
			if(isUnique == true)
				nutrientUnique.append(nutrientEntries[i] + "\n");
		}
		
		String[] nutrientUniqueEntries = nutrientUnique.toString().split("\n");  	
		nutrients = new Nutrient[nutrientUniqueEntries.length];
		
//		Store the unique nutrients
		for(int i=0; i<nutrientUniqueEntries.length; i++) 
			nutrients[i] = new Nutrient(nutrientUniqueEntries[i].split("\"")[3].trim(),
				nutrientUniqueEntries[i].split("\"")[5].trim(),
				nutrientUniqueEntries[i].split("\"")[11].trim());
	}
	
	
//	3. Reads NutriByte.SERVING_SIZE_FILE to populate four fields – 
//	servingSize, servingUom, householdSize, householdUom - 
//	in each product object in the products array.
	void readServingSizes(String filename) {
		String[] sizeEntries = fileEntries(filename);  	
		
//		As those four variables are not set in the constructor, we can simply store them in products by iteration
		for(int i=0; i<products.length; i++) 
			for(int j=0;j<sizeEntries.length;j++) 
				if(products[i].getNdbNumber().equals(sizeEntries[j].split("\"")[1])) {	//In order to find the correspondent products[i]
					products[i].setServingSize(Float.parseFloat(sizeEntries[j].split("\"")[3]));
					products[i].setServingUom(sizeEntries[j].split("\"")[5]);
					products[i].setHouseholdSize(Float.parseFloat(sizeEntries[j].split("\"")[7]));
					products[i].setHouseholdUom(sizeEntries[j].split("\"")[9]);
					break;	//break to ensure uniqueness
				}
	}
	
	
//	0. Read files and store all entries (each row) - Apply to all three required methods
	String[] fileEntries(String filename){
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
}