package com.stackroute.movieApp.runner;

import com.stackroute.movieApp.domain.Movie;
import com.stackroute.movieApp.service.MovieService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource(value = {"classpath:application.properties"})
public class CommandLineStartupRunner implements CommandLineRunner {
    private MovieService movieService;
    @Value("${sampleMovie.title}")
    private String title;
    @Value("${sampleMovie.voteAverage}")
    private float voteAverage;
    @Value("${sampleMovie.overview}")
    private String overview;
    @Value("${sampleMovie.releaseDate}")
    private String releaseDate;

    public CommandLineStartupRunner(MovieService movieService) {
        this.movieService = movieService;
    }

    @Override
    public void run(String... args) throws Exception {
        Movie movie = new Movie(title, voteAverage, overview, releaseDate);
        movieService.saveMovie(movie);
    }
}
