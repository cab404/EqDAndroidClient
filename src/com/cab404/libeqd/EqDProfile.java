package com.cab404.libeqd;
import com.cab404.moonlight.framework.AccessProfile;
/**
 * Equestria Daily AccessProfile
 * <p/>
 * Created at 10:38 on 18-11-2014
 *
 * @author cab404
 */
public class EqDProfile extends AccessProfile {
    public static final String HOST = "www.equestriadaily.com";

    public EqDProfile() {
        super(HOST, 80);
    }
}
