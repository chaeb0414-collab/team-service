package com.example.team_service.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

/**
 * Parameter Store(/team-service/team-name)에서 주입된 team.name 값을
 * /actuator/info 엔드포인트에 노출한다.
 */
@Component
public class TeamInfoContributor implements InfoContributor {

    @Value("${team.name:}")
    private String teamName;

    @Override
    public void contribute(Info.Builder builder) {
        builder.withDetail("team-name", teamName);
    }
}
