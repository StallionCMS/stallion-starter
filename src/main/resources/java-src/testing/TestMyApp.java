package io.stallion.starter.examples.testing;


import io.stallion.testing.MockResponse;
import io.stallion.testing.TestClient;
import io.stallion.users.IUser;
import io.stallion.users.User;
import io.stallion.users.UserController;
import io.stallion.utils.GeneralUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

public class TestMyApp extends io.stallion.starter.examples.testing.BaseIntegrationTest {

    private IUser user;

    @Test
    public void testHelloWorld() {

        TestClient client = new TestClient();
        MockResponse response = client.get("/hello-world");


    }


    @Test
    public void testMyUser() {
        String userEmail = GeneralUtils.randomToken(20) + "@localhost";
        String displayName = GeneralUtils.randomToken(20);

        IUser user = new User()
                .setApproved(true)
                .setRole(Role.MEMBER)
                .setEmail(userEmail)
                .setUsername(userEmail)
                .setDisplayName(displayName);
        this.user = UserController.instance().createUser(user);


        TestClient client = new TestClient().withUser(userEmail);
        MockResponse<User> response = client.get("/api/v1/users/me");
        User userResponse = response.asObject(User.class);

        Assert.assertEquals(user.getEmail(), userResponse.getEmail());
        Assert.assertEquals(displayName, userResponse.getDisplayName());


    }

    @After
    public void cleanup() {
        if (user != null) {
            UserController.instance().hardDelete(user);
            user = null;
        }
    }
}