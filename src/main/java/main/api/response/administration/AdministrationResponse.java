package main.api.response.administration;

public class AdministrationResponse {
    private boolean result;

    public AdministrationResponse(boolean result) {
        this.result = result;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
}
