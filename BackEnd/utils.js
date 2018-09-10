const MongoClient = require('mongodb').MongoClient;
var MongoUrl = "mongodb://localhost:27017/";  //URL per connettermi al database di MongoDB

const FB = require('fb');

function calculateUserInfos(token){
  token = "EAAcqaQanVTIBAICUvwIyfuvgLbqhYbrG6FzBaF9wsIYvwTV1T0eCeKgV5SZAyfQaOtCZBrbkn87aZCUX5r1Vkieq1p8BMtjp3yBMV2rs0zW23vj7fEZBg3aZBmrE7DMUSR7HQKgHieZBnxDQwIKFToY0Ng1KzeWSlZAb8DyOOjdZAZAEXu3twuiLICNjcX88z77IZD";
  FB.setAccessToken(token);
  var urlPhotos = "me/photos?type=uploaded&limit=500000";
  const arrayID = [];

  FB.api(urlPhotos, {fields: []}, function(res) {
    var i = 0;
    //Salvo gli ID di tutte le foto in arrayID
    console.log("Mi vadi a prendere gli ID delle foto");
    while(res.data[i] != undefined){
        if(res.data[i].created_time.substring(0,7) == '2010-05')
          arrayID.push(res.data[i].id);
      i++;
    }
    console.log("ID presi!");
    var maxSoFar = 0;
    var idSoFar = "";
    for(j=0; j<arrayID.length; j++){

        var urlPhoto = "/" + arrayID[j] + "/reactions?summary=1";
        FB.api(urlPhoto, function(res){
          var likes = res.summary.total_count;
          if(likes > maxSoFar){
            maxSoFar = likes;
            idSoFar = arrayID[j];

            //Se ho finito tutte le funzioni di callback, devo salvare i dati nel database
            console.log("Sono all'iterazione " + j);
            if(j == arrayID.length -1){
              console.log("Infos calculated succesfully (si spera)!!!!!!");
              console.log("ID: " + idSoFar + " | Likes: " + maxSoFar);
              MongoClient.connect(MongoUrl, { useNewUrlParser: true }, function(err, db) {
                if(err) throw err;
                var dbo = db.db("mydb");  //creo l'oggetto  dbo che rappresenta il database "mydb" di MongoDB
                var newValues = { $set: {ID_BEST_PHOTO:idSoFar, BEST_PHOTO_LIKES:maxSoFar} };
                dbo.collection("users").updateOne({FB_TOKEN: token}, newValues, function (err,res) {
                  if(err) throw err;
                });
              });
            }

          }

        });

    }
  });
}

module.exports.calculateUserInfos = calculateUserInfos;
