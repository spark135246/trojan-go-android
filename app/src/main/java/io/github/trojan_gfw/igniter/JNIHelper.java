package io.github.trojan_gfw.igniter;

import trojan.Trojan;

public class JNIHelper {
    //HACK
    public static  void trojan(String config) {
        Trojan.runClient(config);
    }

    public static  void stop() {
        Trojan.stopClient();
    }
}
