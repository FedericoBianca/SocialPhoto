//Required moudules
//_________________________________________________
  const MongoClient = require('mongodb').MongoClient;
  const HashMap = require('hashmap');
  const FB = require('fb');
//_________________________________________________

//var MongoUrl = "mongodb://localhost:27017/";
var MongoUrl = "mongodb://CarmineMansueto95:shadyrabbit95@ds245022.mlab.com:45022/mydb";

var arrayComments = [];
var arrayLikes = [];  //cresce ad ogni funzione di callback, se la size eguaglia quella di arrayID sono all'ultima funzione di callback
var arrayID = [];
var mapLikes = new HashMap();
var mapComments = new HashMap();
var finito = 0;
var maxSoFarLikes = 0;
var maxSoFarComments = 0;
var HttpResponse = "";

function finitoFunction(fb_id){

  console.log("Handler di finito: scrivo nel db");
  MongoClient.connect(MongoUrl, { useNewUrlParser: true }, function(err, db) {
    if (err) throw err;
    var dbo = db.db("mydb");  //creo l'oggetto  dbo che rappresenta il database "mydb" di MongoDB
    var selectResult = dbo.collection("users").findOne({FACEBOOK_ID: fb_id}, function(err, user){
      if(err) throw err;
      var best_photo_likes = user.BEST_PHOTO_LIKES;
      var best_photo_likes_url= user.URL_BEST_PHOTO_LIKES;
      var best_photo_comments = user.BEST_PHOTO_COMMENTS;
      var best_photo_comments_url = user.URL_BEST_PHOTO_COMMENTS;
      var response = best_photo_likes_url + "," + best_photo_likes + "," + best_photo_comments_url + "," + best_photo_comments;
      console.log(response);
      return response;
    });
  });
}


function calculateLikes(id, token, fb, fb_id){
  var urlPhoto = "/" + id + "/reactions?summary=1";
  fb.api(urlPhoto, function(res2){
    var likes = res2.summary.total_count;
    arrayLikes.push(likes);
    mapLikes.set(id, likes);
    if(likes > maxSoFarLikes){
      maxSoFarLikes = likes;
    }

    if(arrayLikes.length == arrayID.length){
      var maxId;
      for(k=0; k<arrayLikes.length; k++){
        if(mapLikes.get(arrayID[k]) == maxSoFarLikes) maxId = arrayID[k];
      }

      var urlImageQuery = maxId + "?fields=id,images";

      fb.api(urlImageQuery, function(res3) {
        var urlImage = res3.images[0].source;

        MongoClient.connect(MongoUrl, { useNewUrlParser: true }, function(err, db) {
          if(err) throw err;
          var dbo = db.db("mydb");
          var newValues = { $set: {ID_BEST_PHOTO_LIKES: maxId, BEST_PHOTO_LIKES: maxSoFarLikes, URL_BEST_PHOTO_LIKES: urlImage} };

          dbo.collection("users").updateOne({FACEBOOK_ID: fb_id}, newValues, function (err,res) {
            if(err) throw err;
            finito++;
            if(finito == 2){
            }
          });

        });

      });
    }
  });
}

function calculateComments(id, token, fb, fb_id){
  var urlPhoto = "/" + id + "/comments?summary=1";
  fb.api(urlPhoto, function(res2){
    var comments = res2.summary.total_count;
    arrayComments.push(comments);
    mapComments.set(id, comments);
    if(comments > maxSoFarComments){
      maxSoFarComments = comments;
    }

    if(arrayComments.length == arrayID.length){
      var maxId;
      for(k=0; k<arrayComments.length; k++){
        if(mapComments.get(arrayID[k]) == maxSoFarComments) maxId = arrayID[k];
      }

      var urlImageQuery = maxId + "?fields=id,images";

      fb.api(urlImageQuery, function(res3) {
        var urlImage = res3.images[0].source;

        MongoClient.connect(MongoUrl, { useNewUrlParser: true }, function(err, db) {
          if(err) throw err;
          var dbo = db.db("mydb");  //creo l'oggetto  dbo che rappresenta il database "mydb" di MongoDB
          var newValues = { $set: {ID_BEST_PHOTO_COMMENTS: maxId, BEST_PHOTO_COMMENTS: maxSoFarComments, URL_BEST_PHOTO_COMMENTS: urlImage} };

          dbo.collection("users").updateOne({FACEBOOK_ID: fb_id}, newValues, function (err,res) {
            if(err) throw err;
            finito++;
            if(finito == 2){
            }
          });
        });

      });
    }
  });
}


function calculateUserInfos(token, fb_id, year, month){
  console.log("YEAR: " + year + " MONTH: " + month);
  arrayComments = [];
  arrayLikes = [];
  arrayID = [];
  mapLikes.clear();
  mapComments.clear();
  finito = 0;
  maxSoFarLikes = 0;
  maxSoFarComments = 0;
  //HttpResponse = "";

  var urlPhotos = "me/photos?type=uploaded&limit=500000";
  const FB = require('fb');
  FB.setAccessToken(token);
  FB.api(urlPhotos, function(res1) {
    //Salvo gli ID di tutte le foto in arrayID
    var i=0;
    while(res1.data[i] != undefined){
      if(res1.data[i].created_time.substring(0,7) == year+"-"+month){  //Per adesso mi limito a un basso numero di foto per non invalidare il token
        arrayID.push(res1.data[i].id);
        mapLikes.set(res1.data[i].id, "");
        mapComments.set(res1.data[i].id, "");
      }
      i++;
    }

    if(arrayID.length == 0){
      console.log("Nothing to show");
    }

    for(i=0; i<arrayID.length; i++){
      calculateLikes(arrayID[i], token, FB, fb_id);
      calculateComments(arrayID[i], token, FB, fb_id);
    }
  });
}

module.exports.calculateUserInfos = calculateUserInfos;
