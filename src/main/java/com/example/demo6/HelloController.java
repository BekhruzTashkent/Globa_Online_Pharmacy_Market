package com.example.demo6;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.util.Random;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class HelloController{

    @FXML
    private Label lblinfo77;

    @FXML
    private Label lblStatus;

    @FXML
    private TextField txtUserName;

    @FXML
    private TextField txtID;

    @FXML
    private TextField txtPassword;

    @FXML
    private TextField txtSearch;

    @FXML
    private Label lbltype;

    @FXML
    private Label lblprice;

    @FXML
    private ListView<String> lblinfo1;

    @FXML
    private Label lblinfo2;

    @FXML
    private Label lblinfo3;

    @FXML
    private Label orderResult;

    @FXML
    private Spinner<Integer> amountSpinner;

    @FXML
    private Button submit;





        @FXML
        private Label lableAdmin;

        @FXML
        private TextField txtamount1;

        @FXML
        private TextField txtamount2;

        @FXML
        private TextField txtprice1;

        @FXML
        private TextField txtprice2;

        @FXML
        private TextField txttype1;

        @FXML
        private TextField txttype2;

        @FXML
        private TextField txttype3;

        // Methods available only for Admin

        // To delete existing medical from Database
        @FXML
        void Delete(ActionEvent event) {
            String name = txttype3.getText();
            String status = user.delete(name);
            lableAdmin.setText(status);
        }

        // To add new medical into Database
        @FXML
        void Insert(ActionEvent event) {
            String name = txttype1.getText();
            double price = Double.parseDouble(txtprice1.getText());
            int amount = Integer.parseInt(txtamount1.getText());
            String status = user.write(name, price, amount);
            lableAdmin.setText(status);
        }

        // To update information about existing medical in Database
        @FXML
        void Update(ActionEvent event) {
            String name = txttype2.getText();
            double price = Double.parseDouble(txtprice2.getText());
            int amount = Integer.parseInt(txtamount2.getText());
            String status = user.update(name, price, amount);
            lableAdmin.setText(status);
        }

    // Creating object of User class to have a connection with Server and access Database
    private static User user;

    private String currentMedical;
    private double currentPrice;
    private int currentAmount;

    // To check which window to open (Admin or User) or not to open at all
    public void Login(ActionEvent event) throws Exception {

        // checking whether password and login of User are correct
        if (
                (txtPassword.getText().equals("pass") && txtUserName.getText().equals("Bekhruz") )
                ||(txtPassword.getText().equals("pass") && txtUserName.getText().equals("Alisher") )
                ||(txtPassword.getText().equals("pass") && txtUserName.getText().equals("Nodir") )
                ||(txtPassword.getText().equals("pass") && txtUserName.getText().equals("Ulugbek") )
        ) {
            user = new User();

            lblStatus.setText("Login Success");

            // opening window for User
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Main.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1390, 780);
            stage.setTitle("Pharmacy App");

            // closing Login window
            Stage subStage = (Stage) submit.getScene().getWindow();
            subStage.close();

            stage.setScene(scene);
            stage.show();

        }

        // checking whether password and login of Admin are correct
        else if(txtPassword.getText().equals("pass") && txtUserName.getText().equals("Admin") || txtID.getText().equals("1")){
            user = new User();

            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("admin.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 400);
            stage.setTitle("Admin");

            Stage subStage = (Stage) submit.getScene().getWindow();
            subStage.close();

            stage.setScene(scene);
            stage.show();
        }

        // Otherwise, if password or login are incorrect
        else {
            lblStatus.setText("Wrong admin or password.");
        }

    }

    // Method which will be implemented when ORDER button is clicked (When User wants to order some medical)
    public void order(ActionEvent event)
    {
        String result;
        int checkAmount = user.readAmount(currentMedical);

        // checking whether med store has enough medicals
        if (checkAmount>0)
        {
            int orderedAmount = amountSpinner.getValue();
            result = user.minusAmount(currentMedical, orderedAmount);
        }
        else
        {
            result = "No " + currentMedical + " is left";
        }
        orderResult.setText(result);
    }

    // Spinner in GUI
    private SpinnerValueFactory<Integer> valueFactory;

    public void Logine(ActionEvent event) throws Exception {

        // getting List of all appropriate medicals for Users input
        List<String> searchingNames = user.readSearchResults(txtSearch.getText());

        System.out.println(searchingNames.size());

        // adding all medicals in the List into ListView in GUI
        lblinfo1.setItems(FXCollections.observableList(searchingNames));

        lblinfo1.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

            // operation to be done when User choose particular medical from ListView in GUI
            @Override
            public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {

                // to know which medical was chosen
                currentMedical = lblinfo1.getSelectionModel().getSelectedItem();

                // to show name of this medical in GUI
                lbltype.setText(currentMedical);

                // getting price of chosen medical
                currentPrice = user.readPrice(currentMedical);

                // to show price of this medical in GUI
                lblprice.setText(String.valueOf(currentPrice));

                // getting existing amount of chosen medical
                currentAmount = user.readAmount(currentMedical);

                // existing amount should be bigger than 0
                if(currentAmount>0)
                {
                    // user can choose from 1 to maximum existing number of medicals to order
                    valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, currentAmount);
                    valueFactory.setValue(1);
                }
                else
                {
                    // User cannot choose number of medicals to order because no medical with this name left
                    valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 0);
                    valueFactory.setValue(0);
                }

                // setting these values to spinner
                amountSpinner.setValueFactory(valueFactory);

                amountSpinner.valueProperty().addListener(new ChangeListener<Integer>() {
                    // method to be implemented when User changes amount of medicals to order
                    @Override
                    public void changed(ObservableValue<? extends Integer> arg0, Integer arg1, Integer arg2) {
                        // now price is shown for chosen amount of medicals
                        currentPrice = user.readPrice(currentMedical) * amountSpinner.getValue();
                        lblprice.setText(String.valueOf(currentPrice));
                    }
                });
            }
        });
    }

    public void Logine11(ActionEvent event) throws Exception {    //button identification

            lbltype.setText("Deocil");    //inserting fixed information to advertisement board
            lblprice.setText("75$");
            lblinfo77.setText("Deocil Tablet is used for the control work");
            lblinfo2.setText("by reducing hormones that cause inflammation");
            lblinfo3.setText("and pain in the bod");
    }

    public void shopnow(ActionEvent event) throws Exception {    //button identification
        Random rand = new Random();
        int random = rand.nextInt(6 - 1 + 1) + 1;
        if (random == 1) {

            lbltype.setText("Deocil");    //inserting fixed information to advertisement board
            lblprice.setText("75$ => 60");
            lblinfo77.setText("Deocil Tablet is used for the control work");
            lblinfo2.setText("by reducing hormones that cause inflammation");
            lblinfo3.setText("and pain in the bod");

        } else if (random == 2) {
            lbltype.setText("Paracetamol");    //inserting fixed information to advertisement board
            lblprice.setText("20$ => 16$");
            lblinfo77.setText("Paracetamol is a commonly used medicine that");
            lblinfo2.setText("can help treat pain and reduce a high ");
            lblinfo3.setText("temperature (fever).");
        } else if (random == 3) {

            lbltype.setText("Detrusitol");    //inserting fixed information to advertisement board
            lblprice.setText("44$ => 35$");
            lblinfo77.setText("DETRUSITOL is used to treat symptoms of an");
            lblinfo2.setText("overactive bladder, for example urinary");
            lblinfo3.setText("frequency, urgency or incontinence.");

        } else if (random == 4) {

            lbltype.setText("Lnauga");    //inserting fixed information to advertisement board
            lblprice.setText("116$ => 93$");
            lblinfo77.setText("Lnauga is used to control blood sugar in");
            lblinfo2.setText("people who have type 1 diabetes (condition in");
            lblinfo3.setText("which the body does not make insulin)");

        } else if (random == 5) {
            lbltype.setText("Moment");    //inserting fixed information to advertisement board
            lblprice.setText("86$ => 69$");
            lblinfo77.setText("Moment(over-the-counter) omeprazole is used to");
            lblinfo2.setText("treat frequent heartburn (heartburn that");
            lblinfo3.setText("occurs at least 2 days a week) in adults.");
        } else if (random == 6){

            lbltype.setText("Amlodipine");    //inserting fixed information to advertisement board
            lblprice.setText("109$ => 87$");
            lblinfo77.setText("Amlodipine is a medicine used to treat high");
            lblinfo2.setText("blood pressure(hyperteion). Amlodipine helps");
            lblinfo3.setText("prevent disease, heart attacks and strokes.");

        }
    }

    public void Logine22(ActionEvent event) throws Exception {    //button identification
        lbltype.setText("Paracetamol");    //inserting fixed information to advertisement board
        lblprice.setText("20$");
        lblinfo77.setText("Paracetamol is a commonly used medicine that");
        lblinfo2.setText("can help treat pain and reduce a high ");
        lblinfo3.setText("temperature (fever).");
    }

    public void Logine33(ActionEvent event) throws Exception {
            lbltype.setText("Detrusitol");    //inserting fixed information to advertisement board
            lblprice.setText("44$");
            lblinfo77.setText("DETRUSITOL is used to treat symptoms of an");
            lblinfo2.setText("overactive bladder, for example urinary");
            lblinfo3.setText("frequency, urgency or incontinence.");

    }

    public void Logine44(ActionEvent event) throws Exception {    //button identification

            lbltype.setText("Lnauga");    //inserting fixed information to advertisement board
            lblprice.setText("116$");
            lblinfo77.setText("Lnauga is used to control blood sugar in");
            lblinfo2.setText("people who have type 1 diabetes (condition in");
            lblinfo3.setText("which the body does not make insulin)");
    }

    public void Logine55(ActionEvent event) throws Exception {    //button identification

            lbltype.setText("Moment");    //inserting fixed information to advertisement board
            lblprice.setText("86$");
            lblinfo77.setText("Moment(over-the-counter) omeprazole is used to");
            lblinfo2.setText("treat frequent heartburn (heartburn that");
            lblinfo3.setText("occurs at least 2 days a week) in adults.");
    }

    public void Logine66(ActionEvent event) throws Exception {    //button identification
            lbltype.setText("Amlodipine");    //inserting fixed information to advertisement board
            lblprice.setText("109$");
            lblinfo77.setText("Amlodipine is a medicine used to treat high");
            lblinfo2.setText("blood pressure(hyperteion). Amlodipine helps");
            lblinfo3.setText("prevent disease, heart attacks and strokes.");
    }
}

