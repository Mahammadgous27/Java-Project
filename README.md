# Smart Hotel Services System

## Overview
Smart Hotel Services is a **Java Swing + MySQL based desktop application** that helps customers and restaurant owners manage hotel rooms, dining, and food orders in one system.  
It provides booking, ordering, payment, and feedback features with a simple graphical interface.

---

## Technologies Used
- **Frontend / UI:** Java Swing  
- **Backend Logic:** Core Java  
- **Database:** MySQL  
- **Connectivity:** JDBC (Java Database Connectivity)  
- **IDE:** NetBeans / IntelliJ / Eclipse  

---

## Modules

### 1. Authentication
- Login & Signup for **Customer** and **Restaurant Owner**
- Username & Password validation
- Role-based access

### 2. Customer Module
- View available **Rooms** and **Dining Tables**
- Book rooms and reserve tables
- View menu and **order food**
- Cancel booking/order within time limit
- Track order status (Placed → Preparing → Ready → Served)
- Make **Online or Cash Payment**
- Give **Feedback & Rating**

### 3. Restaurant Owner Module
- Manage Menu (Add / Update / Delete Items)
- View and manage bookings
- Receive and update food orders
- Assign waiters
- View daily revenue and feedback

### 4. Kitchen Module
- Receive new orders
- Display order details with preparation time
- Notify waiter when food is ready

### 5. Waiter Module
- View assigned tables/orders
- Mark orders as **Served**

### 6. Payment & Feedback
- Generate bill/invoice
- Store payment details in database
- Collect customer reviews

---

## Database (MySQL Tables – Example)
- `users` – customer & owner login details  
- `rooms` – room information  
- `tables` – dining table details  
- `menu` – food items and prices  
- `orders` – food orders  
- `payments` – transaction records  
- `feedback` – ratings and comments  

---

## Objective
To create a **desktop-based smart hotel management system** using **Java Swing and MySQL** that automates booking, ordering, and service management in a simple and efficient way.

---

## Future Enhancements
- Email/SMS notifications  
- Online payment gateway integration  
- Mobile app version  
- Analytics dashboard  

---
