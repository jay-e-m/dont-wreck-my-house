# Don't Wreck My House!
"Don't Wreck My House is similar to Airbnb. A guest chooses a place to stay for a specific date range. If the host location is available, it may be reserved by the guest. Reserved locations are not available to other guests during reserved dates."

## To-Do List

### Setup
- [ ] Create a new Java project with Maven
- [ ] Create "**data**" directory at the same level as src, add .csv files to it
  -  .csv files from the .zip provided in LMS
- [ ] Import dependencies (Spring & JUnit)
- [ ] Create packages in adherence with the Dev10 MVC 'guideline'
  - *data, domain, models, ui*
- [ ] Add a "**resources**" root directory at the same level as java (under main)
  - Create an "**app.properties**" file inside of resources
    - Add path information to app.properties?
- [ ] Create packages in the test directory, under src > java. 
  - "**data**", "**domain**"

### Project Framework Implementation (no code)
- [ ] Create classes (models) for `Hosts`, `Guests`, and `Reservations` (**pkg:** models)
- [ ] Create data repo (classes/interfaces) for corresponding models (**pkg:** data)
- [ ] Create service classes for fetching data from repos (**pkg:** domain)
- [ ] Create an exception class (**pkg:** data)
- [ ] Create a response and a result class for handling service responses (**pkg:** domain)
  - **Response** stores and sends error messages depending on the state of a "isSuccessful" boolean
  - **Result** extends response, returns data of the operation if it was successful
- [ ] Create tests and doubles for each of the models in **test > ... > data**, and service tests in **test > ... > domain** packages
- [ ] Create classes in UI package: `ConsoleIO`, `Controller`, `View`, and possibly a `Menu` Enum
- [ ] **Implement Spring Annotation DI on appropriate components**
  - Typically UI, Domain, and Data package components
  - No interfaces

### Data Administration
[TEST AS YOU GO!]

- [ ] Implement the classes used for our data structures (models), `Hosts`, `Guests`, & `Reservations`
  - Constructors (with overload)
  - Getters
    - Validation for the room total value in the getter?
  - Setters
  - Override equals & hash, or toString if applicable
- [ ] Implement the classes used to access Data (repositories)
  - Utilize java.io.reader(*) methods such as BufferedReader, PrintWriter, FileReader
  - CRUD methods live here
  - Utilize the interface to call the methods
  - Consider public vs. private and Override tagging
  - Place the filepath 'variable' from app.properties inside the Repository declaration with a @Value tag
  - Serialize and deserialize into/out of our data source (Use an array for storage?)

### Service Layer Implementation
[TEST AS YOU GO!]

- [ ] Implement the service classes (**pkg:** domain)
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
[TEST AS YOU GO!]

- Handle exceptions and edge cases, such as invalid input or issues with data
- [ ] Implement `MenuOptions` enum
- [ ] Implement the UI classes
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

