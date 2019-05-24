function loadFilm(title, year, rate, imageURL, duration, genre, directors, actors, plot, type, filmId) {
    document.getElementById("title").innerText = title;
    document.getElementById("year").innerText = "("+year+")";
    document.getElementById("rate").innerText = rate;
    if (imageURL != "N/A")
        document.getElementById("imageURL").src = imageURL;
    document.getElementById("imageURL").alt = title.concat(" Poster");
    document.getElementById("duration").innerText = duration;
    document.getElementById("genre").innerText = genre;
    document.getElementById("directors").innerText = directors;
    document.getElementById("actors").innerText = actors;
    document.getElementById("plot").innerText = plot;
    document.getElementById("type").innerText = type;
    document.getElementById("filmId").innerText = filmId;
}

function loadReview(review) {
    document.getElementById("reviewText").innerHTML = review;
    document.getElementById("review").style.display = "initial";
    document.getElementById("reviewButton").style.display = "none";
}

function removeReview(){
    document.getElementById("reviewText").innerText = "";
    document.getElementById("review").style.display = "none";
    document.getElementById("reviewButton").style.display = "initial";
}

function addViewed() {
    document.getElementById("removeViewButton").style.display = "initial";
    document.getElementById("addViewButton").style.display = "none";
}

function removeView(){
    document.getElementById("removeViewButton").style.display = "none";
    document.getElementById("addViewButton").style.display = "initial";
}