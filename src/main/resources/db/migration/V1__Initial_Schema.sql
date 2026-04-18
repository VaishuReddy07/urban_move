-- ============================================
-- UrbanMove Smart Mobility Platform
-- Database Initialization Script
-- ============================================

-- Create Roles
INSERT INTO roles (name, description, is_active, created_at, updated_at) VALUES
('ADMIN', 'Administrator with full access', true, NOW(), NOW()),
('OPERATOR', 'Fleet operator', true, NOW(), NOW()),
('DRIVER', 'Vehicle driver', true, NOW(), NOW()),
('RIDER', 'Passenger/rider', true, NOW(), NOW());

-- Create Permissions
INSERT INTO permissions (code, name, description, category, is_active, created_at, updated_at) VALUES
('USER_READ', 'Read users', 'Can view user information', 'USER_MANAGEMENT', true, NOW(), NOW()),
('USER_CREATE', 'Create users', 'Can create new users', 'USER_MANAGEMENT', true, NOW(), NOW()),
('USER_UPDATE', 'Update users', 'Can update user information', 'USER_MANAGEMENT', true, NOW(), NOW()),
('USER_DELETE', 'Delete users', 'Can delete users', 'USER_MANAGEMENT', true, NOW(), NOW()),
('VEHICLE_READ', 'Read vehicles', 'Can view vehicles', 'VEHICLE_MANAGEMENT', true, NOW(), NOW()),
('VEHICLE_CREATE', 'Create vehicles', 'Can register new vehicles', 'VEHICLE_MANAGEMENT', true, NOW(), NOW()),
('VEHICLE_UPDATE', 'Update vehicles', 'Can update vehicle information', 'VEHICLE_MANAGEMENT', true, NOW(), NOW()),
('ROUTE_READ', 'Read routes', 'Can view routes', 'ROUTE_MANAGEMENT', true, NOW(), NOW()),
('ROUTE_CREATE', 'Create routes', 'Can create new routes', 'ROUTE_MANAGEMENT', true, NOW(), NOW()),
('ROUTE_UPDATE', 'Update routes', 'Can update routes', 'ROUTE_MANAGEMENT', true, NOW(), NOW()),
('FLEET_READ', 'Read fleets', 'Can view fleets', 'FLEET_MANAGEMENT', true, NOW(), NOW()),
('FLEET_CREATE', 'Create fleets', 'Can create new fleets', 'FLEET_MANAGEMENT', true, NOW(), NOW()),
('ANALYTICS_READ', 'View analytics', 'Can view analytics and reports', 'ANALYTICS', true, NOW(), NOW());

-- Assign permissions to roles (Admin has all permissions)
INSERT INTO role_permissions (role_id, permission_id) 
SELECT r.id, p.id FROM roles r, permissions p WHERE r.name = 'ADMIN';

-- Assign permissions to Operator role
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p 
WHERE r.name = 'OPERATOR' AND p.code IN ('USER_READ', 'VEHICLE_READ', 'VEHICLE_UPDATE', 'ROUTE_READ', 'ROUTE_CREATE', 'ROUTE_UPDATE', 'FLEET_READ', 'ANALYTICS_READ');

-- Assign permissions to Driver role
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p 
WHERE r.name = 'DRIVER' AND p.code IN ('VEHICLE_READ', 'ROUTE_READ', 'ROUTE_UPDATE');

-- Assign permissions to Rider role
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p 
WHERE r.name = 'RIDER' AND p.code IN ('ROUTE_READ');

-- Create test user (Admin)
INSERT INTO users (username, email, password, full_name, phone_number, role, email_verified, account_locked, login_attempts, status, is_active, created_at, updated_at) VALUES
('admin', 'admin@urbanmove.com', '$2a$10$slYQmyNdGzin7olVn76p2OPST9/PgBkqquzi.Tz0KKUgO2t0jKMm.', 'Admin User', '+1-555-0001', 'ADMIN', true, false, 0, 'ACTIVE', true, NOW(), NOW());
-- Password: admin123

-- Create test fleets
INSERT INTO fleets (name, fleet_code, description, base_location, base_longitude, base_latitude, total_vehicles, active_vehicles, status, total_distance_km, is_active, created_at, updated_at) VALUES
('Downtown Fleet', 'DT001', 'Fleet operating in downtown area', 'Downtown Station', -74.0060, 40.7128, 0, 0, 'OPERATIONAL', 0.0, true, NOW(), NOW()),
('Airport Fleet', 'AP001', 'Fleet serving airport terminals', 'Airport Terminal 1', -73.8740, 40.7769, 0, 0, 'OPERATIONAL', 0.0, true, NOW(), NOW()),
('Suburban Fleet', 'SB001', 'Fleet operating in suburban areas', 'Suburban Hub', -74.1552, 40.5553, 0, 0, 'OPERATIONAL', 0.0, true, NOW(), NOW());
