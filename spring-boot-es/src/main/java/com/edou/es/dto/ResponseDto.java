package com.edou.es.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName ResponseDto
 * @Description TODO
 * @Author 中森明菜
 * @Date 2019/12/27 9:24
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDto<T> {
    private int code;
    private boolean success;
    private T data;

    public static ResponseDto getInstance(int code,boolean success,Object data){
        return new ResponseDto<>(code,success,data);
    }

}
