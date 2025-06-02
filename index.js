const express = require('express');
const mysql = require('mysql2');
const cors = require('cors');
require('dotenv').config();

const app = express();
app.use(cors());
app.use(express.json());

// MySQL connection
const db = mysql.createConnection({
  host: process.env.MYSQLHOST,
  user: process.env.MYSQLUSER,
  password: process.env.MYSQLPASSWORD,
  database: process.env.MYSQLDATABASE,
  port: process.env.MYSQLPORT
});

db.connect(err => {
  if (err) {
    console.error('❌ DB connection failed:', err);
    return;
  }
  console.log('✅ Connected to MySQL database.');
});

// Root endpoint
app.get('/', (req, res) => {
  res.send('✅ Backend API is running and connected to DB.');
});

// Login endpoint
app.post('/auth/login', (req, res) => {
  console.log('Login request body:', req.body);  // Add this
  const { username, password } = req.body;

  if (!username || !password) {
    return res.status(400).json({ success: false, message: 'Missing username or password' });
  }
  const sql = 'SELECT * FROM admins WHERE username = ? AND password = ? LIMIT 1';
  db.query(sql, [username, password], (err, results) => {
    if (err) {
      console.error('DB query error:', err);
      return res.status(500).json({ success: false, message: 'Server error' });
    }

    if (results.length > 0) {
      // Success: user found
      return res.json({ success: true, message: 'Login successful' });
    } else {
      // Fail: no matching user
      return res.json({ success: false, message: 'Invalid username or password' });
    }
  });
});
// Register endpoint
app.post('/auth/register', (req, res) => {
  const { username, password } = req.body;

  if (!username || !password) {
    return res.status(400).json({ success: false, message: 'Missing username or password' });
  }

  const sql = 'INSERT INTO admins (username, password) VALUES (?, ?)';
  db.query(sql, [username, password], (err, result) => {
    if (err) {
      if (err.code === 'ER_DUP_ENTRY') {
        return res.status(409).json({ success: false, message: 'Username already exists' });
      }
      console.error('DB error:', err);
      return res.status(500).json({ success: false, message: 'Server error' });
    }

    res.json({ success: true, message: 'User registered successfully' });
  });
})

// Check if username exists endpoint
app.get('/auth/check-username', (req, res) => {
  const username = req.query.username;

  if (!username) {
    return res.status(400).json({ success: false, message: 'Missing username parameter' });
  }

  const sql = 'SELECT * FROM admins WHERE username = ? LIMIT 1';
  db.query(sql, [username], (err, results) => {
    if (err) {
      console.error('DB query error:', err);
      return res.status(500).json({ success: false, message: 'Server error' });
    }

    if (results.length > 0) {
      // Username exists
      return res.json({ success: true, exists: true });
    } else {
      // Username not found
      return res.json({ success: true, exists: false });
    }
  });
});

// printBooks

app.get('/books', (req, res) => {
  const sql = 'SELECT * FROM books'; // 
  db.query(sql, (err, results) => {
    if (err) {
      console.error('DB query error:', err);
      return res.status(500).json({ success: false, message: 'Server error' });
    }
    res.json(results); // ترجع قائمة الكتب JSON
  });
});

//

app.post('/books', (req, res) => {
  const { title, author, isbn, quantity, borrowed } = req.body;

  // تحقق من الحقول المطلوبة (مثلاً title و author على الأقل)
  if (!title || !author || !isbn || quantity == null || borrowed == null) {
    return res.status(400).json({ success: false, message: 'Missing required fields' });
  }

  const sql = 'INSERT INTO books (title, author, isbn, quantity, borrowed) VALUES (?, ?, ?, ?, ?)';
  db.query(sql, [title, author, isbn, quantity, borrowed], (err, result) => {
    if (err) {
      console.error('DB error:', err);
      return res.status(500).json({ success: false, message: 'Server error' });
    }
    res.json({ success: true, message: 'Book added successfully' });
  });
});

//  remove book 

// حذف كتاب حسب الـ id
app.delete('/books/:id', (req, res) => {
  const bookId = req.params.id;

  const sql = 'DELETE FROM books WHERE id = ?';
  db.query(sql, [bookId], (err, result) => {
    if (err) {
      console.error('DB error:', err);
      return res.status(500).json({ success: false, message: 'Server error' });
    }

    if (result.affectedRows === 0) {
      return res.status(404).json({ success: false, message: 'Book not found' });
    }

    res.json({ success: true, message: 'Book deleted successfully' });
  });
});

//search book by title

// Search books by title endpoint
app.get('/books/search', (req, res) => {
  const title = req.query.title;

  if (!title) {
    return res.status(400).json({ success: false, message: 'Missing title parameter' });
  }

  // البحث باستخدام LIKE مع % للبحث الجزئي داخل العنوان
  const sql = 'SELECT * FROM books WHERE title LIKE ?';
  const searchTerm = `%${title}%`;

  db.query(sql, [searchTerm], (err, results) => {
    if (err) {
      console.error('DB query error:', err);
      return res.status(500).json({ success: false, message: 'Server error' });
    }

    // نرجع النتائج على شكل JSON فقط (قائمة الكتب)
    res.json(results);
  });
});




































// Start server
const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
  console.log(`🚀 Server running on port ${PORT}`);
});
