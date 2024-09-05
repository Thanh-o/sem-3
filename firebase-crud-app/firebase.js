// firebase.js cấu hình kết nối FileStorage
const admin = require('firebase-admin');
const serviceAccount = require('./serviceAccountKey.json');

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
  databaseURL: 'https://nodejsapp-692ac-default-rtdb.asia-southeast1.firebasedatabase.app'
});

const db = admin.firestore();

module.exports = db;
