package cabbit24.glassnote;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import org.fxmisc.richtext.InlineCssTextArea;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.IndexRange;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.collections.ListChangeListener;


public class Controller implements Initializable
{
	@FXML 
	private TabPane tabPane;
	@FXML
	private MenuBar menuBar;

	private static double textSize = 24;
	
	@FXML
	private BorderPane borderPane;
	
	private Model model;
	
	public void undo()
	{
		Tab tab = tabPane.getSelectionModel().getSelectedItem();
		InlineCssTextArea textArea = (InlineCssTextArea) tab.getContent().lookup("#textArea");
		if(textArea.isUndoAvailable()) 
		{
			textArea.undo();
		}
	}
	public void redo() 
	{
		Tab tab = tabPane.getSelectionModel().getSelectedItem();
		InlineCssTextArea textArea = (InlineCssTextArea) tab.getContent().lookup("#textArea");
		if(textArea.isRedoAvailable()) 
		{
			textArea.redo();
		}
	}
	public void cut() 
	{
		Tab tab = tabPane.getSelectionModel().getSelectedItem();
		InlineCssTextArea textArea = (InlineCssTextArea) tab.getContent().lookup("#textArea");
		textArea.cut();
	}
	public void copy() 
	{
		Tab tab = tabPane.getSelectionModel().getSelectedItem();
		InlineCssTextArea textArea = (InlineCssTextArea) tab.getContent().lookup("#textArea");
		textArea.copy();
	}
	public void paste()  
	{
		Tab tab = tabPane.getSelectionModel().getSelectedItem();
		InlineCssTextArea textArea = (InlineCssTextArea) tab.getContent().lookup("#textArea");
		textArea.paste();
	}
	public void delete() 
	{
		Tab tab = tabPane.getSelectionModel().getSelectedItem();
		InlineCssTextArea textArea = (InlineCssTextArea) tab.getContent().lookup("#textArea");
		textArea.deleteText(textArea.getSelection());
	}
	public void selectAll() 
	{
		Tab tab = tabPane.getSelectionModel().getSelectedItem();
		InlineCssTextArea textArea = (InlineCssTextArea) tab.getContent().lookup("#textArea");
		textArea.selectAll();
	}
	@FXML
	public void exit() 
	{
		System.exit(0);
	}
	public void getTab() 
	{
		if(tabPane == null) 
		{
			return;
		}
		if(tabPane.getTabs().size() <= 0) 
		{
			return;
		}
	}
	public void openTab() 
	{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("tab.fxml"));

		try
		{
			Tab tab = loader.load();
			InlineCssTextArea textArea = (InlineCssTextArea) tab.getContent().lookup("#textArea");
			formatTextArea(textArea,textSize);
			TextField titleField = (TextField) tab.getContent().lookup("#titleField");
			tab.textProperty().bind(titleField.textProperty());
			DateFormat dateFormat = DateFormat.getDateTimeInstance();
			titleField.setText(dateFormat.format(new Date()));
			titleField.setPromptText(dateFormat.format(new Date()));
			model.getTabs().add(tab);
			tabPane.getSelectionModel().select(tab);	
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void setModel(Model model) 
	{
		this.model = model;

		model.getTabs().addListener(new ListChangeListener<Tab>()
		{

			@Override
			public void onChanged(Change<? extends Tab> change)
			{
				while(change.next()) 
				{
					tabPane.getTabs().setAll(model.getTabs());
				}
			}
		});
	}
	public void closeTab() 
	{
		if(model.getTabs().size() <= 1) 
		{
			return;
		}
		int i = tabPane.getSelectionModel().getSelectedIndex();
		tabPane.getSelectionModel().clearSelection();
		model.getTabs().remove(model.getTabs().get(i));
		if(i-1 == 0) 
		{
			tabPane.getSelectionModel().select(0);
		}
		else 
		{
			tabPane.getSelectionModel().select(i-1);
		}
	} 
	@Override
	public void initialize(URL fxmlLocation, ResourceBundle resources)
	{
		tabPane.getStyleClass().add(TabPane.STYLE_CLASS_FLOATING);
	}
	public void openConfig() 
	{
		ConfigController configController = model.getConfigController();
		
		configController.openWindow();
	}
	public TabPane getTabPane() 
	{
		return tabPane;
	}
	public void bold() 
	{
		Tab tab = tabPane.getSelectionModel().getSelectedItem();
		InlineCssTextArea textArea = (InlineCssTextArea) tab.getContent().lookup("#textArea");
		IndexRange selection = textArea.getSelection();
		boolean[] formatting = getFormatting(textArea,selection);
		
		//textArea.bold = !bold
		formatTextAreaSelection(!formatting[0],formatting[1],formatting[2],formatting[3],textArea,selection);
	}
	public void italic() 
	{
		Tab tab = tabPane.getSelectionModel().getSelectedItem();
		InlineCssTextArea textArea = (InlineCssTextArea) tab.getContent().lookup("#textArea");
		IndexRange selection = textArea.getSelection();
		boolean[] formatting = getFormatting(textArea,selection);
		
		//textArea.italic = !italic
		formatTextAreaSelection(formatting[0],!formatting[1],formatting[2],formatting[3],textArea,selection);
	}
	public void underline() 
	{
		Tab tab = tabPane.getSelectionModel().getSelectedItem();
		InlineCssTextArea textArea = (InlineCssTextArea) tab.getContent().lookup("#textArea");
		IndexRange selection = textArea.getSelection();
		boolean[] formatting = getFormatting(textArea,selection);
		
		//textArea.underline = !underline
		formatTextAreaSelection(formatting[0],formatting[1],!formatting[2],formatting[3],textArea,selection);
	}
	public void strikethrough() 
	{
		Tab tab = tabPane.getSelectionModel().getSelectedItem();
		InlineCssTextArea textArea = (InlineCssTextArea) tab.getContent().lookup("#textArea");
		IndexRange selection = textArea.getSelection();
		boolean[] formatting = getFormatting(textArea,selection);	
		//strikethrough = !strikethrough
		formatTextAreaSelection(formatting[0],formatting[1],formatting[2],!formatting[3],textArea,selection);
	}
	public void zoomIn() 
	{
		if(textSize >=72) 
		{
			return;
		}
		textSize+=2;

		for(Tab tab : tabPane.getTabs()) 
		{
			InlineCssTextArea textArea = (InlineCssTextArea) tab.getContent().lookup("#textArea");
			formatTextArea(textArea,textSize);
		}
	}
	
	public void zoomOut() 
	{
		if(textSize <=8) 
		{
			return;
		}
		textSize-=2;

		for(Tab tab : tabPane.getTabs()) 
		{
			InlineCssTextArea textArea = (InlineCssTextArea) tab.getContent().lookup("#textArea");
			formatTextArea(textArea,textSize);
		}

	}
	public void clearFormatting() 
	{
		Tab tab = tabPane.getSelectionModel().getSelectedItem();
		InlineCssTextArea textArea = (InlineCssTextArea) tab.getContent().lookup("#textArea");
		IndexRange selection = textArea.getSelection();
		formatTextAreaSelection(false,false,false,false,textArea,selection);
	}
	boolean[] getFormatting(InlineCssTextArea textArea, IndexRange selection) 
	{
		boolean bold = textArea.getStyleSpans(selection).styleStream().anyMatch(style-> style.contains("-fx-font-weight: bold;"));
		boolean italic = textArea.getStyleSpans(selection).styleStream().anyMatch(style-> style.contains("-fx-font-style: italic;"));
		boolean underline = textArea.getStyleSpans(selection).styleStream().anyMatch(style-> style.contains("-fx-underline: true;"));
		boolean strikethrough = textArea.getStyleSpans(selection).styleStream().anyMatch(style-> style.contains("-fx-strikethrough: true;"));
		
		return new boolean[] {bold,italic,underline,strikethrough};
	}
	void formatTextAreaSelection(Boolean bold, Boolean italic, Boolean underline, Boolean strikethrough, InlineCssTextArea textArea, IndexRange selection) 
	{		
		StringBuilder stringBuilder = new StringBuilder();
		
		stringBuilder.append("-fx-font-weight: " + (bold ? "bold" : "normal") + ";");
		stringBuilder.append("-fx-font-style: " + (italic ? "italic" : "normal") + ";");
		stringBuilder.append("-fx-underline: " + (underline ? "true" : "false") + ";");
		stringBuilder.append("-fx-strikethrough: " + (strikethrough ? "true" : "false") + ";");	

		textArea.setStyle(selection.getStart(), selection.getEnd(), stringBuilder.toString());
	}
	void formatTextArea(InlineCssTextArea textArea, Double fontSize) 
	{
		textArea.setStyle("-fx-font-size: " + textSize + "px;");
	}
	public double getTextSize() 
	{
		return textSize;
	}
	public void setTextSize(int textSize) 
	{
		Controller.textSize = textSize;
	}
	public void openGithub() 
	{
		try 
		{
			Desktop.getDesktop().browse(new URI("https://github.com/cabbit24/glassnote"));
		} 
		catch (IOException | URISyntaxException e) 
		{
			e.printStackTrace();
		} 
	}
	public void find() 
	{
		model.getSearchController().openSearchWindow();
	}
}
