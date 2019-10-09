package com.stackroute.movieApp.service;

import com.stackroute.movieApp.domain.Movie;
import com.stackroute.movieApp.exceptions.MovieAlreadyExistsException;
import com.stackroute.movieApp.exceptions.MovieNotFoundException;
import com.stackroute.movieApp.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieDummyServiceImpl implements MovieService{
    private MovieRepository movieRepository;

    @Autowired
    public MovieDummyServiceImpl(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public Movie saveMovie(Movie movie) throws MovieAlreadyExistsException {
        if(movieRepository.findDuplicate(movie.getTitle(), movie.getReleaseDate()) != null) {
            throw new MovieAlreadyExistsException("Movie with title: "+movie.getTitle()+
                    " and release_date: "+movie.getReleaseDate()+" already exits!");
        } else {
            movieRepository.save(movie);
            return movie;
        }
    }

    @Override
    public Movie updateMovie(Movie movie) throws MovieNotFoundException, MovieAlreadyExistsException{
        if(!movieRepository.existsById(movie.getId())){
            throw new MovieNotFoundException("Movie with id: "+movie.getId()+" not found!");
        } else{
            Movie toUpdate = movieRepository.findById(movie.getId()).get();
            if (movie.getTitle() != null)
                toUpdate.setTitle(movie.getTitle());
            if (movie.getOverview() != null)
                toUpdate.setOverview(movie.getOverview());
            if (movie.getVoteAverage() != 0)
                toUpdate.setVoteAverage(movie.getVoteAverage());
            if (movie.getReleaseDate() != null)
                toUpdate.setReleaseDate(movie.getReleaseDate());
            if(movieRepository.findDuplicate(toUpdate.getTitle(), toUpdate.getReleaseDate()) != null) {
                throw new MovieAlreadyExistsException("Movie with title: "+toUpdate.getTitle()+
                        " and release_date: "+toUpdate.getReleaseDate()+" already exits!");
            } else {
                movieRepository.save(toUpdate);
                return toUpdate;
            }
        }
    }

    @Override
    public Movie deleteMovie(int id) throws MovieNotFoundException{
        if(!movieRepository.existsById(id))
            throw new MovieNotFoundException("Movie with id: "+id+" not found!");
        else {
            movieRepository.deleteById(id);
            return getMovie(id);
        }
    }

    @Override
    public List<Movie> getMovies() {
        return movieRepository.findAll();
    }

    @Override
    public Movie getMovie(int id) throws MovieNotFoundException{
        if(!movieRepository.existsById(id))
            throw new MovieNotFoundException("Movie with id: "+id+" not found!");
        else {
            Movie movie = movieRepository.findById(id).get();
            return movie;
        }
    }

    @Override
    public List<Movie> getMovieByName(String title) {
        return movieRepository.findMovieByName(title);
    }
}
