package com.stackroute.movieApp.service;

import com.stackroute.movieApp.domain.Movie;
import com.stackroute.movieApp.exceptions.MovieAlreadyExistsException;
import com.stackroute.movieApp.exceptions.MovieNotFoundException;

import java.util.List;

public interface MovieService {
    public Movie saveMovie(Movie movie) throws MovieAlreadyExistsException;
    public Movie updateMovie(Movie movie) throws MovieNotFoundException, MovieAlreadyExistsException;
    public Movie deleteMovie(int id) throws MovieNotFoundException;
    public Movie getMovie(int id) throws MovieNotFoundException;
    public List<Movie> getMovieByName(String title);
    public List<Movie> getMovies();
}
