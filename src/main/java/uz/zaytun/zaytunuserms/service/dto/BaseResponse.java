package uz.zaytun.zaytunuserms.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponse<T> {
    private Boolean success;
    private String message;
    private T data;

    public BaseResponse(T data) {
        this.message = "Success";
        this.success = true;
        this.data = data;
    }

    public BaseResponse(Boolean success, String message) {
        this.success = success;
        this.message = message;
        this.data = null;
    }
}
