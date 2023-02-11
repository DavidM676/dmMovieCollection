import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

public class MovieCollection {
  private ArrayList<Movie> movies;
  private Scanner scanner;

  public MovieCollection(String fileName) {
    importMovieList(fileName);
    scanner = new Scanner(System.in);
  }

  public ArrayList<Movie> getMovies() {
    return movies;
  }

  

  public void menu() {
    String menuOption = "";

    System.out.println("Welcome to the movie collection!");
    System.out.println("Total: " + movies.size() + " movies");

    while (!menuOption.equals("q")) {
      System.out.println("------------ Main Menu ----------");
      System.out.println("- search (t)itles");
      System.out.println("- search (k)eywords");
      System.out.println("- search (c)ast");
      System.out.println("- see all movies of a (g)enre");
      System.out.println("- list top 50 (r)ated movies");
      System.out.println("- list top 50 (h)igest revenue movies");
      System.out.println("- (q)uit");
      System.out.print("Enter choice: ");
      menuOption = scanner.nextLine();

      if (menuOption.equals("t")) {
        searchTitles();
      } else if (menuOption.equals("c")) {
        searchCast();
      } else if (menuOption.equals("k")) {
        searchKeywords();
      } else if (menuOption.equals("g")) {
        listGenres();
      } else if (menuOption.equals("r")) {
        listHighestRated();
      } else if (menuOption.equals("h")) {
        listHighestRevenue();
      } else if (menuOption.equals("q")) {
        System.out.println("Goodbye!");
      } else {
        System.out.println("Invalid choice!");
      }
    }
  }

  private void importMovieList(String fileName) {
    try {
      movies = new ArrayList<Movie>();
      FileReader fileReader = new FileReader(fileName);
      BufferedReader bufferedReader = new BufferedReader(fileReader);
      String line = bufferedReader.readLine();

      while ((line = bufferedReader.readLine()) != null) {
        // get data from the columns in the current row and split into an array
        String[] movieFromCSV = line.split(",");
        String title = movieFromCSV[0];
        String cast = movieFromCSV[1];
        String director = movieFromCSV[2];
        String tagline = movieFromCSV[3];
        String keywords = movieFromCSV[4];
        String overview = movieFromCSV[5];
        int runtime = Integer.parseInt(movieFromCSV[6]);
        String genres = movieFromCSV[7];
        double userRating = Double.parseDouble(movieFromCSV[8]);
        int year = Integer.parseInt(movieFromCSV[9]);
        int revenue = Integer.parseInt(movieFromCSV[10]);

        Movie nextMovie = new Movie(title, cast, director, tagline, keywords, overview, runtime, genres, userRating, year, revenue);
        movies.add(nextMovie);

      }
      bufferedReader.close();
    } catch(IOException exception) {
      System.out.println("Unable to access " + exception.getMessage());
    }
  }

  private void searchTitles() {
    String s = "title";
    System.out.print("Enter a " + s + " search term: ");
    String searchTerm = scanner.nextLine();

    // prevent case sensitivity
    searchTerm = searchTerm.toLowerCase();

    // arraylist to hold search results
    ArrayList<Movie> results = new ArrayList<Movie>();

    // search through ALL movies in collection
    for (int i = 0; i < movies.size(); i++) {
      String movieTitle = "";
      movieTitle = movies.get(i).getTitle();

      movieTitle = movieTitle.toLowerCase();
      if (movieTitle.indexOf(searchTerm) != -1) {
        //add the Movie objest to the results list
        results.add(movies.get(i));
      }
    }
    listMovies(results, s);
  }

  private void searchKeywords() {
    String s = "keyword";
    System.out.print("Enter a " + s + " search term: ");
    String searchTerm = scanner.nextLine();

    // prevent case sensitivity
    searchTerm = searchTerm.toLowerCase();

    // arraylist to hold search results
    ArrayList<Movie> results = new ArrayList<Movie>();

    // search through ALL movies in collection
    for (int i = 0; i < movies.size(); i++) {
      String movieKey = "";

      movieKey = movies.get(i).getKeywords();

      movieKey = movieKey.toLowerCase();

      if (movieKey.indexOf(searchTerm) != -1) {
        //add the Movie objest to the results list
        results.add(movies.get(i));
      }
    }
    listMovies(results, s);
    /* TASK 3: IMPLEMENT ME */
  }

  private void searchCast() {
    System.out.print("Enter a search term: ");
    String searchTerm = scanner.nextLine();
    searchTerm = searchTerm.toLowerCase();
    ArrayList<String> castName = new ArrayList<>();


    for (int i = 0; i< movies.size(); i++) {
      String castLst = movies.get(i).getCast();
      castLst = "|"+castLst+"|";
      int lastIdx = 1;

      for (int j = 1; j<castLst.length(); j++) {
        if (castLst.charAt(j) == '|') {
          String name = castLst.substring(lastIdx, j);

          if (name.toLowerCase().contains(searchTerm)) {
            if (!(castName.contains(name))) {
              castName.add(name);
            }
          }
          lastIdx = j+1;
        }
      }

    }
    sortString(castName);

    for (int i = 0; i<castName.size(); i++) {
      System.out.println(i+1+". "+castName.get(i));
    }
    System.out.print("Which would you like to see all movies for: ");
    int caster = scanner.nextInt();
    scanner.nextLine();
    getMoviesFor(castName.get(caster-1), "cast");
  }
  

  
  private void listHighestRated() {
    ArrayList<Double> ratings = new ArrayList<Double>();
    for (int i = 0; i< movies.size(); i++) {
      ratings.add(movies.get(i).getUserRating());
    }
    sortDouble(ratings, movies);
    ArrayList<Movie> high = new ArrayList<>();
    for (int i = 0; i< movies.size(); i++) {
       high.add(movies.get(i));
      if (i==49) {
        break;
      }
    }
    listMovies(high, "movie", false);
    /* TASK 6: IMPLEMENT ME */
  }
  
  private void listHighestRevenue() {
    ArrayList<Double> rev = new ArrayList<Double>();
    for (int i = 0; i< movies.size(); i++) {
      rev.add((double)movies.get(i).getRevenue());
    }
    sortDouble(rev, movies);
    ArrayList<Movie> high = new ArrayList<>();
    for (int i = 0; i< movies.size(); i++) {
      high.add(movies.get(i));
      if (i==49) {
        break;
      }
    }
    listMovies(high, "movie", false);
  }

  private void listGenres() {
    String allG = "";
    for (int i = 0; i< movies.size(); i++) {
      allG += "|"+movies.get(i).getGenres()+"|";
    }
    ArrayList<String> genres = new ArrayList<>();
    genres = splitString(allG);

    sortString(genres);
    genres.remove(0);

    for (int i = 0; i<genres.size(); i++) {
      System.out.println(i+1+". "+genres.get(i));
    }
    System.out.print("Which would you like to see all movies for: ");
    int caster = scanner.nextInt();
    scanner.nextLine();
    getMoviesFor(genres.get(caster-1), "genre");
  }





  private void listMovies(ArrayList<Movie> results, String s, boolean sort) {
    if (results.size() > 0) {
      // sort the results by title
      if (sort)
        sortResults(results);

      // now, display them all to the user
      for (int i = 0; i < results.size(); i++) {
        String title = results.get(i).getTitle();

        // this will print index 0 as choice 1 in the results list; better for user!
        int choiceNum = i + 1;
        System.out.println("" + choiceNum + ". " + title);
      }

      System.out.println("Which movie would you like to learn more about?");
      System.out.print("Enter number: ");
      int choice = scanner.nextInt();
      scanner.nextLine();
      Movie selectedMovie = results.get(choice - 1);
      displayMovieInfo(selectedMovie);
      System.out.println("\n ** Press Enter to Return to Main Menu **");
      scanner.nextLine();
    } else {
      System.out.println("\nNo movie " +s+ " match that search term!");
      System.out.println("** Press Enter to Return to Main Menu **");
      scanner.nextLine();
    }
  }

  private void listMovies(ArrayList<Movie> results, String s) {
    listMovies(results, s, true);
  }


  
  private void displayMovieInfo(Movie movie) {
    System.out.println();
    System.out.println("Title: " + movie.getTitle());
    System.out.println("Tagline: " + movie.getTagline());
    System.out.println("Runtime: " + movie.getRuntime() + " minutes");
    System.out.println("Year: " + movie.getYear());
    System.out.println("Directed by: " + movie.getDirector());
    System.out.println("Cast: " + movie.getCast());
    System.out.println("Overview: " + movie.getOverview());
    System.out.println("User rating: " + movie.getUserRating());
    System.out.println("Box office revenue: " + movie.getRevenue());
  }


  private void getMoviesFor(String str, String cat) {
      ArrayList<Movie> movie = new ArrayList<>();
      for (int c = 0; c<movies.size(); c++) {
        Movie m = movies.get(c);
        String s = switch(cat) {
          case "cast" -> m.getCast();
          case "genre" -> m.getGenres();
          default -> null;
        };
        
        if (s.contains(str)) {
          movie.add(movies.get(c));
        }
      }
      listMovies(movie, "movie");

  }


  
  private ArrayList<String> splitString(String splitStr) {
    ArrayList<String> finalLst = new ArrayList<>();
    splitStr = splitStr+"|";
    int lastIdx = 1;

    for (int j = 1; j<splitStr.length(); j++) {
      if (splitStr.charAt(j) == '|') {

        String yes = splitStr.substring(lastIdx, j);
        if (!(finalLst.contains(yes))) {
          finalLst.add(yes);
        }
        lastIdx = j+1;
      }

    }
    return finalLst;
  }



  private void sortResults(ArrayList<Movie> listToSort) {
    for (int j = 1; j < listToSort.size(); j++) {
      Movie temp = listToSort.get(j);
      String tempTitle = temp.getTitle();

      int possibleIndex = j;
      while (possibleIndex > 0 && tempTitle.compareTo(listToSort.get(possibleIndex - 1).getTitle()) < 0) {
        listToSort.set(possibleIndex, listToSort.get(possibleIndex - 1));
        possibleIndex--;
      }
      listToSort.set(possibleIndex, temp);
    }
  }

  private void sortString(ArrayList<String> listToSort) {
    for (int j = 1; j < listToSort.size(); j++) {
      String tempTitle = listToSort.get(j);

      int possibleIndex = j;
      while (possibleIndex > 0 && tempTitle.compareTo(listToSort.get(possibleIndex - 1)) < 0) {
        listToSort.set(possibleIndex, listToSort.get(possibleIndex - 1));
        possibleIndex--;
      }
      listToSort.set(possibleIndex, tempTitle);
    }
    
  }


  private void sortDouble(ArrayList<Double> listToSort, ArrayList<Movie> also) {
    for (int j = 1; j < listToSort.size(); j++) {
      Double tempTitle = listToSort.get(j);
      Movie temp = also.get(j);

      int possibleIndex = j;
      while (possibleIndex > 0 && tempTitle > listToSort.get(possibleIndex - 1)) {
        listToSort.set(possibleIndex, listToSort.get(possibleIndex - 1));
        also.set(possibleIndex, also.get(possibleIndex-1));
        possibleIndex--;
      }
      listToSort.set(possibleIndex, tempTitle);
      also.set(possibleIndex, temp);
    }
  }

  

}