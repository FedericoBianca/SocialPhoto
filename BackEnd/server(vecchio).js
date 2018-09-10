const MongoClient = require('mongodb').MongoClient; //importo il modulo per MongoDB
const express = require('express'), //importo il modulo express per gestire i metodi HTTP e creare routes e controllers
    app = express(),  //creo un'instanza del modulo express
    port = process.env.PORT || 3000;

//Setto il token per usarlo per le FB api
const FB = require('fb');

const TOKEN = "EAAcqaQanVTIBAHT3XPLoQtVWqeUL1PUpZBmpqVXty6tUVjZBQY8TZCXcHfEWWwZA6l4H4PVGY0PI8QjWBoZCa5ZCjiXcp9oLTg20p2XAyQDOHZAiTmZCZCoHpZBwkK1E5kE1rO1bOmT6g9ChPnp4Om2Mw62gRFKYySCHf5WvjDXhQIl9mWvymANh4MChGdEazJ18cZD";
FB.setAccessToken(TOKEN);

const events = require('events');
var eventEmitter = new events.EventEmitter();

//const utils = require('./utils');

var MongoUrl = "mongodb://localhost:27017/";  //URL per connettermi al database di MongoDB

MongoClient.connect(MongoUrl, { useNewUrlParser: true }, function(err, db) {
  if (err) throw err;
  var dbo = db.db("mydb");  //creo l'oggetto  dbo che rappresenta il database "mydb" di MongoDB
  //creo la collection "users"
  dbo.createCollection("users", function(err, res) {
    if (err) throw err;
    console.log("Collection created!");
    //Inserisco manualmente un utente di prova
    var newUser = { "FB_TOKEN": TOKEN, "db_num_likes": 0, "id_best_photo": "" };
    dbo.collection("users").findOne({"FB_TOKEN": TOKEN}, function(err, user){
      if(err) throw err;
      console.log("findOne succesfull");
      if(!user){
        dbo.collection("users").insertOne(newUser, function(err, res) {
          if (err) throw err;

          console.log("User inserted");
        });
      }
    });
  });
  //db.close();
});

var routes = require('./api/routes/todoListRoutes'); //importing route
routes(app); //register the route

app.listen(port); //metto in ascolto sulla porta specificata

console.log('todo list RESTful API server started on: ' + port);

//utils.calcola();

const arrayID = [];
const arrayLike = [];

var calculatedLikesHandler = function () {

  for(i=0; i<arrayLike.length; i++){
    console.log("Pat't: " + arrayLike[i]);
  }

  var maxCurr = 0;
  var bestPhotoPosition = 0;
  for(i=0; i<arrayLike.length; i++){
    if(arrayLike[i] > maxCurr){
      maxCurr = parseInt(arrayLike[i]);
      bestPhotoPosition=i;
    }
  }
  console.log(maxCurr);

  var idBestPhoto = arrayID[bestPhotoPosition];

  MongoClient.connect(MongoUrl, { useNewUrlParser: true}, function(err, db){
    if(err) throw err;
    var dbo = db.db('mydb');
    dbo.collection("users").updateOne({"FB_TOKEN": TOKEN }, {$set: {"db_num_likes": arrayLike[maxCurr], "id_best_photo": idBestPhoto}});
  });
}

eventEmitter.on('likesCalculated', calculatedLikesHandler);

var url = "me/photos?type=uploaded&limit=500000";

function getNumLikes(url, length, position){
  FB.api(url, function(res) {
    var numLikes = res.summary.total_count;
    arrayLike[position] = parseInt(numLikes);

    /*
    MongoClient.connect(MongoUrl, { useNewUrlParser: true }, function(err, db) {
      if (err) throw err;
      var dbo = db.db("mydb");  //creo l'oggetto  dbo che rappresenta il database "mydb" di MongoDB

      dbo.collection("users").findOne( {"FB_TOKEN": TOKEN}, function(err, res) {
          if (err) throw err;
          if(res.db_num_likes < numLikes){
            dbo.collection("users").updateOne({"FB_TOKEN": TOKEN}, { $set: {"db_num_likes": numLikes}});
          }
        });
        //db.close();
      });
    });
    */
    if(length == position){
      eventEmitter.emit('likesCalculated');
    }
  });
}

function calculateLikes(url){
  FB.api(url, {fields: []}, function(res) {
    var i = 0;
    while(res.data[i] != undefined){
      if(res.data[i].created_time.substring(0,7) == '2010-05'){
        arrayID.push(res.data[i].id);
      }
      i++;
    }

    for(i=0; i<arrayID.length; i++){
      url = "/" + arrayID[i] + "/reactions?summary=1";
      getNumLikes(url, arrayID.length-1, i);
    }
  });
}

// Lancio la computazione dei like
calculateLikes(url);
