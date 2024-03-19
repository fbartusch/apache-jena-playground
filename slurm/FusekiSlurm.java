package slurm;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.impl.PropertyImpl;
import org.apache.jena.rdf.model.impl.ResourceImpl;
import org.apache.jena.rdfconnection.RDFConnection;

public class FusekiSlurm {
    
    public static void main(String[] args) {

        // Should it run prolog or epilog?
        boolean runProlog = false;
        boolean runEpilog = false;

        if(Objects.equals("prolog", new String(args[0]))) {
            runProlog = true;
        } else if (Objects.equals("epilog", new String(args[0]))) {
            runEpilog = true;
        }
        
        
        // TODO make this filename an option
        String fileName = "/var/home/slurm/connection.conf";
        Properties prop = new Properties();
        try (FileInputStream fis = new FileInputStream(fileName)) {
            prop.load(fis);
        } catch (FileNotFoundException ex) {
            System.out.println("FileNotFoundException");
        } catch (IOException ex) {
            System.out.println("IOException");
        }

        System.out.println(prop.getProperty("fuseki.server"));
        System.out.println(prop.getProperty("fuseki.port"));
        System.out.println(prop.getProperty("fuseki.dataset"));
        System.out.println(prop.getProperty("fuseki.user"));
        System.out.println(prop.getProperty("fuseki.pw"));


        // Fuseki connection details
        // TODO Don't hard code but read from a file
        String host = prop.getProperty("fuseki.server");
        String port = prop.getProperty("fuseki.port");
        String dataset = prop.getProperty("fuseki.dataset");
        String destination = "http://" + host + ":" + port + "/" + dataset + "/";

        String user = prop.getProperty("fuseki.user");
        String pw = prop.getProperty("fuseki.pw");

        // This model will hold the job information
        Model m = ModelFactory.createDefaultModel();
        String slurmClusterName = System.getenv().get("SLURM_CLUSTER_NAME");
        String slurmJobID = System.getenv().get("SLURM_JOB_ID");

        if(runProlog) {
            String cudaVisibleDevices = System.getenv().get("CUDA_VISIBLE_DEVICES");
            String gpuDeviceOrdinal = System.getenv().get("GPU_DEVICE_ORDINAL");
            String slurmArrayJobID = System.getenv().get("SLURM_ARRAY_JOB_ID ");
            String slurmArrayTaskID = System.getenv().get("SLURM_ARRAY_TASK_ID ");
            String slurmCpusOnNode = System.getenv().get("SLURM_CPUS_ON_NODE ");
            String slurmJobAccount = System.getenv().get("SLURM_JOB_ACCOUNT ");
            String slurmJobComment = System.getenv().get("SLURM_JOB_COMMENT ");
            String slurmJobConstraints = System.getenv().get("SLURM_JOB_CONSTRAINTS ");
            String slurmJobCpusPerNode = System.getenv().get("SLURM_JOB_CPUS_PER_NODE ");
            String slurmJobGpus = System.getenv().get("SLURM_JOB_GPUS ");
            String slurmJobName = System.getenv().get("SLURM_JOB_NAME");
            String slurmJobNodeList = System.getenv().get("SLURM_JOB_NODELIST");
            String slurmNtasks = System.getenv().get("SLURM_NTASKS");
            String slurmJobNumNodes = System.getenv().get("SLURM_JOB_NUM_NODES");
            String slurmJobPartition = System.getenv().get("SLURM_JOB_PARTITION");
            String slurmStartTime = System.getenv().get("SLURM_JOB_START_TIME");
            String slurmJobStderr = System.getenv().get("SLURM_JOB_STDERR");
            String slurmJobStdout = System.getenv().get("SLURM_JOB_STDOUT");
            String slurmJobUser = System.getenv().get("SLURM_JOB_USER");
            String slurmJobWorkDir = System.getenv().get("SLURM_JOB_WORK_DIR");
            String slurmNnodes = System.getenv().get("SLURM_NNODES");
            String slurmTasksPerNode = System.getenv().get("SLURM_TASKS_PER_NODE");

            // Properties used in the knowledge graph
            // TODO use proper Literals and datatypes
            // TODO slurmNodeList are edges to the cluster graph containing the hardware information
            Property hasID = new PropertyImpl("http://example.org/hasID");
            Property hasCudaVisibleDevices = new PropertyImpl("http://example.org/hasCudaVisibleDevices");
            Property hasGpuDeviceOrdinal = new PropertyImpl("http://example.org/hasGpuDeviceOrdinal");
            Property hasArrayJobID = new PropertyImpl("http://example.org/hasArrayJobID");
            Property hasArrayTaskID = new PropertyImpl("http://example.org/hasArrayTaskID");
            Property hasCpusOnNode = new PropertyImpl("http://example.org/hasCpusOnNode");
            Property hasAccount = new PropertyImpl("http://example.org/hasAccount");
            Property hasComment = new PropertyImpl("http://example.org/hasComment");
            Property hasConstraints = new PropertyImpl("http://example.org/hasConstraints");
            Property hasCpusPerNode = new PropertyImpl("http://example.org/hasCpusPerNode");
            Property hasGpus = new PropertyImpl("http://example.org/hasGpus");
            Property hasName = new PropertyImpl("http://example.org/hasName");
            Property hasNodeList = new PropertyImpl("http://example.org/hasNodeList");
            Property hasNtasks = new PropertyImpl("http://example.org/hasNtasks");
            Property hasNumNodes = new PropertyImpl("http://example.org/hasNumNodes");
            Property hasPartition = new PropertyImpl("http://example.org/hasPartition");
            Property hasStartTime = new PropertyImpl("http://example.org/hasStartTime");
            Property hasStderr = new PropertyImpl("http://example.org/hasStderr");
            Property hasStdout = new PropertyImpl("http://example.org/hasStdout");
            Property hasUser = new PropertyImpl("http://example.org/hasUser");
            Property hasWorkDir = new PropertyImpl("http://example.org/hasWorkDir");
            Property hasNnodes = new PropertyImpl("http://example.org/hasNnodes");
            Property hasTasksPerNode = new PropertyImpl("http://example.org/hasTasksPerNode");

            // Qualified Name of the job consists of: "SLURM_CLUSTER_NAME"_"SLURM_JOB_ID"
            String jobQN = slurmClusterName + "_" + slurmJobID;
            Resource job = new ResourceImpl("http://example.org/", jobQN);
            
            // Properties present for every job
            m.add(job, hasID, slurmJobID);
            m.add(job, hasName, slurmJobName);
            m.add(job, hasStartTime, slurmStartTime);
            m.add(job, hasNodeList, slurmJobNodeList);
            m.add(job, hasPartition, slurmJobPartition);
            m.add(job, hasWorkDir, slurmJobWorkDir);
            m.add(job, hasNumNodes, slurmJobNumNodes);
            m.add(job, hasUser, slurmJobUser);

            // These values can be missing ...
            if(slurmJobStderr != null)
                m.add(job, hasStderr, slurmJobStderr);
            if(slurmJobStdout != null)
                m.add(job, hasStdout, slurmJobStdout);
            if(slurmCpusOnNode != null)
                m.add(job, hasCpusOnNode, slurmCpusOnNode);
            if(slurmJobAccount != null)
                m.add(job, hasAccount, slurmJobAccount);
            if(slurmJobComment != null)
                m.add(job, hasComment, slurmJobComment);
            if(slurmJobConstraints != null)
                m.add(job, hasConstraints, slurmJobConstraints);
            if(slurmJobCpusPerNode != null)
                m.add(job, hasCpusPerNode, slurmJobCpusPerNode);
            if(slurmNtasks != null)
                m.add(job, hasNtasks, slurmNtasks);
            if(slurmNnodes != null)
                m.add(job, hasNnodes, slurmNnodes);
            if(slurmTasksPerNode != null)
                m.add(job, hasTasksPerNode, slurmTasksPerNode);

            // Properties of GPU jobs
            if(cudaVisibleDevices != null)
                m.add(job, hasCudaVisibleDevices, cudaVisibleDevices);
            if(gpuDeviceOrdinal != null)
                m.add(job, hasGpuDeviceOrdinal, gpuDeviceOrdinal);
            if(slurmJobGpus != null)
                m.add(job, hasGpus, slurmJobGpus);

            
            // Properties of ArrayJobs
            if(slurmArrayJobID != null)
                m.add(job, hasArrayJobID, slurmArrayJobID);
            if(slurmArrayTaskID != null)
                m.add(job, hasArrayTaskID, slurmArrayTaskID);
        }

        if (runEpilog) {
            System.out.println("Inside runEpilog");
            String slurmJobEndTime = System.getenv().get("SLURM_JOB_END_TIME");
            String slurmJobExitCode = System.getenv().get("SLURM_JOB_EXIT_CODE");
            //String slurmJobExitCode2 = System.getenv().get("SLURM_JOB_EXIT_CODE2");

            // Properties used in the knowledge graph
            // TODO use proper Literals and datatypes
            // TODO slurmNodeList are edges to the cluster graph containing the hardware information
            Property hasEndTime = new PropertyImpl("http://example.org/hasEndTime");
            Property hasJobExitCode = new PropertyImpl("http://example.org/hasExitCode");
            //Property hasJobExitCode2 = new PropertyImpl("http://example.org/hasExitCode2");

            // Qualified Name of the job consists of: "SLURM_CLUSTER_NAME"_"SLURM_JOB_ID"
            String jobQN = slurmClusterName + "_" + slurmJobID;
            Resource job = new ResourceImpl("http://example.org/", jobQN);
            
            // node001
            m.add(job, hasEndTime, slurmJobEndTime);
            m.add(job, hasJobExitCode, slurmJobExitCode);
            //m.add(job, hasJobExitCode2, slurmJobExitCode2);
        }


        // Send data to Fuseki Triple Store
        try ( RDFConnection conn =RDFConnection.connectPW(destination, user, pw) ) {
            // Load (add, append) RDF into a named graph in a dataset
            conn.load("http://example/scheduler", m);
        }

        // Sanity Test: Query job data from the graph
        /*
        try ( RDFConnection conn =RDFConnection.connectPW(destination, user, pw) ) {
            // Load (add, append) RDF into a named graph in a dataset
            RDFDataMgr.write(System.out, conn.fetch("http://example/scheduler"), Lang.TRIG);
        }
        */
    }
}