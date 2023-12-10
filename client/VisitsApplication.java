package client;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.util.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import javafx.stage.WindowEvent;

public class VisitsApplication extends Application {

    private DatePicker dateControl;

    private TextField hourControl;

    private TextField phoneNumberControl;

    private final ObservableList<VisitProjection> visitData = FXCollections.observableArrayList();

    private MessageListener messageListener;

    private ServerConnector serverConnector;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        messageListener = new MessageListener();
        addMessageHandlers();
        serverConnector = new ServerConnector(messageListener);
        serverConnector.start();
        serverConnector.sendListVisitsRequest();

        GridPane grid = new GridPane();
        grid.setVgap(5);
        grid.setHgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));
        addHoursField(grid);
        addPhoneField(grid);
        addDatePicker(grid);
        addAddVisitButton(grid);
        addTable(grid);

        primaryStage.setTitle("HairdresserSalon Application");
        Scene scene = new Scene(grid);
        primaryStage.setScene(scene);

        primaryStage.setOnCloseRequest((WindowEvent we) -> {
            try {
                serverConnector.sendDisconnectMessage();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        primaryStage.show();
    }

    private void addMessageHandlers() {
        messageListener.addClientDisconnectedHandler((message) -> {
            try {
                serverConnector.getSocket().close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        messageListener.addAddVisitHandler((message) -> {
            Platform.runLater(() -> {
                if (!message.isSuccess()) {
                    showAlert("Failed to add visit");
                }
            });
        });
        messageListener.addVisitsListHandler((message) -> {
            Platform.runLater(() -> {
                visitData.setAll(VisitProjection.fromVisitDtos(message.getVisits()));
            });
        });
        messageListener.addRemoveVisitHandler((message) -> {
            Platform.runLater(() -> {
                if (!message.isSuccess()) {
                    showAlert("Failed to remove visit");
                }
            });
        });
    }
    private void addHoursField(GridPane parent) {
        TextField hourField = new TextField();
        hourField.setPromptText("Hour");
        GridPane.setConstraints(hourField, 1, 0);
        hourField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty() || !newValue.matches("\\d*")) {
                hourField.setText("");
            } else {
                int hour = Integer.parseInt(newValue);
                if (hour > 17) {
                    hourField.setText(oldValue);
                }
            }
        });
        parent.getChildren().add(hourField);
        this.hourControl = hourField;
    }

    private void addDatePicker(GridPane parent) {
        dateControl = new DatePicker();
        GridPane.setConstraints(dateControl, 0, 0);
        parent.getChildren().add(dateControl);
    }

    private void addPhoneField(GridPane parent) {
        TextField phoneField = new TextField();
        phoneField.setPromptText("Phone Number");
        GridPane.setConstraints(phoneField, 2, 0);
        parent.getChildren().add(phoneField);
        this.phoneNumberControl = phoneField;
    }

    private void addAddVisitButton(GridPane parent) {
        Button addButton = new Button("Add Visit");
        GridPane.setConstraints(addButton, 3, 0);
        addButton.setOnAction((event) -> {
            Date date = convertLocalDateToDate(dateControl.getValue());
            int hours = Integer.parseInt(hourControl.getText());
            String phoneNumber = phoneNumberControl.getText();
            try {
                serverConnector.sendAddVisit(date, hours, phoneNumber);
            } catch (IOException e) {
                showAlert(e.getCause().toString());
            }
        });
        parent.getChildren().add(addButton);
    }

    private void addTable(GridPane parent) {
        TableView<VisitProjection> table = new TableView<>();
        GridPane.setConstraints(table, 0, 1, 4, 1);
        table.setEditable(false);

        TableColumn<VisitProjection, LocalDate> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDate()));

        TableColumn<VisitProjection, Integer> hourColumn = new TableColumn<>("Hour");
        hourColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getHour()));

        TableColumn<VisitProjection, String> phoneColumn = new TableColumn<>("Phone Number");
        phoneColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getPhoneNumber()));

        TableColumn<VisitProjection, Void> removeVisitColumn = getVisitProjectionVoidTableColumn();


        table.getColumns().addAll(dateColumn, hourColumn, phoneColumn, removeVisitColumn);
        table.setItems(visitData);
        parent.getChildren().add(table);
    }

    private TableColumn<VisitProjection, Void> getVisitProjectionVoidTableColumn() {
        TableColumn<VisitProjection, Void> removeVisitColumn = new TableColumn<>("Remove");
        removeVisitColumn.setCellFactory(col -> new TableCell<VisitProjection, Void>() {
            private final Button btn = new Button("Remove");

            {
                btn.setOnAction(event -> {
                    VisitProjection visit = getTableView().getItems().get(getIndex());
                    if (visit.isRemovable()) {
                        try {
                            serverConnector.sendRemoveVisit(visit.getVisitId());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    VisitProjection visit = getTableView().getItems().get(getIndex());
                    if (visit.isRemovable()) {
                        setGraphic(btn);
                    } else {
                        setGraphic(null);
                    }
                }
            }
        });
        return removeVisitColumn;
    }

    public static void showAlert(String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(content);
        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        delay.setOnFinished(event -> {
            Platform.runLater(alert::hide);
        });
        alert.show();
        delay.play();
    }

    public static Date convertLocalDateToDate(LocalDate localDate) {
        ZonedDateTime zonedDateTime = localDate.atStartOfDay(ZoneId.systemDefault());
        return Date.from(zonedDateTime.toInstant());
    }

}
