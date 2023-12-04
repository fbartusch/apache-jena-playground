# Apache Jena and Fuseki Playground

This repository serves as playground for checking out Apache Jena and Apache Fuseki.
The repository provides

* Dockerfiles for running an Apache Fuseki server
* Dockerfiles for Apache Jena RDF I/O (RIOT)
* Example data
* Java code showing how to use Apache Jena and how to interact with Apache Fuseki

## Setup

1. Clone this repository

2. Install Docker and Docker Compose

    ```
    # Install Docker Compose
    sudo apt-get update
    sudo apt-get install docker-compose-plugin
    docker compose version
    ```

3. Build Apache Fuseki Docker container

    ```
    cd docker/jena-fuseki-docker-4.9.0
    sudo docker compose build --build-arg JENA_VERSION=4.9.0
    # Test the build
    # Start Fuseki with an in-memory, updatable dataset at http://host:3030/ds
    sudo docker compose run --rm --service-ports fuseki --mem /ds
    ```

    For more information how to use the container, check:
    https://jena.apache.org/documentation/fuseki2/fuseki-docker.html

4. Build Apache Jena Docker container

    This Docker container provides Jena binaries for interacting with Fuseki and working with RDF files.

    ```
    cd docker/jena-docker-4.9.0
    sudo docker compose build
    # Test the build
    sudo docker run jena:latest sparql
    ```

## Resources

* [Website](https://jena.apache.org/)
* [Tutorials and Documentation](https://jena.apache.org/getting_started/index.html)

## Working with RDF serialization formats

Jena provides tools for working with several RDF serialization formats:
* `riot`: parse, guessing the syntax from the file extension
* special parsers for particular languages (e.g. `turtle` for Turtle)
An overview can be found in the [Jena documentation](https://jena.apache.org/documentation/io/#command-line-tools)

### Validate

```
sudo docker run -v \
  -it \
  -v "$(pwd)"/example_datasets:/input \
  jena:latest \
  riot --validate /input/fmri_snakemake.jsonld
```


## Installation without Docker image

### Ubuntu 20.04

#### Apache Jena

```
# Install Java
$ sudo apt install openjdk-17-jre-headless
$ export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64/
$ java --version
openjdk 17.0.8 2023-07-18

# Download Maven binaries
$ wget https://dlcdn.apache.org/maven/maven-3/3.9.4/binaries/apache-maven-3.9.4-bin.tar.gz
$ tar xvf apache-maven-3.9.4-bin.tar.gz
$ sudo mv apache-maven-3.9.4 /opt/

# Add to ~/.bashrc
M2_HOME='/opt/apache-maven-3.9.4'
PATH="$M2_HOME/bin:$PATH"

# Verify
$ mvn -version
Apache Maven 3.9.4 (dfbb324ad4a7c8fb0bf182e6d91b0ae20e3d2dd9)
Maven home: /opt/apache-maven-3.9.4
Java version: 17.0.8, vendor: Private Build, runtime: /usr/lib/jvm/java-17-openjdk-amd64

# Download Jena and Fuseki
cd /opt
wget https://dlcdn.apache.org/jena/binaries/apache-jena-fuseki-4.9.0.tar.gz
wget https://dlcdn.apache.org/jena/binaries/apache-jena-4.9.0.tar.gz
tar xzf apache-jena-fuseki-4.9.0.tar.gz
tar xzf apache-jena-4.9.0.tar.gz
```

#### Apache Fuseki

```
# The binaries for the server were already downloaded in the Jena section
export PATH=/opt/apache-jena-4.9.0/bin/:/opt/apache-jena-fuseki-4.9.0/bin:$PATH

# Start the server
cd /opt/apache-jena-fuseki-4.9.0
sudo ./fuseki-server --mem /NAME

# The server UI is available here:
http://localhost:3030/#/



wget https://repo1.maven.org/maven2/org/apache/jena/jena-fuseki-server/4.9.0/jena-fuseki-server-4.9.0.jar
tar xzf jena-fuseki-server-4.9.0.jar


https://www.bobdc.com/blog/jenagems/

```

#### Adding Binaries to PATH

These directories contain among other things the Apache Fuseki executable and `sparql`.

```
export PATH=/opt/apache-jena-4.9.0/bin/:/opt/apache-jena-fuseki-4.9.0/bin:$PATH
```

## SPARQL Queries on RDF

SPARQL queries can be performed from the command line:

```
# Example: https://jena.apache.org/tutorials/sparql_query1.html
sparql --data=vc-db-1.rdf --query=q1.rq

--------------------------------
| x                            |
================================
| <http://somewhere/JohnSmith> |
--------------------------------
```