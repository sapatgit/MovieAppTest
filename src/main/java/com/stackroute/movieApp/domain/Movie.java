package com.stackroute.movieApp.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@Table(name = "Movie")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private int id;
    @Column(name="title")
    private String title;
    @JsonProperty(value = "vote_average")
    private float voteAverage;
    @Column(name = "overview")
    private String overview;
    @JsonProperty(value = "release_date")
    private String releaseDate;


    public Movie(String title, float voteAverage, String overview, String releaseDate) {
        this.title = title;
        this.voteAverage = voteAverage;
        this.overview = overview;
        this.releaseDate = releaseDate;
    }
    
}
