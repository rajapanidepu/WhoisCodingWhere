var express = require('express');
var router = express.Router();
var User = require('../models/usermodel');


/* GET users listing. */
router.get('/', function(req, res, next) {
  res.send('respond with a resource');
});

router.post('/', function(req, res, next) {
	var user = new User(req.body);
	console.log(req.body);
	user.save(function(err){
		if(err) throw err;
		console.log("Saved to database");
	});

  res.send('respond with a resource');
});

module.exports = router;
