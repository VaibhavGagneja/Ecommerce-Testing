const axios = require('axios');
const FormData = require('form-data');
const fs = require('fs');
const path = require('path');

const API_BASE_URL = 'http://localhost:8080/api';
const ARTIFACTS_DIR = 'C:\\Users\\vaibh\\.gemini\\antigravity\\brain\\2705dafa-43fb-476c-a2a2-69bfc55030a1';

// Map each product to its generated image file
const productImages = [
  { product: 'iPhone 15 Pro Max (256 GB, Natural Titanium)', file: 'iphone_15_pro_max_1779362385160.png' },
  { product: 'Logitech MX Master 3S Wireless Mouse',        file: 'logitech_mouse_1779362408138.png' },
  { product: 'Slim Fit Cotton Casual Shirt (Black)',         file: 'cotton_casual_shirt_1779362432169.png' },
  { product: 'Premium Leather Classic Wallet',               file: 'leather_wallet_1779362454873.png' },
  { product: 'Ultrasonic Cool Mist Humidifier',              file: 'humidifier_1779362474448.png' },
  { product: 'Ceramide Barrier Hydrating Cream',             file: 'skincare_cream_1779362499649.png' },
  { product: 'Digital Air Fryer (4.2L, 1500W)',              file: 'air_fryer_1779426112074.png' },
];

async function uploadAndSeed() {
  try {
    // 1. Authenticate
    console.log('Authenticating...');
    const loginRes = await axios.post(`${API_BASE_URL}/auth/login`, {
      emailOrPhone: 'adarsht072@gmail.com',
      password: 'Adarsh@123'
    });
    const token = loginRes.data.token;
    console.log('Authenticated!\n');

    // 2. Upload each image to Cloudinary via backend
    const cloudinaryUrls = {};
    for (const item of productImages) {
      const filePath = path.join(ARTIFACTS_DIR, item.file);
      if (!fs.existsSync(filePath)) {
        console.error(`File not found: ${filePath}`);
        continue;
      }

      console.log(`Uploading ${item.file} for "${item.product}"...`);
      const fileBuffer = fs.readFileSync(filePath);
      const formData = new FormData();
      formData.append('image', fileBuffer, { filename: item.file, contentType: 'image/png' });

      const uploadRes = await axios.post(`${API_BASE_URL}/upload/image`, formData, {
        headers: {
          ...formData.getHeaders(),
          'Authorization': `Bearer ${token}`
        }
      });

      cloudinaryUrls[item.product] = uploadRes.data.url;
      console.log(`  -> ${uploadRes.data.url}`);
    }

    console.log('\n--- Cloudinary URLs ---');
    console.log(JSON.stringify(cloudinaryUrls, null, 2));

    // 3. Write the URLs to a JSON file for seed.js to use
    const outputPath = path.join(__dirname, 'cloudinary_urls.json');
    fs.writeFileSync(outputPath, JSON.stringify(cloudinaryUrls, null, 2));
    console.log(`\nSaved URLs to ${outputPath}`);

  } catch (err) {
    console.error('Error:', err.response ? err.response.data : err.message);
  }
}

uploadAndSeed();
