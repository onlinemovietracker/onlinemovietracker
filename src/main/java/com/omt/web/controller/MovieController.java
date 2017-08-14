package com.omt.web.controller;

import com.omt.JsonResults.CreditsResults;
import com.omt.JsonResults.QueryResultsMovie;
import com.omt.JsonResults.TrailerResults;
import com.omt.domain.*;
import com.omt.domain.Character;
import com.omt.repository.GenreRepository;
import com.omt.service.CharacterService;
import com.omt.service.MovieService;
import com.omt.service.PersonService;
import com.omt.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/movies")
public class MovieController {

    MovieService movieService;
    VideoService videoService;
    PersonService personService;
    CharacterService characterService;
    GenreRepository genreRepository;
    RestOperations restTemplate = new RestTemplate();

    String POSTER_PATH = "src/main/resources/static/img/posters/movies/poster";
    String BACKDROP_PATH = "src/main/resources/static/img/backdrops/movies/backdrop";

    String API_SEARCH = "https://api.themoviedb.org/3/search/movie?api_key={api_key}&query={search}";
    String API_GET_MOVIE = "https://api.themoviedb.org/3/movie/{id}?api_key={api_key}&language=en-US";
    String API_GET_CREDITS = "https://api.themoviedb.org/3/movie/{id}/credits?api_key={api_key}";
    String API_GET_PERSON = "https://api.themoviedb.org/3/person/{person_id}?api_key={api_key}&language=en-US";
    String API_GET_VIDEO = "https://api.themoviedb.org/3/movie/{id}/videos?api_key={api_key}";
    String API_KEY = "550e1867817e4bf3266023c5274d8858";


    @Autowired
    public MovieController(MovieService movieService, VideoService videoService, PersonService personService, CharacterService characterService, GenreRepository genreRepository) {
        this.movieService = movieService;
        this.videoService = videoService;
        this.personService = personService;
        this.characterService = characterService;
        this.genreRepository = genreRepository;
    }


    @RequestMapping(method = RequestMethod.GET)
    public List<Movie> findAll(){
        return movieService.findAll();
    }

    @RequestMapping(path = "{id}", method = RequestMethod.GET)
    public Movie findOne(@PathVariable("id") Long id){
        return movieService.findOne(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    public Movie save(@RequestBody Movie movie)throws Exception{
        if(movie.getId()!= null) {
            if (movieService.findOne(movie.getId()) != null) throw new Exception("You can't do that");
            if (videoService.findOne(movie.getId()) != null) throw new Exception("You can't use that id");
        }

        return movieService.save(movie);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public Movie update(@RequestBody Movie movie)throws Exception{
        if(videoService.findOne(movie.getId()).getDtype().equals("TvShow")) throw new Exception("You can't use that id");

        List<Genre> genresToBeAdded = new ArrayList<>();
        for(Genre genre: movie.getGenres()){
            genresToBeAdded.add(getGenres(genre.getName()));
        }
        movie.getGenres().clear();
        movie.setGenres(genresToBeAdded);

        return movieService.save(movie);
    }

    @RequestMapping(path="{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Long id){
//        deletePersons(id);
        deleteGenres(id);
        movieService.delete(id);
    }

    private void deleteGenres(Long id) {
        Movie movie = movieService.findOne(id);
        movie.getGenres().clear();
        movieService.save(movie);
    }

    @RequestMapping(path = "getMovie/{id}", method = RequestMethod.GET)
    public Movie saveFromTMDB(@PathVariable("id") Long id){

        Movie checkIfAlreadyExists = movieService.findByTmdbMovieId(id);
        if(checkIfAlreadyExists != null){
            return checkIfAlreadyExists;
        }

        Movie movie = restTemplate.getForObject(API_GET_MOVIE, Movie.class, id, API_KEY);
        getCharacters(id);

        movie.setTmdbMovieId(movie.getId());
        movie.setId(null);
        movie.setPosterPath("https://image.tmdb.org/t/p/w640" + movie.getPosterPath());
        movie.setImdbPage("http://www.imdb.com/title/" + movie.getImdbPage());
        movie.setBackdropPath( "http://image.tmdb.org/t/p/original" + movie.getBackdropPath());

        List<Character> characterList = characterService.findByTmdbMediaId(movie.getTmdbMovieId());
        movie.setCharacterList(characterList);

        TrailerResults trailerResults = restTemplate.getForObject(API_GET_VIDEO, TrailerResults.class, id, API_KEY);
        String youtube =  trailerResults.getTrailers().get(0).getTrailerLink();

        if(youtube == null){
            movie.setTrailerLink(null);

        }
        else{
            movie.setTrailerLink("https://www.youtube.com/embed/" + youtube);
        }

        List<Genre> genresToBeAdded = new ArrayList<>();
        for(Genre genre: movie.getGenres()){
            genresToBeAdded.add(getGenres(genre.getName()));
        }
        movie.getGenres().clear();
        movie.setGenres(genresToBeAdded);

        String ext = movie.getTmdbMovieId() + ".jpg";

        try {
            saveImage(movie.getPosterPath(), POSTER_PATH + ext);
            movie.setPosterPath("/img/posters/movies/poster" + ext);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            saveImage(movie.getBackdropPath(), BACKDROP_PATH + ext);
            movie.setBackdropPath("/img/backdrops/movies/backdrop" + ext);
        } catch (IOException e) {
            e.printStackTrace();
        }


        return movieService.save(movie);
    }

    public static void saveImage(String imageUrl, String destinationFile) throws IOException {
        URL url = new URL(imageUrl);
        InputStream is = url.openStream();
        OutputStream os = new FileOutputStream(destinationFile);
        byte[] b = new byte[2048];
        int length;

        while ((length = is.read(b)) != -1) {
            os.write(b, 0, length);
        }

        is.close();
    }

    public Genre getGenres(String name){
        Genre genre = genreRepository.findByName(name);
        if( genreRepository.findByName(name) != null){
            return genre;
        }else{
            genre = new Genre();
            genre.setName(name);
            return genreRepository.save(genre);
        }
    }

    public void getCharacters(Long id){
        Person person;
        int size;
        CreditsResults creditsResults = restTemplate.getForObject(API_GET_CREDITS, CreditsResults.class,  id, API_KEY);
        List<Character> characterList = creditsResults.getCharacters();
        if(characterList.size()<14){
            size = characterList.size();
        }
        else{
            size = 14;
        }

        for(int i = 0; i < size; i++){
            person = getPerson(characterList.get(i).getId());
            characterList.get(i).setActorId(characterList.get(i).getId());
            characterList.get(i).setTmdbMediaId(creditsResults.getTmdbMediaId());
            characterList.get(i).setId(null);
            characterList.get(i).setPerson(person);
            characterService.save(characterList.get(i));
        }
    }

    public Person getPerson(Long id){
        Person personCheck = personService.findByTmdbPersonId(id);
        if(personCheck != null) {
            return personCheck;
        }else{
            Person person;
            person = restTemplate.getForObject(API_GET_PERSON, Person.class, id, API_KEY);
            person.setTmdbPersonId(person.getId());
            person.setId(null);
            return personService.save(person);
        }
    }

    @RequestMapping(path = "search/{query}", method = RequestMethod.GET)
    public List<Movie> searchOnline(@PathVariable("query") String query) {

        QueryResultsMovie queryResults = restTemplate.getForObject(API_SEARCH,
                QueryResultsMovie.class, API_KEY, query);

        String moviesString = restTemplate.getForObject(API_SEARCH,
                String.class, API_KEY, query);
        System.out.println(moviesString);
        List<Movie> movies = queryResults.getMovies();
        for(int i = 0; i<movies.size(); i++){
            Long id = movies.get(i).getId();
            String trailers = restTemplate.getForObject("http://api.themoviedb.org/3/movies/{id}/videos?api_key={api_key}", String.class, id, API_KEY);
            System.out.println(trailers);
        }
        System.out.println(queryResults.getTrailers());
        return movies;
    }

}
