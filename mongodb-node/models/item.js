const mongoosee = require('mongoose');
const itemSchema = new mongoosee.Schema(
    {
        name:{
            type: String,
            required:true,
        },
        description:{
            type:String,
            required:true,
        },
        prire:{
            type:String,
            required:true,
        }
    }
);
module.exports = mongoosee.model('Item',itemSchema);