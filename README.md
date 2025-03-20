LU-Connect - Multithreaded Chat and File Transfer Application

Project Description: LU-Connect is a Java-based multithreaded chat
application that allows multiple users to communicate in real time.
It supports:

  - User authentication
  - Encrypted text messaging
  - File transfer (.docx, .pdf, .jpeg)
  - Concurrent client handling with semaphores and waiting queues
  - The project demonstrates the use of the Producer-Consumer pattern
    to manage client connections and concurrency.

How to run:
  1. Compile: javac -d bin src/common/.java src/client/.java src/server/*.java
  2. Run Server: java -cp bin server.Server
  3. Run Client: java -cp bin client.Client
  4. Run File Sender: java -cp bin client.FileSender <path_to_file>

Author
Studen ID. 39225941
