package com.ros.lmsdesktopclient.commands;

import com.ros.lmsdesktopclient.services.service.GenreService;
import com.ros.lmsdesktopclient.util.UpFrontDataHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;
import java.util.concurrent.ExecutorService;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetGenresCommandTest {

    @Mock
    GenreService genreService;
    @Mock
    UpFrontDataHandler upFrontDataHandler;
    @Mock
    ExecutorService executorService;

    GetGenresCommand command;

    @BeforeEach
    void setup() {
        command = new GetGenresCommand(genreService, executorService, upFrontDataHandler);
    }

    @Test
    void runCommand_shouldSaveAllGenres() throws Exception {
        Set<String> genres = Set.of("SciFi", "Fantasy");
        when(genreService.getAllGenres()).thenReturn(genres);

        command.runCommand();

        verify(upFrontDataHandler).saveGenres(genres);
    }

    @Test
    void runCommand_shouldRethrowExceptionFromGenreService() throws Exception {
        when(genreService.getAllGenres()).thenThrow(new RuntimeException("fail"));

        assertThrows(RuntimeException.class, command::runCommand);
        verify(upFrontDataHandler, never()).saveGenres(any());
    }
}
