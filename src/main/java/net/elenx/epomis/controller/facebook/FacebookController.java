package net.elenx.epomis.controller.facebook;

import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Operations;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

@Log
@Data
@Controller
@RequestMapping("/fb")
class FacebookController {
    private static final MultiValueMap<String, String> ADDITONAL_PARAMETERS = null;
    private static final String REDIRECT_URL = "http://localhost:8080/fb/get-token";
    private final Facebook facebook;
    private final ConnectionRepository connectionRepository;
    private final OAuth2Operations oAuth2Operations;

    @GetMapping
    public String helloFacebook(Model model) {
        if (connectionRepository.findPrimaryConnection(Facebook.class) == null) {
            return "redirect:/connect/facebookConnect";
        }

        log.finer("tak");

        model.addAttribute("facebookProfile", facebook.userOperations().getUserProfile());
        model.addAttribute("feed", facebook.feedOperations().getFeed());

        return "hello";
    }

    @SneakyThrows
    @GetMapping("get-code")
    public void authorizeUrl(HttpServletResponse response) {
        OAuth2Parameters params = new OAuth2Parameters();
        params.setRedirectUri(REDIRECT_URL);
        response.sendRedirect(oAuth2Operations.buildAuthorizeUrl(params));
    }

    @GetMapping("get-token")
    @ResponseBody
    public String fetchLongLiveToken(@RequestParam("code") String code) {
        AccessGrant accessGrant = oAuth2Operations
                .exchangeForAccess(code, REDIRECT_URL, ADDITONAL_PARAMETERS);
        return accessGrant.getAccessToken();
    }

}
