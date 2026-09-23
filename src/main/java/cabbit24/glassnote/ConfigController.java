package cabbit24.glassnote;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class ConfigController implements Initializable
{
	@FXML
	Pane configPane;
	@FXML
	ColorPicker backgroundColorPicker;
	@FXML
	ColorPicker foregroundColorPicker;
	@FXML
	ColorPicker fontColorPicker;
	@FXML
	Slider transparencySlider;	
	@FXML
	CheckBox wifiCheckbox;
	@FXML
	Model model;
		
	File fontFile;
	
	Path cssPath;
	
	Stage stage;
	Scene scene;
	Stage rootStage;

	private static Properties properties;
	
	Color foregroundColor;
	Color backgroundColor;
	Color fontColor;
		
	File configFile;
	

	
	public Pane getRoot() 
	{
		return configPane;
	}
	
	public void openWindow() 
	{
		stage.show();
		stage.setResizable(false);
		stage.getIcons().add(new Image(getClass().getResourceAsStream("glassnote.png")));
	}

	public void changeForegroundColor(ActionEvent e) 
	{
		model.setForegroundColor(foregroundColorPicker.getValue());
		properties.setProperty("foregroundColor", foregroundColorPicker.getValue().toString());
		setColors();
		saveConfig();
	}
	public String formatColor(Color color) 
	{
		return "rgb("+ (int)(color.getRed()*255)
		          + "," + (int)(color.getGreen()*255)
		          + "," + (int)(color.getBlue()*255)
		          + ")";
	}

	@FXML
	public void changeBackgroundColor(ActionEvent e) 
	{
		properties.setProperty("backgroundColor", backgroundColorPicker.getValue().toString());
		setColors();
		saveConfig();
	}
	public void changeFontColor(ActionEvent e) 
	{
		properties.setProperty("fontColor", fontColorPicker.getValue().toString());
		setColors();
		saveConfig();
	}
	public void setModel(Model model) 
	{
		this.model = model;
	}
	public void loadCustomFont() 
	{
		FileChooser fileChooser = new FileChooser();
		ExtensionFilter extensionFilterFonts = new ExtensionFilter("Font Files","*.ttf","*.woff","*.woff2","*.otf");
		ExtensionFilter extensionFilterAll = new ExtensionFilter("All Files","*.*");
		fileChooser.getExtensionFilters().addAll(extensionFilterFonts, extensionFilterAll);
		fileChooser.setTitle("Choose font file");
		fontFile = null;
		try 
		{
			fontFile = fileChooser.showOpenDialog(model.getRoot().getWindow());
			if(fontFile != null && fontFile.isFile()) 
			{
				Font font = Font.loadFont(fontFile.toURI().toString(),model.getController().getTextSize());
				if(font != null) 
				{
					properties.setProperty("Font", font.getName());
					properties.setProperty("FontPath", fontFile.toURI().toString());
					saveConfig();
					setColors();
				}
			}
		}
		catch(Exception e) 
		{
			e.printStackTrace();
		}		
	}
	public void setColors() 
	{
		try 
		{
			TabPane tabPane = model.getController().getTabPane();

			cssPath = Files.createTempFile("fx-theme-",".css");
			
			Font.loadFont(properties.getProperty("Font"), model.getController().getTextSize());
			Font.loadFont(getClass().getResourceAsStream("NDS12.ttf"),model.getController().getTextSize());
			if(properties.getProperty("FontPath") != null) 
			{
				Font.loadFont(properties.getProperty("FontPath"), model.getController().getTextSize());
			}
			Files.writeString(cssPath,
						".styled-text-area{-fx-background-color: " +  formatColor(Color.web(properties.getProperty("foregroundColor"))) + ";}"
							+".tab-pane.floating{-fx-background-color:" + formatColor(Color.web(properties.getProperty("backgroundColor"))) + ";}"
							+".tab-pane.floating .tab-header-area{-fx-background-color: " + formatColor(Color.web(properties.getProperty("backgroundColor"))) + ";}"
							+".text-field{-fx-background-color: " + formatColor(Color.web(properties.getProperty("backgroundColor"))) + "; -fx-text-fill: " + formatColor(Color.web(properties.getProperty("fontColor"))) + "; -fx-font-family: \"" + properties.getProperty("Font") + "\";}"
							+"#anchorPane{-fx-background-color:" + formatColor(Color.web(properties.getProperty("backgroundColor"))) + ";}"
							+".tab{-fx-background-color:" + formatColor(Color.web(properties.getProperty("backgroundColor"))) + ";}"
							+".tab-label .text{-fx-font-family: \"" + properties.getProperty("Font") + "\"; -fx-fill: " + formatColor(Color.web(properties.getProperty("fontColor"))) + ";}"
							+".styled-text-area .text{-fx-font-family: \""+ properties.getProperty("Font") +"\"; -fx-fill:" + formatColor(Color.web(properties.getProperty("fontColor"))) + ";}"
							+"#menuBar {-fx-background-color: " + formatColor(Color.web(properties.getProperty("backgroundColor"))) + ";}"
							+"#menuBar .label{-fx-text-fill:" + formatColor(Color.web(properties.getProperty("fontColor"))) + ";}"
							+".context-menu {-fx-background-color:" + formatColor(Color.web(properties.getProperty("foregroundColor"))) + ";}"
					);
			model.getStage().setOpacity(Double.parseDouble(properties.getProperty("transparency")));

	        cssPath.toFile().deleteOnExit();
	        
	        tabPane.getStyleClass().setAll("themed");
	        model.getRoot().getStylesheets().setAll(
	        		cssPath.toUri().toURL().toExternalForm()
	        		);
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	@Override
	public void initialize(URL location, ResourceBundle resources) 
	{
		stage = new Stage();
		scene = new Scene(configPane);
		stage.setScene(scene);	
	}
	public Path getCssPath() 
	{
		return cssPath;
	}
	public void setTransparency() 
	{
		properties.setProperty("transparency", String.valueOf(transparencySlider.getValue()));
		model.getStage().setOpacity(Double.parseDouble(properties.getProperty("transparency")));
		saveConfig();
	}
	public void setTransparency(Double value) 
	{
		properties.setProperty("transparency", value.toString());
		model.getStage().setOpacity(value);
		saveConfig();
	}
	public PrintWriter createConfigFile() 
	{
		String fileName = "config.properties";
		PrintWriter writer = null;
		try 
		{
			writer = new PrintWriter(fileName);
			
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
		return writer;
	}
	public void saveConfig() 
	{			
		configFile = new File("config.properties");
		if(configFile.isFile()) 
		{
			PrintWriter writer;
			try 
			{
				writer = new PrintWriter(configFile);
						
				try 
				{
					properties.store(writer,null);
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
				finally 
				{
					writer.close();
				}
			} 
			catch (FileNotFoundException e) 
			{
				e.printStackTrace();
			}

		}
	}

	
	public Properties loadConfig() 
	{
		try
		{
			
			configFile = new File("config.properties");
			//if there is already a "config.properties" file 
			if(configFile.isFile()) 
			{
				BufferedReader reader = new BufferedReader(new FileReader(configFile));
				properties = new Properties();
				properties.load(reader);
				reader.close();				
				//set configuration pane to be the values inside of the properties file
				transparencySlider.setValue(Double.parseDouble(properties.getProperty("transparency")));
				foregroundColorPicker.setValue(Color.web(properties.getProperty("foregroundColor")));
				backgroundColorPicker.setValue(Color.web(properties.getProperty("backgroundColor")));
				fontColorPicker.setValue(Color.web(properties.getProperty("fontColor")));
				wifiCheckbox.setSelected(Boolean.parseBoolean(properties.getProperty("disableWifi")));
			}
			//if "config.properties" file does not exist, create new config.properties/create default config.properties
			else 
			{
				properties = new Properties();
				PrintWriter writer = createConfigFile();
				try 
				{
					properties.setProperty("transparency", Double.toString(1.0));
					properties.setProperty("fontSize", Integer.toString(24));
					Font font = Font.loadFont(getClass().getResourceAsStream("NDS12.ttf"),24.0d);
					properties.setProperty("Font", font.getName());
					
					properties.setProperty("foregroundColor", Color.BLACK.toString());
					properties.setProperty("backgroundColor", Color.BLACK.toString());
					properties.setProperty("fontColor", Color.WHITE.toString());
					properties.setProperty("disableWifi", "false");
					
					properties.store(writer,null);
					
					//initialize config pane to default values
					transparencySlider.setValue(Double.parseDouble(properties.getProperty("transparency")));
					foregroundColorPicker.setValue(Color.web(properties.getProperty("foregroundColor")));
					backgroundColorPicker.setValue(Color.web(properties.getProperty("backgroundColor")));
					fontColorPicker.setValue(Color.web(properties.getProperty("fontColor")));
					wifiCheckbox.setSelected(false);

				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
				finally 
				{
					writer.close();		
				}
			}
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		return properties;
	}
	public void resetConfig() 
	{
		properties = new Properties();
		properties.setProperty("transparency", Double.toString(1.0));
		model.getController().setTextSize(24);
		Font font = Font.loadFont(getClass().getResourceAsStream("NDS12.ttf"), model.getController().getTextSize());
		
		model.getController().zoomIn();
		model.getController().zoomOut();
		
		properties.setProperty("Font", font.getName());
		properties.setProperty("foregroundColor", Color.BLACK.toString());
		properties.setProperty("backgroundColor", Color.BLACK.toString());
		properties.setProperty("fontColor", Color.WHITE.toString());
		properties.setProperty("disableWifi", "false");

		
		transparencySlider.setValue(1.0);
		foregroundColorPicker.setValue(Color.BLACK);
		backgroundColorPicker.setValue(Color.BLACK);
		fontColorPicker.setValue(Color.WHITE);
		wifiCheckbox.setSelected(false);
		
		saveConfig();
		setColors();
	}
	public void disableWifi() 
	{
		properties.setProperty("disableWifi", Boolean.toString(wifiCheckbox.isSelected()));
		wifiCheckbox.setSelected(Boolean.parseBoolean(properties.getProperty("disableWifi")));
		saveConfig();
	}
}
