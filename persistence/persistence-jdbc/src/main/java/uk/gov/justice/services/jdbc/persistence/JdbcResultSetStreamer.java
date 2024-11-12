package uk.gov.justice.services.jdbc.persistence;

import static java.lang.Long.MAX_VALUE;
import static java.util.Spliterator.ORDERED;
import static java.util.stream.StreamSupport.stream;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Provides methods for returning result sets as streams
 * WARNING: the returned stream needs to be closed,
 * preferably in a try-with-resource otherwise,
 * connections, statement, etc are left open
 */
public class JdbcResultSetStreamer {

    public <T> Stream<T> streamOf(final PreparedStatementWrapper psWrapper, final Function<ResultSet, T> resultSetToEntityMapper) throws SQLException {

        // In order to stream data, set fetchSize on statement before querying
        psWrapper.setFetchSize();
        final ResultSet resultSet = psWrapper.executeQuery();

        return internalStreamOf(psWrapper, resultSet, resultSetToEntityMapper);
    }

    private <T> Stream<T> internalStreamOf(final PreparedStatementWrapper psWrapper, final ResultSet resultSet, final Function<ResultSet, T> resultSetToEntityMapper) {
        return spliteratorStreamOf(psWrapper, resultSet, e -> {
            try {
                return resultSetToEntityMapper.apply(e);
            } catch (final Exception ex) {
                throw handled(ex, psWrapper);
            }
        });

    }

    private  <U> Stream<U> spliteratorStreamOf(final PreparedStatementWrapper psWrapper, final ResultSet resultSet, final Function<ResultSet, U> resultSetToEntityMapper) {

        final Spliterators.AbstractSpliterator<U> spliterator = new Spliterators.AbstractSpliterator<U>(MAX_VALUE, ORDERED) {
            @Override
            public boolean tryAdvance(final Consumer<? super U> action) {
                try {
                    if (!resultSet.next()) {
                        return false;
                    }
                    action.accept(resultSetToEntityMapper.apply(resultSet));
                    return true;
                } catch (final SQLException ex) {
                    throw handled(ex, psWrapper);
                }
            }
        };
        return stream(spliterator, false).onClose(psWrapper::close);
    }

    private JdbcRepositoryException handled(final Exception ex, final PreparedStatementWrapper psWrapper) {
        psWrapper.close();
        return new JdbcRepositoryException(ex);
    }
}
