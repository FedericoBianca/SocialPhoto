const MongoClient = require('mongodb').MongoClient; //importo il modulo per MongoDB
const express = require('express'), //importo il modulo express per gestire i metodi HTTP e creare routes e controllers
    app = express(),  //creo un'instanza del modulo express
    port = process.env.PORT || 3000;

const sleep = require('sleep');
const utils = require('./utils');

var routes = require('./api/routes/todoListRoutes'); //importing route
routes(app); //register the route

app.listen(port); //metto in ascolto sulla porta specificata

console.log('RESTful API server started on: ' + port);

var MongoUrl = "mongodb://localhost:27017/";  //URL per connettermi al database di MongoDB

//Creo la collection degli utenti
MongoClient.connect(MongoUrl, { useNewUrlParser: true }, function(err, db) {
  if (err) throw err;
  var dbo = db.db("mydb");  //creo l'oggetto  dbo che rappresenta il database "mydb" di MongoDB
  //creo la collection "users"
  dbo.createCollection("users", function(err, res) {
    if (err) throw err;
    console.log("Collection created!");
  });
});
