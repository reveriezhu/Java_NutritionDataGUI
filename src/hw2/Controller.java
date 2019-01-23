//AndrewID: yuhaozhu Name: Yuhao Zhu

package hw2;

import java.io.File;

import hw2.NutriProfiler.PhysicalActivityEnum;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class Controller {

	class RecommendNutrientsButtonHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			String[] values = new String[6];
			values[0] = NutriByte.view.genderComboBox.getSelectionModel().getSelectedItem();
			values[1] = NutriByte.view.ageTextField.getText();
			values[2] = NutriByte.view.weightTextField.getText();
			values[3] = NutriByte.view.heightTextField.getText();
			values[4] = NutriByte.view.physicalActivityComboBox.getSelectionModel().getSelectedItem();
			values[5] = NutriByte.view.ingredientsToWatchTextArea.getText();

			for (PhysicalActivityEnum physicalActivity : PhysicalActivityEnum.values()) {
				if (values[4].trim().equalsIgnoreCase(physicalActivity.getName())) {
					values[4] = String.valueOf(physicalActivity.getPhysicalActivityLevel());
				}
			}

			if (values[0].trim().equalsIgnoreCase("male")) {
				NutriByte.person = new Male(Float.parseFloat(values[1].trim()), Float.parseFloat(values[2].trim()),
						Float.parseFloat(values[3].trim()), Float.parseFloat(values[4].trim()), values[5].trim());
			}
			if (values[0].trim().equalsIgnoreCase("female")) {
				NutriByte.person = new Female(Float.parseFloat(values[1].trim()), Float.parseFloat(values[2].trim()),
						Float.parseFloat(values[3].trim()), Float.parseFloat(values[4].trim()), values[5].trim());
			}

			NutriProfiler.createNutriProfile(NutriByte.person);
		}
	}

	class OpenMenuItemHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Select file");
			fileChooser.setInitialDirectory(new File("/Users/ReverieHome/CharlieZYH-Cloud/eclipse-workspace/HW")); // local
																													// path
			fileChooser.getExtensionFilters().addAll(new ExtensionFilter("CSV Files", "*.csv"),
					new ExtensionFilter("XML Files", "*.xml"));
			File file = null;
			Stage mainStage = new Stage();

			if ((file = fileChooser.showOpenDialog(mainStage)) != null) {
				NutriByte.model.readProfile(file.getAbsolutePath());
				NutriByte.view.ageTextField.setText(String.format("%.2f", NutriByte.person.age));
				NutriByte.view.weightTextField.setText(String.format("%.2f", NutriByte.person.weight));
				NutriByte.view.heightTextField.setText(String.format("%.2f", NutriByte.person.height));
				NutriByte.view.ingredientsToWatchTextArea.setText(NutriByte.person.ingredientsToWatch);

				if (NutriByte.person instanceof Female) {
					NutriByte.view.genderComboBox.getSelectionModel().select(0);
				}
				if (NutriByte.person instanceof Male) {
					NutriByte.view.genderComboBox.getSelectionModel().select(1);
				}

				int physicalActivityIndex = 0;
				for (int i = 0; i < PhysicalActivityEnum.values().length - 1; i++) {
					if (NutriByte.person.physicalActivityLevel <= PhysicalActivityEnum.values()[i]
							.getPhysicalActivityLevel()
							&& NutriByte.person.physicalActivityLevel > PhysicalActivityEnum.values()[i - 1]
									.getPhysicalActivityLevel()) {
						physicalActivityIndex = i;
						break;
					}
				}
				NutriByte.view.physicalActivityComboBox.getSelectionModel().select(physicalActivityIndex);

				NutriProfiler.createNutriProfile(NutriByte.person);
			}
		}
	}

	class NewMenuItemHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			NutriByte.view.root.setCenter(NutriByte.view.nutriTrackerPane);
			NutriByte.view.initializePrompts();
		}
	}

	class AboutMenuItemHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("About");
			alert.setHeaderText("NutriByte");
			alert.setContentText(
					"Version 3.0 \nRelease 1.0\nCopyleft Java Nerds\nThis software is designed purely for educational purposes.\nNo commercial use intended");
			Image image = new Image(getClass().getClassLoader().getResource(NutriByte.NUTRIBYTE_IMAGE_FILE).toString());
			ImageView imageView = new ImageView();
			imageView.setImage(image);
			imageView.setFitWidth(300);
			imageView.setPreserveRatio(true);
			imageView.setSmooth(true);
			alert.setGraphic(imageView);
			alert.showAndWait();
		}
	}
}
