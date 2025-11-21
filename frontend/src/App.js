import React, { useState, useEffect } from 'react';
import './App.css';
import axios from 'axios';

const API_URL = '/api/product';

function App() {
  const [products, setProducts] = useState([]);
  const [showForm, setShowForm] = useState(false);
  const [formData, setFormData] = useState({
    name: '',
    description: '',
    price: ''
  });

  // Fetch products on component mount
  useEffect(() => {
    fetchProducts();
  }, []);

  const fetchProducts = async () => {
    try {
      const response = await axios.get(API_URL);
      setProducts(response.data);
    } catch (error) {
      console.error('Error fetching products:', error);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await axios.post(API_URL, {
        name: formData.name,
        description: formData.description,
        price: parseFloat(formData.price)
      });
      
      // Reset form
      setFormData({ name: '', description: '', price: '' });
      setShowForm(false);
      
      // Refresh products
      fetchProducts();
      
      alert('Product added successfully!');
    } catch (error) {
      console.error('Error creating product:', error);
      alert('Error adding product. Check console for details.');
    }
  };

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  // Sample product images (will use placeholder images)
  const getProductImage = (productName) => {
    const imageMap = {
      'iphone': 'https://images.unsplash.com/photo-1592286927505-f0b00ea6f345?w=400',
      'macbook': 'https://images.unsplash.com/photo-1517336714731-489689fd1ca8?w=400',
      'airpods': 'https://images.unsplash.com/photo-1606841837239-c5a1a4a07af7?w=400',
      'ipad': 'https://images.unsplash.com/photo-1544244015-0df4b3ffc6b0?w=400',
      'watch': 'https://images.unsplash.com/photo-1579586337278-3befd40fd17a?w=400',
      'default': 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=400'
    };

    const name = productName.toLowerCase();
    for (let key in imageMap) {
      if (name.includes(key)) {
        return imageMap[key];
      }
    }
    return imageMap.default;
  };

  return (
    <div className="App">
      {/* Header */}
      <header className="header">
        <div className="container">
          <h1>🛒 Shopping Application</h1>
          <p>Your one-stop shop for amazing products</p>
        </div>
      </header>

      {/* Main Content */}
      <main className="container">
        {/* Add Product Button */}
        <div className="action-bar">
          <button 
            className="btn-primary" 
            onClick={() => setShowForm(!showForm)}
          >
            {showForm ? '✖ Cancel' : '➕ Add New Product'}
          </button>
        </div>

        {/* Add Product Form */}
        {showForm && (
          <div className="form-container">
            <h2>Add New Product</h2>
            <form onSubmit={handleSubmit}>
              <div className="form-group">
                <label>Product Name</label>
                <input
                  type="text"
                  name="name"
                  value={formData.name}
                  onChange={handleChange}
                  placeholder="e.g., iPhone 15 Pro"
                  required
                />
              </div>
              
              <div className="form-group">
                <label>Description</label>
                <textarea
                  name="description"
                  value={formData.description}
                  onChange={handleChange}
                  placeholder="Enter product description"
                  rows="4"
                  required
                />
              </div>
              
              <div className="form-group">
                <label>Price ($)</label>
                <input
                  type="number"
                  name="price"
                  value={formData.price}
                  onChange={handleChange}
                  placeholder="99.99"
                  step="0.01"
                  min="0"
                  required
                />
              </div>
              
              <button type="submit" className="btn-primary">
                Add Product
              </button>
            </form>
          </div>
        )}

        {/* Products Grid */}
        <div className="products-section">
          <h2>Available Products ({products.length})</h2>
          
          {products.length === 0 ? (
            <div className="empty-state">
              <p>No products available yet.</p>
              <p>Click "Add New Product" to get started!</p>
            </div>
          ) : (
            <div className="products-grid">
              {products.map((product) => (
                <div key={product.id} className="product-card">
                  <div className="product-image">
                    <img 
                      src={getProductImage(product.name)} 
                      alt={product.name}
                      onError={(e) => {
                        e.target.src = 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=400';
                      }}
                    />
                  </div>
                  <div className="product-info">
                    <h3>{product.name}</h3>
                    <p className="product-description">{product.description}</p>
                    <div className="product-footer">
                      <span className="product-price">${product.price.toFixed(2)}</span>
                      <button className="btn-secondary">Add to Cart</button>
                    </div>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
      </main>

      {/* Footer */}
      <footer className="footer">
        <p>© 2025 Shopping Application | Built with React & Spring Boot</p>
      </footer>
    </div>
  );
}

export default App;
