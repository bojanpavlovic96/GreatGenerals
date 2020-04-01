package app.launcher;

public class BrokerInfo {

    public String Password;
    public String Username;
    public String Hostname;

    public String GetUri() {
        return "amqp://" + Username + ":" + Password + "@" + Hostname + "/" + Username;
    }

    public BrokerInfo(String username, String Password, String Hostname) {
        this.Username = username;
        this.Password = Password;
        this.Hostname = Hostname;
    }

    public BrokerInfo(String info) {
        this("", "", "localhost");
        System.out.println(
                "ERROR: Parsing broker info from string not implemented, broker info set to defult localBroker values ... ");
    }

}