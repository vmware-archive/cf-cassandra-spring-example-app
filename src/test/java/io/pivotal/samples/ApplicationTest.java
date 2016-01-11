
package io.pivotal.samples;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import io.pivotal.samples.domain.User;
import io.pivotal.samples.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class ApplicationTest {

    @Autowired
    private UserRepository repository;

    User user;

    @Before
    public void setUser() {
        user = new User();
        user.setUsername("foobar");
        user.setLname("foo");
        user.setFname("bar");
        repository.save(user);
    }

    @Test
    public void findSavedUserByUsername() {
        assertThat(repository.findOne(user.getUsername()).getUsername(), is(user.getUsername()));
    }
}
