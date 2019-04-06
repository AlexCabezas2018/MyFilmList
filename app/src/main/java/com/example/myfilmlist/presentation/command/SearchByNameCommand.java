package com.example.myfilmlist.presentation.command;

import com.example.myfilmlist.presentation.context.Context;

public class SearchByNameCommand implements Command {
    @Override
    public Context execute(Context inputData) {
        //TODO implement the command
        //En este caso, se llama al SA con los datos (que en este caso será el nombre de la pelicula)
        //El SA devuelve la lista de pelicula (con un elemento si solo hay una o con varios si hay varias)
        //Se crea un context nuevo con esos datos, el mismo evento y la misma actividad (o con distinto evento si queremos disinguir el caso anterior)
        return null;
    }
}
