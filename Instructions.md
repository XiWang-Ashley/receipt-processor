# Receipt Processor Application

## Prerequisites

- Docker

## Building and Running the Application

1. **Build the Docker image**:

    ```sh
    docker build -t receipt-processor .
    ```

2. **Run the Docker container**:

    ```sh
    docker run -p 8080:8080 receipt-processor
    ```

3. **Access the application**:

   Open your web browser and navigate to `http://localhost:8080`.

## Endpoints

- **Process Receipts**: `POST /receipts/process`
- **Get Points**: `GET /receipts/{id}/points`

Use a tool like Postman to test the endpoints.

## Testing

To run the tests locally (outside of Docker), you need to have Java and Maven installed. Use the following command to run the tests:

```sh
./mvnw test
