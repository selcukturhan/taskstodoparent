package taskcontextsearch.rank;

public final class QueryContext {
    private final String field;
    private final String text;
    private final float boost;
    private QueryContext parent;


    public static QueryContext newInstance(final String field, final String text, final float boost, final QueryContext parent){
        return new QueryContext(field, text, boost, parent);
    }

    private QueryContext(final String field, final String text, final float boost, final QueryContext parent) {
        this.field = field;
        this.text = text;
        this.boost = boost;
        this.parent = parent;
    }

    public String getField() {
        return field;
    }

    public QueryContext getParent() {
        return parent;
    }

    public void setParent(QueryContext parent) {
        this.parent = parent;
    }

    public String getText() {
        return text;
    }

    public float getBoost() {
        return boost;
    }
}
