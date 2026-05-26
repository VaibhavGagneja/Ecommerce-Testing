const axios = require('axios');

const API_BASE_URL = 'http://localhost:8080/api';

const mockProducts = [
  {
    name: 'iPhone 15 Pro Max (256 GB, Natural Titanium)',
    brand: 'Apple',
    description: 'Experience the ultimate iPhone. Featuring a strong and light aerospace-grade titanium design, a new Action button, powerful camera upgrades, and the A17 Pro chip for next-level gaming.',
    price: 139900.00,
    mrp: 159900.00,
    stockQuantity: 15,
    imageUrl: 'https://res.cloudinary.com/drls7brw2/image/upload/v1779426155/naztsv2xftjdhrvkctxz.jpg',
    category: 'Electronics',
    rating: 4.8,
    reviewsCount: 345,
    images: [
      'https://res.cloudinary.com/drls7brw2/image/upload/v1779426155/naztsv2xftjdhrvkctxz.jpg'
    ],
    highlights: [
      'Titanium design with textured matte glass back',
      'A17 Pro chip with 6-core GPU',
      'Pro camera system (48MP Main, Ultra Wide, and Telephoto)',
      'Up to 29 hours video playback'
    ],
    specifications: [
      { key: 'Model Name', value: 'iPhone 15 Pro Max' },
      { key: 'Color', value: 'Natural Titanium' },
      { key: 'Storage', value: '256 GB' },
      { key: 'Screen Size', value: '6.7 Inches' }
    ],
    inTheBox: 'iPhone, USB-C Charge Cable, Documentation',
    warranty: {
      type: 'Brand Warranty',
      summary: '1 Year Manufacturer Warranty'
    },
    manufacturer: {
      name: 'Apple Inc.',
      countryOfOrigin: 'China'
    },
    productLatitude: 28.6139,
    productLongitude: 77.2090
  },
  {
    name: 'Logitech MX Master 3S Wireless Mouse',
    brand: 'Logitech',
    description: 'Meet MX Master 3S – an iconic mouse remastered. Feel every moment of your workflow with even more precision, tactility, and performance, thanks to Quiet Clicks and an 8,000 DPI track-on-glass sensor.',
    price: 9495.00,
    mrp: 10995.00,
    stockQuantity: 50,
    imageUrl: 'https://res.cloudinary.com/drls7brw2/image/upload/v1779426159/jdpn7ei4cb36gcood4yf.jpg',
    category: 'Electronics',
    rating: 4.7,
    reviewsCount: 890,
    images: [
      'https://res.cloudinary.com/drls7brw2/image/upload/v1779426159/jdpn7ei4cb36gcood4yf.jpg'
    ],
    highlights: [
      'Any-surface tracking - now 8K DPI',
      'Quiet clicks with same satisfying click feel',
      'MagSpeed electromagnetic scrolling',
      'Ergonomic silhouette crafted for all-day comfort'
    ],
    specifications: [
      { key: 'Sensor Technology', value: 'Darkfield High Precision' },
      { key: 'DPI Range', value: '200 to 8000 DPI' },
      { key: 'Buttons', value: '7 Buttons' },
      { key: 'Connectivity', value: 'Bluetooth & Logi Bolt' }
    ],
    inTheBox: 'Mouse, Logi Bolt USB Receiver, USB-C Charging Cable, User Documents',
    warranty: {
      type: 'Limited Hardware Warranty',
      summary: '1 Year Limited Hardware Warranty'
    },
    manufacturer: {
      name: 'Logitech Asia Pacific',
      countryOfOrigin: 'China'
    },
    productLatitude: 19.0760,
    productLongitude: 72.8777
  },
  {
    name: 'Slim Fit Cotton Casual Shirt (Black)',
    brand: 'Zara',
    description: 'Slim fit casual shirt in breathable cotton fabric. Classic collar, long sleeves with buttoned cuffs, and button-up front. Perfect for everyday office wear or evening hangouts.',
    price: 1999.00,
    mrp: 2999.00,
    stockQuantity: 120,
    imageUrl: 'https://res.cloudinary.com/drls7brw2/image/upload/v1779426162/cy9n5wep18by5rp0qoew.jpg',
    category: 'Fashion',
    rating: 4.1,
    reviewsCount: 112,
    images: [
      'https://res.cloudinary.com/drls7brw2/image/upload/v1779426162/cy9n5wep18by5rp0qoew.jpg'
    ],
    highlights: [
      '100% Breathable Premium Cotton',
      'Slim Fit silhouette',
      'Long sleeves with adjustable cuffs',
      'Machine washable'
    ],
    specifications: [
      { key: 'Fit', value: 'Slim Fit' },
      { key: 'Material', value: '100% Cotton' },
      { key: 'Pattern', value: 'Solid' },
      { key: 'Occasion', value: 'Casual / Semi-formal' }
    ],
    inTheBox: '1 Casual Shirt',
    warranty: {
      type: 'No Warranty',
      summary: 'No brand warranty. 7-day return policy applies.'
    },
    manufacturer: {
      name: 'Zara Apparels Ltd',
      countryOfOrigin: 'India'
    },
    productLatitude: 12.9716,
    productLongitude: 77.5946
  },
  {
    name: 'Premium Leather Classic Wallet',
    brand: 'Tommy Hilfiger',
    description: 'Crafted from soft grain leather, this classic billfold wallet features multiple card slots, a coin pouch, and Tommy Hilfiger signature branding details. Ideal for everyday use.',
    price: 3499.00,
    mrp: 4499.00,
    stockQuantity: 80,
    imageUrl: 'https://res.cloudinary.com/drls7brw2/image/upload/v1779426165/xygqvazki6ayewrwdoqv.jpg',
    category: 'Fashion',
    rating: 4.5,
    reviewsCount: 75,
    images: [
      'https://res.cloudinary.com/drls7brw2/image/upload/v1779426165/xygqvazki6ayewrwdoqv.jpg'
    ],
    highlights: [
      'Genuine Grain Leather',
      '6 card slots, 2 slip pockets',
      '1 bill compartment',
      'RFID blocking technology built-in'
    ],
    specifications: [
      { key: 'Material', value: 'Genuine Leather' },
      { key: 'Style', value: 'Bi-fold Wallet' },
      { key: 'Dimensions', value: '11 x 9 x 2 cm' },
      { key: 'RFID Protected', value: 'Yes' }
    ],
    inTheBox: '1 Leather Wallet, Brand Box',
    warranty: {
      type: 'Domestic Warranty',
      summary: '6 Months Brand Warranty against manufacturing defects'
    },
    manufacturer: {
      name: 'Tommy Hilfiger Licensing LLC',
      countryOfOrigin: 'India'
    },
    productLatitude: 28.6139,
    productLongitude: 77.2090
  },
  {
    name: 'Ultrasonic Cool Mist Humidifier',
    brand: 'Philips',
    description: 'Philips cool mist humidifier helps maintain healthy indoor humidity levels. Its ultrasonic whisper-quiet operation is perfect for bedrooms and nurseries. Automatic shutoff when water runs out.',
    price: 4999.00,
    mrp: 6999.00,
    stockQuantity: 40,
    imageUrl: 'https://res.cloudinary.com/drls7brw2/image/upload/v1779426168/gkfovp4dkwse5wj0rhod.jpg',
    category: 'Home',
    rating: 4.3,
    reviewsCount: 215,
    images: [
      'https://res.cloudinary.com/drls7brw2/image/upload/v1779426168/gkfovp4dkwse5wj0rhod.jpg'
    ],
    highlights: [
      '3-Liter Water Tank capacity',
      'Whisper-quiet Ultrasonic operation',
      '360-degree rotating mist nozzle',
      'Up to 24 hours of continuous mist output'
    ],
    specifications: [
      { key: 'Tank Capacity', value: '3 Liters' },
      { key: 'Control Type', value: 'Dial Knob Control' },
      { key: 'Coverage Area', value: 'Up to 250 sq ft' },
      { key: 'Noise Level', value: 'Less than 26 dB' }
    ],
    inTheBox: 'Humidifier Unit, Filter, Adapter, User Manual',
    warranty: {
      type: 'Brand Warranty',
      summary: '2 Years Philips India Warranty'
    },
    manufacturer: {
      name: 'Philips India Limited',
      countryOfOrigin: 'India'
    },
    productLatitude: 13.0827,
    productLongitude: 80.2707
  },
  {
    name: 'Ceramide Barrier Hydrating Cream',
    brand: 'The Ordinary',
    description: 'Formulated with 5 essential ceramides and hyaluronic acid, this hydrating cream locks in moisture and repairs the skin barrier. Cruelty-free, fragrance-free, and suitable for sensitive skin.',
    price: 899.00,
    mrp: 1200.00,
    stockQuantity: 200,
    imageUrl: 'https://res.cloudinary.com/drls7brw2/image/upload/v1779426171/hgxkxypfyws0fvk64a9s.jpg',
    category: 'Beauty',
    rating: 4.6,
    reviewsCount: 1240,
    images: [
      'https://res.cloudinary.com/drls7brw2/image/upload/v1779426171/hgxkxypfyws0fvk64a9s.jpg'
    ],
    highlights: [
      'Formulated with 5 Essential Ceramides',
      'Deep 24-hour hydration lock',
      'Non-greasy, fast-absorbing texture',
      'Dermatologically tested'
    ],
    specifications: [
      { key: 'Skin Type', value: 'Dry / Sensitive / Normal' },
      { key: 'Volume', value: '50 ml' },
      { key: 'Key Ingredients', value: 'Ceramides, Hyaluronic Acid' },
      { key: 'Fragrance Free', value: 'Yes' }
    ],
    inTheBox: '1 Moisturizer Tube',
    warranty: {
      type: 'No Warranty',
      summary: 'Cosmetic product. 7-day return policy applies only for unused/sealed items.'
    },
    manufacturer: {
      name: 'Deciem Beauty Group',
      countryOfOrigin: 'Canada'
    },
    productLatitude: 19.0760,
    productLongitude: 72.8777
  },
  {
    name: 'Digital Air Fryer (4.2L, 1500W)',
    brand: 'Havells',
    description: 'Fry, bake, grill, and roast with up to 85% less oil. Havells Digital Air Fryer features a large touchscreen panel, 8 preset cooking options, and rapid air convection technology.',
    price: 6499.00,
    mrp: 9999.00,
    stockQuantity: 25,
    imageUrl: 'https://res.cloudinary.com/drls7brw2/image/upload/v1779426174/tltzic65qo2zps685buf.jpg',
    category: 'Appliances',
    rating: 4.4,
    reviewsCount: 198,
    images: [
      'https://res.cloudinary.com/drls7brw2/image/upload/v1779426174/tltzic65qo2zps685buf.jpg'
    ],
    highlights: [
      '4.2 Liters cooking basket capacity',
      'Touch control with 8 preset programs',
      'Rapid air convection heating',
      'Auto-off timer up to 60 mins'
    ],
    specifications: [
      { key: 'Capacity', value: '4.2 Liters' },
      { key: 'Wattage', value: '1500 W' },
      { key: 'Preset Recipes', value: '8 presets' },
      { key: 'Temperature Range', value: '80 to 200 deg C' }
    ],
    inTheBox: 'Air Fryer Main Unit, Non-stick Basket, Recipe Book, User Manual',
    warranty: {
      type: 'Brand Warranty',
      summary: '2 Years Manufacturer Warranty'
    },
    manufacturer: {
      name: 'Havells India Ltd',
      countryOfOrigin: 'India'
    },
    productLatitude: 28.6139,
    productLongitude: 77.2090
  }
];

async function seed() {
  try {
    console.log('1. Ensuring Admin and Seller accounts exist...');
    
    // Create admin account
    const adminRes = await axios.post(`${API_BASE_URL}/auth/create-admin`);
    console.log('Admin:', adminRes.data.message);

    // Create seller account
    const sellerRes = await axios.post(`${API_BASE_URL}/auth/create-seller`).catch(err => {
      if (err.response && err.response.data && typeof err.response.data === 'string' && err.response.data.includes('already exists')) {
        return { data: { message: 'Seller already exists' } };
      }
      throw err;
    });
    console.log('Seller:', sellerRes.data.message || 'Already exists');

    console.log('\n2. Authenticating as Admin...');
    const loginRes = await axios.post(`${API_BASE_URL}/auth/login`, {
      emailOrPhone: 'adarsht072@gmail.com',
      password: 'Adarsh@123'
    });

    const token = loginRes.data.token;
    console.log('Authentication successful!');

    console.log('\n3. Seeding products into database...');
    const headers = {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    };

    for (const product of mockProducts) {
      console.log(`Adding: ${product.name}...`);
      await axios.post(`${API_BASE_URL}/products`, product, { headers });
    }

    console.log('\n✅ Database successfully seeded with 7 premium products across categories!');
  } catch (error) {
    console.error('❌ Seeding failed:', error.response ? error.response.data : error.message);
  }
}

seed();
