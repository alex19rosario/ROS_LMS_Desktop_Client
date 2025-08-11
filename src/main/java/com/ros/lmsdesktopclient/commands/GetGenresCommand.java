package com.ros.lmsdesktopclient.commands;

import com.ros.lmsdesktopclient.services.service.GenreService;
import com.ros.lmsdesktopclient.util.UpFrontDataHandler;
import javafx.concurrent.Task;

import javax.inject.Inject;

public class GetGenresCommand extends Command{

    private final GenreService genreService;
    private final UpFrontDataHandler upFrontDataHandler;

    @Inject
    public GetGenresCommand(GenreService genreService) {
        this.genreService = genreService;
        upFrontDataHandler = UpFrontDataHandler.getInstance();
    }

    @Override
    protected Task<Void> createCommandTask() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                upFrontDataHandler.saveGenres(genreService.getAllGenres());
                return null;
            }
        };
    }
}
