package com.ros.lmsdesktopclient.views;

import com.ros.lmsdesktopclient.util.enums.Roles;
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

import javax.inject.Inject;

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
    private TokenHandler tokenHandler;

    @Inject
    public MainMenuView(MainMenuViewModel mainMenuViewModel, TokenHandler tokenHandler) {
        this.mainMenuViewModel = mainMenuViewModel;
        this.tokenHandler = tokenHandler;
    }

    @Override
    public void start(Stage stage) {
        initComponents();
        Scene scene = new Scene(createContent(), stage.getScene().getWidth(), stage.getScene().getWidth());
        scene.getStylesheets().add(getClass().getResource("/styles/main-menu-view.css").toExternalForm());
        bindComponents();
        stage.setScene(scene);
    }

    private void initComponents() {
        lblHeaderTitle = new Label("Main Menu");
        menuBtnAccount = new MenuButton(tokenHandler.getUsername());
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
        if(!tokenHandler.getAuthorities().contains(Roles.ADMIN.str())){
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
        borderPane.getTop().setId("header");
        return borderPane;
    }

    private Node createHeader() {
        BorderPane header = new BorderPane();
        header.setPadding(new Insets(25));
        lblHeaderTitle.setId("lblHeaderTitle");
        header.setCenter(lblHeaderTitle);
        header.setRight(menuBtnAccount);
        header.setId("header");
        return header;
    }

    private Node createMenu() {
        setSizeToAll(160,
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
        tilePane.setPadding(new Insets(60));
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
        tilePane.setId("menu-container");
        return tilePane;
    }

    private void setSizeToAll(int size, Button... buttons) {
        for (Button button : buttons) {
            button.setPrefWidth(size);
            button.setPrefHeight(size);
        }
    }
}
