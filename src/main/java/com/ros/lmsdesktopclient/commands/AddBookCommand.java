package com.ros.lmsdesktopclient.commands;

import com.ros.lmsdesktopclient.dtos.AddBookDTO;
import com.ros.lmsdesktopclient.models.AuthorModel;
import com.ros.lmsdesktopclient.models.BookModel;
import com.ros.lmsdesktopclient.models.GenreModel;
import com.ros.lmsdesktopclient.services.service.BookService;
import com.ros.lmsdesktopclient.util.ViewHandler;
import com.ros.lmsdesktopclient.util.enums.AlertContents;
import com.ros.lmsdesktopclient.util.enums.Alerts;
import com.ros.lmsdesktopclient.util.TokenHandler;
import com.ros.lmsdesktopclient.util.enums.ViewType;
import com.ros.lmsdesktopclient.util.exceptions.*;
import javafx.beans.property.ListProperty;
import javafx.collections.ObservableList;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

public class AddBookCommand extends Command{

    private final BookModel book;
    private final ListProperty<AuthorModel> authorModelListProperty;
    private final BookService bookService;
    private final Command openLoginViewCommand;
    private final ObservableList<GenreModel> genreModelObservableList;
    private Throwable lastException; // store exception for onFailure()

    @Inject
    public AddBookCommand(
            BookModel book,
            ListProperty<AuthorModel> authorModelListProperty,
            BookService bookService,
            ObservableList<GenreModel> genreModelObservableList,
            ExecutorService executorService,
            ViewHandler viewHandler
    ){
        super(executorService);
        this.book = book;
        this.authorModelListProperty = authorModelListProperty;
        this.genreModelObservableList = genreModelObservableList;
        this.bookService = bookService;
        this.openLoginViewCommand = new OpenViewCommand(ViewType.LOGIN, executorService, viewHandler);
        this.setOnCommandSuccess(this::onSuccess);
        this.setOnCommandFailure(this::onFailure);
    }

    @Override
    protected void runCommand() throws Exception {
        try {
            List<AuthorModel> authorModelList = authorModelListProperty.stream().toList();
            checkForm(book, authorModelList);
            String authorsString = authorModelList.stream()
                    .map(author -> author.getFirstName().toUpperCase() + "-" + author.getLastName().toUpperCase())
                    .collect(Collectors.joining(","));

            String genresString = genreModelObservableList.stream()
                    .filter(GenreModel::isSelected)
                    .map(GenreModel::getGenre)
                    .collect(Collectors.joining(","));

            String staffUsername = TokenHandler.getInstance().getUsername();

            AddBookDTO bookDTO = new AddBookDTO(book.getIsbn(), book.getTitle(), authorsString, genresString, staffUsername, book.getCoverImageFile());
            bookService.addBook(bookDTO);
        } catch (Exception ex) {
            this.lastException = ex;
            throw ex; // triggers failure in base class
        }

    }

    private void onSuccess(){
        setAlert(Alerts.BOOK_ADDED_SUCCESS);
        getAlert().getModal(AlertContents.BOOK_ADDED_OK.getValue());
        //To reset the screen
        book.clear();
        authorModelListProperty.clear();

        //Inserting the first empty author to display the text-fields in table-view
        authorModelListProperty.addFirst(new AuthorModel());
        genreModelObservableList.forEach(genre -> genre.setSelected(false));
    }

    private void onFailure(){

        if (lastException != null) {
            Alerts alert = switch (lastException){
                case EmptyFieldsException ignored -> Alerts.EMPTY_FIELDS_WARN;
                case NetworkException ignored -> Alerts.NETWORK_ERROR;
                case ServerErrorException ignored -> Alerts.SERVER_ERROR;
                case InvalidISBNException ignored -> Alerts.INVALID_ISBN_ERROR;
                case ExpiredSessionException ignored -> Alerts.EXPIRED_SESSION_ERROR;
                case BookAlreadyExistException ignored -> Alerts.EXISTING_BOOK_ERROR;
                default -> throw new IllegalStateException("Unexpected exception: " + lastException);
            };

            setAlert(alert);
            getAlert().getModal(lastException.getMessage());
        }

        if(lastException instanceof ExpiredSessionException){
            openLoginViewCommand.execute();
        }
    }

    private void checkForm(BookModel bookModel, List<AuthorModel> authorModels) throws EmptyFieldsException {
        if (!bookModel.isComplete() || authorModels.isEmpty() || !authorModels.stream().allMatch(AuthorModel::isComplete)) {
            throw new EmptyFieldsException("Add Book Form: there are empty fields");
        }
    }


}
