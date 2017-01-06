package br.com.eduardo.project1_popularmovies.data;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

/**
 * Created by Eduardo on 05/01/2017.
 */


@Database(version = MovieDB.VERSION)
public final class MovieDB {

    private MovieDB(){}

    public static final int VERSION = 3;

    @Table(MovieColumns.class) public static final String MOVIES = "movies";
}
