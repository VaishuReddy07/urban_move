// UrbanMove Main JavaScript
document.addEventListener('DOMContentLoaded', function() {
    console.log('UrbanMove platform loaded');
    
    // Add any global event listeners or initializations here
});

// API Helper Functions
const API = {
    async get(url) {
        return this.request(url, 'GET');
    },
    
    async post(url, data) {
        return this.request(url, 'POST', data);
    },
    
    async put(url, data) {
        return this.request(url, 'PUT', data);
    },
    
    async delete(url) {
        return this.request(url, 'DELETE');
    },
    
    async request(url, method = 'GET', data = null) {
        const options = {
            method,
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            }
        };
        
        // Add JWT token if available
        const token = localStorage.getItem('jwt_token');
        if (token) {
            options.headers['Authorization'] = `Bearer ${token}`;
        }
        
        if (data) {
            options.body = JSON.stringify(data);
        }
        
        try {
            const response = await fetch(url, options);
            if (!response.ok) {
                if (response.status === 401) {
                    localStorage.removeItem('jwt_token');
                    window.location.href = '/login';
                }
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return await response.json();
        } catch (error) {
            console.error('API request failed:', error);
            throw error;
        }
    }
};

// Notification Helper
const Notify = {
    success(message) {
        console.log('Success:', message);
        // You can add toast notification library here
    },
    
    error(message) {
        console.error('Error:', message);
    },
    
    warning(message) {
        console.warn('Warning:', message);
    }
};
