package com.stackroute.movieApp.controller;

import com.stackroute.movieApp.domain.Movie;
import com.stackroute.movieApp.exceptions.Errors;
import com.stackroute.movieApp.exceptions.MovieAlreadyExistsException;
import com.stackroute.movieApp.exceptions.MovieNotFoundException;
import com.stackroute.movieApp.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value="api/v1")
public class MovieController {
    private MovieService movieService;

    @Autowired
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @PostMapping("/movie")
    public ResponseEntity<?> addMovie(@RequestBody Movie movie) throws MovieAlreadyExistsException{
        ResponseEntity responseEntity;
        responseEntity = new ResponseEntity<Movie> (movieService.saveMovie(movie), HttpStatus.CREATED);
        return responseEntity;
    }

    @DeleteMapping("/movie/{id}")
    public ResponseEntity<?> deleteMovie(@PathVariable("id") int id) throws MovieNotFoundException{
        ResponseEntity responseEntity;
        responseEntity = new ResponseEntity<Movie> (movieService.deleteMovie(id), HttpStatus.NO_CONTENT);
        return responseEntity;
    }

    @GetMapping("/movies")
    public ResponseEntity<?> getMovies() {
        ResponseEntity responseEntity;
        responseEntity = new ResponseEntity<List<Movie>> (movieService.getMovies(), HttpStatus.OK);
        return responseEntity;
    }

    @GetMapping("/movie/{id:[\\d]+}")
    public ResponseEntity<?> getMovie(@PathVariable int id) throws MovieNotFoundException{
        ResponseEntity responseEntity;
        responseEntity = new ResponseEntity<Movie> (movieService.getMovie(id), HttpStatus.OK);
        return responseEntity;
    }

    @GetMapping("/movie/{title:[a-zA-Z][a-zA-Z-_0-9]+}")
    public ResponseEntity<?> getMovieByName(@PathVariable String title) {
        ResponseEntity responseEntity;
        responseEntity = new ResponseEntity<List<Movie>> (movieService.getMovieByName(title), HttpStatus.OK);
        return responseEntity;
    }

    @PutMapping("/movie")
    public ResponseEntity<?> updateMovie(@RequestBody Movie movie) throws MovieNotFoundException, MovieAlreadyExistsException{
        ResponseEntity responseEntity;
        responseEntity = new ResponseEntity<Movie> (movieService.updateMovie(movie), HttpStatus.OK);
        return responseEntity;
    }
}
