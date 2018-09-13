'use strict';
const MongoClient = require('mongodb').MongoClient;
const utils = require('../../utils');

//var MongoUrl = "mongodb://localhost:27017/";
var MongoUrl = "mongodb://CarmineMansueto95:shadyrabbit95@ds245022.mlab.com:45022/mydb";

exports.create_a_user = function(req, res){
  var token = req.query.facebook_token;
  var fb_id = req.query.facebook_id;

  MongoClient.connect(MongoUrl, { useNewUrlParser: true }, function(err, db) {
    if (err) throw err;
    var dbo = db.db("mydb");
    var newUser = { "FACEBOOK_ID": fb_id, "LOGGED_IN": true, "BEST_PHOTO_LIKES": 0, "URL_BEST_PHOTO_LIKES": "", "BEST_PHOTO_COMMENTS": 0, "URL_BEST_PHOTO_COMMENTS": ""}; //creo il documento da inserire nella collection
    var selectResult = dbo.collection("users").findOne({FACEBOOK_ID: fb_id}, function(err,user){
      if(err) throw err;
      if(!user){
        dbo.collection("users").insertOne(newUser, function(err, res) {
          if (err) throw err;
          console.log("User inserted");
        });
      }
    });
  });
  res.json();
}

exports.read_a_user = function(req, res){
  var fb_id = req.query.facebook_id;
  var token = req.query.facebook_token;
  var year = req.query.year;
  var month = req.query.month;

  utils.calculateUserInfos(token, fb_id, year, month);
  res.send("");

}

exports.setLoggedOut = function(req, res){
  var token = req.query.facebook_token;
  var fb_id = req.query.facebook_id;
  var newValues = { $set: {LOGGED_IN: false} };

  MongoClient.connect(MongoUrl, { useNewUrlParser: true }, function(err, db) {
    if (err) throw err;
    var dbo = db.db("mydb");  //creo l'oggetto  dbo che rappresenta il database "mydb" di MongoDB

    dbo.collection("users").updateOne({FACEBOOK_ID: fb_id}, newValues, function (err,res1) {
      if(err) throw err;
      console.log("User succesfully marked ad LoggedOut!");
      res.json();
    });
  });
}
exports.respond = function(req, res){
  var fb_id = req.query.facebook_id;
  MongoClient.connect(MongoUrl, { useNewUrlParser: true }, function(err, db) {
    if (err) throw err;
    var dbo = db.db("mydb");
    dbo.collection("users").findOne({FACEBOOK_ID: fb_id},function(err,user){
        var url_photo_likes = user.URL_BEST_PHOTO_LIKES;
        var photo_likes = user.BEST_PHOTO_LIKES;
        var url_photo_comments = user.URL_BEST_PHOTO_COMMENTS;
        var photo_comments = user.BEST_PHOTO_COMMENTS;
        var result = url_photo_likes + "," + photo_likes + "," + url_photo_comments + "," + photo_comments;
        res.send(result);
    });
  });
}
