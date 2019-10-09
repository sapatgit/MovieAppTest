package com.stackroute.movieApp.repository;

import com.stackroute.movieApp.domain.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Integer> {
    @Query("from Movie where title=?1")
    public List<Movie> findMovieByName(String title);

    @Query("from Movie where title=?1 and releaseDate=?2")
    public Movie findDuplicate(String title, String releaseDate);
}
