package guru.qa.niffler.config;

public interface Config {

    static Config getInstance() {
        return "docker".equals(System.getProperty("test.env"))
                ? DockerConfig.INSTANCE
                : LocalConfig.INSTANCE;
    }

    String frontUrl();

    String authUrl();

    String gatewayUrl();

    String userdataUrl();

    String spendUrl();

    String spendJdbcUrl();

    String githubUrl();

    String authJdbcUrl();

    String userdataJdbcUrl();

    String currencyJdbcUrl();
}
