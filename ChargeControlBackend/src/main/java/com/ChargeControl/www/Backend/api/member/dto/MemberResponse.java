package com.ChargeControl.www.Backend.api.member.dto;

import com.ChargeControl.www.Backend.api.member.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberResponse {

    private String name;
    private String carNumber;
    private String email;
    private Role role;
}
