package dev.ljcaliwan.cmbackend.customer;

import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class CustomerRowMapperTest {

    @Test
    void itShouldMapRow() throws SQLException {
        // Given
        CustomerRowMapper customerRowMapper = new CustomerRowMapper();

        ResultSet resultSet = mock(ResultSet.class);
        given(resultSet.getLong("id")).willReturn(1L);
        given(resultSet.getString("name")).willReturn("John Doe");
        given(resultSet.getString("email")).willReturn("johndoe@gmail.com");
        given(resultSet.getInt("age")).willReturn(18);
        // When
        Customer customer = customerRowMapper.mapRow(resultSet, 1);
        // then
        Customer result = new Customer(1L, "John Doe", "johndoe@gmail.com", 18);

        assertThat(customer).isEqualTo(result);
    }
}