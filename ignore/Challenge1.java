

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;

// Implementation of Challenge1 from the book:
// "Semantic Web for the Working Ontologist" by Dean Alemang and Jim Hendler
public class Challenge1 {

    // The mfg  (manuracturing) namespace
    public String mfgNS = "https://example.com/mfg#";

    // The model representing tabular data presented in Challenge 1
    public Model m;

    public Challenge1() {

        // create an empty Model and set Namespace
        this.m = ModelFactory.createDefaultModel();
        this.m.setNsPrefix("mfg", this.mfgNS);

        // The resources in the model are products of the manufacturing company
        Resource p1 = m.createResource(mfgNS + "Product1");
        Resource p2 = m.createResource(mfgNS + "Product2");
        Resource p3 = m.createResource(mfgNS + "Product3");
        Resource p4 = m.createResource(mfgNS + "Product4");
        Resource p5 = m.createResource(mfgNS + "Product5");
        Resource p6 = m.createResource(mfgNS + "Product6");
        Resource p7 = m.createResource(mfgNS + "Product7");
        Resource p8 = m.createResource(mfgNS + "Product8");

        // Properties in the mode.
        Property prodID = m.createProperty(mfgNS + "Product_ID");
        Property prodModelNumber = m.createProperty(mfgNS + "Product_ModelNo");
        Property prodDivision = m.createProperty(mfgNS + "Product_Division");
        Property prodProdLine = m.createProperty(mfgNS + "Product_ProductLine");
        Property prodManufactureLocation = m.createProperty(mfgNS + "Product_Manuracture_Location");
        Property prodSKU = m.createProperty(mfgNS + "Product_SKU");
        Property prodAvailable = m.createProperty(mfgNS + "Product_Available");

        // Add all the relations
        p1.addProperty(prodID, "1")
          .addProperty(prodAvailable, "23")
          .addProperty(prodDivision, "Manufacturing Support")
          .addProperty(prodManufactureLocation, "Sacramento")
          .addProperty(prodModelNumber, "ZX-3")
          .addProperty(prodProdLine, "Paper Machine")
          .addProperty(prodSKU, "FB3524");

        p2.addProperty(prodID, "2")
          .addProperty(prodAvailable, "4")
          .addProperty(prodDivision, "Manufacturing Support")
          .addProperty(prodManufactureLocation, "Sacramento")
          .addProperty(prodModelNumber, "ZX-3P")
          .addProperty(prodProdLine, "Paper Machine")
          .addProperty(prodSKU, "KD5243");  
        
        p3.addProperty(prodID, "3")
          .addProperty(prodAvailable, "34")
          .addProperty(prodDivision, "Manufacturing Support")
          .addProperty(prodManufactureLocation, "Sacramento")
          .addProperty(prodModelNumber, "ZX-3S")
          .addProperty(prodProdLine, "Paper Machine")
          .addProperty(prodSKU, "IL4028");
        
        p4.addProperty(prodID, "4")
          .addProperty(prodAvailable, "23")
          .addProperty(prodDivision, "Control Engineering")
          .addProperty(prodManufactureLocation, "Elizabeth")
          .addProperty(prodModelNumber, "B-1430")
          .addProperty(prodProdLine, "Feedback Line")
          .addProperty(prodSKU, "KS4520");

        p5.addProperty(prodID, "5")
          .addProperty(prodAvailable, "14")
          .addProperty(prodDivision, "Control Engineering")
          .addProperty(prodManufactureLocation, "Elizabeth")
          .addProperty(prodModelNumber, "B-1430X")
          .addProperty(prodProdLine, "Feedback Line")
          .addProperty(prodSKU, "CL5934");

        p6.addProperty(prodID, "6")
          .addProperty(prodAvailable, "0")
          .addProperty(prodDivision, "Control Engineering")
          .addProperty(prodManufactureLocation, "Seoul")
          .addProperty(prodModelNumber, "B-1431")
          .addProperty(prodProdLine, "Active Sensor")
          .addProperty(prodSKU, "KK3945");

        p7.addProperty(prodID, "7")
          .addProperty(prodAvailable, "100")
          .addProperty(prodDivision, "Accessories")
          .addProperty(prodManufactureLocation, "Hong Kong")
          .addProperty(prodModelNumber, "DBB-12")
          .addProperty(prodProdLine, "Monitor")
          .addProperty(prodSKU, "ND5520");

        p8.addProperty(prodID, "8")
          .addProperty(prodAvailable, "4")
          .addProperty(prodDivision, "Safety")
          .addProperty(prodManufactureLocation, "Cleveland")
          .addProperty(prodModelNumber, "SP-1234")
          .addProperty(prodProdLine, "Safety valve")
          .addProperty(prodSKU, "HI4554");

        p8.addProperty(prodID, "9")
          .addProperty(prodAvailable, "14")
          .addProperty(prodDivision, "Safety")
          .addProperty(prodManufactureLocation, "Cleveland")
          .addProperty(prodModelNumber, "SPX-1234")
          .addProperty(prodProdLine, "Safety valve")
          .addProperty(prodSKU, "OP5333");
    }

    public static void main(String[] args) {

        // Create the model
        Challenge1 c1 = new Challenge1();

        // list all statements in the model
        StmtIterator iter = c1.m.listStatements();

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
        c1.m.write(System.out);
    }
}