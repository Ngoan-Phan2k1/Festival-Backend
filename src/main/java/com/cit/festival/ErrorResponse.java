package com.cit.festival;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor// XÂY DỰNG HÀM CONSTRUCTOR với 2 tham số message và statuscode;
public class ErrorResponse {
    
    @NonNull
    private String message;

    @Builder.Default
    private List<String> details = new ArrayList<>();

    @NonNull
    private String httpStatusCode;
}
