//Jiahui Cai (jiahuic)
package victoria;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

public class Model {
	//key: ndbNumber, Value: Product object
	static ObservableMap<String,Product> productsMap = FXCollections.observableHashMap();
	//key: nutrientCode, Value: Nutrient object
	static ObservableMap<String,Nutrient> nutrientsMap = FXCollections.observableHashMap();
	ObservableList<Product>searchResultsList = FXCollections.observableArrayList();
	/*to do:
	 *allows user to save profile data and diet products in a new profile file as csv
	 * */
	boolean writeProfile(String filename){
		String[] fileExtensions = {"csv","xml"};
		String extension = filename.substring(filename.length()-3);
		DataFiler file = null;
		//based on selected file
		if (extension.equalsIgnoreCase(fileExtensions[0])) {
			file = new CSVFiler();
			return file.writeFile(filename);
		}
		return false;
	}
	/*
	 * read the file by line
	 * instantiate a product object for each valid line
	 * and populate the productsMap with product object*/
	void readProducts(String filename){
		Scanner input1;
		try {
			input1 = new Scanner(new FileReader(filename));
			input1.nextLine();//skip the first line
			while(input1.hasNextLine()){
				String line = input1.nextLine();
				String ndbNumber = (line.split("\",\"")[0].replace("\"", ""));//delete the first double quote;
				String productName = (line.split("\",\"")[1]);
				String manufaturer = (line.split("\",\"")[4]);
				String ingredients = (line.split("\",\"")[7].replace("\"", ""));//delete the last double quote;
				productsMap.put(ndbNumber,new Product(ndbNumber,productName,manufaturer,ingredients));
			}
			input1.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}


	}
	/*
	 * read the file by line
	 * instantiate a nutrient object for each unique nutrientCode
	 * populate the nutrientsMap with nutrient object
	 * populate the productNutrient Map with nutrient object if the quantity of nutrient is not 0 */
	void readNutrients(String filename){
		Scanner input2;
		try {
			input2 = new Scanner(new FileReader(filename));
			input2.nextLine();//skip the first line
			String line = null;
			while(input2.hasNextLine()){
				line = input2.nextLine();
				//the loop is used to
				//(1) populate ProductNutrient
				//(2) populate nutrientsMap
				String nutrientCode = line.split("\",\"")[1];
				String nutrientName = line.split("\",\"")[2];
				String nutrientUom = line.split("\",\"")[5].replace("\"", "");
				String ndbNumber = line.split("\",\"")[0].replace("\"", "");
				float quantity = 0;
				if(line.split("\",\"")[4].length()>0){
					quantity = Float.parseFloat(line.split("\",\"")[4].trim());
				}
				//----(1)----
				// if the quantity value stored in source file is 0,jump to next line
				// to populate productNutrients,a hash map
				if(quantity!=0){
					productsMap.get(ndbNumber).getProductNutrients().put(nutrientCode,productsMap.get(ndbNumber).new ProductNutrient(nutrientCode,quantity));
				}
				//----(2)----
				// add a new Nutrient object and its key value if the nutrientsMap does not contain the key
				if(!nutrientsMap.containsKey(nutrientCode)){
					nutrientsMap.put(nutrientCode,new Nutrient(nutrientCode, nutrientName, nutrientUom));
				}
			}

			input2.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	/*populate serving sizes fields of product objects in the productsMap
	 * match the inputing fields with products by ndbNumber */
	void readServingSizes(String filename){
		Scanner input3;
		try {
			input3 =new Scanner(new FileReader(filename));
			input3.nextLine();//skip the first line
			while(input3.hasNextLine()){
				String line = input3.nextLine();
				String ndbNumber = line.split("\",\"")[0].replace("\"", "").trim();
				float servingSize = 0;
				if(line.split("\",\"")[1].length()>0){
					servingSize= Float.parseFloat(line.split("\",\"")[1].trim());
				}
				String servingUom = line.split("\",\"")[2].trim();
				float householdSize = 0;
				if(line.split("\",\"")[3].length()>0){
					householdSize=Float.parseFloat(line.split("\",\"")[3].trim());
				}
				String householdUom= line.split("\",\"")[4].replace("\"", "").trim();
				productsMap.get(ndbNumber).setServingSize(servingSize);
				productsMap.get(ndbNumber).setServingUom(servingUom);
				productsMap.get(ndbNumber).setHouseholdSize(householdSize);
				productsMap.get(ndbNumber).setHouseholdUom(householdUom);
			}
			input3.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}
	/*get the filename and a substring of it ,
	 * to get the extension
	 * based on the extension, instantiate a child class of DataFiler
	 * return true if childclassobject.readFile() successfully read file and return true*/
	boolean readProfiles(String filename){
		String[] fileExtensions = {"csv","xml"};
		String extension = filename.substring(filename.length()-3);
		DataFiler file = null;
		//based on selected file
		if (extension.equalsIgnoreCase(fileExtensions[0])) {
			file = new CSVFiler();
			return file.readFile(filename);
		} else if (extension.equalsIgnoreCase(fileExtensions[1])) {
			file = new XMLFiler();
			return file.readFile(filename);
		}

		return false;
	}
}
