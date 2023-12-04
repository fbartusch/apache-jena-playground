/* 
 * 
 * Playground for Jena RDF API
 * https://jena.apache.org/tutorials/rdf_api.html
 *  
 */



 
import java.io.InputStream;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.vocabulary.VCARD;
 
class RDF {
    public static void main(String[] args)
    {
        // some definitions
        final String personURI    = "http://somewhere/JohnSmith";
        final String givenName    = "John";
        final String familyName   = "Smith";
        final String fullName     = givenName + " " + familyName;

        // create an empty Model
        Model model = ModelFactory.createDefaultModel();

        // create the resource
        Resource johnSmith =
            model.createResource(personURI)
                 .addProperty(VCARD.FN, fullName)
                 .addProperty(VCARD.N,
                              model.createResource()
                                   .addProperty(VCARD.Given, givenName)
                                   .addProperty(VCARD.Family, familyName));
        
        // list all statements in the model
        StmtIterator iter = model.listStatements();

        // print out the predicate, subject and object of each statement
        while (iter.hasNext()) {
            Statement stmt = iter.nextStatement();
            Resource subject = stmt.getSubject();
            Property predicate = stmt.getPredicate();
            RDFNode object = stmt.getObject();

            System.out.print(subject.toString());
            System.out.print(" " + predicate.toString() + " ");
            if (object instanceof Resource) {
                System.out.print(object.toString());
            } else {
                // object is a literal
                System.out.print(" \"" + object.toString() + "\"");
            }

            System.out.println(" .");
        }
        System.out.println("");
        System.out.println("");
        model.write(System.out);


        // now write the model in a pretty form
        RDFDataMgr.write(System.out, model, Lang.NTRIPLES);


        final String inputFileName = "vc-db-1.rdf";

        // create an empty model
        Model model2 = ModelFactory.createDefaultModel();

        // use the RDFDataMgr to find the input file
        InputStream in = RDFDataMgr.open( inputFileName );
        if (in == null) {
            throw new IllegalArgumentException("File: " + inputFileName + " not found");
        }

        // read the RDF/XML file
        model2.read(in, null);

        // write it to standard out
        System.out.println("");
        System.out.println("");
        model2.write(System.out);

        // Next: Reading RDF: https://jena.apache.org/tutorials/rdf_api.html
        Model m = ModelFactory.createDefaultModel();
        String nsA = "http://somewhere/else#";
        String nsB = "http://nowhere/else#";
        Resource root = m.createProperty(nsA + "root");
        Property P = m.createProperty(nsA + "P");
        Property Q = m.createProperty(nsB + "Q");
        Resource x = m.createResource(nsA + "x");
        Resource y = m.createResource(nsA + "y");
        Resource z = m.createResource(nsA + "z");
        m.add(root, P, x).add(root, P, y).add(y, Q, z);
        System.out.println();
        System.out.println( "# -- no special prefixes defined" );
        m.write( System.out );
        System.out.println( "# -- nsA defined" );
        m.setNsPrefix( "nsA", nsA );
        m.write( System.out );
        System.out.println( "# -- nsA and cat defined" );
        m.setNsPrefix( "cat", nsB );
        m.write( System.out );



        Model m2 = ModelFactory.createDefaultModel();   
        m2.read( "file:./fragment.rdf" );
        m2.write( System.out );


        // retrieve the John Smith vcard resource from the model
        final String johnSmithURI = personURI;
        Resource vcard = model.getResource(johnSmithURI);

        // retrieve the value of the N property
        Resource name = (Resource) vcard.getProperty(VCARD.N).getResource();
        String fullName2 = vcard.getProperty(VCARD.FN).getString();
        System.out.println("");
        System.out.println(fullName2);

        // add two nickname properties to vcard
        vcard.addProperty(VCARD.NICKNAME, "Smithy")
             .addProperty(VCARD.NICKNAME, "Adman");

        String nickName = vcard.getProperty(VCARD.NICKNAME).getString();
        System.out.println(nickName);

        // set up tth eoutput
        System.out.println("The nicknames of \"" + fullName + "\" + are:");
        StmtIterator iter2 = vcard.listProperties(VCARD.NICKNAME);
        while (iter2.hasNext()) {
            System.out.println("    " + iter2.nextStatement().getObject().toString());
        }

        // List vcards
        ResIterator iter3 = model2.listSubjectsWithProperty(VCARD.FN);
        if (iter3.hasNext()) {
            System.out.println("The database contains vcards for:");
            while (iter3.hasNext()) {
                System.out.println("  " + iter3.nextResource()
                                               .getProperty(VCARD.FN)
                                               .getString());
            }
        } else {
            System.out.println("No vcards were found in the database");
        }


        // Check https://github.com/owlcs/owlapi for OWL 2 ontologies
        // Jena, OWL1, OWL2 etc: https://lists.apache.org/thread/ztfvvz55oz3xmddtrpkfowtsgo9czg5n
        // https://github.com/apache/jena/issues/1968#issuecomment-1652193209
    }
}