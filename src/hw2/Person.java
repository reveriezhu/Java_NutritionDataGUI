//AndrewID: yuhaozhu Name: Yuhao Zhu

package hw2;

import hw2.NutriProfiler.AgeGroupEnum;
import hw2.NutriProfiler.NutriEnum;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public abstract class Person {

	float age, weight, height, physicalActivityLevel; // age in years, weight in kg, height in cm
	String ingredientsToWatch;
	float[][] nutriConstantsTable = new float[NutriProfiler.RECOMMENDED_NUTRI_COUNT][NutriProfiler.AGE_GROUP_COUNT];

	ObservableList<RecommendedNutrient> recommendedNutrientsList = FXCollections.observableArrayList();

	AgeGroupEnum ageGroup;

	abstract void initializeNutriConstantsTable();

	abstract float calculateEnergyRequirement();

	public Person(float age, float weight, float height, float physicalActivityLevel, String ingredientsToWatch) {
		this.age = age;
		this.weight = weight;
		this.height = height;
		this.physicalActivityLevel = physicalActivityLevel;
		this.ingredientsToWatch = ingredientsToWatch;

		int ageIndex = 0;
		for (int i = 0; i < NutriProfiler.AGE_GROUP_COUNT - 1; i++) {
			if (age <= AgeGroupEnum.values()[i].getAge() && age > AgeGroupEnum.values()[i - 1].getAge()) {
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

}
