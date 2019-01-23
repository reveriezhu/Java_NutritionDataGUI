//AndrewID: yuhaozhu Name: Yuhao Zhu

package hw2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class CSVFiler extends DataFiler {

	@Override
	public void writeFile(String filename) {

	}

	@Override
	public boolean readFile(String filename) {
		try (Scanner s = new Scanner(new BufferedReader(new FileReader(filename)))) {
			String profileLine = null;
			while ((profileLine = s.nextLine()) != null) {
				String[] values = profileLine.split(",");
				StringBuilder ingredients = new StringBuilder();

				for (int i = 5; i < values.length; i++) {
					ingredients.append(values[i]);
				}

				if (values[0].trim().equalsIgnoreCase("male")) {
					NutriByte.person = new Male(Float.parseFloat(values[1].trim()), Float.parseFloat(values[2].trim()),
							Float.parseFloat(values[3].trim()), Float.parseFloat(values[4].trim()),
							ingredients.toString().trim());
				} else if (values[0].trim().equalsIgnoreCase("female")) {
					NutriByte.person = new Female(Float.parseFloat(values[1].trim()),
							Float.parseFloat(values[2].trim()), Float.parseFloat(values[3].trim()),
							Float.parseFloat(values[4].trim()), ingredients.toString().trim());
				} else {
					return false;
				}
				return true;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}

}
