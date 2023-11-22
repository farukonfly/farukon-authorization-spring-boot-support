package org.farukon.authorization.jdbc.session.cache;
import java.util.concurrent.TimeUnit;
import org.springframework.session.MapSession;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import org.springframework.stereotype.Repository;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

public class GuavaSessionRepository implements SessionRepository<Session> {
    private final Cache<String, Session> cache;

    public GuavaSessionRepository() {
        this.cache = CacheBuilder.newBuilder()
                .expireAfterAccess(30, TimeUnit.MINUTES) // 设置 session 的过期时间
                .build();
    }

    @Override
    public Session createSession() {
        Session session = new MapSession();
        this.cache.put(session.getId(), session);
        return session;
    }

    @Override
    public void save(Session session) {
        this.cache.put(session.getId(), session);
    }

    @Override
    public Session findById(String id) {
        return this.cache.getIfPresent(id);
    }

    @Override
    public void deleteById(String id) {
        this.cache.invalidate(id);
    }
}
