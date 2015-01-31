package taskcontextsearch.rank;

/**
 * Created by regulator on 9/3/14.
 */
public final class QueryContext {
    private final String field;
    private final String text;
    private final float boost;
    private QueryContext parent;

    public QueryContext(final String field, final String text, final float boost) {
        this.field = field;
        this.text = text;
        this.boost = boost;
    }

    public QueryContext(String field, String text, float boost, QueryContext parent) {
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
