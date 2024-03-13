package slurm;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdfconnection.RDFConnection;
import org.apache.jena.rdfconnection.RDFConnectionFuseki;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;

public class PrologSlurm {
    
    public static void main(String[] args) {

        // This model will hold the job information
        Model m = ModelFactory.createDefaultModel();

        // Fuseki connection details
        String port = "3030";
        String dataset = "ds";
        String destination = "http://localhost:" + port + "/" + dataset + "/";
        String graphName = "http://graph/scheduler";

        try ( RDFConnection conn = RDFConnectionFuseki.connect(destination) ) {
            // Load (add, append) RDF into a named graph in a dataset
            conn.load(graphName, m);
        }

        // Sanity Test: Query job data from the graph
        try ( RDFConnection conn =RDFConnection.connectPW(destination, "user1", "pw2") ) {
            // Load (add, append) RDF into a named graph in a dataset
            RDFDataMgr.write(System.out, conn.fetch(graphName), Lang.TRIG);
        }
    }
}