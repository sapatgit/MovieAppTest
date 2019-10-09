package com.stackroute.movieApp.service;


import com.stackroute.movieApp.domain.Movie;
import com.stackroute.movieApp.exceptions.MovieAlreadyExistsException;
import com.stackroute.movieApp.exceptions.MovieNotFoundException;
import com.stackroute.movieApp.repository.MovieRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class MovieServiceTest {
    Movie movie;

    @Mock
    MovieRepository movieRepository;

    @InjectMocks
    MovieServiceImpl movieService;
    List<Movie> movieList;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        movie = new Movie("Deadpool", (float) 8.1, "Marvel action", "22/10/2018");
        movieList = new ArrayList<Movie>();
        movieList.add(movie);
    }

    @Test
    public void saveMovieSuccess() throws MovieAlreadyExistsException {
        when(movieRepository.save((Movie)any())).thenReturn(movie);
        when(movieRepository.findDuplicate((String)any(), (String)any())).thenReturn(null);
        Movie savedMovie = movieService.saveMovie(movie);

        verify(movieRepository, times(1)).save(movie);
        verify(movieRepository, times(1)).findDuplicate(movie.getTitle(), movie.getReleaseDate());
    }

    @Test(expected = MovieAlreadyExistsException.class)
    public void saveMovieFailure() throws MovieAlreadyExistsException{
        when(movieRepository.findDuplicate((String)any(), (String)any())).thenReturn(movie);
        movieService.saveMovie(movie);
    }

    @Test
    public void updateMovieSuccess() throws MovieAlreadyExistsException, MovieNotFoundException {
        when(movieRepository.existsById((Integer) any())).thenReturn(true);
        when(movieRepository.findById((Integer) any())).thenReturn(java.util.Optional.ofNullable(movie));
        when(movieRepository.findDuplicate((String)any(), (String)any())).thenReturn(null);
        when(movieRepository.save((Movie)any())).thenReturn(movie);
        movieService.updateMovie(movie);

        verify(movieRepository, times(1)).existsById(movie.getId());
        verify(movieRepository, times(1)).findById(movie.getId());
        verify(movieRepository, times(1)).findDuplicate(movie.getTitle(), movie.getReleaseDate());
        verify(movieRepository, times(1)).save(movie);
    }

    @Test(expected = MovieAlreadyExistsException.class)
    public void updateMovieFailure_MovieAlreadyExists() throws MovieNotFoundException, MovieAlreadyExistsException {
        when(movieRepository.existsById((Integer) any())).thenReturn(true);
        when(movieRepository.findById((Integer) any())).thenReturn(java.util.Optional.ofNullable(movie));
        when(movieRepository.findDuplicate((String)any(), (String)any())).thenReturn(movie);
        movieService.updateMovie(movie);
    }

    @Test(expected = MovieNotFoundException.class)
    public void updateMovieFailure_MovieNotFound() throws MovieAlreadyExistsException, MovieNotFoundException {
        when(movieRepository.existsById((Integer) any())).thenReturn(false);
        movieService.updateMovie(movie);
    }

    @Test
    public void deleteMovieSuccess() throws MovieNotFoundException {
        when(movieRepository.existsById((Integer)any())).thenReturn(true);
        when(movieRepository.findById((Integer)any())).thenReturn(java.util.Optional.ofNullable(movie));
        movieService.deleteMovie(movie.getId());

        verify(movieRepository, times(2)).existsById(movie.getId());
        verify(movieRepository, times(1)).findById(movie.getId());
        verify(movieRepository, times(1)).deleteById(movie.getId());
    }

    @Test(expected = MovieNotFoundException.class)
    public void deleteMovieFailure() throws MovieNotFoundException {
        when(movieRepository.existsById((Integer)any())).thenReturn(false);
        movieService.deleteMovie(movie.getId());
    }

    @Test
    public void getMoviesSuccess() {
        when(movieRepository.findAll()).thenReturn(movieList);
        movieService.getMovies();

        verify(movieRepository, times(1)).findAll();
    }

    @Test
    public void getMovieSuccess() throws MovieNotFoundException {
        when(movieRepository.existsById((Integer) any())).thenReturn(true);
        when(movieRepository.findById((Integer)any())).thenReturn(java.util.Optional.ofNullable(movie));
        movieService.getMovie(movie.getId());

        verify(movieRepository, times(1)).existsById(movie.getId());
        verify(movieRepository, times(1)).findById(movie.getId());
    }

    @Test(expected = MovieNotFoundException.class)
    public void getMovieFailure() throws MovieNotFoundException {
        when(movieRepository.existsById((Integer) any())).thenReturn(false);
        movieService.getMovie(movie.getId());
    }

    @Test
    public void getMovieByNameSuccess() {
        when(movieRepository.findMovieByName((String)any())).thenReturn(movieList);
        movieService.getMovieByName(movie.getTitle());
    }
}
