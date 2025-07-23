package com.ros.lmsdesktopclient.commands;

import com.ros.lmsdesktopclient.dtos.BookDTO;
import com.ros.lmsdesktopclient.dtos.PaginatedBooksDTO;
import com.ros.lmsdesktopclient.dtos.SearchBookDTO;
import com.ros.lmsdesktopclient.models.BookDisplayModel;
import com.ros.lmsdesktopclient.models.SearchBookModel;
import com.ros.lmsdesktopclient.services.service.BookService;
import com.ros.lmsdesktopclient.util.Alerts;
import com.ros.lmsdesktopclient.util.BookStatus;
import com.ros.lmsdesktopclient.util.GenreType;
import com.ros.lmsdesktopclient.util.Views;
import com.ros.lmsdesktopclient.util.exceptions.*;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;

import java.util.List;
import java.util.function.Function;

public class SearchBooksCommand extends Command{

    private final StringProperty isbn;
    private final IntegerProperty totalPages;
    private final SearchBookModel searchBookModel;
    private final ListProperty<BookDisplayModel> books;
    private final BookService bookService;
    private final Command openLoginViewCommand;

    public SearchBooksCommand(StringProperty isbn, IntegerProperty totalPages, SearchBookModel searchBookModel, ListProperty<BookDisplayModel> books, BookService bookService) {
        this.isbn = isbn;
        this.totalPages = totalPages;
        this.searchBookModel = searchBookModel;
        this.books = books;
        this.bookService = bookService;
        this.openLoginViewCommand = new OpenViewCommand(Views.LOGIN);
        this.setOnCommandFailure(this::onFailure);
    }

    @Override
    protected Task<Void> createCommandTask() {
        return new Task<Void>() {
            @Override
            protected Void call() throws EmptyFieldsException, NetworkException, ServerErrorException, ExpiredSessionException, BookNotFoundException{

                // Check the form has at least  one search filter
                if(isbn.isNotEmpty().get()){
                    BookDTO bookDTO = bookService.searchBookByIsbn(isbn.get());
                    BookDisplayModel bookDisplayModel = mapToDisplayModel(bookDTO);
                    Platform.runLater(() -> {
                        searchBookModel.setPage(0);
                        books.setAll(bookDisplayModel);
                    });
                }
                else {
                    checkForm(searchBookModel);
                    // If form is valid, consume the service
                    PaginatedBooksDTO paginatedBooksDTO = bookService.searchBooks(mapToSearchBookDTO.apply(searchBookModel));
                    List<BookDTO> bookDTOList = paginatedBooksDTO.bookDTOList();
                    // Update the book-display-model list
                    List<BookDisplayModel> bookDisplayModels = bookDTOList.stream()
                            .map(SearchBooksCommand.this::mapToDisplayModel)
                            .toList();
                    Platform.runLater(() -> {
                        books.setAll(bookDisplayModels);
                        totalPages.set(paginatedBooksDTO.totalPages());
                        searchBookModel.setSize(paginatedBooksDTO.size());
                    });
                }
                return null;
            }
        };
    }


    private void onFailure() {
        Throwable exception = getCommandTask().getException();

        Alerts alert = switch (exception){
            case EmptyFieldsException ignored -> Alerts.EMPTY_FIELDS_WARN;
            case NetworkException ignored -> Alerts.NETWORK_ERROR;
            case ServerErrorException ignored -> Alerts.SERVER_ERROR;
            case ExpiredSessionException ignored -> Alerts.EXPIRED_SESSION_ERROR;
            case BookNotFoundException ignored -> Alerts.BOOK_NOT_FOUND;
            default -> throw new IllegalStateException("Unexpected exception: " + exception);
        };
        String content = exception.getMessage();
        setAlert(alert);
        getAlert().getModal(content);

        if(exception instanceof ExpiredSessionException){
            openLoginViewCommand.execute();
        }
    }

    private void checkForm(SearchBookModel searchBookModel) throws EmptyFieldsException {
        if(!searchBookModel.isComplete())
            throw new EmptyFieldsException("Search Books: Please fill out at least one search filter.");
    }

    private final Function<SearchBookModel, SearchBookDTO> mapToSearchBookDTO = model -> {

        // If isAvailable is null, it will not be considered for the filter; therefore, it will pull both available and unavailable books
        Boolean isAvailable = switch (model.getStatus()) {
            case null -> null;
            case String s when s.equalsIgnoreCase(BookStatus.AVAILABLE.toString()) -> true;
            default -> false;
        };

        GenreType genre = model.getGenre() == null ? null
                : GenreType.valueOf(model.getGenre());

        return new SearchBookDTO(
                model.getPage(),
                model.getSize(),
                model.getTitle(),
                genre,
                model.getAuthorFirstName(),
                model.getAuthorLastName(),
                isAvailable
        );
    };

    private BookDisplayModel mapToDisplayModel(BookDTO dto) {
        BookDisplayModel model = new BookDisplayModel();
        model.setId(dto.id());
        model.setIsbn(dto.isbn());
        model.setTitle(dto.title());

        String authors = dto.authors().stream()
                .map(a -> a.firstName() + " " + a.lastName())
                .sorted()
                .reduce((a, b) -> a + ", " + b)
                .orElse("");

        String genres = dto.genres().stream()
                .map(Enum::name)
                .sorted()
                .reduce((a, b) -> a + ", " + b)
                .orElse("");

        model.setAuthors(authors);
        model.setGenres(genres);
        model.setStatus(dto.status() ? BookStatus.AVAILABLE.toString() : BookStatus.UNAVAILABLE.toString());
        model.setImagePath(dto.imagePath());

        return model;
    }

}
