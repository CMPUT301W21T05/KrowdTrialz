package com.T05.krowdtrialz;

import com.T05.krowdtrialz.model.user.User;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the User model class.
 *
 * @author Ryan Shukla
 */
public class UserTests {
    private static final String id = "1234";
    private static final String name = "Name";
    private static final String username = "Username";
    private static final String email = "testemail@test.com";

    @Test
    public void testNoArgConstructor(){
        // Just a smoke test
        User user = new User();
    }

    @Test
    public void testIdConstructor(){
        User user = new User(id);
        assertEquals(id, user.getId());
    }

    @Test
    public void testFullConstructor(){
        User user = new User(name, username, email, id);
        assertEquals(id, user.getId());
        assertEquals(name, user.getName());
        assertEquals(username, user.getUserName());
        assertEquals(email, user.getEmail());
    }

    @Test
    public void testSetGetName() {
        User user = new User();
        user.setName(name);
        assertEquals(name, user.getName());
    }

    @Test
    public void testSetGetUsername() {
        User user = new User();
        user.setUserName(username);
        assertEquals(username, user.getUserName());
    }

    @Test
    public void testSetGetEmail() {
        User user = new User();
        user.setEmail(email);
        assertEquals(email, user.getEmail());
    }

    @Test
    public void testEqualsSameId() {
        // Create 2 users with same id but different info
        User user1 = new User("name1", "username1", "email1@email.com", id);
        User user2 = new User("name2", "username2", "email2@email.com", id);

        // Users with same id should be considered equal
        assertEquals(user1, user2);
    }

    @Test
    public void testEqualsDifferentId() {
        // Create 2 users with same id but different info
        User user1 = new User("name1", "username1", "email1@email.com", "1234");
        User user2 = new User("name2", "username2", "email2@email.com", "5678");

        // Users with different ids should not be considered equal
        assertNotEquals(user1, user2);
    }

    @Test
    public void testHashCode() {
        // Create 2 users with same id but different info
        User user1 = new User("name1", "username1", "email1@email.com", id);
        User user2 = new User("name2", "username2", "email2@email.com", id);

        // Users with same id should have same hash
        assertEquals(user1.hashCode(), user2.hashCode());
    }

    /**
     * Test adding user instances to a set to make sure equals() and hashCode() work as expected.
     */
    @Test
    public void testSet() {
        // Create 2 users with same id but different info
        User user1 = new User("name1", "username1", "email1@email.com", "1234");
        User user2 = new User("name2", "username2", "email2@email.com", "1234");

        // Create a third user with a different id
        User user3 = new User("name3", "username3", "email3@email.com", "5678");

        Set<User> set = new HashSet<>();
        set.add(user1);
        set.add(user2);
        set.add(user3);

        // Set should only contain 2 elements since user1 and user2 have the same id
        assertEquals(2, set.size());

        // Set should contain all 3 users since (user1 and user2 are considered the same user)
        assertTrue(set.contains(user1));
        assertTrue(set.contains(user2));
        assertTrue(set.contains(user3));
    }
}
