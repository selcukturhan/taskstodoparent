package taskcontextsearch.rank;

import com.google.api.services.customsearch.model.Result;
import org.apache.lucene.document.Document;

public class GoogleResultOriginDocument {
    private Result googleResult;
    private Document originDocument;

    public Result getGoogleResult() {
        return googleResult;
    }

    public void setGoogleResult(Result googleResult) {
        this.googleResult = googleResult;
    }

    public Document getOriginDocument() {
        return originDocument;
    }

    public void setOriginDocument(Document originDocument) {
        this.originDocument = originDocument;
    }
}
