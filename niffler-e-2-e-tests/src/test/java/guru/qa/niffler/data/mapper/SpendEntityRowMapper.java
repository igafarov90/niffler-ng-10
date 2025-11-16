package guru.qa.niffler.data.mapper;

import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.data.entity.SpendEntity;
import guru.qa.niffler.model.CurrencyValues;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SpendEntityRowMapper implements RowMapper<SpendEntity> {
    public static SpendEntityRowMapper instance = new SpendEntityRowMapper();

    public SpendEntityRowMapper() {
    }

    @Override
    public SpendEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        CategoryEntity ce = new CategoryEntity();
        ce.setId(rs.getObject("category_id", UUID.class));
        SpendEntity se = new SpendEntity();
        se.setUsername(rs.getString("username"));
        se.setCurrency(
                CurrencyValues.valueOf(rs.getString("currency")));
        se.setSpendDate(rs.getDate("spendDate"));
        se.setAmount(rs.getDouble("amount"));
        se.setDescription(rs.getString("username"));
        se.setCategory(ce);

        return se;
    }
}
