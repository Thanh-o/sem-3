const express = require('express');
const mongoose = require('mongoose');
const Item = require('./models/item');

const app = express();
const port = 3000; 

mongoose.connect('mongodb://localhost:27017/ecommerceDB')
    .then(() => { console.log('Connected to MongoDB'); })
    .catch(err => { console.error('Failed to connect to MongoDB'); });

app.use(express.json()); // Middleware để parse JSON

// API để liệt kê tất cả các item
app.get('/items', async (req, res) => { 
    try {
        const items = await Item.find();
        res.json(items);
    } catch (error) {
        res.status(500).json({ message: error.message });
    }
});

//Lấy API theo ID
app.get('/items/:id',async(req,res) =>{
    try {
        const item = await Item.findById(req.params.id);
        console.log('Item found: ',item);
        if(!item) return res.status(404).json({message: 'Item not found'});
        res.json(item);
    } catch (error) {
        res.status(500).json({message:error.message});
        
    }
});

//API: Tạo 1 item mới
app.post('/items', async (req, res) =>{
    const { name, description, price } = req.body;

    const item = new Item({
        name,
        description,
        price
    });
    try {
        const newItem = await item.save();
        res.status(201).json(newItem);
    } catch (error) {
        res.status(400).json({ message: err.message });
    }
});

// Thiết lập Server
app.listen(port, () => {
    console.log('Server is running on http://localhost:${port}'); 
});
