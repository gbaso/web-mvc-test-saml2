package com.github.gbaso.webmvctestsaml2;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;

class HelloControllerTest {

  @Nested
  @WebMvcTest(
      controllers = HelloController.class,
      properties = {
          "spring.security.saml2.relyingparty.registration.test.entity-id=relyingparty",
          "spring.security.saml2.relyingparty.registration.test.assertingparty.entity-id=assertingparty",
          "spring.security.saml2.relyingparty.registration.test.assertingparty.singlesignon.url=https://example.com",
          "spring.security.saml2.relyingparty.registration.test.assertingparty.singlesignon.sign-request=false"
      }
  )
  // @ImportAutoConfiguration(Saml2RelyingPartyAutoConfiguration.class) // you need to uncomment this for the test to pass
  class Saml2Test {

    @Autowired
    MockMvc mvc;

    @Test
    @WithAnonymousUser
    void loginShouldBeAuthorized() throws Exception {
      mvc
          .perform(get("/hello"))
          .andExpect(status().isFound())
          .andExpect(redirectedUrlPattern("**/saml2/authenticate?registrationId=test"));
    }

  }

  @Nested
  @WebMvcTest(
      controllers = HelloController.class,
      properties = {
          "spring.security.oauth2.client.registration.test.client-id=test",
          "spring.security.oauth2.client.registration.test.authorization-grant-type=authorization-code",
          "spring.security.oauth2.client.provider.test.authorization-uri=https://auth.example.org",
      }
  )
  class OidcTest {

    @Autowired
    MockMvc mvc;

    @Test
    @WithAnonymousUser
    void loginShouldBeAuthorized() throws Exception {
      mvc
          .perform(get("/hello"))
          .andExpect(status().isFound())
          .andExpect(redirectedUrlPattern("**/login"));
    }

  }

}
