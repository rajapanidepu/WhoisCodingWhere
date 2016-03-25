var express = require('express');
var router = express.Router();
var Post = require('../models/postmodel');

/* GET users listing. */
router.get('/', function(req, res, next) {
  res.send('respond with a resource');
});

router.post('/', function(req, res, next) {
	var post = new Post(req.body);
	console.log(req.body);
	post.save(function(err){
		if(err) throw err;
		console.log("Saved to database");
	});

  res.send('respond with a resource');
});

module.exports = router;
