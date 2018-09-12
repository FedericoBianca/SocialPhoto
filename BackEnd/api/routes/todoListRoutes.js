'use strict';
module.exports = function(app) {
  var todoList = require('../controllers/todoListController');

  //todoList Routes
  app.route('/users')
    .post(todoList.create_a_user);

  app.route('/users/myUserTrigger')
    .get(todoList.read_a_user);

  app.route('/users/logOut')
    .post(todoList.setLoggedOut);

  app.route('/users/myUser')
    .get(todoList.respond);
};
