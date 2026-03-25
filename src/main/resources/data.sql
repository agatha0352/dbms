-- Clear all related tables before inserting users
DELETE FROM volunteer_assignments;
DELETE FROM requests;
DELETE FROM resources;
DELETE FROM notifications;
DELETE FROM users;
-- Clear users table before inserting new users
DELETE FROM users;
-- Create roles table if not exists
CREATE TABLE IF NOT EXISTS roles (
    id IDENTITY PRIMARY KEY,
    role_name VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(500),
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

-- Create notifications table if not exists
CREATE TABLE IF NOT EXISTS notifications (
    id IDENTITY PRIMARY KEY,
    user_id BIGINT NOT NULL,
    type VARCHAR(50) NOT NULL,
    title VARCHAR(255) NOT NULL,
    message VARCHAR(1000) NOT NULL,
    is_read BOOLEAN DEFAULT false,
    created_at TIMESTAMP,
    read_at TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Insert sample roles
INSERT INTO roles (role_name, description, created_at, updated_at) VALUES
('ADMIN', 'System Administrator - Full access to all features and user management', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('DONOR', 'Resource Donor - Can post and manage resources for distribution', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('VOLUNTEER', 'Volunteer - Can assist in resource delivery and request fulfillment', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('NEEDER', 'Disaster Needer - Can request resources and assistance', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert sample users with different roles
-- Admin password is: password
-- Other users password is: password
INSERT INTO users (username, password, email, full_name, phone_number, address, city, state, zip_code, role, enabled, verified, created_at) VALUES
('admin', '$2a$10$5gHHf5U6MdQp1a1jFKKYB.lRZ6rH8P0hhNkHKLTz2N8yGnG8h8Jx6', 'admin@sara.com', 'System Administrator', '1234567890', '123 Admin Street', 'Washington', 'DC', '20001', 'ADMIN', true, true, CURRENT_TIMESTAMP),
('donor1', '$2a$10$5gHHf5U6MdQp1a1jFKKYB.lRZ6rH8P0hhNkHKLTz2N8yGnG8h8Jx6', 'donor1@sara.com', 'John Donor', '2345678910', '456 Generous Ave', 'New York', 'NY', '10001', 'DONOR', true, true, CURRENT_TIMESTAMP),
('donor2', '$2a$10$5gHHf5U6MdQp1a1jFKKYB.lRZ6rH8P0hhNkHKLTz2N8yGnG8h8Jx6', 'donor2@sara.com', 'Emily Generous', '2345678940', '654 Charity Lane', 'Chicago', 'IL', '60601', 'DONOR', true, true, CURRENT_TIMESTAMP),
('volunteer1', '$2a$10$5gHHf5U6MdQp1a1jFKKYB.lRZ6rH8P0hhNkHKLTz2N8yGnG8h8Jx6', 'volunteer1@sara.com', 'Sarah Helper', '2345678920', '789 Service Road', 'Los Angeles', 'CA', '90001', 'VOLUNTEER', true, true, CURRENT_TIMESTAMP),
('volunteer2', '$2a$10$5gHHf5U6MdQp1a1jFKKYB.lRZ6rH8P0hhNkHKLTz2N8yGnG8h8Jx6', 'volunteer2@sara.com', 'David Volunteer', '2345678950', '987 Help Street', 'Seattle', 'WA', '98101', 'VOLUNTEER', true, true, CURRENT_TIMESTAMP),
('needer1', '$2a$10$5gHHf5U6MdQp1a1jFKKYB.lRZ6rH8P0hhNkHKLTz2N8yGnG8h8Jx6', 'needer1@sara.com', 'Mike Needhelp', '2345678930', '321 Need Help Blvd', 'Miami', 'FL', '33101', 'NEEDER', true, true, CURRENT_TIMESTAMP);

-- Insert sample resources
INSERT INTO resources (name, description, type, quantity, location, contact_info, status, user_id, verified, created_at, updated_at) VALUES
('Emergency Food Packages', 'Non-perishable food items for families in need', 'FOOD', 50, 'New York Community Center, 456 Generous Ave, New York, NY', 'Contact: +1234567891', 'AVAILABLE', 2, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Bottled Water Supply', 'Clean drinking water bottles', 'WATER', 200, 'Chicago Relief Center, 654 Charity Lane, Chicago, IL', 'Contact: +1234567894', 'AVAILABLE', 5, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Winter Blankets', 'Warm blankets for cold weather', 'BLANKETS', 30, 'New York Community Center, 456 Generous Ave, New York, NY', 'Contact: +1234567891', 'AVAILABLE', 2, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Basic First Aid Kits', 'Medical supplies for minor injuries', 'FIRST_AID', 25, 'Chicago Relief Center, 654 Charity Lane, Chicago, IL', 'Contact: +1234567894', 'AVAILABLE', 5, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Temporary Shelter Tents', 'Weather-resistant tents for temporary housing', 'SHELTER', 10, 'New York Community Center, 456 Generous Ave, New York, NY', 'Contact: +1234567891', 'AVAILABLE', 2, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert sample requests
INSERT INTO requests (title, description, resource_type, quantity_needed, location, urgency, status, user_id, created_at, updated_at, needed_by) VALUES
('Urgent Food Assistance', 'Family of 4 needs emergency food supplies after flooding', 'FOOD', 2, '321 Need Help Blvd, Miami, FL 33101', 'HIGH', 'OPEN', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, DATEADD('DAY', 1, CURRENT_TIMESTAMP)),
('Clean Water Needed', 'Community well contaminated, need clean water for 20 families', 'WATER', 100, 'Disaster Zone, Miami, FL', 'CRITICAL', 'OPEN', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, DATEADD('HOUR', 12, CURRENT_TIMESTAMP)),
('Medicine for Elderly', 'Prescription medication for diabetic patient', 'MEDICINE', 1, '321 Need Help Blvd, Miami, FL 33101', 'HIGH', 'OPEN', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, DATEADD('DAY', 2, CURRENT_TIMESTAMP));

-- Insert sample volunteer assignments
INSERT INTO volunteer_assignments (volunteer_id, request_id, status, assigned_at, notes) VALUES
(3, 1, 'ASSIGNED', CURRENT_TIMESTAMP, 'Will deliver food packages tomorrow morning'),
(6, 2, 'IN_PROGRESS', CURRENT_TIMESTAMP, 'Coordinating with local water distribution center');

-- Insert sample notifications
INSERT INTO notifications (user_id, type, title, message, is_read, created_at, read_at) VALUES
(2, 'RESOURCE_AVAILABLE', 'Food Resources Available', 'Your donated food packages are now available for distribution', false, CURRENT_TIMESTAMP, NULL),
(3, 'VOLUNTEER_ASSIGNED', 'Assignment Confirmed', 'You have been assigned to deliver emergency food supplies to Miami', false, CURRENT_TIMESTAMP, NULL),
(4, 'REQUEST_MATCHED', 'Resource Match Found', 'Your request for emergency food has been matched with available resources', false, CURRENT_TIMESTAMP, NULL),
(5, 'RESOURCE_AVAILABLE', 'Water Supply Ready', 'Clean water bottles are ready for distribution', true, DATEADD('DAY', -1, CURRENT_TIMESTAMP), CURRENT_TIMESTAMP),
(6, 'DELIVERY_COMPLETED', 'Delivery Completed', 'Water distribution to disaster zone completed successfully', false, CURRENT_TIMESTAMP, NULL),
(1, 'SYSTEM_ALERT', 'System Update', 'System maintenance scheduled for tonight at 11 PM', false, CURRENT_TIMESTAMP, NULL),
(4, 'REQUEST_FULFILLED', 'Request Fulfilled', 'Your medicine request has been fulfilled', false, CURRENT_TIMESTAMP, NULL);
