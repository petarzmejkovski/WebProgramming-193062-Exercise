package mk.ukim.finki.wp.kol2023.g2.service.impl;

import mk.ukim.finki.wp.kol2023.g2.model.Director;
import mk.ukim.finki.wp.kol2023.g2.model.Genre;
import mk.ukim.finki.wp.kol2023.g2.model.Movie;
import mk.ukim.finki.wp.kol2023.g2.model.exceptions.InvalidDirectorIdException;
import mk.ukim.finki.wp.kol2023.g2.model.exceptions.InvalidMovieIdException;
import mk.ukim.finki.wp.kol2023.g2.repository.DirectorRepository;
import mk.ukim.finki.wp.kol2023.g2.repository.MovieRepository;
import mk.ukim.finki.wp.kol2023.g2.service.MovieService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final DirectorRepository directorRepository;

    public MovieServiceImpl(MovieRepository movieRepository, DirectorRepository directorRepository) {
        this.movieRepository = movieRepository;
        this.directorRepository = directorRepository;
    }

    @Override
    public List<Movie> listAllMovies() {
        return movieRepository.findAll();
    }

    @Override
    public Movie findById(Long id) {
        return movieRepository.findById(id).orElseThrow(InvalidMovieIdException::new);
    }

    @Override
    public Movie create(String name, String description, Double rating, Genre genre, Long director) {
        Director new_director = directorRepository.findById(director).orElseThrow(InvalidDirectorIdException::new);
        return movieRepository.save(new Movie(name, description, rating, genre, new_director));
    }

    @Override
    public Movie update(Long id, String name, String description, Double rating, Genre genre, Long director) {
        Director new_director = directorRepository.findById(director).orElseThrow(InvalidDirectorIdException::new);
        Movie new_movie = movieRepository.findById(id).orElseThrow(InvalidMovieIdException::new);
        new_movie.setName(name);
        new_movie.setDescription(description);
        new_movie.setRating(rating);
        new_movie.setGenre(genre);
        new_movie.setDirector(new_director);
        return movieRepository.save(new_movie);
    }

    @Override
    public Movie delete(Long id) {
        Movie movie = movieRepository.findById(id).orElseThrow(InvalidMovieIdException::new);
        movieRepository.delete(movie);
        return movie;
    }

    @Override
    public Movie vote(Long id) {
        Movie movie = movieRepository.findById(id).orElseThrow(InvalidMovieIdException::new);
        movie.setVotes(movie.getVotes() + 1);
        return movieRepository.save(movie);
    }

    @Override
    public List<Movie> listMoviesWithRatingLessThenAndGenre(Double rating, Genre genre) {
        List<Movie> result;

        if (rating == null && genre == null) {
            result = movieRepository.findAll();
        } else if (rating != null && genre != null) {
            result = movieRepository.findAllByRatingLessThanAndGenre(rating, genre);
        } else if (rating == null) {
            result = movieRepository.findAllByGenre(genre);
        } else {
            result = movieRepository.findAllByRatingLessThan(rating);
        }

        return result;
    }
}
