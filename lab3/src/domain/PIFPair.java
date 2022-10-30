package domain;

public class PIFPair {
    private String token;
    private Pair pair;
    private int code;

    public PIFPair(String token, Pair pair, int code) {
        this.token = token;
        this.pair = pair;
        this.code = code;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Pair getPair() {
        return pair;
    }

    public void setPair(Pair pair) {
        this.pair = pair;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
