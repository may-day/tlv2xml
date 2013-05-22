package norman.tools.bm.document;

public interface Property
{
    Object getValue();
    boolean isDirty();
    boolean isFromSource ();
}
