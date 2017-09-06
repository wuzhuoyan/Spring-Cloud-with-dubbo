package sample.ds.cn.common;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * Created by wu on 17/8/18.
 */
@Configuration
@ImportResource(locations = {"classpath:dubbo_consumer.xml"})
public class DubboConfigure {
}
