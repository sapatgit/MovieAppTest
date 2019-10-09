package com.stackroute.movieApp.runner;

import com.stackroute.movieApp.domain.Movie;
import com.stackroute.movieApp.exceptions.MovieAlreadyExistsException;
import com.stackroute.movieApp.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties()
public class SpringContextListener implements ApplicationListener<ContextRefreshedEvent> {
    private MovieService movieService;

    @Autowired
    private Environment env;

    @Autowired
    public SpringContextListener(MovieService movieService) {
        this.movieService = movieService;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        Movie movie = new Movie(env.getProperty("anotherMovie.title"),
                Float.parseFloat(env.getProperty("anotherMovie.voteAverage")),
                env.getProperty("anotherMovie.overview"),
                env.getProperty("anotherMovie.releaseDate"));
        try {
            movieService.saveMovie(movie);
        } catch (MovieAlreadyExistsException e) {
            System.out.println(e.getMessage());
        }
    }
}
