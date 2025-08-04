package com.ros.lmsdesktopclient.commands;

import com.ros.lmsdesktopclient.dtos.BookDTO;
import com.ros.lmsdesktopclient.dtos.PaginatedBooksDTO;
import com.ros.lmsdesktopclient.dtos.SearchBookDTO;
import com.ros.lmsdesktopclient.models.BookDisplayModel;
import com.ros.lmsdesktopclient.models.SearchBookModel;
import com.ros.lmsdesktopclient.services.service.BookService;
import com.ros.lmsdesktopclient.util.enums.BookStatus;
import com.ros.lmsdesktopclient.util.enums.GenreType;
import com.ros.lmsdesktopclient.util.enums.PropertyType;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.Property;
import javafx.concurrent.Task;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class LoadBooksCommand extends Command{

    private final IntegerProperty totalPages;
    private final SearchBookModel searchBookModel;
    private final ListProperty<BookDisplayModel> books;
    private final BookService bookService;

    @Inject
    public LoadBooksCommand(Map<PropertyType, Property> properties, SearchBookModel searchBookModel, ListProperty<BookDisplayModel> books, BookService bookService){
        this.totalPages = (IntegerProperty) properties.get(PropertyType.TOTAL_PAGES);
        this.searchBookModel = searchBookModel;
        this.books = books;
        this.bookService = bookService;
    }

    @Override
    protected Task<Void> createCommandTask() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                SearchBookDTO searchBookDTO = mapToSearchBookDTO.apply(searchBookModel);

                PaginatedBooksDTO paginatedBooksDTO = bookService.searchBooks(searchBookDTO);

                List<BookDTO> bookDTOList = paginatedBooksDTO.bookDTOList();

                List<BookDisplayModel> bookDisplayModelList = bookDTOList.stream()
                        .map(LoadBooksCommand.this::mapToDisplayModel)
                        .toList();

                Platform.runLater(() -> {
                    books.setAll(bookDisplayModelList);
                    totalPages.set(paginatedBooksDTO.totalPages());
                    searchBookModel.setSize(paginatedBooksDTO.size());
                });

                return null;
            }
        };
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
