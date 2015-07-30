package taskcontextsearch.rank;

import com.google.api.services.customsearch.model.Result;
import org.apache.lucene.document.Document;

public class SEResultWithOriginDocument {
    private Result seResult;
    private Document originDocument;

    public Result getSeResult() {
        return seResult;
    }

    public void setSeResult(Result seResult) {
        this.seResult = seResult;
    }

    public Document getOriginDocument() {
        return originDocument;
    }

    public void setOriginDocument(Document originDocument) {
        this.originDocument = originDocument;
    }
}
