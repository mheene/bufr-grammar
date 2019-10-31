public class CountReplicationDescriptor extends BUFRBaseListener {
    @Override
    public void enterFixed_replication_descriptor(BUFRParser.Fixed_replication_descriptorContext ctx) {
	System.out.println("Ctx: " + ctx.getText());
    }
}
