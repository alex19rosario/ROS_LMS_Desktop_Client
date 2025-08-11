package com.ros.lmsdesktopclient.di.modules;

import com.ros.lmsdesktopclient.commands.Command;
import com.ros.lmsdesktopclient.models.*;
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
import java.util.List;
import java.util.Map;


@Module
public abstract class ViewModelModule {

    @Provides
    @Singleton
    static AddBookViewModel addBookViewModel(
            Map<CommandType, Command> commands,
            ListProperty<AuthorInputModel> authorInputs,
            BookModel bookModel,
            List<AuthorModel> authors,
            ObservableList<GenreModel> genreModelObservableList
    ) {
        return new AddBookViewModel(commands, authorInputs, bookModel, authors, genreModelObservableList);
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
            Map<CommandType, Command> commands
    ) {
        return new IssueBookViewModel(properties, searchBookModel, books, selectedBookModel, selectedRowModel, commands);
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
