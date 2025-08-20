package com.ros.lmsdesktopclient.commands;

import com.ros.lmsdesktopclient.services.service.GenreService;
import com.ros.lmsdesktopclient.util.UiExecutor;
import com.ros.lmsdesktopclient.util.UpFrontDataHandler;

import javax.inject.Inject;
import java.util.concurrent.ExecutorService;

public class GetGenresCommand extends Command{

    private final GenreService genreService;
    private final UpFrontDataHandler upFrontDataHandler;

    @Inject
    public GetGenresCommand(
            GenreService genreService,
            ExecutorService executorService,
            UpFrontDataHandler upFrontDataHandler,
            UiExecutor uiExecutor
    ) {
        super(executorService, uiExecutor);
        this.genreService = genreService;
        this.upFrontDataHandler = upFrontDataHandler;
    }

    @Override
    protected void runCommand() throws Exception {
        upFrontDataHandler.saveGenres(genreService.getAllGenres());
    }
}
