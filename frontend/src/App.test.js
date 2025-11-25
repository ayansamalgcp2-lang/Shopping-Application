import { render, screen, waitFor, fireEvent } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import axios from 'axios';
import MockAdapter from 'axios-mock-adapter';
import App from './App';

// Create axios mock
const mock = new MockAdapter(axios);

describe('App Component', () => {
  beforeEach(() => {
    // Reset mock before each test
    mock.reset();
  });

  test('renders Shopping Application header', () => {
    render(<App />);
    const headerElement = screen.getByText(/Shopping Application/i);
    expect(headerElement).toBeInTheDocument();
  });

  test('renders Add New Product button', () => {
    render(<App />);
    const buttonElement = screen.getByText(/Add New Product/i);
    expect(buttonElement).toBeInTheDocument();
  });

  test('shows empty state when no products', async () => {
    // Mock empty products response
    mock.onGet('/api/product').reply(200, []);

    render(<App />);

    await waitFor(() => {
      expect(screen.getByText(/No products available yet/i)).toBeInTheDocument();
    });
  });

  test('displays products when API returns data', async () => {
    // Mock products response
    const mockProducts = [
      {
        id: '1',
        name: 'iPhone 15',
        description: 'Latest iPhone',
        price: 999.99
      },
      {
        id: '2',
        name: 'MacBook Pro',
        description: 'M3 chip',
        price: 1999.99
      }
    ];

    mock.onGet('/api/product').reply(200, mockProducts);

    render(<App />);

    await waitFor(() => {
      expect(screen.getByText('iPhone 15')).toBeInTheDocument();
      expect(screen.getByText('MacBook Pro')).toBeInTheDocument();
      expect(screen.getByText(/Available Products \(2\)/i)).toBeInTheDocument();
    });
  });

  test('shows form when Add New Product button is clicked', async () => {
    mock.onGet('/api/product').reply(200, []);

    render(<App />);

    const addButton = screen.getByText(/Add New Product/i);
    fireEvent.click(addButton);

    await waitFor(() => {
      expect(screen.getByPlaceholderText(/e.g., iPhone 15 Pro/i)).toBeInTheDocument();
      expect(screen.getByText(/✖ Cancel/i)).toBeInTheDocument();
    });
  });

  test('creates product successfully', async () => {
    const user = userEvent.setup();

    // Mock initial empty products
    mock.onGet('/api/product').reply(200, []);

    // Mock create product
    mock.onPost('/api/product').reply(201, {
      id: '1',
      name: 'Test Product',
      description: 'Test Description',
      price: 100.00
    });

    // Mock products after creation
    mock.onGet('/api/product').reply(200, [{
      id: '1',
      name: 'Test Product',
      description: 'Test Description',
      price: 100.00
    }]);

    render(<App />);

    // Click Add New Product
    const addButton = screen.getByText(/Add New Product/i);
    await user.click(addButton);

    // Fill form
    const nameInput = screen.getByPlaceholderText(/e.g., iPhone 15 Pro/i);
    const descInput = screen.getByPlaceholderText(/Enter product description/i);
    const priceInput = screen.getByPlaceholderText(/99.99/i);

    await user.type(nameInput, 'Test Product');
    await user.type(descInput, 'Test Description');
    await user.type(priceInput, '100');

    // Submit form
    const submitButton = screen.getByRole('button', { name: /Add Product/i });
    await user.click(submitButton);

    // Wait for success
    await waitFor(() => {
      expect(screen.getByText('Test Product')).toBeInTheDocument();
    });
  });

  test('handles API error gracefully', async () => {
    // Mock API error
    mock.onGet('/api/product').reply(500, { message: 'Server Error' });

    // Spy on console.error
    const consoleSpy = jest.spyOn(console, 'error').mockImplementation(() => {});

    render(<App />);

    await waitFor(() => {
      expect(consoleSpy).toHaveBeenCalledWith(
        'Error fetching products:',
        expect.any(Error)
      );
    });

    consoleSpy.mockRestore();
  });

  test('displays product prices correctly', async () => {
    const mockProducts = [{
      id: '1',
      name: 'iPhone',
      description: 'Test',
      price: 999.99
    }];

    mock.onGet('/api/product').reply(200, mockProducts);

    render(<App />);

    await waitFor(() => {
      expect(screen.getByText('$999.99')).toBeInTheDocument();
    });
  });

  test('validates required fields', async () => {
    const user = userEvent.setup();
    mock.onGet('/api/product').reply(200, []);

    render(<App />);

    // Open form
    const addButton = screen.getByText(/Add New Product/i);
    await user.click(addButton);

    // Try to submit without filling fields
    const submitButton = screen.getByRole('button', { name: /Add Product/i });
    await user.click(submitButton);

    // HTML5 validation should prevent submission
    const nameInput = screen.getByPlaceholderText(/e.g., iPhone 15 Pro/i);
    expect(nameInput).toBeInvalid();
  });

  test('closes form when Cancel is clicked', async () => {
    const user = userEvent.setup();
    mock.onGet('/api/product').reply(200, []);

    render(<App />);

    // Open form
    const addButton = screen.getByText(/Add New Product/i);
    await user.click(addButton);

    // Click Cancel
    const cancelButton = screen.getByText(/✖ Cancel/i);
    await user.click(cancelButton);

    // Form should be hidden
    await waitFor(() => {
      expect(screen.queryByPlaceholderText(/e.g., iPhone 15 Pro/i)).not.toBeInTheDocument();
    });
  });
});