package com.ros.lmsdesktopclient.commands;

import com.ros.lmsdesktopclient.services.service.GenreService;
import com.ros.lmsdesktopclient.util.UpFrontDataHandler;

import javax.inject.Inject;
import java.util.concurrent.ExecutorService;

public class GetGenresCommand extends Command{

    private final GenreService genreService;
    private final UpFrontDataHandler upFrontDataHandler;

    @Inject
    public GetGenresCommand(GenreService genreService, ExecutorService executorService) {
        super(executorService);
        this.genreService = genreService;
        upFrontDataHandler = UpFrontDataHandler.getInstance();
    }

    @Override
    protected void runCommand() throws Exception {
        upFrontDataHandler.saveGenres(genreService.getAllGenres());
    }
}
