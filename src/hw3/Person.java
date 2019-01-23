//AndrewID: yuhaozhu Name: Yuhao Zhu

package hw3;

import java.util.HashMap;
import java.util.Map;

import hw3.NutriProfiler.AgeGroupEnum;
import hw3.NutriProfiler.NutriEnum;
import hw3.Product.ProductNutrient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

public abstract class Person {

	float age, weight, height, physicalActivityLevel; // age in years, weight in kg, height in cm
	String ingredientsToWatch;
	float[][] nutriConstantsTable = new float[NutriProfiler.RECOMMENDED_NUTRI_COUNT][NutriProfiler.AGE_GROUP_COUNT];

	ObservableList<RecommendedNutrient> recommendedNutrientsList = FXCollections.observableArrayList();
	ObservableList<Product> dietProductsList = FXCollections.observableArrayList();
	ObservableMap<String, RecommendedNutrient> dietNutrientsMap = FXCollections.observableHashMap();

	AgeGroupEnum ageGroup;

	NutriChart nutriChart = new NutriChart();

	abstract void initializeNutriConstantsTable();

	abstract float calculateEnergyRequirement();

	public Person(float age, float weight, float height, float physicalActivityLevel, String ingredientsToWatch) {
		this.age = age;
		this.weight = weight;
		this.height = height;
		this.physicalActivityLevel = physicalActivityLevel;
		this.ingredientsToWatch = ingredientsToWatch;

		int ageIndex = 0;
		for (int i = 0; i < NutriProfiler.AGE_GROUP_COUNT; i++) {
			if (age <= AgeGroupEnum.values()[i].getAge()) {
				ageIndex = i;
				break;
			}
		}
		ageGroup = AgeGroupEnum.values()[ageIndex];
	}

	// returns an array of nutrient values of size
	// NutriProfiler.RECOMMENDED_NUTRI_COUNT.
	// Each value is calculated as follows:
	// For Protein, it multiples the constant with the person's weight.
	// For Carb and Fiber, it simply takes the constant from the
	// nutriConstantsTable based on NutriEnums' nutriIndex and the person's ageGroup
	// For others, it multiples the constant with the person's weight and divides by
	// 1000.
	// Try not to use any literals or hard-coded values for age group, nutrient
	// name, array-index, etc.
	float[] calculateNutriRequirement() {
		if (ageGroup != null) {
			float[] nutriRequirement = new float[NutriProfiler.RECOMMENDED_NUTRI_COUNT];
			int ageIndex = ageGroup.getAgeGroupIndex();

			for (int i = 0; i < NutriProfiler.RECOMMENDED_NUTRI_COUNT; i++) {
				nutriRequirement[i] = nutriConstantsTable[i][ageIndex] * weight / 1000;
			}
			nutriRequirement[NutriEnum.PROTEIN
					.getNutriIndex()] = nutriConstantsTable[NutriEnum.PROTEIN.getNutriIndex()][ageIndex] * weight;
			nutriRequirement[NutriEnum.CARBOHYDRATE
					.getNutriIndex()] = nutriConstantsTable[NutriEnum.CARBOHYDRATE.getNutriIndex()][ageIndex];
			nutriRequirement[NutriEnum.FIBER
					.getNutriIndex()] = nutriConstantsTable[NutriEnum.FIBER.getNutriIndex()][ageIndex];

			return nutriRequirement;
		}
		return null;
	}

	public void populateDietNutrientsMap() {
		Map<String, Float> dietNutrients = new HashMap<>();

		for (Product dietProduct : dietProductsList) {
			for (ProductNutrient productNutrient : dietProduct.getProductNutrients().values()) {
				if (!dietNutrients.containsKey(productNutrient.getNutrientCode())) {
					dietNutrients.put(productNutrient.getNutrientCode(),
							productNutrient.getNutrientQuantity() * dietProduct.getServingSize() / 100);
				} else {
					float oldValue = dietNutrients.get(productNutrient.getNutrientCode());
					dietNutrients.put(productNutrient.getNutrientCode(),
							oldValue + productNutrient.getNutrientQuantity() * dietProduct.getServingSize() / 100);
				}
			}
		}

		dietNutrientsMap.clear();

		for (Map.Entry<String, Float> dietEntry : dietNutrients.entrySet()) {
			RecommendedNutrient rn = new RecommendedNutrient(dietEntry.getKey(), dietEntry.getValue());
			dietNutrientsMap.put(dietEntry.getKey(), rn);
		}
	}
}
