docker run -itd --name keycloak \
    --restart=unless-stopped \
    -p 9090:8080 \
    -e KEYCLOAK_ADMIN=admin \
    -e KEYCLOAK_ADMIN_PASSWORD=admin \
    quay.io/keycloak/keycloak:25.0.2 start-dev
