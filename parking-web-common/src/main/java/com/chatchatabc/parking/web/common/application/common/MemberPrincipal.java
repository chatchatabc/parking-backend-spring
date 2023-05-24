package com.chatchatabc.parking.web.common.application.common;

import java.security.Principal;

public interface MemberPrincipal extends Principal {

    String getMemberUuid();

    static MemberPrincipal of(String memberUuid, String username) {
        return new MemberPrincipal() {
            @Override
            public String getMemberUuid() {
                return memberUuid;
            }

            @Override
            public String getName() {
                return username;
            }
        };
    }
}
