function loadFilm(title, year, rate, imageURL, duration, genre, directors, actors, plot, type, filmId) {
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
    document.getElementById("type").innerHTML = type;
    document.getElementById("filmId").innerHTML = filmId;
}

function loadReview(review) {
    document.getElementById("reviewText").innerHTML = review;
    document.getElementById("review").style.display = "block";
    document.getElementById("reviewButton").style.display = "none";
}

function addViewed() {
    document.getElementById("added").style.display = "block";
    document.getElementById("addViewButton").style.display = "none";
}