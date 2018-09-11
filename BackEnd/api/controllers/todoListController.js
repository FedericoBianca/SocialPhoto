'use strict';
const MongoClient = require('mongodb').MongoClient;
const Event = require('events');
const utils = require('../../utils');

var MongoUrl = "mongodb://localhost:27017/";  //URL per connettermi al database di MongoDB
var eventEmitter = new Event.EventEmitter();

//Event Handler lanciato al login
eventEmitter.on('Logged_In', function(token) {
  console.log("Emit presa!");
  utils.calculateUserInfos(token);  //Per ogni utente calcolo le info tramite la funzione definita in utils
});

//HTTP POST di un utente: se non c'è nel DB lo inserisco, altrimenti aggiorno il FB_TOKEN
exports.create_a_user = function(req, res){
  var token = req.query.facebook_token;

  MongoClient.connect(MongoUrl, { useNewUrlParser: true }, function(err, db) {
    if (err) throw err;
    var dbo = db.db("mydb");  //creo l'oggetto  dbo che rappresenta il database "mydb" di MongoDB
    var newUser = { "FB_TOKEN": token, "LOGGED_IN": true, "BEST_PHOTO_LIKES": 0, "ID_BEST_PHOTO": "" }; //creo il documento da inserire nella collection
    // ci mancano i fields BEST_LOCATION, BEST_POST_FRIEND
    var selectResult = dbo.collection("users").findOne({FB_TOKEN:token}, function(err,user){
      if(err) throw err;
      if(!user){
        dbo.collection("users").insertOne(newUser, function(err, res) {
          if (err) throw err;
          console.log("User inserted");
        });
      }
      //Indipendentemente dalla presenza dell'utente, devo aggiornare il token con il nuovo che mi arriva dalla POST
      var newValues = { $set: {FB_TOKEN: token} };
      dbo.collection("users").updateOne({FB_TOKEN: token}, newValues, function (err,res) {
        if(err) throw err;
        console.log("User succesfully updated!");
      });
    });
  });
  eventEmitter.emit('Logged_In', token);
  res.json();
}

//HTTP GET del singolo utente, devo rispondere con le info sull'utente quali id foto più piaciuta...
exports.read_a_user = function(req, res){
  var token = req.query.facebook_token;

  MongoClient.connect(MongoUrl, { useNewUrlParser: true }, function(err, db) {
    if (err) throw err;
    var dbo = db.db("mydb");  //creo l'oggetto  dbo che rappresenta il database "mydb" di MongoDB
    var selectResult = dbo.collection("users").findOne({FB_TOKEN:token}, function(err,user){
      if(err) throw err;
      var id_best_photo = user.ID_BEST_PHOTO;
      var best_photo_likes = user.BEST_PHOTO_LIKES;
      var response = "{\"id_best_photo\": " + id_best_photo + "\", best_photo_likes\": " + best_photo_likes + "\"}"
      res.json(response);
    });
  });
}

exports.update_a_user = function(req, res){
  var risposta = "{\"name\": \"giggino\"}"
  res.json(risposta);
}

exports.setLoggedOut = function(req, res){
  var token = req.query.facebook_token;
  var newValues = { $set: {LOGGED_IN: false} };

  MongoClient.connect(MongoUrl, { useNewUrlParser: true }, function(err, db) {
    if (err) throw err;
    var dbo = db.db("mydb");  //creo l'oggetto  dbo che rappresenta il database "mydb" di MongoDB

    dbo.collection("users").updateOne({FB_TOKEN: token}, newValues, function (err,res) {
      if(err) throw err;
      console.log("User succesfully marked ad LoggedOut!");
    });
  });
}
