const axios = require('axios');

const API_BASE_URL = 'http://localhost:8080/api';

async function deleteAll() {
  try {
    const loginRes = await axios.post(`${API_BASE_URL}/auth/login`, {
      emailOrPhone: 'adarsht072@gmail.com',
      password: 'Adarsh@123'
    });
    const token = loginRes.data.token;
    console.log('Authenticated as Admin.');

    const headers = { 'Authorization': `Bearer ${token}` };

    const getRes = await axios.get(`${API_BASE_URL}/products?size=100`);
    const products = getRes.data.content || getRes.data;

    console.log(`Found ${products.length} products to delete.`);

    for (const p of products) {
      await axios.delete(`${API_BASE_URL}/products/${p.id}`, { headers });
      console.log(`Deleted product ${p.id}`);
    }

    console.log('Done cleaning database.');
  } catch (err) {
    console.error(err);
  }
}

deleteAll();
