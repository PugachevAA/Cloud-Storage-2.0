import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AuthController implements Initializable {
    public AnchorPane anchorPane;
    public TextField passTextField;
    public TextField loginTextField;
    public Hyperlink logRegLink;
    public TextField emailTextField;
    public Button exitButton;
    public Button okButton;

    private boolean isNewUser = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void auth() {
        if (App.getUser().getLogin().isEmpty() || App.getUser().getPassword().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText(null);
            alert.setContentText("Заполнены не все поля.");
            alert.showAndWait();
        } else {
            App.getNetwork().writeAndFlush(App.getUser());
        }
    }

    public void register() {
        if (App.getUser().getLogin().isEmpty()
                || App.getUser().getPassword().isEmpty()
                || App.getUser().getEmail().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText(null);
            alert.setContentText("Заполнены не все поля.");
            alert.showAndWait();
        } else {
            App.getNetwork().writeAndFlush(App.getUser());
        }

    }

    public void checkLogReg(){

        if (!isNewUser) {
            App.stage.setHeight(240);
            logRegLink.setText("Logon");
            anchorPane.setPrefHeight(200);
            emailTextField.setVisible(true);
            passTextField.setLayoutY(100);
            logRegLink.setLayoutY(130);
            isNewUser = true;
        } else {
            App.stage.setHeight(200);
            logRegLink.setText("Register");
            anchorPane.setPrefHeight(160);
            emailTextField.setVisible(false);
            passTextField.setLayoutY(60);
            logRegLink.setLayoutY(90);
            isNewUser = false;
        }
    }

    public void exit() {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }

    public void authAction(){
        App.getUser().setLogin(loginTextField.getText());
        App.getUser().setEmail(emailTextField.getText());
        App.getUser().setPassword(passTextField.getText());
        App.getUser().setNewUser(isNewUser);
        App.getUser().setLogOk(false);
        if (isNewUser) {
            register();
        } else {
            auth();
        }

    }
}
