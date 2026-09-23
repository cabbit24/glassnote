package cabbit24.glassnote;

import java.net.URL;
import java.util.ResourceBundle;
import org.fxmisc.richtext.InlineCssTextArea;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;

public class SearchController implements Initializable
{
	@FXML
	Pane searchPane;
	@FXML
	Button findNextButton;
	@FXML
	TextField findField;
	@FXML
	TextField replaceField;
	@FXML
	CheckBox matchCase;
	@FXML
	Model model;
	
	Stage rootStage;
	
	int n = 1;
	
	Stage stage;
	Scene scene;
	
	public void closeWindow() 
	{
		Stage stage = (Stage) searchPane.getScene().getWindow();
		stage.close();
	}
	public int findNext() 
	{
		TabPane tabPane = model.getController().getTabPane();
		InlineCssTextArea textArea = (InlineCssTextArea) tabPane.getSelectionModel().getSelectedItem().getContent().lookup("#textArea");
		String textAreaText = textArea.getText();
		String searchString = findField.getText();
		if(!matchCase.isSelected()) 
		{
			searchString = searchString.toUpperCase();
			textAreaText = textAreaText.toUpperCase();
		}

		int index = nthIndexOf(textAreaText,searchString,n);
		n+=1;
		if(index == -1)
		{
			n=1;
			int tempSelect = textAreaText.indexOf(searchString);
			if(tempSelect != -1) 
			{
				textArea.selectRange(tempSelect, tempSelect+searchString.length());
				n+=1;
			}
			return -1;
		}
		textArea.selectRange(index, index+searchString.length());
		return index;
	}
	int nthIndexOf(String textAreaText, String searchString, int n) 
	{
	    if (n == 1) 
	    {
	    	return textAreaText.indexOf(searchString);
	    } else 
	    {
	    	return textAreaText.indexOf(searchString, nthIndexOf(textAreaText, searchString, n - 1) + searchString.length());
	    }
	}

	public void replaceNext() 
	{
		TabPane tabPane = model.getController().getTabPane();
		InlineCssTextArea textArea = (InlineCssTextArea) tabPane.getSelectionModel().getSelectedItem().getContent().lookup("#textArea");
		String searchString = findField.getText();
		String replaceString = replaceField.getText();

		if(textArea.getSelectedText().isEmpty()) 
		{
			int index = findNext();
			if(index == -1) 
			{
				return;
			}
		}

		if(replaceString.isEmpty()) 
		{
			textArea.replaceSelection("");
		}
		else 
		{
			//replace selection method
			textArea.replaceSelection(replaceString);
		}

		n-=1;
		if(n<1) {n=1;}
		
		int next = findNext();
		if(next != -1) 
		{
			textArea.selectRange(next, next+searchString.length());
		}
	}
	public void replaceAll() 
	{
		TabPane tabPane = model.getController().getTabPane();
		InlineCssTextArea textArea = (InlineCssTextArea) tabPane.getSelectionModel().getSelectedItem().getContent().lookup("#textArea");
		String searchString = findField.getText();
		String replaceString = replaceField.getText();
		String textAreaString = textArea.getText();
		
		if(replaceString.isEmpty()) 
		{
			textArea.replaceText((textAreaString.replace(searchString,"")));
			return;
		}	
		textArea.replaceText((textAreaString.replace(searchString,replaceString)));
	}
	public void openSearchWindow() 
	{
		stage.show();
		stage.setResizable(false);
		stage.getIcons().add(new Image(getClass().getResourceAsStream("glassnote.png")));
	}
	public void setModel(Model model) 
	{
		this.model = model;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) 
	{
		stage = new Stage();
		scene = new Scene(searchPane);
		stage.setScene(scene);	
	}
}
