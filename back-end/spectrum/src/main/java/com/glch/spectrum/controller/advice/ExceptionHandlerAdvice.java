package com.glch.spectrum.controller.advice;

import com.glch.spectrum.util.Result;
import com.glch.spectrum.util.ResultCode;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.FileNotFoundException;
import java.util.List;

//import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;


@ControllerAdvice
@ResponseBody
public class ExceptionHandlerAdvice {
    /**
     * 当应用程序抛出 MethodArgumentNotValidException 时，会精确匹配到该方法
     * 在方法里面会获取到校验结果，并将所有校验错误中的第一条返回给前端应用
     * import org.springframework.web.bind.MethodArgumentNotValidException;
     * import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result handleIllegalParamException(MethodArgumentNotValidException e) {
        List<ObjectError> errors = e.getBindingResult().getAllErrors();
        String tips = "参数不合法";
        if (errors.size() > 0) {
            tips = errors.get(0).getDefaultMessage();
        }
        Result result = new Result(ResultCode.PARAM_IS_INVALID);
        result.setMsg(tips);
        return result;
    }
    @ExceptionHandler(FileNotFoundException.class)
    public Result FileNotFoundException(FileNotFoundException e){
        System.out.println(e.getMessage());
        return Result.error(ResultCode.RESULE_DATA_NONE);
    }

}
