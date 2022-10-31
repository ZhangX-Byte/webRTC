package signaling.server.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Zhang_Xiang
 * @since 2022/7/12 17:09:32
 */
@Data
@ConfigurationProperties(prefix = "netty")
public class NettyConfig {

    private Integer port;
}
