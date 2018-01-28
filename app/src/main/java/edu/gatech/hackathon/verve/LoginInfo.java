package edu.gatech.hackathon.verve;

/**
 * Created by adhir on 1/27/2018.
 */

class LoginInfo {

    private String name;
    private String phoneId;
    private String loc;
    private String token;

    public LoginInfo(String name, String phoneId, String token, String loc) {
        this.name = name;
        this.phoneId = phoneId;
        this.token = token;
        this.loc = loc;
    }
}
