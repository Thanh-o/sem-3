const express = require('express');
const bodyParser = require('body-parser');
const db = require('./firebase');

const app = express();
app.use(bodyParser.json());

const port = process.env.PORT || 3000;

app.listen(port, () => {
  console.log(`Server is running on port ${port}`);
});

//API Create new products
app.post('/items', async (req, res) => {
    try {
      const data = req.body;
      const ref = await db.collection('products').add(data);
      res.status(201).send({ id: ref.id });
    } catch (error) {
      res.status(500).send(error.message);
    }
  });

//API get product by id
  app.get('/items/:id', async (req, res) => {
    try {
      const id = req.params.id;
      const doc = await db.collection('products').doc(id).get();
  
      if (!doc.exists) {
        res.status(404).send('No document found');
      } else {
        res.status(200).send(doc.data());
      }
    } catch (error) {
      res.status(500).send(error.message);
    }
  });

  //API get all product
  app.get('/items', async (req, res) => {
    try {
      const snapshot = await db.collection('products').get();
      const items = snapshot.docs.map(doc => ({ id: doc.id, ...doc.data() }));
      res.status(200).send(items);
    } catch (error) {
      res.status(500).send(error.message);
    }
  });
//API update by id 
  app.put('/items/:id', async (req, res) => {
    try {
      const id = req.params.id;
      const data = req.body;
      await db.collection('products').doc(id).update(data);
      res.status(200).send('Document successfully updated');
    } catch (error) {
      res.status(500).send(error.message);
    }
  });

//API delete
  app.delete('/items/:id', async (req, res) => {
    try {
      const id = req.params.id;
      await db.collection('products').doc(id).delete();
      res.status(200).send('Document successfully deleted');
    } catch (error) {
      res.status(500).send(error.message);
    }
  });
  
      