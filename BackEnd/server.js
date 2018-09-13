const MongoClient = require('mongodb').MongoClient;
const express = require('express'),
      app = express(),
      port = process.env.PORT || 3000;

const utils = require('./utils');

var routes = require('./api/routes/todoListRoutes');
routes(app);

app.listen(port);

console.log('RESTful API server started on: ' + port);

//var MongoUrl = "mongodb://localhost:27017/";
var MongoUrl = "mongodb://CarmineMansueto95:shadyrabbit95@ds245022.mlab.com:45022/mydb";

//Creo la collection degli utenti
MongoClient.connect(MongoUrl, { useNewUrlParser: true }, function(err, db) {
  if (err) throw err;
  var dbo = db.db("mydb");
  //creo la collection "users"
  dbo.createCollection("users", function(err, res) {
    if (err) throw err;
    console.log("Collection created!");
  });
});
