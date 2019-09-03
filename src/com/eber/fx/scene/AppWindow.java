package com.eber.fx.scene;

import com.eber.fx.util.Util;

import javafx.beans.binding.When;
import javafx.beans.property.StringProperty;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

public class AppWindow {

	public static WindowComponent createGrip(Stage stage) {
		
		stage.initStyle(StageStyle.TRANSPARENT);
		WindowComponent component = new WindowComponent(stage);
		
		StackPane container = Util.addClass(new StackPane(component), "window-container");
		Insets containerPadding = new Insets(8);
		
		container.paddingProperty().bind(new When(stage.maximizedProperty()).then(Insets.EMPTY).otherwise(containerPadding));
		container.setStyle("-fx-background-color: transparent");
		
		container.setAlignment(Pos.CENTER);
		
		Scene scene = new Scene(container);
		
		scene.setFill(Color.TRANSPARENT);
		
		Util.addSheets(scene, "window");
		
		stage.setScene(scene);
		
		return component;
		
	}
	
	public static final class WindowComponent extends BorderPane implements EventHandler<MouseEvent> {
		
		private static PseudoClass FOCUSED = PseudoClass.getPseudoClass("windowfocused");
		
		private Label title;
		private Button minimize, maximize, close;
		private HBox windowBar, windowButtons;
		
		private StackPane contentContainer;
		private Parent content;
		
		private Stage window;
		private double[] initialPosition = new double[2];
		
		private StackPane shadow;
		
		private EventHandler<ActionEvent> buttonActions = e->{
			Object source = e.getSource();
			
			if (source.equals(close)) window.fireEvent(new WindowEvent(null, WindowEvent.WINDOW_CLOSE_REQUEST));
			else if (source.equals(minimize)) window.setIconified(true);
			else if (source.equals(maximize)) window.setMaximized(!window.isMaximized());
		};
		
		public WindowComponent(Stage window) {
			this.window = window;
			(this.title = Util.addClass(new Label(), "title")).textProperty().bind(window.titleProperty());
			
			Util.addClass(this, "window-component");			
			
			shadow = Util.addClass(new StackPane(), "shadow");
			
			minimize = Util.addClass(new Button(), "minimize");
			maximize = Util.addClass(new Button(), "maximize");
			close = Util.addClass(new Button(), "close");
			
			windowButtons = Util.addClass(new HBox(minimize, maximize, close), "window-buttons");
			contentContainer = Util.addClass(new StackPane(), "content-container");
			
			StackPane barFiller = new StackPane();
						
			windowBar = Util.addClass(new HBox(15, this.title, barFiller, windowButtons), "window-bar");
			
			windowBar.setAlignment(Pos.CENTER_LEFT);
			windowBar.setPadding(new Insets(5, 5, 5, 10));
			
			windowBar.setOnMousePressed(this);
			windowBar.setOnMouseDragged(this);
			windowBar.setOnMouseClicked(this);
			
			close.setOnAction(buttonActions);
			minimize.setOnAction(buttonActions);
			maximize.setOnAction(buttonActions);
			window.focusedProperty().addListener((e, o, n)->{pseudoClassStateChanged(FOCUSED, n);});
			
			HBox.setHgrow(barFiller, Priority.ALWAYS);
			Util.maxWidth(this.title, shadow);
			Util.maxHeight(close, maximize, minimize, shadow);
			
			setShadow(false);
			
			setCenter(new StackPane(contentContainer, shadow));
			setTop(windowBar);
			
		}
		
		public WindowComponent(Stage window, Parent content) {
			this(window);
			setContent(content);
		}
		
		public Parent getContent() {
			return content;
		}
		
		private void setShadow(boolean isVisible) {
			shadow.setVisible(isVisible);
			shadow.setManaged(isVisible);
		}
		
		public void setContent(Parent content) {
			contentContainer.getChildren().setAll(content);
			this.content = content;
		}
		
		public void setMaximizable(boolean maximizable) {
			maximize.setVisible(maximizable);
			maximize.setManaged(maximizable);
		}
		
		public void addWindowButton(Button button) {
			windowButtons.getChildren().add(0, button);
		}
		
		public StringProperty titleProperty() {
			return title.textProperty();
		}

		@Override
		public void handle(MouseEvent event) {
			
			if (event.getEventType().equals(MouseEvent.MOUSE_DRAGGED)) {
				if (window.isMaximized()) window.setMaximized(false);
				
				window.setX(event.getScreenX() - initialPosition[0]);
				window.setY(event.getScreenY() - initialPosition[1]);
			} else if (event.getEventType().equals(MouseEvent.MOUSE_PRESSED) && !window.isMaximized()){
				initialPosition[0] = event.getSceneX();
				initialPosition[1] = event.getSceneY();
			} else if (maximize.isVisible() && event.getEventType().equals(MouseEvent.MOUSE_CLICKED) && event.getClickCount() == 2) window.setMaximized(!window.isMaximized());
			
		}
		
	}
	
}
