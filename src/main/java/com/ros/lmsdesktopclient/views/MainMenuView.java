package com.ros.lmsdesktopclient.views;

import com.ros.lmsdesktopclient.util.Roles;
import com.ros.lmsdesktopclient.util.TokenHandler;
import com.ros.lmsdesktopclient.view_models.MainMenuViewModel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;

public class MainMenuView implements BaseView{

    private MainMenuViewModel mainMenuViewModel;

    private Label lblHeaderTitle;
    private MenuButton menuBtnAccount;
    private MenuItem menuItemUpdate;
    private MenuItem menuItemLogOut;
    private Button btnAddBook;
    private Button btnAddMember;
    private Button btnIssueBook;
    private Button btnReturnBook;
    private Button btnManageBooks;
    private Button btnShowStats;
    private Button btnManageAccounts;
    private Button btnAddStaff;

    @Override
    public void start(Stage stage) {
        mainMenuViewModel = new MainMenuViewModel();
        initComponents();
        Scene scene = new Scene(createContent(), stage.getScene().getWidth(), stage.getScene().getWidth());
        bindComponents();
        stage.setScene(scene);
    }

    private void initComponents() {
        lblHeaderTitle = new Label("Main Menu");
        menuBtnAccount = new MenuButton(TokenHandler.getInstance().getUsername());
        menuItemUpdate = new MenuItem("Update Account");
        menuItemLogOut = new MenuItem("Log Out");
        menuBtnAccount.getItems().addAll(menuItemUpdate, menuItemLogOut);
        btnAddBook = new Button("Add Book");
        btnAddMember = new Button("Add Member");
        btnIssueBook = new Button("Issue Book");
        btnReturnBook = new Button("Return Book");
        btnManageBooks = new Button("Manage Books");
        btnShowStats = new Button("Show Stats");
        btnManageAccounts = new Button("Manage Accounts");
        btnAddStaff = new Button("Add Staff");

        //Render GUI according to role
        if(!TokenHandler.getInstance().getAuthorities().contains(Roles.ADMIN.str())){
            btnManageAccounts.setVisible(false);
            btnAddStaff.setVisible(false);
        }
    }

    private void bindComponents() {
        menuItemUpdate.setOnAction(actionEvent -> System.out.println("Updating..."));
        menuItemLogOut.setOnAction(actionEvent -> mainMenuViewModel.executeLogOutCommand());
        btnAddBook.setOnAction(actionEvent -> mainMenuViewModel.executeOpenAddBookViewCommand());
        btnAddMember.setOnAction(actionEvent -> mainMenuViewModel.executeOpenAddMemberViewCommand());
        btnIssueBook.setOnAction(actionEvent -> mainMenuViewModel.executeOpenIssueBookViewCommand());
    }

    private Region createContent() {
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(createHeader());
        borderPane.setCenter(createMenu());
        return borderPane;
    }

    private Node createHeader() {
        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(25));
        borderPane.setCenter(lblHeaderTitle);
        borderPane.setRight(menuBtnAccount);
        return borderPane;
    }

    private Node createMenu() {
        setSizeToAll(150,
                btnAddBook,
                btnAddMember,
                btnIssueBook,
                btnReturnBook,
                btnManageBooks,
                btnShowStats,
                btnManageAccounts,
                btnAddStaff);
        TilePane tilePane = new TilePane();
        tilePane.setHgap(50);
        tilePane.setVgap(50);
        tilePane.setPadding(new Insets(50));
        tilePane.setAlignment(Pos.CENTER);
        tilePane.getChildren()
                .addAll(btnAddBook,
                        btnAddMember,
                        btnIssueBook,
                        btnReturnBook,
                        btnManageBooks,
                        btnShowStats,
                        btnManageAccounts,
                        btnAddStaff);
        return tilePane;
    }

    private void setSizeToAll(int size, Button... buttons) {
        for (Button button : buttons) {
            button.setPrefWidth(size);
            button.setPrefHeight(size);
        }
    }
}
