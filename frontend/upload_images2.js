const axios = require('axios');
const FormData = require('form-data');
const fs = require('fs');
const path = require('path');

const API_BASE_URL = 'http://localhost:8080/api';

async function migrateRemainingImages() {
  try {
    const loginRes = await axios.post(`${API_BASE_URL}/auth/login`, {
      emailOrPhone: 'adarsht072@gmail.com',
      password: 'Adarsh@123'
    });
    const token = loginRes.data.token;
    console.log('Authenticated.');

    const seedPath = path.join(__dirname, 'seed.js');
    let seedContent = fs.readFileSync(seedPath, 'utf8');

    // Find all unsplash urls in seed.js
    const regex = /https:\/\/images\.unsplash\.com\/[^"'\s]+/g;
    const matches = [...new Set(seedContent.match(regex) || [])];

    console.log(`Found ${matches.length} Unsplash URLs to migrate.`);

    for (const url of matches) {
      console.log(`Uploading: ${url}`);
      try {
        const response = await axios.get(url, { responseType: 'arraybuffer' });
        const buffer = Buffer.from(response.data, 'binary');

        const formData = new FormData();
        formData.append('image', buffer, { filename: 'image.jpg', contentType: 'image/jpeg' });

        const uploadRes = await axios.post(`${API_BASE_URL}/upload/image`, formData, {
          headers: {
            ...formData.getHeaders(),
            'Authorization': `Bearer ${token}`
          }
        });
        const newUrl = uploadRes.data.url;
        console.log(`Success -> ${newUrl}`);
        
        // Replace in seed content
        seedContent = seedContent.split(url).join(newUrl);
      } catch (e) {
        console.error(`Failed: ${url}`);
      }
    }

    fs.writeFileSync(seedPath, seedContent, 'utf8');
    console.log('Updated seed.js with Cloudinary URLs!');
  } catch (err) {
    console.error(err);
  }
}

migrateRemainingImages();
