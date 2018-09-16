package sparkdemo.firstwork;

public class OriginalMessageBean {
    String client_ip;
    String is_blocked;
    String args;
    String status;
    String uid;
    String host;
    String request_timestamp;
    int testnum;

    public OriginalMessageBean(String client_ip, String is_blocked, String args, String status, String uid, String host, String request_timestamp, int testnum) {
        this.client_ip = client_ip;
        this.is_blocked = is_blocked;
        this.args = args;
        this.status = status;
        this.uid = uid;
        this.host = host;
        this.request_timestamp = request_timestamp;
        this.testnum = testnum;
    }

    public String getClient_ip() {
        return client_ip;
    }

    public void setClient_ip(String client_ip) {
        this.client_ip = client_ip;
    }

    public String getIs_blocked() {
        return is_blocked;
    }

    public void setIs_blocked(String is_blocked) {
        this.is_blocked = is_blocked;
    }

    public String getArgs() {
        return args;
    }

    public void setArgs(String args) {
        this.args = args;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getRequest_timestamp() {
        return request_timestamp;
    }

    public void setRequest_timestamp(String request_timestamp) {
        this.request_timestamp = request_timestamp;
    }

    public int getTestnum() {
        return testnum;
    }

    public void setTestnum(int testnum) {
        this.testnum = testnum;
    }
}
