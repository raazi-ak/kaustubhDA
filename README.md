# Real Estate Listing Portal

A JSF + Hibernate web application for property listings and customer inquiries.

## Features

- Property listing management
- Customer inquiry system
- Agent management
- Advanced filtering and sorting
- JSF navigation between pages
- Custom JSF converters (currency, date, property category)
- AJAX-enabled event handling for dynamic updates
- Hibernate ORM with entity relationships

## Technology Stack

- **JSF**: Jakarta Faces 4.0.1
- **Hibernate**: 6.2.13.Final
- **Application Server**: Apache Tomcat 10.1
- **Database**: PostgreSQL 15
- **Build Tool**: Maven 3.9
- **Java**: 17
- **Containerization**: Docker Compose

## Project Structure

```
kaustubhDA/
├── docker-compose.yml          # Docker services configuration
├── Dockerfile                   # Tomcat container build
├── pom.xml                      # Maven dependencies
├── src/main/
│   ├── java/com/realestate/
│   │   ├── model/              # Hibernate entities
│   │   ├── dao/                # Data access objects
│   │   ├── converter/          # JSF custom converters
│   │   ├── bean/               # JSF managed beans
│   │   └── util/               # Utility classes
│   ├── resources/
│   │   └── META-INF/
│   │       └── persistence.xml # JPA configuration
│   └── webapp/
│       ├── WEB-INF/
│       │   ├── web.xml         # Web application config
│       │   └── faces-config.xml # JSF navigation & converters
│       ├── resources/css/
│       │   └── styles.css      # Styling
│       └── *.xhtml             # JSF pages
└── init-db/
    └── init.sql                # Database initialization
```

## Entity Relationships

- **Property** ↔️ **Agent**: Many-to-One (Property has one Agent)
- **Property** ↔️ **Inquiry**: One-to-Many (Property has many Inquiries)
- **Agent** ↔️ **Inquiry**: One-to-Many (Agent receives many Inquiries)
- **Customer** ↔️ **Inquiry**: One-to-Many (Customer makes many Inquiries)

## Setup and Running

### Prerequisites

- Docker and Docker Compose installed
- Maven 3.9+ (if building locally)

### Running with Docker

1. Build and start the application:
```bash
docker-compose up --build
```

2. Access the application:
   - Application: http://localhost:8080/realestate-portal/
   - PostgreSQL: localhost:5432

3. Stop the application:
```bash
docker-compose down
```

### Building Locally

1. Build the WAR file:
```bash
mvn clean package
```

2. Deploy the WAR file to Tomcat:
   - Copy `target/realestate-portal.war` to Tomcat's `webapps/` directory
   - Ensure PostgreSQL is running and accessible
   - Update `persistence.xml` with correct database connection details

## Pages

1. **Home** (`home.xhtml`): Welcome page with navigation
2. **Add Listing** (`add-listing.xhtml`): Add new property listings
3. **View Listings** (`view-listings.xhtml`): Browse, filter, and sort properties
4. **Contact Seller** (`contact-seller.xhtml`): Submit inquiries for properties

## JSF Features Demonstrated

### Navigation
- Implicit navigation using outcome values
- Explicit navigation rules in `faces-config.xml`
- Navigation between all four pages

### Converters
- **CurrencyConverter**: Formats prices as currency
- **DateConverter**: Handles date formatting (yyyy-MM-dd)
- **PropertyCategoryConverter**: Validates property types

### Event Handling
- AJAX-enabled filtering (property type, status, price range)
- Real-time search functionality
- Dynamic sorting (by price, date, type)
- Event listeners for form submissions

## Hibernate Features

- Entity relationships with `@OneToMany`, `@ManyToOne`, `@JoinColumn`
- Cascade operations for related entities
- Automatic schema generation (`hibernate.hbm2ddl.auto=update`)
- JPA annotations for entity mapping

## Database Schema

The application automatically creates the following tables:
- `agents`: Real estate agents
- `properties`: Property listings
- `customers`: Customer information
- `inquiries`: Customer inquiries for properties

## Deployment to Render

The application is configured for deployment on Render.com:

### Prerequisites
- Render account
- PostgreSQL database on Render (or external)

### Deployment Steps

1. **Create a PostgreSQL Database on Render:**
   - Go to Render Dashboard → New → PostgreSQL
   - Note the connection details (host, port, database name, user, password)

2. **Deploy the Web Service:**
   - Go to Render Dashboard → New → Web Service
   - Connect your GitHub repository
   - Select the repository and branch
   - Render will detect the `Dockerfile` automatically
   - Set the following environment variables:
     - `PORT`: 8080 (Render sets this automatically, but you can override)
     - `DB_HOST`: Your PostgreSQL host (e.g., `dpg-xxxxx-a.oregon-postgres.render.com`)
     - `DB_PORT`: 5432
     - `DB_NAME`: Your database name
     - `DB_USER`: Your database user
     - `DB_PASSWORD`: Your database password
     - `DB_URL`: (Optional) Full JDBC URL: `jdbc:postgresql://host:port/dbname`

3. **Alternative: Use render.yaml**
   - Push `render.yaml` to your repository
   - Render will automatically create services based on the configuration
   - Update environment variables in the Render dashboard

### Port Configuration

The application automatically:
- Reads the `PORT` environment variable (provided by Render)
- Configures Tomcat to listen on `0.0.0.0` (required for Render)
- Falls back to port 8080 if `PORT` is not set

### Database Connection

The application supports two ways to configure the database:
1. **Individual variables**: `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER`, `DB_PASSWORD`
2. **Full URL**: `DB_URL` (e.g., `jdbc:postgresql://host:port/dbname`)

If environment variables are set, they override the default `persistence.xml` configuration.

## Notes

- The application uses Hibernate's `update` mode for schema generation
- Initial agent data can be added via `init-db/init.sql`
- All entity relationships are properly configured with cascade operations
- For Render deployment, ensure the database is accessible from the web service

