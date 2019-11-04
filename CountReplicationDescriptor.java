
//import org.antlr.v4.runtime.tree.TerminalNode;
import java.util.ArrayList;
import java.util.Iterator;

import java.util.logging.Level;
import java.util.logging.Logger;

public class CountReplicationDescriptor extends BUFRBaseListener {

    private static Logger log =  Logger.getLogger(CountReplicationDescriptor.class.getName());

    private int counterDescriptors = 0;
    private int numberOfFixedReplications = 0;
    private int numberOfDelayedReplications = 0;

    private ArrayList<ReplicationDescriptor> descriptorList = new ArrayList<ReplicationDescriptor>();

    private void addFixedReplicationDescriptorToList(String name, int replications) {
        Iterator<ReplicationDescriptor> it = descriptorList.iterator();
        while(it.hasNext()) {
            ReplicationDescriptor rd = it.next();
            rd.reduceReplications();
            if (rd.getReplications() == 0) {
                it.remove();
            }
        }
        int size = descriptorList.size();
        ReplicationDescriptor p_descriptor = new ReplicationDescriptor(size, name, "fixed", replications);
        descriptorList.add(p_descriptor);
    }

    private void addDelayedReplicationDescriptorToList(String name, int replications) {
       
        Iterator<ReplicationDescriptor> it = descriptorList.iterator();
        while(it.hasNext()) {
            ReplicationDescriptor rd = it.next();
            rd.reduceReplications(2);
            if (rd.getReplications() == 0) {
                it.remove();
            }
        }

        int size = descriptorList.size();
        ReplicationDescriptor p_descriptor = new ReplicationDescriptor(size, name, "delayed", replications);
        descriptorList.add(p_descriptor);
    }

    private void processDescriptor() {
        Iterator<ReplicationDescriptor> it = descriptorList.iterator();

        while(it.hasNext()) {
            ReplicationDescriptor rd = it.next();
            rd.reduceReplications();
            if (rd.getReplications() == 0) {
                it.remove();
            }
        }
    }

    @Override
    public void enterFixed_replication_descriptor(BUFRParser.Fixed_replication_descriptorContext ctx) {
        String fixReplication = ctx.getText();
        System.out.println("Fixed Replication Ctx: " + fixReplication);
        String[] parts = fixReplication.split(" ");
        // System.out.println("" + parts.length + " x:" + parts[1]);
        System.out.println("Replications: " + Integer.parseInt(parts[1]));
        numberOfFixedReplications = Integer.parseInt(parts[1]);
        addFixedReplicationDescriptorToList(fixReplication, numberOfFixedReplications);
        // System.out.println("x_all: " + ctx.x_all().toString());
    }

    @Override
    public void enterElement_descriptor(BUFRParser.Element_descriptorContext ctx) {
        System.out.println("Element Descriptor Ctx: " + ctx.getText());
        counterDescriptors++;
        if (numberOfFixedReplications > 0) {
            numberOfFixedReplications--;
        }

        if (numberOfDelayedReplications > 0) {
            numberOfDelayedReplications--;
        }
        this.processDescriptor();
    }

    /*
    @Override
    
    public void enterOperator_descriptor_expr(BUFRParser.Operator_descriptor_exprContext ctx) {
        System.out.println("Operator Descriptor Expr Ctx: " + ctx.getText());
        counterDescriptors++;
        // @ToDo: konnte mehr als einer sein
        this.processDescriptor();
    }
    */
    @Override 
    public void enterOperator_descriptor(BUFRParser.Operator_descriptorContext ctx) {
        log.severe("Operator Descriptor Ctx: " + ctx.getText());
        counterDescriptors++;
        this.processDescriptor();
     }

     @Override
     public void enterOperator_236000(BUFRParser.Operator_236000Context ctx) {
        System.out.println("Operator 236000 Descriptor Ctx: " + ctx.getText());
        counterDescriptors++;
        this.processDescriptor();
      }
     
    @Override 
    public void enterAssociated_field_significance(BUFRParser.Associated_field_significanceContext ctx) {
        System.out.println("Associated Field Significance  Ctx: " + ctx.getText());
        counterDescriptors++;
        this.processDescriptor();
     }
     @Override 
     public void enterData_present_indicator(BUFRParser.Data_present_indicatorContext ctx) { 
        System.out.println("Data Present Indicator Ctx: " + ctx.getText());
        counterDescriptors++;
        this.processDescriptor();

     }

     /*
     @Override
     public void enterData_description_operator_qualifier(BUFRParser.Data_description_operator_qualifierContext ctx) {
        System.out.println("Data Description Operator Qualifier Ctx: " + ctx.getText());
        counterDescriptors++;
        this.processDescriptor();
      }
     */

    
    @Override
    public void enterSequence_descriptor(BUFRParser.Sequence_descriptorContext ctx) {
        System.out.println("Sequence Descriptor Ctx: " + ctx.getText());
        counterDescriptors++;
        this.processDescriptor();
    }

    @Override
    public void enterReplication_descriptor(BUFRParser.Replication_descriptorContext ctx) {
        System.out.println("Replication Ctx: " + ctx.getText());
    }

    @Override
    public void enterDelayed_replication_descriptor(BUFRParser.Delayed_replication_descriptorContext ctx) {
        String delayedReplication = ctx.getText();
        System.out.println("Delayed Replication Ctx: " + delayedReplication);
        String[] parts = delayedReplication.split(" ");
        // System.out.println("" + parts.length + " x:" + parts[1]);
        System.out.println("Replications: " + Integer.parseInt(parts[1]));
        numberOfDelayedReplications = Integer.parseInt(parts[1]);
        addDelayedReplicationDescriptorToList(delayedReplication, numberOfDelayedReplications);
    }

    @Override
    public void exitTemplate(BUFRParser.TemplateContext ctx) {
        System.out.println("Number of FixedReplications: " + numberOfFixedReplications);
        System.out.println("Number of DelayedReplications: " + numberOfDelayedReplications);
        System.out.println("Counter of Descriptors: " + counterDescriptors);
        System.out.println("DescriptorList");
        Iterator<ReplicationDescriptor> it = descriptorList.iterator();
        while(it.hasNext()) {
            System.out.println(it.next().toString());
        }
    }

    /*
     * @Override public void visitTerminal(TerminalNode node) {
     * System.out.println("TN counter: " + counterDescriptors);
     * System.out.println(node.getText()); }
     */

    class ReplicationDescriptor {
        private int position, replications;
        private String type, name;

        public ReplicationDescriptor (int position, String name, String type, int replications ) {
            this.position = position;
            this.replications = replications;
            this.name = name;
            this.type = type;
         }

         public int getReplications() {
             return this.replications;
         } 

         public int getPosition() {
             return this.position;
         }

         public String getType(){
             return this.type;
         }

         public String getName() {
             return this.name;
         }

         public void reduceReplications(int count) {
             this.replications = this.replications - count;
         }

         public void reduceReplications() {
             reduceReplications(1);
         }
         @Override
         public String toString() {
             return "Descriptor: Position " + this.position + " Name: " + this.name + " Replications: " + this.replications ; 
         }
    }
}
