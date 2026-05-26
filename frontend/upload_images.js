const axios = require('axios');
const FormData = require('form-data');
const fs = require('fs');
const path = require('path');

const API_BASE_URL = 'http://localhost:8080/api';

const mockProducts = [
  {
    name: 'iPhone 15 Pro Max (256 GB, Natural Titanium)',
    imageUrl: 'https://images.unsplash.com/photo-1695048133142-1a20484d2569?auto=format&fit=crop&w=600&q=80',
    images: [
      'https://images.unsplash.com/photo-1695048133142-1a20484d2569?auto=format&fit=crop&w=600&q=80',
      'https://images.unsplash.com/photo-1695048132832-75d315990b79?auto=format&fit=crop&w=600&q=80'
    ]
  },
  {
    name: 'Logitech MX Master 3S Wireless Mouse',
    imageUrl: 'https://images.unsplash.com/photo-1615663245857-ac93bb7c39e7?auto=format&fit=crop&w=600&q=80',
    images: [
      'https://images.unsplash.com/photo-1615663245857-ac93bb7c39e7?auto=format&fit=crop&w=600&q=80',
      'https://images.unsplash.com/photo-1625842268584-8f329034f276?auto=format&fit=crop&w=600&q=80'
    ]
  },
  {
    name: 'Slim Fit Cotton Casual Shirt (Black)',
    imageUrl: 'https://images.unsplash.com/photo-1596755094514-f87e34085b2c?auto=format&fit=crop&w=600&q=80',
    images: [
      'https://images.unsplash.com/photo-1596755094514-f87e34085b2c?auto=format&fit=crop&w=600&q=80'
    ]
  },
  {
    name: 'Premium Leather Classic Wallet',
    imageUrl: 'https://images.unsplash.com/photo-1627124793833-28029fa0bcd3?auto=format&fit=crop&w=600&q=80',
    images: [
      'https://images.unsplash.com/photo-1627124793833-28029fa0bcd3?auto=format&fit=crop&w=600&q=80'
    ]
  },
  {
    name: 'Ultrasonic Cool Mist Humidifier',
    imageUrl: 'https://images.unsplash.com/photo-1585771724684-38269d6639fd?auto=format&fit=crop&w=600&q=80',
    images: [
      'https://images.unsplash.com/photo-1585771724684-38269d6639fd?auto=format&fit=crop&w=600&q=80'
    ]
  },
  {
    name: 'Ceramide Barrier Hydrating Cream',
    imageUrl: 'https://images.unsplash.com/photo-1596462502278-27bfdc403348?auto=format&fit=crop&w=600&q=80',
    images: [
      'https://images.unsplash.com/photo-1596462502278-27bfdc403348?auto=format&fit=crop&w=600&q=80'
    ]
  },
  {
    name: 'Digital Air Fryer (4.2L, 1500W)',
    imageUrl: 'https://images.unsplash.com/photo-1621972750749-0fbb1abb7736?auto=format&fit=crop&w=600&q=80',
    images: [
      'https://images.unsplash.com/photo-1621972750749-0fbb1abb7736?auto=format&fit=crop&w=600&q=80'
    ]
  }
];

async function uploadImage(url, token) {
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
    return uploadRes.data.url;
  } catch (err) {
    console.error('Failed to upload image', url, err.message);
    return url; // fallback
  }
}

async function migrateImages() {
  try {
    console.log('Authenticating...');
    const loginRes = await axios.post(`${API_BASE_URL}/auth/login`, {
      emailOrPhone: 'adarsht072@gmail.com',
      password: 'Adarsh@123'
    });
    const token = loginRes.data.token;
    console.log('Authenticated.');

    const mapping = {}; // old URL -> new URL

    for (const prod of mockProducts) {
      if (!mapping[prod.imageUrl]) {
        console.log(`Uploading: ${prod.imageUrl}`);
        mapping[prod.imageUrl] = await uploadImage(prod.imageUrl, token);
      }
      for (const img of prod.images) {
        if (!mapping[img]) {
          console.log(`Uploading: ${img}`);
          mapping[img] = await uploadImage(img, token);
        }
      }
    }

    console.log('--- MAPPING ---');
    console.log(JSON.stringify(mapping, null, 2));

    // Update seed.js
    const seedPath = path.join(__dirname, 'seed.js');
    let seedContent = fs.readFileSync(seedPath, 'utf8');

    for (const [oldUrl, newUrl] of Object.entries(mapping)) {
      if (newUrl !== oldUrl) {
        seedContent = seedContent.split(oldUrl).join(newUrl);
      }
    }

    fs.writeFileSync(seedPath, seedContent, 'utf8');
    console.log('Updated seed.js with Cloudinary URLs!');
  } catch (err) {
    console.error(err);
  }
}

migrateImages();
