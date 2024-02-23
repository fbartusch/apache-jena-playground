/* 
 * 
 * Playground for Jena RDF API
 * https://jena.apache.org/tutorials/rdf_api.html
 * 
 * Methods for creating example data
 * 
 */

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.impl.PropertyImpl;
import org.apache.jena.rdf.model.impl.ResourceImpl;


class ExampleData {
    public static void main(String[] args) {
        Model exampleCluster = createCluster();
        //RDFDataMgr.write(System.out, exampleCluster, Lang.TRIG);

        Model exampleJobs1 = createJobs("testUser1", 3);
        //RDFDataMgr.write(System.out, exampleJobs1, Lang.TRIG);

        Model exampleJobs2 = createJobs("testUser2", 4);
        //RDFDataMgr.write(System.out, exampleJobs2, Lang.TRIG);

        Model exampleExecution = createExampleExecution();
        //RDFDataMgr.write(System.out, exampleExecution, Lang.TRIG);
    }

    public static Model createExampleExecution() {
        Model m = ModelFactory.createDefaultModel();
        m.read("example_datasets/fMRI_example_full/fmri_provenance.ttl");
        return m;
    }


    public static Model createCluster() {
        Model m = ModelFactory.createDefaultModel();

        // Job properties
        Property hasNode = new PropertyImpl("http://example.org/hasNode");
        Property numCores = new PropertyImpl("http://example.org/numCores");
        Property hasMemory = new PropertyImpl("http://example.org/hasMemory");
        Property hasGPU = new PropertyImpl("http://example.org/hasGPU");

        Resource cluster = new ResourceImpl("http://example.org/", "exampleCluster");
        Resource node001 = new ResourceImpl("http://example.org/", "node001");
        Resource node002 = new ResourceImpl("http://example.org/", "node002");
        Resource node003 = new ResourceImpl("http://example.org/", "node003");
        
        // node001
        m.add(cluster, hasNode, node001);
        m.add(node001, numCores, "28");
        m.add(node001, hasMemory, "128gb");

        // node002 (has GPU)
        m.add(cluster, hasNode, node002);
        m.add(node002, numCores, "28");
        m.add(node002, hasMemory, "128gb");
        m.add(node001, hasGPU, "NVIDIA K80");

        // node003 (high-mem)
        m.add(cluster, hasNode, node003);
        m.add(node003, numCores, "40");
        m.add(node003, hasMemory, "1024gb");

        return m;
    }


    /**
     * Retern a model that contains a number of jobs by a specific user
     */
    public static Model createJobs(String userName, int numJobs) {
        Model m = ModelFactory.createDefaultModel();

        // Job properties
        Property hasUser = new PropertyImpl("http://example.org/hasUser");
        Property hasExitStatus = new PropertyImpl("http://example.org/hasExitStatus");
        Property hasID = new PropertyImpl("http://example.org/hasID");
        Property usedNode = new PropertyImpl("http://example.org/usedNode");
        Property requestedMemory = new PropertyImpl("http://example.org/requestedMemory");
        Property requestedWalltime = new PropertyImpl("http://example.org/requestedWalltime");
        Property usedMemory = new PropertyImpl("http://example.org/usedMemory");
        Property usedWalltime = new PropertyImpl("http://example.org/usedWalltime");

        // Global Resources (user is the same for all jobs)
        Resource user = new ResourceImpl("http://example.org/", userName);
        Resource node = new ResourceImpl("http://example.org/", "node001");
        
        // Create all jobs
        for(int i=1; i<=numJobs; i++) {
            Resource job = new ResourceImpl("http://example.org/", "job" + String.valueOf(i));
            // Set triples for the current job
            m.add(job, hasUser, user);
            m.add(job, hasExitStatus, "0");
            m.add(job, hasID, String.valueOf(i));
            m.add(job, usedNode, node);
            m.add(job, requestedMemory, "20gb");
            m.add(job, requestedWalltime, "24:00:00");
            m.add(job, usedMemory, "4gb");
            m.add(job, usedWalltime, "3:30:00");
        }

        return m;
    }


}