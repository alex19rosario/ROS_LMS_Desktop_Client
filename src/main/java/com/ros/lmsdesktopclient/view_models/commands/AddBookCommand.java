package com.ros.lmsdesktopclient.view_models.commands;

import com.ros.lmsdesktopclient.dtos.AuthorDTO;
import com.ros.lmsdesktopclient.dtos.BookDTO;
import com.ros.lmsdesktopclient.models.AuthorModel;
import com.ros.lmsdesktopclient.models.BookModel;
import com.ros.lmsdesktopclient.services.service.AddBookService;
import com.ros.lmsdesktopclient.util.Alerts;
import com.ros.lmsdesktopclient.util.ViewHandler;
import com.ros.lmsdesktopclient.util.Views;
import com.ros.lmsdesktopclient.util.exceptions.*;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;

import java.net.http.HttpClient;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class AddBookCommand extends Command{

    private BookModel book;
    private List<AuthorModel> authors;
    private AddBookService addBookService;

    public AddBookCommand(BookModel book, List<AuthorModel> authors, AddBookService addBookService){
        this.book = book;
        this.authors = authors;
        this.addBookService = addBookService;
        this.setOnCommandSuccess(this::onSuccess);
        this.setOnCommandFailure(this::onFailure);
    }

    @Override
    protected Task<Void> createCommandTask() {
        return new Task<>() {
            @Override
            protected Void call() throws Exception {
                try{
                    checkForm(book, authors);
                    Set<String> genres = book.getGenres()
                            .stream()
                            .map(StringProperty::get)
                            .map(String::toUpperCase)
                            .collect(Collectors.toSet());

                    Set<AuthorDTO> authorDTOS = authors.stream()
                            .map(author -> new AuthorDTO(author.getFirstName().toUpperCase(), author.getLastName().toUpperCase()))
                            .collect(Collectors.toSet());

                    BookDTO bookDTO = new BookDTO(book.getIsbn(), book.getTitle(), genres, authorDTOS);

                    addBookService.addBook(bookDTO, HttpClient.newHttpClient());

                } catch (EmptyFieldsException e){
                    setAlert(Alerts.EMPTY_FIELDS_WARN);
                    throw new RuntimeException("EMPTY FIELDS ERROR");
                } catch (InvalidISBNException e){
                    setAlert(Alerts.INVALID_ISBN_ERROR);
                    throw new RuntimeException("INVALID ISBN ERROR");
                } catch (NetworkException e){
                    setAlert(Alerts.NETWORK_ERROR);
                    throw new RuntimeException("NETWORK ERROR");
                } catch (ServerErrorException e){
                    setAlert(Alerts.SERVER_ERROR);
                    throw new RuntimeException("SERVER ERROR");
                } catch (ExpiredSessionException e){
                    setAlert(Alerts.EXPIRED_SESSION_ERROR);
                    throw new RuntimeException("EXPIRED SESSION ERROR");
                } catch (BookAlreadyExistException e){
                    setAlert(Alerts.EXISTING_BOOK_ERROR);
                    throw new RuntimeException("BOOK ALREADY EXIST ERROR");
                }
                setAlert(Alerts.BOOK_ADDED_SUCCESS);
                return null;
            }
        };
    }

    private void onSuccess(){
        getAlert().getModal();
        //To reset the screen
        double width = ViewHandler.getInstance().getSceneWidth();
        double height = ViewHandler.getInstance().getSceneHeight();
        ViewHandler.switchTo(Views.ADD_BOOK.getView(), width, height);
    }

    private void onFailure(){
        getAlert().getModal();
    }

    private void checkForm(BookModel bookModel, List<AuthorModel> authorModels) throws EmptyFieldsException {
        if (!bookModel.isComplete() || authorModels.isEmpty() || !authorModels.stream().allMatch(AuthorModel::isComplete)) {
            throw new EmptyFieldsException("Add Book Form: there are empty fields");
        }
    }
}
