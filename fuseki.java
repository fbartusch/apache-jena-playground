/* 
 * 
 * Fuseki playground
 * 
 * 
 */
 
import org.apache.jena.graph.Graph;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.query.ResultSetRewindable;
import org.apache.jena.rdfconnection.RDFConnectionFuseki;
import org.apache.jena.rdfconnection.RDFConnectionRemoteBuilder;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFParser;
import org.apache.jena.sparql.core.DatasetGraph;
import org.apache.jena.sparql.core.DatasetGraphFactory;
import org.apache.jena.sparql.core.DatasetImpl;
import org.apache.jena.sparql.graph.GraphFactory;

public class fuseki {
    public static void main(String[] args) {
        // Get fMRI provenance data into Fuseki Triplestore
        // see: https://jena.apache.org/documentation/rdfconnection/
        // see: https://github.com/apache/jena/blob/main/jena-examples/src/main/java/rdfconnection/examples/RDFConnectionExample6.java
        // see: https://stackoverflow.com/questions/55865903/is-there-a-way-to-upload-a-ttl-file-to-fuseki-programatically

        RDFConnectionRemoteBuilder builder = RDFConnectionFuseki.create()
            .destination("http://localhost:3030/NAME/");

        // In this variation, a connection is built each time. 
        try ( RDFConnectionFuseki conn = (RDFConnectionFuseki)builder.build() ) {
            // Load (add, append) RDF into a named graph in a dataset
            conn.load(null, "fmri_provenance.ttl");
        }

        // Read JSON-LD 1.1 File created by ProvOneToolbox
        GraphFactory gf = new GraphFactory();
        Graph graph = gf.createJenaDefaultGraph();

        RDFParser rdfParser = RDFParser.source("fmri_snakemake.jsonld").forceLang(Lang.JSONLD11).build();
        rdfParser.parse(graph);
        
        try ( RDFConnectionFuseki conn = (RDFConnectionFuseki)builder.build() ) {
            //public void load(String graphName, Model model);
            // Load (add, append) RDF into a named graph in a dataset
            conn.load(null, "fmri_provenance.ttl");
        }
            
        // Perform a query on the fMRI provenance data
        // https://jena.apache.org/documentation/rdfconnection/

        // First Query from the Provenance Challenge:
        // Find the process that led to Atlas X Graphic / everything that caused Atlas X Graphic to be as it is. This should tell us the new brain images from which the averaged atlas was generated, the warping performed etc. 

        // The final Atlas x-slice has the URI fmri:slice-x-img

        // Query for the activity that created fmri:slice-x-img in Jena interface
        // PREFIX fmri: <https://example.com/> 
        // PREFIX prov: <http://www.w3.org/ns/prov#>
        // SELECT ?object
        // WHERE {fmri:slice-x-img prov:wasGeneratedBy ?object}

        // Create Query
        final String NL = System.getProperty("line.separator");
        String prolog = "PREFIX fmri: <https://example.com/>" + NL + 
                        "PREFIX prov: <http://www.w3.org/ns/prov#>";
        String queryString = prolog + NL +
            "SELECT ?object WHERE {fmri:slice-x-img prov:wasGeneratedBy ?object}"; 
        Query query = QueryFactory.create(queryString);



        // Perform this Query programmatically
        String endpoint = "http://localhost:3030/NAME/";
        try(QueryExecution qexec = QueryExecution.service(endpoint, query)) {
            // A ResultSet is an iterator - any query solutions returned by .next()
            // are not accessible again.
            // Create a ResultSetRewindable that can be reset to the beginning.
            // Do before first use.
            
            ResultSetRewindable rewindable = qexec.execSelect().rewindable();
            ResultSetFormatter.out(rewindable) ;
            //rewindable.reset() ;
            //ResultSetFormatter.out(rewindable) ;
        }
    }
}
