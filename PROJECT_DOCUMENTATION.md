# SARA - Smart Aid and Relief Alliance
## Complete Project Documentation

---

## 📋 Project Overview

**SARA** is a disaster resource management web application built with Spring Boot and Thymeleaf. It connects disaster victims, donors, volunteers, and administrators in times of crisis, facilitating efficient resource distribution and relief efforts.

---

## 🎯 Core Features

### 1. **Multi-Role System**
- **Victim**: Request emergency resources
- **Donor**: Donate and manage resources
- **Volunteer**: Help deliver resources and fulfill requests
- **Admin**: Manage users, resources, requests, and assignments

### 2. **Resource Management**
- Post available resources (food, water, shelter, medical supplies, etc.)
- Track resource status (available, reserved, delivered)
- Filter resources by type and location
- Verify resources (admin approval)

### 3. **Request Management**
- Create emergency requests with urgency levels (LOW, MEDIUM, HIGH, CRITICAL)
- Track request status (OPEN, IN_PROGRESS, FULFILLED, CANCELLED)
- Assign volunteers to requests
- Filter requests by urgency and location

### 4. **Volunteer Coordination**
- View available requests
- Accept assignments
- Track delivery status
- Complete assignments

### 5. **Admin Dashboard**
- User management (view, activate/deactivate, delete)
- Resource verification and management
- Request monitoring
- Volunteer assignment coordination
- Platform statistics

---

## 🛠️ Technology Stack

### **Backend Technologies**

#### 1. **Spring Boot 3.3.13** (Core Framework)
   - **Spring MVC**: Web layer and RESTful endpoints
   - **Spring Data JPA**: Database ORM and repositories
   - **Spring Security**: Authentication and authorization
   - **Spring Boot DevTools**: Hot reload during development

#### 2. **Database**
   - **H2 Database**: In-memory database for development
   - **Hibernate**: JPA implementation for ORM

#### 3. **Security**
   - **BCrypt Password Encoder**: Secure password hashing
   - **Role-Based Access Control**: ADMIN, DONOR, VOLUNTEER, VICTIM
   - **Custom UserDetailsService**: User authentication
   - **Access Denied Handler**: Custom error pages

#### 4. **Build Tool**
   - **Maven**: Dependency management and build automation

---

### **Frontend Technologies**

#### 1. **Template Engine**
   - **Thymeleaf**: Server-side Java template engine
   - **Thymeleaf Spring Security**: Security integration

#### 2. **CSS Framework**
   - **Bootstrap 5**: Responsive design framework
   - **Bootstrap Icons**: Icon library

#### 3. **Fonts**
   - **Google Fonts**: Poppins and Inter fonts

#### 4. **Custom Styling**
   - Modern gradient design system
   - CSS variables for consistent theming
   - Custom animations and transitions
   - Responsive card layouts

---

## 📂 Project Structure

### **Backend Structure**

```
src/main/java/com/sara/
│
├── SaraApplication.java                 # Main Spring Boot application
│
├── config/
│   └── SecurityConfig.java              # Spring Security configuration
│
├── controller/
│   ├── HomeController.java              # Home, dashboard, public pages
│   ├── AuthController.java              # Login, register, logout
│   ├── DonorController.java             # Donor resource management
│   ├── VictimController.java            # Victim request management
│   ├── VolunteerController.java         # Volunteer assignments
│   └── AdminController.java             # Admin panel management
│
├── entity/
│   ├── User.java                        # User entity (id, username, email, role, etc.)
│   ├── Resource.java                    # Resource entity (name, type, quantity, location)
│   ├── Request.java                     # Request entity (title, urgency, status)
│   └── VolunteerAssignment.java         # Assignment entity (volunteer, request, status)
│
├── repository/
│   ├── UserRepository.java              # User database operations
│   ├── ResourceRepository.java          # Resource database operations
│   ├── RequestRepository.java           # Request database operations
│   └── VolunteerAssignmentRepository.java # Assignment database operations
│
└── service/
    ├── UserService.java                 # User business logic
    ├── CustomUserDetailsService.java    # Spring Security user details
    ├── ResourceService.java             # Resource business logic
    ├── RequestService.java              # Request business logic
    └── VolunteerService.java            # Volunteer business logic
```

### **Frontend Structure**

```
src/main/resources/
│
├── templates/
│   ├── home.html                        # Landing page with hero, CTA cards
│   ├── about.html                       # About page
│   ├── contact.html                     # Contact page
│   ├── resources.html                   # Public resources listing
│   ├── requests.html                    # Public requests listing
│   ├── access-denied.html               # Custom 403 error page
│   │
│   ├── auth/
│   │   ├── login.html                   # Login page
│   │   └── register.html                # Registration page
│   │
│   ├── donor/
│   │   ├── dashboard.html               # Donor dashboard
│   │   ├── resources.html               # Donor's resources list
│   │   ├── resource-form.html           # Add/edit resource form
│   │   └── resources_view.html          # View single resource
│   │
│   ├── victim/
│   │   ├── dashboard.html               # Victim dashboard
│   │   ├── requests.html                # Victim's requests list
│   │   ├── request-form.html            # Add/edit request form
│   │   ├── resources_view.html          # Browse available resources
│   │   └── requests_view.html           # View single request
│   │
│   ├── volunteer/
│   │   ├── dashboard.html               # Volunteer dashboard
│   │   ├── requests.html                # Available requests
│   │   ├── assignments.html             # Volunteer's assignments
│   │   └── volunteer_form.html          # Assignment acceptance form
│   │
│   ├── admin/
│   │   ├── dashboard.html               # Admin dashboard with statistics
│   │   ├── users.html                   # User management
│   │   ├── resources.html               # Resource management
│   │   ├── resources_view.html          # View/verify resources
│   │   ├── requests.html                # Request management
│   │   ├── requests_view.html           # View request details
│   │   ├── assignments.html             # Assignment management
│   │   └── assignments_view.html        # View assignment details
│   │
│   └── fragments/
│       └── layout.html                  # Reusable components (navbar, footer, head, scripts)
│
├── static/
│   ├── css/
│   │   └── style.css                    # Custom CSS with modern design system
│   └── js/
│       └── app.js                       # Custom JavaScript
│
├── application.properties               # Spring Boot configuration
└── data.sql                             # Initial database seed data
```

---

## 🎨 Design System

### **Color Palette**
- **Primary Blue**: `#1E90FF` - Main actions, primary buttons
- **Accent Teal**: `#00BFA6` - Success states, highlights
- **Success Green**: `#10B981` - Success messages, available status
- **Warning Yellow**: `#F59E0B` - Warnings, pending status
- **Danger Red**: `#EF4444` - Errors, critical urgency
- **Info Blue**: `#3B82F6` - Information, volunteer role

### **Typography**
- **Primary Font**: Poppins (headings and UI)
- **Secondary Font**: Inter (body text)
- **Font Weights**: 300 (light), 400 (regular), 600 (semibold), 700 (bold)

### **Design Elements**
- Gradient hero sections
- Soft shadows for depth
- Rounded corners (8px, 12px)
- Smooth transitions (0.3s ease)
- Hover effects on interactive elements
- Responsive card layouts

---

## 🔐 Security Features

### **Authentication**
- BCrypt password hashing
- Session-based authentication
- Remember-me functionality
- Logout with session invalidation

### **Authorization**
- Role-based access control (RBAC)
- Method-level security
- URL-based security rules
- Custom access denied page

### **Security Rules**
```
Public Access: /, /home, /register, /login, /resources, /requests, /about, /contact
Admin Only: /admin/**
Donor Only: /donor/**
Volunteer Only: /volunteer/**
Victim Only: /victim/**
Authenticated: /dashboard
```

---

## 💾 Database Schema

### **Users Table**
- id (Primary Key)
- username (Unique)
- password (Encrypted)
- email (Unique)
- full_name
- phone
- address
- location
- role (ADMIN, DONOR, VOLUNTEER, VICTIM)
- enabled (Active/Inactive)

### **Resources Table**
- id (Primary Key)
- name
- description
- type (FOOD, WATER, SHELTER, MEDICAL, CLOTHING, OTHER)
- quantity
- location
- contact_info
- verified (Boolean)
- donor_id (Foreign Key → Users)

### **Requests Table**
- id (Primary Key)
- title
- description
- resource_type
- quantity_needed
- urgency (LOW, MEDIUM, HIGH, CRITICAL)
- location
- contact_info
- status (OPEN, IN_PROGRESS, FULFILLED, CANCELLED)
- victim_id (Foreign Key → Users)

### **Volunteer Assignments Table**
- id (Primary Key)
- volunteer_id (Foreign Key → Users)
- request_id (Foreign Key → Requests)
- status (PENDING, ACCEPTED, IN_PROGRESS, COMPLETED, CANCELLED)
- assigned_date
- completion_date
- notes

---

## 🌟 Key UI Components

### **Home Page**
- Gradient hero section with mission statement
- Three CTA cards (Get Help, Donate, Volunteer)
- Statistics dashboard (for authenticated users)
- Recent resources section

### **Dashboards**
- **Donor**: Statistics, recent resources, quick actions
- **Victim**: Request list, add request button
- **Volunteer**: Available requests, active assignments
- **Admin**: Platform statistics, user counts, recent activity

### **Forms**
- Modern input styling with focus effects
- Validation messages
- Success/error alerts
- Responsive layout

### **Tables**
- Sortable columns
- Filter options
- Status badges
- Action buttons (view, edit, delete)
- Empty state messages

### **Navigation**
- Responsive navbar with role-based menu items
- User info display (name and role)
- Gradient footer with quick links
- Breadcrumb navigation

---

## 📱 Responsive Design

### **Breakpoints**
- Mobile: < 768px
- Tablet: 768px - 1024px
- Desktop: > 1024px

### **Mobile Features**
- Collapsible navigation menu
- Stacked card layouts
- Touch-friendly buttons (min 44px)
- Optimized forms for mobile input

---

## 🔄 User Workflows

### **Victim Workflow**
1. Register as VICTIM
2. Login → Redirected to victim dashboard
3. Create new request with urgency level
4. View available resources
5. Track request status

### **Donor Workflow**
1. Register as DONOR
2. Login → Redirected to donor dashboard
3. Add resources with details
4. Manage posted resources
5. View requests from victims

### **Volunteer Workflow**
1. Register as VOLUNTEER
2. Login → Redirected to volunteer dashboard
3. Browse available requests
4. Accept assignments
5. Update delivery status
6. Complete assignments

### **Admin Workflow**
1. Login as ADMIN
2. View platform statistics
3. Manage users (activate/deactivate/delete)
4. Verify resources
5. Monitor requests
6. Manage volunteer assignments

---

## 🚀 Features to Highlight to Your Teacher

### **1. Full-Stack Development**
- Backend: Spring Boot with MVC architecture
- Frontend: Thymeleaf with Bootstrap 5
- Database: JPA/Hibernate with H2

### **2. Security Implementation**
- Role-based access control
- Password encryption
- Custom authentication
- Access denied handling

### **3. Modern UI/UX**
- Gradient design system
- Responsive layouts
- Custom CSS animations
- Professional color palette

### **4. Database Design**
- Proper entity relationships
- Foreign key constraints
- Repository pattern
- Service layer abstraction

### **5. Best Practices**
- MVC architecture
- Separation of concerns
- DRY principle (fragments/reusable components)
- RESTful URL structure

---

## 📊 Project Statistics

- **Total Files**: 50+ files
- **Controllers**: 5 (Home, Auth, Donor, Victim, Volunteer, Admin)
- **Entities**: 4 (User, Resource, Request, VolunteerAssignment)
- **Templates**: 30+ HTML pages
- **User Roles**: 4 (Admin, Donor, Volunteer, Victim)
- **Lines of Code**: ~3000+ lines (backend + frontend)

---

## 🎓 Learning Outcomes

### **Backend Skills**
- Spring Boot application development
- Spring Security configuration
- JPA/Hibernate ORM
- RESTful API design
- Service layer architecture

### **Frontend Skills**
- Thymeleaf templating
- Bootstrap responsive design
- Custom CSS styling
- Form validation
- User experience design

### **Software Engineering**
- MVC architecture
- Database design
- Authentication & authorization
- Error handling
- Version control (Git)

---

## 💡 Unique Features

1. **Role-Based Dashboards**: Each user type has a customized dashboard
2. **Access Denied Page**: User-friendly error handling with role-specific suggestions
3. **Modern Design**: Gradient colors, smooth animations, professional UI
4. **Filter & Search**: Filter resources by type, requests by urgency
5. **Status Tracking**: Track resources, requests, and assignments through lifecycle
6. **Responsive Design**: Works on desktop, tablet, and mobile devices

---

## 🔮 Future Enhancements (Optional to Mention)

- Real-time notifications
- Geolocation mapping
- Image upload for resources
- Email notifications
- Chat system between users
- Analytics dashboard
- Export reports (PDF/Excel)
- Multi-language support

---

## 📝 Conclusion

SARA is a comprehensive disaster relief management system that demonstrates full-stack web development skills, including:
- Backend development with Spring Boot
- Frontend development with Thymeleaf and Bootstrap
- Database design and management
- Security implementation
- Modern UI/UX design
- Real-world problem-solving

The application helps communities during disasters by efficiently connecting those who need help with those who can provide it.

---

**Developed by**: [Your Name]
**Date**: October 2025
**Framework**: Spring Boot 3.3.13
**Database**: H2
**Frontend**: Thymeleaf + Bootstrap 5
