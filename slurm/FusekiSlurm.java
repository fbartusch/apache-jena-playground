package slurm;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

import org.apache.commons.cli.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.impl.PropertyImpl;
import org.apache.jena.rdf.model.impl.ResourceImpl;
import org.apache.jena.rdfconnection.RDFConnection;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;

public class FusekiSlurm {
    
    public static void main(String[] args) {

        Options options = new Options();

        //define options
        Option prologOption = new Option(null, "prolog", false, "Run as Slurm prolog.");
        Option epilogOption = new Option(null, "epilog", false, "Run as Slurm epilog.");
        Option configOption = new Option("c", "config", true, "Configuration file with Fuseki connection details. Default: /var/home/slurm/connection.conf");
        Option hostnamesOption = new Option(null, "hostnames", true, "Hostnames of nodes the job runs on. Mandatory if used with --prolog.");
        options.addOption(prologOption);
        options.addOption(epilogOption);
        options.addOption(configOption);
        options.addOption(hostnamesOption);

        // define parser
        CommandLine cmd;
        CommandLineParser parser = new DefaultParser();
        HelpFormatter helper = new HelpFormatter();

        boolean runProlog = false;
        boolean runEpilog = false;
        String configPath = "";
        String hostnames = "";

        try {
            cmd = parser.parse(options, args);
            runProlog = cmd.hasOption("prolog");
            runEpilog = cmd.hasOption("epilog");
            
            if(runProlog && runEpilog) {
                System.out.println("Only use one of --prolog or --epilog");
                System.exit(0);
            }

            if(!(runProlog || runEpilog)) {
                System.out.println("Set either --prolog or --epilog");
                System.exit(0);
            }

            configPath = cmd.getOptionValue("config", "/var/home/slurm/connection.conf");

            if(runProlog) {
                hostnames = cmd.getOptionValue("hostnames");
            }

        } catch (ParseException e) {
            System.out.println(e.getMessage());
            helper.printHelp("Usage:", options);
            System.exit(0);
        }
        
        Properties prop = new Properties();
        try (FileInputStream fis = new FileInputStream(configPath)) {
            prop.load(fis);
        } catch (FileNotFoundException ex) {
            System.out.println("FileNotFoundException");
        } catch (IOException ex) {
            System.out.println("IOException");
        }

        // Fuseki connection details
        String protocol = prop.getProperty("fuseki.protocol");
        String host = prop.getProperty("fuseki.server");
        String port = prop.getProperty("fuseki.port");
        String dataset = prop.getProperty("fuseki.dataset");
        String endpoint = prop.getProperty("fuseki.endpoint");
        String destination = protocol + "://" + host + ":" + port + "/" + dataset + "/" + endpoint;

        String user = prop.getProperty("fuseki.user");
        String pw = prop.getProperty("fuseki.pw");

        // Parse hostnames
        System.out.println("Hostnames:" + hostnames);

        // Get list of hostnames
        String[] hostnameValues = hostnames.split(",");

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
            Property executedOn = new PropertyImpl("http://example.org/executedOn");

              // Qualified Name of the job consists of: "SLURM_CLUSTER_NAME"_"SLURM_JOB_ID"
            String jobQN = slurmClusterName + "_" + slurmJobID;
            Resource job = new ResourceImpl("http://example.org/", jobQN);
            
            // Properties present for every job
            //TODO: User as RDFNode, not as String
            m.add(job, hasID, slurmJobID);
            m.add(job, hasName, slurmJobName);
            m.add(job, hasStartTime, slurmStartTime);
            m.add(job, hasPartition, slurmJobPartition);
            m.add(job, hasWorkDir, slurmJobWorkDir);
            m.add(job, hasNumNodes, slurmJobNumNodes);
            m.add(job, hasUser, slurmJobUser);
            m.add(job, hasNodeList, slurmJobNodeList);

            // Iterate over the values and print them
            for (String hostname : hostnameValues) {
                m.add(job, executedOn, new ResourceImpl("http://example.org/", hostname));
            }

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
            String slurmJobEndTime = System.getenv().get("SLURM_JOB_END_TIME");
            String slurmJobExitCode = System.getenv().get("SLURM_JOB_EXIT_CODE");
            //String slurmJobExitCode2 = System.getenv().get("SLURM_JOB_EXIT_CODE2");

            // Properties used in the knowledge graph
            // TODO use proper Literals and datatypes
            Property hasEndTime = new PropertyImpl("http://example.org/hasEndTime");
            Property hasJobExitCode = new PropertyImpl("http://example.org/hasExitCode");

            // Qualified Name of the job consists of: "SLURM_CLUSTER_NAME"_"SLURM_JOB_ID"
            String jobQN = slurmClusterName + "_" + slurmJobID;
            Resource job = new ResourceImpl("http://example.org/", jobQN);
            
            // node001
            m.add(job, hasEndTime, slurmJobEndTime);
            m.add(job, hasJobExitCode, slurmJobExitCode);
        }
        // Send data to Fuseki Triple Store
        try ( RDFConnection conn = RDFConnection.connectPW(destination, user, pw) ) {
            // Load (add, append) RDF into a named graph in a dataset
            System.out.println("test");
            conn.load("http://example/scheduler", m);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace(System.out);
            System.out.println("Write model to disk at /var/home/slurm/fuseki_failed/");

            String outputFile = "";
            if (runProlog) {
                outputFile = "/var/home/slurm/fuseki_failed/" + slurmJobID + "_prolog.ttl";
            } else if (runEpilog) {
                outputFile = "/var/home/slurm/fuseki_failed/" + slurmJobID + "_epilog.ttl";
            }
            try (OutputStream out = new FileOutputStream(outputFile)) {
                RDFDataMgr.write(out, m, Lang.TURTLE);
            } catch (Exception ee) {
                System.out.println(ee.getMessage());
            }
        }
    }
}