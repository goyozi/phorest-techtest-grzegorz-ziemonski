## Building the project

Make sure development database is running:
```
docker-compose up
```

Run Gradle build task:
```
./gradlew build
```

## Running the app

### Using an IDE

Run the `main` function in `App.kt`

### Using docker-compose

Build the project via Gradle and start docker-compose using the `app` profile:
```
docker-compose --profile app up --build
```

## API Docs

There is a very simple API documentation provided in the `app.http` file. Most code editors should allow executing requests directly from the file. 

*I wanted to provide a Postman collection but the tool refused to work on my laptop.*
