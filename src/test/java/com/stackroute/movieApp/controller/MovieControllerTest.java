package com.stackroute.movieApp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stackroute.movieApp.domain.Movie;
import com.stackroute.movieApp.exceptions.GlobalExceptions;
import com.stackroute.movieApp.exceptions.MovieAlreadyExistsException;
import com.stackroute.movieApp.exceptions.MovieNotFoundException;
import com.stackroute.movieApp.service.MovieService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebMvcTest
public class MovieControllerTest {

    @Autowired
    private MockMvc mockMvc;
    private Movie movie;

    @MockBean
    private MovieService movieService;

    @InjectMocks
    private MovieController movieController;

    private List<Movie> list = null;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(movieController)
                                    .setControllerAdvice(GlobalExceptions.class)
                                    .build();
        movie = new Movie("Deadpool", (float) 8.1, "Marvel action", "22/10/2018");
        list = new ArrayList<Movie>();
        list.add(movie);
    }

    @Test
    public void saveMovieSuccess() throws Exception {
        when(movieService.saveMovie(any())).thenReturn(movie);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/movie")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(movie)))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void saveMovieFailure() throws Exception {
        when(movieService.saveMovie(any())).thenThrow(MovieAlreadyExistsException.class);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/movie")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(movie)))
                .andExpect(MockMvcResultMatchers.status().isConflict());
    }

    @Test
    public void deleteMovieSuccess() throws Exception {
        when(movieService.deleteMovie(anyInt())).thenReturn(movie);
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/movie/" + movie.getId()))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void deleteMovieFailure() throws Exception {
        when(movieService.deleteMovie(anyInt())).thenThrow(MovieNotFoundException.class);
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/movie/" + movie.getId()))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void getMovies() throws Exception {
        when(movieService.getMovies()).thenReturn(list);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/movies"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getMovieSuccess() throws Exception {
        when(movieService.getMovie(anyInt())).thenReturn(movie);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/movie/" + movie.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getMovieFailure() throws Exception {
        when(movieService.getMovie(anyInt())).thenThrow(MovieNotFoundException.class);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/movie/" + movie.getId()))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void getMovieByName() throws Exception {
        when(movieService.getMovieByName((String) any())).thenReturn(list);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/movie/abcd"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void updateMovieSuccess() throws Exception {
        when(movieService.updateMovie((Movie) any())).thenReturn(movie);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/movie")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(movie)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void updateMovieFailure_MovieNotFound() throws Exception {
        when(movieService.updateMovie(any())).thenThrow(MovieNotFoundException.class);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/movie/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(movie)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void updateMovieFailure_MovieAlreadyExists() throws Exception {
        when(movieService.updateMovie(any())).thenThrow(MovieAlreadyExistsException.class);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/movie/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(movie)))
                .andExpect(MockMvcResultMatchers.status().isConflict());
    }

    private static String asJsonString(final Object obj) {
        try{
            return new ObjectMapper().writeValueAsString(obj);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }
}