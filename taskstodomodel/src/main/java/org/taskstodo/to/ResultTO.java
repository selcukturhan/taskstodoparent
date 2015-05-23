package org.taskstodo.to;

public class ResultTO {
    private String url;
    private String value;
    private int initialPosition;
    private int rerankedPosition;
    private boolean isReranked;


    public ResultTO(final String value, final String url) {
        this.url = url;
        this.value = value;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getInitialPosition() {
        return initialPosition;
    }

    public void setInitialPosition(int initialPosition) {
        this.initialPosition = initialPosition;
    }

    public int getRerankedPosition() {
        return rerankedPosition;
    }

    public void setRerankedPosition(int rerankedPosition) {
        this.rerankedPosition = rerankedPosition;
    }

    public boolean isReranked() {
        return isReranked;
    }

    public void setReranked(boolean isReranked) {
        this.isReranked = isReranked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ResultTO)) return false;

        ResultTO resultTO = (ResultTO) o;

        if (!url.equals(resultTO.url)) return false;
        if (!value.equals(resultTO.value)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = url.hashCode();
        result = 31 * result + value.hashCode();
        return result;
    }
}
