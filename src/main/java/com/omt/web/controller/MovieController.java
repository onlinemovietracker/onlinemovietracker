package com.omt.web.controller;

import com.omt.JsonResults.*;
import com.omt.config.LoginUserService;
import com.omt.domain.*;
import com.omt.domain.Character;
import com.omt.repository.GenreRepository;
import com.omt.service.CharacterService;
import com.omt.service.KeywordService;
import com.omt.service.MovieService;
import com.omt.service.PersonService;
import com.omt.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/movies")
public class MovieController {

    MovieService movieService;
    VideoService videoService;
    PersonService personService;
    CharacterService characterService;
    GenreRepository genreRepository;
    KeywordService keywordService;
    RestOperations restTemplate = new RestTemplate();
    LoginUserService loginUserService;

    final static String API_SEARCH = "https://api.themoviedb.org/3/search/movie?api_key={api_key}&query={search}";
    final static String API_GET_MOVIE = "https://api.themoviedb.org/3/movie/{id}?api_key={api_key}&language=en-US";
    final static String API_GET_CREDITS = "https://api.themoviedb.org/3/movie/{id}/credits?api_key={api_key}";
    final static String API_GET_PERSON = "https://api.themoviedb.org/3/person/{person_id}?api_key={api_key}&language=en-US";
    final static String API_GET_VIDEO = "https://api.themoviedb.org/3/movie/{id}/videos?api_key={api_key}";
    final static String API_GET_ACTOR_PROFILE = "https://api.themoviedb.org/3/person/{person_id}/images?api_key={api_key}";
    final static String API_GET_ALL_BACKDROPS = "https://api.themoviedb.org/3/movie/{movie_id}/images?api_key={api_key}";
    final static String API_GET_MOVIE_KEYWORDS = "https://api.themoviedb.org/3/movie/{movie_id}/keywords?api_key={api_key}";
    final static String API_KEY = "550e1867817e4bf3266023c5274d8858";


    @Autowired
    public MovieController(MovieService movieService, VideoService videoService, PersonService personService, CharacterService characterService, GenreRepository genreRepository, KeywordService keywordService, LoginUserService loginUserService) {
        this.movieService = movieService;
        this.videoService = videoService;
        this.personService = personService;
        this.characterService = characterService;
        this.genreRepository = genreRepository;
        this.keywordService = keywordService;
        this.loginUserService = loginUserService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Movie> findAll() {
        return movieService.findAll();
    }

    @RequestMapping(path="get-latest-three", method= RequestMethod.GET)
    public List<Movie> getLatestThree() {
        List<Movie> movies = movieService.findAll();
        List<Movie> latestThree = movies.subList(Math.max(movies.size() - 3, 0), movies.size());
        return latestThree;
    }

    @RequestMapping(path = "{id}", method = RequestMethod.GET)
    public Movie findOne(@PathVariable("id") Long id) {
        return movieService.findOne(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    public Movie save(@RequestBody Movie movie) throws Exception {
        if (movie.getId() != null) {
            if (movieService.findOne(movie.getId()) != null) throw new Exception("You can't do that");
            if (videoService.findOne(movie.getId()) != null) throw new Exception("You can't use that id");
        }
        movie.setAddedBy(loginUserService.getCurrentUser().getUsername());
        if(!movie.getGenres().isEmpty()) {
            List<Genre> genresToBeAdded = new ArrayList<>();
            for (Genre genre : movie.getGenres()) {
                genresToBeAdded.add(getGenres(genre.getName()));
            }
            movie.getGenres().clear();
            movie.setGenres(genresToBeAdded);
        }
    
    List<Keyword> keywordsToBeAdded = new ArrayList<>();
    if(movie.getKeywords() == null) movie.setKeywords(keywordsToBeAdded);
    if (!movie.getKeywords().isEmpty()) {
        for (Keyword keyword : movie.getKeywords()) {
            keywordsToBeAdded.add(getKeywords(keyword.getName()));
        }
        movie.getKeywords().clear();
        movie.setKeywords(keywordsToBeAdded);
    }

    return movieService.save(movie);
}    

    @RequestMapping(method = RequestMethod.PUT)
    public Movie update(@RequestBody Movie movie) throws Exception {
        if (videoService.findOne(movie.getId()).getDtype().equals("TvShow"))
            throw new Exception("You can't use that id");

//        if(!movie.getAddedBy().equals(loginUserService.getCurrentUser().getUsername())){
//            throw new Exception("You can't edit this Movie");
//        }

        if(movie.getAddedBy() == null) {
			movie.setAddedBy(loginUserService.getCurrentUser().getUsername());
		}

        List<Genre> genresToBeAdded = new ArrayList<>();
        for (Genre genre : movie.getGenres()) {
            genresToBeAdded.add(getGenres(genre.getName()));
        }
        movie.getGenres().clear();
        movie.setGenres(genresToBeAdded);

        List<Keyword> keywordsToBeAdded = new ArrayList<>();
        for (Keyword keyword : movie.getKeywords()) {
            keywordsToBeAdded.add(getKeywords(keyword.getName()));
        }
        movie.getKeywords().clear();
        movie.setKeywords(keywordsToBeAdded);

        return movieService.save(movie);
    }

    @RequestMapping(path = "{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Long id) {
        deleteGenres(id);
        deleteKeywords(id);
        movieService.delete(id);
    }

    private void deleteGenres(Long id) {
        Movie movie = movieService.findOne(id);
        movie.getGenres().clear();
        movieService.save(movie);
    }
    
    private void deleteKeywords(Long id) {
        Movie movie = movieService.findOne(id);
        movie.getKeywords().clear();
        movieService.save(movie);
    }

    @RequestMapping(path = "getMovie/{id}", method = RequestMethod.GET)
    public Movie saveFromTMDB(@PathVariable("id") Long id) throws InterruptedException {

        Movie checkIfAlreadyExists = movieService.findByTmdbMovieId(id);
        if (checkIfAlreadyExists != null) {
            return checkIfAlreadyExists;
        }

        Movie movie = restTemplate.getForObject(API_GET_MOVIE, Movie.class, id, API_KEY);

        if(loginUserService.getCurrentUser() != null) {
			movie.setAddedBy(loginUserService.getCurrentUser().getUsername());
		}

		getCharacters(id);
        getCrew(id);
        movie.setTmdbMovieId(movie.getId());
        movie.setId(null);
        Locale[] locales = Locale.getAvailableLocales();
        for (Locale locale : locales) {
            if (locale.getLanguage().equals(movie.getOriginalLanguage())) {
                movie.setOriginalLanguage(locale.getDisplayLanguage());
                break;
            }

        }
        movie.setImdbPage("http://www.imdb.com/title/" + movie.getImdbPage());
        movie.setPosterPath(movie.getPosterPath());
        movie.setBackdropPath(movie.getBackdropPath());

        List<Character> characterList = characterService.findByTmdbMediaId(movie.getTmdbMovieId());
        movie.setCharacterList(characterList);

        TrailerResults trailerResults = restTemplate.getForObject(API_GET_VIDEO, TrailerResults.class, id, API_KEY);
        if (!trailerResults.getTrailers().isEmpty()) {
            String youtube = trailerResults.getTrailers().get(0).getTrailerLink();
            movie.setTrailerLink("https://www.youtube.com/embed/" + youtube);
        } else {
            movie.setTrailerLink(null);
        }


        List<Genre> genresToBeAdded = new ArrayList<>();
        for (Genre genre : movie.getGenres()) {
            genresToBeAdded.add(getGenres(genre.getName()));
        }
        movie.getGenres().clear();
        movie.setGenres(genresToBeAdded);
        
        KeywordsResults keywordsResults = restTemplate.getForObject(API_GET_MOVIE_KEYWORDS, KeywordsResults.class, id, API_KEY);
        List<Keyword> keywords = keywordsResults.getMovieKeywords();

        List<Keyword> keywordsToBeAdded = new ArrayList<>();
        for (Keyword keyword:keywords) {
            keywordsToBeAdded.add(getKeywords(keyword.getName()));
        }
        movie.setKeywords(keywordsToBeAdded);
        
        ApiImageResults results = restTemplate.getForObject(API_GET_ALL_BACKDROPS, ApiImageResults.class, id, API_KEY);
        List<String> backdrops = results.returnApiImagePaths(results.getBackdrops());

        movie.setAdditionalBackdrops(new ArrayList<>());
        int size;
        if (backdrops.size() < 5) {
            size = backdrops.size();
        } else {
            size = 5;
        }

        for (int i = 0; i < size; i++) {
            movie.getAdditionalBackdrops().add(backdrops.get(i));
        }

        movieService.save(movie);
        return movieService.findOne(movie.getId());
    }

    public Genre getGenres(String name) {
        Genre genre = genreRepository.findByName(name);
        if (genreRepository.findByName(name) != null) {
            return genre;
        } else {
            genre = new Genre();
            genre.setName(name);
            return genreRepository.save(genre);
        }
    }
    
    public Keyword getKeywords(String name) {
        Keyword keyword = keywordService.findByName(name);
        if (keyword == null) {
            keyword = new Keyword();
            keyword.setId(null);
            keyword.setName(name);
            return keywordService.save(keyword);
        }
        return keyword;
    }

    public void getCrew(Long id) {
        Person person;
        CrewResults crewResults = restTemplate.getForObject(API_GET_CREDITS, CrewResults.class, id, API_KEY);
        List<Character> crewList = crewResults.getCharacters();
        int size = crewList.size();

        for(int i = 0; i < size; i++) {
            if(crewList.get(i).getJob().equals("Director")) {
                person = getPerson(crewList.get(i).getId());
                crewList.get(i).setActorId(crewList.get(i).getId());
                crewList.get(i).setTmdbMediaId(crewResults.getTmdbMediaId());
                crewList.get(i).setId(null);
                crewList.get(i).setPerson(person);
                crewList.get(i).setJob("director");
                characterService.save(crewList.get(i));
            }
        }
    }

    public void getCharacters(Long id) {
        Person person;
        int size;
        CreditsResults creditsResults = restTemplate.getForObject(API_GET_CREDITS, CreditsResults.class, id, API_KEY);
        List<Character> characterList = creditsResults.getCharacters();
        if (characterList.size() < 14) {
            size = characterList.size();
        } else {
            size = 14;
        }

        for (int i = 0; i < size; i++) {
            person = getPerson(characterList.get(i).getId());
            characterList.get(i).setActorId(characterList.get(i).getId());
            characterList.get(i).setTmdbMediaId(creditsResults.getTmdbMediaId());
            characterList.get(i).setId(null);
            characterList.get(i).setPerson(person);
            characterList.get(i).setJob("actor");
            characterService.save(characterList.get(i));
        }
    }

    public Person getPerson(Long id) {
        Person personCheck = personService.findByTmdbPersonId(id);
        if (personCheck != null) {
            return personCheck;
        } else {
            Person person;
            person = restTemplate.getForObject(API_GET_PERSON, Person.class, id, API_KEY);
            person.setTmdbPersonId(person.getId());
            person.setId(null);
            ApiImageResults results = restTemplate.getForObject(API_GET_ACTOR_PROFILE, ApiImageResults.class, id, API_KEY);
            if (!results.getProfiles().isEmpty()) {
                person.setPicture(results.getProfiles().get(0).getFilePath());
            }
            return personService.save(person);
        }
    }

    @RequestMapping(path = "search/{query}", method = RequestMethod.GET)
    public List<Movie> searchOnline(@PathVariable("query") String query) {

        QueryResultsMovie queryResults = restTemplate.getForObject(API_SEARCH,
                QueryResultsMovie.class, API_KEY, query);

        String moviesString = restTemplate.getForObject(API_SEARCH,
                String.class, API_KEY, query);
        List<Movie> movies = queryResults.getMovies();
        for (int i = 0; i < movies.size(); i++) {
            Long id = movies.get(i).getId();
            String trailers = restTemplate.getForObject("http://api.themoviedb.org/3/movies/{id}/videos?api_key={api_key}", String.class, id, API_KEY);            
        }        
        return movies;
    }

}
