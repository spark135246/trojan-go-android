package io.github.trojan_gfw.igniter;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONObject;

public class TrojanConfig implements Parcelable {

    private String localAddr;
    private int localPort;
    private String remoteAddr;
    private int remotePort;
    private String password;
    private boolean verifyCert;
    private String caCertPath;
    private String cipherList;
    private String tls13CipherList;

    private String sni;
    private boolean mux;
    private boolean bypassChina;

    public TrojanConfig() {
        // defaults
        this.localAddr = "127.0.0.1";
        this.localPort = 1081;
        this.remotePort = 443;
        this.verifyCert = true;
        this.cipherList = "ECDHE-ECDSA-AES128-GCM-SHA256:"
                + "ECDHE-RSA-AES128-GCM-SHA256:"
                + "ECDHE-ECDSA-CHACHA20-POLY1305:"
                + "ECDHE-RSA-CHACHA20-POLY1305:"
                + "ECDHE-ECDSA-AES256-GCM-SHA384:"
                + "ECDHE-RSA-AES256-GCM-SHA384:"
                + "ECDHE-ECDSA-AES256-SHA:"
                + "ECDHE-ECDSA-AES128-SHA:"
                + "ECDHE-RSA-AES128-SHA:"
                + "ECDHE-RSA-AES256-SHA:"
                + "DHE-RSA-AES128-SHA:"
                + "DHE-RSA-AES256-SHA:"
                + "AES128-SHA:"
                + "AES256-SHA:"
                + "DES-CBC3-SHA";
        this.tls13CipherList = "TLS_AES_128_GCM_SHA256:"
                + "TLS_CHACHA20_POLY1305_SHA256:"
                + "TLS_AES_256_GCM_SHA384";
    }

    protected TrojanConfig(Parcel in) {
        localAddr = in.readString();
        localPort = in.readInt();
        remoteAddr = in.readString();
        remotePort = in.readInt();
        password = in.readString();
        verifyCert = in.readByte() != 0;
        caCertPath = in.readString();
        cipherList = in.readString();
        tls13CipherList = in.readString();
        sni = in.readString();
        mux = in.readByte() != 0;
        bypassChina = in.readByte() != 0;
    }

    public static final Creator<TrojanConfig> CREATOR = new Creator<TrojanConfig>() {
        @Override
        public TrojanConfig createFromParcel(Parcel in) {
            return new TrojanConfig(in);
        }

        @Override
        public TrojanConfig[] newArray(int size) {
            return new TrojanConfig[size];
        }
    };

    public String generateTrojanConfigJSON() {
        try {
            JSONObject obj = new JSONObject()
                    .put("run_type", "client")
                    .put("local_addr", this.localAddr)
                    .put("local_port", this.localPort)
                    .put("remote_addr", this.remoteAddr)
                    .put("remote_port", this.remotePort)
                    .put("password", new JSONArray().put(password))
                    .put("log_level", 0)
                    .put("ssl", new JSONObject()
                            .put("verify", this.verifyCert)
                            .put("sni", this.sni))
                            .put("cert", this.caCertPath)
                            //.put("cipher", this.cipherList)
                    .put("mux", new JSONObject()
                            .put("enabled", this.mux))
                    .put("bypass_china", this.bypassChina);
            if(this.bypassChina){
                obj.put("router", new JSONObject()
                        .put("enabled", true)
                        .put("geoip", Globals.getGeoIPPath())
                        .put("geosite", Globals.getGeoSitePath())
                        .put("bypass", new JSONArray()
                                .put("geoip:cn")
                                .put("geoip:private")
                                .put("geosite:cn")
                        )
                );
            }
            return obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void fromJSON(String jsonStr) {
        try {
            JSONObject json = new JSONObject(jsonStr);
            this.setLocalAddr(json.getString("local_addr"))
                    .setLocalPort(json.getInt("local_port"))
                    .setRemoteAddr(json.getString("remote_addr"))
                    .setRemotePort(json.getInt("remote_port"))
                    .setPassword(json.getJSONArray("password").getString(0))
                    .setVerifyCert(json.getJSONObject("ssl").getBoolean("verify"))
                    .setSni(json.getJSONObject("ssl").getString("sni"))
                    .setMux(json.getJSONObject("mux").getBoolean("enabled"))
                    .setBypassChina(json.getBoolean("bypass_china"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void copyFrom(TrojanConfig that) {
        this
                .setLocalAddr(that.localAddr)
                .setLocalPort(that.localPort)
                .setRemoteAddr(that.remoteAddr)
                .setRemotePort(that.remotePort)
                .setPassword(that.password)
                .setVerifyCert(that.verifyCert)
                .setCaCertPath(that.caCertPath)
                .setCipherList(that.cipherList)
                .setTls13CipherList(that.tls13CipherList)
                .setSni(that.sni)
                .setMux(that.mux)
                .setBypassChina(that.bypassChina);
    }

    public boolean isValidRunningConfig() {
        return !TextUtils.isEmpty(this.caCertPath)
                && !TextUtils.isEmpty(this.remoteAddr)
                && !TextUtils.isEmpty(this.password);
    }

    public String getLocalAddr() {
        return localAddr;
    }

    public TrojanConfig setLocalAddr(String localAddr) {
        this.localAddr = localAddr;
        return this;
    }

    public int getLocalPort() {
        return localPort;
    }

    public TrojanConfig setLocalPort(int localPort) {
        this.localPort = localPort;
        return this;
    }

    public String getRemoteAddr() {
        return remoteAddr;
    }

    public TrojanConfig setRemoteAddr(String remoteAddr) {
        this.remoteAddr = remoteAddr;
        return this;
    }

    public int getRemotePort() {
        return remotePort;
    }

    public TrojanConfig setRemotePort(int remotePort) {
        this.remotePort = remotePort;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public TrojanConfig setPassword(String password) {
        this.password = password;
        return this;
    }

    public boolean getVerifyCert() {
        return verifyCert;
    }

    public TrojanConfig setVerifyCert(boolean verifyCert) {
        this.verifyCert = verifyCert;
        return this;
    }

    public String getCaCertPath() {
        return caCertPath;
    }

    public TrojanConfig setCaCertPath(String caCertPath) {
        this.caCertPath = caCertPath;
        return this;
    }

    public String getCipherList() {
        return cipherList;
    }

    public TrojanConfig setCipherList(String cipherList) {
        this.cipherList = cipherList;
        return this;
    }

    public String getTls13CipherList() {
        return tls13CipherList;
    }

    public TrojanConfig setTls13CipherList(String tls13CipherList) {
        this.tls13CipherList = tls13CipherList;
        return this;
    }

    public String getSni() { return sni; }

    public TrojanConfig setSni(String sni) {
        this.sni = sni;
        return this;
    }

    public boolean getMux() {
        return mux;
    }

    public TrojanConfig setMux(boolean mux) {
        this.mux = mux;
        return this;
    }

    public boolean getBypassChina() {
        return bypassChina;
    }

    public TrojanConfig setBypassChina(boolean bypassChina) {
        this.bypassChina = bypassChina;
        return this;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof TrojanConfig)) {
            return false;
        }
        TrojanConfig that = (TrojanConfig) obj;
        return (paramEquals(remoteAddr, that.remoteAddr) && paramEquals(remotePort, that.remotePort)
                && paramEquals(localAddr, that.localAddr) && paramEquals(localPort, that.localPort))
                && paramEquals(password, that.password) && paramEquals(verifyCert, that.verifyCert)
                && paramEquals(cipherList, that.cipherList) && paramEquals(tls13CipherList, that.tls13CipherList);
    }

    private static boolean paramEquals(Object a, Object b) {
        if (a == b) {
            return true;
        }
        if (a == null || b == null) {
            return false;
        }
        return a.equals(b);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(localAddr);
        dest.writeInt(localPort);
        dest.writeString(remoteAddr);
        dest.writeInt(remotePort);
        dest.writeString(password);
        dest.writeByte((byte) (verifyCert ? 1 : 0));
        dest.writeString(caCertPath);
        dest.writeString(cipherList);
        dest.writeString(tls13CipherList);
        dest.writeString(sni);
        dest.writeByte((byte) (mux ? 1 : 0));
        dest.writeByte((byte) (bypassChina ? 1 : 0));
    }
}
