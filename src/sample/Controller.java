package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Controller {

    @FXML
    TextField name;

    @FXML
    Label hello;

    private Database db;

    public Controller(){
        db = new Database("coolDB");
        db.connect();
        db.createTable("test", new Database.TableEntity("UserName", "varchar(255)"));
    }

    public void sayHello(){
        String usersName = name.getText();
        hello.setText("Hello " + usersName);
        db.insert("'" + usersName + "'", "test");
        try {
            ResultSet resultSet = db.select("*", "test");
            while(resultSet.next()) {
                System.out.println(resultSet.getString("UserName"));
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
