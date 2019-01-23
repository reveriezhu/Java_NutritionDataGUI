//Jiahui Cai (jiahuic)
package victoria;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import victoria.NutriProfiler.AgeGroupEnum;

public abstract class Person {

	float age, weight, height, physicalActivityLevel; //age in years, weight in kg, height in cm
	String ingredientsToWatch;
	float[][] nutriConstantsTable = new float[NutriProfiler.RECOMMENDED_NUTRI_COUNT][NutriProfiler.AGE_GROUP_COUNT];
	ObservableList<RecommendedNutrient> recommendedNutrientsList = FXCollections.observableArrayList();
	ObservableList<Product> dietProductsList =FXCollections.observableArrayList();
	ObservableMap<String,RecommendedNutrient> dietNutrientsMap = FXCollections.observableHashMap();
	NutriProfiler.AgeGroupEnum ageGroup;

	abstract void initializeNutriConstantsTable();
	abstract float calculateEnergyRequirement();
	/*to do:
	 * take the data from dietProductList populated in CSVFiler or AddDietButtonHandler
	 * and populates dietNutrientsMap
	 * */
	void populateDietNutrientMap(){
		Product selectedProd = null;
		NutriByte.person.dietNutrientsMap.clear();
		for(Product changedProd : dietProductsList){
            selectedProd= Model.productsMap.get(changedProd.getNdbNumber());
            float servQ = changedProd.getServingSize();
			for(String nutCod: selectedProd.getProductNutrients().keySet()){
				if(NutriByte.person.dietNutrientsMap.get(nutCod)==null){
					Float q =selectedProd.getProductNutrients().get(nutCod).getNutrientQuantity()*servQ/100;
					NutriByte.person.dietNutrientsMap.put(nutCod,new RecommendedNutrient(nutCod,q));
				}else{
					Float q = NutriByte.person.dietNutrientsMap.get(nutCod).getNutrientQuantity()
							+selectedProd.getProductNutrients().get(nutCod).getNutrientQuantity()*servQ/100;
					NutriByte.person.dietNutrientsMap.get(nutCod).setNutrientQuantity(q);
				}
			}
		}

	}
	/*a non-default constructor of Person
	 * initialize age, weight, height, physicalActivityLevel ingredientsToWatch
	 * and initialize the ageGroup,which is of type NutriProfiler.AgeGroupEnum
	 * by comparing the age with the max age of each age group  */
	Person(float age, float weight, float height, float physicalActivityLevel, String ingredientsToWatch) {
		this.age=age;
		this.weight=weight;
		this.height=height;
		this.physicalActivityLevel=physicalActivityLevel;
		this.ingredientsToWatch=ingredientsToWatch;
		//initialize ageGroup? find the ageGroup that this person belongs to
		AgeGroupEnum[] ages = AgeGroupEnum.values();
		//get the ageGroup index
		int index = 0;//the index will retain 0 if the person's age is smaller than any of the max age of ageGroups
		for(AgeGroupEnum agG : AgeGroupEnum.values()){
			//if the person belongs to second or higher age group,
			//the boolean value that's evaluated in the "if" expression
			//will become true when the loop reach the right group
			if(this.age<=agG.getAge()){
				index = agG.getAgeGroupIndex();
				break;
			}
		}
		if(index<ages.length) ageGroup =ages[index];
	}

	//returns an array of nutrient values of size NutriProfiler.RECOMMENDED_NUTRI_COUNT.
	//Each value is calculated as follows:
	//For Protein, it multiples the constant with the person's weight.
	//For Carb and Fiber, it simply takes the constant from the
	//nutriConstantsTable based on NutriEnums' nutriIndex and the person's ageGroup
	//For others, it multiples the constant with the person's weight and divides by 1000.
	//Try not to use any literals or hard-coded values for age group, nutrient name, array-index, etc.

	float[] calculateNutriRequirement() {
		//write your code here
		float[] req =new float[NutriProfiler.RECOMMENDED_NUTRI_COUNT];
		if(ageGroup==null){
			//if the age of this person exceed 150
			//System.out.println("invalid age input");
		}
		else{
			int index = ageGroup.getAgeGroupIndex();
			//get constants of each ingredient for the person based on it's ageGroup
			float[] Constants = new float[NutriProfiler.RECOMMENDED_NUTRI_COUNT];
			for (int i= 0;i<Constants.length;i++){// the integer i stands for nutriIndex here
				//populate constants for this person based on ageGroup(index) and nutriIndex(i)
				Constants[i]=nutriConstantsTable[i][index];
				//get nutriRequirement based on the constants;
				if(i==0){
					req[i] = Constants[i]*weight;
				}
				else if(i==1||i==2){
					req[i] = Constants[i];
				}
				else{
					req[i] = Constants[i]*weight/1000;
				}
			}
		}

		return req;
	}
}
