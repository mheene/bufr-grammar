import org.antlr.v4.runtime.tree.TerminalNode;

public class CountReplicationDescriptor extends BUFRBaseListener {
    private int counterDescriptors = 0;
    private int numberOfFixedReplications = 0;
    private int numberOfDelayedReplications = 0;

    @Override
    public void enterFixed_replication_descriptor(BUFRParser.Fixed_replication_descriptorContext ctx) {
        String fixReplication = ctx.getText();
        System.out.println("Fixed Replication Ctx: " + fixReplication);
        String[] parts = fixReplication.split(" ");
        // System.out.println("" + parts.length + " x:" + parts[1]);
        System.out.println("Replications: " + Integer.parseInt(parts[1]));
        numberOfFixedReplications = Integer.parseInt(parts[1]);
        // System.out.println("x_all: " + ctx.x_all().toString());
    }

    @Override
    public void enterElement_descriptor(BUFRParser.Element_descriptorContext ctx) {
        System.out.println("Element Descriptor Ctx: " + ctx.getText());
        counterDescriptors++;
        if (numberOfFixedReplications > 0) {
            numberOfFixedReplications--;
        }

        if (numberOfDelayedReplications > 0 ) {
            numberOfDelayedReplications--;
        }
    }

    @Override
    public void enterOperator_descriptor_expr(BUFRParser.Operator_descriptor_exprContext ctx) {
        System.out.println("Operator Descriptor Expr Ctx: " + ctx.getText());
        counterDescriptors++;
    }

    @Override
    public void enterSequence_descriptor(BUFRParser.Sequence_descriptorContext ctx) {
        System.out.println("Sequence Descriptor Ctx: " + ctx.getText());
        counterDescriptors++;
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
    }

    @Override
    public void exitTemplate(BUFRParser.TemplateContext ctx) {
        System.out.println("Number of FixedReplications: " + numberOfFixedReplications);
        System.out.println("Number of DelayedReplications: " + numberOfDelayedReplications);
        System.out.println("Counter of Descriptors: " + counterDescriptors);
    }

    /*
     * @Override public void visitTerminal(TerminalNode node) {
     * System.out.println("TN counter: " + counterDescriptors);
     * System.out.println(node.getText()); }
     */
}
