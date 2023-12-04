# Inference Examples

The Quijote example is taken from:

> Muñoz-Fernández, J. F. (2016, December 30). Configuring Apache Jena Fuseki 2.4.1 inference and reasoning support using SPARQL 1.1: Jena inference rules, RDFS Entailment Regimes and OWL reasoning. Retrieved September 13, 2023, from https://github.com/jfmunozf/Jena-Fuseki-Reasoner-Inference/wiki/Configuring-Apache-Jena-Fuseki-2.4.1-inference-and-reasoning-support-using-SPARQL-1.1:-Jena-inference-rules,-RDFS-Entailment-Regimes-and-OWL-reasoning

The fMRI workflow example:



# Setup Apache Fuseki

The principles of Apache Fuseki configuration is explained [here](https://jena.apache.org/documentation/fuseki2/fuseki-configuration.html)



Copy files:

```
sudo cp model.ttl data.ttl /opt/apache-jena-fuseki-4.9.0/run/databases
sudo cp ElQuijote.ttl /opt/apache-jena-fuseki-4.9.0/run/configuration
```

Start Apache Fuseki:

```
(base) felix@kfurt:/opt/apache-jena-fuseki-4.9.0$ sudo ./fuseki-server
06:43:04 INFO  Server          :: Apache Jena Fuseki 4.9.0
06:43:05 INFO  Config          :: FUSEKI_HOME=/opt/apache-jena-fuseki-4.9.0/.
06:43:05 INFO  Config          :: FUSEKI_BASE=/opt/apache-jena-fuseki-4.9.0/run
06:43:05 INFO  Config          :: Shiro file: file:///opt/apache-jena-fuseki-4.9.0/run/shiro.ini
06:43:05 INFO  Config          :: Template file: templates/config-mem
06:43:05 INFO  Config          :: Load configuration: file:///opt/apache-jena-fuseki-4.9.0/run/configurat
ion/ElQuijote.ttl
06:43:05 INFO  Server          :: Database: in-memory
06:43:05 INFO  Server          :: Path = /ElQuijote
06:43:05 INFO  Server          :: Path = /NAME
06:43:05 INFO  Server          ::   Memory: 4.0 GiB
06:43:05 INFO  Server          ::   Java:   17.0.8.1
06:43:05 INFO  Server          ::   OS:     Linux 5.15.0-83-generic amd64
06:43:05 INFO  Server          ::   PID:    20612
06:43:05 INFO  Server          :: Started 2023/09/13 06:43:05 CEST on port 3030
```

## Check if reasoning works:

### #knows Symmetry

```
PREFIX ns:<http://www.example.org/ns#>

SELECT *
WHERE {
  ?s ns:knows ?o .
}

```

| s | o |
|---|---|
| <http://www.example.org/ns#Don_Quijote> | <http://www.example.org/ns#Sancho> |
| <http://www.example.org/ns#Sancho> | <http://www.example.org/ns#Don_Quijote> | 


### Person defined via UnionOf

```
PREFIX ns:<http://www.example.org/ns#>
PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>

SELECT *
WHERE {
	?s rdf:type ns:Person .
}
```

| s |
|---|
| <http://www.example.org/ns#Don_Quijote> |
| <http://www.example.org/ns#Don_Quijote> |
| <http://www.example.org/ns#Don_Quijote> |