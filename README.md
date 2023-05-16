# Don't Wreck My House!
"Don't Wreck My House is similar to Airbnb. A guest chooses a place to stay for a specific date range. If the host location is available, it may be reserved by the guest. Reserved locations are not available to other guests during reserved dates."

## To-Do List

### Setup
- [ ] Create a new Java project with Maven (t: 5 minutes)
- [ ] Create "**data**" directory at the same level as src, add .csv files to it (t: 5 minutes)
  -  .csv files from the .zip provided in LMS 
- [ ] Import dependencies (Spring & JUnit) (t: 5 minutes)
- [ ] Create packages in adherence with the Dev10 MVC 'guideline' (t: 5 minutes)
  - *data, domain, models, ui*
- [ ] Add a "**resources**" root directory at the same level as java (under main) (t: 5 minutes)
  - Create an "**app.properties**" file inside of resources
    - Add path information to app.properties?
- [ ] Create packages in the test directory, under src > java. (t: 5 minutes)
  - "**data**", "**domain**"

### Project Framework Implementation (no code)
- [ ] Create classes (models) for `Hosts`, `Guests`, and `Reservations` (**pkg:** models) (t: 5 minutes)
- [ ] Create data repo (classes/interfaces) for corresponding models (**pkg:** data) (t: 10 minutes)
- [ ] Create service classes for fetching data from repos (**pkg:** domain) (t: 5 minutes)
- [ ] Create an exception class (**pkg:** data) (t: 5 minutes)
- [ ] Create a response and a result class for handling service responses (**pkg:** domain) (t: 5 minutes)
  - **Response** stores and sends error messages depending on the state of a "isSuccessful" boolean
  - **Result** extends response, returns data of the operation if it was successful
- [ ] Create tests and doubles for each of the models in **test > ... > data**, and service tests in **test > ... > domain** packages (t: 5 minutes)
- [ ] Create classes in UI package: `ConsoleIO`, `Controller`, `View`, and possibly a `Menu` Enum (t: 5 minutes)
- [ ] **Implement Spring Annotation DI on appropriate components** (t: 10 minutes)
  - Typically UI, Domain, and Data package components
  - No interfaces

### Data Administration
[TEST AS YOU GO!] (t: 4 hours)

- [ ] Implement the classes used for our data structures (models), `Hosts`, `Guests`, & `Reservations`
- [ ] Model Implementation (NEXT SECTION)
  - Constructors (with overload)
  - Getters
    - Validation for the room total value in the getter?
  - Setters
  - Override equals & hash, or toString if applicable
- [ ] Implement the classes used to access Data (repositories) (t: 2 hours)
  - Utilize java.io.reader(*) methods such as BufferedReader, PrintWriter, FileReader
  - CRUD methods live here
  - Utilize the interface to call the methods
  - Consider public vs. private and Override tagging
  - Place the filepath 'variable' from app.properties inside the Repository declaration with a @Value tag
  - Serialize and deserialize into/out of our data source (Use an array for storage?)

### Models Implementation
- [ ] `Host` (t: 30 minutes)
  - String id
  - String name
  - String email
  - String phoneNumber
  - String address
  - String city
  - String state
  - String postalCode
  - BigDecimal standardRate
  - BigDecimal weekendRate
- [ ] `Guest` (t: 30 minutes)
  - String id
  - String firstName
  - String lastName
  - String email
  - String phoneNumber
  - String state
- [ ] `Reservation` (t: 2 hours)
  - String id
  - LocalDate startDate
  - LocalDate endDate
    - long getWeekendDays()
    - boolean isOverlapping()
    - boolean isInFuture()
  - Guest guest
  - Host host
  - BigDecimal total
    - BigDecimal getTotalCost()

### Service Layer Implementation
[TEST AS YOU GO!] (t: 4 hours)

- [ ] Implement the service classes (**pkg:** domain)  (t: 4 hours)
    - Create methods to fetch data from the repositories
      - findAll, findByHostID, createReservation (call from repos)
      - VALIDATE! Reservation dates CAN NOT overlap
    - Implement validation rules
        - Ensure all necessary fields are present and correctly formatted
        - Add business logic
    - Use the `Result` class for methods that require error handling
        - In case of error, create a new `Result` instance and store the error
        - If the operation is successful, create a new `Result` and store
    - Add methods for complex operations that involve multiple repositories

### UI Implementation
[TEST AS YOU GO!] (t: 4 hours)

- Handle exceptions and edge cases, such as invalid input or issues with data
- [ ] Implement `MenuOptions` enum (t: 10 minutes)
- [ ] Implement the UI classes (t: 4 hours)
    - **ConsoleIO**: Responsible for all input/output in the console.
        - Create methods for printing to the console, reading string, reading integers, reading dates, etc.
    - **View**: Prints intuitive and user-readable text to the console
        - Create methods for displaying a menu, displaying errors, displaying reservations, etc.
    - **Controller**: *Controls* workflow of the application
        - Create methods for each menu option
        - Instantiate `View` and the `Service` classes here
        - Implement the logic for handling user choices
          - While loop for main menu
          - Interact with Service layer to perform operations
- Handle exceptions and edge cases, such as invalid input or issues with data

