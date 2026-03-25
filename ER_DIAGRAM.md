# SARA Database - Entity Relationship Diagram

## **Complete Database Schema with Relationships**

```
┌─────────────────────────────────────────────────────────────────────────────────────────┐
│                                     DATABASE SCHEMA                                     │
└─────────────────────────────────────────────────────────────────────────────────────────┘

                            ┌──────────────────────┐
                            │       ROLES          │
                            ├──────────────────────┤
                            │ id (PK)              │
                            │ roleName (UNIQUE)    │
                            │ description          │
                            │ created_at           │
                            │ updated_at           │
                            └──────────────────────┘
                                      ▲
                                      │ 1:N
                                      │
                            ┌─────────┴──────────┐
                            │                    │
┌──────────────────────────────────────────────────────────────────────┐
│                           USERS                                      │
├──────────────────────────────────────────────────────────────────────┤
│ id (PK)                                                              │
│ username                                                             │
│ password                                                             │
│ email                                                                │
│ fullName                                                             │
│ phoneNumber                                                          │
│ address                                                              │
│ city                                                                 │
│ state                                                                │
│ zipCode                                                              │
│ role (FK to ROLES)                                                   │
│ enabled                                                              │
│ verified                                                             │
│ created_at                                                           │
└──────────────────────────────────────────────────────────────────────┘
        ▲         ▲         ▲              ▲              ▲
        │         │         │              │              │
   1:N  │    1:N  │    1:N  │          1:N │          1:N │
        │         │         │              │              │
        │         │         │              │              │
        │    ┌────┴─────────┴──────┬───────┴──────┐       │
        │    │                     │              │       │
        │    │                     │              │       │
┌──────┴──────────────────┐  ┌─────┴────────────────┐     │
│     RESOURCES           │  │     REQUESTS         │     │
├─────────────────────────┤  ├──────────────────────┤     │
│ id (PK)                 │  │ id (PK)              │     │
│ name                    │  │ title                │     │
│ description             │  │ description          │     │
│ type (ENUM)             │  │ resourceType (ENUM)  │     │
│ quantity                │  │ quantityNeeded       │     │
│ location                │  │ location             │     │
│ contactInfo             │  │ urgency (ENUM)       │     │
│ status (ENUM)           │  │ status (ENUM)        │     │
│ user_id (FK) ──────────┐│  │ user_id (FK) ───────┐│     │
│ verified                ││  │ created_at           ││     │
│ created_at              ││  │ updated_at           ││     │
│ updated_at              ││  │ needed_by            ││     │
└────────┬────────────────┘│  └─────────┬────────────┘│    │
         │                 │            │              │    │
         │ 1:N             │            │ 1:N          │    │
         │                 │            │              │    │
         │         ┌───────┴─────────────┴──────┐      │    │
         │         │                           │      │    │
         │    ┌────────────────────────────────────────┴────┘
         │    │
┌────────┴──────────────────┐  ┌──────────────────────────────────┐
│ RESOURCE_ASSIGNMENTS      │  │ VOLUNTEER_ASSIGNMENTS            │
├────────────────────────────┤  ├───────────────────────────────────┤
│ id (PK)                    │  │ id (PK)                           │
│ resource_id (FK) ──────────┼──┼─ volunteer_id (FK)            │   │
│ volunteer_id (FK) ─────────┼──┼─ request_id (FK) ──────────────────┤─ (to REQUESTS)
│ assigned_at                │  │ status (ENUM)                     │
│ delivered_at               │  │ assigned_at                       │
│ status (ENUM)              │  │ completed_at                      │
│ notes                      │  │ notes                             │
└────────────────────────────┘  └───────────────────────────────────┘
         ▲                                 ▲
         └─────────────────┬───────────────┘
                           │ Both reference USERS
                           │ (Volunteers)
                           │

┌──────────────────────────────────┐
│      NOTIFICATIONS               │
├──────────────────────────────────┤
│ id (PK)                          │
│ user_id (FK) ──────────────┐     │
│ type (ENUM)                │     │
│ title                      │     │
│ message                    │     │
│ is_read                    │     │
│ created_at                 │     │
│ read_at                    │     │
└──────────────────────────────────┘
```

---

## **Detailed Relationship Mapping**

| Relationship ID | From Table | To Table | Type | Cardinality | Foreign Key | Description |
|---|---|---|---|---|---|---|
| **R1** | ROLES | USERS | References | 1:N | `users.role` | Each role can be assigned to many users |
| **R2** | USERS | RESOURCES | Creates | 1:N | `resources.user_id` | Each donor user can post multiple resources |
| **R3** | USERS | REQUESTS | Creates | 1:N | `requests.user_id` | Each victim user can create multiple requests |
| **R4** | USERS | RESOURCE_ASSIGNMENTS | Assigns | 1:N | `resource_assignments.volunteer_id` | Each volunteer can have multiple resource assignments |
| **R5** | USERS | VOLUNTEER_ASSIGNMENTS | Assigns | 1:N | `volunteer_assignments.volunteer_id` | Each volunteer can have multiple volunteer assignments |
| **R6** | USERS | NOTIFICATIONS | Receives | 1:N | `notifications.user_id` | Each user can receive multiple notifications |
| **R7** | RESOURCES | RESOURCE_ASSIGNMENTS | Assigned To | 1:N | `resource_assignments.resource_id` | Each resource can be assigned to multiple volunteers |
| **R8** | REQUESTS | VOLUNTEER_ASSIGNMENTS | Assigned To | 1:N | `volunteer_assignments.request_id` | Each request can have multiple volunteer assignments |

---

## **Relationship Explanations**

### **R1: ROLES → USERS (1:N)**
```
One role can be held by many users.
Example: The DONOR role can be assigned to user 'donor1', 'donor2', etc.

SQL: SELECT * FROM users WHERE role = 'DONOR';
```

### **R2: USERS → RESOURCES (1:N)**
```
One user (donor) can donate/create many resources.
Example: Donor 'John' can post Food, Water, and Blankets.

Foreign Key: resources.user_id = users.id
SQL: SELECT * FROM resources WHERE user_id = 2;
```

### **R3: USERS → REQUESTS (1:N)**
```
One user (victim) can create many resource requests.
Example: Victim 'Mike' can request Food, Water, and Medicine.

Foreign Key: requests.user_id = users.id
SQL: SELECT * FROM requests WHERE user_id = 4;
```

### **R4: USERS → RESOURCE_ASSIGNMENTS (1:N)**
```
One volunteer user can be assigned to deliver many resources.
Example: Volunteer 'Sarah' can deliver 3 different resource shipments.

Foreign Key: resource_assignments.volunteer_id = users.id
SQL: SELECT * FROM resource_assignments WHERE volunteer_id = 3;
```

### **R5: USERS → VOLUNTEER_ASSIGNMENTS (1:N)**
```
One volunteer user can be assigned to fulfill many requests.
Example: Volunteer 'David' can be assigned to handle 5 different requests.

Foreign Key: volunteer_assignments.volunteer_id = users.id
SQL: SELECT * FROM volunteer_assignments WHERE volunteer_id = 6;
```

### **R6: USERS → NOTIFICATIONS (1:N)**
```
One user can receive many notifications from the system.
Example: User 'Sarah' receives notifications about:
  - Assignment confirmations
  - Delivery updates
  - System alerts

Foreign Key: notifications.user_id = users.id
SQL: SELECT * FROM notifications WHERE user_id = 3 ORDER BY created_at DESC;
```

### **R7: RESOURCES → RESOURCE_ASSIGNMENTS (1:N)**
```
One resource can be assigned to multiple volunteers for delivery.
Example: A batch of 50 food packages can be split and assigned to 3 volunteers.

Foreign Key: resource_assignments.resource_id = resources.id
SQL: SELECT * FROM resource_assignments WHERE resource_id = 1;
```

### **R8: REQUESTS → VOLUNTEER_ASSIGNMENTS (1:N)**
```
One request can have multiple volunteers assigned to fulfill it.
Example: A request for 100 water bottles can be fulfilled by multiple volunteers
coordinating together.

Foreign Key: volunteer_assignments.request_id = requests.id
SQL: SELECT * FROM volunteer_assignments WHERE request_id = 2;
```

---

## **Data Flow Examples**

### **Example 1: Resource Donation and Delivery**
```
1. Donor (user_id=2) creates RESOURCES entry
   ↓
2. Admin creates RESOURCE_ASSIGNMENTS linking Resource to Volunteer (user_id=3)
   ↓
3. System sends NOTIFICATION to Volunteer
   ↓
4. Volunteer delivers resource (updates status in RESOURCE_ASSIGNMENTS)
   ↓
5. System sends NOTIFICATION to Donor and Victim
```

### **Example 2: Victim Request and Fulfillment**
```
1. Victim (user_id=4) creates REQUEST
   ↓
2. System matches REQUEST with available RESOURCES
   ↓
3. Admin creates VOLUNTEER_ASSIGNMENTS linking Volunteer to REQUEST
   ↓
4. System sends NOTIFICATION to Volunteer
   ↓
5. Volunteer completes assignment (updates status in VOLUNTEER_ASSIGNMENTS)
   ↓
6. System updates REQUEST status to FULFILLED
   ↓
7. System sends NOTIFICATION to Victim
```

---

## **Key Points**

✅ **Primary Key (PK)**: Unique identifier for each row (id field)
✅ **Foreign Key (FK)**: Links to another table's primary key
✅ **1:N Relationship**: One record in Table A relates to many records in Table B
✅ **ROLES Table**: Master reference table for user types
✅ **USERS Table**: Central hub connecting to all other tables
✅ **NOTIFICATIONS**: Enables system-wide communication with users
