package cabbit24.glassnote;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.paint.Color;
import java.util.Properties;
import java.io.*;


public class App extends Application 
{
	String ssid;
	@Override
	public void start(Stage primaryStage) 
	{
		try 
		{
			loadFXML("config");
			Model model = new Model();
			primaryStage.setTitle("Glassnote");
			primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("glassnote.png")));
			FXMLLoader loader = new FXMLLoader(getClass().getResource("view.fxml"));
			BorderPane root = loader.load();
			
			Controller controller = loader.getController();
			controller.setModel(model);		
			
			loader = new FXMLLoader(getClass().getResource("search.fxml"));
			loader.load();
			SearchController searchController = loader.getController();
			searchController.setModel(model);

			Scene scene = new Scene(root,960,1000);
			primaryStage.setMaximized(true);
			model.setRoot(scene);
			
			KeyCombination newTab = new KeyCodeCombination(KeyCode.T,KeyCombination.SHORTCUT_DOWN);
			KeyCombination closeTab = new KeyCodeCombination(KeyCode.W,KeyCombination.SHORTCUT_DOWN);
			KeyCombination find = new KeyCodeCombination(KeyCode.F,KeyCombination.SHORTCUT_DOWN);
			KeyCombination zoomIn = new KeyCodeCombination(KeyCode.EQUALS,KeyCombination.SHORTCUT_DOWN);
			KeyCombination zoomOut = new KeyCodeCombination(KeyCode.MINUS,KeyCombination.SHORTCUT_DOWN);
			
			KeyCombination bold = new KeyCodeCombination(KeyCode.B,KeyCombination.SHORTCUT_DOWN);
			KeyCombination italic = new KeyCodeCombination(KeyCode.I,KeyCombination.SHORTCUT_DOWN);
			KeyCombination underline = new KeyCodeCombination(KeyCode.U,KeyCombination.SHORTCUT_DOWN);
			KeyCombination strikethrough = new KeyCodeCombination(KeyCode.X,KeyCombination.SHORTCUT_DOWN,KeyCombination.SHIFT_DOWN);
	
			scene.setOnKeyPressed(e -> 
			{
				if(newTab.match(e))
				{
					controller.openTab();
				}
				if(closeTab.match(e)) 
				{
					controller.closeTab();
				}
				if(find.match(e)) 
				{
					searchController.openSearchWindow();
				}
				if(zoomIn.match(e)) 
				{
					controller.zoomIn();
				}
				if(zoomOut.match(e)) 
				{
					controller.zoomOut();
				}
				if(bold.match(e)) 
				{
					controller.bold();
				}
				if(italic.match(e)) 
				{
					controller.italic();
				}
				if(underline.match(e)) 
				{
					controller.underline();
				}
				if(strikethrough.match(e)) 
				{
					controller.strikethrough();
				}
			});

			controller.openTab();
	        
			scene.setFill(Color.TRANSPARENT);
			primaryStage.setScene(scene);
			
			primaryStage.show();
			model.setStage(primaryStage);
			
			//CONFIGURATION LOADING
			//load config if it exists else default config
			Properties properties = model.getConfigController().loadConfig();
			model.getConfigController().setColors();
			if(properties != null) 
			{
				if(Boolean.parseBoolean(properties.getProperty("disableWifi"))) 
				{
					//ssid = executePowershellCommand("powershell (get-netconnectionProfile).Name");	
					executePowershellCommand("netsh wlan disconnect");
				}
			}
			primaryStage.setOnHiding(e -> model.getConfigController().saveConfig());
		}
		catch(Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	public static void launchGlassnote(String[] args) 
	{
		launch(args);
	}
	
	public String executePowershellCommand(String command) 
	{
		try 
		{
			Runtime runtime = Runtime.getRuntime();
			Process process = runtime.exec(command);
			boolean processExecuted = process.isAlive();
			System.out.println(command + "executed? : " + processExecuted);
			StringBuilder output = new StringBuilder();
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;
			while((line = reader.readLine()) != null) 
			{
				output.append(line + "\n");
			}
			int exitVal = process.waitFor();
			if(exitVal == 0) 	
			{
				return output.toString();
			}
			else 
			{
				System.out.println(output.toString());
				System.out.println("Failed to execute the command " + command);
			}
		}
		catch(IOException | InterruptedException e) 
		{
			System.out.println(e.getMessage());
		}
		return null;
	}
	private static Parent loadFXML(String fxml) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
		return fxmlLoader.load();
	}

}
