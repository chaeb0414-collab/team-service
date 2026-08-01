package com.example.team_service.common;

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ssm.SsmClient;
import software.amazon.awssdk.services.ssm.model.GetParametersByPathRequest;
import software.amazon.awssdk.services.ssm.model.GetParametersByPathResponse;
import software.amazon.awssdk.services.ssm.model.Parameter;
import software.amazon.awssdk.services.ssm.paginators.GetParametersByPathIterable;

/**
 * EC2(prod) 기동 시 AWS Parameter Store 값을 읽어와 System Property로 주입한다.
 *
 * application-prod.yml의 ${DB_URL}, ${DB_USERNAME}, ${DB_PASSWORD} 플레이스홀더와
 * application.yml의 ${TEAM_NAME} 플레이스홀더가 이 값들을 사용하게 된다.
 *
 * 로컬 개발 환경(H2)에서는 PARAMETER_STORE_ENABLED 환경변수가 없으므로 아무 동작도 하지 않는다.
 * (즉, AWS 자격 증명이 없는 로컬 PC에서도 애플리케이션이 정상적으로 기동된다.)
 *
 * Parameter Store에 아래 경로로 파라미터가 등록되어 있어야 한다.
 *   /team-service/db/url
 *   /team-service/db/username
 *   /team-service/db/password
 *   /team-service/team-name
 */
public final class ParameterStoreLoader {

    private static final String DEFAULT_PATH = "/team-service/";
    private static final String DEFAULT_REGION = "ap-northeast-2";

    private ParameterStoreLoader() {
    }

    public static void loadIfEnabled() {
        boolean enabled = Boolean.parseBoolean(System.getenv("PARAMETER_STORE_ENABLED"));

        if (!enabled) {
            return;
        }

        String path = System.getenv().getOrDefault("PARAMETER_STORE_PATH", DEFAULT_PATH);
        String region = System.getenv().getOrDefault("AWS_REGION", DEFAULT_REGION);

        try (SsmClient ssmClient = SsmClient.builder()
                .region(Region.of(region))
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build()) {

            GetParametersByPathRequest request = GetParametersByPathRequest.builder()
                    .path(path)
                    .withDecryption(true)
                    .recursive(true)
                    .build();

            GetParametersByPathIterable pages = ssmClient.getParametersByPathPaginator(request);

            for (GetParametersByPathResponse page : pages) {
                for (Parameter parameter : page.parameters()) {
                    applyParameter(path, parameter);
                }
            }

            System.out.println("[ParameterStore] " + path + " 하위 파라미터를 불러왔습니다.");

        } catch (Exception e) {
            System.err.println("[ParameterStore] 파라미터를 불러오지 못했습니다: " + e.getMessage());
        }
    }

    private static void applyParameter(String basePath, Parameter parameter) {
        String key = parameter.name().substring(basePath.length());

        String systemPropertyKey = switch (key) {
            case "db/url" -> "DB_URL";
            case "db/username" -> "DB_USERNAME";
            case "db/password" -> "DB_PASSWORD";
            case "team-name" -> "TEAM_NAME";
            default -> null;
        };

        if (systemPropertyKey != null) {
            System.setProperty(systemPropertyKey, parameter.value());
        }
    }
}
