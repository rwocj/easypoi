package cn.afterturn.easypoi.wps.entity.resreq;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author jueyue on 20-5-8.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WpsRenameRequest extends WpsResponse implements Serializable {

    private String name;
}
