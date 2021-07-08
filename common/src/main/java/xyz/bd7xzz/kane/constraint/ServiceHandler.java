package xyz.bd7xzz.kane.constraint;

public interface ServiceHandler {

    /**
     * 执行业务处理
     * @param param 业务参数
     * @param <P>
     */
    <P> void doService(P param);
}
