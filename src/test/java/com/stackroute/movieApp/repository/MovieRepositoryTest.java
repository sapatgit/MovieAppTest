package com.stackroute.movieApp.repository;

import com.stackroute.movieApp.domain.Movie;
import com.stackroute.movieApp.exceptions.MovieAlreadyExistsException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
public class MovieRepositoryTest {
    @Autowired
    MovieRepository movieRepository;
    Movie movie;
    List<Movie> list = null;

    @Before
    public void setup() {
        movie = new Movie("Deadpool", (float) 8.1, "Marvel action", "22/10/2018");
        list = new ArrayList<Movie>();
        list.add(movie);
    }

    @After
    public void tear() {
        movieRepository.deleteAll();
    }

    @Test
    public void testSaveMovie() {
        movieRepository.save(movie);
        Movie fetchMovie = movieRepository.findById(movie.getId()).get();
        Assert.assertEquals("Deadpool", fetchMovie.getTitle());
        Assert.assertEquals(8.1, fetchMovie.getVoteAverage(), 0.01);
        Assert.assertEquals("Marvel action", fetchMovie.getOverview());
        Assert.assertEquals("22/10/2018", fetchMovie.getReleaseDate());
    }

    @Test
    public void testSaveMovieFailure() {
        Movie testMovie = new Movie("Deadpool2", (float) 7.1, "Marvel action", "22/10/2019");
        movieRepository.save(movie);
        Movie fetchMovie = movieRepository.findById(movie.getId()).get();
        Assert.assertNotSame(testMovie, fetchMovie);
    }

    @Test
    public void testDeleteMovieSuccess() {
        movieRepository.save(movie);
        Assert.assertTrue(movieRepository.findById(movie.getId()).isPresent());
        movieRepository.deleteById(movie.getId());
        Assert.assertTrue(movieRepository.findById(movie.getId()).isEmpty());
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void testDeleteMovieFailure() {
        movieRepository.deleteById(1);
    }

    @Test
    public void testExistsByIdSuccess() {
        movieRepository.save(movie);
        Assert.assertTrue(movieRepository.existsById(movie.getId()));
    }

    @Test
    public void testExistsByIdFailure() {
        Assert.assertFalse(movieRepository.existsById(1));
    }

    @Test
    public void testFindByIdSuccess() {
        movieRepository.save(movie);
        Assert.assertTrue(movieRepository.findById(movie.getId()).isPresent());
        Assert.assertEquals(movie, movieRepository.findById(movie.getId()).get());
    }

    @Test
    public void testFindByIdFailure() {
        Assert.assertFalse(movieRepository.findById(movie.getId()).isPresent());
    }

    @Test
    public void testFindDuplicateSuccess() {
        movieRepository.save(movie);
        Assert.assertEquals(movie, movieRepository.findDuplicate(movie.getTitle(), movie.getReleaseDate()));
    }

    @Test
    public void testFindDuplicateFailure() {
        Assert.assertNull(movieRepository.findDuplicate(movie.getTitle(), movie.getReleaseDate()));
    }

    @Test
    public void testFindMovieByNameSuccess() {
        movieRepository.save(movie);
        Assert.assertEquals(list, movieRepository.findMovieByName(movie.getTitle()));
    }

    @Test
    public void testFindMovieByNameFailure() {
        Assert.assertEquals(0, movieRepository.findMovieByName(movie.getTitle()).size());
    }
}