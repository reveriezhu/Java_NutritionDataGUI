//Jiahui Cai (jiahuic)
package victoria;

public class Male extends Person{
	float[][] nutriConstantsTableMale = new float[][]{
		//AgeGroups: 3M, 6M, 1Y, 3Y, 8Y, 13Y, 18Y, 30Y, 50Y, ABOVE
		{1.52f, 1.52f, 1.2f, 1.05f, 0.95f, 0.95f, 0.73f, 0.8f, 0.8f, 0.8f}, //Protein
		{60, 60, 95, 130, 130, 130, 130, 130, 130, 130}, //Carbohydrate
		{19, 19, 19, 19, 25, 31, 38, 38, 38, 30},       //Fiber
		{36, 36, 32, 21, 16, 17, 15, 14, 14, 14	},  //Histidine
		{88, 88, 43, 28, 22, 22, 21, 19, 19, 19	}, 	//isoleucine
		{156, 156, 93, 63, 49, 49, 47, 42, 42, 42},//leucine
		{107, 107, 89, 58, 46, 46, 43, 38, 38, 38 },//lysine
		{59, 59, 43, 28, 22, 22, 21, 19, 19, 19	}, 	//methionine
		{59, 59, 43, 28, 22, 22, 21, 19, 19, 19	}, 	//cysteine
		{135, 135, 84, 54, 41, 41, 38, 33, 33, 33 },//phenylalanine
		{135, 135, 84, 54, 41, 41, 38, 33, 33, 33 },//tyrosine
		{73, 73, 49, 32, 24, 24, 22, 20, 20, 20}, 	//threonine
		{28, 28, 13, 8, 6, 6, 6, 5, 5, 5}, 			//tryptophan
		{87, 87, 58, 37, 28, 28, 27, 24, 24, 24}  	//valine
	};
    /*non-default constructor that invoke parent's non-default constructor
     *and invoke initializeNutriConstantsTable() to populate nutrient constants table
     *call NutriProfiler.createNutriProfile(Person p) to populate recommendedNutrientsList
     * */
	Male(float age, float weight, float height, float physicalActivityLevel, String ingredientsToWatch) {
		//write your code here
		super(age,weight, height,physicalActivityLevel,ingredientsToWatch);//invoke parent's constructor
		this.age=age;
		this.weight= weight;
		this.height = height;
		this.physicalActivityLevel = physicalActivityLevel;
		this.ingredientsToWatch = ingredientsToWatch;
		initializeNutriConstantsTable();
		NutriProfiler.createNutriProfile(this);
	}
	/*
     * calculate energy requirement for the person(male)
     * different formula and constants are provided
     * based on his age group, gender*/
	@Override
	float calculateEnergyRequirement() {
		//write your code here
		float X=0,A=0,B=0,C=0,D=0;
		float result=0;
		int index = ageGroup.getAgeGroupIndex();
		if(index<=3){
			switch(index){
			case 0 : {X=-75; break;}
			case 1 : {X=44; break;}
			case 2 : {X=78; break;}
			case 3 : {X=80; break;}
			}
			result = 89*weight-X;
		}
		else if(index<=9){
			if(index<=6){
				A=88.5f;
				B=61.9f;
				C=26.7f;
				D=903f;
				result = A-(B*age) + physicalActivityLevel*(C*weight+D*height/100)+20;
			}
			else{
				A=662f;
				B=9.53f;
				C=15.91f;
				D=539.6f;
				result = A-(B*age) + physicalActivityLevel*(C*weight+D*height/100);
			}

		}
		return result;
	}

	@Override
	void initializeNutriConstantsTable() {
		//write your code here
		int len = nutriConstantsTableMale.length;
		nutriConstantsTable=new float[len][];
		int width = 0;
		for(int i = 0;i<len;i++){
			width = nutriConstantsTableMale[i].length;
			nutriConstantsTable[i] = new float[width];
			for(int j = 0 ; j < width;j++){
				nutriConstantsTable[i][j]=nutriConstantsTableMale[i][j];
			}
		}
	}
}
