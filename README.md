# YSB_sample

# About

The scripts within this repository allow one to set up a Kafka producer, that will generate streaming data for your streaming applications. The repository also contains a sample Spark program for the Yahoo Streaming Benchmark.


## Dependencies
openjdk 11
sbt
virtualenv

# Setup

The setup script downloads, Kafka, Zookeeper, and Cluster Manager for Kafka and configures them for a local deployment. The run script would run the above programs, and keep them running in the background.

The Cluster Manager for Apache Kafka(CMAK) provides a graphical interface to create clusters, add topics and partitions etc.

Run the script as follows:

`./setup.sh`

# Running the producer

### Starting Kafka, Zookeeper and CMAK

Once the setup is done, start the processes by executing the *[start.sh](http://start.sh)* in separate terminal instances and in order as shown below:

`./start.sh START_ZOOKEEPER` 

`./start.sh START_KAFKA` 

`./start.sh START_CMAK` 

This runs the Kafka, Zookeeper and CMAK in the background.

## Creating a cluster and adding a topic

Creating a cluster and adding topics can be done using the GUI provided by CMAK, without using any scripts. The web interface is available at `[localhost:8080](http://localhost:8080)` .

# Spark program

Open the /YSB_sample folder within the repository in IntelliJ Idea and run the following commands through maven:

```jsx
maven clean install
```

Build and run the project to start the Spark program.

## References

- [https://sparkbyexamples.com/spark/spark-setup-run-with-scala-intellij/](https://sparkbyexamples.com/spark/spark-setup-run-with-scala-intellij/)
