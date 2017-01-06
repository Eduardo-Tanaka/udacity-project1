package br.com.eduardo.project1_popularmovies.data;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

/**
 * Created by Eduardo on 05/01/2017.
 */

public interface MovieColumns {

    @DataType(DataType.Type.TEXT) @PrimaryKey
    public static final String _ID = "_id";

    @DataType(DataType.Type.TEXT) @NotNull
    public static final String TITLE = "title";

    @DataType(DataType.Type.TEXT) @NotNull
    public static final String POSTER = "poster";

    @DataType(DataType.Type.TEXT) @NotNull
    public static final String SYNOPSIS = "synopsis";

    @DataType(DataType.Type.TEXT) @NotNull
    public static final String RATING = "rating";

    @DataType(DataType.Type.TEXT) @NotNull
    public static final String DATE = "date";
}
