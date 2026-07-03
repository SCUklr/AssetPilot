package com.mashu.assetpilot.common;
// 这个类的作用是统一所有返回给前端的 JSON 格式, 这是所有Controller方法的返回类型
//{
//    "code": 200,
//    "msg": "success",
//    "data": {}
//}
// code：业务状态码，前端靠它判断结果
// msg：给人看的提示
// data：真正的数据（用泛型 T，可以是任何类型）
import lombok.Data; // Lombok 自动生成 getter/setter
@Data
public class Result<T> {
    private Integer code;
    private String msg;
    private T data;// 真正的业务数据，用泛型 T 表示可以是任意类型

    // 成功态
    public static<T> Result<T> success(T data) {
        Result<T> result = new Result<T>();
        result.setCode(200); // 成功状态码200
        result.setMsg("success");
        result.setData(data);
        return result;
    }

    // 失败态: 返回特定业务码
    public static<T> Result<T> error(Integer code, String msg) {
        Result<T> result = new Result<T>();
        result.setCode(code);
        result.setMsg(msg);
        // result.setData(null);
        return result;
    }

    // 失败态：快捷版，默认状态为 500
    public static <T> Result<T> error(String msg) {
        return error(500, msg);
        // return error(500, msg);
    }


}
