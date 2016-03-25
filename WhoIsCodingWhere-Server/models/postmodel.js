var mongoose = require('mongoose');
var Schema = mongoose.Schema;

// create a schema
var postSchema = new Schema({
  name: String,
  location: String,
  meta: {
    age: Number,
    website: String
  },
  text: String,
  description: String,
  need_help : Boolean,
  created_at: Date,
  updated_at: Date
});

// the schema is useless so far
// we need to create a model using it
var Post = mongoose.model('Post', postSchema);

// make this available to our Posts in our Node applications
module.exports = Post;