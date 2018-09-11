//Moduli richiesti
//_________________________________________________
  const MongoClient = require('mongodb').MongoClient;
  const HashMap = require('hashmap');
  const FB = require('fb');
  const Event = require('events');
//_________________________________________________

var MongoUrl = "mongodb://localhost:27017/";
var eventEmitter = new Event.EventEmitter();

const arrayLikes = [];  //cresce ad ogni funzione di callback, se la size eguaglia quella di arrayID sono all'ultima funzione di callback
const arrayID = [];
const map = new HashMap();  //conterrà i match tra ID della foto e numero like corrispodente
var maxSoFar = 0;

function calculateLikes(id, token, fb, fb_id){
  var urlPhoto = "/" + id + "/reactions?summary=1";
  fb.api(urlPhoto, function(res2){
    var likes = res2.summary.total_count;
    arrayLikes.push(likes);
    map.set(id, likes);
    if(likes > maxSoFar){
      maxSoFar = likes;
    }

    if(arrayLikes.length == arrayID.length){
      var maxId;
      for(k=0; k<arrayLikes.length; k++){
        console.log(map.get(arrayID[k]));
        if(map.get(arrayID[k]) == maxSoFar) maxId = arrayID[k];
      }
      MongoClient.connect(MongoUrl, { useNewUrlParser: true }, function(err, db) {
        if(err) throw err;
        var dbo = db.db("mydb");  //creo l'oggetto  dbo che rappresenta il database "mydb" di MongoDB
        var newValues = { $set: {ID_BEST_PHOTO:maxId, BEST_PHOTO_LIKES:maxSoFar} };

        dbo.collection("users").updateOne({FACEBOOK_ID: fb_id}, newValues, function (err,res) {
          if(err) throw err;
        });
        console.log(maxId + " | " + maxSoFar);
      });
    }
  });
}


function calculateUserInfos(token, fb_id, year, month){
  var urlPhotos = "me/photos?type=uploaded&limit=500000";
  const FB = require('fb');
  FB.setAccessToken(token);
  FB.api(urlPhotos, function(res1) {
    //Salvo gli ID di tutte le foto in arrayID
    var i=0;
    while(res1.data[i] != undefined){
      if(res1.data[i].created_time.substring(0,7) == year+"-"+month){  //Per adesso mi limito a un basso numero di foto per non invalidare il token
        arrayID.push(res1.data[i].id);
        map.set(res1.data[i].id, "");
      }
      i++;
    }

    if(arrayID.length == 0){
      console.log("Nun c sta nu cazz");
    }

    for(i=0; i<arrayID.length; i++){
      calculateLikes(arrayID[i], token, FB, fb_id);
    }
  });
}

module.exports.calculateUserInfos = calculateUserInfos;
