import { render, screen } from '@testing-library/react';
import ProductCard from './ProductCard';

describe('ProductCard Component', () => {
  const mockProduct = {
    id: '1',
    name: 'iPhone 15',
    description: 'Latest iPhone model',
    price: 999.99
  };

  test('renders product name', () => {
    render(<ProductCard product={mockProduct} />);
    expect(screen.getByText('iPhone 15')).toBeInTheDocument();
  });

  test('renders product description', () => {
    render(<ProductCard product={mockProduct} />);
    expect(screen.getByText('Latest iPhone model')).toBeInTheDocument();
  });

  test('renders product price', () => {
    render(<ProductCard product={mockProduct} />);
    expect(screen.getByText('$999.99')).toBeInTheDocument();
  });

  test('renders Add to Cart button', () => {
    render(<ProductCard product={mockProduct} />);
    expect(screen.getByText(/Add to Cart/i)).toBeInTheDocument();
  });
});