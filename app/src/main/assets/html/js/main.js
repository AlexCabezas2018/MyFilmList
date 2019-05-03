function loadFilm(title, year, rate, imageURL, duration, genre, directors, actors, plot) {
    document.getElementById("title").innerHTML = title;
    document.getElementById("year").innerHTML = "("+year+")";
    document.getElementById("rate").innerHTML = rate;
    if (imageURL != "N/A")
        document.getElementById("imageURL").src = imageURL;
    document.getElementById("imageURL").alt = title.concat(" Poster");
    document.getElementById("duration").innerHTML = duration;
    document.getElementById("genre").innerHTML = genre;
    document.getElementById("directors").innerHTML = directors;
    document.getElementById("actors").innerHTML = actors;
    document.getElementById("plot").innerHTML = plot;
}