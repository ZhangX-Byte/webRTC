package signaling.server.event;

import lombok.Data;

/**
 * 注册
 *
 * @author Zhang_Xiang
 * @since 2022/7/13 10:17:40
 */
@Data
public class WebRtcEvent {

    private String from;

    private String to;

    private String type;

    private Object data;

}
