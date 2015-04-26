package proj;
import java.util.*;
import javafx.geometry.*;
import java.io.*;
import java.awt.Insets;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.scene.*;
import javafx.scene.*;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.web.*;
import javafx.scene.web.*;
import javafx.stage.*;
import javax.lang.model.element.Element;
import javax.lang.model.util.Elements;
import javax.swing.text.Document;
import org.w3c.dom.NodeList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author sohamp
 */
public class Browser extends Application{

    /**
     * @param args the command line arguments
     */
    
    int counter = 0;
    int bnflag = 0;
    int hypflag = 1;
    static ArrayList<String> path = new ArrayList<String>();
    static ArrayList<String> search1_results = new ArrayList<String>();
    static ArrayList<String> search2_results = new ArrayList<String>();
    static ArrayList<String> book_search = new ArrayList<String>();
    static ArrayList<String> hs = new ArrayList<String>();
    static ArrayList<String> bookmarks = new ArrayList<String>();
    @Override
    public void start(Stage stage) throws FileNotFoundException, IOException  {
        ObservableList<String> history = FXCollections.observableArrayList();
        BufferedReader his = new BufferedReader(new FileReader("History.txt"));
        BufferedReader book = new BufferedReader(new FileReader("Bookmark.txt"));
        String inp;
        while((inp = his.readLine()) != null){
            hs.add(inp);
            history.add(inp);
        }
        his.close();
        
        while((inp = book.readLine()) != null){
            bookmarks.add(inp);
            history.add(inp);
        }
        book.close();
        
        BorderPane whole = new BorderPane();
        ComboBox<String> dropdown;
        dropdown = new ComboBox(history);
        dropdown.setPrefSize(200, 33);
        HBox toolbar = new HBox();
        toolbar.setSpacing(10);
        HBox search_engine = new HBox();
        VBox display = new VBox();
        toolbar.setSpacing(10);
        whole.setTop(toolbar);
        whole.setCenter(display);
        whole.setBottom(search_engine);
        Button books = new Button("Bookmarks");
        Button back = new Button("Back");
        Button next = new Button("Next");
        Button refresh = new Button("REF");
        Button iit_search = new Button("IIT Pages");
        Button history_search = new Button("Search in History");
        ToggleButton bookmark = new ToggleButton("");
        
        bookmark.setPrefSize(50, 33);
        back.prefHeight(10);
        back.prefWidth(100);
        TextField url = new TextField();
        TextField search2 = new TextField();
        TextField search1 = new TextField();
        search1.setPrefSize(400, 33);
        search2.setPrefSize(400, 33);
        search_engine.setSpacing(20);
        search_engine.setPrefHeight(130);
        search_engine.setStyle("-fx-background-color: #336699;");
        url.setPrefSize(1000, 33);
        back.setPrefSize(60, 33);
        next.setPrefSize(50, 33);
        stage.setTitle("Browser");
        stage.setWidth(800);
        stage.setHeight(800); 
        Scene scene = new Scene(whole, 600, 800);
        WebView browser = new WebView();
        WebEngine engine = browser.getEngine();
        
        engine.getLoadWorker().stateProperty().addListener(
            new ChangeListener<State>() {
              @Override public void changed(ObservableValue ov, State oldState, State newState) {

                  if (newState == Worker.State.SUCCEEDED) {
                    url.setText(engine.getLocation());
                    if(!history.contains(engine.getLocation()))history.add(engine.getLocation());
                    if(!hs.contains(engine.getLocation()))hs.add(engine.getLocation());
                    if(bnflag == 0) path.add(engine.getLocation());
                    bnflag = 0;
                    counter = counter + 1;
                    if(hypflag == 0) counter = counter + 1;
                    hypflag = 1;
                    try{
                        bookmark.setSelected(false);
                        bookmark.setText("FAV");
                        bookmark.setStyle(" -fx-font : 10 Arial");
                        update();
                    }
                    catch(IOException ex){
                        
                    }
                }
                  
                }
            });
        
        dropdown.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                for(int i = counter + 1; i < path.size(); i++){
                    path.remove(i);
                }
                hypflag = 0;
                String str = dropdown.getValue();
                engine.load(str);
                path.add(engine.getLocation());
            }
        });
        
        back.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                hypflag = 0;
                if(counter > 3){
                    bnflag = 1;
                    counter = counter - 4;
                    engine.load(path.get(counter + 1));
                }
            }
        });
        
        next.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                hypflag = 0;
                if(counter < path.size() - 1){
                    bnflag = 1;
                    engine.load(path.get(counter + 1));
                    url.setText(path.get(counter));
                }
            }
        });
        url.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                hypflag = 0;
                String str = url.getText();
                for(int i = counter; i < path.size(); i++){
                    path.remove(i);
                }
                if(str.startsWith("http://www.") || str.startsWith("https://www.")){
                    engine.load(str);
                    path.add(str);
                    if(!history.contains(str)) {
                        history.add(engine.getLocation());
                        hs.add(engine.getLocation());
                    }
                    
                }
                else if(str.startsWith("www.")){
                    engine.load("https://" + str);
                    url.setText("https://" + str);
                    path.add("https://" + str);
                    if(!history.contains(engine.getLocation())) {
                        history.add(engine.getLocation());
                        hs.add(engine.getLocation());
                    }
                }
                else {
                    engine.load("https://www." + str);
                    url.setText("https://www." + str);
                    path.add("https://www." + str);
                    if(!history.contains(engine.getLocation())) {
                        history.add(engine.getLocation());
                        hs.add(engine.getLocation());
                    }
                }
                try {
                    update();
                } catch (IOException ex) {
                    
                }
            }
        });
  
        refresh.setPrefSize(50, 33);
        refresh.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                hypflag = 0;
                engine.load(engine.getLocation());
                counter = counter - 2;
            }
        });
        
        books.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                String str = "<html><body><h1>Search Results</h1>";
                for(int i = 0; i < bookmarks.size(); i++){
                    str += "<a href = \"";
                    str += bookmarks.get(i);
                    str += "\">";
                    str += bookmarks.get(i);
                    str += "</a>";
                    str += "<br>";
                }
                str += "</body></html>";
                engine.loadContent(str);
            }
        });
        
        bookmark.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                if(bookmark.isSelected()) {
                    bookmark.setStyle("-fx-base: red; -fx-font : 10 Arial");
                    bookmark.setText("Fav");
                    //System.out.println("Yes");
                    bookmarks.add(engine.getLocation());
                }
                else {
                    bookmark.setStyle(" -fx-font : 10 Arial");
                    bookmark.setText("");
                    bookmarks.remove(engine.getLocation());
                }
            }
        });
        
        search1.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                for(int i = 0; i < history.size(); i++){
                    if(history.get(i).contains(search1.getText())){
                        search1_results.add(history.get(i));
                    }
                }
                String str = "<html><body><h1>Search Results</h1>";
                for(int i = 0; i < search1_results.size(); i++){
                    str += "<a href = \"";
                    str += search1_results.get(i);
                    str += "\">";
                    str += search1_results.get(i);
                    str += "</a>";
                    str += "<br>";
                }
                str += "</body></html>";
                engine.loadContent(str);
            }
        });
        
        history_search.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                for(int i = 0; i < history.size(); i++){
                    if(history.get(i).contains(search1.getText())){
                        search1_results.add(history.get(i));
                    }
                }
                String str = "<html><body><h1>Search Results</h1>";
                for(int i = 0; i < search1_results.size(); i++){
                    str += "<a href = \"";
                    str += search1_results.get(i);
                    str += "\">";
                    str += search1_results.get(i);
                    str += "</a>";
                    str += "<br>";
                }
                str += "</body></html>";
                engine.loadContent(str);
            }
        });
        
        iit_search.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                String query = search2.getText();
                AvlTree avl = null ;
		try{
                        System.out.println("Here1");
			FileInputStream fileIn = new FileInputStream("rel.ser") ; 
			ObjectInputStream in = new ObjectInputStream(fileIn) ; 
			avl = (AvlTree)in.readObject() ; 
			in.close() ; 
			fileIn.close() ;
		}
		catch(IOException i ){
                        System.out.println("rel.ser not opened");
		}
		catch(ClassNotFoundException c){
                    System.out.println("Cannot be serialized");
		}
                Rank_Algorithm r = new Rank_Algorithm();
                try{
                    AvlImplementation imp = new AvlImplementation() ; 
                    imp.Inorder(avl.head,query, r);
                    String str = "";
                    str += "<html>\n<body>\n<h1> Search Results </h1>\n";
                    str += "<p>";
                    while(r.rank.size() > 0){
                        Page temp = r.rank.poll();
                        str += "<a href = \"";
                        str += temp.url;
                        str += "\">";
                        str += temp.url;
                        str += "</a>";
                        str += "<br>";
                    }
                    str += "</p>\n";
                    str += "</body> \n </html>";
                    System.out.println(str);
                    engine.loadContent(str);
                }
                catch(IOException io){
                    
                }
            }
        });
        
        
        search2.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                String query = search2.getText();
                AvlTree avl = null ;
		try{
                        System.out.println("Here1");
			FileInputStream fileIn = new FileInputStream("rel.ser") ; 
			ObjectInputStream in = new ObjectInputStream(fileIn) ; 
			avl = (AvlTree)in.readObject() ; 
			in.close() ; 
			fileIn.close() ;
		}
		catch(IOException i ){
                        System.out.println("rel.ser not opened");
		}
		catch(ClassNotFoundException c){
                    System.out.println("Cannot be serialized");
		}
                Rank_Algorithm r = new Rank_Algorithm();
                try{
                    AvlImplementation imp = new AvlImplementation() ; 
                    imp.Inorder(avl.head,query, r);
                    String str = "";
                    str += "<html>\n<body>\n<h1> Search Results </h1>\n";
                    str += "<p>";
                    while(r.rank.size() > 0){
                        Page temp = r.rank.poll();
                        str += "<a href = \"";
                        str += temp.url;
                        str += "\">";
                        str += temp.url;
                        str += "</a>";
                        str += "<br>";
                    }
                    str += "</p>\n";
                    str += "</body> \n </html>";
                    System.out.println(str);
                    engine.loadContent(str);
                }
                catch(IOException io){
                    
                }
            }
        });
        
        
        
        refresh.setStyle("-fx-font: 10 arial; ");
        next.setStyle("-fx-font: 10 arial; ");
        back.setStyle("-fx-font: 10 arial; ");
        
        toolbar.getChildren().add(bookmark);
        toolbar.getChildren().add(refresh);
        toolbar.getChildren().add(url);
        toolbar.getChildren().add(dropdown);
        toolbar.getChildren().add(back);
        toolbar.getChildren().add(next);
        display.getChildren().add(browser);
        search_engine.getChildren().add(search2);
        search_engine.getChildren().add(iit_search);
        search_engine.setSpacing(60);
        search_engine.getChildren().add(search1);
        search_engine.setSpacing(20);
        search_engine.getChildren().add(history_search);
        search_engine.getChildren().add(books);
        next.relocate(100, 100);
        scene.setRoot(whole);
        stage.setScene(scene);
        stage.show();
    }
    
    static void update() throws FileNotFoundException, IOException{
        File fout = new File("History.txt");
	FileOutputStream fos = new FileOutputStream(fout);
	BufferedWriter outp = new BufferedWriter(new OutputStreamWriter(fos));
        for(int i = 0; i < hs.size(); i++){
            outp.write(hs.get(i));
            outp.newLine();
        }
        outp.close();
        File fot = new File("Bookmark.txt");
	FileOutputStream fo = new FileOutputStream(fot);
	BufferedWriter otp = new BufferedWriter(new OutputStreamWriter(fo));
        for(int i = 0; i < bookmarks.size(); i++){
            otp.write(bookmarks.get(i));
            otp.newLine();
        }
        otp.close();
    }
    
    public static void main(String[] args) {
        System.setProperty("jsse.enableSNIExtension", "false");
        launch(args);
    }
}
