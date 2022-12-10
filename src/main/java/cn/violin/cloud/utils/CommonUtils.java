package cn.violin.cloud.utils;

import java.util.stream.Stream;

public class CommonUtils {

    public static String createRequestParameters(Parameter parameter) {
        Class<?> clazz = parameter.getClass();
        StringBuilder requestParameter = new StringBuilder();
        Stream.of(clazz.getDeclaredFields()).forEach(field -> {
            field.setAccessible(true);
            try {
                Object fieldValue = field.get(parameter);
                if (fieldValue != null) {
                    if (requestParameter.length() == 0) {
                        requestParameter.append(field.getName()).append("=").append(fieldValue);
                    } else {
                        requestParameter.append("&").append(field.getName()).append("=").append(fieldValue);
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        return requestParameter.toString();
    }
}
