package nom.aob.sb.sbbench02.utils;

import nom.aob.sb.sbbench02.model.SimpleResponse;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;

public class Utils {

    private static String HOSTNAME = null;

    private static final Object HOSTNAME_SYNC = new Object();

    private static final Random RANDOM = new Random();

    public static String getHostname() {
        if (HOSTNAME == null) {
            synchronized (HOSTNAME_SYNC) {
                // no need to double check, temporary overwriting will not make any harm..
                HOSTNAME = findHostName();
            }
        }

        return HOSTNAME;
    }

    private static String findHostName() {
        String hostname = null;
        try {
            hostname = InetAddress.getLocalHost().toString();
        } catch (UnknownHostException e) {
//            e.printStackTrace();
            hostname = null;
        }
        return hostname;
    }

    public static String getCurrentTimeString() {
        long millis = System.currentTimeMillis();

        return Long.toString(millis);
    }

    public static int newRandomInt() {
//        return new Random().nextInt(1000000);
        return RANDOM.nextInt();
    }

    public static SimpleResponse newSimpleResponse(String path) {
        return new SimpleResponse(
                getHostname(),
                path,
                getCurrentTimeString(),
                newRandomInt(),
                Thread.currentThread().getId()
        );
    }

    public static <T> String printObject(T t) {
        StringBuilder sb = new StringBuilder();
        sb.append("Printing: " + t.getClass().getName() + ":\n");
        for (Field field : t.getClass().getDeclaredFields()) {
            field.setAccessible(true);

            try {
                sb.append(field.getName()).append(": ").append(field.get(t)).append('\n');
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }

    public static String beanToString(Object bean, boolean showNulls) {
        if (bean == null)
            return null;
        StringBuilder sb = new StringBuilder(bean.getClass().getName())
                .append("[");
        // new ToStringCreator(this)
        try {
            BeanInfo bi = Introspector.getBeanInfo(bean.getClass());
            PropertyDescriptor[] pd = bi.getPropertyDescriptors();
            for (int i = 0; i < pd.length; i++) {
                if (!"class".equals(pd[i].getName())) {
                    Object result = pd[i].getReadMethod().invoke(bean);
                    if (showNulls || result != null) {
                        sb.append(pd[i].getDisplayName()).append("=")
                                .append(result);
                        if (i == pd.length - 1)
                            continue;
                        sb.append(",");
                    }
                }
            }
        } catch (Exception ex) {
        }

        return sb.append("]").toString();
    }


}
