# Notes on Challenge Completion
I completed [Task 1](#task-1-1) and [Task 2](#task-2-1) as described below. Information on new endpoints  and how to use the additions to the API are [here](#how-to-run). I also added some very basic test cases for the services.

## Design and Implementation Choices Explained
I haven't worked much with Spring Boot before, so I heavily leveraged the existing Employee classes to create the Reporting Structure and Compensation classes. My process was basically copying the Employee classes, removing the parts that were unnecessary for each task, e.g. removing create and update code for Reporting Structure, and adding the minimal necessary changes and logic to accomplish each task. This also helped to maintain consistency and readability throughout the project.

### Task 1
I create a Reporting Structure object with an `employee` field and `numberOfReports` field as asked. This data is not stored in the database; the service creates a new Reporting Structure every time the data is requested, as per the task requirements. To calculate the `numberOfReports`, I wrote a simple function that recursively counts child reporters. 

Because the Employee objects stored in the field `directReports` are initialized with only the `employeeId`, all the other fields in these objects are null. I had to query the Employee table every time to get more information on the child reporters, including their `directReports` field. I think this design is odd because having a list of Employee objects instead of just a list of keys I feel implies that you should be able to get more information from the objects than just the key. A bigger issue though is that I don't think this design follows best practices for one-to-many relationships. I think the child reporters should point to the parents they're reporting to rather than the other way around as it is currently. I didn't implement these changes though in the interest of time and in case there's a good reason for this design that I don't know about.

### Task 2

This was even easier than Task 1. It probably would've been better design for me to add the data as a new field to the Employee table, but since I don't know Spring Boot well and didn't want to touch the existing classes, I created a new Compensation table in the same way the Employee table was created. Compensation also uses employeeId as its primary key and doesn't generate new ones. This means Compensation data can only be added after Employees are created. This makes sense and links Compensation to Employee objects through the shared key.

## How to Run
The application may be executed by running `gradlew bootRun`. Tests may be run by using `gradlew test`.

### Added EndPoints
```
* CREATE
    * HTTP Method: POST 
    * URL: localhost:8080/compensation
    * PAYLOAD: Compensation
    * RESPONSE: Compensation

* READ
    * HTTP Method: GET 
    * URL: localhost:8080/reporting-structure/{id}
    * RESPONSE: ReportingStructure

    * HTTP Method: GET 
    * URL: localhost:8080/compensation/{id}
    * RESPONSE: Compensation

```

### Example Requests and Responses
- Sample Compensation Payload and Response for `POST localhost:8080/compensation`
```
{
    "employeeId" : "16a596ae-edd3-4847-99fe-c4518e82c86f",
    "salary": 60000,
    "effectiveDate": "2024-12-15"
}
```

- Sample Reporting Structure Response for `GET localhost:8080/reporting-structure/16a596ae-edd3-4847-99fe-c4518e82c86f`
```
{
    "employee": {
        "employeeId": "16a596ae-edd3-4847-99fe-c4518e82c86f",
        "firstName": "John",
        "lastName": "Lennon",
        "position": "Development Manager",
        "department": "Engineering",
        "directReports": [
            {
                "employeeId": "b7839309-3348-463b-a7e3-5de1c168beb3",
                "firstName": null,
                "lastName": null,
                "position": null,
                "department": null,
                "directReports": null
            },
            {
                "employeeId": "03aa1462-ffa9-4978-901b-7c001562cf6f",
                "firstName": null,
                "lastName": null,
                "position": null,
                "department": null,
                "directReports": null
            }
        ]
    },
    "numberOfReports": 4
}
```

- Sample Compensation Response for `GET localhost:8080/compensation/16a596ae-edd3-4847-99fe-c4518e82c86f`
```
{
    "employeeId" : "16a596ae-edd3-4847-99fe-c4518e82c86f",
    "salary": 60000,
    "effectiveDate": "2024-12-15"
}
```

# Coding Challenge
## What's Provided
A simple [Spring Boot](https://projects.spring.io/spring-boot/) web application has been created and bootstrapped with data. The application contains 
information about all employees at a company. On application start-up, an in-memory Mongo database is bootstrapped with 
a serialized snapshot of the database. While the application runs, the data may be accessed and mutated in the database 
without impacting the snapshot.

### How to Run
The application may be executed by running `gradlew bootRun`.

*Spring Boot 3 requires Java 17 or higher. This project targets Java 17. If you want to change the targeted Java 
version, you can modify the `sourceCompatibility` variable in the `build.gradle` file.*

### How to Use
The following endpoints are available to use:
```
* CREATE
    * HTTP Method: POST 
    * URL: localhost:8080/employee
    * PAYLOAD: Employee
    * RESPONSE: Employee
* READ
    * HTTP Method: GET 
    * URL: localhost:8080/employee/{id}
    * RESPONSE: Employee
* UPDATE
    * HTTP Method: PUT 
    * URL: localhost:8080/employee/{id}
    * PAYLOAD: Employee
    * RESPONSE: Employee
```

The Employee has a JSON schema of:
```json
{
  "title": "Employee",
  "type": "object",
  "properties": {
    "employeeId": {
      "type": "string"
    },
    "firstName": {
      "type": "string"
    },
    "lastName": {
      "type": "string"
    },
    "position": {
      "type": "string"
    },
    "department": {
      "type": "string"
    },
    "directReports": {
      "type": "array",
      "items": {
        "anyOf": [
          {
            "type": "string"
          },
          {
            "type": "object"
          }
        ]
      }
    }
  }
}
```
For all endpoints that require an `id` in the URL, this is the `employeeId` field.

## What to Implement
This coding challenge was designed to allow for flexibility in the approaches you take. While the requirements are 
minimal, we encourage you to explore various design and implementation strategies to create functional features. Keep in
mind that there are multiple valid ways to solve these tasks. What's important is your ability to justify and articulate
the reasoning behind your design choices. We value your thought process and decision-making skills. Also, If you 
identify any areas in the existing codebase that you believe can be enhanced, feel free to make those improvements.

### Task 1
Create a new type called `ReportingStructure` that has two fields: `employee` and `numberOfReports`.

The field `numberOfReports` should equal the total number of reports under a given employee. The number of reports is 
determined by the number of `directReports` for an employee, all of their distinct reports, and so on. For example,
given the following employee structure:
```
                   John Lennon
                 /             \
         Paul McCartney     Ringo Starr
                            /         \
                       Pete Best    George Harrison
```
The `numberOfReports` for employee John Lennon (`employeeId`: 16a596ae-edd3-4847-99fe-c4518e82c86f) would be equal to 4.

This new type should have a new REST endpoint created for it. This new endpoint should accept an `employeeId` and return
the fully filled out `ReportingStructure` for the specified `employeeId`. The values should be computed on the fly and 
will not be persisted.

### Task 2
Create a new type called `Compensation` to represent an employee's compensation details. A `Compensation` should have at 
minimum these two fields: `salary` and `effectiveDate`. Each `Compensation` should be associated with a specific 
`Employee`. How that association is implemented is up to you.

Create two new REST endpoints to create and read `Compensation` information from the database. These endpoints should 
persist and fetch `Compensation` data for a specific `Employee` using the persistence layer.

## Delivery
Please upload your results to a publicly accessible Git repo. Free ones are provided by GitHub and Bitbucket.
