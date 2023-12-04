FROM ubuntu:22.04

# Install Java
RUN apt-get update && apt-get install -y \
    openjdk-17-jre-headless \
    wget \
    ca-certificates \
    && rm -rf /var/lib/apt/lists/*
ENV JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64/

# Install Maven binaries
WORKDIR /opt
RUN wget http://dlcdn.apache.org/maven/maven-3/3.9.4/binaries/apache-maven-3.9.4-bin.tar.gz
RUN tar xvf apache-maven-3.9.4-bin.tar.gz && \
    rm apache-maven-3.9.4-bin.tar.gz
ENV M2_HOME='/opt/apache-maven-3.9.4'
ENV PATH="$M2_HOME/bin:$PATH"

# Download Apache Jena
RUN wget https://archive.apache.org/dist/jena/binaries/apache-jena-4.9.0.tar.gz && \
    tar xvf apache-jena-4.9.0.tar.gz && \
    rm apache-jena-4.9.0.tar.gz
ENV PATH=/opt/apache-jena-4.9.0/bin/:$PATH

RUN mkdir /input
WORKDIR /input

# The default command is to run a sparql query
CMD ["sparql"]