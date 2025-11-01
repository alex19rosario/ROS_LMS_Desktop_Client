package com.ros.lmsdesktopclient.di.modules;

import com.ros.lmsdesktopclient.commands.Command;
import com.ros.lmsdesktopclient.models.*;
import com.ros.lmsdesktopclient.util.UiExecutor;
import com.ros.lmsdesktopclient.util.enums.CommandType;
import com.ros.lmsdesktopclient.util.enums.PropertyType;
import com.ros.lmsdesktopclient.view_models.*;
import dagger.Module;
import dagger.Provides;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.collections.ObservableList;

import javax.inject.Singleton;
import java.util.Map;


@Module
public abstract class ViewModelModule {

    @Provides
    @Singleton
    static AddBookViewModel addBookViewModel(
            Map<CommandType, Command> commands,
            BookModel bookModel,
            ObservableList<GenreModel> genreModelObservableList,
            ListProperty<AuthorModel> authorModelListProperty
    ) {
        return new AddBookViewModel(commands, bookModel, genreModelObservableList, authorModelListProperty);
    }

    @Provides
    @Singleton
    static AddMemberViewModel addMemberViewModel(Map<CommandType, Command> commands, MemberModel memberModel) {
        return new AddMemberViewModel(commands, memberModel);
    }

    @Provides
    @Singleton
    static IssueBookViewModel issueBookViewModel(
            Map<PropertyType, Property> properties,
            SearchBookModel searchBookModel,
            ListProperty<BookDisplayModel> books,
            SelectedBookModel selectedBookModel,
            ObjectProperty<BookDisplayModel> selectedRowModel,
            Map<CommandType, Command> commands,
            UiExecutor uiExecutor
    ) {
        return new IssueBookViewModel(properties, searchBookModel, books, selectedBookModel, selectedRowModel, commands, uiExecutor);
    }

    @Provides
    @Singleton
    static ReturnBookViewModel returnBookViewModel(ReturnBookModel returnBookModel, Map<CommandType, Command> commands) {
        return new ReturnBookViewModel(returnBookModel, commands);
    }

    @Provides
    @Singleton
    static LoginViewModel loginViewModel(LoginModel loginModel, Map<CommandType, Command> commands) {
        return new LoginViewModel(loginModel, commands);
    }

    @Provides
    @Singleton
    static MainMenuViewModel mainMenuViewModel(Map<CommandType, Command> commands) {
        return new MainMenuViewModel(commands);
    }

}
