package cabbit24.glassnote;

import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Model
{
	private final ObservableList<Tab> tabs = FXCollections.observableArrayList();

	ConfigController configController;
	SearchController searchController;
	Controller controller;
	
	Stage stage;
	Scene root;
	TabPane tabPane;
	
	Color foregroundColor;
	Color backgroundColor;
	Color fontColor;
	public Model() 
	{
		try
		{
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/cabbit24/glassnote/config.fxml"));
			loader.load();
			configController = loader.getController();
			configController.setModel(this);
			
			loader = loadFXML("search");
			loader.load();
			searchController = loader.getController();
			searchController.setModel(this);
			
			loader = loadFXML("view");
			loader.load();
			controller = loader.getController();
			controller.setModel(this);

		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public ObservableList<Tab> getTabs()
	{
		return tabs;
	}
	
	public ConfigController getConfigController() 
	{
		return configController;
	}
	public SearchController getSearchController() 
	{
		return searchController;
	}
	public Controller getController() 
	{
		return controller;
	}
	public void setRoot(Scene root) 
	{
		this.root = root;
		root.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		configController.getRoot().getStylesheets().add(getClass().getResource("application.css").toExternalForm());
	}
	public Scene getRoot() 
	{
		return root;
	}
	public void setTabPane(TabPane tabPane) 
	{
		this.tabPane = tabPane;
	}
	public TabPane getTabPane() 
	{
		return tabPane;
	}
	public static FXMLLoader loadFXML(String fxml) throws IOException 
	{
		FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
		return fxmlLoader;
	}
	public void setStage(Stage stage) 
	{
		this.stage = stage;
	}
	public Stage getStage() 
	{
		return stage;
	}
	public void setBackgroundColor(Color backgroundColor) 
	{
		this.backgroundColor = backgroundColor;
	}
	public void setForegroundColor(Color foregroundColor) 
	{
		this.foregroundColor = foregroundColor;
	}
	public void setFontColor(Color fontColor) 
	{
		this.fontColor = fontColor;
	}
	public Color getBackgroundColor() 
	{
		return backgroundColor;
	}
	public Color getForegroundColor() 
	{
		return foregroundColor;
	}
	public Color setFontColor() 
	{
		return fontColor;
	}
}
