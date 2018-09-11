//Moduli richiesti
//_________________________________________________
  const MongoClient = require('mongodb').MongoClient;
  const HashMap = require('hashmap');
  const FB = require('fb');
  const Event = require('events');
//_________________________________________________

var MongoUrl = "mongodb://localhost:27017/";
var eventEmitter = new Event.EventEmitter();

//Per motivi di debug, anziche prendere il token da android lo setto manualmente
/*token = "EAANxJDFMaiUBABgrMiHppKXMEF07tbnrSHVQ0oGcIre1awWxj3NkcxPBOib4qtUEIRwcHtbsCcUMqUqeXVDOt4uAMjmnTtYGcMV9WOsQDvnX9zd6OHgRQN1GpTDAtKFydovT5UjiYtlPl7Bey7BVZBvVOmz1cRxZC8jya725beFPMj3AiXq3SZATTY7dvMZD";
FB.setAccessToken(token);*/

const arrayLikes = [];  //cresce ad ogni funzione di callback, se la size eguaglia quella di arrayID sono all'ultima funzione di callback
const arrayID = [];
const map = new HashMap();  //conterrà i match tra ID della foto e numero like corrispodente
var maxSoFar = 0;

function calculateLikes(id){
  var urlPhoto = "/" + id + "/reactions?summary=1";
  FB.api(urlPhoto, function(res2){
    var likes = res2.summary.total_count;
    arrayLikes.push(likes);
    map.set(id, likes);
    if(likes > maxSoFar){
      maxSoFar = likes;
    }

    if(arrayLikes.length == arrayID.length){
      var maxId;
      for(k=0; k<arrayLikes.length; k++){
        if(map.get(arrayID[k]) == maxSoFar) maxId = arrayID[k];
      }
      MongoClient.connect(MongoUrl, { useNewUrlParser: true }, function(err, db) {
        if(err) throw err;
        var dbo = db.db("mydb");  //creo l'oggetto  dbo che rappresenta il database "mydb" di MongoDB
        var newValues = { $set: {ID_BEST_PHOTO:maxId, BEST_PHOTO_LIKES:maxSoFar} };

        dbo.collection("users").updateOne({FB_TOKEN: token}, newValues, function (err,res) {
          if(err) throw err;
        });
        console.log(maxId + " | " + maxSoFar);
      });
    }
  });
}


function calculateUserInfos(token){
  var urlPhotos = "me/photos?type=uploaded&limit=500000";

  FB.api(urlPhotos, function(res1) {
    //Salvo gli ID di tutte le foto in arrayID
    var i=0;
    while(res1.data[i] != undefined){
      if(res1.data[i].created_time.substring(0,7) == '2010-05'){  //Per adesso mi limito a un basso numero di foto per non invalidare il token
        arrayID.push(res1.data[i].id);
        map.set(res1.data[i].id, "");
      }
      i++;
    }

    for(i=0; i<arrayID.length; i++){
      calculateLikes(arrayID[i]);
    }
  });
}

module.exports.calculateUserInfos = calculateUserInfos;
